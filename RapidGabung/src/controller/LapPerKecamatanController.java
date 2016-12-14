package controller;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import entity.TrPerwakilan;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import service.LaporanPerKecamatanService;
import service.MasterPerwakilanService;
import util.DateUtil;
import util.DtoListener;
import util.ExportToExcell;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import util.formatRupiah;

public class LapPerKecamatanController implements Initializable {
	
	@FXML
	private DatePicker dpAwal, dpAkhir;

	@FXML
	private ComboBox cmbKodePerwakilan;

	@FXML
	private RadioButton rbJmlhPaket, rbSudahReport, rbBelumReport, rbMasalah;
	
	@FXML
	private TableView<LapPerKecamatanTV> tvLapPerKecamatan;
	
	@FXML
	private TextField txtCari;
	
	@FXML
	private Label lblPaket, lblBerat, lblHarga;
	
	//FA
	@FXML
	private TableColumn<LapPerKecamatanTV, String> 
		noCol,
		awbCol,
		tglCol,
//		penerimaCol,
		tujuanCol,
		perwakilanCol,
		zonaCol,
		kecamatanCol,
		kabupatenCol,
		propinsiCol,
		layananCol,
		hargaCol;
	
	private ObservableList<LapPerKecamatanTV> masterData = FXCollections.observableArrayList();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;
		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());
		this.populateComboPerwakilan();
//		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
//			
//		});
	}
	
	private void populateComboPerwakilan() {
		cmbKodePerwakilan.getItems().add("All Cabang");
		cmbKodePerwakilan.getItems().add("JNE");		
		List<TrPerwakilan> lstPerwakilan = MasterPerwakilanService.getAllPerwakilanCabangDistinct();
		for (TrPerwakilan trPerwakilan : lstPerwakilan) {
			cmbKodePerwakilan.getItems().add(trPerwakilan.getKodePerwakilan());
		}
		cmbKodePerwakilan.setValue("All Cabang");
	}
	
	//FA
	public class LapPerKecamatanTV{
		private StringProperty no;
		private StringProperty awb;
		private StringProperty tglCreate;
//		private StringProperty penerima;
		private StringProperty tujuan;
		private StringProperty kodePerwakilan;
		private StringProperty zona;
		private StringProperty kecamatan;
		private StringProperty kabupaten;
		private StringProperty propinsi;
		private StringProperty layanan;
		private StringProperty harga;
		
		public LapPerKecamatanTV(
				String no, 
				String awb, 
				String tglCreate, 
//				String penerima, 
				String tujuan, 
				String kodePerwakilan, 
				String zona,
				String kecamatan,
				String kabupaten,
				String propinsi,
				String layanan,
				String harga
			){
			this.no = new SimpleStringProperty(no);
			this.awb = new SimpleStringProperty(awb);
			this.tglCreate = new SimpleStringProperty(tglCreate);
//			this.penerima = new SimpleStringProperty(penerima);
			this.tujuan = new SimpleStringProperty(tujuan);
			this.kodePerwakilan = new SimpleStringProperty(kodePerwakilan);
			this.zona = new SimpleStringProperty(zona);
			this.kecamatan = new SimpleStringProperty(kecamatan);
			this.kabupaten = new SimpleStringProperty(kabupaten);
			this.propinsi = new SimpleStringProperty(propinsi);
			this.layanan = new SimpleStringProperty(layanan);
			this.harga = new SimpleStringProperty(harga);			
		}

		public String getNo() {
			return no.get();
		}

		public void setNo(String no) {
			this.no.set(no);
		}

		public String getAwb() {
			return awb.get();
		}

		public void setAwb(String awb) {
			this.awb.set(awb);
		}

		public String getTglCreate() {
			return tglCreate.get();
		}

		public void setTglCreate(String tglCreate) {
			this.tglCreate.set(tglCreate);
		}

//		public String getPenerima() {
//			return penerima.get();
//		}
//
//		public void setPenerima(String penerima) {
//			this.penerima.set(penerima);
//		}

		public String getTujuan() {
			return tujuan.get();
		}

		public void setTujuan(String tujuan) {
			this.tujuan.set(tujuan);
		}

		public String getKodePerwakilan() {
			return kodePerwakilan.get();
		}

		public void setKodePerwakilan(String kodePerwakilan) {
			this.kodePerwakilan.set(kodePerwakilan);
		}
		
		//FA
		public String getZona() {
			return zona.get();
		}

		//FA
		public void setZona(String zona) {
			this.zona.set(zona);
		}

		public String getKecamatan() {
			return kecamatan.get();
		}

		public void setKecamatan(String kecamatan) {
			this.kecamatan.set(kecamatan);
		}

		public String getKabupaten() {
			return kabupaten.get();
		}

		public void setKabupaten(String kabupaten) {
			this.kabupaten.set(kabupaten);
		}

		public String getPropinsi() {
			return propinsi.get();
		}

		public void setPropinsi(String propinsi) {
			this.propinsi.set(propinsi);
		}

		public String getLayanan() {
			return layanan.get();
		}

		public void setLayanan(String layanan) {
			this.layanan.set(layanan);
		}

		public String getHarga() {
			return harga.get();
		}

		public void setHarga(String harga) {
			this.harga.set(harga);
		}
		
		public StringProperty getNoProperty(){
			return no;
		}
		
		public StringProperty getAwbProperty(){
			return awb;
		}
		
		public StringProperty getTglCreateProperty(){
			return tglCreate;
		}
		
//		public StringProperty getPenerimaProperty(){
//			return penerima;
//		}
		
		public StringProperty getTujuanProperty(){
			return tujuan;
		}
		
		public StringProperty getKodePerwakilanProperty(){
			return kodePerwakilan;
		}
		
		//FA
		public StringProperty getZonaProperty(){
			return zona;
		}
		
		public StringProperty getKecamatanProperty(){
			return kecamatan;
		}
		
		public StringProperty getKabupatenProperty(){
			return kabupaten;
		}
		
		public StringProperty getPropinsiProperty(){
			return propinsi;
		}
		
		public StringProperty getLayananProperty(){
			return layanan;
		}
		
		public StringProperty getHargaProperty(){
			return harga;
		}
	}
	
	//FA
	public void SetTable(){
		List<Map> result = LaporanPerKecamatanService.getLaporanByParam(DateUtil.convertToDatabaseColumn(dpAwal.getValue()), DateUtil.convertToDatabaseColumn(dpAkhir.getValue()), (String) cmbKodePerwakilan.getSelectionModel().getSelectedItem().toString());
		Integer no = 0;
		long harga = 0;
		double berat = 0.00;
		for (Map obj : result) {
			no++;
			LapPerKecamatanTV row = new LapPerKecamatanTV(
					no.toString(),
					(String) obj.get("AWB_DATA_ENTRY"),
					DateUtil.getStdDateDisplay((Date) obj.get("TGL_CREATE")),
//					(String) obj.get("PENERIMA"),
					(String) obj.get("TUJUAN"),
					(String) obj.get("KODE_PERWAKILAN"),
					(String) obj.get("ZONA"),
					(String) obj.get("KECAMATAN"),
					new String((String) obj.get("KABUPATEN")).toUpperCase(),
					(String) obj.get("PROPINSI"),
					(String) obj.get("LAYANAN"),
					new Integer((Integer) obj.get("HARGA")).toString());
//			harga += new Integer((Integer) obj.get("HARGA"));
			harga += Long.parseLong(obj.get("HARGA").toString());
//			berat += Integer.valueOf((String) obj.get("PBCLOSE"));
			berat += Double.valueOf((String)obj.get("PBCLOSE"));
			masterData.add(row);
		}
		
		lblPaket.setText(no.toString());
		lblHarga.setText(String.valueOf(harga));
		lblBerat.setText(String.valueOf(berat));
		
		noCol.setCellValueFactory(cellData -> cellData.getValue().getNoProperty());
		awbCol.setCellValueFactory(cellData -> cellData.getValue().getAwbProperty());
		tglCol.setCellValueFactory(cellData -> cellData.getValue().getTglCreateProperty());
//		penerimaCol.setCellValueFactory(cellData -> cellData.getValue().getPenerimaProperty());
		tujuanCol.setCellValueFactory(cellData -> cellData.getValue().getTujuanProperty());
		perwakilanCol.setCellValueFactory(cellData -> cellData.getValue().getKodePerwakilanProperty());
		zonaCol.setCellValueFactory(cellData -> cellData.getValue().getZonaProperty());
		kecamatanCol.setCellValueFactory(cellData -> cellData.getValue().getKecamatanProperty());
		kabupatenCol.setCellValueFactory(cellData -> cellData.getValue().getKabupatenProperty());
		propinsiCol.setCellValueFactory(cellData -> cellData.getValue().getPropinsiProperty());
		layananCol.setCellValueFactory(cellData -> cellData.getValue().getLayananProperty());
		hargaCol.setCellValueFactory(cellData -> cellData.getValue().getHargaProperty());
		
		FilteredList<LapPerKecamatanTV> filteredData = new FilteredList<>(masterData, p -> true);
		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {			
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
		
				String lowerCaseFilter = newValue.toLowerCase();
		
				if (data.getAwb().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}else if(data.getTglCreate().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
//				}else if(data.getPenerima().toLowerCase().indexOf(lowerCaseFilter) != -1){
//					return true;
				}else if(data.getTujuan().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getKodePerwakilan().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
//				}else if (data.getZona().toLowerCase().indexOf(lowerCaseFilter) != -1){
//					return true;
				}else if(data.getKecamatan().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getKabupaten().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getPropinsi().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getLayanan().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getHarga().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}
				return false;
			});
		});
		
		SortedList<LapPerKecamatanTV> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvLapPerKecamatan.comparatorProperty());
		tvLapPerKecamatan.setItems(sortedData);
	}
	
	@FXML
	public void onClikCari() {
		masterData.clear();		
		SetTable();
	}	

	@FXML
	public void onClikExcel() {
		try {
			Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
			Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
			
			String dateFile = DateUtil.getDateNotSeparator(dateAwl)+" sd "+DateUtil.getDateNotSeparator(dateAkh).substring(4);
			
			ExportToExcell.exportToExcellReportPerKecamatan(masterData, "Report_Laporan_per_kecamatan",
					dateFile);
			MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORT/"
					+dateFile
					+" Report_Lap_Per_Kecamatan.xls");
		} catch (Exception e) {
			MessageBox.alert(e.getMessage());
		}
	}
	
	@FXML
	public void onClikPDF() {
		
	}
	@DtoListener(idDtoListener = "backTop")
    public void backDtoListener()
    {
        try {
            HBox bodyHBox = new HBox();
            FXMLLoader menu = new FXMLLoader(getClass().getResource("/controller/Menu.fxml"));
            ScrollPane menuPage = (ScrollPane) menu.load();
            menuPage.prefWidthProperty().bind(WindowsHelper.primaryStage.widthProperty());       //FullScreen, hilangin klo g mau fullscreen
            bodyHBox.getChildren().add(menuPage);
            bodyHBox.setAlignment(Pos.CENTER);
            
            if(WindowsHelper.rootLayout!=null)
            {
                WindowsHelper.rootLayout.setCenter(bodyHBox);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}