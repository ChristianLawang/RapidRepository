package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import VO.EntryDataShowVO;
import entity.TrUser;
import util.HibernateUtil;

public class UserService {
	public static List<TrUser> getDataUser(String namaUser) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrUser.class);
		if (namaUser == "" || namaUser.equals("") || namaUser == null) {
		} else {
			c.add(Restrictions.eq("namaUser", namaUser));
		}
		List<TrUser> data = c.list();
		s.getTransaction().commit();
		return data;
	}
	

	public static TrUser getDataUserById(Integer id) {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TrUser.class);
		c.add(Restrictions.eq("idUser", id));
		TrUser data = (TrUser) c.uniqueResult();
		s.getTransaction().commit();
		return data;
	}
	
}
