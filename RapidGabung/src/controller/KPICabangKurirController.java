package controller;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import VO.LaporanKurirVO;
import entity.TrPerwakilan;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import service.KPICabangKurirService;
import service.MasterPerwakilanService;
import util.DateUtil;
import util.DtoListener;
import util.ExportToExcell;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;

public class KPICabangKurirController implements Initializable {
	
	@FXML
	private DatePicker dpAwal, dpAkhir;
	
	@FXML
	private CheckBox chkAllPerwakilan;
	
	@FXML
	private ComboBox cmbPerwakilan;

	@FXML
	private TableView<LapKurirCabangTV> tvLapKurirCabang;
	
	@FXML
	private TextField txtCari, txtAu, txtBa, txtNth, txtCoda, txtMiss, txtCnee, txtJne, txtTotal;
	
	@FXML
	private TableColumn<LapKurirCabangTV, String> colTanggal, colKdPerwakilan, colPersen;
	
	@FXML
	private TableColumn<LapKurirCabangTV, Number> colKirim, colTerima, colSisa;
	
	private ObservableList<LaporanKurirVO> masterHeader = FXCollections.observableArrayList();
	private ObservableList<LaporanKurirVO> masterDetail = FXCollections.observableArrayList();
	private ObservableList<LaporanKurirVO> masterDetail2 = FXCollections.observableArrayList();
	private ObservableList<LaporanKurirVO> masterFooter = FXCollections.observableArrayList();
	
	private ObservableList<LapKurirCabangTV> masterData = FXCollections.observableArrayList();
	
	private DecimalFormat f = new DecimalFormat("##.0");
	
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		 //Set Objek kelas ini
        ManagedFormHelper.instanceController = this;
        dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());
		
		cmbPerwakilan.getItems().add(" - ");
		List<TrPerwakilan> lstPerwakilan = MasterPerwakilanService.getAllPerwakilanCabangDistinct();
		for (TrPerwakilan trPerwakilan : lstPerwakilan) {
			cmbPerwakilan.getItems().add(trPerwakilan.getKodePerwakilan());
		}
		cmbPerwakilan.setValue(" - ");
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
	
	@FXML
	public void onClikCari() throws NullPointerException{
		masterHeader.clear();
		masterDetail.clear();
		masterFooter.clear();
		
		masterData.clear();
		
		if (cmbPerwakilan.getSelectionModel().getSelectedItem().toString().equals(" - ")) {
			MessageBox.alert("Pilih Kode Perwakilan Terlebih Dahulu!");
			return;
		}
		
		//Start set Header
		int counterKirim = 0;
		int counterTerima = 0;
				
		int sisa = 0;
		double persen = 0;
						
		Date dKirim;
		Date dTerima;
						
		BigInteger hKirim = null;
		BigInteger hTerima = null;
						
		for (LocalDate start = dpAwal.getValue(); start.isBefore(dpAkhir.getValue().plusDays(1)); start = start.plusDays(1)) {
			List<Map> resultHeaderKirim = KPICabangKurirService.getLaporanHeaderKirimByParam(DateUtil.convertToDatabaseColumn(start)
				, DateUtil.convertToDatabaseColumn(start), (String) cmbPerwakilan.getSelectionModel().getSelectedItem().toString());
							
			List<Map> resultHeaderTerima = KPICabangKurirService.getLaporanHeaderTerimaByParam(DateUtil.convertToDatabaseColumn(start)
				, DateUtil.convertToDatabaseColumn(start), (String) cmbPerwakilan.getSelectionModel().getSelectedItem().toString());
						
			for (Map objKirim : resultHeaderKirim) {
				counterKirim++;
				dKirim = (Date) objKirim.get("TGL");
				hKirim = (BigInteger) objKirim.get("KIRIM");
				
				for (Map objTerima : resultHeaderTerima) {
					counterTerima++;
					dTerima = (Date) objTerima.get("TGL");
					hTerima = (BigInteger) objTerima.get("TERIMA");
							
					sisa = hKirim.intValueExact() - hTerima.intValueExact();
					persen = (double) Math.round(hTerima.intValueExact() * 100)/hKirim.intValueExact();

					if (counterKirim == 1 && counterTerima == 1) {
						LaporanKurirVO voHeader = new LaporanKurirVO(
							DateUtil.getStdDateDisplayDash(dKirim),
							String.valueOf(Integer.valueOf(hKirim.intValueExact())),
							String.valueOf(Integer.valueOf(hTerima.intValueExact())),
							String.valueOf(Integer.valueOf(sisa)),
							f.format(persen));
						masterHeader.add(voHeader);
						counterKirim = 0;
						counterTerima = 0;
					}
					LapKurirCabangTV tv = new LapKurirCabangTV(DateUtil.getStdDateDisplayDash(dKirim),
							(String) objTerima.get("KODE_PERWAKILAN"),
							hKirim.intValueExact(),
							hTerima.intValueExact(),
							sisa,
							f.format(persen));
					masterData.add(tv);
				}
			}
		}
						
		//End set Header
		
		colTanggal.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
		colKdPerwakilan.setCellValueFactory(cellData -> cellData.getValue().getKdPerwakilanProperty());
		colKirim.setCellValueFactory(cellData -> cellData.getValue().getKirimProperty());
		colTerima.setCellValueFactory(cellData -> cellData.getValue().getTerimaProperty());
		colSisa.setCellValueFactory(cellData -> cellData.getValue().getSisaProperty());
		colPersen.setCellValueFactory(cellData -> cellData.getValue().getPersenProperty());
		
		FilteredList<LapKurirCabangTV> filteredData = new FilteredList<>(masterData, p -> true);
		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {			
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
		
				String lowerCaseFilter = newValue.toLowerCase();
		
				if(data.getDate().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getKdPerwakilan().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getPersen().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}
				return false;
			});
		});
		
		SortedList<LapKurirCabangTV> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvLapKurirCabang.comparatorProperty());
		tvLapKurirCabang.setItems(sortedData);
		
		//Start set Footer
		
		String status = "";
		
		BigInteger total;
		BigInteger tAu = null;
		BigInteger tBa = null;
		BigInteger tNth = null;
		BigInteger tCoda = null;
		BigInteger tMr = null;
		BigInteger tCnee = null;
		BigInteger tJne = null;
		
		Integer tJml = 0;
		
		Integer tJmlAu = 0;
		Integer tJmlBa = 0;
		Integer tJmlNth = 0;
		Integer tJmlCoda = 0;
		Integer tJmlMr = 0;
		Integer tJmlCnee = 0;
		Integer tJmlJne = 0;
		
		for (LocalDate start = dpAwal.getValue(); start.isBefore(dpAkhir.getValue().plusDays(1)); start = start.plusDays(1)) {
			List<Map> resultFooter = KPICabangKurirService.getLaporanFooterByParam(DateUtil.convertToDatabaseColumn(start)
					, DateUtil.convertToDatabaseColumn(start), (String) cmbPerwakilan.getSelectionModel().getSelectedItem().toString());
			
			int counter = 0;
			for (Map obj : resultFooter) {
				counter++;
				status = (String) obj.get("STATUS");
				total = (BigInteger) obj.get("JML");
				tJml += total.intValueExact();
				if (status.equals("AU")) {
					tAu = (BigInteger) obj.get("JML");
					tJmlAu += tAu.intValueExact();
				} else if (status.equals("BA")) {
					tBa = (BigInteger) obj.get("JML");
					tJmlBa += tBa.intValueExact();
				} else if (status.equals("NTH")) {
					tNth = (BigInteger) obj.get("JML");
					tJmlNth += tNth.intValueExact();
				} else if (status.equals("CODA")) {
					tCoda = (BigInteger) obj.get("JML");
					tJmlCoda += tCoda.intValueExact();
				} else if (status.equals("CC")) {
					tCnee = (BigInteger) obj.get("JML");
					tJmlCnee += tCnee.intValueExact();
				} else if (status.equals("MR")) {
					tMr = (BigInteger) obj.get("JML");
					tJmlMr += tMr.intValueExact();
//				} else if (status.equals("JNE")) {
//					tJne = (BigInteger) obj.get("JML");
//					tJmlJne += tJne.intValueExact();
				}
				
				//Tambah counter jadi == 7 klo jne sudah bisa dipakai
				if (counter == 6) {
					LaporanKurirVO voFooter = new LaporanKurirVO(
						String.valueOf(tAu != null ? tAu.intValueExact() : 0),
						String.valueOf(tBa != null ? tBa.intValueExact() : 0),
						String.valueOf(tCoda != null ? tCoda.intValueExact() : 0),
						String.valueOf(tNth != null ? tNth.intValueExact() : 0),
						String.valueOf(tCnee != null ? tCnee.intValueExact() : 0),
						String.valueOf(tMr != null ? tMr.intValueExact() : 0),
						String.valueOf(0),
						String.valueOf(tAu.intValueExact() + tBa.intValueExact() + tCoda.intValueExact() + tNth.intValueExact() + tCnee.intValueExact() + tMr.intValueExact() + 0));
					masterFooter.add(voFooter);
					counter = 0;
				}
			}
			
			System.out.println("Date : " + start);
		}
		
		//End set Footer
		
		txtAu.setText(String.valueOf(tJmlAu));
		txtBa.setText(String.valueOf(tJmlBa));
		txtNth.setText(String.valueOf(tJmlNth));
		txtCoda.setText(String.valueOf(tJmlCoda));
		txtCnee.setText(String.valueOf(tJmlCnee));
		txtMiss.setText(String.valueOf(tJmlMr));
		txtJne.setText(String.valueOf(tJmlJne));
		txtTotal.setText(String.valueOf(tJml));
	}
	
	@FXML
	public void onClikExcel(){
		try {
			Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
			Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
			
			String dateFile = DateUtil.getDateNotSeparator(dateAwl)+" sd "+DateUtil.getDateNotSeparator(dateAkh).substring(4);
			String title = "KPI_Kurir_Per_Cabang("+cmbPerwakilan.getSelectionModel().getSelectedItem().toString()+")";
			
//			setDataHeader();
			setDataDetail();
//			setDataFooter();
			System.out.println("masterHeader size : " + masterHeader.size());
			System.out.println("masterDetail size : " + masterDetail.size());
			System.out.println("masterDetail2 size : " + masterDetail2.size());
			System.out.println("masterFooter size : " + masterFooter.size());
			ExportToExcell.exportToReportKurir(masterHeader, masterDetail, masterDetail2, masterFooter, title , dateFile);
			
			MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORT/"
					+dateFile
					+ " " + title + ".xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setDataHeader() {
		//format voHeader (Tanggal, Kirim, Terima, Sisa, Percentage) order by date
//		LaporanKurirVO rowH1 = new LaporanKurirVO("21-11-2016", "517", "477", "40", "92.3");
//		LaporanKurirVO rowH2 = new LaporanKurirVO("22-11-2016", "0", "0", "0", "0");
//		LaporanKurirVO rowH3 = new LaporanKurirVO("23-11-2016", "400", "380", "20", "95.0");
//		LaporanKurirVO rowH4 = new LaporanKurirVO("24-11-2016", "469", "429", "40", "91.5");
//		LaporanKurirVO rowH5 = new LaporanKurirVO("25-11-2016", "407", "399", "8", "98.0");
//		LaporanKurirVO rowH6 = new LaporanKurirVO("26-11-2016", "422", "403", "19", "95.5");
//		LaporanKurirVO rowH7 = new LaporanKurirVO("27-11-2016", "358", "311", "47", "86.9");
//		LaporanKurirVO rowH8 = new LaporanKurirVO("28-11-2016", "295", "273", "22", "92.5");
//		LaporanKurirVO rowH9 = new LaporanKurirVO("29-11-2016", "0", "0", "0", "0");
//		LaporanKurirVO rowH10 = new LaporanKurirVO("30-11-2016", "476", "412", "64", "86.6");
//		masterHeader.add(rowH1);
//		masterHeader.add(rowH2);
//		masterHeader.add(rowH3);
//		masterHeader.add(rowH4);
//		masterHeader.add(rowH5);
//		masterHeader.add(rowH6);
//		masterHeader.add(rowH7);
//		masterHeader.add(rowH8);
//		masterHeader.add(rowH9);
//		masterHeader.add(rowH10);
	}
	
	private void setDataDetail() {
		masterDetail.clear();
		
		BigDecimal dKirim = null;
		BigDecimal dTerima = null;
		
		BigDecimal dTotalKirim = null;
		BigDecimal dTotalTerima = null;
		
		int sisa = 0;
		double persen = 0;
		
		List<String> kurirName = KPICabangKurirService.getListKurir(cmbPerwakilan.getSelectionModel().getSelectedItem().toString());
		for (String name : kurirName) {
			for (LocalDate start = dpAwal.getValue(); start.isBefore(dpAkhir.getValue().plusDays(1)); start = start.plusDays(1)) {
				List<Map> resultDetail = KPICabangKurirService.getLaporanDetailByParam(DateUtil.convertToDatabaseColumn(start), DateUtil.convertToDatabaseColumn(start),
						(String) cmbPerwakilan.getSelectionModel().getSelectedItem().toString(), name);
				
				for (Map obj : resultDetail) {
					dKirim = (BigDecimal) obj.get("PAKET_KIRIM");
					dTerima = (BigDecimal) obj.get("PAKET_TERIMA");
					sisa = dKirim.intValueExact() - dTerima.intValueExact();
					
					List<Map> resultDetailTotal = KPICabangKurirService.getLaporanDetailByParam(DateUtil.convertToDatabaseColumn(dpAwal.getValue())
							, DateUtil.convertToDatabaseColumn(dpAkhir.getValue()), (String) cmbPerwakilan.getSelectionModel().getSelectedItem().toString(), name);
					
					for (Map objTotal : resultDetailTotal) {
						dTotalKirim = (BigDecimal) objTotal.get("PAKET_KIRIM");
						dTotalTerima = (BigDecimal) objTotal.get("PAKET_TERIMA");

						if (dTotalKirim.intValueExact() > 0) {
							persen = (double) Math.round(dTotalTerima.intValueExact() * 100)/dTotalKirim.intValueExact();
						} else {
							persen = 0;
						}
					}
					
					LaporanKurirVO voDetail = new LaporanKurirVO(
						name,
						String.valueOf(Integer.valueOf(dKirim != null ? dKirim.intValueExact() : 0)),
						String.valueOf(Integer.valueOf(dTerima != null ? dTerima.intValueExact() : 0)),
						String.valueOf(Integer.valueOf(sisa)),
						String.valueOf(Integer.valueOf(dTotalTerima != null ? dTotalTerima.intValueExact() : 0)),
						f.format(persen));
					masterDetail.add(voDetail);	
				}
			}
		}
		
		List<Map> resultDetailPersen = KPICabangKurirService.getLaporanDetailPersenByParam(DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()), (String) cmbPerwakilan.getSelectionModel().getSelectedItem().toString());
		
		BigDecimal dPersen = null;
		
		for (Map objPersen : resultDetailPersen) {
			dPersen = (BigDecimal) objPersen.get("PERSEN");
			LaporanKurirVO voDetailPersen = new LaporanKurirVO(
					(String) objPersen.get("NAMA"),
					String.valueOf(Integer.valueOf(dPersen != null ? dPersen.intValueExact() : 0)));
			masterDetail2.add(voDetailPersen);
		}
	}
	
//	private void setDataDetail() {
		//format voDetail (Nama, Kirim, Terima, Sisa, Total) Order by Name
		//Abdul1
//		LaporanKurirVO rowD1 = new LaporanKurirVO("Abdul", "23", "23", "0", "464");
//		LaporanKurirVO rowD2 = new LaporanKurirVO("Abdul", "0", "0", "0", "464");
//		LaporanKurirVO rowD3 = new LaporanKurirVO("Rochim", "56", "56", "0", "516");
//		LaporanKurirVO rowD4 = new LaporanKurirVO("Rochim", "0", "0", "0", "167");
//		LaporanKurirVO rowD5 = new LaporanKurirVO("Rochim", "133", "132", "1", "718");
//		LaporanKurirVO rowD6 = new LaporanKurirVO("Marcel", "56", "55", "1", "567");
//		LaporanKurirVO rowD7 = new LaporanKurirVO("Deni", "25", "24", "1", "479");
//		LaporanKurirVO rowD8 = new LaporanKurirVO("Ara", "39", "39", "0", "410");
//		LaporanKurirVO rowD9 = new LaporanKurirVO("Danang", "55", "51", "4", "536");
//		LaporanKurirVO rowD10 = new LaporanKurirVO("Dedi", "79", "64", "15", "243");
//		LaporanKurirVO rowD11 = new LaporanKurirVO("Ichwan", "44", "28", "16", "409");
//		LaporanKurirVO rowD12 = new LaporanKurirVO("Eman", "7", "5", "2", "167");
//		LaporanKurirVO rowD13 = new LaporanKurirVO("Boy", "7", "5", "2", "167");
		
		//Popo
//		LaporanKurirVO rowD1 = new LaporanKurirVO("Abdul", "23", "23", "0", "464");
//		LaporanKurirVO rowD2 = new LaporanKurirVO("Popo", "56", "56", "0", "516");
//		LaporanKurirVO rowD3 = new LaporanKurirVO("Rochim", "133", "132", "1", "718");
//		LaporanKurirVO rowD4 = new LaporanKurirVO("Marcel", "56", "55", "1", "567");
//		LaporanKurirVO rowD5 = new LaporanKurirVO("Deni", "25", "24", "1", "479");
//		LaporanKurirVO rowD6 = new LaporanKurirVO("Ara", "39", "39", "0", "410");
//		LaporanKurirVO rowD7 = new LaporanKurirVO("Danang", "55", "51", "4", "536");
//		LaporanKurirVO rowD8 = new LaporanKurirVO("Dedi", "79", "64", "15", "243");
//		LaporanKurirVO rowD9 = new LaporanKurirVO("Ichwan", "44", "28", "16", "409");
//		LaporanKurirVO rowD10 = new LaporanKurirVO("Eman", "7", "5", "2", "167");
//				
//		masterDetail.add(rowD1);
//		masterDetail.add(rowD2);
//		masterDetail.add(rowD3);
//		masterDetail.add(rowD4);
//		masterDetail.add(rowD5);
//		masterDetail.add(rowD6);
//		masterDetail.add(rowD7);
//		masterDetail.add(rowD8);
//		masterDetail.add(rowD9);
//		masterDetail.add(rowD10);
//		masterDetail.add(rowD11);
//		masterDetail.add(rowD12);
//		masterDetail.add(rowD13);
//	}
	
	private void setDataFooter() {
		//format voFooter (Au, Ba, Coda, nth, cnee, miss_route, jne, total) Order by date
		LaporanKurirVO rowF1 = new LaporanKurirVO("100", "100", "0", "0", "100", "100", "0", "100");
		LaporanKurirVO rowF2 = new LaporanKurirVO("0", "100", "100", "100", "0", "100", "100", "0");
		
		masterFooter.add(rowF1);
		masterFooter.add(rowF2);
	}
	
	public class LapKurirCabangTV {
		private StringProperty date;
		private StringProperty kdPerwakilan;
		private IntegerProperty kirim;
		private IntegerProperty terima;
		private IntegerProperty sisa;
		private StringProperty persen;
		
		public LapKurirCabangTV(String date, String kdPerwakilan, Integer kirim, Integer terima, Integer sisa, String persen) {
			this.date = new SimpleStringProperty(date);
			this.kdPerwakilan = new SimpleStringProperty(kdPerwakilan);
			this.kirim = new SimpleIntegerProperty(kirim);
			this.terima = new SimpleIntegerProperty(terima);
			this.sisa = new SimpleIntegerProperty(sisa);
			this.persen = new SimpleStringProperty(persen);
		}
		
		public String getDate() {
			return date.get();
		}

		public void setDate(String date) {
			this.date.set(date);
		}
		
		public StringProperty getDateProperty(){
			return date;
		}
		
		public String getKdPerwakilan() {
			return kdPerwakilan.get();
		}

		public void setKdPerwakilan(String kdPerwakilan) {
			this.kdPerwakilan.set(kdPerwakilan);
		}
		
		public StringProperty getKdPerwakilanProperty(){
			return kdPerwakilan;
		}
		
		public Integer getKirim() {
			return kirim.get();
		}

		public void setKirim(Integer kirim) {
			this.kirim.set(kirim);
		}
		
		public IntegerProperty getKirimProperty(){
			return kirim;
		}
		
		public Integer getTerima() {
			return terima.get();
		}

		public void setTerima(Integer terima) {
			this.terima.set(terima);
		}
		
		public IntegerProperty getTerimaProperty(){
			return terima;
		}
		
		public Integer getSisa() {
			return sisa.get();
		}

		public void setSisa(Integer sisa) {
			this.sisa.set(sisa);
		}
		
		public IntegerProperty getSisaProperty(){
			return sisa;
		}
		
		public String getPersen() {
			return persen.get();
		}

		public void setPersen(String persen) {
			this.persen.set(persen);
		}
		
		public StringProperty getPersenProperty(){
			return persen;
		}
		
	}
}