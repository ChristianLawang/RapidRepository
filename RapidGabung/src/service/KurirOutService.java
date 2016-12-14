package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import VO.EntryDataShowVO;
import util.DateUtil;
import util.HibernateUtil;

public class KurirOutService {
	
	//OK
	public static String getMaxCodeKurir(String idKurir) {
		String FinalAutuNo;
		Session s = HibernateUtil.openSession();
		String sql = "select max(substr(NO_STRUK_KURIR_OUT,15)) "
				+ "from tt_status_kurir_out where id_kurir=:pIdKurir "
				+ "and substr(tgl_create, 1, 10) = curdate()";
		Query query = s.createSQLQuery(sql).setParameter("pIdKurir", idKurir);
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
	//OK
	public static String getMaxIdKurirOut(String id) {
		String FinalAutuNo;
		Session s = HibernateUtil.openSession();
		String sql = "select max(substr(id, 7, 3)) from "
				+ "tt_status_kurir_out "
				+ "where substr(id, 1, 6) =:pId";
		Query query = s.createSQLQuery(sql).setParameter("pId", id);
		String MaxCode = (String) query.uniqueResult();
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
	//OK
	public static String cekAwbForKurir(String awb){
		Session s = HibernateUtil.openSession();
		String sql = "select b.awb_poto_timbang "
				+ "from tt_header a, tt_poto_timbang b, tt_data_entry c "
				+ "where a.awb_header = b.awb_poto_timbang "
				+ "and a.awb_header = c.awb_data_entry "
				+ "and c.flag_entry = 2 "
				+ "and a.id_kurir is null and a.flag=0 "
				+ "and b.awb_poto_timbang=:pAwb";
		Query query = s.createSQLQuery(sql).setParameter("pAwb", awb);
		String data = (String) query.uniqueResult();
		s.getTransaction().commit();
		return data;
	}
	
	//OK
	public static List<EntryDataShowVO> getListAfterScan(String NoStruk) {
		Session s = HibernateUtil.openSession();
		String sql = "select b.id_barang, c.bclose, d.penerima, c.bpclose "
				+ "from tt_header a, tt_status_kurir_out b, tt_poto_timbang c, tt_data_entry d "
				+ "where a.awb_header = c.awb_poto_timbang "
				+ "and a.awb_header = d.awb_data_entry "
				+ "and b.id_barang = c.awb_poto_timbang and a.flag=0 "
				+ "and b.no_struk_kurir_out=:pNoStruk order by b.tgl_create desc";

		Query query = s.createSQLQuery(sql).setParameter("pNoStruk", NoStruk);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0] != null ? (String) objects[0] : "");
			en.setbFinal(objects[1] != null ? (String) objects[1] : "");
			en.setPenerima(objects[2] != null ? (String) objects[2] : "");
			en.setBpFinal(objects[3] != null ? (String) objects[3] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}
	
	//OK
	public static Boolean updateStaturKurir(String idKurir, String awb){
		Session sess=HibernateUtil.openSession();
			String sql ="update tt_header a "
					+ "set a.id_kurir=:pIdKurir, a.tgl_update=:pUpdated "
					+ "where a.awb_header=:pAwb";
			Query query = sess.createSQLQuery(sql)
					.setParameter("pIdKurir", idKurir) 
					.setParameter("pUpdated", DateUtil.getNow())
					.setParameter("pAwb", awb);
			int result = query.executeUpdate();
			sess.getTransaction().commit();
			return true;
	}
	
	//OK
	public static List<EntryDataShowVO> getListForPrintKurirOut(String NoStruk) {
		Session s = HibernateUtil.openSession();
		String sql = "select b.id_barang, c.bclose, d.penerima, e.nama, b.tgl_create "
				+ "from tt_header a, tt_status_kurir_out b, tt_poto_timbang c, tt_data_entry d, tr_kurir e "
				+ "where a.awb_header = c.awb_poto_timbang "
				+ "and a.awb_header = d.awb_data_entry "
				+ "and b.id_barang = c.awb_data  "
				+ "and b.id_kurir = e.nik and a.flag=0 "
				+ "and b.no_struk_kurir_out=:pNoStruk";

		Query query = s.createSQLQuery(sql).setParameter("pNoStruk", NoStruk);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0] != null ? (String) objects[0] : "");
			en.setbFinal(objects[1] != null ? (String) objects[1] : "");
			en.setPenerima(objects[2] != null ? (String) objects[2] : "");
			en.setUser(objects[3] != null ? (String) objects[3] : "");
			en.setCreated((Date) objects[4]);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}
	//OK
	public static Boolean deleteStatusKurir(String awb, String noStruk){
		Session sess=HibernateUtil.openSession();
			String sql ="update tt_header a "
					+ "set a.id_kurir=:pIdKurir, a.tgl_update=:pUpdated "
					+ "where a.awb_header=:pAwb";
			Query query = sess.createSQLQuery(sql)
					.setParameter("pIdKurir", null) 
					.setParameter("pUpdated", null)
					.setParameter("pAwb", awb);
			query.executeUpdate();
			
			String sql2 ="delete from tt_status_kurir_out "
					+ "where id_barang=:pIdBarang  and no_struk_kurir_out=:PnoStruk ";
			Query query2 = sess.createSQLQuery(sql2)
			.setParameter("pIdBarang", awb)
			.setParameter("PnoStruk", noStruk);
			query2.executeUpdate();
			
			sess.getTransaction().commit();
			return true;
	}
	
		//OK
	// Jangan Di tiru ini tar mau di hapus
		public static void deleteStaturKurirAll(List<EntryDataShowVO> listEntry, String idKurir ,String noStruk) {
			Session sess = HibernateUtil.openSession();
			for(EntryDataShowVO en : listEntry){
			String sql = "update tt_header a " + "set a.id_kurir=:pIdKurir, a.tgl_update=:pUpdated "
					+ "where a.awb_header=:pAwb";
			Query query = sess.createSQLQuery(sql).setParameter("pIdKurir", null).setParameter("pUpdated", null)
					.setParameter("pAwb", en.getAwbData());
			query.executeUpdate();

			String sql2 = "delete from tt_status_kurir_out " + "where id_barang=:pIdBarang and no_struk_kurir_out=:PnoStruk";
			Query query2 = sess.createSQLQuery(sql2)
					.setParameter("pIdBarang", en.getAwbData())
					.setParameter("PnoStruk", noStruk);
			query2.executeUpdate();
			}
			sess.getTransaction().commit();
		}

}