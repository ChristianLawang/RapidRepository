/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import entity.TrJabatan;
import util.HibernateUtil;

/**
 *
 * @author HP
 */
public class MasterPosisiService{

	public static List<TrJabatan> getDataPosisi(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select a.jabatan"
			  + " from tr_jabatan a;";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TrJabatan> returnList = new ArrayList<TrJabatan>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrJabatan everyRow = new TrJabatan();
			everyRow.setJabatan((String) row.get("JABATAN"));
			
			returnList.add(everyRow);
		}
		return returnList;
		
	}
    
}
