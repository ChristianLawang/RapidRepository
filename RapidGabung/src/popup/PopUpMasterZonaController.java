package popup;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;

import VO.ResiJneRapidVO;
import VO.MasterZonaVO;
import entity.TrPerwakilan;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import service.MasterPerwakilanService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;

public class PopUpMasterZonaController implements Initializable {
	@FXML
	private TableView<MasterZonaVO> lb_header;

	@FXML
	private TextField txtCari;
	@FXML
	private TableColumn<MasterZonaVO, String> colKdZona;
	@FXML
	private TableColumn<MasterZonaVO, String> colPropinsi;
	@FXML
	private TableColumn<MasterZonaVO, String> colKabupaten;
	@FXML
	private TableColumn<MasterZonaVO, String> colKecamatan;
	@FXML
	private TableColumn<MasterZonaVO, String> colPerwakilan;
	@FXML
	private TableColumn<MasterZonaVO, String> colZona;
	@FXML
	private TableColumn<MasterZonaVO, String> colReg;
	@FXML
	private TableColumn<MasterZonaVO, String> colOne;

	private ObservableList<MasterZonaVO> masterData = FXCollections.observableArrayList();

	private String awbTableJne;

	private int selectdIndex1;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ManagedFormHelper.instanceController = this;
		addTableZona();
	}

	// -------------------------------------------------------------------------add
	// Table
	public void addTableZona() {
		List<TrPerwakilan> tt = MasterPerwakilanService.getDataPerwakilan();
		for (TrPerwakilan t : tt) {
			masterData.add(new MasterZonaVO(t.getKodeZona(), t.getKabupaten(), t.getKecamatan(), t.getPropinsi(),
					t.getKodePerwakilan(), t.getZona(), t.getRegperwakilan(), t.getOneperwakilan()));
		}
		// UPDATE COL PERWAKILAN
		colPerwakilan.setCellFactory(TextFieldTableCell.forTableColumn());
		colPerwakilan.setOnEditCommit(new EventHandler<CellEditEvent<MasterZonaVO, String>>() {
			public void handle(CellEditEvent<MasterZonaVO, String> t) {

				((MasterZonaVO) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setPerwakilan(t.getNewValue());
				MasterZonaVO mas = (t.getTableView().getItems().get(t.getTablePosition().getRow()));
				MasterPerwakilanService.updateMaserPerwakilan("KODE_PERWAKILAN", t.getNewValue(), mas.getKdZona());
			}
		});

		// UPDATE COL REG
		colReg.setCellFactory(TextFieldTableCell.forTableColumn());
		colReg.setOnEditCommit(new EventHandler<CellEditEvent<MasterZonaVO, String>>() {
			public void handle(CellEditEvent<MasterZonaVO, String> t) {

				((MasterZonaVO) t.getTableView().getItems().get(t.getTablePosition().getRow())).setReg(t.getNewValue());
				MasterZonaVO mas = (t.getTableView().getItems().get(t.getTablePosition().getRow()));
				MasterPerwakilanService.updateMaserPerwakilan("REGPERWAKILAN", t.getNewValue(), mas.getKdZona());
			}
		});

		// UPDATE COL ONE
		colOne.setCellFactory(TextFieldTableCell.forTableColumn());
		colOne.setOnEditCommit(new EventHandler<CellEditEvent<MasterZonaVO, String>>() {
			public void handle(CellEditEvent<MasterZonaVO, String> t) {

				((MasterZonaVO) t.getTableView().getItems().get(t.getTablePosition().getRow())).setOne(t.getNewValue());
				MasterZonaVO mas = (t.getTableView().getItems().get(t.getTablePosition().getRow()));
				MasterPerwakilanService.updateMaserPerwakilan("ONEPERWAKILAN", t.getNewValue(), mas.getKdZona());
			}
		});

		// btnNew.setOnAction(btnNewHandler);
		colKdZona.setCellValueFactory(cellData -> cellData.getValue().kdZonaProperty());
		colPropinsi.setCellValueFactory(cellData -> cellData.getValue().propinsiProperty());
		colKabupaten.setCellValueFactory(cellData -> cellData.getValue().kabupatenProperty());
		colKecamatan.setCellValueFactory(cellData -> cellData.getValue().kecamatanProperty());
		colPerwakilan.setCellValueFactory(cellData -> cellData.getValue().perwakilanProperty());
		colZona.setCellValueFactory(cellData -> cellData.getValue().zonaProperty());
		colReg.setCellValueFactory(cellData -> cellData.getValue().regProperty());
		colOne.setCellValueFactory(cellData -> cellData.getValue().oneProperty());
		FilteredList<MasterZonaVO> filteredData = new FilteredList<>(masterData, p -> true);

		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getKdZona().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getPropinsi().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getKabupaten().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getKecamatan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getPerwakilan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getZona().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getReg().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});
		SortedList<MasterZonaVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(lb_header.comparatorProperty());
		lb_header.setItems(sortedData);
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
