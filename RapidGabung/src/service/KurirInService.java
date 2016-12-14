package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import entity.TtStatusResiBermasalah;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import VO.KurirInVO;
import entity.TtStatusKurirIn;
import util.DateUtil;
import util.HibernateUtil;

public class KurirInService {

	//OK
	public static List<KurirInVO> getResiByKurir(String idKurir) {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select a.id_kurir, a.id_barang, b.penerima "
				+ "from tt_status_kurir_out a, tt_data_entry b, tt_header c, tt_poto_timbang d "
				+ "where b.awb_data_entry = c.awb_header and b.awb_data_entry = d.awb_poto_timbang "
				+ "and a.id_barang = d.awb_data and a.id_kurir = c.id_kurir and a.id_kurir=:pIdKurir "
				+ "and a.status='ONPROCESS' and d.flag=0";
		Query query = session.createSQLQuery(nativeSql).setParameter("pIdKurir", idKurir);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();

		List<KurirInVO> returnList = new ArrayList<KurirInVO>();
		for (Object obj : result) {
			Map row = (Map) obj;
			KurirInVO everyRow = new KurirInVO();
			everyRow.setIdBarang((String) row.get("ID_BARANG") != null ? (String) row.get("ID_BARANG") : "");

			everyRow.setPenerima((String) row.get("PENERIMA") != null ? (String) row.get("PENERIMA") : "");

			returnList.add(everyRow);
		}
		return returnList;

	}

	// OK
	public static Boolean updateStatusResiKurir(TtStatusKurirIn ts, Boolean flagMasalah) {

		Session sess = HibernateUtil.openSession();
		String sql = "update tt_status_kurir_out a set a.status=:pSts1, a.tgl_update=:pTUpd1 "
				+ "where a.masalah is null and a.id_barang=:pAwbUpdt1";
		Query query = sess.createSQLQuery(sql).setParameter("pSts1", "SEND")
				.setParameter("pTUpd1", DateUtil.getNow())
				.setParameter("pAwbUpdt1", ts.getIdBarang());
		query.executeUpdate();
		sess.getTransaction().commit();
		
		GenericService.save(TtStatusKurirIn.class, ts, true);
		
		if (flagMasalah) {
			// JIKA BARANG DITERIMA
			sess = HibernateUtil.openSession();
			String sql2 = "update tt_header a set a.barang_terkirim_flag = 1, a.tgl_update=:pUpdate2 "
					+ "where a.awb_header=:pAwbUpdate";
			Query query2 = sess.createSQLQuery(sql2).setParameter("pUpdate2", DateUtil.getNow())
					.setParameter("pAwbUpdate", ts.getIdBarang());
			query2.executeUpdate();
			sess.getTransaction().commit();
		} else {
			// JIKA BARANG BERMASALAH
			sess = HibernateUtil.openSession();
			String sql2 = "update tt_header a set a.id_kurir = null, a.tgl_update=:pUpdate3 "
					+ "where a.awb_header=:pAwbUpdate2";
			Query query2 = sess.createSQLQuery(sql2).setParameter("pUpdate3", DateUtil.getNow())
					.setParameter("pAwbUpdate2", ts.getIdBarang());
			query2.executeUpdate();
			sess.getTransaction().commit();
			// INSERT TO TABLE BERMSALAH
			TtStatusResiBermasalah ber = new TtStatusResiBermasalah();
			ber.setTglCreate(DateUtil.getNow());
			ber.setIdBarang(ts.getIdBarang());
			ber.setIdKurir(ts.getIdKurir());
			ber.setMasalah(ts.getMasalah());
			ber.setFlag(0);
			ber.setId(ts.getId());

			GenericService.save(TtStatusResiBermasalah.class, ber, true);

		}

		return true;
	}

	// OK
	public static List<KurirInVO> getListAfterScan(String awb, String idKur, String Status) {
		Session s = HibernateUtil.openSession();
		String sql = "select a.id_barang, a.penerima " + "from tt_status_kurir_out a "
				+ "where a.id_barang = :pAWB and a.id_kurir=:pidKur and a.status=:pStatus and a.flag=0";
		Query query = s.createSQLQuery(sql).setParameter("pAWB", awb).setParameter("pidKur", idKur)
				.setParameter("pStatus", Status);
		List<KurirInVO> returnList = new ArrayList<KurirInVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			KurirInVO en = new KurirInVO();
			en.setIdBarang(objects[0] != null ? (String) objects[0] : "");
			en.setPenerima(objects[1] != null ? (String) objects[1] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}

	// OK
	public static List<KurirInVO> getListPrintKurIn(String noStruk) {
		Session s = HibernateUtil.openSession();
		String sql = "select a.id_barang, a.penerima, b.BCLOSE, b.BPCLOSE, a.tgl_create, c.nama "
				+ "from tt_status_kurir_in a, tt_poto_timbang b, tr_kurir c "
				+ "where a.id_barang = b.AWB_POTO_TIMBANG and a.id_Kurir=c.nik and "
				+ "a.no_struk_kurir_in=:pNoStruk and b.flag=0";
		Query query = s.createSQLQuery(sql).setParameter("pNoStruk", noStruk);
		List<KurirInVO> returnList = new ArrayList<KurirInVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			KurirInVO en = new KurirInVO();
			en.setIdBarang(objects[0] != null ? (String) objects[0] : "");
			en.setPenerima(objects[1] != null ? (String) objects[1] : "");
			en.setBfinal(objects[2] != null ? (String) objects[2] : "");
			en.setBpfinal(objects[3] != null ? (String) objects[3] : "");
			en.setUpdated((Date) objects[4]);
			en.setIdKurir(objects[5] != null ? (String) objects[5] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	//OK
	public static String getMaxCodeKurirIN(String idKurir) {
		String FinalAutuNo;
		Session s = HibernateUtil.openSession();
		String sql = "select max(substr(NO_STRUK_KURIR_IN,15)) " + "from tt_status_kurir_in"
				+ " where id_kurir=:pIdKur "
				+ "and substr(tgl_create, 1, 10) = curdate()";
		Query query = s.createSQLQuery(sql).setParameter("pIdKur", idKurir);
		String MaxCode = (String) query.uniqueResult();
		int AutoNo = MaxCode != null ? Integer.parseInt(MaxCode) : 0;
		if (AutoNo > 0) {
			AutoNo = AutoNo + 1;
			if (AutoNo <= 9) {
				FinalAutuNo = "0" + AutoNo;
			} else {
				FinalAutuNo = String.valueOf(AutoNo);
			}
		} else {
			FinalAutuNo = "01";
		}
		s.getTransaction().commit();
		return FinalAutuNo;
	}

	// OK
	public static String getMaxIdKurirIn(String id) {
		String FinalAutuNo;
		Session s = HibernateUtil.openSession();
		String sql = "select max(substr(id, 7, 3)) from " + "tt_status_kurir_in " + "where substr(id, 1, 6) =:pId";
		Query query = s.createSQLQuery(sql).setParameter("pId", id);
		String MaxCode = (String) query.uniqueResult();
		int AutoNo = MaxCode != null ? Integer.parseInt(MaxCode) : 0;
		if (AutoNo > 0) {
			AutoNo = AutoNo + 1;
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

}
