package service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import entity.TtHeader;
import util.HibernateUtil;

public class HeaderService {

	public static void save(TtHeader dPoto) {
		GenericService.save(TtHeader.class, dPoto, true);
	}
	// chris
	public static TtHeader getDataPotoByPotoPaketID(String idPotoPaket) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtHeader.class);
		c.add(Restrictions.eq("awbHeader", idPotoPaket));
		List<TtHeader> data = c.list();
		s.getTransaction().commit();
		return data.get(0);
	}
}
