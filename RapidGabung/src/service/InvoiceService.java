package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import VO.InvoiceVO;
import util.HibernateUtil;

public class InvoiceService {
	public static List<InvoiceVO> getDataInvoice(String kdPickup) {
		Session s = HibernateUtil.openSession();
		String sql = "select id, kode_pickup, tanggal_bayar, bank_id from tt_invoice where kode_pickup = :pKdPickup";
		Query query = s.createSQLQuery(sql).setParameter("pKdPickup", kdPickup);

		List<InvoiceVO> returnList = new ArrayList<InvoiceVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			InvoiceVO en = new InvoiceVO();
			en.setId(objects[0] != null ? (int) objects[0] : 0);
			en.setKdPickup(objects[1] != null ? (String) objects[1] : "");
			en.setTglBayar(objects[2] != null ? (Date) objects[2] : new Date());
			en.setBankId(objects[3] != null ? (int) objects[3] : 0);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static Boolean insertData(String kdPickup, Date tglBayar, Integer bankId) {
		Session sess = HibernateUtil.openSession();
		String sql = "INSERT INTO TT_INVOICE(KODE_PICKUP, TANGGAL_BAYAR, BANK_ID) VALUES(:pKdPickup, :pTglBayar, :pBankId) ";
		Query query = sess.createSQLQuery(sql).setParameter("pKdPickup", kdPickup).setParameter("pTglBayar", tglBayar)
				.setParameter("pBankId", bankId);
		query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
	
	public static Boolean updateData(String kdPickup, Date tglBayar, Integer bankId) {
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE TT_INVOICE set TANGGAL_BAYAR = :pTglBayar, BANK_ID = :pBankId where KODE_PICKUP = :pKdPickup ";
		Query query = sess.createSQLQuery(sql).setParameter("pKdPickup", kdPickup).setParameter("pTglBayar", tglBayar)
				.setParameter("pBankId", bankId);
		query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
}
