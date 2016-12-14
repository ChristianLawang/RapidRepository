package service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import VO.EntryDataShowVO;
import entity.TtDataEntry;
import entity.TtPotoTimbang;
import util.DateUtil;
import util.HibernateUtil;

public class DataPaketService {
	
	//OK
	public static List<TtDataEntry> getBrowseDataEntry() {
		Session session = HibernateUtil.openSession();
		String nativeSql = " SELECT a.pengirim, count(a.pengirim) jumlah_pelanggan, a.tgl_create "
				+ " FROM tt_data_entry a where a.Flag_entry=0 "
//				+ "and substr(a.tgl_create, 1, 10) = "
//				+ " (select case "
//				+ " when DATE_FORMAT(now(),'%H:%i:%s') >  '00:00:00'  "
//				+ " and DATE_FORMAT(now(),'%H:%i:%s') <=  '11:00:00' "
//				+ " then date_add(curdate(), interval -1 day) else curdate() "
//				+ " end as time_zone from dual) "
				+ " group by a.pengirim";
		SQLQuery query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();

		List<TtDataEntry> returnList = new ArrayList<TtDataEntry>();
		for (Object obj : result) {
			Map row = (Map) obj;
			TtDataEntry everyRow = new TtDataEntry();

			everyRow.setPengirim((String) row.get("PENGIRIM"));

			BigInteger bgInt = getBigInteger(row.get("jumlah_pelanggan"));
			Integer test = bgInt.intValue();
			everyRow.setJumlahPelanggan(test);

			everyRow.setTglCreate((Date) row.get("TGL_CREATE"));

			returnList.add(everyRow);
		}
		return returnList;

	}

	//OK
	public static BigInteger getBigInteger(Object value) {
		BigInteger ret = null;
		if (value != null) {
			if (value instanceof BigInteger) {
				ret = (BigInteger) value;
			} else if (value instanceof String) {
				ret = new BigInteger((String) value);
			} else if (value instanceof BigDecimal) {
				ret = ((BigDecimal) value).toBigInteger();
			} else if (value instanceof Number) {
				ret = BigInteger.valueOf(((Number) value).longValue());
			} else {
				throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass()
						+ " into a BigInteger.");
			}
		}
		return ret;
	}

	// chris
	public static Integer getCount() {
		Session session = HibernateUtil.openSession();
		Criteria crit = session.createCriteria(TtDataEntry.class);
		crit.setProjection(Projections.rowCount());
		long lngCount = (long) crit.uniqueResult();
		session.getTransaction().commit();
		return (int) lngCount;
	}

	// chris
	public static void save(TtPotoTimbang potoPaket) {
		GenericService.save(TtPotoTimbang.class, potoPaket, true);
	}

	//OK
	public static Boolean updateClass(List<EntryDataShowVO> listData, String nmUser) {
		Session sess = HibernateUtil.openSession();
		int no = 1;
		for (EntryDataShowVO data : listData) {
			String sql = "UPDATE tt_data_entry SET USER=:pUser, FLAG_ENTRY=:pEntry WHERE awb_data_entry=:rId";
			Query query = sess.createSQLQuery(sql).setParameter("pUser", nmUser).setParameter("pEntry", 1)
					.setParameter("rId", data.getIdDataPaket());
			int result = query.executeUpdate();
		}
		sess.getTransaction().commit();
		return true;
	}

	//OK
	public static Boolean updateSaveEntry(TtDataEntry par) {
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE tt_data_entry SET PENERIMA=:pPenerima, tujuan=:pTujuan, TELP_PENERIMA=:pTlpPen, "
				+ "RESELLER=:pReseller, KETERANGAN=:pKet, ASURANSI=:pAsuransi, TOTAL_BIAYA=:pTBiaya, "
				+ "Flag_Entry=:pEntry, tgl_update=:pUpdate, biaya=:pBiaya, user=:pUser, harga=:pHarga, Kode_perwakilan=:PkdPerw "
				+ "WHERE awb_data_entry=:rId";
		Query query = sess.createSQLQuery(sql).setParameter("pPenerima", par.getPenerima())
				.setParameter("pTujuan", par.getTujuan()).setParameter("pTlpPen", par.getTelpPenerima())
				.setParameter("pReseller", par.getReseller()).setParameter("pKet", par.getKeterangan())
				.setParameter("pAsuransi", par.getAsuransi()).setParameter("pTBiaya", par.getTotalBiaya())
				.setParameter("pEntry", 2).setParameter("pUpdate", par.getTglUpdate())
				.setParameter("pBiaya", par.getBiaya()).setParameter("pUser", par.getUser())
				.setParameter("pHarga", par.getHarga()).setParameter("PkdPerw", par.getKodePerwakilan())
				.setParameter("rId", par.getAwbDataEntry());
		int result = query.executeUpdate();

		if (par.getKodePerwakilan().equals("JNE")) {
			String sql2 = "UPDATE tt_data_entry SET JNE_FLAG='1' WHERE awb_data_entry=:rIdPak";
			Query query2 = sess.createSQLQuery(sql2).setParameter("rIdPak", par.getAwbDataEntry());
			query2.executeUpdate();
		}
		sess.getTransaction().commit();
		return true;
	}
	
	//OK
	public static Boolean updateCancel(TtDataEntry par) {
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE tt_data_entry SET Flag_Entry=:pEntry " + "WHERE awb_data_entry=:rId";
		Query query = sess.createSQLQuery(sql).setParameter("pEntry", 0).setParameter("rId", par.getAwbDataEntry());
		int result = query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}

	//OK
	public static Boolean updateLayanan(String awb, Boolean one, Boolean reg, Boolean pending, String kdPerwakilan){
		String layanan = null;
		if(one){
			layanan = "ONE";
		}else if(pending){
			layanan = "XXX";
		}else{
			layanan = "REG";
		}
		if(one && reg){
			layanan = "ONE";
		}
		Session sess=HibernateUtil.openSession();
			String sql ="UPDATE tt_poto_timbang SET Layanan=:pLayanan, Kode_perwakilan=:pkdPerw "
					+ "WHERE Awb_poto_timbang=:rId";
			Query query = sess.createSQLQuery(sql)
					.setParameter("pLayanan", layanan)
					.setParameter("pkdPerw", kdPerwakilan) 
					.setParameter("rId", awb);
			int result = query.executeUpdate();
			sess.getTransaction().commit();
			return true;
	}

	//OK
	public static Boolean updateSkip(String idPaket) {
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE tt_data_entry set " + "Flag_Entry=0 " + "WHERE awb_data_entry=:rId";
		Query query = sess.createSQLQuery(sql).setParameter("rId", idPaket);
		query.executeUpdate();

		sess.getTransaction().commit();
		return true;
	}
	
	//OK
	public static List<EntryDataShowVO> getDataEntryFromPopup(String pengirim) {
		Session s = HibernateUtil.openSession();
		String sql = "select c.awb_data, c.kode_pickup, b.pengirim, c.bclose, c.bpclose, a.tgl_create,"
				+ " c.kode_perwakilan, b.asal_paket, b.awb_data_entry, c.gambar"
				+ " from  tt_header a, tt_data_entry b, tt_poto_timbang c"
				+ " where b.pengirim =:prPengirim"
				+ " and a.awb_header = b.awb_data_entry "
				+ " and a.awb_header = c.awb_poto_timbang "
				+ " and b.Flag_entry=0 "
//				+ " and substr(b.tgl_create, 1, 10) = "
//				+ " (select case when DATE_FORMAT(now(),'%H:%i:%s') >  '00:00:00'  "
//				+ " and DATE_FORMAT(now(),'%H:%i:%s') <=  '11:00:00' "
//				+ " then date_add(curdate(), interval -1 day) else curdate() end as time_zone from dual) "
				+ " and a.flag=0 LIMIT 5";
		
		Query query = s.createSQLQuery(sql).setParameter("prPengirim", pengirim);
		System.out.println("--> sql : " + sql);
		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();	
		 List<Object[]> list=query.list();
		    for (Object[] objects : list) {
		    	EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0]!=null?(String) objects[0]:"");
			en.setKdPickup(objects[1]!=null?(String) objects[1]:"");
			en.setPengirim(objects[2]!=null?(String) objects[2]:"");
			en.setbFinal(objects[3]!=null?(String) objects[3]:"");
			en.setBpFinal(objects[4]!=null?(String) objects[4]:"");
			en.setCreated((Date) objects[5]);
			en.setKdPerwakilan(objects[6]!=null?(String) objects[6]:"");
			en.setAsalPaket(objects[7]!=null?(String) objects[7]:"");
			en.setIdDataPaket(objects[8]!=null?(String) objects[8]:"");
			en.setGambar(objects[9]!=null?(String) objects[9]:"");
			returnList.add(en);
		}
		    s.getTransaction().commit();
		return returnList;

	}
	
	//OK
	public static List<EntryDataShowVO> getEditDataEntryFrom(String strAwb) {
		Session s = HibernateUtil.openSession();
		String sql = "select c.awb_data, c.kode_pickup, b.pengirim, c.bclose, c.bpclose, a.tgl_create"
				+ ", c.kode_perwakilan, b.asal_paket, b.awb_data_entry, c.gambar, b.reseller"
				+ ", b.penerima, b.telp_penerima, b.tujuan, b.kode_perwakilan, b.keterangan"
				+ ", b.asal_paket, b.biaya, b.asuransi, b.total_biaya, c.layanan, d.kecamatan, d.kabupaten, d.propinsi "
				+ "from  tt_header a, tt_data_entry b, tt_poto_timbang c, tr_perwakilan d "
				+ "where "
				+ "a.awb_header = b.awb_data_entry "
				+ "and a.awb_header = c.awb_poto_timbang "
				+ "and b.tujuan = d.kode_zona "
				+ "and c.awb_poto_timbang=:pAwb and a.flag=0";
		Query query = s.createSQLQuery(sql).setParameter("pAwb", strAwb);
		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();	
		 List<Object[]> list=query.list();
		    for (Object[] objects : list) {
		    	EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0]!=null?(String) objects[0]:"");
			en.setKdPickup(objects[1]!=null?(String) objects[1]:"");
			en.setPengirim(objects[2]!=null?(String) objects[2]:"");
			en.setbFinal(objects[3]!=null?(String) objects[3]:"");
			en.setBpFinal(objects[4]!=null?(String) objects[4]:"");
			en.setCreated((Date) objects[5]);
			en.setKdPerwakilan(objects[6]!=null?(String) objects[6]:"");
			en.setAsalPaket(objects[7]!=null?(String) objects[7]:"");
			en.setIdDataPaket(objects[8]!=null?(String) objects[8]:"");
			en.setGambar(objects[9]!=null?(String) objects[9]:"");
			en.setResseler(objects[10]!=null?(String) objects[10]:"");
			en.setPenerima(objects[11]!=null?(String) objects[11]:"");
			en.setNoTlpn(objects[12]!=null?(String) objects[12]:"");
			en.setTujuan(objects[13]!=null?(String) objects[13]:"");
			en.setKdPerwakilan(objects[14]!=null?(String) objects[14]:"");
			en.setKet(objects[15]!=null?(String) objects[15]:"");
			en.setAsalPaket(objects[16]!=null?(String) objects[16]:"");
			en.setBiaya(objects[17]!=null?(Integer) objects[17]:0);
			en.setAsuransi(objects[18]!=null?(Integer) objects[18]:0);
			en.setTotalBiaya(objects[19]!=null?(Integer) objects[19]:0);
			en.setLayanan(objects[20]!=null?(String) objects[20]:"");
			en.setKecamatan(objects[21]!=null?(String) objects[21]:"");
			en.setKabupaten(objects[22]!=null?(String) objects[22]:"");
			en.setPorvinsi(objects[23]!=null?(String) objects[23]:"");
			returnList.add(en);
		}
		    s.getTransaction().commit();
		return returnList;

	}
	// chriss
	public static List<TtDataEntry> getDataPaketForCabang(Date dtPeriode) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtDataEntry.class);
		c.add(Restrictions.gt("tglCreate", new Timestamp(dtPeriode.getTime ())));
		List<TtDataEntry> data = c.list();
		s.getTransaction().commit();
		return data;
	}
	
	public static void saveAll(List<TtDataEntry> dataEntry) {
		Session s = HibernateUtil.openSession();
		for (TtDataEntry data : dataEntry) {
			Criteria c = s.createCriteria(TtDataEntry.class);
			c.add(Restrictions.eq("awbDataEntry", data.getAwbDataEntry()));
			List<TtDataEntry> listEntry = c.list();
			if(listEntry.isEmpty()){
				s.save(data);
			}
		}
		s.getTransaction().commit();	
	}
	
}
