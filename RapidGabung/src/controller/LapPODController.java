package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import VO.EntryDataShowVO;
import VO.LapPODVO;
import entity.TrKurir;
import entity.TrPerwakilan;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import popup.PopUpLapPODController;
import service.LaporanPODService;
import service.MasterKurirService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.WindowsHelper;
import utilfx.AutoCompleteComboBoxListener;

public class LapPODController implements Initializable {

	@FXML
	private DatePicker dpAwal, dpAkhir;

	@FXML
	private TextField txtCari;

	@FXML
	private ComboBox cbPerwakilan;

	@FXML
	private TableView<LapPODVO> tvLapPOD;

	@FXML
	private TableColumn<LapPODVO, Number> colNo;

	@FXML
	private TableColumn<LapPODVO, String> colPerwakilan;

	@FXML
	private TableColumn<LapPODVO, Number> colJumlahPaket;

	@FXML
	private TableColumn<LapPODVO, Number> colSudahReport;

	@FXML
	private TableColumn<LapPODVO, Number> colBelumReport;

	@FXML
	private TableColumn<LapPODVO, Number> colMasalah;

	@FXML
	private TableColumn<LapPODVO, Number> colStockCabang;

	private ObservableList<LapPODVO> masterData = FXCollections.observableArrayList();

	private String comboPerwakilan;

	@FXML
	RadioButton rbJmlhPaket, rbSudahReport, rbBelumReport, rbMasalah;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;
		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());

		MasterKurirService referensiKurir = new MasterKurirService();
		ObservableList<TrPerwakilan> listNamaKurir = FXCollections
				.observableArrayList(LaporanPODService.getDataPerwakilan());
		for (TrPerwakilan i : listNamaKurir) {
			cbPerwakilan.getItems().add(i.getKodePerwakilan());
		}

		new AutoCompleteComboBoxListener<TrKurir>(cbPerwakilan);

		cbPerwakilan.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				comboPerwakilan = t1;
			}
		});

		ToggleGroup myToggleGroup = new ToggleGroup();
		rbJmlhPaket.setToggleGroup(myToggleGroup);
		rbJmlhPaket.setSelected(true);
		rbSudahReport.setToggleGroup(myToggleGroup);
		rbBelumReport.setToggleGroup(myToggleGroup);
		rbMasalah.setToggleGroup(myToggleGroup);

		tvLapPOD.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() > 1) {
					LapPODVO dataHeader = (LapPODVO) tvLapPOD.getSelectionModel().getSelectedItem();
					if(rbJmlhPaket.isSelected()){
						ShowPopup(dataHeader.getPerwakilan(), "mnJmlhPaket");
					}else if(rbSudahReport.isSelected()){
						ShowPopup(dataHeader.getPerwakilan(), "mnSudahReport");
					}else if(rbBelumReport.isSelected()){
						ShowPopup(dataHeader.getPerwakilan(), "mnBelumReport");
					}else if(rbMasalah.isSelected()){
						ShowPopup(dataHeader.getPerwakilan(), "mnMasalah");
					}
				}
			}
		});
	}

	public void SetTable() {
		List<EntryDataShowVO> tt = LaporanPODService.getListPrintKurIn(comboPerwakilan,
				DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
		Integer no = 1;

		for (EntryDataShowVO t : tt) {
			Integer stockCabang = 0;
			Integer blmReport = 0;
			
			blmReport = t.getJmlhPaket().intValue() - (t.getSudahReport().intValue() + t.getJmlhMasalah().intValue());
			stockCabang = blmReport + t.getJmlhMasalah().intValue();
			masterData.add(new LapPODVO(no++, t.getKdPerwakilan(), t.getJmlhPaket().intValue(),
					t.getSudahReport().intValue(), blmReport, t.getJmlhMasalah().intValue(), stockCabang));
		}
		colNo.setCellValueFactory(cellData -> cellData.getValue().noProperty());
		colPerwakilan.setCellValueFactory(cellData -> cellData.getValue().perwakilanProperty());
		colJumlahPaket.setCellValueFactory(cellData -> cellData.getValue().jumlahPaketProperty());
		colSudahReport.setCellValueFactory(cellData -> cellData.getValue().sudahReportProperty());
		colBelumReport.setCellValueFactory(cellData -> cellData.getValue().belumReportProperty());
		colMasalah.setCellValueFactory(cellData -> cellData.getValue().masalahProperty());
		colStockCabang.setCellValueFactory(cellData -> cellData.getValue().stockCabangProperty());

		FilteredList<LapPODVO> filteredData = new FilteredList<>(masterData, p -> true);

		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getPerwakilan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});

		SortedList<LapPODVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvLapPOD.comparatorProperty());
		tvLapPOD.setItems(sortedData);
	}

	public void ShowPopup(String kdPerwakilan, String Menu) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/popup/PopUpLaporanPOD.fxml"));
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Laporan POD");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(WindowsHelper.primaryStage);
			dialogStage.initStyle(StageStyle.UTILITY);
			dialogStage.setResizable(false);
			Parent root;

			root = (Parent) fxmlLoader.load();

			PopUpLapPODController popUpControl = fxmlLoader.<PopUpLapPODController> getController();
			popUpControl.setFromParent(kdPerwakilan, Menu, DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
					DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
			Scene scene = new Scene(root);
			dialogStage.setScene(scene);
			dialogStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void onClikCari() {
		masterData.clear();
		SetTable();
	}

	@FXML
	public void onClikExcel() {
	}

	@DtoListener(idDtoListener = "backTop")
	public void backDtoListener() {
		try {
			HBox bodyHBox = new HBox();
			FXMLLoader menu = new FXMLLoader(getClass().getResource("/controller/Menu.fxml"));
			ScrollPane menuPage = (ScrollPane) menu.load();
			menuPage.prefWidthProperty().bind(WindowsHelper.primaryStage.widthProperty());
			bodyHBox.getChildren().add(menuPage);
			bodyHBox.setAlignment(Pos.CENTER);

			if (WindowsHelper.rootLayout != null) {
				WindowsHelper.rootLayout.setCenter(bodyHBox);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
