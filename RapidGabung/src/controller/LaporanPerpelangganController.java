package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import VO.EntryDataShowVO;
import VO.ReportVO;
import entity.TrKurir;
import entity.TrPelanggan;
import entity.TrPerwakilan;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import net.sf.jasperreports.engine.JRException;
import service.KurirOutService;
import service.MasterPerwakilanService;
import service.PelangganService;
import service.ReportService;
import util.BigDecimalUtil;
import util.DateUtil;
import util.DtoListener;
import util.ExportJasper;
import util.ExportToExcell;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import util.formatRupiah;
import utilfx.AutoCompleteComboBoxListener;

public class LaporanPerpelangganController implements Initializable {

	@FXML
	private Button btnPrintRResi, btnPrintRTagihan;

	@FXML
	private DatePicker dpAwal, dpAkhir;

	@FXML
	private TextField txtCari, txtTotal, txtAsuransi, txtBiaya, 
	txtTotalDiskonAll, txtHargaSetelahDiskonAll, txtTotalBiayaAll, 
	txtTotalBaAll, txtTotalBeratAll, txtAwbAll;

	@FXML
	private ComboBox cbPelanggan;

	@FXML
	private TableView<ReportVO> tvPelanggan;

	@FXML
	private TableColumn<ReportVO, Number> colNo;

	@FXML
	private TableColumn<ReportVO, String> colResi;

	@FXML
	private TableColumn<ReportVO, String> colTujuan;

	@FXML
	private TableColumn<ReportVO, String> colPenerima;

	@FXML
	private TableColumn<ReportVO, String> colTlp;

	@FXML
	private TableColumn<ReportVO, String> colResiJne;

	@FXML
	private TableColumn<ReportVO, String> colBerat;

	@FXML
	private TableColumn<ReportVO, String> colBiaya;

	@FXML
	private TableColumn<ReportVO, Number> colAsuransi;

	@FXML
	private TableColumn<ReportVO, Number> colDiskon;

	@FXML
	private TableColumn<ReportVO, String> colTotal;

	private ObservableList<ReportVO> masterData = FXCollections.observableArrayList();

	private ObservableList<ReportVO> masterData2 = FXCollections.observableArrayList();

	private String pelanggan = "";

	// ---------------------------------------------------TABLE 2
	@FXML
	private CheckBox chkAllPelanggan;

	@FXML
	private TableView<ReportVO> tvAllPelanggan;

	@FXML
	private TableColumn<ReportVO, Number> colNoAll;

	@FXML
	private TableColumn<ReportVO, String> colNamaSalesAll;

	@FXML
	private TableColumn<ReportVO, String> colPengirimAll;

	@FXML
	private TableColumn<ReportVO, Number> colAwbAll;

	@FXML
	private TableColumn<ReportVO, Number> collBeratAll;

	@FXML
	private TableColumn<ReportVO, String> colBeratAsliAll;

	@FXML
	private TableColumn<ReportVO, String> colTotalBiayaAll;

	@FXML
	private TableColumn<ReportVO, String> colHargaSetelahDiskonAll;

	@FXML
	private TableColumn<ReportVO, String> colTotalDiskonAll;
	
	@FXML
	private ComboBox cmbPerwakilan;

	Boolean Checklist = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;
		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());

		// memanggil untuk combo referensi
		ObservableList<TrPelanggan> listReferensi = FXCollections
				.observableArrayList(PelangganService.getDataReferensi());
		for (TrPelanggan i : listReferensi) {
			cbPelanggan.getItems().add(i.getNamaAkun());
		}

		new AutoCompleteComboBoxListener<TrKurir>(cbPelanggan);

		cbPelanggan.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				pelanggan = t1;
			}
		});
		
		cmbPerwakilan.getItems().add("All Cabang");
		List<TrPerwakilan> lstPerwakilan = MasterPerwakilanService.getAllPerwakilanCabangDistinct();
		for (TrPerwakilan trPerwakilan : lstPerwakilan) {
			cmbPerwakilan.getItems().add(trPerwakilan.getKodePerwakilan());
		}
		cmbPerwakilan.setValue("All Cabang");
		chkAllPelanggan.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					tvPelanggan.setVisible(false);
					txtTotal.setVisible(false);
					txtAsuransi.setVisible(false);
					txtBiaya.setVisible(false);
					cbPelanggan.setDisable(true);
					tvAllPelanggan.setVisible(true);
					Checklist = true;
					txtTotalDiskonAll.setVisible(true);
					txtHargaSetelahDiskonAll.setVisible(true);
					txtTotalBiayaAll.setVisible(true);
					txtTotalBaAll.setVisible(true);
					txtTotalBeratAll.setVisible(true);
					txtAwbAll.setVisible(true);
				} else {
					tvPelanggan.setVisible(true);
					txtTotal.setVisible(true);
					txtAsuransi.setVisible(true);
					txtBiaya.setVisible(true);
					cbPelanggan.setDisable(false);
					tvAllPelanggan.setVisible(false);
					Checklist = false;
					txtTotalDiskonAll.setVisible(false);
					txtHargaSetelahDiskonAll.setVisible(false);
					txtTotalBiayaAll.setVisible(false);
					txtTotalBaAll.setVisible(false);
					txtTotalBeratAll.setVisible(false);
					txtAwbAll.setVisible(false);
				}
			}
		});
	}

	public void SetTable() {
		
		System.out.println("KODE PELANGGAN : "+pelanggan);
		List<String> listPelangganNotExists = ReportService.getIfPelangganExistForLapPerPelanggan(
				DateUtil.convertToDatabaseColumn(dpAwal.getValue()), 
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
		System.out.println("--> listPelangganNotExists : " + listPelangganNotExists.size());
		if(listPelangganNotExists.size()>0){
			String lstPel = "";
			Integer numInc = 0;
			for (String string : listPelangganNotExists) {
				numInc++;
				lstPel+=numInc + ". " + string + System.getProperty("line.separator");
			}
			lstPel = "Load laporan per pelanggan gagal, pelanggan ini tidak terdaftar pada tabel pelanggan : " + System.getProperty("line.separator") + lstPel;
			MessageBox.alertBesar(200, lstPel);
		}else{
			List<EntryDataShowVO> tt = ReportService.dataPerpelanggan(DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
					DateUtil.convertToDatabaseColumn(dpAkhir.getValue()), pelanggan);
			Integer no = 1;
			int totalBiaya = 0;
			int asuransi = 0;
			int harga = 0;
			for (EntryDataShowVO t : tt) {
				totalBiaya += t.gettBiaya().intValueExact();
				asuransi += t.getAsuransi();
				harga += t.getHarga();
				txtTotal.setText(String.valueOf(formatRupiah.formatIndonesia(totalBiaya)));
				txtAsuransi.setText(String.valueOf(formatRupiah.formatIndonesia(asuransi)));
				txtBiaya.setText(String.valueOf(formatRupiah.formatIndonesia(harga)));
				masterData.add(new ReportVO(no++, t.getAwbData(), t.getPengirim(), t.getTujuan(), t.getPenerima(),
						t.getNoTlpn(), t.getResiJne(), t.getbFinal(),
						String.valueOf(formatRupiah.formatIndonesia(t.getHarga())), t.getAsuransi(), t.getDiskon(),
						t.getDiskonJne(), t.getDiskonRapid(), t.getDiskonPelanggan(),
						String.valueOf(formatRupiah.formatIndonesia(t.gettBiaya().intValueExact()))));
			}
			colNo.setCellValueFactory(cellData -> cellData.getValue().noProperty());
			colResi.setCellValueFactory(cellData -> cellData.getValue().awbDataProperty());
			colTujuan.setCellValueFactory(cellData -> cellData.getValue().tujuanProperty());
			colPenerima.setCellValueFactory(cellData -> cellData.getValue().penerimaProperty());
			colTlp.setCellValueFactory(cellData -> cellData.getValue().tlpProperty());
			colResiJne.setCellValueFactory(cellData -> cellData.getValue().resiJNEProperty());
			colBerat.setCellValueFactory(cellData -> cellData.getValue().bFinalProperty());
			colBiaya.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
			colAsuransi.setCellValueFactory(cellData -> cellData.getValue().asuransiProperty());
			colDiskon.setCellValueFactory(cellData -> cellData.getValue().diskonProperty());
			colTotal.setCellValueFactory(cellData -> cellData.getValue().biayaProperty());
	
			FilteredList<ReportVO> filteredData = new FilteredList<>(masterData, p -> true);
	
			txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(data -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
	
					String lowerCaseFilter = newValue.toLowerCase();
	
					if (data.getAwbData().toLowerCase().indexOf(lowerCaseFilter) != -1) {
						return true;
					} else if (data.getTujuan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
						return true;
					} else if (data.getPenerima().toLowerCase().indexOf(lowerCaseFilter) != -1) {
						return true;
					} else if (data.getTujuan().toLowerCase().indexOf(lowerCaseFilter) != -1) {
						return true;
					} else if (data.getResiJNE().toLowerCase().indexOf(lowerCaseFilter) != -1) {
						return true;
					}
					return false;
				});
			});
	
			SortedList<ReportVO> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(tvPelanggan.comparatorProperty());
			tvPelanggan.setItems(sortedData);
		}
	}

	@FXML
	public void onClikCari() {
		String kdPerwakilan = (String) cmbPerwakilan.getSelectionModel().getSelectedItem().toString();
		System.out.println("--> kdPerwakilan : " + kdPerwakilan);
		if(kdPerwakilan.equals("All Cabang")){
			if (Checklist) {
				masterData2.clear();
				SetTableB();
			} else {
				masterData.clear();
				SetTable();
			}
		}else{
			Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
			Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
			
			String kdPelanggan = pelanggan;			
			List<EntryDataShowVO> ed = ReportService.dataPerPelangganNativeSQL(dateAwl, dateAkh, kdPelanggan, kdPerwakilan, Checklist);
			
			
//			txtTotalBaAll, txtTotalBeratAll, txtAwbAll;
			Integer no = 1;
			if (!Checklist) {
				int totalBiaya = 0;
				int asuransi = 0;
				int harga = 0;
				masterData.clear();
				for (EntryDataShowVO t : ed) {	
					totalBiaya += t.gettBiaya().intValueExact();
					asuransi += t.getAsuransi();
					harga += t.getHarga();
					txtTotal.setText(String.valueOf(formatRupiah.formatIndonesia(totalBiaya)));
					txtAsuransi.setText(String.valueOf(formatRupiah.formatIndonesia(asuransi)));
					txtBiaya.setText(String.valueOf(formatRupiah.formatIndonesia(harga)));
					masterData.add(new ReportVO(no++, t.getAwbData(), t.getPengirim(), t.getTujuan(), t.getPenerima(),
							t.getNoTlpn(), t.getResiJne(), t.getbFinal(),
							String.valueOf(formatRupiah.formatIndonesia(t.getHarga())), t.getAsuransi(), t.getDiskon(),
							t.getDiskonJne(), t.getDiskonRapid(), t.getDiskonPelanggan(),
							String.valueOf(formatRupiah.formatIndonesia(t.gettBiaya().intValueExact()))));
				}
					colNo.setCellValueFactory(cellData -> cellData.getValue().noProperty());
					colResi.setCellValueFactory(cellData -> cellData.getValue().awbDataProperty());
					colTujuan.setCellValueFactory(cellData -> cellData.getValue().tujuanProperty());
					colPenerima.setCellValueFactory(cellData -> cellData.getValue().penerimaProperty());
					colTlp.setCellValueFactory(cellData -> cellData.getValue().tlpProperty());
					colResiJne.setCellValueFactory(cellData -> cellData.getValue().resiJNEProperty());
					colBerat.setCellValueFactory(cellData -> cellData.getValue().bFinalProperty());
					colBiaya.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
					colAsuransi.setCellValueFactory(cellData -> cellData.getValue().asuransiProperty());
					colDiskon.setCellValueFactory(cellData -> cellData.getValue().diskonProperty());
					colTotal.setCellValueFactory(cellData -> cellData.getValue().biayaProperty());
					FilteredList<ReportVO> filteredData = new FilteredList<>(masterData, p -> true);
	
					txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
						filteredData.setPredicate(data -> {
							if (newValue == null || newValue.isEmpty()) {
								return true;
							}
	
							String lowerCaseFilter = newValue.toLowerCase();
	
							if (data.getNmSales().toLowerCase().indexOf(lowerCaseFilter) != -1) {
								return true;
							} else if (data.getPengirim().toLowerCase().indexOf(lowerCaseFilter) != -1) {
								return true;
							}
							return false;
						});
					});
	
					SortedList<ReportVO> sortedData = new SortedList<>(filteredData);
					sortedData.comparatorProperty().bind(tvPelanggan.comparatorProperty());
					tvPelanggan.setItems(sortedData);
			}else{
				masterData2.clear();
				int totalBiaya = 0;
				int totalHargaSetelahDiskon = 0;
				int totalDiskon = 0;
				double totalBa = 0.00;
				int totalBerat = 0;
				int totalawb = 0;
				System.out.println("--> ed.size() + " + ed.size());
				for (EntryDataShowVO t : ed) {
					totalBiaya += t.gettBiaya().intValueExact();
					totalDiskon += t.getTotalDiskon().intValue();
					totalHargaSetelahDiskon += t.getHargaSetelahDiskon().intValue();
					totalBa += t.getSumBeratAsli();
					totalBerat += t.getSumBerat();
					totalawb += t.getCount().intValue();
					txtHargaSetelahDiskonAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalHargaSetelahDiskon)));
					txtTotalDiskonAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalDiskon)));
					txtTotalBiayaAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalBiaya)));
					txtTotalBaAll.setText(BigDecimalUtil.truncateDecimal(totalBa, 2).toString());
					txtTotalBeratAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalBerat)));
					txtAwbAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalawb)));
					
					masterData2.add(new ReportVO(no++, t.getNmSales(), t.getPengirim(), t.getCount().intValueExact(),
							t.getSumBerat().intValue(), String.valueOf(t.getSumBeratAsli()),
							String.valueOf(formatRupiah.formatIndonesia(t.gettBiaya().intValue())),
							String.valueOf(formatRupiah.formatIndonesia(t.getHargaSetelahDiskon().intValue())),
							String.valueOf(formatRupiah.formatIndonesia(t.getTotalDiskon().intValue()))));
				}
					colNoAll.setCellValueFactory(cellData -> cellData.getValue().noProperty());
					colNamaSalesAll.setCellValueFactory(cellData -> cellData.getValue().nmSalesProperty());
					colPengirimAll.setCellValueFactory(cellData -> cellData.getValue().pengirimProperty());
					colAwbAll.setCellValueFactory(cellData -> cellData.getValue().jumlahBarangProperty());
					collBeratAll.setCellValueFactory(cellData -> cellData.getValue().sumBeratProperty());
					colBeratAsliAll.setCellValueFactory(cellData -> cellData.getValue().sumBeratAsliProperty());
					colTotalBiayaAll.setCellValueFactory(cellData -> cellData.getValue().biayaProperty());
					colHargaSetelahDiskonAll.setCellValueFactory(cellData -> cellData.getValue().hargaSetelahDiskonProperty());
					colTotalDiskonAll.setCellValueFactory(cellData -> cellData.getValue().totalDiskonproperty());
					FilteredList<ReportVO> filteredData = new FilteredList<>(masterData2, p -> true);

					txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
						filteredData.setPredicate(data -> {
							if (newValue == null || newValue.isEmpty()) {
								return true;
							}

							String lowerCaseFilter = newValue.toLowerCase();

							if (data.getNmSales().toLowerCase().indexOf(lowerCaseFilter) != -1) {
								return true;
							} else if (data.getPengirim().toLowerCase().indexOf(lowerCaseFilter) != -1) {
								return true;
							}
							return false;
						});
					});

					SortedList<ReportVO> sortedData = new SortedList<>(filteredData);
					sortedData.comparatorProperty().bind(tvAllPelanggan.comparatorProperty());
					tvAllPelanggan.setItems(sortedData);
			}
		}
	}

	@FXML
	public void onClikPdf() {
		Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
		Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
		
		String dateFile = DateUtil.getDateNotSeparator(dateAwl)+" sd "+DateUtil.getDateNotSeparator(dateAkh).substring(4);
		
		if (Checklist) {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("tgl_awal", DateUtil.convertToDatabaseColumn(dpAwal.getValue()));
				parameters.put("tgl_akhir", DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
				ExportJasper.JasperExportPdf(parameters, "C:/DLL/REPORT/perpelanggan.jrxml", "C:/DLL/REPORT/EXPORT/",
						"SemuaPelanggan", dateFile);
				MessageBox.alert("Export Berhasil di Drive C:/DLL/REPORT/EXPORT/"
						+dateFile
						+" SemuaPelanggan.pdf");
			} catch (Exception e) {
				MessageBox.alert(e.getMessage());
			}

		} else {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("kode_pelanggan", pelanggan);
				parameters.put("tgl_awal", DateUtil.convertToDatabaseColumn(dpAwal.getValue()));
				parameters.put("tgl_akhir", DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
				ExportJasper.JasperExportPdf(parameters, "C:/DLL/REPORT/email_tagihan.jrxml", "C:/DLL/REPORT/EXPORT/",
						pelanggan, dateFile);
				MessageBox.alert("Export Berhasil di Drive C:/DLL/REPORT/EXPORT/"
						+dateFile
						+" "+pelanggan+".pdf");
			} catch (Exception e) {
				MessageBox.alert(e.getMessage());
			}
		}
	}

	@FXML
	public void onClikExcel() {
  
		try {
			Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
			Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
			
			String dateFile = DateUtil.getDateNotSeparator(dateAwl)+" sd "+DateUtil.getDateNotSeparator(dateAkh).substring(4);
			String namaFile = "";
			if(!Checklist){
				namaFile = " Report_PerPelanggan ("+pelanggan+")";
			}else{
				namaFile = " Report_PerPelanggan";
			}
			
			ExportToExcell.exportToExcellReportPerPelanggan(Checklist, masterData, masterData2, namaFile,
					dateFile);
			MessageBox.alert("Export Berhasil Di Drive C:/DLL/REPORT/EXPORT/"
					+dateFile
					+namaFile+".xls");
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

	public void SetTableB() {
		List<String> listPelangganNotExists = ReportService.getIfPelangganExistForLapPerPelanggan(
				DateUtil.convertToDatabaseColumn(dpAwal.getValue()), 
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
		System.out.println("--> listPelangganNotExists : " + listPelangganNotExists.size());
		if(listPelangganNotExists.size()>0){
			String lstPel = "";
			Integer numInc = 0;
			for (String string : listPelangganNotExists) {
				numInc++;
				lstPel+=numInc + ". " + string + System.getProperty("line.separator");
			}
			lstPel = "Load laporan per pelanggan gagal, pelanggan ini tidak terdaftar pada tabel pelanggan : " + System.getProperty("line.separator") + lstPel;
			MessageBox.alertBesar(200, lstPel);
		}else{
			List<EntryDataShowVO> ed = ReportService.dataPerpelangganAll(
					DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
					DateUtil.convertToDatabaseColumn(dpAkhir.getValue()));
			Integer no = 1;
			int totalBiaya = 0;
			int totalHargaSetelahDiskon = 0;
			int totalDiskon = 0;
			double totalBa = 0.00;
			int totalBerat = 0;
			int totalawb = 0;
	//		txtTotalBaAll, txtTotalBeratAll, txtAwbAll;
			for (EntryDataShowVO t : ed) {
				totalBiaya += t.gettBiaya().intValueExact();
				totalDiskon += t.getTotalDiskon().intValue();
				totalHargaSetelahDiskon += t.getHargaSetelahDiskon().intValue();
				totalBa += t.getSumBeratAsli();
				totalBerat += t.getSumBerat();
				totalawb += t.getCount().intValue();
				txtHargaSetelahDiskonAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalHargaSetelahDiskon)));
				txtTotalDiskonAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalDiskon)));
				txtTotalBiayaAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalBiaya)));
				txtTotalBaAll.setText(BigDecimalUtil.truncateDecimal(totalBa, 2).toString());
				txtTotalBeratAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalBerat)));
				txtAwbAll.setText(String.valueOf(formatRupiah.formatIndonesia(totalawb)));
				
				masterData2.add(new ReportVO(no++, t.getNmSales(), t.getPengirim(), t.getCount().intValueExact(),
						t.getSumBerat().intValue(), String.valueOf(t.getSumBeratAsli()),
						String.valueOf(formatRupiah.formatIndonesia(t.gettBiaya().intValue())),
						String.valueOf(formatRupiah.formatIndonesia(t.getHargaSetelahDiskon().intValue())),
						String.valueOf(formatRupiah.formatIndonesia(t.getTotalDiskon().intValue()))));
	
				colNoAll.setCellValueFactory(cellData -> cellData.getValue().noProperty());
				colNamaSalesAll.setCellValueFactory(cellData -> cellData.getValue().nmSalesProperty());
				colPengirimAll.setCellValueFactory(cellData -> cellData.getValue().pengirimProperty());
				colAwbAll.setCellValueFactory(cellData -> cellData.getValue().jumlahBarangProperty());
				collBeratAll.setCellValueFactory(cellData -> cellData.getValue().sumBeratProperty());
				colBeratAsliAll.setCellValueFactory(cellData -> cellData.getValue().sumBeratAsliProperty());
				colTotalBiayaAll.setCellValueFactory(cellData -> cellData.getValue().biayaProperty());
				colHargaSetelahDiskonAll.setCellValueFactory(cellData -> cellData.getValue().hargaSetelahDiskonProperty());
				colTotalDiskonAll.setCellValueFactory(cellData -> cellData.getValue().totalDiskonproperty());
				FilteredList<ReportVO> filteredData = new FilteredList<>(masterData2, p -> true);
	
				txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
					filteredData.setPredicate(data -> {
						if (newValue == null || newValue.isEmpty()) {
							return true;
						}
	
						String lowerCaseFilter = newValue.toLowerCase();
	
						if (data.getNmSales().toLowerCase().indexOf(lowerCaseFilter) != -1) {
							return true;
						} else if (data.getPengirim().toLowerCase().indexOf(lowerCaseFilter) != -1) {
							return true;
						}
						return false;
					});
				});
	
				SortedList<ReportVO> sortedData = new SortedList<>(filteredData);
				sortedData.comparatorProperty().bind(tvAllPelanggan.comparatorProperty());
				tvAllPelanggan.setItems(sortedData);
			}
		}
	}
}
