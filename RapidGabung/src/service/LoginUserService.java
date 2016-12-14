package service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import entity.TrUser;
import util.HibernateUtil;

public class LoginUserService {
	public static TrUser getPelangganUserLogin(String userName, String password) {
		Session s=HibernateUtil.beginSession();
		Criteria c=s.createCriteria(TrUser.class);
			c.add(Restrictions.eq("namaUser", userName));
			c.add(Restrictions.eq("password", password));
			c.add(Restrictions.eq("status", 0));
		TrUser user = (TrUser) c.uniqueResult();
		s.close();
		return user;
	}
}
