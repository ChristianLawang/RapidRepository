package service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import util.HibernateUtil;

public class KPICabangKurirService {
	
	public static List<Map> getLaporanHeaderKirimByParam(Date startDate, Date endDate, String kdPerwakilan) {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select date(a.TGL_CREATE) as TGL,b.KODE_PERWAKILAN,count(distinct(a.ID_BARANG)) as KIRIM " +
				"from tt_status_kurir_out a join tt_data_entry b ON a.ID_BARANG=b.AWB_DATA_ENTRY " +
				"where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir ";
		
				if(!kdPerwakilan.equals("All Perwakilan")){
					nativeSql += " and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' GROUP BY b.KODE_PERWAKILAN ";
				} else {
					nativeSql += " GROUP BY b.KODE_PERWAKILAN ";
				}
		
		Query query = session.createSQLQuery(nativeSql).setParameter("pTglMulai", startDate + " 00:00:00")
			.setParameter("pTglAkhir", endDate + " 23:59:59");
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
								
		return result;
	}
	
	public static List<Map> getLaporanHeaderTerimaByParam(Date startDate, Date endDate, String kdPerwakilan) {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select date(a.TGL_CREATE) as TGL,b.KODE_PERWAKILAN,count(distinct(a.ID_BARANG)) as 'TERIMA' " +
				"from tt_status_kurir_in a JOIN tt_data_entry b ON a.ID_BARANG=b.AWB_DATA_ENTRY " +
				"where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir ";
		
				if(!kdPerwakilan.equals("All Perwakilan")){
					nativeSql += " and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' GROUP BY b.KODE_PERWAKILAN ";
				} else {
					nativeSql += " GROUP BY b.KODE_PERWAKILAN ";
				}
		
		Query query = session.createSQLQuery(nativeSql).setParameter("pTglMulai", startDate + " 00:00:00")
			.setParameter("pTglAkhir", endDate + " 23:59:59");
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
								
		return result;
	}
	
	public static List<Map> getLaporanFooterByParam(Date startDate, Date endDate, String kdPerwakilan) {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select xa.TGL_CREATE, 'AU' as 'STATUS',count(xa.status) as 'JML' from (SELECT " +
				"a.TGL_CREATE,a.ID_BARANG, a.PENERIMA," +
				"case " +
				"when a.masalah =  '1 - AU (Antar Ulang)' or a.masalah =  'AU' then 'AU' " +
				"when a.masalah =  '2 - BA (Bad Address)' or a.masalah =  'BA' or a.masalah ='2 - BA (Bad Address)' then 'BA' " +
				"when a.masalah =  '3 - NTH (Not at Home)' or a.masalah =  'NTH' then 'NTH' " +
				"when a.masalah =  '4 - CODA (Closed of Delivery Attempt)' or a.masalah =  'CODA' then 'CODA' " +
				"when a.masalah =  '5 - CrisCross' or a.masalah =  'CC' then 'CC' " +
				"when a.masalah =  '8 - Pending' then 'PENDING' " +
				"WHEN a.MASALAH =  '9 - Retur' then 'RETUR' " +
				"when a.masalah =  '0 - Miss Route' or a.masalah =  '6 - Miss Route' or a.masalah =  'MR' then 'MR' " +
				"when a.masalah is null and a.PENERIMA is NOT NULL or a.masalah='-1 - Diterima' or a.masalah='-1- Diterima' then 'DITERIMA' " +
				"end as 'status' " +
				"from tt_status_kurir_in a JOIN tt_data_entry b ON a.ID_BARANG=b.AWB_DATA_ENTRY where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir";
		
				if(!kdPerwakilan.equals("All Perwakilan")){
					nativeSql += " and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' order by a.TGL_CREATE)  as xa where status='AU' ";
				} else {
					nativeSql += " order by a.TGL_CREATE)  as xa where status='AU' ";
				}
				
		nativeSql += "union ALL " +
				"select xa.TGL_CREATE, 'BA' as 'STATUS',count(xa.status) as 'JML_BA' from (SELECT " +
				"a.TGL_CREATE,a.ID_BARANG, a.PENERIMA," +
				"case " +
				"when a.masalah =  '1 - AU (Antar Ulang)' or a.masalah =  'AU' then 'AU' " +
				"when a.masalah =  '2 - BA (Bad Address)' or a.masalah =  'BA' or a.masalah ='2 - BA (Bad Address)' then 'BA' " +
				"when a.masalah =  '3 - NTH (Not at Home)' or a.masalah =  'NTH' then 'NTH' " +
				"when a.masalah =  '4 - CODA (Closed of Delivery Attempt)' or a.masalah =  'CODA' then 'CODA' " +
				"when a.masalah =  '5 - CrisCross' or a.masalah =  'CC' then 'CC' " +
				"when a.masalah =  '8 - Pending' then 'PENDING' " +
				"WHEN a.MASALAH =  '9 - Retur' then 'RETUR' " +
				"when a.masalah =  '0 - Miss Route' or a.masalah =  '6 - Miss Route' or a.masalah =  'MR' then 'MR' " +
				"when a.masalah is null and a.PENERIMA is NOT NULL or a.masalah='-1 - Diterima' or a.masalah='-1- Diterima' then 'DITERIMA' " +
				"end as 'status' " +
				"from tt_status_kurir_in a JOIN tt_data_entry b ON a.ID_BARANG=b.AWB_DATA_ENTRY where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir";
		
				if(!kdPerwakilan.equals("All Perwakilan")){
					nativeSql += " and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' order by a.TGL_CREATE)  as xa where status='BA' ";
				} else {
					nativeSql += " order by a.TGL_CREATE)  as xa where status='BA' ";
				}
				
		nativeSql += "union ALL " +
				"select xa.TGL_CREATE, 'CODA' as 'STATUS',count(xa.status) as 'JML_CODA' from (SELECT " +
				"a.TGL_CREATE,a.ID_BARANG, a.PENERIMA," +
				"case " +
				"when a.masalah =  '1 - AU (Antar Ulang)' or a.masalah =  'AU' then 'AU' " +
				"when a.masalah =  '2 - BA (Bad Address)' or a.masalah =  'BA' or a.masalah ='2 - BA (Bad Address)' then 'BA' " +
				"when a.masalah =  '3 - NTH (Not at Home)' or a.masalah =  'NTH' then 'NTH' " +
				"when a.masalah =  '4 - CODA (Closed of Delivery Attempt)' or a.masalah =  'CODA' then 'CODA' " +
				"when a.masalah =  '5 - CrisCross' or a.masalah =  'CC' then 'CC' " +
				"when a.masalah =  '8 - Pending' then 'PENDING' " +
				"WHEN a.MASALAH =  '9 - Retur' then 'RETUR' " +
				"when a.masalah =  '0 - Miss Route' or a.masalah =  '6 - Miss Route' or a.masalah =  'MR' then 'MR' " +
				"when a.masalah is null and a.PENERIMA is NOT NULL or a.masalah='-1 - Diterima' or a.masalah='-1- Diterima' then 'DITERIMA' " +
				"end as 'status' " +
				"from tt_status_kurir_in a JOIN tt_data_entry b ON a.ID_BARANG=b.AWB_DATA_ENTRY where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir";
		
				if(!kdPerwakilan.equals("All Perwakilan")){
					nativeSql += " and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' order by a.TGL_CREATE)  as xa where status='CODA' ";
				} else {
					nativeSql += " order by a.TGL_CREATE)  as xa where status='CODA' ";
				}
		
		nativeSql += "union ALL " +
				"select xa.TGL_CREATE, 'NTH' as 'STATUS',count(xa.status) as 'JML_NTH' from (SELECT " +
				"a.TGL_CREATE,a.ID_BARANG, a.PENERIMA," +
				"case " +
				"when a.masalah =  '1 - AU (Antar Ulang)' or a.masalah =  'AU' then 'AU' " +
				"when a.masalah =  '2 - BA (Bad Address)' or a.masalah =  'BA' or a.masalah ='2 - BA (Bad Address)' then 'BA' " +
				"when a.masalah =  '3 - NTH (Not at Home)' or a.masalah =  'NTH' then 'NTH' " +
				"when a.masalah =  '4 - CODA (Closed of Delivery Attempt)' or a.masalah =  'CODA' then 'CODA' " +
				"when a.masalah =  '5 - CrisCross' or a.masalah =  'CC' then 'CC' " +
				"when a.masalah =  '8 - Pending' then 'PENDING' " +
				"WHEN a.MASALAH =  '9 - Retur' then 'RETUR' " +
				"when a.masalah =  '0 - Miss Route' or a.masalah =  '6 - Miss Route' or a.masalah =  'MR' then 'MR' " +
				"when a.masalah is null and a.PENERIMA is NOT NULL or a.masalah='-1 - Diterima' or a.masalah='-1- Diterima' then 'DITERIMA' " +
				"end as 'status' " +
				"from tt_status_kurir_in a JOIN tt_data_entry b ON a.ID_BARANG=b.AWB_DATA_ENTRY where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir";
		
				if(!kdPerwakilan.equals("All Perwakilan")){
					nativeSql += " and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' order by a.TGL_CREATE)  as xa where status='NTH' ";
				} else {
					nativeSql += " order by a.TGL_CREATE)  as xa where status='NTH' ";
				}
				
		nativeSql += "union ALL " +
				"select xa.TGL_CREATE, 'CC' as 'STATUS',count(xa.status) as 'JML_CC' from (SELECT " +
				"a.TGL_CREATE,a.ID_BARANG, a.PENERIMA," +
				"case " +
				"when a.masalah =  '1 - AU (Antar Ulang)' or a.masalah =  'AU' then 'AU' " +
				"when a.masalah =  '2 - BA (Bad Address)' or a.masalah =  'BA' or a.masalah ='2 - BA (Bad Address)' then 'BA' " +
				"when a.masalah =  '3 - NTH (Not at Home)' or a.masalah =  'NTH' then 'NTH' " +
				"when a.masalah =  '4 - CODA (Closed of Delivery Attempt)' or a.masalah =  'CODA' then 'CODA' " +
				"when a.masalah =  '5 - CrisCross' or a.masalah =  'CC' then 'CC' " +
				"when a.masalah =  '8 - Pending' then 'PENDING' " +
				"WHEN a.MASALAH =  '9 - Retur' then 'RETUR' " +
				"when a.masalah =  '0 - Miss Route' or a.masalah =  '6 - Miss Route' or a.masalah =  'MR' then 'MR' " +
				"when a.masalah is null and a.PENERIMA is NOT NULL or a.masalah='-1 - Diterima' or a.masalah='-1- Diterima' then 'DITERIMA' " +
				"end as 'status' " +
				"from tt_status_kurir_in a JOIN tt_data_entry b ON a.ID_BARANG=b.AWB_DATA_ENTRY where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir";
		
				if(!kdPerwakilan.equals("All Perwakilan")){
					nativeSql += " and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' order by a.TGL_CREATE)  as xa where status='CC' ";
				} else {
					nativeSql += " order by a.TGL_CREATE)  as xa where status='CC' ";
				}
				
		nativeSql += "union ALL " +
				"select xa.TGL_CREATE, 'MR' as 'STATUS',count(xa.status) as 'JML_MR' from (SELECT " +
				"a.TGL_CREATE,a.ID_BARANG, a.PENERIMA," +
				"case " +
				"when a.masalah =  '1 - AU (Antar Ulang)' or a.masalah =  'AU' then 'AU' " +
				"when a.masalah =  '2 - BA (Bad Address)' or a.masalah =  'BA' or a.masalah ='2 - BA (Bad Address)' then 'BA' " +
				"when a.masalah =  '3 - NTH (Not at Home)' or a.masalah =  'NTH' then 'NTH' " +
				"when a.masalah =  '4 - CODA (Closed of Delivery Attempt)' or a.masalah =  'CODA' then 'CODA' " +
				"when a.masalah =  '5 - CrisCross' or a.masalah =  'CC' then 'CC' " +
				"when a.masalah =  '8 - Pending' then 'PENDING' " +
				"WHEN a.MASALAH =  '9 - Retur' then 'RETUR' " +
				"when a.masalah =  '0 - Miss Route' or a.masalah =  '6 - Miss Route' or a.masalah =  'MR' then 'MR' " +
				"when a.masalah is null and a.PENERIMA is NOT NULL or a.masalah='-1 - Diterima' or a.masalah='-1- Diterima' then 'DITERIMA' " +
				"end as 'status' " +
				"from tt_status_kurir_in a JOIN tt_data_entry b ON a.ID_BARANG=b.AWB_DATA_ENTRY where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir";
		
				if(!kdPerwakilan.equals("All Perwakilan")){
					nativeSql += " and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' order by a.TGL_CREATE)  as xa where status='MR' ";
				} else {
					nativeSql += " order by a.TGL_CREATE)  as xa where status='MR' ";
				}
				
		Query query = session.createSQLQuery(nativeSql).setParameter("pTglMulai", startDate + " 00:00:00")
			.setParameter("pTglAkhir", endDate + " 23:59:59");
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
							
		return result;
	}
	
	public static List<String> getListKurir(String kdPerwakilan) {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select distinct b.NAMA " +
				"from tr_kurir b " +
				"where b.ID_JABATAN = 'Kurir' and b.KODE_PERWAKILAN = :pKdPerwakilan " +
				"ORDER BY b.NAMA ";
		
		Query query = session.createSQLQuery(nativeSql).setParameter("pKdPerwakilan", kdPerwakilan);
//		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
								
		return result;
	}
	
	public static List<Map> getLaporanDetailByParam(Date startDate, Date endDate, String kdPerwakilan, String nama) {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select xx.NAMA,xx.KODE_PERWAKILAN,sum(xx.KIRIM) as 'PAKET_KIRIM',sum(xx.TERIMA) as 'PAKET_TERIMA' " +
				"from (select b.NAMA,b.KODE_PERWAKILAN,COUNT(a.ID_BARANG) as 'KIRIM', 0 as 'TERIMA' " +
				"from tt_status_kurir_out a " +
				"JOIN tr_kurir b on a.ID_KURIR=b.NIK " +
				"where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir " +
				"and b.ID_JABATAN='Kurir' and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' and b.NAMA = '"+nama+"' " +
				"group by b.NAMA " +
				"union all " +
				"select bb.NAMA,bb.KODE_PERWAKILAN,0 as 'KIRIM'," +
				"COUNT(aa.ID_BARANG) as 'TERIMA' " +
				" from tt_status_kurir_in aa " +
				"JOIN tr_kurir bb ON aa.ID_KURIR=bb.NIK " +
				"where date(aa.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir " +
				"and bb.ID_JABATAN='Kurir' and bb.KODE_PERWAKILAN = '"+kdPerwakilan+"' and bb.NAMA = '"+nama+"' " +
				"group by bb.NAMA) as XX group BY xx.NAMA,xx.KODE_PERWAKILAN ORDER BY XX.NAMA";
		
		Query query = session.createSQLQuery(nativeSql).setParameter("pTglMulai", startDate + " 00:00:00")
			.setParameter("pTglAkhir", endDate + " 23:59:59");
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
								
		return result;
	}
	
	public static List<Map> getLaporanDetailPersenByParam(Date startDate, Date endDate, String kdPerwakilan) {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select xx.NAMA,xx.KODE_PERWAKILAN,sum(xx.KIRIM) as 'PAKET_KIRIM',sum(xx.TERIMA) as 'PAKET_TERIMA', " +
				"ROUND((sum(xx.TERIMA)*100)/sum(xx.KIRIM)) as 'PERSEN' " +
				"from (select b.NAMA,b.KODE_PERWAKILAN,COUNT(a.ID_BARANG) as 'KIRIM', 0 as 'TERIMA' " +
				"from tt_status_kurir_out a " +
				"JOIN tr_kurir b on a.ID_KURIR=b.NIK " +
				"where date(a.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir " +
				"and b.ID_JABATAN='Kurir' and b.KODE_PERWAKILAN = '"+kdPerwakilan+"' " +
				"group by b.NAMA " +
				"union all " +
				"select bb.NAMA,bb.KODE_PERWAKILAN,0 as 'KIRIM'," +
				"COUNT(aa.ID_BARANG) as 'TERIMA' " +
				" from tt_status_kurir_in aa " +
				"JOIN tr_kurir bb ON aa.ID_KURIR=bb.NIK " +
				"where date(aa.TGL_CREATE) BETWEEN :pTglMulai and :pTglAkhir " +
				"and bb.ID_JABATAN='Kurir' and bb.KODE_PERWAKILAN = '"+kdPerwakilan+"' " +
				"group by bb.NAMA) as XX group BY xx.NAMA,xx.KODE_PERWAKILAN ORDER BY PERSEN desc";
		
		Query query = session.createSQLQuery(nativeSql).setParameter("pTglMulai", startDate + " 00:00:00")
			.setParameter("pTglAkhir", endDate + " 23:59:59");
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
								
		return result;
	}

}