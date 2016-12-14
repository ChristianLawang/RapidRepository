package service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import VO.EntryDataShowVO;
import entity.TtDataEntry;
import entity.TtGabungPaket;
import entity.TtGabungSementara;
import entity.TtHeader;
import util.DateUtil;
import util.HibernateUtil;
import util.PropertiesUtil;

public class GabungPaketService {

	public static List<EntryDataShowVO> getNonGabungPaket(String kdTujuan) {
		System.out.println("--> run service : getNonGabungPaket");
		Session s = HibernateUtil.openSession();
		String kodeKomputer = PropertiesUtil.getPerwakilan();
//		String sql = "select b.kode_perwakilan, c.awb_data, b.tujuan "
//				+ "from "
//				+ "tt_header a, tt_data_entry b, tt_poto_timbang c "
//				+ "where "
//				+ "a.awb_header = b.awb_data_entry "
//				+ "and a.awb_header = c.awb_poto_timbang "
//				+ "and a.gabung_paket_flag = 0 "
//				+ "and b.kode_perwakilan =:prPerwakilan "
//				+ "and left(c.awb_data,3)='"+kodeKomputer+"'"
//				+ "and a.waiting_pending_flag=0 and a.flag=0 order by a.wk_gabung_paket";
		String sql = 
				"select b.kode_perwakilan, a.awb_header, b.tujuan from tt_header a " +
				"inner join tt_data_entry b on a.awb_header = b.awb_data_entry " +
				"where a.gabung_paket_flag = '0' " +
				"and b.kode_perwakilan = :prPerwakilan " +
				"and left(a.awb_header, 3) = '"+kodeKomputer+"' " +
				"and a.waiting_pending_flag = '0' " +
				"and a.flag = '0' " +
				"order by a.wk_gabung_paket";

		Query query = s.createSQLQuery(sql).setParameter("prPerwakilan", kdTujuan);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setKdPerwakilan(objects[0] != null ? (String) objects[0] : "");
			en.setAwbData(objects[1] != null ? (String) objects[1] : "");
			en.setTujuan(objects[2] != null ? (String) objects[2] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}
	//new
	public static List<EntryDataShowVO> getNonGabungPaketHO(String strThisKdCabang) {
		System.out.println("--> run service : getNonGabungPaketHO");
		Session s = HibernateUtil.openSession();
		String sql = "select c.kode_perwakilan, c.awb_data, b.tujuan "
				+ "from "
				+ "tt_header a, tt_data_entry b, tt_poto_timbang c "
				+ "where "
				+ "a.awb_header = b.awb_data_entry "
				+ "and a.awb_header = c.awb_poto_timbang "
				+ "and a.gabung_paket_flag = 0 "
				+ "and b.kode_perwakilan <> 'JNE' and b.kode_perwakilan <> 'XXX' "
				+ "and b.kode_perwakilan <>:pThisKdCabang "
				+ "and a.waiting_pending_flag=0 and a.flag=0 order by a.wk_gabung_paket";

		Query query = s.createSQLQuery(sql).setParameter("pThisKdCabang", strThisKdCabang);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setKdPerwakilan(objects[0] != null ? (String) objects[0] : "");
			en.setAwbData(objects[1] != null ? (String) objects[1] : "");
			en.setTujuan(objects[2] != null ? (String) objects[2] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}


	public static String getMaxCode(String kdPerwakilan) {
		System.out.println("--> run service : getMaxCode");
		System.out.println("--> kdPerwakilan : " + kdPerwakilan);
		String FinalAutuNo;
		Session s = HibernateUtil.openSession();
		String sql = "select max(substr(id_kardus, 4, 3)) from tt_gabung_paket "
				+ "where KODE_PERWAKILAN=:pKdPerwakilan " + "and substr(tgl_create, 1, 10) = curdate()";
		Query query = s.createSQLQuery(sql).setParameter("pKdPerwakilan", kdPerwakilan);
		String MaxCode = (String) query.uniqueResult();
		System.out.println("--> maxCode : " + MaxCode);
		int AutoNo = MaxCode != null ? Integer.parseInt(MaxCode) : 0;
		if (AutoNo > 0) {
			AutoNo = AutoNo + 1;
			System.out.println("Auto No : "+AutoNo);
			if (AutoNo <= 9) {
				FinalAutuNo = "00" + AutoNo;
			}else if (AutoNo > 9 && AutoNo <= 99 ) {
				FinalAutuNo = "0" + AutoNo;
			} else {
				FinalAutuNo = String.valueOf(AutoNo);
			}
		} else {
			FinalAutuNo = "001";
		}
		s.getTransaction().commit();
		System.out.println("--> FinalAutoNo : " + FinalAutuNo);
		return FinalAutuNo;
	}
	
	public static String getMaxIdGabungSementara(String id, String idKardus) {
		System.out.println("--> run service : getMaxIdGabungSementara");
		String FinalAutuNo;
		Session s = HibernateUtil.openSession();
		String sql = "select max(substr(id, 7, 3)) from "
				+ "tt_gabung_sementara "
				+ "where substr(id, 1, 6) =:pId and id_kardus=:pIdKardus";
		Query query = s.createSQLQuery(sql)
				.setParameter("pId", id)
				.setParameter("pIdKardus", idKardus);
		String MaxCode = (String) query.uniqueResult();
		System.out.println("--> maxCode : " + MaxCode);
		int AutoNo = MaxCode != null ? Integer.parseInt(MaxCode) : 0;
		if (AutoNo > 0) {
			AutoNo = AutoNo + 1;
			System.out.println("Auto No : "+AutoNo);
			if (AutoNo <= 9) {
				FinalAutuNo = "00" + AutoNo;
			}else if (AutoNo > 9 && AutoNo <= 99 ) {
				FinalAutuNo = "0" + AutoNo;
			} else {
				FinalAutuNo = String.valueOf(AutoNo);
			}
		} else {
			FinalAutuNo = "001";
		}
		s.getTransaction().commit();
		return FinalAutuNo;
	}
	
	public static Boolean updateSesudahGabung(int Flag, java.util.Date date, String awb, String kdKotak) {
		System.out.println("--> run service : updateSesudahGabung");
		Session sess = HibernateUtil.openSession();
		sess.beginTransaction();
		String sql = "UPDATE tt_header SET Gabung_Paket_Flag=:pFlag, Wk_Gabung_Paket=:pWkGabung, waiting_pending_flag=0, id_kardus=:pIdKardus "
				+ "WHERE awb_header=:rId";
		Query query = sess.createSQLQuery(sql).setParameter("pFlag", Flag).setParameter("pWkGabung", date).setParameter("pIdKardus", kdKotak)
				.setParameter("rId", awb);
		query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}

	public static List<EntryDataShowVO> getGabungPaketAfterSave(String kodeKotak) {
		System.out.println("--> run service : getGabungPaketAfterSave");
		Session s = HibernateUtil.openSession();
//		String sql = "select a.awb, b.bpClose, d.TUJUAN, b.bClose  "
//				+ "from tt_gabung_sementara a, tt_poto_timbang b, tt_header c, tt_data_entry d "
//				+ "where a.awb = b.awb_data "
//				+ "and c.awb_header = d.awb_data_entry "
//				+ "and c.awb_header = b.awb_poto_timbang "
//				+ "and c.Gabung_Paket_Flag=2 and a.id_kardus=:prKodeKotak "
//				+ "and c.flag=0 order by a.tgl_create desc";
		String sql =
				"select a.awb, d.bpClose, c.tujuan, d.bClose from " +
				"tt_gabung_sementara a " +
				"inner join tt_header b on a.awb = b.awb_header " +
				"inner join tt_data_entry c on a.awb = c.awb_data_entry " +
				"inner join tt_poto_timbang d on a.awb = d.awb_poto_timbang " +
				"where " +
				"a.id_kardus = :prKodeKotak " +
				"and b.flag = '0' " +
				"and b.gabung_paket_flag = '2' " +
				"order by a.tgl_create";
		Query query = s.createSQLQuery(sql).setParameter("prKodeKotak", kodeKotak);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0] != null ? (String) objects[0] : "");
			en.setBpFinal(objects[1] != null ? (String) objects[1] : "");
			en.setTujuan(objects[2] != null ? (String) objects[2] : "");
			en.setbFinal(objects[3] != null ? (String) objects[3] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}
	
	public static List<EntryDataShowVO> getGabungPaketPrintUlang(String kodeKotak) {//TODO lIST
		System.out.println("--> run service : getGabungPaketPrintUlang");
		Session s = HibernateUtil.openSession();
		String sql = "select a.awb, b.BPClose, d.TUJUAN, b.bClose "
				+ "from tt_gabung_paket a, tt_poto_timbang b, tt_header c, tt_data_entry d  "
				+ "where a.awb = b.awb_data "
				+ "and c.awb_header = d.awb_data_entry "
				+ "and c.awb_header = b.awb_poto_timbang "
				+ "and c.Gabung_Paket_Flag=1 "
				+ "and a.ID_KARDUS=:prKodeKotak and c.flag=0 order by a.tgl_create desc";
		Query query = s.createSQLQuery(sql).setParameter("prKodeKotak", kodeKotak);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0] != null ? (String) objects[0] : "");
			en.setBpFinal(objects[1] != null ? (String) objects[1] : "");
			en.setTujuan(objects[2] != null ? (String) objects[2] : "");
			en.setbFinal(objects[3] != null ? (String) objects[3] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}


	public static List<EntryDataShowVO> getDataGabPaketPrint(String kodeKotak) {
		System.out.println("--> run service : getDataGabPaketPrint");
		Session s = HibernateUtil.openSession();
//		String sql = "select a.awb, b.bClose, b.penerima, a.tgl_create, b.pbClose "
//				+ "from tt_gabung_paket a, tt_data_entry b, tt_poto_timbang c "
//				+ "where a.awb = c.awb_data "
//				+ "and b.awb_data_entry = c.awb_poto_timbang "
//				+ "and a.id_kardus =:prKdKardus and c.flag=0 order by a.awb";
		
		String sql = 
				"select a.awb, b.bClose, b.penerima,  b.pbClose from tt_gabung_paket a " +
				"inner join tt_data_entry b on a.awb = b.awb_data_entry " +
				"inner join tt_header c on a.awb = c.awb_header " +
				"where c.flag = '0' " +
				"and a.id_kardus = :prKdKardus";
//		
//		String sql =
//				"select a.awb, c.bClose, d.penerima, c.bpClose from tt_gabung_sementara a " +
//				"inner join tt_header b on a.awb = b.awb_header " +
//				"inner join tt_poto_timbang c on a.awb = c.awb_poto_timbang " +
//				"inner join tt_data_entry d on a.awb = d.awb_data_entry " +
//				"where a.id_kardus = :prKdKardus " +
//				"and b.gabung_paket_flag = '2' " +
//				"and b.flag = '0'";
		
		Query query = s.createSQLQuery(sql).setParameter("prKdKardus", kodeKotak);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0] != null ? (String) objects[0] : "");
			en.setbFinal(objects[1] != null ? (String) objects[1] : "");
			en.setPenerima(objects[2] != null ? (String) objects[2] : "");
//			en.setCreated((java.util.Date) objects[3]);
			en.setBpFinal(objects[3] != null ? (String) objects[3] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
	
	public static Boolean updatePending(String awb, int flag){
		System.out.println("--> run service : updatePending");
		Session sess=HibernateUtil.openSession();
		sess.beginTransaction();
			String sql ="update tt_header a set WAITING_PENDING_FLAG=:pFleg "
					+ "where awb_header =:pAwb";
			Query query = sess.createSQLQuery(sql)
					.setParameter("pFleg", flag)
					.setParameter("pAwb", awb);
			int result = query.executeUpdate();
			sess.getTransaction().commit();
			return true;
	}
	
	public static Boolean deleteSesudahGabung(String awb) {
		System.out.println("--> run service : deleteSesudahGabung");
		Session sess = HibernateUtil.openSession();
		sess.beginTransaction();
		
		String sql = "UPDATE tt_header SET Gabung_Paket_Flag=:pFlag, Wk_Gabung_Paket=:pWkGabung, waiting_pending_flag=0, id_kardus=null "
				+ "WHERE awb_header=:rId";
		Query query = sess.createSQLQuery(sql).setParameter("pFlag", 0).setParameter("pWkGabung", null)
				.setParameter("rId", awb);
		int result = query.executeUpdate();
		
		String sqlDelete="delete from tt_gabung_sementara where awb=:prAwbId";
		Query queryDelete = sess.createSQLQuery(sqlDelete).setParameter("prAwbId", awb);
		queryDelete.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
	
	public static String noResiTerakhir(){
		System.out.println("--> run service : noResiTerakhir");
		Session s = HibernateUtil.openSession();
		String sql = "select distinct(a.id_kardus) "
				+ "from tt_gabung_paket a, tt_header b, tt_poto_timbang c "
				+ "where b.awb_header = c.awb_poto_timbang and a.awb = c.awb_data "
				+ "and a.tgl_create = (select max(a.tgl_create) from tt_gabung_paket a) and b.flag=0";
		Query query = s.createSQLQuery(sql);
		String noResiTerakhir = (String) query.uniqueResult();
		return noResiTerakhir;
	}
	
	public static List<TtGabungSementara> getDataGabungPaketSession(String kdKotak) {
		System.out.println("--> run service : getDataGabungPaketSession");
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtGabungSementara.class);
		c.add(Restrictions.eq("idKardus", kdKotak));
		List<TtGabungSementara> data = c.list();
		s.getTransaction().commit();
		return data;
	}
	
	public static String getKdKotakGabungPaketSessionTampilTableB(String tujuan) {
		System.out.println("--> run service : getKdKotakGabungPaketSessionTampilTableB");
		String noKotak = null;
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtGabungSementara.class);
		c.add(Restrictions.eq("tujuan", tujuan));
		List<TtGabungSementara> data = c.list();
		for(TtGabungSementara gab : data){
			noKotak=gab.getIdKardus();
		}
		s.getTransaction().commit();
		return noKotak;
	}
	
	public static Boolean saveGabungSessToGabungPaket(List<TtGabungPaket> data){
		System.out.println("--> run service : saveGabungSessToGabungPaket");
		Session s=HibernateUtil.openSession();
		String kdKotak="";
		for(TtGabungPaket dat : data){
			dat.print();
			s.save(dat);
			kdKotak = dat.getIdKardus();
		}
		String sqlDelete="delete from tt_gabung_sementara where id_kardus=:pKdKotak";
		Query queryDelete = s.createSQLQuery(sqlDelete).setParameter("pKdKotak", kdKotak);
		queryDelete.executeUpdate();
		s.getTransaction().commit();
		return true;
	}
	

	public static String validasiGabungPaket(String cgk) {
		System.out.println("--> run service : validasiGabungPaket");
		Session s = HibernateUtil.openSession();
		String sql = "select id_kardus from tt_gabung_sementara where awb=:pAwb ";
		Query query = s.createSQLQuery(sql).setParameter("pAwb", cgk);
		String kdKotak = (String) query.uniqueResult();
		s.getTransaction().commit();
		if (kdKotak != null && !kdKotak.equals("")) {
			return kdKotak;
		} else {
			Session s2 = HibernateUtil.openSession();
			String sql2 = "select id_kardus from tt_gabung_paket where awb=:pAwb and kode_perwakilan <> 'RPD'";
			Query query2 = s2.createSQLQuery(sql2).setParameter("pAwb", cgk);
			String kdKotak2 = (String) query2.uniqueResult();
			s2.getTransaction().commit();
			return kdKotak2;
		}
	}
	
	public static String getTujuanPaket(String cgk) {
		System.out.println("--> run service : getTujuanPaket");
		Session s = HibernateUtil.openSession();
//		String sql = "select c.kode_perwakilan "
//				+ "from tt_poto_timbang a, tt_header b, tt_data_entry c "
//				+ "where a.awb_poto_timbang = b.awb_header and b.awb_header = c.awb_data_entry "
//				+ "and a.awb_data=:awbPar and b.gabung_paket_flag = 0 and c.flag_entry=2 and b.flag=0";
		String sql = 
				"select c.kode_perwakilan from tt_header a " +
				"inner join tt_poto_timbang b on a.awb_header = b.awb_poto_timbang " +
				"inner join tt_data_entry c on a.awb_header = c.awb_data_entry " +
				"where a.awb_header = :awbPar  " +
				"and a.gabung_paket_flag = '0' " +
				"and c.flag_entry = '2' " +
				"and a.flag = '0'";
		
		Query query = s.createSQLQuery(sql).setParameter("awbPar", cgk);
		String tujuan = (String) query.uniqueResult();
		s.getTransaction().commit();
		return tujuan;
	}
	
	public static List<TtGabungPaket> getGabungPaketForCabang(Date dtPeriode) {
		System.out.println("--> run service : getGabungPaketForCabang");
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtGabungPaket.class);
		c.add(Restrictions.gt("tglCreate", new Timestamp(dtPeriode.getTime ())));
		List<TtGabungPaket> data = c.list();
		s.getTransaction().commit();
		return data;
	}
	
	public static void saveAll(List<TtGabungPaket> gabungPaket) {
		System.out.println("--> run service : saveAll");
		Session s = HibernateUtil.openSession();
		for (TtGabungPaket data : gabungPaket) {
			Criteria c = s.createCriteria(TtGabungPaket.class);
			c.add(Restrictions.eq("awb", data.getAwb()));
			List<TtGabungPaket> listGabung = c.list();
			if(listGabung.isEmpty()){
				s.save(data);
			}
		}
		s.getTransaction().commit();	
	}
//	public static List<TtGabungSementara> loadGabungPaketSementaraKemarin() {
//		Session s = HibernateUtil.openSession();
//
//		Criteria c = s.createCriteria(TtGabungSementara.class);
//		List<TtGabungSementara> data = c.list();
//		s.getTransaction().commit();
//		return data;
//	}
//	public static void restoreNoResiFromSession(TtGabungSementara data) {
//		Session s = HibernateUtil.openSession();
//	      try{
//	    	  TtHeader header = (TtHeader)s.get(TtHeader.class, data.getAwb()); 
//	    	  header.setGabungPaketFlag(0);
//	    	  header.setWkGabungPaket(null);
//	    	  header.setWaitingPendingFlag(0);
//	    	  header.setIdKardus("");
//	    	  s.update(header);
//	    	  s.delete(data);
//	    	  s.getTransaction().commit();
//	      }catch (HibernateException e) {
//	         if (s.getTransaction()!=null) s.getTransaction().rollback();
//	         e.printStackTrace(); 
//	      }finally {
//	    	  
//	      }
//	}
	public static void deleteSessionKemarin() {
		Session s = HibernateUtil.openSession();
		String sqlDelete="delete from tt_gabung_sementara where tgl_create < curdate()";
		Query queryDelete = s.createSQLQuery(sqlDelete);
		queryDelete.executeUpdate();
		s.getTransaction().commit();
	}
	public static List<TtGabungPaket> unionGabungPaket(
			String idKardusParent, 
			String kodePerwakilan) {
		
		Session s = HibernateUtil.openSession();
		String sql = 
				"select "
				+ "ID, "
				+ "ID_KARDUS, "
				+ "ID_KARDUS_SUB, "
				+ "AWB, "
				+ "KODE_PERWAKILAN, "
				+ "OLEH, "
				+ "TGL_CREATE, "
				+ "TGL_UPDATE, "
				+ "FLAG "
				+ "from tt_gabung_paket "
				+ "where id_kardus = '"+idKardusParent+"'";
//						+ " and substring(ID_KARDUS,14,3) != '"+kodePerwakilan+"'";
		SQLQuery  query = s.createSQLQuery(sql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		s.getTransaction().commit();
		List<TtGabungPaket> returnList = new ArrayList<TtGabungPaket>();
		for (Object o : result) {
			Map m = (Map) o;
			TtGabungPaket en = new TtGabungPaket();
			en.setId(m != null ? (String) m.get("ID") : "");
			en.setIdKardus(m != null ? (String) m.get("ID_KARDUS") : "");
			en.setIdKardusSub(m != null ? (String) m.get("ID_KARDUS_SUB") : "");
			en.setAwb(m != null ? (String) m.get("AWB") : "");
			en.setKodePerwakilan(m != null ? (String) m.get("KODE_PERWAKILAN") : "");
			en.setOleh(m != null ? (String) m.get("OLEH") : "");
			en.setTglCreate(m != null ? (Date) m.get("TGL_CREATE") : null);
			en.setTglUpdate(m != null ? (Date) m.get("TGL_UPDATE") : null);
			en.setFlag(m != null ? (Integer) m.get("FLAG") : null);
			returnList.add(en);
		}
		
		return returnList;
	}
	public static String getMaxCode2(String kdPerwakilan, Date fotoTimbangDateGenerateRule) {
		System.out.println("--> run service : getMaxCode");
		System.out.println("--> kdPerwakilan : " + kdPerwakilan);
		String FinalAutuNo;
		Session s = HibernateUtil.openSession();
		String sql = "select max(substr(id_kardus, 4, 3)) from tt_gabung_paket "
				+ "where KODE_PERWAKILAN=:pKdPerwakilan " + "and date(tgl_create) = '"+DateUtil.getDateRipTimeForSQL(fotoTimbangDateGenerateRule)+"'";
		Query query = s.createSQLQuery(sql).setParameter("pKdPerwakilan", kdPerwakilan);
		String MaxCode = (String) query.uniqueResult();
		System.out.println("--> maxCode : " + MaxCode);
		int AutoNo = MaxCode != null ? Integer.parseInt(MaxCode) : 0;
		if (AutoNo > 0) {
			AutoNo = AutoNo + 1;
			System.out.println("Auto No : "+AutoNo);
			if (AutoNo <= 9) {
				FinalAutuNo = "00" + AutoNo;
			}else if (AutoNo > 9 && AutoNo <= 99 ) {
				FinalAutuNo = "0" + AutoNo;
			} else {
				FinalAutuNo = String.valueOf(AutoNo);
			}
		} else {
			FinalAutuNo = "001";
		}
		s.getTransaction().commit();
		System.out.println("--> FinalAutoNo : " + FinalAutuNo);
		return FinalAutuNo;
	}
	
	public static void resetGabungPaket(String idKardus, String awb) {
		Session s = HibernateUtil.openSession();
		String sqlDelete="delete from tt_gabung_paket where id_kardus = '"+idKardus+"' and awb = '"+awb+"'";
		Query queryDelete = s.createSQLQuery(sqlDelete);
		queryDelete.executeUpdate();
		String sqlUpdate = "UPDATE tt_header SET Gabung_Paket_Flag='0', Wk_Gabung_Paket=null, waiting_pending_flag=0, id_kardus=null "
				+ "WHERE awb_header='"+awb+"'";
		Query queryUpdate = s.createSQLQuery(sqlUpdate);
		queryUpdate.executeUpdate();
		s.getTransaction().commit();
	}
}

