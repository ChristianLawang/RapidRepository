package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import VO.EntryDataShowVO;
import VO.MapingJneVO;
import entity.TtMappingResiJne;
import util.DateUtil;
import util.HibernateUtil;

public class MapingJneService {	
	//OK
	public static List<TtMappingResiJne> getDataUploadJne(java.sql.Date dateAwal, java.sql.Date dateAkhir) {
		Session s = HibernateUtil.openSession();
		String sql = "select a.Resi_jne, a.penerima, a.pengirim, "
				+ "a.tujuan, a.service, a.harga "
				+ "from tt_mapping_resi_jne a "
				+ "where date(a.tgl_create) between :pTglMulai and :pTglAkhir and upload_Flag=0 and a.flag=0";
		Query query = s.createSQLQuery(sql)
				.setParameter("pTglMulai", dateAwal)
				.setParameter("pTglAkhir", dateAkhir);
		
		List<TtMappingResiJne> returnList = new ArrayList<TtMappingResiJne>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			TtMappingResiJne en = new TtMappingResiJne();
			en.setResiJne(objects[0] != null ? (String) objects[0] : "");
			en.setPenerima(objects[1] != null ? (String) objects[1] : "");
			en.setPengirim(objects[2] != null ? (String) objects[2] : "");
			en.setTujuan(objects[3] != null ? (String) objects[3] : "");
			en.setService(objects[4] != null ? (String) objects[4] : "");
			en.setHarga(objects[5] != null ? (Integer) objects[5] : 0);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}
	
	//OK
	public static List<TtMappingResiJne> getListCariJneRapid(MapingJneVO mapJne) {
		Session s = HibernateUtil.openSession();
		String sql = "select c.awb_data, b.penerima, b.pengirim, "
				+ "concat(d.kecamatan,', ',d.kabupaten) tujuan, c.layanan, b.harga, a.tgl_create "
				+ "from tt_header a, tt_data_entry b, tt_poto_timbang c, tr_perwakilan d "
				+ "where "
				+ "a.awb_header = b.awb_data_entry "
				+ "and a.awb_header = c.awb_poto_timbang "
				+ "and b.tujuan = d.kode_zona and a.resi_jne is null and "
				+ "b.jne_flag=1 and upper(b.penerima) like :pPenerima and c.flag=0";
//				+ "and b.pengirim like :pPengirim and d.kecamatan like :pKec";
		Query query = s.createSQLQuery(sql)
				.setParameter("pPenerima", "%"+mapJne.getPenerima().toUpperCase()+"%");
//				.setParameter("pPengirim", "%"+pengirim+"%")
//				.setParameter("pKec", "%"+kec+"%");
		
		List<TtMappingResiJne> returnList = new ArrayList<TtMappingResiJne>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			TtMappingResiJne en = new TtMappingResiJne();
			en.setResiJne(objects[0] != null ? (String) objects[0] : "");
			en.setPenerima(objects[1] != null ? (String) objects[1] : "");
			en.setPengirim(objects[2] != null ? (String) objects[2] : "");
			en.setTujuan(objects[3] != null ? (String) objects[3] : "");
			en.setService(objects[4] != null ? (String) objects[4] : "");
			en.setHarga(objects[5] != null ? (Integer) objects[5] : 0);
			en.setTglCreate((Date) objects[6]);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}
	
	//OK
	public static List<TtMappingResiJne> getListCariJneRapidAll(MapingJneVO mapJne) {
		Session s = HibernateUtil.openSession();
		String sql = "select c.awb_data, b.penerima, b.pengirim, "
				+ "concat(d.kecamatan,', ',d.kabupaten) tujuan, c.layanan, b.harga, a.tgl_create "
				+ "from tt_header a, tt_data_entry b, tt_poto_timbang c, tr_perwakilan d "
				+ "where "
				+ "a.awb_header = b.awb_data_entry "
				+ "and a.awb_header = c.awb_poto_timbang "
				+ "and b.tujuan = d.kode_zona and a.resi_jne is null and b.jne_flag=1 "
				+ "and date(a.tgl_create) between :pTglMulai and :pTglAkhir and c.flag=0";
		Query query = s.createSQLQuery(sql)
				.setParameter("pTglMulai", mapJne.getDpAwalRapid())
				.setParameter("pTglAkhir", mapJne.getDpAkhirRapid());
		
		List<TtMappingResiJne> returnList = new ArrayList<TtMappingResiJne>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			TtMappingResiJne en = new TtMappingResiJne();
			en.setResiJne(objects[0] != null ? (String) objects[0] : "");
			en.setPenerima(objects[1] != null ? (String) objects[1] : "");
			en.setPengirim(objects[2] != null ? (String) objects[2] : "");
			en.setTujuan(objects[3] != null ? (String) objects[3] : "");
			en.setService(objects[4] != null ? (String) objects[4] : "");
			en.setHarga(objects[5] != null ? (Integer) objects[5] : 0);
			en.setTglCreate((Date) objects[6]);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}
	
	//OK
	public static Boolean updateResiJne(String resiJN, String awb){
		Session sess=HibernateUtil.openSession();
			String sql ="update tt_header a "
					+ "set a.resi_jne=:pResi, a.tgl_update=:pUpdated "
					+ "where a.awb_header=:pAwb";
			Query query = sess.createSQLQuery(sql)
					.setParameter("pResi", resiJN) 
					.setParameter("pUpdated", DateUtil.getNow())
					.setParameter("pAwb", awb);
			query.executeUpdate();
			
			String sql2 ="update tt_mapping_resi_jne a "
					+ "set a.upload_flag= 1, a.tgl_update=:pUpdat  "
					+ "where a.resi_jne=:pResiJne";
			Query query2 = sess.createSQLQuery(sql2)
					.setParameter("pUpdat", DateUtil.getNow()) 
					.setParameter("pResiJne", resiJN);
			query2.executeUpdate();
			
			sess.getTransaction().commit();
			return true;
	}
	//OK
	public static void save(TtMappingResiJne upl) {
		GenericService.save(TtMappingResiJne.class, upl, true);
	}
}
