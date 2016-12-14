package service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import VO.EntryDataShowVO;
import entity.TrCabang;
import util.DateUtil;
import util.HibernateUtil;

public class MasterCabangService {
	// chris
	public static List<TrCabang> getAllPerwakilanCabangDistinct() {
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select distinct a.kode_perwakilan"
			  + " from tr_cabang a;";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TrCabang> returnList = new ArrayList<TrCabang>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrCabang everyRow = new TrCabang();
			
			everyRow.setKodePerwakilan((String) row.get("KODE_PERWAKILAN"));
			
			returnList.add(everyRow);
		}
		return returnList;
	}
	
	public static List<TrCabang> getDataCabang_() {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrCabang.class);
		c.add(Restrictions.eq("flag", 0));
		List<TrCabang> data = c.list();
		s.getTransaction().commit();
		return data;
	}

	public static Boolean showTableSetelahDelete(String KdCab) {
		Session sess = HibernateUtil.openSession();
		
		String sql = "UPDATE tr_cabang a "
				+ "SET flag = :pFlag  "
				+ "WHERE a.kode_cabang = :pKodeCab";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pFlag", 1)
				.setParameter("pKodeCab", KdCab);
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
	
	public static Boolean updateDataCabang(String kdCabang, String kdPropinsi, String KdPerwakilan
			, String email, String nmCabang) {
		Session sess = HibernateUtil.openSession();
		
		String sql = "update tr_cabang a "
				+ "set a.kode_cabang = :pKdCabang "
				+ ", a.kode_propinsi = :pKdPropinsi "
				+ ", a.kode_perwakilan = :pKdPerwakilan "
				+ ", a.email = :pEmail "
				+ ", a.nama_cabang = :pNmCabang "
				+ ", a.tgl_update = :pUpdated "
				+ "where a.kode_cabang = :pKdCabang";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pKdCabang", kdCabang)
				.setParameter("pKdPropinsi", kdPropinsi)
				.setParameter("pKdPerwakilan", KdPerwakilan)
				.setParameter("pEmail", email)
				.setParameter("pNmCabang", nmCabang)
				.setParameter("pUpdated", DateUtil.getNow());
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
	
	public static List<TrCabang> getDataCabangByID(String id) {
		Session s=HibernateUtil.openSession();
		
		Criteria c=s.createCriteria(TrCabang.class);
		c.add(Restrictions.eq("kodeCabang", id));
		
		List<TrCabang> list = c.list();
		
		s.getTransaction().commit();
		
		return list;
	}


	
	public static Object transformSqlToJavaVariabel(Class classObject, Object sample){
		if(classObject == Integer.class){ // bila ingin Integer
			if(sample instanceof BigDecimal){ // bila dasarnya BigDecimal
				return Integer.parseInt(sample.toString());
			}else if(sample instanceof String){ // bila dasarnya String
				return Integer.parseInt((String) sample);
			}
		}else if(classObject == String.class){ // bila ingin String
			if(sample instanceof BigDecimal){ // bila dasarnya BigDecimal
				return sample.toString();
			}else if(sample instanceof String){ // bila dasarnya String
				return sample;
			}
		}
		return null;
	}
	
	public static BigInteger getBigInteger(Object value) {
	    BigInteger ret = null;
	    if ( value != null ) {
	        if ( value instanceof BigInteger ) {
	            ret = (BigInteger) value;
	        } else if ( value instanceof String ) {
	            ret = new BigInteger( (String) value );
	        } else if ( value instanceof BigDecimal ) {
	            ret = ((BigDecimal) value).toBigInteger();
	        } else if ( value instanceof Number ) {
	            ret = BigInteger.valueOf( ((Number) value).longValue() );
	        } else {
	            throw new ClassCastException( "Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigInteger." );
	        }
	    }
	    return ret;
	}
	
	public static List<EntryDataShowVO> getDataCabangTagihan(Date dtAwal, Date dtAkhir) {
		Session s = HibernateUtil.openSession();
		String sql = "select distinct b.kode_perwakilan, count(c.awb_data) AWB, d.email "
				+ "from tt_header a, tt_data_entry b, tt_poto_timbang c, tr_cabang d "
				+ "where a.awb_header = b.awb_data_entry "
				+ "and d.kode_perwakilan = c.kode_perwakilan "
				+ "and a.awb_header = c.awb_poto_timbang "
				+ "and a.resi_jne is null and b.kode_perwakilan <> 'CGK' "
				+ "and b.kode_perwakilan is not null and a.flag=0 and "
				+ "date(a.tgl_create) between :pTglMulai and :pTglAkhir "
				+ "group by b.kode_perwakilan";

		Query query = s.createSQLQuery(sql).setParameter("pTglMulai", dtAwal)
				.setParameter("pTglAkhir", dtAkhir);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setKdPerwakilan(objects[0] != null ? (String) objects[0] : "");
			en.setCount((BigInteger) objects[1]);
			en.setEmail((String) objects[2]);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}
	
	
	public static List<EntryDataShowVO> getDataCabangExportExcell(Date dtAwal, Date dtAkhir, String strKdPerw) {
		Session s = HibernateUtil.openSession();
		String sql = "select c.awb_data, a.tgl_create, b.penerima, b.telp_penerima "
				+ "from tt_header a, tt_data_entry b, tt_poto_timbang c "
				+ "where a.awb_header = b.awb_data_entry "
				+ "and a.awb_header = c.awb_poto_timbang "
				+ "and a.flag=0 and a.resi_jne is null and b.kode_perwakilan=:pKdPerw and "
				+ "date(a.tgl_create) between :pTglMulai and :pTglAkhir ";

		Query query = s.createSQLQuery(sql)
				.setParameter("pKdPerw", strKdPerw)
				.setParameter("pTglMulai", dtAwal)
				.setParameter("pTglAkhir", dtAkhir);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0] != null ? (String) objects[0] : "");
			en.setCreated((Date) objects[1]);
			en.setPenerima(objects[2] != null ? (String) objects[2] : "");
			en.setNoTlpn(objects[3] != null ? (String) objects[3] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}
	
	public static List<TrCabang> getDataCabang(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select a.kode_cabang, a.nama_cabang"
			  + " from tr_cabang a;";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TrCabang> returnList = new ArrayList<TrCabang>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrCabang everyRow = new TrCabang();
			
			everyRow.setKodeCabang((String) row.get("KODE_CABANG"));
			
			everyRow.setNamaCabang((String) row.get("NAMA_CABANG"));
			
			returnList.add(everyRow);
		}
		return returnList;
		
	}
	
}
