package service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import util.HibernateUtil;

public class LaporanPerKecamatanService {

	//FA
	public static List<Map> getLaporanByParam(
			Date startDate,
			Date endDate,
			String kodePerwakilan) {
		Session session = HibernateUtil.openSession();
		String nativeSql = 
				"select " +
				"	distinct " + 
		        "        a.awb_data_entry, " +
		        "        a.tgl_create,  " +
		        "        a.pbclose,  " +
		        "        a.tujuan, " +
		        "        a.kode_perwakilan, " +
		        "        b.zona,  " +	
		        "        b.kecamatan,  " +
		        "        b.kabupaten,  " +
		        "        b.propinsi, " +
		        "        d.layanan, " +
		        "        a.harga  " +
				"from tt_data_entry a " +
				"inner join tr_harga b on a.tujuan = b.kode_zona " +
				"inner join tt_poto_timbang d on a.awb_data_entry = d.awb_poto_timbang " +
		
				"where date(a.tgl_create) between :pTglMulai and :pTglAkhir ";
				if(!kodePerwakilan.equals("All Cabang")){
					nativeSql +="      and a.kode_perwakilan = '"+kodePerwakilan+"' ";
				}
					nativeSql +="order by tgl_create, kecamatan";
				
		Query query = session.createSQLQuery(nativeSql).setParameter("pTglMulai", startDate + " 00:00:00")
				.setParameter("pTglAkhir", endDate + " 23:59:59");
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
				
		return result;
	}

}
