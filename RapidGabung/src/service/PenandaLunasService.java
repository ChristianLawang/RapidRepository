package service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import VO.EntryDataShowVO;
import util.HibernateUtil;

public class PenandaLunasService {
	
	public static List<EntryDataShowVO> getDataPelunasan(String nmPelanggan, Date tglAwal, Date tglAkhir) {
		Session s = HibernateUtil.openSession();
		String sql = " select a.tgl_create, a.kode_pelanggan, a.kode_pickup, sum(total_biaya), i.tanggal_bayar, "
				+ " bk.NAMA_BANK, a.flag_lunas " + " from tt_poto_timbang a "
				+ " left join tt_data_entry b on b.awb_data_entry = a.awb_poto_timbang "
				+ " left join tt_invoice i on i.kode_pickup = a.kode_pickup "
				+ " left join tr_bank bk on bk.ID_BANK = i.BANK_ID " + " where  " + " a.kode_pelanggan = :pNmPelanggan "
				+ " and a.tgl_create between :pTglAwal and :ptglAkhir "
				+ " GROUP by a.KODE_PICKUP order by a.kode_pelanggan ";
		Query query = s.createSQLQuery(sql).setParameter("pNmPelanggan", nmPelanggan).setParameter("pTglAwal", tglAwal)
				.setParameter("ptglAkhir", tglAkhir);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setTglCreated((Date) objects[0]);
			en.setKdPelanggan(objects[1] != null ? (String) objects[1] : "");
			en.setKdPickup(objects[2] != null ? (String) objects[2] : "");
			en.settBiaya(objects[3] != null ? (BigDecimal) objects[3] : BigDecimal.ZERO);
			en.setTglBayar((Date) objects[4]);
			en.setBank(objects[5] != null ? (String) objects[5] : "");
			en.setFlagLunas(objects[6] != null ? (int) objects[6] : 0);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static List<EntryDataShowVO> getDataPelunasanAll(Date tglAwal, Date tglAkhir) {
		Session s = HibernateUtil.openSession();
		String sql = " select a.tgl_create, a.kode_pelanggan, a.kode_pickup, sum(total_biaya), i.tanggal_bayar, "
				+ " bk.NAMA_BANK, a.flag_lunas " + " from tt_poto_timbang a "
				+ " left join tt_data_entry b on b.awb_data_entry = a.awb_poto_timbang "
				+ " left join tt_invoice i on i.kode_pickup = a.kode_pickup "
				+ " left join tr_bank bk on bk.ID_BANK = i.BANK_ID " + " where  "
				+ " a.tgl_create between :pTglAwal and :ptglAkhir "
				+ " GROUP by a.KODE_PICKUP order by a.kode_pelanggan ";
		Query query = s.createSQLQuery(sql).setParameter("pTglAwal", tglAwal).setParameter("ptglAkhir", tglAkhir);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setTglCreated((Date) objects[0]);
			en.setKdPelanggan(objects[1] != null ? (String) objects[1] : "");
			en.setKdPickup(objects[2] != null ? (String) objects[2] : "");
			en.settBiaya(objects[3] != null ? (BigDecimal) objects[3] : BigDecimal.ZERO);
			en.setTglBayar((Date) objects[4]);
			en.setBank(objects[5] != null ? (String) objects[5] : "");
			en.setFlagLunas(objects[6] != null ? (int) objects[6] : 0);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static List<EntryDataShowVO> getDataPickup(Date tglAwal, Date tglAkhir) {
		Session s = HibernateUtil.openSession();
		String sql = "select a.tgl_create, a.kode_pickup, a.kode_pelanggan, "
				+ "count(a.kode_pickup) jumlahBarang, sum(a.bpClose) totalBerat, " + "sum(b.total_biaya) totalTagihan "
				+ "from tt_poto_timbang a, tt_data_entry b " + "where a.awb_poto_timbang=b.awb_data_entry "
				+ "and a.tgl_create between :pTglAwal and :ptglAkhir and a.flag_Lunas=0 "
				+ "group by a.kode_pickup order by a.kode_pelanggan";
		Query query = s.createSQLQuery(sql).setParameter("pTglAwal", tglAwal).setParameter("ptglAkhir", tglAkhir);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setCreated((Date) objects[0]);
			en.setKdPickup(objects[1] != null ? (String) objects[1] : "");
			en.setKdPelanggan(objects[2] != null ? (String) objects[2] : "");
			en.setJmlhBarang(objects[3] != null ? (BigInteger) objects[3] : BigInteger.ZERO);
			en.setSumBeratAsli((Double) (objects[4] != null ? (Double) objects[4] : 0));
			en.settBiaya((BigDecimal) objects[5] != null ? (BigDecimal) objects[5] : BigDecimal.ZERO);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;

	}

	public static Boolean updateLunas(String kdPickup) {
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE tt_poto_timbang SET flag_lunas='1' WHERE kode_pickup=:pKdPickup";
		Query query = sess.createSQLQuery(sql).setParameter("pKdPickup", kdPickup);
		query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
}
