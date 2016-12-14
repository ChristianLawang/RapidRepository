package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.TrHarga;
import entity.TtJadwalPickup;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import util.DateUtil;
import util.HibernateUtil;

public class MasterHargaService {

	public static List<TrHarga> getDataHarga_() {
		GenericService service = new GenericService();
		List<TrHarga> lsharga = GenericService.getList(TrHarga.class);
		return lsharga;
	}
	
	public static List<TrHarga> getCariHarga(String kabupaten) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrHarga.class);
		if (kabupaten == "" || kabupaten.equals("") || kabupaten == null) {
		} else {
			c.add(Restrictions.eq("kabupaten", kabupaten));
		}
		@SuppressWarnings("unchecked")
		List<TrHarga> data = c.list();
		s.getTransaction().commit();
		return data;
	}
	
	public static List<TrHarga> getDataHarga(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				"select a.kode_zona, a.kode_asal, a.propinsi, a.kabupaten, a.kecamatan"
				+ ", a.kode_perwakilan, a.zona, a.reg, a.etd, a.one from tr_harga a "
				+ " where a.flag=0";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TrHarga> returnList = new ArrayList<TrHarga>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrHarga everyRow = new TrHarga();
			
			//get data NIK Kurir
//			everyRow.setNik((String) row.get("NIK")!=null?(String) row.get("NIK"):"");
//			System.out.println(row.get("NIK"));
////			everyRow.setAwbData((String) row.get("kode_pelanggan")!=null?(String) row.get("kode_pelanggan"):"");
//			
//			//get data kode_zona
			everyRow.setKodeZona((String) row.get("KODE_ZONA")!=null?(String) row.get("KODE_ZONA"):"");
//			
//			//get data kode_asal
			everyRow.setKodeAsal((String) row.get("KODE_ASAL")!=null?(String) row.get("KODE_ASAL"):"");
//			
//			//get data propinsi
			everyRow.setPropinsi((String) row.get("PROPINSI")!=null?(String) row.get("PROPINSI"):"");
//			
//			//get data Kabupaten
			everyRow.setKabupaten((String) row.get("KABUPATEN")!=null?(String) row.get("KABUPATEN"):"");
//			
//			//get data Kecamatan
			everyRow.setKecamatan((String) row.get("KECAMATAN")!=null?(String) row.get("KECAMATAN"):"");
//			
//			//get data Kode Perwakilan
			everyRow.setKodePerwakilan((String) row.get("KODE_PERWAKILAN")!=null?(String) row.get("KODE_PERWAKILAN"):"");
//			
//			//get data zona
			everyRow.setZona((String) row.get("ZONA")!=null?(String) row.get("ZONA"):"");
			
		
			everyRow.setReg((Integer) row.get("REG"));
//			System.out.println(row.get("REG"));

			//get data etd
			everyRow.setEtd((String) row.get("ETD")!=null?(String) row.get("ETD"):"");
			
			//get data one
//			everyRow.setOne((Integer) row.get("ONE")!=null?(Integer) row.get("ONE"):0);
			everyRow.setOne((Integer) row.get("ONE"));
			
			//get angka sampah
			everyRow.setFlag((Integer) row.get("FLAG"));
			
			returnList.add(everyRow);
		}
		return returnList;
		
	}
	
	public static Boolean showTableSetelahDelete(String Nik) {
		Session sess = HibernateUtil.openSession();
		sess.beginTransaction();
		
		String sql = "UPDATE tr_harga a "
				+ "SET flag = :pSampah  "
				+ "WHERE a.kode_zona = :pKodeCab";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pSampah", 1)
				.setParameter("pKodeCab", Nik);
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
	
	public static Boolean updateDataHarga(String kdZona, String kdAsal, String kdProp, String kdKab, String kdKecamatan, String kdPerwakilan,String Zona,Integer reg,String etd,Integer one ) {
		Session sess = HibernateUtil.openSession();
		sess.beginTransaction();
		
		String sql = "update tr_harga a "
				+ "set "
//				+ "a.kode_zona = :pKdZona "
//				+ ", a.kode_asal = :pKdAsal "
//				+ ", "
				+ "a.propinsi = :pKdProp "
				+ ", a.kabupaten = :pKdKab "
				+ ", a.kecamatan = :pKdKecamatan "
				+ ", a.kode_perwakilan = :pKdPerwakilan "
				+ ", a.zona = :pZona "
				+ ", a.reg = :pReg "
				+ ", a.etd = :pEtd "
				+ ", a.one = :pOne "
				+ ", a.tgl_update = :pUpdated "
				+ "where a.kode_zona = :pKdZona and a.kode_asal = :pKdAsal";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pKdZona", kdZona)
				.setParameter("pKdAsal", kdAsal)
				.setParameter("pKdProp", kdProp)
				.setParameter("pKdKab", kdKab)
				.setParameter("pKdKecamatan", kdKecamatan)
				.setParameter("pZona", Zona)
				.setParameter("pKdPerwakilan", kdPerwakilan)
				.setParameter("pReg", reg)
				.setParameter("pEtd", etd)
				.setParameter("pOne", one)
				.setParameter("pUpdated", DateUtil.getNow());
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
		
		sess = HibernateUtil.openSession();
		sess.beginTransaction();
		
		sql = "update tr_perwakilan a "
				+ "set "
//				+ "a.kode_zona = :pKdZona "
//				+ ", a.kode_asal = :pKdAsal "
//				+ ", "
				+ "a.propinsi = :pKdProp "
				+ ", a.kabupaten = :pKdKab "
				+ ", a.kecamatan = :pKdKecamatan "
				+ ", a.kode_perwakilan = :pKdPerwakilan "
				+ ", a.zona = :pZona "
				+ ", a.regperwakilan = :pReg "				
				+ ", a.oneperwakilan = :pOne "
				+ ", a.tgl_update = :pUpdated "
				+ "where a.kode_zona = :pKdZona";
		Query queryPerwakilanUpdate = sess.createSQLQuery(sql)
				.setParameter("pKdZona", kdZona)
				.setParameter("pKdProp", kdProp)
				.setParameter("pKdKab", kdKab)
				.setParameter("pKdKecamatan", kdKecamatan)
				.setParameter("pZona", Zona)
				.setParameter("pKdPerwakilan", kdPerwakilan)
				.setParameter("pReg", reg)
				.setParameter("pOne", one)
				.setParameter("pUpdated", DateUtil.getNow());
		int resultPerwakilan = queryPerwakilanUpdate.executeUpdate();
		queryPerwakilanUpdate.executeUpdate();
		sess.getTransaction().commit();
//		sess.close();
		return true;
	}
	
	public static List<TrHarga> getDataHargaByID(String kodeZona, String kodeAsal) {
		Session s=HibernateUtil.openSession();
		
		Criteria c=s.createCriteria(TrHarga.class);
		c.add(Restrictions.eq("kodeZona", kodeZona));
		c.add(Restrictions.eq("kodeAsal", kodeAsal));
		
		List<TrHarga> list = c.list();
		
		s.getTransaction().commit();
		
		return list;
	}

	
	public static BigInteger getBigInteger(Object value) {
	    BigInteger ret = null;
	    if ( value != null ) {
	        if ( value instanceof BigInteger ) {
	            ret = (BigInteger) value;
	        } else if ( value instanceof String ) {
	            ret = new BigInteger( (String) value );
	        } else if ( value instanceof BigDecimal ) {
	            ret = ((BigDecimal) value).toBigInteger();
	        } else if ( value instanceof Number ) {
	            ret = BigInteger.valueOf( ((Number) value).longValue() );
	        } else {
	            throw new ClassCastException( "Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigInteger." );
	        }
	    }
	    return ret;
	}

	public static List<TrHarga> getListHarga() {
		return GenericService.getList(TrHarga.class);
	}

	public static void updateDataHarga2(String idZona, String idAsal, String reg, String one, String perwakilan) {
		Session sess = HibernateUtil.openSession();
		sess.beginTransaction();
		
		String sql = "update tr_harga a "
				+ "set "
				+ " a.kode_perwakilan = :pKdPerwakilan "
				+ ", a.tgl_update = :pUpdated "
				+ "where a.kode_zona = :pKdZona and a.kode_asal = :pKdAsal";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pKdZona", idZona)
				.setParameter("pKdAsal", idAsal)
				.setParameter("pKdPerwakilan", perwakilan)
				.setParameter("pUpdated", DateUtil.getNow());
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
	}
}
