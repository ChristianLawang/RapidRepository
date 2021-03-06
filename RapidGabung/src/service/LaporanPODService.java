package service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;

import VO.EntryDataShowVO;
import VO.KurirInVO;
import VO.LapPODVO;
import entity.TrKurir;
import entity.TrPerwakilan;
import util.HibernateUtil;

public class LaporanPODService {
	public static List<EntryDataShowVO> getListPrintKurIn(String kdPerwakilan, Date tglAwl, Date tglAkhir) {
		Session s = HibernateUtil.openSession();
//		String sql = "select distinct b.kode_perwakilan, "
//		+ "count(a.awb_header) jmlhPaket, "
//		+ "(select count(z.inbound_flag) from tt_header z, tt_data_entry y "
//		+ "where z.awb_header=y.awb_data_entry and z.inbound_flag=1 and y.kode_perwakilan=b.kode_perwakilan) sudahReport, "
//		+ "(select count(d.inbound_flag) from tt_header d, tt_data_entry e "
//		+ "where d.awb_header=e.awb_data_entry and d.inbound_flag is null and e.kode_perwakilan=b.kode_perwakilan) blmReport, "
//		+ "(select count(f.inbound_flag) from tt_header f, tt_data_entry g "
//		+ "where f.awb_header=g.awb_data_entry and f.inbound_flag=0 and g.kode_perwakilan=b.kode_perwakilan) masalah "
//		+ "from tt_header a, tt_data_entry b "
//		+ "where a.awb_header = b.awb_data_entry "
//		+ "and b.kode_perwakilan is not null ";
		
		String sql = 
			"select " +
			"       distinct b.kode_perwakilan, " +
			" 		case when t.jumlah_awb is null then 0 else t.jumlah_awb end as jmlhPaket, " +
			"       case when x.sudah_report is null then 0 else x.sudah_report end as sudahReport, " +
			"       case when y.belum_report is null then 0 else y.belum_report end as blmReport, " +
			"       case when z.masalah is null then 0 else z.masalah end as masalah       " +
			       
			"from tt_header as a  " +
			"inner join tt_data_entry as b on a.awb_header = b.awb_data_entry " +
			"left join ( " +
			"    select  " +
			"           b.kode_perwakilan,  " +
			"           count(a.awb_header) as jumlah_awb " +
			"    from  " +
			"         tt_header as a  " +
			"         inner join tt_data_entry as b on a.awb_header = b.awb_data_entry " +
			"    where " +
			"         date(a.tgl_create) between '"+ tglAwl +"' and '" +tglAkhir +"'  " +
			"    group by b.kode_perwakilan " +
			") as t on b.kode_perwakilan = t.kode_perwakilan " +
			"left join ( " +
			"    select  " +
			"           b.kode_perwakilan,  " +
			"           count(a.awb_header) as masalah " +
			"    from  " +
			"         tt_header as a  " +
			"         inner join tt_data_entry as b on a.awb_header = b.awb_data_entry  " +
			"    where a.inbound_flag = 0  " +
			"          and date(a.tgl_create) between '"+ tglAwl +"' and '" +tglAkhir +"'  " +
			"    group by b.kode_perwakilan " +
			") as z on b.kode_perwakilan = z.kode_perwakilan " +
			"left join ( " +
			"    select  " +
			"           b.kode_perwakilan,  " +
			"           count(a.awb_header) belum_report " +
			"    from  " +
			"         tt_header as a  " +
			"         inner join tt_data_entry as b on a.awb_header = b.awb_data_entry  " +
			"    where a.inbound_flag is null " +
			"          and date(a.tgl_create) between '"+ tglAwl +"' and '" +tglAkhir +"'  " +
			"    group by b.kode_perwakilan " +
			") as y on b.kode_perwakilan = y.kode_perwakilan " +
			"left join ( " +
			"    select  " +
			"           b.kode_perwakilan,  " +
			"           count(a.awb_header) as sudah_report " +
			"    from  " +
			"         tt_header as a  " +
			"         inner join tt_data_entry as b on a.awb_header = b.awb_data_entry  " +
			"    where a.inbound_flag=1 " +
			"          and date(a.tgl_create) between '"+ tglAwl +"' and '" +tglAkhir +"'  " +
			"    group by b.kode_perwakilan " +
			") as x on b.kode_perwakilan = x.kode_perwakilan";
		
		if(kdPerwakilan == null || kdPerwakilan.equals("")){
		}else{
			sql += " where b.kode_perwakilan =  '" +kdPerwakilan +"' " ;
		}
		sql += " order by b.kode_perwakilan";
		System.out.println("--> sql : " + sql);
		Query query = s.createSQLQuery(sql);
		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setKdPerwakilan(objects[0] != null ? (String) objects[0] : "");
			en.setJmlhPaket(objects[2] != null ? (BigInteger) objects[1] : BigInteger.ZERO);
			en.setSudahReport(objects[2] != null ? (BigInteger) objects[2] : BigInteger.ZERO);
			en.setBlmReport(objects[3] != null ? (BigInteger) objects[3] : BigInteger.ZERO);
			en.setJmlhMasalah(objects[4] != null ? (BigInteger) objects[4] : BigInteger.ZERO);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
	
	public static List<TrPerwakilan> getDataPerwakilan(){
		Session session=HibernateUtil.openSession();
		String nativeSql = "select distinct kode_perwakilan from tr_perwakilan where flag=0";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TrPerwakilan> returnList = new ArrayList<TrPerwakilan>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrPerwakilan everyRow = new TrPerwakilan();
			everyRow.setKodePerwakilan((String) row.get("KODE_PERWAKILAN")!=null?(String) row.get("KODE_PERWAKILAN"):"");
			returnList.add(everyRow);
		}
		return returnList;
		
	}
	
	public static List<EntryDataShowVO> getReportPOD(String kdPerwakilan, Date tglAwl, Date tglAkhir, String menu) {
		Session s = HibernateUtil.openSession();
		String sql = "select b.kode_perwakilan, a.awb_header from tt_header a, tt_data_entry b "
				+ "where a.awb_header = b.awb_data_entry and b.kode_perwakilan=:pPwerwakilan "
				+ "and b.kode_perwakilan is not null "
				+ "and a.tgl_create between :pTglAwl and :pTglAkhir ";
		
		if(menu.equals("mnSudahReport")){
			sql += "and a.inbound_flag=1";
		}else if(menu.equals("mnBelumReport")){
			sql += "and a.inbound_flag is null";
		}else if(menu.equals("mnMasalah")){
			sql += "and a.inbound_flag=0";
		}
		
		Query query = s.createSQLQuery(sql)
				.setParameter("pPwerwakilan", kdPerwakilan)
				.setParameter("pTglAwl", tglAwl)
				.setParameter("pTglAkhir", tglAkhir);
		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setKdPerwakilan(objects[0] != null ? (String) objects[0] : "");
			en.setAwbData(objects[1] != null ? (String) objects[1] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
}
