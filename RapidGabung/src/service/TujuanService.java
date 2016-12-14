package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import VO.EntryDataShowVO;
import VO.ServicePerwakilanVO;
import entity.TrCabang;
import entity.TrHarga;
import util.HibernateUtil;
import util.PropertiesUtil;

public class TujuanService {
	
	//OK
	public static List<ServicePerwakilanVO> getTujuanPaket(String kdTujuan) {

		Session s=HibernateUtil.openSession();
		String sql=null;
		Query query;
		sql ="select a.kode_zona, a.kabupaten, a.kecamatan, a.propinsi, a.reg, a.one, "
				+ "b.regPerwakilan, b.onePerwakilan "
				+ "from tr_harga a, tr_perwakilan b "
				+ "where a.kode_zona = b.kode_zona and a.flag=0 and a.kode_zona like '%"+kdTujuan+"%'";
		System.out.println("--> sql : " + sql);
		query = s.createSQLQuery(sql);
		List<Object[]> list = query.list();
			
		if(list.isEmpty()){
			sql ="select a.kode_zona, a.kabupaten, a.kecamatan, a.propinsi, a.reg, a.one, "
					+ "b.regPerwakilan, b.onePerwakilan "
					+ "from tr_harga a, tr_perwakilan b "
					+ "where a.kode_zona = b.kode_zona and a.flag=0 and a.kecamatan like '%"+kdTujuan+"%'";
			System.out.println("--> sql : " + sql);
			query = s.createSQLQuery(sql);
			list = query.list();
		}
			
		List<ServicePerwakilanVO> data = new ArrayList<ServicePerwakilanVO>();
		for (Object[] objects : list) {
			ServicePerwakilanVO en = new ServicePerwakilanVO();
			en.setKode_zona(objects[0] != null ? (String) objects[0] : "");
			en.setKabupaten(objects[1] != null ? (String) objects[1] : "");
			en.setKecamatan(objects[2] != null ? (String) objects[2] : "");
			en.setPropinsi(objects[3] != null ? (String) objects[3] : "");
			en.setRegHarga(objects[4] != null ? (Integer) objects[4] : 0);
			en.setOneHarga(objects[5] != null ? (Integer) objects[5] : 0);
			en.setRegPerwakilan(objects[6] != null ? (String) objects[6] : "");
			en.setOnePerwakilan(objects[7] != null ? (String) objects[7] : "");
			data.add(en);
		}
		s.getTransaction().commit();
		return data;
	}
	
	//CHRIS
	public static List<ServicePerwakilanVO> getTujuanPaket(String kdTujuan, String awb) {
		// chris nambah
		String kodeAsal = awb.substring(0, 3);
		
		if( kodeAsal.toUpperCase().equals("CGK")	|| // hardcode
			kodeAsal.toUpperCase().equals("DPK")	||
			kodeAsal.toUpperCase().equals("BKI")	||
			kodeAsal.toUpperCase().equals("TGR")	||
			kodeAsal.toUpperCase().equals("BOO")){
			
			kodeAsal = "CGK";
		}
		
		System.out.println("--> kodeAsal : " + kodeAsal);
		
		Session s=HibernateUtil.openSession();
		String sql=null;
		Query query;
		sql ="select a.kode_zona, a.kabupaten, a.kecamatan, a.propinsi, a.reg, a.one, "
				+ "b.regPerwakilan, b.onePerwakilan "
				+ "from tr_harga a, tr_perwakilan b "
				+ "where a.kode_asal = '"+kodeAsal+"' and a.kode_zona = b.kode_zona and a.flag=0 and a.kode_zona like '%"+kdTujuan+"%'";
		System.out.println("--> sql : " + sql);
		query = s.createSQLQuery(sql);
		List<Object[]> list = query.list();
		
		if(list.isEmpty()){
			sql ="select a.kode_zona, a.kabupaten, a.kecamatan, a.propinsi, a.reg, a.one, "
					+ "b.regPerwakilan, b.onePerwakilan "
					+ "from tr_harga a, tr_perwakilan b "
					+ "where a.kode_asal = '"+kodeAsal+"' and a.kode_zona = b.kode_zona and a.flag=0 and a.kecamatan like '%"+kdTujuan+"%'";
			System.out.println("--> sql : " + sql);
			query = s.createSQLQuery(sql);
			list = query.list();
		}
		
		List<ServicePerwakilanVO> data = new ArrayList<ServicePerwakilanVO>();
		for (Object[] objects : list) {
			ServicePerwakilanVO en = new ServicePerwakilanVO();
			en.setKode_zona(objects[0] != null ? (String) objects[0] : "");
			en.setKabupaten(objects[1] != null ? (String) objects[1] : "");
			en.setKecamatan(objects[2] != null ? (String) objects[2] : "");
			en.setPropinsi(objects[3] != null ? (String) objects[3] : "");
			en.setRegHarga(objects[4] != null ? (Integer) objects[4] : 0);
			en.setOneHarga(objects[5] != null ? (Integer) objects[5] : 0);
			en.setRegPerwakilan(objects[6] != null ? (String) objects[6] : "");
			en.setOnePerwakilan(objects[7] != null ? (String) objects[7] : "");
			data.add(en);
		}
		s.getTransaction().commit();
		return data;
	}
	
	//OK
	public static ServicePerwakilanVO getTujuanPaketByKdZona(String kdZona) {
		Session s=HibernateUtil.openSession();
		String sql ="select a.kode_zona, a.kabupaten, a.kecamatan, a.propinsi, a.reg, a.one, "
				+ "b.regPerwakilan, b.onePerwakilan "
				+ "from tr_harga a, tr_perwakilan b "
				+ "where a.kode_zona = b.kode_zona and a.kode_zona=:pKdZona and a.flag=0 ";
		
		Query query = s.createSQLQuery(sql).setParameter("pKdZona", kdZona);
		List<ServicePerwakilanVO> data = new ArrayList<ServicePerwakilanVO>();
		List<Object[]> list = query.list();
		ServicePerwakilanVO en = new ServicePerwakilanVO();
		for (Object[] objects : list) {
			en.setKode_zona(objects[0] != null ? (String) objects[0] : "");
			en.setKabupaten(objects[1] != null ? (String) objects[1] : "");
			en.setKecamatan(objects[2] != null ? (String) objects[2] : "");
			en.setPropinsi(objects[3] != null ? (String) objects[3] : "");
			en.setRegHarga(objects[4] != null ? (Integer) objects[4] : 0);
			en.setOneHarga(objects[5] != null ? (Integer) objects[5] : 0);
			en.setRegPerwakilan(objects[6] != null ? (String) objects[6] : "");
			en.setOnePerwakilan(objects[7] != null ? (String) objects[7] : "");
			data.add(en);
		}
		s.getTransaction().commit();
		return en;
	}
}
