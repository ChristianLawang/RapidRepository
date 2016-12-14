package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import entity.TrCabang;
import entity.TrMasalah;
import entity.TrPelanggan;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import util.HibernateUtil;

public class MasterMasalahService {

	public static List<TrMasalah> getDataMasalah(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				"select a.id_masalah, a.nama_masalah"
				+ " from tr_masalah a;";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		System.out.println(result);
		session.getTransaction().commit();
		
		List<TrMasalah> returnList = new ArrayList<TrMasalah>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrMasalah everyRow = new TrMasalah();
			
			everyRow.setIdMasalah((Integer) row.get("ID_MASALAH"));
			System.out.println(row.get("ID_MASALAH"));
			
			everyRow.setNamaMasalah((String) row.get("NAMA_MASALAH")!=null?(String) row.get("NAMA_MASALAH"):"");
			
			
			returnList.add(everyRow);
		}
		return returnList;
		
	}
}
