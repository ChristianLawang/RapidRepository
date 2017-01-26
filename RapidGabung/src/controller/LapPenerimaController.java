package controller;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import VO.BrowseSemuaDataVO;
import VO.EntryDataShowVO;
import VO.LapPODVO;
import VO.LaporanPenerimaVO;
import entity.TrKurir;
import entity.TrPerwakilan;
import entity.TtDataEntry;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import popup.PopUpLapPODController;
import service.BrowseSemuaDataService;
import service.LaporanPODService;
import service.LaporanPenerimaService;
import service.MasterKurirService;
import service.MasterPerwakilanService;
import service.ReportService;
import util.DateUtil;
import util.DtoListener;
import util.ExportToExcell;
import util.ManagedFormHelper;
import util.MessageBox;
import util.WindowsHelper;
import utilfx.AutoCompleteComboBoxListener;

public class LapPenerimaController implements Initializable {

	
	@FXML
	private DatePicker dpAwal, dpAkhir;

	@FXML
	private TextField txtCari;

	@FXML
	private ComboBox cbPengirim, cbPerwakilan;
	
	@FXML
	private CheckBox chkAll;
	
	@FXML
	private CheckBox chkDetail;
	
	@FXML
	private TableView<LaporanPenerimaVO> tvLapPengiriman;

	@FXML
	private TableColumn<LaporanPenerimaVO, Number> colNo;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colAwb;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colTanggal;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colPengirim;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colPenerima;
	
	@FXML
	private TableColumn<LaporanPenerimaVO, String> colTelpPenerima;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colTujuan;
	
	@FXML
	private TableColumn<LaporanPenerimaVO, String> colZona;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colBerat;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colEtd;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colTglPenerima;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colPenerimaPaket;

	@FXML
	private TableColumn<LaporanPenerimaVO, String> colStatus;
	
	@FXML
	private TableColumn<LaporanPenerimaVO, String> colKeterangan;
	
	@FXML
	private TableColumn<LaporanPenerimaVO, String> colWaktuPenerima;
	
	private ObservableList<LaporanPenerimaVO> masterData = FXCollections.observableArrayList();

	@FXML
	RadioButton rbJmlhPaket, rbSudahReport, rbBelumReport, rbMasalah;
	
	private String cbPengirimValue;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;
		dpAwal.setValue(LocalDate.now());
		dpAkhir.setValue(LocalDate.now());
		
		ObservableList<TtDataEntry> listNamaKurir = FXCollections
				.observableArrayList(LaporanPenerimaService.getDataPengirim());
		for (TtDataEntry i : listNamaKurir) {
			cbPengirim.getItems().add(i.getPengirim());
		}

		new AutoCompleteComboBoxListener<TrKurir>(cbPengirim);
		
		cbPengirim.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				cbPengirimValue = t1;
			}
		});
		cbPerwakilan.getItems().add("All Cabang");
		List<TrPerwakilan> lstPerwakilan = MasterPerwakilanService.getAllPerwakilanCabangDistinct();
		for (TrPerwakilan trPerwakilan : lstPerwakilan) {
			cbPerwakilan.getItems().add(trPerwakilan.getKodePerwakilan());
		}
		cbPerwakilan.setValue("All Cabang");
		
	}

	public void SetTable() {
		List<EntryDataShowVO> tt = 
			LaporanPenerimaService.getListTableLapPenerima(
				cbPengirimValue,
				DateUtil.convertToDatabaseColumn(dpAwal.getValue()),
				DateUtil.convertToDatabaseColumn(dpAkhir.getValue()), 
				cbPerwakilan.getValue().toString(), 
				chkAll,
				chkDetail);
		
		System.out.println("--> table size : " + tt.size());
		
		Integer no = 1;
		for (EntryDataShowVO t : tt) {
			masterData.add(new LaporanPenerimaVO(no++, DateUtil.getStdDateDisplay2(t.getTglEntry()), t.getAwbData(), 
					t.getPenerima(), t.getNoTlpn(), t.getPengirim(), t.getTujuan(), t.getZona(), t.getbFinal(), t.getEtd(), DateUtil.getStdDateDisplay2(t.getTglTerimaPaketDate()),
					t.getWaktuTerimaPaket(), t.getStatus(), t.getPenerimaPaket(),  t.getKeterangan()));
		}
		colNo.setCellValueFactory(cellData -> cellData.getValue().noProperty());
		colTanggal.setCellValueFactory(cellData -> cellData.getValue().tanggalProperty());
		colAwb.setCellValueFactory(cellData -> cellData.getValue().awbProperty());
		colPengirim.setCellValueFactory(cellData -> cellData.getValue().pengirimProperty());
		colPenerima.setCellValueFactory(cellData -> cellData.getValue().penerimaProperty());
		colTelpPenerima.setCellValueFactory(cellData -> cellData.getValue().telpPenerimaProperty());
		colTujuan.setCellValueFactory(cellData -> cellData.getValue().tujuanProperty());
		colZona.setCellValueFactory(cellData -> cellData.getValue().zonaProperty());
		colBerat.setCellValueFactory(cellData -> cellData.getValue().beratProperty());
		colEtd.setCellValueFactory(cellData -> cellData.getValue().EtdProperty());
		colTglPenerima.setCellValueFactory(cellData -> cellData.getValue().tglPenerimaProperty());
		colTglPenerima.setCellFactory(TextFieldTableCell.forTableColumn());
		colTglPenerima.setOnEditCommit(new EventHandler<CellEditEvent<LaporanPenerimaVO, String>>() {
			public void handle(CellEditEvent<LaporanPenerimaVO, String> t) {
				LaporanPenerimaVO lp = (t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(lp.getStatus().equals("JNE PROCESS")){
					MessageBox.alert("Paket ini telah ditangani pihak JNE");
				}else if(lp.getStatus().equals("ONPROCESS")){
					MessageBox.alert("Paket ini masih belum di proses oleh kurir");
				}else{
					String newValue = t.getNewValue();
					Date newDate = DateUtil.stdDateLiteralToDate(newValue);
					
					if(newDate==null){
						MessageBox.alert("Format tanggal harusnya yyyy-mm-dd");
					}else{
						LaporanPenerimaService.updateTglTerima(
								lp.getAwb(), 
								t.getOldValue(), 
								lp.getWaktuPenerima().split(":")[0], 
								lp.getWaktuPenerima().split(":")[1],
								newDate);
					}
				}
				onClikCari();
			}
		});
		colWaktuPenerima.setCellValueFactory(cellData -> cellData.getValue().waktuPenerimaProperty());
		colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		colStatus.setCellFactory(TextFieldTableCell.forTableColumn());
		colStatus.setOnEditCommit(new EventHandler<CellEditEvent<LaporanPenerimaVO, String>>() {
			public void handle(CellEditEvent<LaporanPenerimaVO, String> t) {
				LaporanPenerimaVO lp = (t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(lp.getStatus().equals("JNE PROCESS")){
					MessageBox.alert("Paket ini telah ditangani pihak JNE");
				}else if(lp.getStatus().equals("ONPROCESS")){
					MessageBox.alert("Paket ini masih belum di proses oleh kurir");
				}else{
					
					String newValue = ValidationStatus(t.getNewValue());
					if(newValue==null){
						MessageBox.alert("Input status tidak dikenal, pastikan anda entry yg sesuai, contoh : diterima/au/antar ulang/coda/close/ba/bad address/nth/not at home/miss route/criscross/retur/pending");
					}else{
						LaporanPenerimaService.updateStatusPaket(
								lp.getAwb(), 
								lp.getTglPenerima(), 
								lp.getWaktuPenerima().split(":")[0], 
								lp.getWaktuPenerima().split(":")[1],
								newValue);
					}
				}
				onClikCari();
			}

			private String ValidationStatus(String newValue) {
				String diterima = newValue.toUpperCase();
				if(diterima.contains("DITERIMA")){
					return "-1 - Diterima";
				}
				if(diterima.contains("AU")||diterima.contains("ANTAR ULANG")){
					return "1 - AU (Antar Ulang)";
				}
				if(diterima.contains("CODA")||diterima.contains("CLOSE")){
					return "4 - CODA (Closed of Delivery Attempt)";
				}
				if(diterima.contains("BA")||diterima.contains("BAD ADDRESS")){
					return "2 - BA (Bad Address)";
				}
				if(diterima.contains("NTH")||diterima.contains("NOT AT HOME")){
					return "3 - NTH (Not at Home)";
				}
				if(diterima.contains("MISS ROUTE")){
					return "6 - Miss Route";
				}
				if(diterima.contains("CRISCROSS")){
					return "5 - CrisCross";
				}
				if(diterima.contains("RETUR")){
					return "9 - Retur";
				}
				if(diterima.contains("PENDING")){
					return "8 - Pending";
				}
				return null;
			}
		});
		colPenerimaPaket.setCellValueFactory(cellData -> cellData.getValue().penerimaPaketProperty());
		colPenerimaPaket.setCellFactory(TextFieldTableCell.forTableColumn());
		colPenerimaPaket.setOnEditCommit(new EventHandler<CellEditEvent<LaporanPenerimaVO, String>>() {
			public void handle(CellEditEvent<LaporanPenerimaVO, String> t) {
				LaporanPenerimaVO lp = (t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(lp.getStatus().equals("JNE PROCESS")){
					MessageBox.alert("Paket ini telah ditangani pihak JNE");
				}else if(lp.getStatus().equals("ONPROCESS")){
					MessageBox.alert("Paket ini masih belum di proses oleh kurir");
				}else{
					if(lp.getTglPenerima()==null||lp.getWaktuPenerima()==null){
						MessageBox.alert("Nama penerima tidak bisa diberikan bila kurir belum pernah mengantarkan paket tersebut");
					}else{
						LaporanPenerimaService.updateNamaPenerimaBelakang(
								lp.getAwb(), 
								lp.getTglPenerima(), 
								lp.getWaktuPenerima().split(":")[0], 
								lp.getWaktuPenerima().split(":")[1],
								t.getNewValue());
					}
				}
				onClikCari();
			}
		});
		colKeterangan.setCellValueFactory(cellData -> cellData.getValue().keteranganProperty());
		FilteredList<LaporanPenerimaVO> filteredData = new FilteredList<>(masterData, p -> true);

		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(data -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (data.getAwb().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}else if(data.getPengirim().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getPenerima().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getTujuan().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getPenerimaPaket().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getStatus().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getZona().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}else if(data.getKeterangan().toLowerCase().indexOf(lowerCaseFilter) != -1){
					return true;
				}
				return false;
			});
		});

		SortedList<LaporanPenerimaVO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvLapPengiriman.comparatorProperty());
		tvLapPengiriman.setItems(sortedData);
	}

	@FXML
	public void onClikCari() {
		masterData.clear();
		SetTable();
	}

	@FXML
	public void onClikExcel() {
		Date dateAwl = DateUtil.convertToDatabaseColumn(dpAwal.getValue());
		Date dateAkh = DateUtil.convertToDatabaseColumn(dpAkhir.getValue());
			
		String dateFile = DateUtil.getDateNotSeparator(dateAwl)+" sd "+DateUtil.getDateNotSeparator(dateAkh).substring(4);
		String namaFile = "";

		namaFile = " Report_Penerima";

			
		ExportToExcell.exportToExcellReportPenerima(masterData, namaFile,
				dateFile);


	}
	
	@FXML
	public void onClikPDF() {
		
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
