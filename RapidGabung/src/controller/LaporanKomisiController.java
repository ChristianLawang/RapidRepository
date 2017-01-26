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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import service.LaporanKomisiService;
import util.DateUtil;
import util.DtoListener;
import util.ExportToExcell;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import util.formatRupiah;

public class LaporanKomisiController implements Initializable {
	
	@FXML
	private DatePicker dpAwal, dpAkhir;
	
	@FXML
	private ComboBox cmbKdSales;
	
	@FXML
	private TableView<LapKomisiTV> tvLaporanKomisi;
	
	@FXML
	private TextField txtCari, txtAwb, txtBerat, txtBeratAsli, txtTotal, txtDiskon, txtHargaStlhDiskon;
	
	@FXML
	private Label lblNamaSales, lblKomisi;
	
	@FXML
	private TableColumn<LapKomisiTV, String> 
		colNamaSales,
		colNamaPelanggan,
		colAwb,
		colBerat,
		colBeratAsli,
		colTotal,
		colDiskon,
		colHargaSetelahDiskon,
		colStatusPembayaran;
	
	@FXML
	private TableColumn<LapKomisiTV, Number> colNo;
	
	private ObservableList<LapKomisiTV> masterData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ManagedFormHelper.instanceController = this;
		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());
		
		this.populateComboSales();
	}
	
	private void populateComboSales() {
		cmbKdSales.getItems().add("All Sales");
		List<String> result = LaporanKomisiService.getNamaSales();
		for (String obj : result) {
			cmbKdSales.getItems().add((String) obj);
		}
		
		cmbKdSales.setValue("All Sales");
	}

	@FXML
	public void onClikCari() {
		masterData.clear();		
		SetTable();
	}
	
	//FA
	public void SetTable() {
		List<Map> result = LaporanKomisiService.getLaporanByParam(DateUtil.convertToDatabaseColumn(dpAwal.getValue()), 
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()), (String) cmbKdSales.getSelectionModel().getSelectedItem().toString());
		
		Integer no = 0;
		BigInteger awb;
		Double berat;
		Double beratAsli;
		BigDecimal harga;
		BigDecimal diskon;
		BigDecimal hargaStlhDiskon;
		
		long total = 0;
		Integer tAwb = 0;
		Integer tBerat = 0;
		double tBeratAsli = 0;
		long tHarga = 0;
		Integer tDiskon = 0;
		
		for (Map obj : result) {
			no++;
			awb = (BigInteger) obj.get("AWB");
			berat = (Double) obj.get("BERAT");
			beratAsli = (Double) obj.get("BERAT_ASLI");
			harga = (BigDecimal) obj.get("HARGA_AWAL") != null ? (BigDecimal) obj.get("HARGA_AWAL") : BigDecimal.ZERO;
			diskon = (BigDecimal) obj.get("DISKON") != null ? (BigDecimal) obj.get("DISKON") : BigDecimal.ZERO;
			hargaStlhDiskon = (BigDecimal) obj.get("HARGA_SETELAH_DISKON") != null ? (BigDecimal) obj.get("HARGA_SETELAH_DISKON") : BigDecimal.ZERO;
			LapKomisiTV row = new LapKomisiTV(
					no,
					(String) obj.get("NAMA_SALES"),
					(String) obj.get("PENGIRIM"),
					String.valueOf(awb.intValueExact()),
					String.valueOf(berat.intValue()),
					String.valueOf(beratAsli),
					String.valueOf(harga.intValue()),
					String.valueOf(diskon.intValueExact()),
					String.valueOf(hargaStlhDiskon.intValueExact()),
					(String) obj.get("STATUS_PEMBAYARAN"));
			tAwb += awb.intValueExact();
			tBerat += berat.intValue();
//			tBeratAsli += Integer.valueOf(String.valueOf(beratAsli));
			tBeratAsli += beratAsli;
			tHarga += Long.parseLong(String.valueOf(harga.intValue()));
			tDiskon += diskon.intValueExact();
			total += Long.parseLong(String.valueOf(hargaStlhDiskon.intValueExact()));
			masterData.add(row);
		}
		
		System.out.println("total : " + total);
		
		//Hitung Komisi
		setKomisi(cmbKdSales.getSelectionModel().getSelectedItem().toString(), total);
		
		//set Total Value
		txtAwb.setText(String.valueOf(tAwb));
		txtBerat.setText(String.valueOf(tBerat));
		txtBeratAsli.setText(String.valueOf(Math.round(tBeratAsli * 100.0)/100.0));
		txtTotal.setText(formatRupiah.formatIndonesiaTanpaKoma(String.valueOf(tHarga)));
		txtDiskon.setText(formatRupiah.formatIndonesia(tDiskon));
		txtHargaStlhDiskon.setText(formatRupiah.formatIndonesiaTanpaKoma(String.valueOf(total)));
		
//		Long.v
		
		colNo.setCellValueFactory(cellData -> cellData.getValue().getNoProperty());
		colNamaSales.setCellValueFactory(cellData -> cellData.getValue().getNamaSalesProperty());
		colNamaPelanggan.setCellValueFactory(cellData -> cellData.getValue().getNamaPengirimProperty());
		colAwb.setCellValueFactory(cellData -> cellData.getValue().getAwbProperty());
		colBerat.setCellValueFactory(cellData -> cellData.getValue().getBeratProperty());
		colBeratAsli.setCellValueFactory(cellData -> cellData.getValue().getBeratAsliProperty());
		colTotal.setCellValueFactory(cellData -> cellData.getValue().getHargaProperty());
		colDiskon.setCellValueFactory(cellData -> cellData.getValue().getDiskonProperty());
		colHargaSetelahDiskon.setCellValueFactory(cellData -> cellData.getValue().getHargaStlhDiskonProperty());
		colStatusPembayaran.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
		
		FilteredList<LapKomisiTV> filteredData = new FilteredList<>(masterData, p -> true);
		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {			
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
		
				String lowerCaseFilter = newValue.toLowerCase();
		
				if(data.getNamaPengirim().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getAwb().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getBerat().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getBeratAsli().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getHarga().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getDiskon().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getHargaStlhDiskon().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getStatus().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}
				return false;
			});
		});
		
		SortedList<LapKomisiTV> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvLaporanKomisi.comparatorProperty());
		tvLaporanKomisi.setItems(sortedData);
	}
	
	private void setKomisi(String namaSales, long total) {
		lblNamaSales.setText("");
		lblKomisi.setText("");
		if (!namaSales.equals("All Sales")) {
			lblNamaSales.setText(cmbKdSales.getSelectionModel().getSelectedItem().toString());
			
			String nama = null;
			String jabatan1 = null;
			String jabatan2 = null;
			List<Map> results = LaporanKomisiService.getSalesData(namaSales);
			for (Map map : results) {
				nama = (String) map.get("NAMA_SALES");
				jabatan1 = (String) map.get("JABATAN1");
				jabatan2 = (String) map.get("JABATAN2");
			}
			
			System.out.println("nama : " + nama);
			System.out.println("jabatan1 : " + jabatan1);
			System.out.println("jabatan2 : " + jabatan2);
			
			long komisi = 0;
			if (jabatan1.equals("Sales")) {
				if (jabatan2.equals("Referensi")) {
//					komisi = (0.0025 * total);
					komisi = (long)(0.0025 * total);
					lblKomisi.setText(formatRupiah.formatIndonesia(Integer.valueOf(String.valueOf(komisi))));
					System.out.println("Komisi jabatan1 sales dan jabatan2 Referensi : " + komisi);
				} else {
					komisi = (long)(0.0075 * total);
					lblKomisi.setText(String.valueOf(komisi));
					System.out.println("Komisi jabatan1 sales dan jabatan2 Referensi : " + komisi);
				}
			} else if (jabatan1.equals("Agen")) {
				if (total < 100000000) {
					if (jabatan2.equals("Sales")) {
						komisi = (long)(0.0125 * total);
						lblKomisi.setText(formatRupiah.formatIndonesia(Integer.valueOf(String.valueOf(komisi))));
						System.out.println("Komisi jabatan1 agen dan jabatan2 sales dibawah 100.000.000 : " + komisi);
					} else {
						komisi = (long)(0.002 * total);
						lblKomisi.setText(String.valueOf(komisi));
						System.out.println("Komisi jabatan1 agen dan jabatan2 null dibawah 100.000.000 : " + komisi);
					}
					
				} else if (total >= 100000000 && total < 300000000) {
					if (jabatan2.equals("Sales")) {
						komisi = (long)(0.0225 * total);
						lblKomisi.setText(formatRupiah.formatIndonesia(Integer.valueOf(String.valueOf(komisi))));
						System.out.println("Komisi jabatan1 agen dan jabatan2 sales diantara 100 - 300 : " + komisi);
					} else {
						komisi = (long)(0.003 * total);
						lblKomisi.setText(String.valueOf(komisi));
						System.out.println("Komisi jabatan1 agen dan jabatan2 null diantara 100 - 300 : " + komisi);
					}
					
				} else if (total > 300000000){
					if (jabatan2.equals("Sales")) {
						komisi = (long)(0.0425 * total);
						lblKomisi.setText(formatRupiah.formatIndonesia(Integer.valueOf(String.valueOf(komisi))));
						System.out.println("Komisi jabatan1 agen dan jabatan2 sales diatas 300.000.000 : " + komisi);
					} else {
						komisi = (long)(0.005 * total);
						lblKomisi.setText(formatRupiah.formatIndonesia(Integer.valueOf(String.valueOf(komisi))));
						System.out.println("Komisi jabatan1 agen dan jabatan2 null diatas 300.000.000 : " + komisi);
					}
					
				}
			} else if (jabatan1.equals("Referensi")) {
				komisi = (long)(0.05 * total);
				lblKomisi.setText(formatRupiah.formatIndonesia(Integer.valueOf(String.valueOf(komisi))));
				System.out.println("Komisi jabatan1 Referensi : " + komisi);
			}
			
		}
	}

	@FXML
	public void onClikExcel() {

			Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
			Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
			
			String dateFile = DateUtil.getDateNotSeparator(dateAwl)+" sd "+DateUtil.getDateNotSeparator(dateAkh).substring(4);
			
			ExportToExcell.exportToExcellReportKomisi(masterData, "Laporan_Komisi",
					dateFile);
			MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORT/"
					+dateFile
					+" Laporan_Komisi.xls");
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
	
	public class LapKomisiTV {
		private IntegerProperty no;
		private StringProperty namaSales;
		private StringProperty namaPengirim;
		private StringProperty awb;
		private StringProperty berat;
		private StringProperty beratAsli;
		private StringProperty harga;
		private StringProperty diskon;
		private StringProperty hargaStlhDiskon;
		private StringProperty status;
		
		public LapKomisiTV(Integer no, String namaSales, String namaPengirim, String awb, String berat, String beratAsli, String harga, String diskon, String hargaStlhDiskon, String status) {
			this.no = new SimpleIntegerProperty(no);
			this.namaSales = new SimpleStringProperty(namaSales);
			this.namaPengirim = new SimpleStringProperty(namaPengirim);
			this.awb = new SimpleStringProperty(awb);
			this.berat = new SimpleStringProperty(berat);
			this.beratAsli = new SimpleStringProperty(beratAsli);
			this.harga = new SimpleStringProperty(harga);
			this.diskon = new SimpleStringProperty(diskon);
			this.hargaStlhDiskon = new SimpleStringProperty(hargaStlhDiskon);
			this.status = new SimpleStringProperty(status);
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
		
		public String getNamaSales() {
			return namaSales.get();
		}

		public void setNamaSales(String namaSales) {
			this.namaSales.set(namaSales);
		}
		
		public StringProperty getNamaSalesProperty(){
			return namaSales;
		}
		
		public String getNamaPengirim() {
			return namaPengirim.get();
		}

		public void setNamaPengirim(String namaSales) {
			this.namaPengirim.set(namaSales);
		}
		
		public StringProperty getNamaPengirimProperty(){
			return namaPengirim;
		}
		
		public String getAwb() {
			return awb.get();
		}

		public void setAwb(String awb) {
			this.awb.set(awb);
		}
		
		public StringProperty getAwbProperty(){
			return awb;
		}
		
		public String getBerat() {
			return berat.get();
		}

		public void setBerat(String berat) {
			this.berat.set(berat);
		}
		
		public StringProperty getBeratProperty(){
			return berat;
		}
		
		public String getBeratAsli() {
			return beratAsli.get();
		}

		public void setBeratAsli(String beratAsli) {
			this.beratAsli.set(beratAsli);
		}
		
		public StringProperty getBeratAsliProperty(){
			return beratAsli;
		}
		
		public String getHarga() {
			return harga.get();
		}

		public void setHarga(String harga) {
			this.harga.set(harga);
		}
		
		public StringProperty getHargaProperty(){
			return harga;
		}
		
		public String getDiskon() {
			return diskon.get();
		}

		public void setDiskon(String diskon) {
			this.diskon.set(diskon);
		}
		
		public StringProperty getDiskonProperty(){
			return diskon;
		}
		
		public String getHargaStlhDiskon() {
			return hargaStlhDiskon.get();
		}

		public void setHargaStlhDiskon(String diskon) {
			this.hargaStlhDiskon.set(diskon);
		}
		
		public StringProperty getHargaStlhDiskonProperty(){
			return hargaStlhDiskon;
		}
		
		public String getStatus() {
			return status.get();
		}

		public void setStatus(String status) {
			this.status.set(status);
		}
		
		public StringProperty getStatusProperty(){
			return status;
		}
	}

}