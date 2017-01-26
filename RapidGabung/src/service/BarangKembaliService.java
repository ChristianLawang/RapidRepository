package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import entity.TrPelanggan;
import entity.TtStatusResiBermasalah;
import javafx.beans.property.StringProperty;
import util.HibernateUtil;

public class BarangKembaliService {

	public List<Object[]> getResiBermasalah(String dtAwal, String dtAkhir) {
		Session s = HibernateUtil.openSession();
		String sql = 
				"select a.id_barang, b.penerima, a.masalah from tt_status_resi_bermasalah a, tt_data_entry b where a.id_barang = b.awb_data_entry and a.flag=0 and date(a.tgl_create) between '"+dtAwal+"' and '"+dtAkhir+"'";
		
		System.out.println(sql);
		Query query = s.createSQLQuery(sql);

		List<Object[]> list = query.list();
		s.getTransaction().commit();
		
		return list;
	}

	public static String getFotoImageByAWB(String resi) {
		Session session = HibernateUtil.openSession();
		String nativeSql = 
				"select gambar from tt_poto_timbang where awb_poto_timbang = '"+resi+"'";
		System.out.println("--> " + nativeSql);
		Query query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		Map result = (Map) query.uniqueResult();
		session.getTransaction().commit();
		return (String) result.get("GAMBAR");
	}	
}
