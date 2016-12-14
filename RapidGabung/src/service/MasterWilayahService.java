/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import entity.TrCabang;
import tpb.helper.JpaDatabaseHelper;
import util.HibernateUtil;
import entity.TrUser;

/**
 *
 * @author HP
 */
public class MasterWilayahService{

	public static List<TrCabang> getDataCabang(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select a.nama_cabang"
			  + " from tr_cabang a;";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TrCabang> returnList = new ArrayList<TrCabang>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrCabang everyRow = new TrCabang();
			
			everyRow.setNamaCabang((String) row.get("NAMA_CABANG"));
			
			returnList.add(everyRow);
		}
		return returnList;
		
	}
    
}
