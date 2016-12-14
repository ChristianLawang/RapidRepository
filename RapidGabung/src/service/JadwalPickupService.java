package service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import controller.LoginController;
import entity.TrKurir;
import entity.TrPickup;
import entity.TtDataEntry;
import entity.TtJadwalPickup;
import entity.TtPickup;
import util.DateUtil;
import util.HibernateUtil;
import util.PropertiesUtil;

public class JadwalPickupService {
	public static TrKurir getKurirByName(String namaKurir){
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TrKurir.class);
		c.add(Restrictions.eq("nama", namaKurir));
		List<TrKurir> data = c.list();
		s.getTransaction().commit();
		
		return data.get(0);
	}
	
	public static List<TrKurir> getKurirLokal(){
		String kodePerwakilan = PropertiesUtil.getPerwakilan();
		
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TrKurir.class);
		c.add(Restrictions.eq("kodePerwakilan", kodePerwakilan));
		c.add(Restrictions.eq("flag", 0));
		c.add(Restrictions.eq("idJabatan", "kurir"));
		List<TrKurir> data = c.list();
		s.getTransaction().commit();
		
		return data;
	}
	
	public static List<TrKurir> getKurirLokalAsc(){
		String kodePerwakilan = PropertiesUtil.getPerwakilan();
		
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TrKurir.class);
		c.add(Restrictions.eq("kodePerwakilan", kodePerwakilan));
		c.add(Restrictions.eq("flag", 0));
//		c.add(Restrictions.disjunction()
//        .add(Restrictions.eq("idJabatan", "Pickup")))
//        .add(Restrictions.eq("idJabatan", "Delivery"));  
		c.add(Restrictions.in("idJabatan", new String[] {"Pickup", "Delivery"}));
		c.addOrder(Order.asc("nama"));
		List<TrKurir> data = c.list();
		s.getTransaction().commit();
		
		return data;
	}

	public static List<Map> getMasterPickupByDay(Date currDate) {
		Session session = HibernateUtil.openSession();
//		String sql =
//				"select * from tr_pickup a where a.kode_hari = '"+DateUtil.getNomorHariDalamSeminggu(currDate).toString()+"' and a.id not in (select id_pickup from tt_jadwal_pickup) and a.flag = '0'";
		String sql = 
			"select " +
			"      x.ID, " +
			"      x.KODE_PELANGGAN, " +
			"      x.KODE_HARI, " +
			"      x.JAM_PICKUP, " +
			"      x.TGL_CREATE, " +
			"      x.TGL_UPDATE, " +
			"      x.FLAG, " +
			"      x.SOURCE " +
			"from ( " +
			"    select  " +
			"           ID, " +
			"           KODE_PELANGGAN, " +
			"           KODE_HARI, " +
			"           JAM_PICKUP, " +
			"           TGL_CREATE, " +
			"           TGL_UPDATE, " +
			"           FLAG, " +
			"           'MASTER' as SOURCE " +
			"    from tr_pickup " +
			"    where " +
			"         kode_hari = '"+DateUtil.getNomorHariDalamSeminggu(currDate).toString()+"' " +
			"         and ID not in  " +
			"         (select id_pickup from tt_jadwal_pickup) " +
			"         and flag = '0'  " +

			"    union all " +
			    
			"    select " +
			"          ID, " +
			"          KODE_PELANGGAN, " +
			"          KODE_HARI, " +
			"          JAM_PICKUP, " +
			"          TGL_CREATE, " +
			"          TGL_UPDATE, " +
			"          FLAG, " +
			"          'MANUAL' " +
			"    from tt_pickup " +
			"    where " +
			"         ID not in  " +
			"         (select id_pickup from tt_jadwal_pickup) " +
			"         and flag = '0'  " +
			"         and date(tgl_create) = '"+DateUtil.getDateRipTimeForSQL(currDate)+"' " +
			") as x	";
		
		
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();

		List<Map> returnList = new ArrayList<Map>();
		for (Object obj : result) {
			Map row = (Map) obj;
			returnList.add(row);
//			TrPickup everyRow = new TrPickup();
//			everyRow.setId((Integer)row.get("ID"));
//			everyRow.setKodePelanggan((String)row.get("KODE_PELANGGAN"));
//			everyRow.setKodeHari((String)row.get("KODE_HARI"));
//			everyRow.setJamPickup((String)row.get("JAM_PICKUP"));
//			everyRow.setTglCreate((Date)row.get("TGL_CREATE"));
//			everyRow.setTglUpdate((Date)row.get("TGL_UPDATE"));
//			everyRow.setFlag((String)row.get("FLAG"));
//			returnList.add(everyRow);
		}
		return returnList;
	}

	public static void insertJadwalPickup(Integer newId, String idPickup, Integer day, String kodePelanggan, String jamPickup, String namaKurir, Integer pos, Date dtCurrent) {
		TtJadwalPickup jp = new TtJadwalPickup();
		jp.setId(newId+1);
		jp.setKodePelanggan(kodePelanggan);
		jp.setKodeHari(day.toString());
		jp.setJamPickup(jamPickup);
		jp.setNikKurir(
				MasterKurirService.getIdKurirByName(
						namaKurir, 
						PropertiesUtil.getPerwakilan()
					));
		jp.setAssignBy(LoginController.getUserLogin().getIdUser());
		jp.setSentMail(0);
		jp.setFlagJalan(0);
		jp.setTglCreate(dtCurrent);
		jp.setTglUpdate(dtCurrent);
		jp.setFlag("0");
		jp.setPosisi(pos);
		jp.setIdPickup(idPickup);
		
		GenericService.save(TtJadwalPickup.class, jp, true);
	}

	public static void deleteJadwalPickup(Integer id) {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtJadwalPickup.class);
		c.add(Restrictions.eq("id", id));
		List<TtJadwalPickup> data = c.list();
		TtJadwalPickup tt = data.get(0);
		
		s.delete(tt);
		s = HibernateUtil.openSession();
		s.delete(tt);
		s.flush();
		s.getTransaction().commit();
	}

	public static void deleteJadwalPickupLangsung(String id) {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtPickup.class);
		c.add(Restrictions.eq("id", id));
		List<TtPickup> data = c.list();
		TtPickup tt = data.get(0);
		
		s.delete(tt);
		s = HibernateUtil.openSession();
		s.delete(tt);
		s.flush();
		s.getTransaction().commit();
	}	
	
	public static List<TtJadwalPickup> getJadwalPickupByKurirByNikKurir(String nik, Date dtCurrent) {
		Calendar calStart=Calendar.getInstance();
		Calendar calEnd=Calendar.getInstance();
		calStart.setTime(dtCurrent);
		calStart.set(Calendar.HOUR_OF_DAY, 0);
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		calStart.set(Calendar.MILLISECOND, 0);			
			
		calEnd.setTime(dtCurrent);
		calEnd.set(Calendar.HOUR_OF_DAY, 23);
		calEnd.set(Calendar.MINUTE, 59);
		calEnd.set(Calendar.SECOND, 59);
		calEnd.set(Calendar.MILLISECOND, 59);
		
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtJadwalPickup.class);
		c.add(Restrictions.eq("nikKurir", nik));
		c.add(Restrictions.between("tglCreate", calStart.getTime(), calEnd.getTime()));	
		List<TtJadwalPickup> data = c.list();	
		s.getTransaction().commit();
		
		return data;
	}

	public static List<TtJadwalPickup> getEachKurirJadwalPickup(Date dtCurrent) {
		Session session = HibernateUtil.openSession();
		String sql =
				"select distinct a.nik_kurir, a.posisi from tt_jadwal_pickup a where date(tgl_create) = '"+DateUtil.getDateRipTimeForSQL(dtCurrent)+"' order by 2";
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();

		List<TtJadwalPickup> returnList = new ArrayList<TtJadwalPickup>();
		for (Object obj : result) {
			Map row = (Map) obj;
			TtJadwalPickup everyRow = new TtJadwalPickup();
			everyRow.setNikKurir((String)row.get("NIK_KURIR"));
			everyRow.setPosisi((Integer)row.get("POSISI"));
			
			returnList.add(everyRow);
		}	
		
		return returnList;
	}

	public static Integer getMaxJadwalPickup(Date dtCurrent) {
		Calendar calStart=Calendar.getInstance();
		Calendar calEnd=Calendar.getInstance();
		calStart.setTime(dtCurrent);
		calStart.set(Calendar.HOUR_OF_DAY, 0);
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		calStart.set(Calendar.MILLISECOND, 0);			
			
		calEnd.setTime(dtCurrent);
		calEnd.set(Calendar.HOUR_OF_DAY, 23);
		calEnd.set(Calendar.MINUTE, 59);
		calEnd.set(Calendar.SECOND, 59);
		calEnd.set(Calendar.MILLISECOND, 59);
		
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtJadwalPickup.class);
		c.add(Restrictions.between("tglCreate", calStart.getTime(), calEnd.getTime()));	
		c.setProjection(Projections.max("posisi"));	
		Integer result = (Integer)c.uniqueResult();
		s.getTransaction().commit();
		if(result!=null){
			return result;
		}else{
			return 0;
		}
	}

	public static TrKurir getKurirPerPickup(Integer posisi, Date dtCurrent) {
		Calendar calStart=Calendar.getInstance();
		Calendar calEnd=Calendar.getInstance();
		calStart.setTime(dtCurrent);
		calStart.set(Calendar.HOUR_OF_DAY, 0);
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		calStart.set(Calendar.MILLISECOND, 0);			
			
		calEnd.setTime(dtCurrent);
		calEnd.set(Calendar.HOUR_OF_DAY, 23);
		calEnd.set(Calendar.MINUTE, 59);
		calEnd.set(Calendar.SECOND, 59);
		calEnd.set(Calendar.MILLISECOND, 59);
		
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtJadwalPickup.class);
		c.add(Restrictions.eq("posisi", posisi));
		c.add(Restrictions.between("tglCreate", calStart.getTime(), calEnd.getTime()));	
		List<TtJadwalPickup> data = c.list();	
		s.getTransaction().commit();
		TtJadwalPickup jp = null;		
		if(data.size()>0){
			jp = data.get(0);
		}		
		if(jp!=null){
			Session s1 = HibernateUtil.openSession();
			Criteria c1 = s1.createCriteria(TrKurir.class);
			c1.add(Restrictions.eq("nik", jp.getNikKurir()));
			TrKurir data1 = (TrKurir) c1.uniqueResult();
			s1.getTransaction().commit();
			return data1;
		}
		return null;
	}

	public static void updateJadwalPickup(Integer flagEmail, Integer flagJalan, Integer idPickup) {
		Session s = HibernateUtil.openSession();
		TtJadwalPickup m = (TtJadwalPickup) s.get(TtJadwalPickup.class, idPickup);
		m.setFlagJalan(flagJalan);
		m.setSentMail(flagEmail);
		s.update(m);
	}

	public static void insertJadwalPickupLangsung(
			String ids, 
			String kodePelanggan, 
			Integer nomorHariDalamSeminggu,
			String jamPicup, 
			Date tglCreate, 
			Date tglUpdate, 
			String flag) {
		TtPickup trPickup = new TtPickup();
		trPickup.setId(ids);
		trPickup.setKodePelanggan(kodePelanggan);
		trPickup.setKodeHari(nomorHariDalamSeminggu.toString());
		trPickup.setJamPickup(jamPicup);
		trPickup.setTglCreate(tglCreate);
		trPickup.setTglUpdate(tglUpdate);
		trPickup.setFlag(flag);
		GenericService.save(TtPickup.class, trPickup, true);		
	}

	public static List<Map> getMasterPickupByDayAfter(Date currDate) {
		Session session = HibernateUtil.openSession();
		String sql = 
			"select " +
			"      a.KODE_PELANGGAN, " +
			"      a.JAM_PICKUP, " +
			"      b.NAMA " +
			"from " +
			"      tt_jadwal_pickup a " +
			"inner join " +
			"      tr_kurir b on a.NIK_KURIR = b.NIK " +
			"where " +
			"     date(a.TGL_CREATE) = '"+DateUtil.getDateRipTimeForSQL(currDate)+"'";
		
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();

		List<Map> returnList = new ArrayList<Map>();
		for (Object obj : result) {
			Map row = (Map) obj;
			returnList.add(row);
		}
		return returnList;
	}

	public static String getTTPickupMaxID() {
		Session session = HibernateUtil.openSession();
		String sql = 
				"SELECT ID " +
				"FROM  tt_pickup " +
				"ORDER BY LENGTH(ID) DESC, ID DESC " +
				"LIMIT 1";
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		Map result = (Map) query.uniqueResult();
		System.out.println("--> result : " + result);
		return (String) result.get("Id");
	}
}
