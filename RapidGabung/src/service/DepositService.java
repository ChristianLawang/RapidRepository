package service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import VO.DepositVO;
import util.HibernateUtil;

public class DepositService {
	public static List<DepositVO> getDataDeposit(String kdPelanggan) {
		Session s = HibernateUtil.openSession();
		String sql = "select id, kd_pelanggan, sisa_deposit from tt_deposit where kd_pelanggan = :pKdPelanggan";
		Query query = s.createSQLQuery(sql).setParameter("pKdPelanggan", kdPelanggan);

		List<DepositVO> returnList = new ArrayList<DepositVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			DepositVO en = new DepositVO();
			en.setId(objects[0] != null ? (int) objects[0] : 0);
			en.setKdPelanggan(objects[1] != null ? (String) objects[1] : "");
			en.setSisaDeposit(objects[2] != null ? (int) objects[2] : 0);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static Boolean insertData(String kdPelanggan, Integer sisaDeposit) {
		Session sess = HibernateUtil.openSession();
		String sql = "INSERT INTO TT_DEPOSIT(KD_PELANGGAN, SISA_DEPOSIT) VALUES(:pKdPelanggan, :pSisaDeposit) ";
		Query query = sess.createSQLQuery(sql).setParameter("pKdPelanggan", kdPelanggan).setParameter("pSisaDeposit",
				sisaDeposit);
		query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}

	public static Boolean updateData(String kdPelanggan, Integer sisaDeposit) {
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE TT_DEPOSIT set SISA_DEPOSIT = :pSisaDeposit where KD_PELANGGAN = :pKdPelanggan ";
		Query query = sess.createSQLQuery(sql).setParameter("pSisaDeposit", sisaDeposit).setParameter("pKdPelanggan",
				kdPelanggan);
		query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
}
