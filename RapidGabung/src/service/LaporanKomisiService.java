package service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import util.HibernateUtil;

public class LaporanKomisiService {

	public static List<String> getNamaSales() {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select distinct a.nama_sales from tr_pelanggan a";
		
		Query query = session.createSQLQuery(nativeSql);
		List result = query.list();
		session.getTransaction().commit();
				
		return result;
		
//		Criteria c = session.createCriteria(TrKurir.class);
//		List<TrKurir> data = c.list();
//		session.getTransaction().commit();
//		return data;
	}
	
	public static List<Map> getLaporanByParam(Date startDate, Date endDate, String namaSales) {
		Session session = HibernateUtil.openSession();
		String nativeSql = 
				"select " +
				"        b.NAMA_SALES, " +
				"        a.PENGIRIM,  " +
				"        count(a.AWB_DATA_ENTRY) AWB,  " +
				"        sum(a.PBCLOSE) BERAT,  " +
				"        round(sum(a.BCLOSE),2) BERAT_ASLI, " +
				"        sum(a.HARGA) HARGA_AWAL, " +
				"        round(sum((a.HARGA)*b.DISKON_RAPID/100),0) DISKON,  " +
				"        round(sum(a.HARGA)-sum((a.HARGA)*b.DISKON_RAPID/100),0) HARGA_SETELAH_DISKON,  " +
				"        CASE WHEN c.FLAG_LUNAS='1' THEN 'LUNAS' else 'BELUM' END STATUS_PEMBAYARAN " +
				"from tt_data_entry a " +
				"inner join tr_pelanggan b on a.PENGIRIM = b.KODE_PELANGGAN " +
				"inner join tt_poto_timbang c on a.awb_data_entry = c.awb_poto_timbang " +
				
				"where date(a.TGL_CREATE) between :pTglMulai and :pTglAkhir ";
				if(!namaSales.equals("All Sales")){
					nativeSql +="      and b.NAMA_SALES = '"+namaSales+"' ";
				}
					nativeSql +="GROUP BY b.NAMA_SALES,a.PENGIRIM,c.KODE_PICKUP";
		
		Query query = session.createSQLQuery(nativeSql).setParameter("pTglMulai", startDate + " 00:00:00")
				.setParameter("pTglAkhir", endDate + " 23:59:59");
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
							
		return result;
	}
	
	public static List<Map> getSalesData(String namaSales) {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select distinct a.nama_sales, a.jabatan1, a.jabatan2 from tr_pelanggan a where a.nama_sales = :pNamaSales ";
		
		Query query = session.createSQLQuery(nativeSql)
				.setParameter("pNamaSales", namaSales);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
				
		return result;
	}
}