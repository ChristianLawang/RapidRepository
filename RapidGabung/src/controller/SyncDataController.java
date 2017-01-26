package controller;

import java.io.IOException;

import java.net.URL;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;

import VO.BrowseSemuaDataVO;
import controller.AwbHistoryController.KurirTV;
import controller.MasterPelangganController.DiskonTV;
import entity.TrCabang;
import entity.TrHarga;
import entity.TrKurir;
import entity.TrPelanggan;
import entity.TrPerwakilan;
import entity.TrUser;
import entity.TtDataEntry;
import entity.TtHeader;
import entity.TtPotoTimbang;
import entity.TtStatusKurirIn;
import entity.TtStatusKurirOut;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import net.sf.jasperreports.engine.util.MessageUtil;
import service.BrowseSemuaDataService;
import service.PelangganService;
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
	
	// TABEL KURIR	
	List<TrKurir> trKurirHO = new ArrayList<TrKurir>();
	List<TrKurir> trKurirCabang = new ArrayList<TrKurir>();
	
	// TABEL HARGA
	List<TrHarga> trHargaHO = new ArrayList<TrHarga>();
	List<TrHarga> trHargaCabang = new ArrayList<TrHarga>();
	
	// TABEL PERWAKILAN
	List<TrPerwakilan> trPerwakilanHO = new ArrayList<TrPerwakilan>();
	List<TrPerwakilan> trPerwakilanCabang = new ArrayList<TrPerwakilan>();
	
	// TABEL USER
	List<TrUser> trUserHO = new ArrayList<TrUser>();
	List<TrUser> trUserCabang = new ArrayList<TrUser>();
	
	// TABEL CABANG
	List<TrCabang> trCabangHO = new ArrayList<TrCabang>();
	List<TrCabang> trCabangCabang = new ArrayList<TrCabang>();
	
	// list untuk di lempar dari cabang ke HO
	List<TtHeader> ttHeaderCabangHOFilteredHO = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryCabangHOFilteredHO = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangCabangHOFilteredHO = new ArrayList<TtPotoTimbang>();
		
	List<TtHeader> ttHeaderCabangHOFilteredCabang = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryCabangHOFilteredCabang = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangCabangHOFilteredCabang = new ArrayList<TtPotoTimbang>();
		
//	List<TrPelanggan> trPelangganCabangHOFilteredHO = new ArrayList<TrPelanggan>();
//	List<TrPelanggan> trPelangganCabangHOFilteredCabang = new ArrayList<TrPelanggan>();
		
	List<TrKurir> trKurirCabangHOFilteredHO = new ArrayList<TrKurir>();
	List<TrKurir> trKurirCabangHOFilteredCabang = new ArrayList<TrKurir>();
	
	// list untuk dilempar dari HO ke cabang
	List<TtHeader> ttHeaderHOCabangFilteredHO = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryHOCabangFilteredHO = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangHOCabangFilteredHO = new ArrayList<TtPotoTimbang>();
		
	List<TtHeader> ttHeaderHOCabangFilteredCabang = new ArrayList<TtHeader>();
	List<TtDataEntry> ttDataEntryHOCabangFilteredCabang = new ArrayList<TtDataEntry>();
	List<TtPotoTimbang> ttPotoTimbangHOCabangFilteredCabang = new ArrayList<TtPotoTimbang>();
		
	List<TrPelanggan> trPelangganHOCabangFilteredHO = new ArrayList<TrPelanggan>();
	List<TrPelanggan> trPelangganHOCabangFilteredCabang = new ArrayList<TrPelanggan>();
	
	List<TrKurir> trKurirHOCabangFilteredHO = new ArrayList<TrKurir>();
	List<TrKurir> trKurirHOCabangFilteredCabang = new ArrayList<TrKurir>();
	
	List<TrHarga> trHargaHOCabangFilteredHO = new ArrayList<TrHarga>();
	List<TrHarga> trHargaHOCabangFilteredCabang = new ArrayList<TrHarga>();
	
	List<TrPerwakilan> trPerwakilanHOCabangFilteredHO = new ArrayList<TrPerwakilan>();
	List<TrPerwakilan> trPerwakilanHOCabangFilteredCabang = new ArrayList<TrPerwakilan>();
	
	List<TrUser> trUserHOCabangFilteredHO = new ArrayList<TrUser>();
	List<TrUser> trUserHOCabangFilteredCabang = new ArrayList<TrUser>();
	
	List<TrCabang> trCabangHOCabangFilteredHO = new ArrayList<TrCabang>();
	List<TrCabang> trCabangHOCabangFilteredCabang = new ArrayList<TrCabang>();
	
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
		// TABEL KURIR
		trKurirHO = SyncService.loadItemsFromHO(TrKurir.class, null, null);
		trKurirCabang = SyncService.loadItemsFromCabang(TrKurir.class, null, null);
		// TABEL HARGA
		trHargaHO = SyncService.loadItemsFromHO(TrHarga.class, null, null);
		trHargaCabang = SyncService.loadItemsFromCabang(TrHarga.class, null, null);
		// TABEL PERWAKILAN
		trPerwakilanHO = SyncService.loadItemsFromHO(TrPerwakilan.class, null, null);
		trPerwakilanCabang = SyncService.loadItemsFromCabang(TrPerwakilan.class, null, null);
		// TABEL USER
		trUserHO = SyncService.loadItemsFromHO(TrUser.class, null, null);
		trUserCabang = SyncService.loadItemsFromCabang(TrUser.class, null, null);
		// TABEL CABANG
		trCabangHO = SyncService.loadItemsFromHO(TrCabang.class, null, null);
		trCabangCabang = SyncService.loadItemsFromCabang(TrCabang.class, null, null);
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
		List<TrKurir> lstInsertKurirCabang = new ArrayList<TrKurir>();
		List<TrHarga> lstInsertHargaCabang = new ArrayList<TrHarga>();
		List<TrPerwakilan> lstInsertPerwakilanCabang = new ArrayList<TrPerwakilan>();
		List<TrUser> lstInsertUserCabang = new ArrayList<TrUser>();
		List<TrCabang> lstInsertCabangCabang = new ArrayList<TrCabang>();
		
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
		SyncService.clearPelangganCabang();
		for (TrPelanggan tt : trPelangganHO) {
				lstInsertPelangganCabang.add(tt);
				SyncService.addToCabangSaveOrUpdate(tt, TrPelanggan.class);
			bol = false;
		}
		for(TrKurir tt : trKurirHO){
			for(TrKurir ss : trKurirCabang){
				if(tt.getNik().toUpperCase().equals(ss.getNik().toUpperCase()))bol=true;
			}
			if(!bol){
				lstInsertKurirCabang.add(tt);
				SyncService.addToCabang(tt, TrKurir.class);
			}
			bol = false;
		}
		SyncService.clearHargaCabang();
		for(TrHarga tt : trHargaHO){
			for(TrHarga ss : trHargaCabang){
				
				String zonaHO = tt.getZona()==null?"":tt.getZona();
				String zonaCabang = ss.getZona()==null?"":ss.getZona();
				String propinsiHO = tt.getPropinsi()==null?"":tt.getPropinsi();
				String propinsiCabang = ss.getPropinsi()==null?"":ss.getPropinsi();
				String kabupatenHO = tt.getKabupaten()==null?"":tt.getKabupaten();
				String kabupatenCabang = ss.getKabupaten()==null?"":ss.getKabupaten();
				String kecamatanHO = tt.getKecamatan()==null?"":tt.getKecamatan();
				String kecamatanCabang = ss.getKecamatan()==null?"":ss.getKecamatan();
				String kodePerwakilanHO = tt.getKodePerwakilan()==null?"":tt.getKodePerwakilan();
				String kodePerwakilanCabang = ss.getKodePerwakilan()==null?"":ss.getKodePerwakilan();
				String etdHO = tt.getEtd()==null?"":tt.getEtd();
				String etdCabang = ss.getEtd()==null?"":ss.getEtd();
				if(tt.getPk().getKodeZona().equalsIgnoreCase(ss.getPk().getKodeZona()) 
						&& tt.getPk().getKodeAsal().equalsIgnoreCase(ss.getPk().getKodeAsal())
						&&propinsiHO.equalsIgnoreCase(propinsiCabang)
						&&kabupatenHO.equalsIgnoreCase(kabupatenCabang)
						&&kecamatanHO.equalsIgnoreCase(kecamatanCabang)
						&&kodePerwakilanHO.equalsIgnoreCase(kodePerwakilanCabang)
						&&zonaHO.equalsIgnoreCase(zonaCabang)
						&&tt.getReg().equals(ss.getReg())
						&&etdHO.equalsIgnoreCase(etdCabang)
						&&tt.getOne().equals(ss.getOne())
						&&tt.getTglCreate().equals(ss.getTglCreate())
						&&tt.getTglUpdate().equals(ss.getTglUpdate())
						&&tt.getFlag().equals(ss.getFlag()))bol=true;
			}
			if(!bol){
				lstInsertHargaCabang.add(tt);
				SyncService.addToCabangSaveOrUpdate(tt, TrHarga.class);
			}
			bol = false;
		}
		SyncService.clearPerwakilanCabang();
		for(TrPerwakilan tt : trPerwakilanHO){
			for(TrPerwakilan ss : trPerwakilanCabang){
				String zonaHO = tt.getZona()==null?"":tt.getZona();
				String zonaCabang = ss.getZona()==null?"":ss.getZona();
				
				String propinsiHO = tt.getPropinsi()==null?"":tt.getPropinsi();
				String propinsiCabang = ss.getPropinsi()==null?"":ss.getPropinsi();
				
				String kabupatenHO = tt.getKabupaten()==null?"":tt.getKabupaten();
				String kabupatenCabang = ss.getKabupaten()==null?"":ss.getKabupaten();
				
				String kecamatanHO = tt.getKecamatan()==null?"":tt.getKecamatan();
				String kecamatanCabang = ss.getKecamatan()==null?"":ss.getKecamatan();
				
				String kodePerwakilanHO = tt.getKodePerwakilan()==null?"":tt.getKodePerwakilan();
				String kodePerwakilanCabang = ss.getKodePerwakilan()==null?"":ss.getKodePerwakilan();
				
				String regPerwakilanHO = tt.getRegperwakilan()==null?"":tt.getRegperwakilan();
				String regPerwakilanCabang = ss.getRegperwakilan()==null?"":ss.getRegperwakilan();
				
				String onePerwakilanHO = tt.getOneperwakilan()==null?"":tt.getOneperwakilan();
				String onePerwakilanCabang = ss.getOneperwakilan()==null?"":ss.getOneperwakilan();
				
				if(tt.getKodeZona().equalsIgnoreCase(ss.getKodeZona()) 
						&&propinsiHO.equalsIgnoreCase(propinsiCabang)
						&&kabupatenHO.equalsIgnoreCase(kabupatenCabang)
						&&kecamatanHO.equalsIgnoreCase(kecamatanCabang)
						&&kodePerwakilanHO.equalsIgnoreCase(kodePerwakilanCabang)
						&&zonaHO.equalsIgnoreCase(zonaCabang)
						&&regPerwakilanHO.equalsIgnoreCase(regPerwakilanCabang)
						&&onePerwakilanHO.equalsIgnoreCase(onePerwakilanCabang)
						&&tt.getTglCreate().equals(ss.getTglCreate())
						&&tt.getTglUpdate().equals(ss.getTglUpdate())
						&&tt.getFlag().equals(ss.getFlag()))bol=true;
			}
			if(!bol){
				lstInsertPerwakilanCabang.add(tt);
				SyncService.addToCabangSaveOrUpdate(tt, TrPerwakilan.class);
			}
			bol = false;
		}
		SyncService.clearUserCabang();
		for(TrUser tt : trUserHO){
			for(TrUser ss : trUserCabang){
				String idUserHO = tt.getIdUser()==null?"":tt.getIdUser();
				String idUserCabang = ss.getIdUser()==null?"":ss.getIdUser();
				String namaUserHO = tt.getNamaUser()==null?"":tt.getNamaUser();
				String namaUserCabang = ss.getNamaUser()==null?"":ss.getNamaUser();
				String emailHO = tt.getEmail()==null?"":tt.getEmail();
				String emailCabang = ss.getEmail()==null?"":ss.getEmail();
				String passwordHO = tt.getPassword()==null?"":tt.getPassword();
				String passwordCabang = ss.getPassword()==null?"":ss.getPassword();
				Integer statusHO = tt.getStatus()==null?0:tt.getStatus();
				Integer statusCabang = ss.getStatus()==null?0:ss.getStatus();
				String kodeCabangHO = tt.getKodeCabang()==null?"":tt.getKodeCabang();
				String kodeCabangCabang = ss.getKodeCabang()==null?"":ss.getKodeCabang();
				Integer flagHO = tt.getFlag()==null?0:tt.getFlag();
				Integer flagCabang = ss.getFlag()==null?0:ss.getFlag();
				String idRoleHO = tt.getIdRole()==null?"":tt.getIdRole();
				String idRoleCabang = ss.getIdRole()==null?"":ss.getIdRole();
				if(
						idUserHO.equals(idUserCabang)
						&&namaUserHO.equals(namaUserCabang)
						&&emailHO.equals(emailCabang)
						&&passwordHO.equals(passwordCabang)
						&&statusHO.equals(statusCabang)
						&&kodeCabangHO.equals(kodeCabangCabang)
						&&flagHO.equals(flagCabang)
						&&idRoleHO.equals(idRoleCabang))bol=true;
			if(!bol){
				lstInsertUserCabang.add(tt);
				SyncService.addToCabangSaveOrUpdate(tt, TrUser.class);
			}
			bol = false;
			}
		}
		SyncService.clearCabangCabang();
		for(TrCabang tt : trCabangHO){
			for(TrCabang ss : trCabangCabang){
				
				String kodeCabangHO = tt.getKodeCabang()==null?"":tt.getKodeCabang();
				String kodeCabangCabang = ss.getKodeCabang()==null?"":ss.getKodeCabang();
				String kodePropinsiHO = tt.getKodePropinsi()==null?"":tt.getKodePropinsi();
				String kodePropinsiCabang = ss.getKodePropinsi()==null?"":ss.getKodePropinsi();
				String kodePerwakilanHO = tt.getKodePerwakilan()==null?"":tt.getKodePerwakilan();
				String kodePerwakilanCabang = ss.getKodePerwakilan()==null?"":ss.getKodePerwakilan();
				String emailHO = tt.getEmail()==null?"":tt.getEmail();
				String emailCabang = ss.getEmail()==null?"":ss.getEmail();
				String namaCabangHO = tt.getNamaCabang()==null?"":tt.getNamaCabang();
				String namaCabangCabang = ss.getNamaCabang()==null?"":ss.getNamaCabang();
				
				if(
						kodeCabangHO.equals(kodeCabangCabang)
						&&kodePropinsiHO.equals(kodePropinsiCabang)
						&&kodePerwakilanHO.equals(kodePerwakilanCabang)
						&&emailHO.equals(emailCabang)
						&&namaCabangHO.equals(namaCabangCabang))
				bol=true;
			if(!bol){
				lstInsertCabangCabang.add(tt);
				SyncService.addToCabangSaveOrUpdate(tt, TrCabang.class);
			}
			bol = false;
			}
		}
	}

	private void executeCabangToHo(Integer number) {
		// TABEL TRANSAKSI
		List<TtHeader> lstInsertHeaderHO = new ArrayList<TtHeader>();
		List<TtDataEntry> lstInsertDataEntryHO = new ArrayList<TtDataEntry>();
		List<TtPotoTimbang> lstInsertPotoTimbangHO = new ArrayList<TtPotoTimbang>();
		
		// TABEL KURIR IN OUT
		List<TtStatusKurirIn> lstInsertStatusKurirInHO = new ArrayList<TtStatusKurirIn>();
		List<TtStatusKurirOut> lstInsertStatusKurirOutHO = new ArrayList<TtStatusKurirOut>();
		
		// TABEL PELANGGAN
		List<TrPelanggan> lstInsertPelangganHO = new ArrayList<TrPelanggan>();
		
		// TABEL KURIR
		List<TrKurir> lstInsertKurirHO = new ArrayList<TrKurir>();
		
		Boolean bol = false;
		for (TtHeader tt : ttHeaderCabangHOFilteredCabang) {			
			for (TtHeader ss : ttHeaderCabangHOFilteredHO) {
				if(tt.getAwbHeader().equals(ss.getAwbHeader()))bol=true;
			}
			if(!bol){
				lstInsertHeaderHO.add(tt);
//				SyncService.addToHO(tt, TtHeader.class);
			}
			bol = false;
		}
		for (TtDataEntry tt : ttDataEntryCabangHOFilteredCabang) {
			for (TtDataEntry ss : ttDataEntryCabangHOFilteredHO) {
				if(tt.getAwbDataEntry().equals(ss.getAwbDataEntry()))bol=true;
			}
			if(!bol){
				lstInsertDataEntryHO.add(tt);
//				SyncService.addToHO(tt, TtDataEntry.class);
			}
			bol = false;
		}
		for (TtPotoTimbang tt : ttPotoTimbangCabangHOFilteredCabang) {
			for (TtPotoTimbang ss : ttPotoTimbangCabangHOFilteredHO) {
				if(tt.getAwbPotoTimbang().equals(ss.getAwbPotoTimbang()))bol=true;
			}
			if(!bol){
				lstInsertPotoTimbangHO.add(tt);
//				SyncService.addToHO(tt, TtPotoTimbang.class);
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
//				SyncService.addToHO(tt, TtStatusKurirIn.class);
			}
			bol = false;
		}	
		for (TtStatusKurirOut tt : ttStatusKurirOutCabang) {
			for (TtStatusKurirOut ss : ttStatusKurirOutFilteredCabang) {
				if(tt.getIdBarang().equals(ss.getIdBarang())&&tt.getStatus().equals(ss.getStatus()))bol=true;
			}
			if(!bol){
				lstInsertStatusKurirOutHO.add(tt);
//				SyncService.addToHO(tt, TtStatusKurirOut.class);
			}
			bol = false;
		}		
//		for(TrKurir tt : trKurirCabang){
//			for(TrKurir ss : trKurirCabangHOFilteredHO){
//				if(tt.getNik().toUpperCase().equals(ss.getNik().toUpperCase()))bol=true;
//			}
//			if(!bol){
//				lstInsertKurirHO.add(tt);
//				SyncService.addToHO(tt, TrKurir.class);
//			}
//			bol = false;
//		}
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

		// TABEL PELANGGAN TIDAK DIARAHKAN BALIK KE HO
//		// tabel pelanggan
//		trPelangganCabangHOFilteredHO.clear();
//		for(TrPelanggan tt : trPelangganCabang){
//			for(TrPelanggan ss : trPelangganHO){
//				if(tt.getKodePelanggan().toUpperCase().equals(ss.getKodePelanggan().toUpperCase())){
//					trPelangganCabangHOFilteredHO.add(tt);
//				}
//			}
//		}
		// TABEL KURIR TIDAK DIARAHKAN BALIK KE HO
//		// tabel kurir
//		trKurirCabangHOFilteredHO.clear();
//		for(TrKurir tt : trKurirCabang){
//			for(TrKurir ss : trKurirHO){
//				if(tt.getNik().toUpperCase().equals(ss.getNik().toUpperCase())){
//					trKurirCabangHOFilteredHO.add(tt);
//				}
//			}
//		}
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
			
				String kodePelangganHO = tt.getKodePelanggan()==null?"":tt.getKodePelanggan();
				String kodePelangganCabang = ss.getKodePelanggan()==null?"":ss.getKodePelanggan();
				String namaAkunHO = tt.getNamaAkun()==null?"":tt.getNamaAkun();
				String namaAkunCabang = ss.getNamaAkun()==null?"":ss.getNamaAkun();
				String namaPemilikHO = tt.getNamaPemilik()==null?"":tt.getNamaPemilik();
				String namaPemilikCabang = ss.getNamaPemilik()==null?"":ss.getNamaPemilik();
				String keteranganHO = tt.getKeterangan()==null?"":tt.getKeterangan();
				String keteranganCabang = ss.getKeterangan()==null?"":ss.getKeterangan();
				Integer diskonRapidHO = tt.getDiskonRapid()==null?0:tt.getDiskonRapid();
				Integer diskonRapidCabang = ss.getDiskonRapid()==null?0:ss.getDiskonRapid();
				Integer diskonJneHO = tt.getDiskonJne()==null?0:tt.getDiskonJne();
				Integer diskonJneCabang = ss.getDiskonJne()==null?0:ss.getDiskonJne();
				Date tglMulaiDiskonHO = tt.getTglMulaiDiskon()==null?new Date():tt.getTglMulaiDiskon();
				Date tglMulaiDiskonCabang = ss.getTglMulaiDiskon()==null?new Date():ss.getTglMulaiDiskon();
				Date tglAkhirDiskonHO = tt.getTglAkhirDiskon()==null?new Date():tt.getTglAkhirDiskon();
				Date tglAkhirDiskonCabang = ss.getTglAkhirDiskon()==null?new Date():ss.getTglAkhirDiskon();
				String smsHO = tt.getSms()==null?"":tt.getSms();
				String smsCabang = ss.getSms()==null?"":ss.getSms();
				String namaSalesHO = tt.getNamaSales()==null?"":tt.getNamaSales();
				String namaSalesCabang = ss.getNamaSales()==null?"":ss.getNamaSales();
				String referensiHO = tt.getReferensi()==null?"":tt.getReferensi();
				String referensiCabang = ss.getReferensi()==null?"":ss.getReferensi();
				Date tglGabungHO = tt.getTglGabung()==null?new Date():tt.getTglGabung();
				Date tglGabungCabang = ss.getTglGabung()==null?new Date():ss.getTglGabung();
				Integer flagHO = tt.getFlag()==null?0:tt.getFlag();
				Integer flagCabang = ss.getFlag()==null?0:ss.getFlag();
				String jabatan1HO = tt.getJabatan1()==null?"":tt.getJabatan1();
				String jabatan1Cabang = ss.getJabatan1()==null?"":ss.getJabatan1();
				String jabatan2HO = tt.getJabatan2()==null?"":tt.getJabatan2();
				String jabatan2Cabang = ss.getJabatan2()==null?"":ss.getJabatan2();
				
				if(kodePelangganHO.equals(kodePelangganCabang)
						&&namaAkunHO.equalsIgnoreCase(namaAkunCabang)
						&&namaPemilikHO.equalsIgnoreCase(namaPemilikCabang)
						&&keteranganHO.equalsIgnoreCase(keteranganCabang)
						&&diskonRapidHO.equals(diskonRapidCabang)
						&&diskonJneHO.equals(diskonJneCabang)
						&&tglMulaiDiskonHO.equals(tglMulaiDiskonCabang)
						&&tglAkhirDiskonHO.equals(tglAkhirDiskonCabang)
						&&smsHO.equalsIgnoreCase(smsCabang)
						&&namaSalesHO.equalsIgnoreCase(namaSalesCabang)
						&&referensiHO.equalsIgnoreCase(referensiCabang)
						&&tglGabungHO.equals(tglGabungCabang)
						&&flagHO.equals(flagCabang)
						&&jabatan1HO.equalsIgnoreCase(jabatan1Cabang)
						&&jabatan2HO.equalsIgnoreCase(jabatan2Cabang)
						){
					trPelangganHOCabangFilteredCabang.add(ss);
				}
			}
		}
		trKurirHOCabangFilteredCabang.clear();
		for(TrKurir tt : trKurirHO){
			for(TrKurir ss : trKurirCabang){
				if(tt.getNik().toUpperCase().equals(ss.getNik().toUpperCase())){
					trKurirHOCabangFilteredCabang.add(ss);
				}
			}
		}
		trHargaHOCabangFilteredCabang.clear();
		for(TrHarga tt : trHargaHO){
			for(TrHarga ss : trHargaCabang){
				String zonaHO = tt.getZona()==null?"":tt.getZona();
				String zonaCabang = ss.getZona()==null?"":ss.getZona();
				String propinsiHO = tt.getPropinsi()==null?"":tt.getPropinsi();
				String propinsiCabang = ss.getPropinsi()==null?"":ss.getPropinsi();
				String kabupatenHO = tt.getKabupaten()==null?"":tt.getKabupaten();
				String kabupatenCabang = ss.getKabupaten()==null?"":ss.getKabupaten();
				String kecamatanHO = tt.getKecamatan()==null?"":tt.getKecamatan();
				String kecamatanCabang = ss.getKecamatan()==null?"":ss.getKecamatan();
				String kodePerwakilanHO = tt.getKodePerwakilan()==null?"":tt.getKodePerwakilan();
				String kodePerwakilanCabang = ss.getKodePerwakilan()==null?"":ss.getKodePerwakilan();
				String etdHO = tt.getEtd()==null?"":tt.getEtd();
				String etdCabang = ss.getEtd()==null?"":ss.getEtd();

				if(tt.getPk().getKodeZona().equalsIgnoreCase(ss.getPk().getKodeZona()) 
					&& tt.getPk().getKodeAsal().equalsIgnoreCase(ss.getPk().getKodeAsal())
					&&propinsiHO.equalsIgnoreCase(propinsiCabang)
					&&kabupatenHO.equalsIgnoreCase(kabupatenCabang)
					&&kecamatanHO.equalsIgnoreCase(kecamatanCabang)
					&&kodePerwakilanHO.equalsIgnoreCase(kodePerwakilanCabang)
					&&zonaHO.equalsIgnoreCase(zonaCabang)
					&&tt.getReg().equals(ss.getReg())
					&&etdHO.equalsIgnoreCase(etdCabang)
					&&tt.getOne().equals(ss.getOne())
					&&tt.getTglCreate().equals(ss.getTglCreate())
					&&tt.getTglUpdate().equals(ss.getTglUpdate())
					&&tt.getFlag().equals(ss.getFlag())){
					trHargaHOCabangFilteredCabang.add(ss);
				}
			}
		}
		trPerwakilanHOCabangFilteredCabang.clear();
		for(TrPerwakilan tt : trPerwakilanHO){
			for(TrPerwakilan ss : trPerwakilanCabang){
				String zonaHO = tt.getZona()==null?"":tt.getZona();
				String zonaCabang = ss.getZona()==null?"":ss.getZona();
				
				String propinsiHO = tt.getPropinsi()==null?"":tt.getPropinsi();
				String propinsiCabang = ss.getPropinsi()==null?"":ss.getPropinsi();
				
				String kabupatenHO = tt.getKabupaten()==null?"":tt.getKabupaten();
				String kabupatenCabang = ss.getKabupaten()==null?"":ss.getKabupaten();
				
				String kecamatanHO = tt.getKecamatan()==null?"":tt.getKecamatan();
				String kecamatanCabang = ss.getKecamatan()==null?"":ss.getKecamatan();
				
				String kodePerwakilanHO = tt.getKodePerwakilan()==null?"":tt.getKodePerwakilan();
				String kodePerwakilanCabang = ss.getKodePerwakilan()==null?"":ss.getKodePerwakilan();
				
				String regPerwakilanHO = tt.getRegperwakilan()==null?"":tt.getRegperwakilan();
				String regPerwakilanCabang = ss.getRegperwakilan()==null?"":ss.getRegperwakilan();
				
				String onePerwakilanHO = tt.getOneperwakilan()==null?"":tt.getOneperwakilan();
				String onePerwakilanCabang = ss.getOneperwakilan()==null?"":ss.getOneperwakilan();
				
				if(tt.getKodeZona().equalsIgnoreCase(ss.getKodeZona()) 
					&&propinsiHO.equalsIgnoreCase(propinsiCabang)
					&&kabupatenHO.equalsIgnoreCase(kabupatenCabang)
					&&kecamatanHO.equalsIgnoreCase(kecamatanCabang)
					&&kodePerwakilanHO.equalsIgnoreCase(kodePerwakilanCabang)
					&&zonaHO.equalsIgnoreCase(zonaCabang)
					&&regPerwakilanHO.equalsIgnoreCase(regPerwakilanCabang)
					&&onePerwakilanHO.equalsIgnoreCase(onePerwakilanCabang)
					&&tt.getFlag().equals(ss.getFlag())){
						trPerwakilanHOCabangFilteredCabang.add(ss);
				}
			}
		}
		trUserHOCabangFilteredCabang.clear();
		for(TrUser tt : trUserHO){
			for(TrUser ss : trUserCabang){
			
				String idUserHO = tt.getIdUser()==null?"":tt.getIdUser();
				String idUserCabang = ss.getIdUser()==null?"":ss.getIdUser();
				String namaUserHO = tt.getNamaUser()==null?"":tt.getNamaUser();
				String namaUserCabang = ss.getNamaUser()==null?"":ss.getNamaUser();
				String emailHO = tt.getEmail()==null?"":tt.getEmail();
				String emailCabang = ss.getEmail()==null?"":ss.getEmail();
				String passwordHO = tt.getPassword()==null?"":tt.getPassword();
				String passwordCabang = ss.getPassword()==null?"":ss.getPassword();
				Integer statusHO = tt.getStatus()==null?0:tt.getStatus();
				Integer statusCabang = ss.getStatus()==null?0:ss.getStatus();
				String kodeCabangHO = tt.getKodeCabang()==null?"":tt.getKodeCabang();
				String kodeCabangCabang = ss.getKodeCabang()==null?"":ss.getKodeCabang();
				Integer flagHO = tt.getFlag()==null?0:tt.getFlag();
				Integer flagCabang = ss.getFlag()==null?0:ss.getFlag();
				String idRoleHO = tt.getIdRole()==null?"":tt.getIdRole();
				String idRoleCabang = ss.getIdRole()==null?"":ss.getIdRole();
				if(
						idUserHO.equals(idUserCabang)
						&&namaUserHO.equals(namaUserCabang)
						&&emailHO.equals(emailCabang)
						&&passwordHO.equals(passwordCabang)
						&&statusHO.equals(statusCabang)
						&&kodeCabangHO.equals(kodeCabangCabang)
						&&flagHO.equals(flagCabang)
						&&idRoleHO.equals(idRoleCabang)
						){
					trUserHOCabangFilteredCabang.add(ss);
				}
			}
		}
		trCabangHOCabangFilteredCabang.clear();
		for(TrCabang tt : trCabangHO){
			for(TrCabang ss : trCabangCabang){
				String kodeCabangHO = tt.getKodeCabang()==null?"":tt.getKodeCabang();
				String kodeCabangCabang = ss.getKodeCabang()==null?"":ss.getKodeCabang();
				String kodePropinsiHO = tt.getKodePropinsi()==null?"":tt.getKodePropinsi();
				String kodePropinsiCabang = ss.getKodePropinsi()==null?"":ss.getKodePropinsi();
				String kodePerwakilanHO = tt.getKodePerwakilan()==null?"":tt.getKodePerwakilan();
				String kodePerwakilanCabang = ss.getKodePerwakilan()==null?"":ss.getKodePerwakilan();
				String emailHO = tt.getEmail()==null?"":tt.getEmail();
				String emailCabang = ss.getEmail()==null?"":ss.getEmail();
				String namaCabangHO = tt.getNamaCabang()==null?"":tt.getNamaCabang();
				String namaCabangCabang = ss.getNamaCabang()==null?"":ss.getNamaCabang();
				
				if(
						kodeCabangHO.equals(kodeCabangCabang)
						&&kodePropinsiHO.equals(kodePropinsiCabang)
						&&kodePerwakilanHO.equals(kodePerwakilanCabang)
						&&emailHO.equals(emailCabang)
						&&namaCabangHO.equals(namaCabangCabang)){
					trCabangHOCabangFilteredCabang.add(ss);
				}
			}
		}
	}
	
	private void loadTableTransaksi() {
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
		ObservableList<SyncTV> pops = FXCollections.observableArrayList();
		SyncTV ttHeader = new SyncTV(1, "TT_HEADER", ttHeaderCabangHOFilteredHO.size() + " of " + ttHeaderCabangHOFilteredCabang.size(), ttHeaderHOCabangFilteredCabang.size() + " of " + ttHeaderHOCabangFilteredHO.size());
		SyncTV ttDataEntry = new SyncTV(2, "TT_DATA_ENTRY", ttDataEntryCabangHOFilteredHO.size() + " of " + ttDataEntryCabangHOFilteredCabang.size(), ttDataEntryHOCabangFilteredCabang.size() + " of " + ttDataEntryHOCabangFilteredHO.size());
		SyncTV ttPotoTimbang = new SyncTV(3, "TT_POTO_TIMBANG", ttPotoTimbangCabangHOFilteredHO.size() + " of " + ttPotoTimbangCabangHOFilteredCabang.size(), ttPotoTimbangHOCabangFilteredCabang.size() + " of " + ttPotoTimbangHOCabangFilteredHO.size());
		
		SyncTV ttStatusKurirIn = new SyncTV(4, "TT_STATUS_KURIR_IN", ttStatusKurirInFilteredCabang.size() + " of " + ttStatusKurirInCabang.size(), "-");
		SyncTV ttStatusKurirOut = new SyncTV(5, "TT_STATUS_KURIR_OUT", ttStatusKurirOutFilteredCabang.size() + " of " + ttStatusKurirOutCabang.size(), "-");
		
		SyncTV trPelanggan = new SyncTV(6, "TR_PELANGGAN", "-", trPelangganHOCabangFilteredCabang.size() + " of " + trPelangganHO.size());
		
		SyncTV trKurir = new SyncTV(7, "TR_KURIR", "-", trKurirHOCabangFilteredCabang.size() + " of " + trKurirHO.size());
		
		SyncTV trHarga = new SyncTV(8, "TR_HARGA", "-", trHargaHOCabangFilteredCabang.size() + " of " + trHargaHO.size());
		
		SyncTV trPerwakilan = new SyncTV(9, "TR_PERWAKILAN", "-", trPerwakilanHOCabangFilteredCabang.size() + " of " + trPerwakilanHO.size());
		SyncTV trUser = new SyncTV(10, "TR_USER", "-", trUserHOCabangFilteredCabang.size() + " of " + trUserHO.size());
		
		SyncTV trCabang = new SyncTV(11, "TR_CABANG", "-", trCabangHOCabangFilteredCabang.size() + " of " + trCabangHO.size());
		
		pops.add(ttHeader);
		pops.add(ttDataEntry);
		pops.add(ttPotoTimbang);
		pops.add(ttStatusKurirIn);
		pops.add(ttStatusKurirOut);
		pops.add(trPelanggan);
		pops.add(trKurir);
		pops.add(trHarga);
		pops.add(trPerwakilan);
		pops.add(trUser);
		pops.add(trCabang);
		
		tblView.getColumns().clear();
		tblView.getItems().clear();
		
		tblView.setItems(pops);
		
		TableColumn<SyncTV, Integer> noCol = new TableColumn<SyncTV, Integer>("No");
		TableColumn<SyncTV, String> tabelCol = new TableColumn<SyncTV, String>("Nama Tabel");
		TableColumn<SyncTV, String> cabangToHOCol = new TableColumn<SyncTV, String>("Cabang To HO");
		TableColumn<SyncTV, String> HOToCabangCol = new TableColumn<SyncTV, String>("HO To Cabang");
		TableColumn actionCol = new TableColumn();
		Callback<TableColumn<SyncTV, String>, TableCell<SyncTV, String>> cellFactory = //
                new Callback<TableColumn<SyncTV, String>, TableCell<SyncTV, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<SyncTV, String> param ){
                        final TableCell<SyncTV, String> cell = new TableCell<SyncTV, String>(){

                            Button btn = new Button( "Sync" );
                            
                            @Override
                            public void updateItem( String item, boolean empty ){
                                super.updateItem( item, empty );
                                if ( empty ){setGraphic( null );setText( null );
                                }else{
                                	SyncTV row = getTableView().getItems().get( getIndex() );
                                	if(row.getNo()==1
                                			||row.getNo()==6
                                			||row.getNo()==7
                                			||row.getNo()==8
                                			||row.getNo()==10
                                			||row.getNo()==11
                                			){
                                		btn.setVisible(true);
                                	}else{
                                		btn.setVisible(false);
                                	}
                                    btn.setOnAction( ( ActionEvent event ) -> {
                                    	executeSync(row.getNo());
//                    						PelangganService.setDiskonFlag(row.getIdDiskon(),1);
//                    						PelangganService.updateWholeDataEntry(row.getIdDiskon());
//                    						PelangganService.updateDiskonMasterPelanggan(row.getKode(), row.getDiskonJNE(), row.getDiskonRapid());
//                    						loadRightList(tbl, kode);
	                                });
                                    setGraphic( btn );
                                    setText( null );
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionCol.setCellFactory( cellFactory );
		
		tblView.getColumns().addAll(noCol, tabelCol, cabangToHOCol, HOToCabangCol, actionCol);
		
		noCol.setCellValueFactory(new PropertyValueFactory<SyncTV, Integer>("no"));
		tabelCol.setCellValueFactory(new PropertyValueFactory<SyncTV, String>("table"));
		cabangToHOCol.setCellValueFactory(new PropertyValueFactory<SyncTV, String>("cabangToHo"));
		HOToCabangCol.setCellValueFactory(new PropertyValueFactory<SyncTV, String>("HOToCabang"));
		
		tblView.setEditable(true);
	}
		
	private void executeSync(Integer no) {
		switch(no){
			case 1:
				--
				break;
			case 6 :
				--
				break;
			case 7 :
				--
				break;
			case 8 :
				--
				break;
			case 10 :
				--
				break;
			case 11 :
				--
				break;
		}
	}
	
	private class ButtonCell extends TableCell<Record, Boolean> {

		TableView<SyncTV> tab;

		ButtonCell(final TableView<SyncTV> tblView) {
			this.tab = tblView;
		}

		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				int selectdIndex = getTableRow().getIndex();
				SyncTV boVo = (SyncTV) tab.getItems().get(selectdIndex);
				final HBox hbox = new HBox(5);
				Button button = new Button("Sync");
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						
					}
				});
				hbox.getChildren().add(button);
				setGraphic(hbox);
			}
		}
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
