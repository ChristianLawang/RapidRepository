package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import service.LaporanDiskonService;
import util.DateUtil;
import util.DtoListener;
import util.ExportToExcell;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;

public class LaporanDiskonController implements Initializable {
	
	@FXML
	private DatePicker dpAwal, dpAkhir;
	
	@FXML
	private ComboBox cmbKdPelanggan;
	
	@FXML
	private TableView<LapDiskonTV> tvLaporanDiskon;
	
	@FXML
	private TextField txtCari;
	
	@FXML
	private TableColumn<LapDiskonTV, String> 
		colNamaPelanggan,
		colDiskonRapid,
		colDiskonJNE,
		colTglDiskon;
	
	@FXML
	private TableColumn<LapDiskonTV, Number> 
		colNo;
	
	private ObservableList<LapDiskonTV> masterData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ManagedFormHelper.instanceController = this;
		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());
		
		this.populateComboPelanggan();
	}
	
	private void populateComboPelanggan() {
		cmbKdPelanggan.getItems().add("All Pelanggan");
		List<String> result = LaporanDiskonService.getNamaPelanggan();
		for (String obj : result) {
			cmbKdPelanggan.getItems().add((String) obj);
		}
		
		cmbKdPelanggan.setValue("All Pelanggan");
	}

	@FXML
	public void onClikCari() {
		masterData.clear();		
		SetTable();
	}
	
	//FA
	public void SetTable() {
		List<Map> result = LaporanDiskonService.getLaporanByParam(DateUtil.convertToDatabaseColumn(dpAwal.getValue()), 
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()), (String) cmbKdPelanggan.getSelectionModel().getSelectedItem().toString());
		
		Integer no = 0;
		BigInteger awb;
		Double berat;
		Double beratAsli;
		BigDecimal harga;
		BigDecimal diskon;
		BigDecimal hargaStlhDiskon;
		for (Map obj : result) {
			no++;
			awb = (BigInteger) obj.get("AWB");
			berat = (Double) obj.get("BERAT");
			beratAsli = (Double) obj.get("BERAT_ASLI");
			harga = (BigDecimal) obj.get("HARGA_AWAL") != null ? (BigDecimal) obj.get("HARGA_AWAL") : BigDecimal.ZERO;
			diskon = (BigDecimal) obj.get("DISKON") != null ? (BigDecimal) obj.get("DISKON") : BigDecimal.ZERO;
			hargaStlhDiskon = (BigDecimal) obj.get("HARGA_SETELAH_DISKON") != null ? (BigDecimal) obj.get("HARGA_SETELAH_DISKON") : BigDecimal.ZERO;
			LapDiskonTV row = new LapDiskonTV(
					no,
					(String) obj.get("KODE_PELANGGAN"),
//					(String) obj.get("DISKON_RAPID"),
					new Integer((Integer) obj.get("DISKON_RAPID")).toString(),
					new Integer((Integer) obj.get("DISKON_JNE")).toString(),
					DateUtil.getStdDateDisplay((Date) obj.get("TGL_MULAI_DISKON")));
			masterData.add(row);
		}
		
		colNo.setCellValueFactory(cellData -> cellData.getValue().getNoProperty());
		colNamaPelanggan.setCellValueFactory(cellData -> cellData.getValue().getNamaPelangganProperty());
		colDiskonRapid.setCellValueFactory(cellData -> cellData.getValue().getDiskonRapidProperty());
		colDiskonJNE.setCellValueFactory(cellData -> cellData.getValue().getDiskonJNEProperty());
		colTglDiskon.setCellValueFactory(cellData -> cellData.getValue().getTglDiskonProperty());
		
		FilteredList<LapDiskonTV> filteredData = new FilteredList<>(masterData, p -> true);
		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {			
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
		
				String lowerCaseFilter = newValue.toLowerCase();
		
				if(data.getNamaPelanggan().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getDiskonRapid().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getDiskonJNE().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getTglDiskon().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}
				return false;
			});
		});
		
		SortedList<LapDiskonTV> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvLaporanDiskon.comparatorProperty());
		tvLaporanDiskon.setItems(sortedData);
	}

	@FXML
	public void onClikExcel() {
		try {
			Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
			Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
			
			String dateFile = DateUtil.getDateNotSeparator(dateAwl)+" sd "+DateUtil.getDateNotSeparator(dateAkh).substring(4);
			
			ExportToExcell.exportToExcellReportDiskon(masterData, "Laporan_Diskon",
					dateFile);
			MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORT/"
					+dateFile
					+" Laporan_Diskon.xls");
		} catch (Exception e) {
			MessageBox.alert(e.getMessage());
		}
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
	
	public class LapDiskonTV {
		private IntegerProperty no;
		private StringProperty namaPelanggan;
		private StringProperty diskonRapid;
		private StringProperty diskonJNE;
		private StringProperty tglDiskon;
		
		public LapDiskonTV(Integer no, String namaPelanggan, String diskonRapid, String diskonJNE, String tglDiskon) {
			this.no = new SimpleIntegerProperty(no);
			this.namaPelanggan = new SimpleStringProperty(namaPelanggan);
			this.diskonRapid = new SimpleStringProperty(diskonRapid);
			this.diskonJNE = new SimpleStringProperty(diskonJNE);
			this.tglDiskon = new SimpleStringProperty(tglDiskon);
		}
		
		public Integer getNo() {
			return no.get();
		}

		public void setNo(Integer no) {
			this.no.set(no);
		}
		
		public IntegerProperty getNoProperty(){
			return no;
		}
		
		public String getNamaPelanggan() {
			return namaPelanggan.get();
		}

		public void setNamaPelanggan(String namaPelanggan) {
			this.namaPelanggan.set(namaPelanggan);
		}
		
		public StringProperty getNamaPelangganProperty(){
			return namaPelanggan;
		}
		
		public String getDiskonRapid() {
			return diskonRapid.get();
		}

		public void setDiskonRapid(String diskonRapid) {
			this.diskonRapid.set(diskonRapid);
		}
		
		public StringProperty getDiskonRapidProperty(){
			return diskonRapid;
		}
		
		public String getDiskonJNE() {
			return diskonJNE.get();
		}

		public void setDiskonJNE(String diskonJNE) {
			this.diskonJNE.set(diskonJNE);
		}
		
		public StringProperty getDiskonJNEProperty(){
			return diskonJNE;
		}
		
		public String getTglDiskon() {
			return tglDiskon.get();
		}

		public void setTglDiskon(String tglDiskon) {
			this.tglDiskon.set(tglDiskon);
		}
		
		public StringProperty getTglDiskonProperty(){
			return tglDiskon;
		}
	}

}