package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;

import VO.EntryDataShowVO;
import VO.PenandaLunasVO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import service.PenandaLunasService;
import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.WindowsHelper;
import util.formatRupiah;



public class PenandaLunasController implements Initializable {

	@FXML
	private DatePicker dpAwal, dpAkhir;
	
	@FXML
	private TextField txtCari;

	@FXML
	private TableView<PenandaLunasVO> tvPenandaLunas;

	@FXML
	private TableColumn<PenandaLunasVO, Number> colNo;

	@FXML
	private TableColumn<PenandaLunasVO, String> colTgl;
	
	@FXML
	private TableColumn<PenandaLunasVO, String> colKdPickup;

	@FXML
	private TableColumn<PenandaLunasVO, String> colKdpelanggan;

	@FXML
	private TableColumn<PenandaLunasVO, Number> colJmlBarang;

	@FXML
	private TableColumn<PenandaLunasVO, Number> colTotalBerat;
	
	@FXML
	private TableColumn<PenandaLunasVO, String> colTotalTagihan;

	private ObservableList<PenandaLunasVO> DataPickup = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<PenandaLunasVO, Boolean> colCheklist;
	
	private CheckBox cbAll = new CheckBox();

	private int selectdIndex;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;
		
		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());
		
		cbAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
	           public void changed(ObservableValue<? extends Boolean> ov,
	             Boolean old_val, Boolean new_val) {
	             if(cbAll.isSelected()){
	            	 DataPickup.clear();
	            	 showTable(true);
	             }else{
	            	 DataPickup.clear();
	            	 showTable(false);
	             }
	          }
	        });
		
	}
	
	public void onClikLunas() {
		for (PenandaLunasVO p : tvPenandaLunas.getItems()) {
			if(p.getChecked()){
				System.out.println("No Pick Up : "+p.getKdPickup());
				PenandaLunasService.updateLunas(p.getKdPickup());
				DataPickup.clear();
				showTable(false);
			}
		}
	}
	
	public void onClikCari() {
		DataPickup.clear();
		showTable(false);
	}

	private void showTable(boolean chkAll) {
		List<EntryDataShowVO> listEntry = PenandaLunasService.getDataPickup(DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
		Integer no=1;
		for (EntryDataShowVO t : listEntry) {
			DataPickup.add(new PenandaLunasVO(no++, 
					String.valueOf(DateUtil.dateToStdDateLiteral(t.getCreated())), 
					t.getKdPickup(),
					t.getKdPelanggan(), 
					t.getJmlhBarang().intValue(),
					t.getSumBeratAsli().intValue(),
					String.valueOf(formatRupiah.formatIndonesia(t.gettBiaya().intValue())),
					chkAll));
		}
		
		
		colNo.setCellValueFactory(cellData -> cellData.getValue().noProperty());
		colTgl.setCellValueFactory(cellData -> cellData.getValue().createdProperty());
		colKdPickup.setCellValueFactory(cellData -> cellData.getValue().kdPickupProperty());
		colKdpelanggan.setCellValueFactory(cellData -> cellData.getValue().kdPelangganProperty());
		colJmlBarang.setCellValueFactory(cellData -> cellData.getValue().jumlahBarangProperty());
		colTotalBerat.setCellValueFactory(cellData -> cellData.getValue().totalBeratProperty());
		colTotalTagihan.setCellValueFactory(cellData -> cellData.getValue().totalTagihanProperty());
		colCheklist.setCellValueFactory(cellData -> cellData.getValue().checkedProperty());
		
//		colCheklist.setCellValueFactory(new PropertyValueFactory<Person, Boolean>("vegetarian"));
		colCheklist.setCellFactory(CheckBoxTableCell.forTableColumn(colCheklist));
		colCheklist.setGraphic(cbAll);
		colCheklist.setEditable(true);
		
		FilteredList<PenandaLunasVO> filteredData = new FilteredList<>(DataPickup, p -> true);

		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getCreated().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getKdPickup().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (data.getKdPelanggan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});
		
		SortedList<PenandaLunasVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvPenandaLunas.comparatorProperty());
		tvPenandaLunas.setItems(sortedData);

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
