package service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;

import VO.EntryDataShowVO;
import entity.TrPerwakilan;
import entity.TtDataEntry;
import javafx.scene.control.CheckBox;
import util.DateUtil;
import util.HibernateUtil;


public class LaporanPenerimaService {
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

	public static List<EntryDataShowVO> getListTableLapPenerima(String pengirim, Date tglAwl, Date tglAkhir,
			String kdPerwakilan, CheckBox chkAll, CheckBox chkDetail) {
		Session s = HibernateUtil.openSession();
		String sql =
				"select * from (" +
				"select " +
			    "   a.tgl_create as tanggal_create, " +
			    "   a.awb_data_entry,  " +
			    "   a.pengirim, " +
			    "   a.penerima, " +
			    "   a.telp_penerima, " +
			    "   a.tujuan,    " +
			    "   c.zona, " +
			    "   a.bclose, " +
			    "   c.etd, " +
			    "   a.keterangan, " +
			    "   b.tgl_create as tanggal_terima, " +
			    "   concat(b.waktu_jam,':',waktu_menit), " +
			    "   CASE " +
			    "	WHEN e.resi_jne is not null then 'JNE PROCESS' WHEN b.masalah is null and b.penerima is null then 'ONPROCESS' else b.masalah " +
			    "	END as masalah, " +
			    "   CASE " +
			    "	WHEN b.masalah != '-1 - Diterima' then '' else b.penerima end as diterima_oleh " +
			"from tt_data_entry a " +
			"left join tt_status_kurir_in b on a.awb_data_entry = b.id_barang  " +
			"inner join tr_harga c on c.kode_asal = ( " +
                "select CASE  " +
                "  WHEN left(a.awb_data_entry,3) = 'TGR'   " +
                "  or left(a.awb_data_entry,3) = 'BKI'   " +
                "  or left(a.awb_data_entry,3) = 'DPK'   " +
                "  or left(a.awb_data_entry,3) = 'BOO'    " +               
                "  then 'CGK'   " +
                "  else left(a.awb_data_entry,3)    " +
                "  END as asalpaket from dual  " +
                "  )  " +
			"inner join tt_header e on a.awb_data_entry = e.awb_header " +
			"where  " +
			"      c.kode_zona = a.tujuan and " +
			"      date(a.tgl_create) between :pTglAwl and :pTglAkhr and e.flag=0 ";
			if (pengirim == null || pengirim.equals("")) {
			} else {
					sql += " and a.pengirim =  '" + pengirim + "' ";
			}
		    if (!kdPerwakilan.equals("All Cabang")) {
						sql += " and a.kode_perwakilan =  '" + kdPerwakilan + "' ";			}
		    sql += ") x ORDER BY 2";
		
		System.out.println("--> sql : " + sql);
		Query query = s.createSQLQuery(sql).setParameter("pTglAwl", tglAwl).setParameter("pTglAkhr", tglAkhir);
		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		s.getTransaction().commit();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setTglEntry((Date) objects[0]);
			en.setAwbData(objects[1] != null ? (String) objects[1] : "");
			en.setPengirim(objects[2] != null ? (String) objects[2] : "");
			en.setPenerima(objects[3] != null ? (String) objects[3] : "");
			en.setNoTlpn(objects[4] != null ? (String) objects[4] : "");
			en.setTujuan(objects[5] != null ? (String) objects[5] : "");
			en.setZona(objects[6] != null ? (String) objects[6] : "");
			en.setbFinal(objects[7] != null ? (String) objects[7] : "");
			en.setEtd(objects[8] != null ? (String) objects[8] : "");
			en.setKeterangan(objects[9] != null ? (String) objects[9] : "");
			en.setTglTerimaPaketDate(objects[10] != null ? (Date) objects[10] : null);
			en.setWaktuTerimaPaket(objects[11] != null ? (String) objects[11] : null);
			en.setStatus(objects[12] != null ? (String) objects[12] : "");
			en.setPenerimaPaket(objects[13] != null ? (String) objects[13] : "");
			
			//System.out.println("Penerima Paket ---------->>> "+objects[7] != null ? (String) objects[7] : "");
			returnList.add(en);
		}
		// ditambah untuk dapetin MAX(date)
		if (!chkDetail.isSelected()) {	
			List<EntryDataShowVO> templistF = new ArrayList<EntryDataShowVO>();
			for (Integer i=0; i<returnList.size();i++) {
				Boolean loop = true;
				Integer loopNumber = 0;
				List<EntryDataShowVO> templist = new ArrayList<EntryDataShowVO>();
				templist.add(returnList.get(i));
				while(loop){
					if(i!=(returnList.size()-1)){
						if((i+loopNumber+1)==returnList.size()){
							loop=false;
						}else{
							if(returnList.get(i+loopNumber).getAwbData().equalsIgnoreCase(returnList.get(i+loopNumber+1).getAwbData())){
								templist.add(returnList.get(i+loopNumber+1));	
								loopNumber++;
							}else{
								loop=false;
							}
						}
					}else{
						loop=false;
					}
				}					
				EntryDataShowVO lastKurirIN = Collections.max(templist, Comparator.comparing(c -> c.getTglTerimaPaketDate()));
				templistF.add(lastKurirIN);
				i = i+loopNumber;
				
			}
			return templistF;
		}else{
			return returnList;
		}
	}

	public static void updateTglTerima(
			String awb, 
			String oldValue, 
			String jam, 
			String menit, 
			Date newDate) {
		
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE tt_status_kurir_in SET tgl_create=:newDate WHERE id_barang=:awb and waktu_jam=:jam and waktu_menit=:menit and date(tgl_create) = :oldValue";
			Query query = sess.createSQLQuery(sql)
					.setParameter("newDate", newDate)
					.setParameter("awb", awb)
					.setParameter("jam", jam)
					.setParameter("menit", menit)
					.setParameter("oldValue", oldValue);
		int result = query.executeUpdate();

		sess.getTransaction().commit();
	}

	public static void updateStatusPaket(
			String awb, 
			String oldValue, 
			String jam, 
			String menit, 
			String newStatus) {
		
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE tt_status_kurir_in SET masalah=:newStatus WHERE id_barang=:awb and waktu_jam=:jam and waktu_menit=:menit and date(tgl_create) = :oldValue";
			Query query = sess.createSQLQuery(sql)
					.setParameter("newStatus", newStatus)
					.setParameter("awb", awb)
					.setParameter("jam", jam)
					.setParameter("menit", menit)
					.setParameter("oldValue", oldValue);
		int result = query.executeUpdate();

		sess.getTransaction().commit();
	}
	
	public static void updateNamaPenerimaBelakang(
			String awb, 
			String oldValue, 
			String jam, 
			String menit, 
			String penerima) {
		
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE tt_status_kurir_in SET penerima=:penerima WHERE id_barang=:awb and waktu_jam=:jam and waktu_menit=:menit and date(tgl_create) = :oldValue";
			Query query = sess.createSQLQuery(sql)
					.setParameter("penerima", penerima)
					.setParameter("awb", awb)
					.setParameter("jam", jam)
					.setParameter("menit", menit)
					.setParameter("oldValue", oldValue);
		int result = query.executeUpdate();

		sess.getTransaction().commit();
	}
}
