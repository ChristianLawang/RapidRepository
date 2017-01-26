package service;


import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import util.HibernateUtil;

public class GenericService{
	public static <T> void saveOrUpdate(Class<T> cls, T data){
		Session s=HibernateUtil.openSession();
		
		s.saveOrUpdate(data);
		
		s.getTransaction().commit();
	}
	
	public static <T> void saveOrUpdate(Session s, Class<T> cls, T data){
//		Session s=HibernateUtil.openSession();
		
		s.saveOrUpdate(data);
		
//		s.getTransaction().commit();
	}
	
	public static <T> T  load(Class<T> cls, Serializable key){
		Session s=HibernateUtil.openSession();
		
		 
		T data=(T) s.get(cls, key);
		
		s.close();
		
		
		return data;			
	}
	
	public static <T> void  save(Class<T> cls, T data, Boolean isNew){
		Session s=HibernateUtil.openSession();
		
		if(isNew){
			s.save(data);
		}
		else{
			s.update(data);
		}
		
		s.getTransaction().commit();
	}
	
	public static <T> void  update(Class<T> cls, T data, Boolean isNew){
		Session s=HibernateUtil.openSession();
		
//		Transaction tx=s.getTransaction();
//		tx.begin();
		
		s.beginTransaction();
		
		if(isNew){
			s.update(data);
		}
		else{
			s.save(data);
		}
		
//		tx.commit();
		
//		s.close();
		s.getTransaction().commit();
	}
	
	
	public static <T> Boolean  saveClass(Class<T> cls, T data, Boolean isNew){
		Session s=HibernateUtil.openSession();
		
//		Transaction tx=s.getTransaction();
//		tx.begin();
		s.beginTransaction();
		if(isNew){
			s.save(data);
			return true;
		}
		else{
			s.update(data);
		}
		
//		tx.commit();
//		
//		s.close();
		s.getTransaction().commit();
		return false;
	}
	
	public static <T> void  batchInsert(Class<T> cls, List<T> list){
		Session s=HibernateUtil.openSession();
		
//		Transaction tx=s.getTransaction();
//		tx.begin();
		
		for(T row: list){
			s.save(row);
		}
		
//		tx.commit();
//		
//		s.close();
		s.getTransaction().commit();
	}	
		
	
	public static <T> List<T>  getList(Class<T> cls){
		Session s=HibernateUtil.openSession();
		
		Criteria c=s.createCriteria(cls);
		List<T> data =  c.list();
		
//		s.close();
		s.getTransaction().commit();
		
		return data;			
	}

	public static Map<String, Object> getConfig() {

		Session session=HibernateUtil.openSession();
		String nativeSql = " select * from tr_config a limit 1";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		return (Map<String, Object>) result.get(0);
	}
	
	public static <T> Integer getSizeTable(Class<T> cls){
		Session s=HibernateUtil.openSession();
		
		Criteria c=s.createCriteria(cls);
		List<T> data =  c.list();
		
		s.getTransaction().commit();
		
		return data.size();
	}
	
	public static <T> Integer getMaxTable(Class<T> cls, String field){
		Session s=HibernateUtil.openSession();
		Criteria criteria = s
			    .createCriteria(cls)
			    .setProjection(Projections.max(field));
		Integer result = (Integer)criteria.uniqueResult();
		return result==null?0:result;
	}
	
	public static <T> String getMaxTableString(Class<T> cls, String field){
		Session s=HibernateUtil.openSession();
		Criteria criteria = s
			    .createCriteria(cls)
			    .setProjection(Projections.max(field));
		String result = (String)criteria.uniqueResult();
		if(result!=null){
			Integer intResult = Integer.parseInt(result);
			intResult++;
			return intResult.toString();
		}else{
			return "1";
		}
		
	}
	
	public static <T> String getMaxTableStringRaw(Class<T> cls, String field){
		Session s=HibernateUtil.openSession();
		Criteria criteria = s
			    .createCriteria(cls)
			    .setProjection(Projections.max(field));
		String result = (String)criteria.uniqueResult();
		return result;
	}
	
	public static String getLastVarcharID(String tabelNative, String fieldNative){
		Session session=HibernateUtil.openSession();
		String sql =
				"select "+fieldNative+" from "+tabelNative+" order by 1 desc limit 1";
		SQLQuery  query = session.createSQLQuery(sql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		Map result = (Map) query.uniqueResult();
		System.out.println(result);
		session.getTransaction().commit();
		if(result==null){
			return null;
		}else{
			return (String) result.get(fieldNative);
		}
	}
}
