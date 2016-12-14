package controller;

import java.io.IOException;

import java.net.URL;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import controller.AwbHistoryController.KurirTV;
import entity.TrPelanggan;
import entity.TtDataEntry;
import entity.TtHeader;
import entity.TtPotoTimbang;
import entity.TtStatusKurirIn;
import entity.TtStatusKurirOut;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import net.sf.jasperreports.engine.util.MessageUtil;
import service.SyncService;

import util.DateUtil;
import util.DtoListener;
import util.ManagedFormHelper;
import util.MessageBox;
import util.PropertiesUtil;
import util.WindowsHelper;

public class SyncDataController implements Initializable {
	
	@FXML
	DatePicker dtStart, dtEnd;
	@FXML
	Button btnTest, btnAnalisa;
	@FXML
	TableView tblView;	
	
	String perwakilan = PropertiesUtil.getPerwakilan().toUpperCase();
	
	// TABEL TRANSAKSI
	// list kumpulan data transaksi berdasarkan periode
	List<TtHeader> ttHeaderHO = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryHO = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangHO = new ArrayList<TtPotoTimbang>();
	
	List<TtHeader> ttHeaderCabang = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryCabang = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangCabang = new ArrayList<TtPotoTimbang>();
	
	// TABEL KURIR IN OUT
	List<TtStatusKurirIn> ttStatusKurirInHO = new ArrayList<TtStatusKurirIn>();
	List<TtStatusKurirOut> ttStatusKurirOutHO = new ArrayList<TtStatusKurirOut>();
	
	List<TtStatusKurirIn> ttStatusKurirInCabang = new ArrayList<TtStatusKurirIn>();
	List<TtStatusKurirOut> ttStatusKurirOutCabang = new ArrayList<TtStatusKurirOut>();
	
	List<TtStatusKurirIn> ttStatusKurirInFilteredHO = new ArrayList<TtStatusKurirIn>();
	List<TtStatusKurirOut> ttStatusKurirOutFilteredHO = new ArrayList<TtStatusKurirOut>();
	
	List<TtStatusKurirIn> ttStatusKurirInFilteredCabang = new ArrayList<TtStatusKurirIn>();
	List<TtStatusKurirOut> ttStatusKurirOutFilteredCabang = new ArrayList<TtStatusKurirOut>();
	
	// TABEL PELANGGAN
	List<TrPelanggan> trPelangganHO = new ArrayList<TrPelanggan>();	
	List<TrPelanggan> trPelangganCabang = new ArrayList<TrPelanggan>();
	
	// list untuk di lempar dari cabang ke HO
	List<TtHeader> ttHeaderCabangHOFilteredHO = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryCabangHOFilteredHO = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangCabangHOFilteredHO = new ArrayList<TtPotoTimbang>();
		
	List<TtHeader> ttHeaderCabangHOFilteredCabang = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryCabangHOFilteredCabang = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangCabangHOFilteredCabang = new ArrayList<TtPotoTimbang>();
		
	List<TrPelanggan> trPelangganCabangHOFilteredHO = new ArrayList<TrPelanggan>();
	List<TrPelanggan> trPelangganCabangHOFilteredCabang = new ArrayList<TrPelanggan>();
		
	// list untuk dilempar dari HO ke cabang
	List<TtHeader> ttHeaderHOCabangFilteredHO = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryHOCabangFilteredHO = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangHOCabangFilteredHO = new ArrayList<TtPotoTimbang>();
		
	List<TtHeader> ttHeaderHOCabangFilteredCabang = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryHOCabangFilteredCabang = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangHOCabangFilteredCabang = new ArrayList<TtPotoTimbang>();
		
	List<TrPelanggan> trPelangganHOCabangFilteredHO = new ArrayList<TrPelanggan>();
	List<TrPelanggan> trPelangganHOCabangFilteredCabang = new ArrayList<TrPelanggan>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManagedFormHelper.instanceController = this;
		dtStart.setValue(LocalDate.now());
		dtEnd.setValue(LocalDate.now());
		btnTest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
								
				try {
					executeSync();
					
					loadTableTransaksi();
					loadTableKurirInOut();
					loadTableMaster();
					makeDataCabangToHo();
					makeDataHoToCabang();				
					
					MessageBox.alert("Synchronize sudah selesai");
					showAnalisa();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("ERROR TRY CATCH : " + e.getMessage());
					MessageBox.alert("Synchronize gagal, silahkan sampaikan error ini kepada pak wiko : " + e.getMessage());
				}
			}
		});
		btnAnalisa.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {			
				loadTableTransaksi();
				loadTableKurirInOut();
				loadTableMaster();
				
				makeDataCabangToHo();
				makeDataHoToCabang();				
				
				showAnalisa();
			}
		});		
	}
		
	private void loadTableMaster() {
		// TABEL PELANGGAN
		trPelangganHO = SyncService.loadItemsFromHO(TrPelanggan.class, null, null);
		trPelangganCabang = SyncService.loadItemsFromCabang(TrPelanggan.class, null, null);
	}
	
	private void loadTableKurirInOut() {
		ttStatusKurirInHO = SyncService.getTtStatusKurirIn(true, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
		ttStatusKurirInCabang = SyncService.getTtStatusKurirIn(false, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
		
		ttStatusKurirOutHO = SyncService.getTtStatusKurirOut(true, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
		ttStatusKurirOutCabang = SyncService.getTtStatusKurirOut(false, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
		
		System.out.println("--> ttStatusKurirInCabang : " + ttStatusKurirInCabang.size());
		System.out.println("--> ttStatusKurirOutCabang : " + ttStatusKurirOutCabang.size());
		System.out.println("--> ttStatusKurirInHO : " + ttStatusKurirInHO.size());
		System.out.println("--> ttStatusKurirOutHO : " + ttStatusKurirOutHO.size());
	}
	
	private void executeSync() {
		executeCabangToHo();
		executeHoToCabang();
	}
	
	private void executeHoToCabang() {
		List<TtHeader> lstInsertHeaderCabang = new ArrayList<TtHeader>();
		List<TtDataEntry> lstInsertDataEntryCabang = new ArrayList<TtDataEntry>();
		List<TtPotoTimbang> lstInsertPotoTimbangCabang = new ArrayList<TtPotoTimbang>();
		List<TrPelanggan> lstInsertPelangganCabang = new ArrayList<TrPelanggan>();
		
		Boolean bol = false;
		for (TtHeader tt : ttHeaderHOCabangFilteredHO) {			
			for (TtHeader ss : ttHeaderHOCabangFilteredCabang) {
				if(tt.getAwbHeader().equals(ss.getAwbHeader()))bol=true;
			}
			if(!bol){
				lstInsertHeaderCabang.add(tt);
				SyncService.addToCabang(tt, TtHeader.class);
			}
			bol = false;
		}
		for (TtDataEntry tt : ttDataEntryHOCabangFilteredHO) {
			for (TtDataEntry ss : ttDataEntryHOCabangFilteredCabang) {
				if(tt.getAwbDataEntry().equals(ss.getAwbDataEntry()))bol=true;
			}
			if(!bol){
				lstInsertDataEntryCabang.add(tt);
				SyncService.addToCabang(tt, TtDataEntry.class);
			}
			bol = false;
		}
		for (TtPotoTimbang tt : ttPotoTimbangHOCabangFilteredHO) {
			for (TtPotoTimbang ss : ttPotoTimbangHOCabangFilteredCabang) {
				if(tt.getAwbPotoTimbang().equals(ss.getAwbPotoTimbang()))bol=true;
			}
			if(!bol){
				lstInsertPotoTimbangCabang.add(tt);
				SyncService.addToCabang(tt, TtPotoTimbang.class);
			}
			bol = false;
		}	
		for (TrPelanggan tt : trPelangganHO) {
			for (TrPelanggan ss : trPelangganCabang) {
				if(tt.getKodePelanggan().toUpperCase().equals(ss.getKodePelanggan().toUpperCase()))bol=true;
			}
			if(!bol){
				lstInsertPelangganCabang.add(tt);
				tt.setTelp("xxx");
				tt.setEmail("xxx");
				tt.setAlamat("xxx");
				tt.setLine("xxx");
				tt.setInstagram("xxx");
				SyncService.addToCabang(tt, TrPelanggan.class);
			}
			bol = false;
		}
	}

	private void executeCabangToHo() {
		// TABEL TRANSAKSI
		List<TtHeader> lstInsertHeaderHO = new ArrayList<TtHeader>();
		List<TtDataEntry> lstInsertDataEntryHO = new ArrayList<TtDataEntry>();
		List<TtPotoTimbang> lstInsertPotoTimbangHO = new ArrayList<TtPotoTimbang>();
		
		// TABEL KURIR IN OUT
		List<TtStatusKurirIn> lstInsertStatusKurirInHO = new ArrayList<TtStatusKurirIn>();
		List<TtStatusKurirOut> lstInsertStatusKurirOutHO = new ArrayList<TtStatusKurirOut>();
		
		// TABEL PELANGGAN
		List<TrPelanggan> lstInsertPelangganHO = new ArrayList<TrPelanggan>();
		
		Boolean bol = false;
		for (TtHeader tt : ttHeaderCabangHOFilteredCabang) {			
			for (TtHeader ss : ttHeaderCabangHOFilteredHO) {
				if(tt.getAwbHeader().equals(ss.getAwbHeader()))bol=true;
			}
			if(!bol){
				lstInsertHeaderHO.add(tt);
				SyncService.addToHO(tt, TtHeader.class);
			}
			bol = false;
		}
		for (TtDataEntry tt : ttDataEntryCabangHOFilteredCabang) {
			for (TtDataEntry ss : ttDataEntryCabangHOFilteredHO) {
				if(tt.getAwbDataEntry().equals(ss.getAwbDataEntry()))bol=true;
			}
			if(!bol){
				lstInsertDataEntryHO.add(tt);
				SyncService.addToHO(tt, TtDataEntry.class);
			}
			bol = false;
		}
		for (TtPotoTimbang tt : ttPotoTimbangCabangHOFilteredCabang) {
			for (TtPotoTimbang ss : ttPotoTimbangCabangHOFilteredHO) {
				if(tt.getAwbPotoTimbang().equals(ss.getAwbPotoTimbang()))bol=true;
			}
			if(!bol){
				lstInsertPotoTimbangHO.add(tt);
				SyncService.addToHO(tt, TtPotoTimbang.class);
			}
			bol = false;
		}	
		// ======
		for (TtStatusKurirIn tt : ttStatusKurirInCabang) {
			
			for (TtStatusKurirIn ss : ttStatusKurirInFilteredCabang) {
				System.out.println("ss : " + ss.getIdBarang());
				if(tt.getIdBarang().equals(ss.getIdBarang())&&tt.getStatus().equals(ss.getStatus()))bol=true;
			}
			if(!bol){
				lstInsertStatusKurirInHO.add(tt);
				SyncService.addToHO(tt, TtStatusKurirIn.class);
			}
			bol = false;
		}	
		for (TtStatusKurirOut tt : ttStatusKurirOutCabang) {
			for (TtStatusKurirOut ss : ttStatusKurirOutFilteredCabang) {
				if(tt.getIdBarang().equals(ss.getIdBarang())&&tt.getStatus().equals(ss.getStatus()))bol=true;
			}
			if(!bol){
				lstInsertStatusKurirOutHO.add(tt);
				SyncService.addToHO(tt, TtStatusKurirOut.class);
			}
			bol = false;
		}		
		for(TrPelanggan tt : trPelangganCabang){
			for(TrPelanggan ss : trPelangganCabangHOFilteredHO){
				if(tt.getKodePelanggan().toUpperCase().equals(ss.getKodePelanggan().toUpperCase()))bol=true;
			}
			if(!bol){
				lstInsertPelangganHO.add(tt);
				SyncService.addToHO(tt, TrPelanggan.class);
			}
			bol = false;
		}
	}

	private void makeDataCabangToHo() {
		// tabel header
		ttHeaderCabangHOFilteredHO.clear();
		ttHeaderCabangHOFilteredCabang.clear();
		for (TtHeader tt : ttHeaderHO) {
			if(tt.getAwbHeader().toUpperCase().contains(perwakilan)){
				ttHeaderCabangHOFilteredHO.add(tt);
			}
		}
		for (TtHeader tt : ttHeaderCabang) {
			if(tt.getAwbHeader().toUpperCase().contains(perwakilan)){
				ttHeaderCabangHOFilteredCabang.add(tt);
			}
		}
		// tabel data entry
		ttDataEntryCabangHOFilteredHO.clear();
		ttDataEntryCabangHOFilteredCabang.clear();
		for (TtDataEntry tt : ttDataEntryHO) {
			if(tt.getAwbDataEntry().toUpperCase().contains(perwakilan)){
				ttDataEntryCabangHOFilteredHO.add(tt);
			}
		}
		for (TtDataEntry tt : ttDataEntryCabang) {
			if(tt.getAwbDataEntry().toUpperCase().contains(perwakilan)){
				ttDataEntryCabangHOFilteredCabang.add(tt);
			}
		}
		// tabel poto timbang
		ttPotoTimbangCabangHOFilteredHO.clear();
		ttPotoTimbangCabangHOFilteredCabang.clear();
		for (TtPotoTimbang tt : ttPotoTimbangHO) {
			if(tt.getAwbPotoTimbang().toUpperCase().contains(perwakilan)){
				ttPotoTimbangCabangHOFilteredHO.add(tt);
			}
		}
		for (TtPotoTimbang tt : ttPotoTimbangCabang) {
			if(tt.getAwbPotoTimbang().toUpperCase().contains(PropertiesUtil.getPerwakilan().toUpperCase())){
				ttPotoTimbangCabangHOFilteredCabang.add(tt);
			}
		}
		// tabel status kurir in di cabang
		ttStatusKurirInFilteredCabang.clear();
		for (TtStatusKurirIn tt : ttStatusKurirInCabang) {
			for (TtStatusKurirIn ss : ttStatusKurirInHO) {
				if(tt.getIdBarang().equals(ss.getIdBarang())&&tt.getStatus().equals(ss.getStatus())){
					ttStatusKurirInFilteredCabang.add(tt);
				}
			}
		}
		// tabel status kurir out di cabang
		ttStatusKurirOutFilteredCabang.clear();
		for (TtStatusKurirOut tt : ttStatusKurirOutCabang) {
			for (TtStatusKurirOut ss : ttStatusKurirOutHO) {
				if(tt.getIdBarang().equals(ss.getIdBarang())&&tt.getStatus().equals(ss.getStatus())){
					ttStatusKurirOutFilteredCabang.add(tt);
				}
			}
		}
		// tabel status kurir in di HO
		ttStatusKurirInFilteredHO.clear();
		for (TtStatusKurirIn tt : ttStatusKurirInFilteredCabang) {
			for (TtStatusKurirIn ss : ttStatusKurirInHO) {
				if(tt.equals(ss)){
					ttStatusKurirInFilteredHO.add(tt);
				}
			}
		}
		// tabel status kurir out di HO
		ttStatusKurirOutFilteredHO.clear();
		for (TtStatusKurirOut tt : ttStatusKurirOutFilteredCabang) {
			for (TtStatusKurirOut ss : ttStatusKurirOutHO) {
				if(tt.equals(ss)){
					ttStatusKurirOutFilteredHO.add(tt);
				}
			}
		}

		// tabel pelanggan
		trPelangganCabangHOFilteredHO.clear();
		for(TrPelanggan tt : trPelangganCabang){
			for(TrPelanggan ss : trPelangganHO){
				if(tt.getKodePelanggan().toUpperCase().equals(ss.getKodePelanggan().toUpperCase())){
					trPelangganCabangHOFilteredHO.add(tt);
				}
			}
		}
	}

	private void makeDataHoToCabang() {
		List<String> daftarAwb = new ArrayList<String>();
		for (TtPotoTimbang ho : ttPotoTimbangHO) {
			if(ho.getKodePerwakilan()!=null){
				if(ho.getKodePerwakilan().toUpperCase().contains(perwakilan)){
					if(!ho.getAwbPotoTimbang().toUpperCase().contains(perwakilan)){
						daftarAwb.add(ho.getAwbPotoTimbang());
					}
				}
			}
		}
		ttHeaderHOCabangFilteredHO.clear();
		for (TtHeader tt : ttHeaderHO) {
			for (String awb : daftarAwb) {
				if(tt.getAwbHeader().equals(awb)){
					ttHeaderHOCabangFilteredHO.add(tt);
				}
			}			
		}
		ttDataEntryHOCabangFilteredHO.clear();
		for (TtDataEntry tt : ttDataEntryHO) {
			for (String awb : daftarAwb) {
				if(tt.getAwbDataEntry().equals(awb)){
					ttDataEntryHOCabangFilteredHO.add(tt);
				}
			}			
		}
		ttPotoTimbangHOCabangFilteredHO.clear();
		for (TtPotoTimbang tt : ttPotoTimbangHO) {
			for (String awb : daftarAwb) {
				if(tt.getAwbPotoTimbang().equals(awb)){
					ttPotoTimbangHOCabangFilteredHO.add(tt);
				}
			}			
		}
		Boolean bol = false;
		
		ttHeaderHOCabangFilteredCabang.clear();
		for (TtHeader tt : ttHeaderHOCabangFilteredHO) {
			for (TtHeader ss : ttHeaderCabang) {
				if(tt.getAwbHeader().equals(ss.getAwbHeader()))bol=true;
			}
			if(bol){
				ttHeaderHOCabangFilteredCabang.add(tt);
				bol = false;
			}
		}
		ttDataEntryHOCabangFilteredCabang.clear();
		for (TtDataEntry tt : ttDataEntryHOCabangFilteredHO) {
			for (TtDataEntry ss : ttDataEntryCabang) {
				if(tt.getAwbDataEntry().equals(ss.getAwbDataEntry()))bol=true;
			}
			if(bol){
				ttDataEntryHOCabangFilteredCabang.add(tt);
				bol = false;
			}
		}
		ttPotoTimbangHOCabangFilteredCabang.clear();
		for (TtPotoTimbang tt : ttPotoTimbangHOCabangFilteredHO) {
			for (TtPotoTimbang ss : ttPotoTimbangCabang) {
				if(tt.getAwbPotoTimbang().equals(ss.getAwbPotoTimbang()))bol=true;
			}
			if(bol){
				ttPotoTimbangHOCabangFilteredCabang.add(tt);
				bol = false;
			}
		}
		trPelangganHOCabangFilteredCabang.clear();
		for(TrPelanggan tt : trPelangganHO){
			for(TrPelanggan ss : trPelangganCabang){
				if(tt.getKodePelanggan().toUpperCase().equals(ss.getKodePelanggan().toUpperCase())){
					trPelangganHOCabangFilteredCabang.add(ss);
				}
			}
		}
	}
	
	private void loadTableTransaksi() {
//		ttHeaderHO = SyncService.loadItemsFromHO(TtHeader.class, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
//		ttDataEntryHO = SyncService.loadItemsFromHO(TtDataEntry.class, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
//		ttPotoTimbangHO = SyncService.loadItemsFromHO(TtPotoTimbang.class, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
		
//		ttHeaderCabang = SyncService.loadItemsFromCabang(TtHeader.class, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
//		ttDataEntryCabang = SyncService.loadItemsFromCabang(TtDataEntry.class, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
//		ttPotoTimbangCabang = SyncService.loadItemsFromCabang(TtPotoTimbang.class, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
		
		Map<String, Object> mapHO = SyncService.loadItemsTransactionalNative(true, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
		Map<String, Object> mapCabang = SyncService.loadItemsTransactionalNative(false, DateUtil.convertToDatabaseColumn(dtStart.getValue()), DateUtil.convertToDatabaseColumn(dtEnd.getValue()));
		
		ttHeaderHO = (List<TtHeader>) mapHO.get("HEADER");
		ttDataEntryHO = (List<TtDataEntry>) mapHO.get("DATAENTRY");
		ttPotoTimbangHO = (List<TtPotoTimbang>) mapHO.get("POTOTIMBANG");
		
		ttHeaderCabang = (List<TtHeader>) mapCabang.get("HEADER");
		ttDataEntryCabang = (List<TtDataEntry>) mapCabang.get("DATAENTRY");
		ttPotoTimbangCabang = (List<TtPotoTimbang>) mapCabang.get("POTOTIMBANG");
		
		
		
	}
	
	private void showAnalisa() {
		tblView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		ObservableList<SyncTV> pops = FXCollections.observableArrayList();
		SyncTV ttHeader = new SyncTV(1, "TT_HEADER", ttHeaderCabangHOFilteredHO.size() + " of " + ttHeaderCabangHOFilteredCabang.size(), ttHeaderHOCabangFilteredCabang.size() + " of " + ttHeaderHOCabangFilteredHO.size());
		SyncTV ttDataEntry = new SyncTV(2, "TT_DATA_ENTRY", ttDataEntryCabangHOFilteredHO.size() + " of " + ttDataEntryCabangHOFilteredCabang.size(), ttDataEntryHOCabangFilteredCabang.size() + " of " + ttDataEntryHOCabangFilteredHO.size());
		SyncTV ttPotoTimbang = new SyncTV(3, "TT_POTO_TIMBANG", ttPotoTimbangCabangHOFilteredHO.size() + " of " + ttPotoTimbangCabangHOFilteredCabang.size(), ttPotoTimbangHOCabangFilteredCabang.size() + " of " + ttPotoTimbangHOCabangFilteredHO.size());
		
		SyncTV ttStatusKurirIn = new SyncTV(4, "TT_STATUS_KURIR_IN", ttStatusKurirInFilteredCabang.size() + " of " + ttStatusKurirInCabang.size(), "-");
		SyncTV ttStatusKurirOut = new SyncTV(5, "TT_STATUS_KURIR_OUT", ttStatusKurirOutFilteredCabang.size() + " of " + ttStatusKurirOutCabang.size(), "-");
		
		SyncTV trPelanggan = new SyncTV(5, "TR_PELANGGAN", trPelangganCabangHOFilteredHO.size() + " of " + trPelangganCabang.size(), trPelangganHOCabangFilteredCabang.size() + " of " + trPelangganHO.size());
		
		pops.add(ttHeader);
		pops.add(ttDataEntry);
		pops.add(ttPotoTimbang);
		pops.add(ttStatusKurirIn);
		pops.add(ttStatusKurirOut);
		pops.add(trPelanggan);
		
		tblView.getColumns().clear();
		tblView.getItems().clear();
		
		tblView.setItems(pops);
		
		TableColumn<KurirTV, Integer> noCol = new TableColumn<KurirTV, Integer>("No");
		TableColumn<KurirTV, String> tabelCol = new TableColumn<KurirTV, String>("Nama Tabel");
		TableColumn<KurirTV, String> cabangToHOCol = new TableColumn<KurirTV, String>("Cabang To HO");
		TableColumn<KurirTV, String> HOToCabangCol = new TableColumn<KurirTV, String>("HO To Cabang");
		
		
		tblView.getColumns().addAll(noCol, tabelCol, cabangToHOCol, HOToCabangCol);
		
		noCol.setCellValueFactory(new PropertyValueFactory<KurirTV, Integer>("no"));
		tabelCol.setCellValueFactory(new PropertyValueFactory<KurirTV, String>("table"));
		cabangToHOCol.setCellValueFactory(new PropertyValueFactory<KurirTV, String>("cabangToHo"));
		HOToCabangCol.setCellValueFactory(new PropertyValueFactory<KurirTV, String>("HOToCabang"));
		
		tblView.setEditable(true);
	}
		
	public class SyncTV{
		private IntegerProperty no;
		private StringProperty table;
		private StringProperty cabangToHo;
		private StringProperty HOToCabang;
		
		public SyncTV(Integer no, String table, String cabangToHo, String HOToCabang){
			this.no = new SimpleIntegerProperty(no);
			this.table = new SimpleStringProperty(table);
			this.cabangToHo = new SimpleStringProperty(cabangToHo);
			this.HOToCabang = new SimpleStringProperty(HOToCabang);
		}

		public Integer getNo() {
			return no.get();
		}

		public void setNo(Integer no) {
			this.no.set(no);
		}

		public String getTable() {
			return table.get();
		}

		public void setTable(String table) {
			this.table.set(table);
		}

		public String getCabangToHo() {
			return cabangToHo.get();
		}

		public void setCabangToHo(String cabangToHo) {
			this.cabangToHo.set(cabangToHo);
		}
		
		public String getHOToCabang() {
			return HOToCabang.get();
		}

		public void setHOToCabang(String hOToCabang) {
			HOToCabang.set(hOToCabang);
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

}
