package service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import entity.TtPotoTimbang;
import util.HibernateUtil;

public class PotoPaketService {

	// chris
	public static Integer getCountMax(String CGK, String year) {
		Session s = HibernateUtil.openSession();
		String sql = "select max(substr(a.AWB_HEADER,9,8)) from tt_header a where substr(a.AWB_HEADER,1,8) = '" + CGK+year
				+ "'";

		Query query = s.createSQLQuery(sql);
		String result = (String) query.uniqueResult();
		// s.close();
		s.getTransaction().commit();

		if (result == null) {
			return 0;
		} else {
			return Integer.parseInt(result);
		}

	}
	
	
	// chriss
	public static List<TtPotoTimbang> getPotoPaketForCabang(Date dtPeriode) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtPotoTimbang.class);
		c.add(Restrictions.gt("tglCreate", new Timestamp(dtPeriode.getTime ())));
		List<TtPotoTimbang> data = c.list();
		s.getTransaction().commit();
		return data;
	}
	public static void saveAll(List<TtPotoTimbang> potoTimbang) {
		Session s = HibernateUtil.openSession();
		for (TtPotoTimbang data : potoTimbang) {
			Criteria c = s.createCriteria(TtPotoTimbang.class);
			c.add(Restrictions.eq("awbPotoTimbang", data.getAwbPotoTimbang()));
			List<TtPotoTimbang> listFotoTim = c.list();
			if(listFotoTim.isEmpty()){
				System.out.println("---------------MELAKUKAN SAVE FOTO");
				s.save(data);
			}
		}
		s.getTransaction().commit();		
	}
}
