package service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;

import VO.EntryDataShowVO;
import VO.LapPerKecamatanVO;
import entity.TrPerwakilan;
import entity.TtDataEntry;
import javafx.scene.control.CheckBox;
import util.HibernateUtil;

public class LapKecamatanService {
	public static List<TtDataEntry> getDataPengirim() {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select distinct pengirim from tt_data_entry";
		SQLQuery query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();

		List<TtDataEntry> returnList = new ArrayList<TtDataEntry>();
		for (Object obj : result) {
			Map row = (Map) obj;
			TtDataEntry everyRow = new TtDataEntry();
			everyRow.setPengirim((String) row.get("PENGIRIM") != null ? (String) row.get("PENGIRIM") : "");
			returnList.add(everyRow);
		}
		return returnList;
	}

	public static List<LapPerKecamatanVO> getListTableLapPenerima(String pengirim, Date tglAwl, Date tglAkhir,
			String kdPerwakilan, CheckBox chkAll) {
		Session s = HibernateUtil.openSession();
		String sql = "";
		if (!chkAll.isSelected()) {
			sql = "select a.awb_data_entry,a.pengirim from tt_data_entry a where date(a.tgl_create) between :pTglAwl and :pTglAkhr ";
		} else {
			sql = "select a.awb_data_entry,a.pengirim from tt_data_entry a where date(a.tgl_create) between :pTglAwl and :pTglAkhr ";
		}
		if (pengirim == null || pengirim.equals("")) {
		} else {
			sql += "and a.pengirim =  '" + pengirim + "' ";
		}
		if (kdPerwakilan == null || kdPerwakilan.equals("")) {
		} else {
			sql += "and a.kode_perwakilan =  '" + kdPerwakilan + "' ";
		}
		sql += "order by a.awb_data_entry";
		Query query = s.createSQLQuery(sql).setParameter("pTglAwl", tglAwl).setParameter("pTglAkhr", tglAkhir);
		List<LapPerKecamatanVO> returnList = new ArrayList<LapPerKecamatanVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			LapPerKecamatanVO en = new LapPerKecamatanVO();
			en.setAwbData(objects[0] != null ? (String) objects[0] : "");
//			en.setTglEntry((Date) objects[1]);
//			en.setPengirim(objects[2] != null ? (String) objects[2] : "");
//			en.setPenerima(objects[3] != null ? (String) objects[3] : "");
//			en.setKecamatan(objects[4] != null ? (String) objects[4] : "");
//			en.setKdPerwakilan(objects[5] != null ? (String) objects[5] : "");
//			en.setTglTerimaPaket((Timestamp) objects[6]);
//			en.setPenerimaPaket(objects[7] != null ? (String) objects[7] : "");
//			System.out.println("Penerima Paket ---------->>> "+objects[7] != null ? (String) objects[7] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
}
