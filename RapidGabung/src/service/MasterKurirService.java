package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import entity.TrKurir;
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
import util.PropertiesUtil;

public class MasterKurirService {

	public static List<TrKurir> getDataKurir_() {
		GenericService service = new GenericService();
		List<TrKurir> lsKurir = GenericService.getList(TrKurir.class);
		return lsKurir;
	} 
	
	public static List<TrKurir> getDataKurir(){
		Session session=HibernateUtil.openSession();
		String kodePerwakilan = PropertiesUtil.getPerwakilan();
		String nativeSql = 
				"select a.nik, a.nama, a.telp, a.id_jabatan, a.no_kendaraan, a.kode_perwakilan"
				+ ", a.kode_cabang, a.tgl_masuk, a.keterangan, a.tgl_create, a.tgl_update from tr_kurir a "
				+ "where a.flag=0 and a.kode_perwakilan = '"+kodePerwakilan+"'";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<TrKurir> returnList = new ArrayList<TrKurir>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrKurir everyRow = new TrKurir();
			
			//get data NIK Kurir
			everyRow.setNik((String) row.get("NIK")!=null?(String) row.get("NIK"):"");
			System.out.println(row.get("NIK"));
//			everyRow.setAwbData((String) row.get("kode_pelanggan")!=null?(String) row.get("kode_pelanggan"):"");
			
			//get data nama kurir
			everyRow.setNama((String) row.get("NAMA")!=null?(String) row.get("NAMA"):"");
			
			//get data telp kurir
			everyRow.setTelp((String) row.get("TELP")!=null?(String) row.get("TELP"):"");
			
			//get data Id Jabatan
			everyRow.setIdJabatan((String) row.get("ID_JABATAN")!=null?(String) row.get("ID_JABATAN"):"");
			
			//get data No Kendaraan
			everyRow.setNoKendaraan((String) row.get("NO_KENDARAAN")!=null?(String) row.get("NO_KENDARAAN"):"");
			
			//get data Wilayah
//			everyRow.setWilayah((String) row.get("WILAYAH")!=null?(String) row.get("WILAYAH"):"");
			
			//get data Kode Perwakilan
			everyRow.setKodePerwakilan((String) row.get("KODE_PERWAKILAN")!=null?(String) row.get("KODE_PERWAKILAN"):"");
			
			//get data Kode Cabang
			everyRow.setKodeCabang((String) row.get("KODE_CABANG")!=null?(String) row.get("KODE_CABANG"):"");
			
			
			//get data tgl masuk
//			everyRow.setTglMasuk((Date) row.get("tgl_masuk")!=null?(Date) row.get("tgl_masuk"):"");
			everyRow.setTglMasuk((Date) row.get("TGL_MASUK"));
			
			
			//get data Keterangan
			everyRow.setKeterangan((String) row.get("KETERANGAN")!=null?(String) row.get("KETERANGAN"):"");
			
		
			//get tanggal created
			everyRow.setTglCreate((Date) row.get("TGL_CREATE"));
			
			//get tanggal updated
			everyRow.setTglUpdate((Date) row.get("TGL_UPDATE"));
//			
//			//get tanggal bergabung
//			everyRow.setUpdated((Date) row.get("TGL_GABUNG"));
			
			returnList.add(everyRow);
		}
		return returnList;
		
	}
	
	public static Boolean showTableSetelahDelete(String Nik) {
		Session sess = HibernateUtil.openSession();
		sess.beginTransaction();
		
		String sql = "UPDATE tr_kurir a "
				+ "SET flag = :pFlag  "
				+ "WHERE a.nik = :pKodeCab";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pFlag", 1)
				.setParameter("pKodeCab", Nik);
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
	
	public static Boolean updateDataKurir(String nik, String nm, String telp, Object jab
			,String noKendaraan, String kdPerwakilan,Object wilayah,Date tglMasuk,String ket ) {
		Session sess = HibernateUtil.openSession();
		sess.beginTransaction();
		
		String sql = "update tr_kurir a "
				+ "set a.nik = :pNik "
				+ ", a.nama = :pNm "
				+ ", a.telp = :pTelp "
				+ ", a.id_jabatan = :pJab "
				+ ", a.no_kendaraan = :pNoKendaraan "
				+ ", a.kode_perwakilan = :pKdPerwakilan "
				+ ", a.kode_cabang = :pWilayah "
				+ ", a.tgl_masuk = :pTglMasuk "
				+ ", a.keterangan = :pKet "
				+ ", a.tgl_update = :pUpdated "
				+ "where a.nik = :pNik";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pNik", nik)
				.setParameter("pNm", nm)
				.setParameter("pTelp", telp)
				.setParameter("pJab", jab)
				.setParameter("pNoKendaraan", noKendaraan)
				.setParameter("pWilayah", wilayah)
				.setParameter("pKdPerwakilan", kdPerwakilan)
				.setParameter("pTglMasuk", tglMasuk)
				.setParameter("pKet", ket)
				.setParameter("pUpdated", DateUtil.getNow());
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
	
	public static List<TrKurir> getDataKurirByID(String id) {
		Session s=HibernateUtil.openSession();
		
		Criteria c=s.createCriteria(TrKurir.class);
		c.add(Restrictions.eq("nik", id));
		
		List<TrKurir> list = c.list();
		
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

	public static String getIdKurirByName(String namaKurir, String perwakilan) {
		Session s=HibernateUtil.openSession();
		
		Criteria c=s.createCriteria(TrKurir.class);
		c.add(Restrictions.eq("nama", namaKurir));
		c.add(Restrictions.eq("kodePerwakilan", perwakilan));
		
		List<TrKurir> list = c.list();
		
		System.out.println("--> list " + list.size());
		s.getTransaction().commit();
		
		TrKurir tr = list.get(0);
		return tr.getNik();
	}

	public static TrKurir getKurirByNama(String kur) {
		Session s=HibernateUtil.openSession();
		
		Criteria c=s.createCriteria(TrKurir.class);
		c.add(Restrictions.eq("nama", kur));
		
		List<TrKurir> list = c.list();
		s.getTransaction().commit();
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
}
