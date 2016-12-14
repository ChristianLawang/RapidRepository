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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import VO.EntryDataShowVO;
import entity.TtDataEntry;
import entity.TtPotoTimbang;
import util.HibernateUtil;

public class DataEntryService {
	// chris
	public static Integer getCount() {
		Session session = HibernateUtil.openSession();
		Criteria crit = session.createCriteria(TtDataEntry.class);
		crit.setProjection(Projections.rowCount());
		long lngCount = (long) crit.uniqueResult();
		session.getTransaction().commit();
		return (int) lngCount;
	}

	// chris
	public static void save(TtPotoTimbang potoPaket) {
		GenericService.save(TtPotoTimbang.class, potoPaket, true);
	}
	// chris
	public static TtDataEntry getDataPaketByIdDataPaket(String idDataPaket) {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtDataEntry.class);
		c.add(Restrictions.eq("awbDataEntry", idDataPaket));
		List<TtDataEntry> data = c.list();
		s.getTransaction().commit();
		return data.get(0);

	}

	public static void update(TtDataEntry dPaket) {
		GenericService.save(TtDataEntry.class, dPaket, false);
	}
}
