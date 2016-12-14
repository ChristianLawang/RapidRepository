package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import entity.TrCabang;
import entity.TtDataEntry;
import entity.TtHeader;
import entity.TtPotoTimbang;
import entity.TtStatusKurirIn;
import entity.TtStatusKurirOut;
import util.HibernateUtil;
import util.PropertiesUtil;

public class SyncService {

	public static <T> List<T> loadItemsFromHO(Class<T> cls, Date start, Date end) {
		Session s=HibernateUtil.openSessionHO();
		Boolean isDateFiltered = start!=null||end!=null;
		Calendar calStart=Calendar.getInstance();
		Calendar calEnd=Calendar.getInstance();
		if(isDateFiltered){			
			calStart.setTime(start);
			calStart.set(Calendar.HOUR_OF_DAY, 0);
			calStart.set(Calendar.MINUTE, 0);
			calStart.set(Calendar.SECOND, 0);
			calStart.set(Calendar.MILLISECOND, 0);			
			
			calEnd.setTime(end);
			calEnd.set(Calendar.HOUR_OF_DAY, 23);
			calEnd.set(Calendar.MINUTE, 59);
			calEnd.set(Calendar.SECOND, 59);
			calEnd.set(Calendar.MILLISECOND, 59);
		}
		Criteria c=s.createCriteria(cls);
		if(isDateFiltered){
			c.add(Restrictions.between("tglCreate", calStart.getTime(), calEnd.getTime()));
		}	
		List<T> data =  c.list();
		
		s.getTransaction().commit();
		
		return data;			
	}

	public static <T> List<T> loadItemsFromCabang(Class<T> cls, Date start, Date end) {
		Session s=HibernateUtil.openSession();
		Boolean isDateFiltered = start!=null||end!=null;
		Calendar calStart=Calendar.getInstance();
		Calendar calEnd=Calendar.getInstance();
		if(isDateFiltered){			
			calStart.setTime(start);
			calStart.set(Calendar.HOUR_OF_DAY, 0);
			calStart.set(Calendar.MINUTE, 0);
			calStart.set(Calendar.SECOND, 0);
			calStart.set(Calendar.MILLISECOND, 0);
			
			calEnd.setTime(end);
			calEnd.set(Calendar.HOUR_OF_DAY, 23);
			calEnd.set(Calendar.MINUTE, 59);
			calEnd.set(Calendar.SECOND, 59);
			calEnd.set(Calendar.MILLISECOND, 59);
		}
		Criteria c=s.createCriteria(cls);
		if(isDateFiltered){
			c.add(Restrictions.between("tglCreate", calStart.getTime(), calEnd.getTime()));
		}
		List<T> data =  c.list();
		
		s.getTransaction().commit();
		
		return data;
	}

	public static <T> void addToHO(T data, Class<T> cls) {
		Session s=HibernateUtil.openSessionHO();
		s.save(data);
		s.getTransaction().commit();
	}

	public static <T> void addToCabang(T data, Class<T> cls) {
		Session s=HibernateUtil.openSession();
		s.save(data);
		s.getTransaction().commit();
		
	}
	
	// khusus tabel TtStatusKurirIn
	public static List<TtStatusKurirIn> getTtStatusKurirIn(Boolean isHO, Date dtStart, Date dtEnd){
		Session session;
		if(isHO){
			session=HibernateUtil.openSessionHO();
		}else{
			session=HibernateUtil.openSession();
		}
		String nativeSql = 
				" select * "
			  + " from tt_status_kurir_in"
			  + " where date(tgl_create) between '" + dtStart + " 00:00:00' and '" + dtEnd + " 23:59:59'";
		
		System.out.println("--> nativeSql : " + nativeSql);
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TtStatusKurirIn> returnList = new ArrayList<TtStatusKurirIn>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TtStatusKurirIn everyRow = new TtStatusKurirIn();
			everyRow.setId((String) row.get("id"));
			everyRow.setNoStrukKurirIn((String) row.get("NO_STRUK_KURIR_IN"));
			everyRow.setIdKurir((String) row.get("ID_KURIR"));
			everyRow.setStatus((String) row.get("STATUS"));
			everyRow.setPenerima((String) row.get("PENERIMA"));
			everyRow.setMasalah((String) row.get("MASALAH"));
			everyRow.setWaktuJam((String) row.get("WAKTU_JAM"));
			everyRow.setWaktuMenit((String) row.get("WAKTU_MENIT"));
			everyRow.setIdBarang((String) row.get("ID_BARANG"));
			everyRow.setTanggal((Date) row.get("TANGGAL"));
			everyRow.setTglCreate((Date) row.get("TGL_CREATE"));
			everyRow.setTglUpdate((Date) row.get("TGL_UPDATE"));
			everyRow.setFlag((Integer) row.get("FLAG"));
			returnList.add(everyRow);
		}
		return returnList;
	}
	// khusus tabel TtStatusKurirIn
	public static List<TtStatusKurirOut> getTtStatusKurirOut(Boolean isHO, Date dtStart, Date dtEnd){
		Session session;
		if(isHO){
			session=HibernateUtil.openSessionHO();
		}else{
			session=HibernateUtil.openSession();
		}
		String nativeSql = 
				" select * "
			  + " from tt_status_kurir_out"
			  + " where date(tgl_create) between '" + dtStart + " 00:00:00' and '" + dtEnd + " 23:59:59'";
		
		System.out.println("--> nativeSql : " + nativeSql);
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TtStatusKurirOut> returnList = new ArrayList<TtStatusKurirOut>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TtStatusKurirOut everyRow = new TtStatusKurirOut();
			everyRow.setId((String) row.get("id"));
			everyRow.setNoStrukKurirOut((String) row.get("NO_STRUK_KURIR_OUT"));
			everyRow.setIdKurir((String) row.get("ID_KURIR"));
			everyRow.setStatus((String) row.get("STATUS"));
			everyRow.setPenerima((String) row.get("PENERIMA"));
			everyRow.setMasalah((String) row.get("MASALAH"));
			everyRow.setWaktuJam((String) row.get("WAKTU_JAM"));
			everyRow.setWaktuMenit((String) row.get("WAKTU_MENIT"));
			everyRow.setIdBarang((String) row.get("ID_BARANG"));
			everyRow.setTanggal((Date) row.get("TANGGAL"));
			everyRow.setTglCreate((Date) row.get("TGL_CREATE"));
			everyRow.setTglUpdate((Date) row.get("TGL_UPDATE"));
			everyRow.setFlag((Integer) row.get("FLAG"));
			returnList.add(everyRow);
		}
		return returnList;
	}

	public static Map<String, Object> loadItemsTransactionalNative(
			Boolean isHO,
			Date dtStart,
			Date dtEnd) {
		Session session;
		if(isHO){
			session=HibernateUtil.openSessionHO();
		}else{
			session=HibernateUtil.openSession();
		}
		String nativeSql = 
				"select a.*, b.*, c.* " +
				"from tt_header a " +
				"inner join tt_poto_timbang b on a.awb_header = b.awb_poto_timbang " +
				"inner join tt_data_entry c on a.awb_header = c.awb_data_entry " +
				"where " +
				"     a.flag = 0 " +
				"and  a.gabung_paket_flag = 1 " +
				"and date(a.tgl_create) between '"+dtStart+" 00:00:00' and '"+dtEnd+" 23:59:59'";
		

		System.out.println("--> nativeSql : " + nativeSql);
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		List<TtHeader> header = new ArrayList<TtHeader>();	
		List<TtPotoTimbang> potoTimbang = new ArrayList<TtPotoTimbang>();
		List<TtDataEntry> dataEntry = new ArrayList<TtDataEntry>();
		for (Object obj : result) {
			Map row = (Map) obj;
			TtHeader headerRow = new TtHeader();
			TtPotoTimbang potoTimbangRow = new TtPotoTimbang();
			TtDataEntry dataEntryRow = new TtDataEntry();
			// tabel header
			headerRow.setAwbHeader((String) row.get("AWB_HEADER"));
			headerRow.setIdKeranjang((String) row.get("ID_KERANJANG"));
			headerRow.setIdUpload((Integer) row.get("ID_UPLOAD"));
			headerRow.setWkSortir((Date) row.get("WK_SORTIR"));
			headerRow.setSortirFlag((Integer) row.get("SORTIR_FLAG"));
			headerRow.setGabungPaketFlag((Integer) row.get("GABUNG_PAKET_FLAG"));
			headerRow.setIdKardus((String) row.get("ID_KARDUS"));
			headerRow.setInboundFlag((Integer) row.get("INBOUND_FLAG"));
			headerRow.setWkInbound((Date) row.get("WK_INBOUND"));
			headerRow.setBarangTerkirimFlag((Integer) row.get("BARANG_TERKIRIM_FLAG"));
			headerRow.setIdKurir((String) row.get("ID_KURIR"));
			headerRow.setSubmitFlag((Integer) row.get("SUBMIT_FLAG"));
			headerRow.setWaitingPendingFlag((Integer) row.get("WAITING_PENDING_FLAG"));
			headerRow.setUserCreate((String) row.get("USER_CREATE"));
			headerRow.setUserUpdate((String) row.get("USER_UPDATE"));
			headerRow.setResiJne((String) row.get("RESI_JNE"));
			headerRow.setTglCreate((Date) row.get("TGL_CREATE"));
			headerRow.setTglUpdate((Date) row.get("TGL_UPDATE"));
			headerRow.setFlag((Integer) row.get("FLAG"));
			headerRow.setUserInbound((String) row.get("USER_INBOUND"));
			// DATA ENTRY
			dataEntryRow.setAwbDataEntry((String) row.get("AWB_DATA_ENTRY"));
			dataEntryRow.setAsalPaket((String) row.get("ASAL_PAKET"));
			dataEntryRow.setPengirim((String) row.get("PENGIRIM"));
			dataEntryRow.setPenerima((String) row.get("PENERIMA"));
			dataEntryRow.setTujuan((String) row.get("TUJUAN"));
			dataEntryRow.setTelpPenerima((String) row.get("TELP_PENERIMA"));
			dataEntryRow.setReseller((String) row.get("RESELLER"));
			dataEntryRow.setKeterangan((String) row.get("KETERANGAN"));
			dataEntryRow.setBclose((String) row.get("BCLOSE"));
			dataEntryRow.setPbclose((String) row.get("PBCLOSE"));
			dataEntryRow.setHarga((Integer) row.get("HARGA"));
			dataEntryRow.setBiaya((Integer) row.get("BIAYA"));
			dataEntryRow.setKodePerwakilan((String) row.get("KODE_PERWAKILAN"));
			dataEntryRow.setJneFlag((Integer) row.get("JNE_FLAG"));
			dataEntryRow.setAsuransi((Integer) row.get("ASURANSI"));
			dataEntryRow.setTotalDiskon((Integer) row.get("TOTAL_DISKON"));
			dataEntryRow.setTotalBiaya((Integer) row.get("TOTAL_BIAYA"));
			dataEntryRow.setUser((String) row.get("USER"));
			dataEntryRow.setFlagEntry((Integer) row.get("FLAG_ENTRY"));
			dataEntryRow.setTglCreate((Date) row.get("TGL_CREATE"));
			dataEntryRow.setTglUpdate((Date) row.get("TGL_UPDATE"));
			dataEntryRow.setFlag((Integer) row.get("FLAG"));
			// poto timbang
			potoTimbangRow.setAwbPotoTimbang((String) row.get("AWB_POTO_TIMBANG"));
			potoTimbangRow.setAsalPaket((String) row.get("ASAL_PAKET"));
			potoTimbangRow.setKodePickup((String) row.get("KODE_PICKUP"));
			potoTimbangRow.setKodePelanggan((String) row.get("KODE_PELANGGAN"));
			potoTimbangRow.setAwbData((String) row.get("AWB_DATA"));
			potoTimbangRow.setVol((String) row.get("VOL"));
			potoTimbangRow.setBvol((String) row.get("BVOL"));
			potoTimbangRow.setBpvol((String) row.get("BPVOL"));
			potoTimbangRow.setTimb((String) row.get("TIMB"));
			potoTimbangRow.setBerattimb((String) row.get("BERATTIMB"));
			potoTimbangRow.setBclose((String) row.get("BCLOSE"));
			potoTimbangRow.setBpclose((String) row.get("BPCLOSE"));
			potoTimbangRow.setLayanan((String) row.get("LAYANAN"));
			potoTimbangRow.setKodePerwakilan((String) row.get("KODE_PERWAKILAN"));
			potoTimbangRow.setJneFlag((Integer) row.get("JNE_FLAG"));
			potoTimbangRow.setGambar((String) row.get("GAMBAR"));
			potoTimbangRow.setTglGambar((Date) row.get("TGL_GAMBAR"));
			potoTimbangRow.setKoli((Integer) row.get("KOLI"));
			potoTimbangRow.setUser((String) row.get("USER"));
			potoTimbangRow.setTglCreate((Date) row.get("TGL_CREATE"));
			potoTimbangRow.setTglUpdate((Date) row.get("TGL_UPDATE"));
			potoTimbangRow.setFlag((Integer) row.get("FLAG"));
			potoTimbangRow.setFlagLunas((Integer) row.get("FLAG_LUNAS"));
			
			header.add(headerRow);
			dataEntry.add(dataEntryRow);
			potoTimbang.add(potoTimbangRow);
		}
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("HEADER", header);
		mapResult.put("DATAENTRY", dataEntry);
		mapResult.put("POTOTIMBANG", potoTimbang);
		return mapResult;
	}
}
