package service;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import entity.TtDataEntry;
import entity.TtHeader;
import entity.TtPotoTimbang;
import util.HibernateUtil;

public class PotoTimbangService {
	// chris
	public static Integer getCountMax(String CGK) {
		Session s = HibernateUtil.openSession();
		String sql = "select max(substr(a.AWB_DATA,9,8)) from tt_poto_paket a where substr(a.AWB_DATA,1,6) = '" + CGK
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

	// chris
	public static Integer getCount() {
		Session session = HibernateUtil.openSession();
		Criteria crit = session.createCriteria(TtPotoTimbang.class);
		crit.setProjection(Projections.rowCount());
		long lngCount = (long) crit.uniqueResult();

		return (int) lngCount;
		// return GenericService.getList(TtPotoTimbang.class).size();
	}

	// chris
	public static void save(TtDataEntry dataPaket) {
		GenericService.save(TtDataEntry.class, dataPaket, true);
	}
	
	// chris
	public static TtPotoTimbang getPotoPaketByAWB(String awb) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtPotoTimbang.class);
		c.add(Restrictions.eq("awbData", awb));

		List<TtPotoTimbang> list = c.list();

		// s.close();
		s.getTransaction().commit();
		return list.get(0);
	}

	public static void update(TtPotoTimbang pp) {
		GenericService.save(TtPotoTimbang.class, pp, false);
	}

	// chris
	public static void insertFotoTimbangTransaction(TtDataEntry dataPaket, TtPotoTimbang potoPaket, TtHeader dataPoto) {

		Session s = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = s.getTransaction();
		tx.begin();

		s.save(dataPaket);
		s.save(potoPaket);
		s.save(dataPoto);

		tx.commit();

	}
	
	// chris
	public static void updateLayanan(String AWB, String strLayanan){
		TtPotoTimbang pt = PotoTimbangService.getPotoPaketByAWB(AWB);
		pt.setLayanan(strLayanan);
		GenericService.save(TtPotoTimbang.class, pt, false);
	}
}
