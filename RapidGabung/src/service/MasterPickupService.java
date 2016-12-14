package service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import entity.TrCabang;
import entity.TrPickup;
import util.HibernateUtil;

public class MasterPickupService {
	public static List<TrPickup> getJadwalPickupByKodePelanggan(String kodePelanggan){
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrPickup.class);
		c.add(Restrictions.eq("kodePelanggan", kodePelanggan));
		c.add(Restrictions.eq("flag", "0"));
		List<TrPickup> data = c.list();
		s.getTransaction().commit();
		return data;
	}
	public static void setFlag(String id, Integer flag){
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrPickup.class);
		c.add(Restrictions.eq("id", id));
		List<TrPickup> data = c.list();
		s.getTransaction().commit();
		TrPickup willProcess = data.get(0);
		willProcess.setFlag(flag.toString());
		GenericService.save(TrPickup.class, willProcess, false);
	}
}
