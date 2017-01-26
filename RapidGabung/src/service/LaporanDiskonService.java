package service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import util.HibernateUtil;

public class LaporanDiskonService {

	public static List<String> getNamaPelanggan() {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select distinct a.kode_pelanggan from tr_pelanggan a";
		
		Query query = session.createSQLQuery(nativeSql);
		List result = query.list();
		session.getTransaction().commit();
				
		return result;
	}
	
	public static List<Map> getLaporanByParam(Date startDate, Date endDate, String namaPelanggan) {
		Session session = HibernateUtil.openSession();
		String nativeSql = 
				"select " +
				"        a.KODE_PELANGGAN, " +
				"        a.DISKON_RAPID,  " +
				"        a.DISKON_JNE,  " +
				"        a.TGL_MULAI_DISKON " +
				"from tr_diskon a " +
				"where date(a.TGL_CREATE) between :pTglMulai and :pTglAkhir ";
				if(!namaPelanggan.equals("All Pelanggan")){
					nativeSql +="      and a.KODE_PELANGGAN = '"+namaPelanggan+"' ";
				}
					nativeSql +="ORDER BY a.TGL_CREATE";
		
		Query query = session.createSQLQuery(nativeSql).setParameter("pTglMulai", startDate + " 00:00:00")
				.setParameter("pTglAkhir", endDate + " 23:59:59");
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
							
		return result;
	}
}