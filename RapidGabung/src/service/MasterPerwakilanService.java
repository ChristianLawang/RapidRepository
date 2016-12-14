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
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import entity.TrCabang;
import entity.TrPerwakilan;
import util.HibernateUtil;

public class MasterPerwakilanService {
	
	public static List<TrPerwakilan> getDataPerwakilan(String propinsi) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrPerwakilan.class);
		if (propinsi == "" || propinsi.equals("") || propinsi == null) {
		} else {
			c.add(Restrictions.eq("propinsi", propinsi));
		}
		@SuppressWarnings("unchecked")
		List<TrPerwakilan> data = c.list();
		s.getTransaction().commit();
		
		return data;
	}

	public static TrPerwakilan getDataPerwakilanById(Integer id) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrPerwakilan.class);
		c.add(Restrictions.eq("kodeZona", id));
		@SuppressWarnings("unchecked")
		TrPerwakilan data = (TrPerwakilan) c.uniqueResult();
		s.getTransaction().commit();
		return data;
	}

	public static List<TrPerwakilan> getDataPerwakilan(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select a.kode_zona, a.propinsi, a.kabupaten, a.kecamatan"
				+ ", a.kode_perwakilan, a.zona, a.regperwakilan, a.oneperwakilan, a.tgl_create, a.tgl_update "
				+ "from tr_perwakilan a;";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TrPerwakilan> returnList = new ArrayList<TrPerwakilan>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrPerwakilan everyRow = new TrPerwakilan();
			
			//get data kode zona
			everyRow.setKodeZona((String) row.get("KODE_ZONA")!=null?(String) row.get("KODE_ZONA"):"");
			
			//get data kode propinsi
			everyRow.setPropinsi((String) row.get("PROPINSI")!=null?(String) row.get("PROPINSI"):"");
			
			//get data kode kabupaten
			everyRow.setKabupaten((String) row.get("KABUPATEN")!=null?(String) row.get("KABUPATEN"):"");

			//get data kode kecamatan
			everyRow.setKecamatan((String) row.get("KECAMATAN")!=null?(String) row.get("KECAMATAN"):"");

			//get data kode perwakilan
			everyRow.setKodePerwakilan((String) row.get("KODE_PERWAKILAN")!=null?(String) row.get("KODE_PERWAKILAN"):"");
			
			//get data kode perwakilan
			everyRow.setZona((String) row.get("ZONA")!=null?(String) row.get("ZONA"):"");
				
			//get data reg
			everyRow.setRegperwakilan((String) row.get("REGPERWAKILAN")!=null?(String) row.get("REGPERWAKILAN"):"");
			
			//get data one
			everyRow.setOneperwakilan((String) row.get("ONEPERWAKILAN")!=null?(String) row.get("ONEPERWAKILAN"):"");
					
			//get tanggal created
			everyRow.setTglCreate((Date) row.get("TGL_CREATE"));
			
			//get tanggal updated
			everyRow.setTglUpdate((Date) row.get("TGL_UPDATE"));
			
			returnList.add(everyRow);
		}
		return returnList;
		
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
	
	public static Boolean updateMaserPerwakilan(String updateField, String valueField, String kdZona){
		Session sess=HibernateUtil.openSession();
//		sess.beginTransaction();
			String sql ="update tr_perwakilan set "+updateField +"=:pUpdate where KODE_ZONA=:pKdZona";
			Query query = sess.createSQLQuery(sql)
					.setParameter("pUpdate", valueField)
					.setParameter("pKdZona", kdZona);
			query.executeUpdate();
			sess.getTransaction().commit();
//			sess.close();
			return true;
	}
	
	// chris
	public static List<TrPerwakilan> getDataPerwakilan1() {
		return GenericService.getList(TrPerwakilan.class);
	}

	// chris
	public static List<TrPerwakilan> getAllPerwakilanCabangDistinct() {
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select distinct a.kode_perwakilan"
			  + " from tr_perwakilan a";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
			
		List<TrPerwakilan> returnList = new ArrayList<TrPerwakilan>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrPerwakilan everyRow = new TrPerwakilan();
				
			everyRow.setKodePerwakilan((String) row.get("KODE_PERWAKILAN"));
				
			returnList.add(everyRow);
		}
		return returnList;
	}
}
