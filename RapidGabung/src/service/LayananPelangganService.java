package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import VO.EntryDataShowVO;
import VO.StatusLaporanVO;
import entity.TtDataEntry;
import util.HibernateUtil;

public class LayananPelangganService {
	public static List<EntryDataShowVO> getDataLayananPelanggan(Date tglAwal, Date tglAkhir) {
		Session s = HibernateUtil.openSession();
		String sql = " select lp.CREATED, de.AWB_DATA_ENTRY, de.KODE_PERWAKILAN, sl.KETERANGAN, ku.NAMA, lp.TGL_SELESAI,  lp.PELANGGARAN, lp.id, lp.no_laporan "
				+ " from tt_layanan_pelanggan lp "
				+ " left join tt_data_entry de on de.AWB_DATA_ENTRY = lp.AWB_LAYANAN_PELANGGAN "
				+ " left join tt_status_kurir_in ki on ki.ID_BARANG = lp.AWB_LAYANAN_PELANGGAN "
				+ " left join tr_kurir ku on ku.NIK = ki.ID_KURIR "
				+ " left join tr_status_laporan sl on sl.ID = lp.ID_STATUS " + " where"
				+ " date(lp.created) between :pTglAwal and :ptglAkhir"
				+ " group by lp.AWB_LAYANAN_PELANGGAN order by lp.AWB_LAYANAN_PELANGGAN ";
		Query query = s.createSQLQuery(sql).setParameter("pTglAwal", tglAwal).setParameter("ptglAkhir", tglAkhir);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setTglCreated((Date) objects[0]);
			en.setAwbData(objects[1] != null ? (String) objects[1] : "");
			en.setKdPerwakilan(objects[2] != null ? (String) objects[2] : "");
			en.setStatusLaporan(objects[3] != null ? (String) objects[3] : "");
			en.setNmKurir(objects[4] != null ? (String) objects[4] : "");
			en.setTglSelesai((Date) objects[5]);
			en.setPelanggaranKurir(objects[6] != null ? (String) objects[6] : "");
			en.setNoLaporan(objects[7] != null ? (int) objects[7] : 0);
			en.setNoLaporan2(objects[8] != null ? (String) objects[8] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static List<EntryDataShowVO> getDataLayananPelangganByNoResi(String noResi) {
		Session s = HibernateUtil.openSession();
		String sql = " select lp.id, lp.CREATED, de.AWB_DATA_ENTRY, de.KODE_PERWAKILAN, sl.KETERANGAN, ku.NAMA, lp.PELANGGARAN, "
				+ " lp.TGL_SELESAI, ki.status, lp.telp_penerima, lp.notes, lp.masalah, lp.updated, lp.no_laporan "
				+ " from tt_layanan_pelanggan lp "
				+ " left join tt_data_entry de on de.AWB_DATA_ENTRY = lp.AWB_LAYANAN_PELANGGAN "
				+ " left join tt_status_kurir_in ki on ki.ID_BARANG = lp.AWB_LAYANAN_PELANGGAN "
				+ " left join tr_kurir ku on ku.NIK = ki.ID_KURIR "
				+ " left join tr_status_laporan sl on sl.ID = lp.ID_STATUS "
				// + " LEFT JOIN tr_pelanggaran pn on pn.ID = lp.ID_PELANGGARAN
				// "
				+ " where lp.awb_layanan_pelanggan = :pNoResi ";
		Query query = s.createSQLQuery(sql).setParameter("pNoResi", noResi);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setId(objects[0] != null ? (int) objects[0] : 0);
			en.setTglCreated((Date) objects[1]);
			en.setAwbData(objects[2] != null ? (String) objects[2] : "");
			en.setKdPerwakilan(objects[3] != null ? (String) objects[3] : "");
			en.setStatusLaporan(objects[4] != null ? (String) objects[4] : "");
			en.setNmKurir(objects[5] != null ? (String) objects[5] : "");
			en.setPelanggaranKurir(objects[6] != null ? (String) objects[6] : "");
			en.setTglSelesai((Date) objects[7]);
			en.setStatus(objects[8] != null ? (String) objects[8] : "");
			en.setNoTlpn(objects[9] != null ? (String) objects[9] : "");
			en.setNotes(objects[10] != null ? (String) objects[10] : "");
			en.setMasalah(objects[11] != null ? (String) objects[11] : "");
			en.setTglUpdated((Date) objects[12]);
			en.setNoLaporan2(objects[13] != null ? (String) objects[13] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static List<EntryDataShowVO> getDataLayananPelangganAll() {
		Session s = HibernateUtil.openSession();
		String sql = " select lp.CREATED, de.AWB_DATA_ENTRY, de.KODE_PERWAKILAN, sl.KETERANGAN, ku.NAMA, lp.TGL_SELESAI,  lp.PELANGGARAN, lp.id "
				+ " from tt_layanan_pelanggan lp "
				+ " left join tt_data_entry de on de.AWB_DATA_ENTRY = lp.AWB_LAYANAN_PELANGGAN "
				+ " left join tt_status_kurir_in ki on ki.ID_BARANG = lp.AWB_LAYANAN_PELANGGAN "
				+ " left join tr_kurir ku on ku.NIK = ki.ID_KURIR "
				+ " left join tr_status_laporan sl on sl.ID = lp.ID_STATUS "
				+ " group by lp.AWB_LAYANAN_PELANGGAN order by lp.AWB_LAYANAN_PELANGGAN ";
		Query query = s.createSQLQuery(sql);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setTglCreated((Date) objects[0]);
			en.setAwbData(objects[1] != null ? (String) objects[1] : "");
			en.setKdPerwakilan(objects[2] != null ? (String) objects[2] : "");
			en.setStatusLaporan(objects[3] != null ? (String) objects[3] : "");
			en.setNmKurir(objects[4] != null ? (String) objects[4] : "");
			en.setTglSelesai((Date) objects[5]);
			en.setPelanggaranKurir(objects[6] != null ? (String) objects[6] : "");
			en.setNoLaporan(objects[7] != null ? (int) objects[7] : 0);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static List<EntryDataShowVO> getDataOtomatis(String noResi) {
		Session s = HibernateUtil.openSession();
		String sql = " select te.awb_data_entry, te.KODE_PERWAKILAN, ku.NAMA, ki.STATUS from tt_data_entry te  "
				+ " LEFT JOIN tt_status_kurir_in ki on ki.ID_BARANG = te.AWB_DATA_ENTRY "
				+ " LEFT JOIN tr_kurir ku on ku.NIK = ki.ID_KURIR where te.awb_data_entry = :pNoResi ";
		Query query = s.createSQLQuery(sql).setParameter("pNoResi", noResi);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0] != null ? (String) objects[0] : "");
			en.setKdPerwakilan(objects[1] != null ? (String) objects[1] : "");
			en.setNmKurir(objects[2] != null ? (String) objects[2] : "");
			en.setStatusLaporan(objects[3] != null ? (String) objects[3] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static List<StatusLaporanVO> getDataStatusLaporan() {
		Session s = HibernateUtil.openSession();
		String sql = " select id, kode, keterangan from tr_status_laporan ";
		Query query = s.createSQLQuery(sql);

		List<StatusLaporanVO> returnList = new ArrayList<StatusLaporanVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			StatusLaporanVO en = new StatusLaporanVO();
			en.setId(objects[0] != null ? (int) objects[0] : 0);
			en.setKode(objects[1] != null ? (int) objects[1] : 0);
			en.setKeterangan(objects[2] != null ? (String) objects[2] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static List<TtDataEntry> getDataAwb() {
		Session s = HibernateUtil.openSession();
		String sql = " select te.AWB_DATA_ENTRY " + " from tt_data_entry  te "
				+ " left join tt_header th on th.AWB_HEADER = te.AWB_DATA_ENTRY " + " where th.flag <> 1 ";
		Query query = s.createSQLQuery(sql);

		List<TtDataEntry> returnList = new ArrayList<TtDataEntry>();
		List<String> list = query.list();
		for (String objects : list) {
			TtDataEntry en = new TtDataEntry();
			en.setAwbDataEntry(objects != null ? (String) objects : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
	
	public static List<TtDataEntry> getDataAwb(String noAwb) {
		Session s = HibernateUtil.openSession();
		String sql = " select te.AWB_DATA_ENTRY " + " from tt_data_entry  te "
				+ " left join tt_header th on th.AWB_HEADER = te.AWB_DATA_ENTRY " + " where th.flag <> 1 and te.AWB_DATA_ENTRY = :pNoAwb ";
		//String sql = " select te.AWB_DATA_ENTRY from tt_data_entry te where te.AWB_DATA_ENTRY = :pNoAwb ";
		Query query = s.createSQLQuery(sql).setParameter("pNoAwb", noAwb);;

		List<TtDataEntry> returnList = new ArrayList<TtDataEntry>();
		List<String> list = query.list();
		for (String objects : list) {
			TtDataEntry en = new TtDataEntry();
			en.setAwbDataEntry(objects != null ? (String) objects : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}

	public static Boolean insertData(String noLaporan, String noResi, String pelanggaran, Integer idStatus,
			Date tglCreated, String telpPenerima, String notes, String masalah) {
		Session sess = HibernateUtil.openSession();
		String sql = " insert into tt_layanan_pelanggan(NO_LAPORAN, AWB_LAYANAN_PELANGGAN, PELANGGARAN, ID_STATUS, CREATED, TELP_PENERIMA, NOTES, MASALAH)"
				+ " values (:pNoLaporan, :pNoResi, :pIdPelanggaran, :pIdStatus, :pCreated, :pTelpPenerima, :pNotes, :pMasalah) ";
		Query query = sess.createSQLQuery(sql).setParameter("pNoLaporan", noLaporan).setParameter("pNoResi", noResi)
				.setParameter("pIdPelanggaran", pelanggaran).setParameter("pIdStatus", idStatus)
				.setParameter("pCreated", tglCreated).setParameter("pTelpPenerima", telpPenerima)
				.setParameter("pNotes", notes).setParameter("pMasalah", masalah);
		query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}

	public static Boolean updateData(String noResi, String pelanggaran, Integer idStatus, Date tglUpdated,
			String telpPenerima, String notes, String masalah) {
		Session sess = HibernateUtil.openSession();
		String sql = " update tt_layanan_pelanggan set PELANGGARAN = :pIdPelanggaran, ID_STATUS = :pIdStatus, UPDATED = :pUpdated, "
				+ " TELP_PENERIMA = :pTelpPenerima, NOTES = :pNotes, MASALAH = :pMasalah "
				+ " where AWB_LAYANAN_PELANGGAN = :pNoResi ";
		Query query = sess.createSQLQuery(sql).setParameter("pNoResi", noResi)
				.setParameter("pIdPelanggaran", pelanggaran).setParameter("pIdStatus", idStatus)
				.setParameter("pUpdated", tglUpdated).setParameter("pTelpPenerima", telpPenerima)
				.setParameter("pNotes", notes).setParameter("pMasalah", masalah);
		query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}

	public static Boolean updateTglSelesai(Date tglSelesai, String noResi) {
		Session sess = HibernateUtil.openSession();
		String sql = " update tt_layanan_pelanggan set TGL_SELESAI = :pTglSelesai "
				+ " where AWB_LAYANAN_PELANGGAN = :pNoResi ";
		Query query = sess.createSQLQuery(sql).setParameter("pTglSelesai", tglSelesai).setParameter("pNoResi", noResi);
		query.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
}
