package service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import VO.BankVO;
import util.HibernateUtil;

public class BankService {
	public static List<BankVO> getDataBank() {
		Session s = HibernateUtil.openSession();
		String sql = "select id_bank, kode_bank, nama_bank, nomor_rekening, atas_nama from tr_bank ";
		Query query = s.createSQLQuery(sql);

		List<BankVO> returnList = new ArrayList<BankVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			BankVO en = new BankVO();
			en.setIdBank(objects[0] != null ? (int) objects[0] : 0);
			en.setKodeBank(objects[1] != null ? (String) objects[1] : "");
			en.setNmBank(objects[2] != null ? (String) objects[2] : "");
			en.setNoRek(objects[3] != null ? (String) objects[3] : "");
			en.setAtasNama(objects[4] != null ? (String) objects[4] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
}
