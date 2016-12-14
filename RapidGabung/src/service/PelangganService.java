package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import entity.TrDiskon;
import entity.TrPelanggan;
import entity.TrPickup;
import entity.TrSales;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import VO.SMSExportVO;
import VO.TagihanVO;
import util.DateUtil;
import util.HibernateUtil;

public class PelangganService {
	
	// chris
	public static List<TrPelanggan> getDataPelangganForTagihan(Date awal, Date akhir, String kodeCabang) {
		Session s = HibernateUtil.openSession();
		String sql = "select distinct b.kode_pelanggan, c.nama_akun, c.email "
				+ " from tt_header a, tt_poto_timbang b, tr_pelanggan c where a.awb_header = b.awb_poto_timbang "
				+ "and b.kode_pelanggan = c.kode_pelanggan and a.flag=0 and date(a.tgl_create) between '"+awal+"' and '"+akhir+"' ";
		
		if(!kodeCabang.equals("All Cabang")){
			sql+= "and substr(b.asal_paket,1,3) = '"+kodeCabang+"' ";
		}
		
		System.out.println(sql);
		Query query = s.createSQLQuery(sql);

		List<TrPelanggan> returnList = new ArrayList<TrPelanggan>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			TrPelanggan en = new TrPelanggan();
			 en.setKodePelanggan(objects[0] != null ? (String) objects[0] : "");
			 en.setNamaAkun(objects[1] != null ? (String) objects[1] : "");
			 en.setEmail(objects[2] != null ? (String) objects[2] : "");
			returnList.add(en);
		}
//		s.close();
		s.getTransaction().commit();
		return returnList;
	}
	
	public static List<TrPelanggan> getDataPelangganForNewsLetter(String kodeCabang) {
		Session s = HibernateUtil.openSession();
		String sql = "select distinct b.kode_pelanggan, c.nama_akun, c.email "
				+ " from tt_header a, tt_poto_timbang b, tr_pelanggan c where a.awb_header = b.awb_poto_timbang "
				+ "and b.kode_pelanggan = c.kode_pelanggan and a.flag=0 ";
		
		if(!kodeCabang.equals("All Cabang")){
			sql+= "and substr(b.asal_paket,1,3) = '"+kodeCabang+"' ";
		}
		
		System.out.println(sql);
		Query query = s.createSQLQuery(sql);

		List<TrPelanggan> returnList = new ArrayList<TrPelanggan>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			TrPelanggan en = new TrPelanggan();
			 en.setKodePelanggan(objects[0] != null ? (String) objects[0] : "");
			 en.setNamaAkun(objects[1] != null ? (String) objects[1] : "");
			 en.setEmail(objects[2] != null ? (String) objects[2] : "");
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
	
	public static List<TrSales> getDataSales(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select a.nama_sales"
			  + " from tr_sales a;";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
//		session.close();
		session.getTransaction().commit();
		
		List<TrSales> returnList = new ArrayList<TrSales>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrSales everyRow = new TrSales();
					
			//get data WILAYAH
			everyRow.setNamaSales((String) row.get("NAMA_SALES"));
				
			returnList.add(everyRow);
		}
		return returnList;
		
	}
	
	public static List<TrPelanggan> getDataReferensi(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select a.nama_akun"
			  + " from tr_pelanggan a;";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
//		session.close();
		session.getTransaction().commit();
		
		List<TrPelanggan> returnList = new ArrayList<TrPelanggan>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrPelanggan everyRow = new TrPelanggan();
					
			//get data WILAYAH
			everyRow.setNamaAkun((String) row.get("NAMA_AKUN"));
				
			returnList.add(everyRow);
		}
		return returnList;
		
	}


	public static List<TrPelanggan> getDataPelanggan_() {
		GenericService service = new GenericService();
		List<TrPelanggan> lsPelanggan = GenericService.getList(TrPelanggan.class);
		return lsPelanggan;
	}

	public static List<TrPelanggan> getDataPelanggan(String kodePerwakilan) {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select a.kode_pelanggan, a.nama_akun, a.nama_pemilik, a.email"
				+ ", a.telp , a.alamat, a.line, a.instagram, a.keterangan, a.tgl_mulai_diskon" + ", a.diskon_rapid"
				+ ", a.diskon_jne, a.sms, a.referensi, a.nama_sales, a.tgl_gabung ,a.tgl_create" + ", a.tgl_update, a.tgl_gabung "
				+ ", a.jabatan1, a.jabatan2 "
				+ " from tr_pelanggan a "
				+ " where a.flag=0 ";
		if(!kodePerwakilan.equals("All Perwakilan")){
			nativeSql+="and a.asal_pelanggan = '"+kodePerwakilan+"'";
		}
		SQLQuery query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		System.out.println(result);
//		session.close();
		session.getTransaction().commit();

		List<TrPelanggan> returnList = new ArrayList<TrPelanggan>();
		for (Object obj : result) {
			Map row = (Map) obj;
			TrPelanggan everyRow = new TrPelanggan();

			// get data kode pelanggan
			everyRow.setKodePelanggan(
					(String) row.get("KODE_PELANGGAN"));
//			System.out.println(row.get("kode_pelanggan"));
			// everyRow.setAwbData((String)
			// row.get("kode_pelanggan")!=null?(String)
			// row.get("kode_pelanggan"):"");

			// get data nama akun
			everyRow.setNamaAkun((String) row.get("NAMA_AKUN"));

			// get data nama pemilik
			everyRow.setNamaPemilik((String) row.get("NAMA_PEMILIK"));

			// get data email pelanggan
			everyRow.setEmail((String) row.get("EMAIL"));

			// get data telp
			everyRow.setTelp((String) row.get("TELP"));

			// get data Alamat
			everyRow.setAlamat((String) row.get("ALAMAT"));

			// get data line
			everyRow.setLine((String) row.get("LINE"));

			// get data instagram
			everyRow.setInstagram((String) row.get("INSTAGRAM"));
			
			// get data instagram
			everyRow.setKeterangan((String) row.get("KETERANGAN"));

			 //get data telp
			 everyRow.setDiskonRapid((Integer) row.get("DISKON_RAPID"));
			
			 //get data diskon jne
			 everyRow.setDiskonJne((Integer) row.get("DISKON_JNE"));
			 
			//get data TglMulaiDiskon
			 everyRow.setTglMulaiDiskon((Date) row.get("TGL_MULAI_DISKON"));
						 
			// //get data sms
			// everyRow.setSms((String) row.get("SMS"));

			// get data nama sales
			everyRow.setNamaSales((String) row.get("NAMA_SALES"));
			
			// get data instagram
		    everyRow.setReferensi((String) row.get("REFERENSI"));

			 //get tanggal bergabung
			 everyRow.setTglGabung((Date) row.get("TGL_GABUNG"));

			 everyRow.setJabatan1((String) row.get("JABATAN1"));
			 
			 everyRow.setJabatan2((String) row.get("JABATAN2"));
			 
			returnList.add(everyRow);
		}
		return returnList;

	}
	
	public static List<TrPelanggan> getDataPelanggan() {
		Session session = HibernateUtil.openSession();
		String nativeSql = "select a.kode_pelanggan, a.nama_akun, a.nama_pemilik, a.email"
				+ ", a.telp , a.alamat, a.line, a.instagram, a.keterangan, a.tgl_mulai_diskon" + ", a.diskon_rapid"
				+ ", a.diskon_jne, a.sms, a.referensi, a.nama_sales, a.tgl_gabung ,a.tgl_create" + ", a.tgl_update, a.tgl_gabung "
				+ ", a.jabatan1, a.jabatan2 "
				+ " from tr_pelanggan a "
				+ " where a.flag=0 ";
		SQLQuery query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		System.out.println(result);
//		session.close();
		session.getTransaction().commit();

		List<TrPelanggan> returnList = new ArrayList<TrPelanggan>();
		for (Object obj : result) {
			Map row = (Map) obj;
			TrPelanggan everyRow = new TrPelanggan();

			// get data kode pelanggan
			everyRow.setKodePelanggan(
					(String) row.get("KODE_PELANGGAN"));
//			System.out.println(row.get("kode_pelanggan"));
			// everyRow.setAwbData((String)
			// row.get("kode_pelanggan")!=null?(String)
			// row.get("kode_pelanggan"):"");

			// get data nama akun
			everyRow.setNamaAkun((String) row.get("NAMA_AKUN"));

			// get data nama pemilik
			everyRow.setNamaPemilik((String) row.get("NAMA_PEMILIK"));

			// get data email pelanggan
			everyRow.setEmail((String) row.get("EMAIL"));

			// get data telp
			everyRow.setTelp((String) row.get("TELP"));

			// get data Alamat
			everyRow.setAlamat((String) row.get("ALAMAT"));

			// get data line
			everyRow.setLine((String) row.get("LINE"));

			// get data instagram
			everyRow.setInstagram((String) row.get("INSTAGRAM"));
			
			// get data instagram
			everyRow.setKeterangan((String) row.get("KETERANGAN"));

			 //get data telp
			 everyRow.setDiskonRapid((Integer) row.get("DISKON_RAPID"));
			
			 //get data diskon jne
			 everyRow.setDiskonJne((Integer) row.get("DISKON_JNE"));
			 
			//get data TglMulaiDiskon
			 everyRow.setTglMulaiDiskon((Date) row.get("TGL_MULAI_DISKON"));
						 
			// //get data sms
			// everyRow.setSms((String) row.get("SMS"));

			// get data nama sales
			everyRow.setNamaSales((String) row.get("NAMA_SALES"));
			
			// get data instagram
		    everyRow.setReferensi((String) row.get("REFERENSI"));

			 //get tanggal bergabung
			 everyRow.setTglGabung((Date) row.get("TGL_GABUNG"));

			 everyRow.setJabatan1((String) row.get("JABATAN1"));
			 
			 everyRow.setJabatan2((String) row.get("JABATAN2"));
			 
			returnList.add(everyRow);
		}
		return returnList;

	}	
	
	public static Boolean showTableSetelahDelete(String Nik) {
		Session sess = HibernateUtil.openSession();
//		sess.beginTransaction();
		
		String sql = "UPDATE tr_pelanggan a "
				+ "SET flag = :pSampah  "
				+ "WHERE a.kode_pelanggan = :pKodeCab";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pSampah", 1)
				.setParameter("pKodeCab", Nik);
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
//		sess.close();
		return true;
	}


	// chris nambah
	public static TrPelanggan getPelangganByName(String strNamaPelanggan) {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TrPelanggan.class);
		if (strNamaPelanggan==null||!strNamaPelanggan.equals("")) {
			c.add(Restrictions.eq("namaAkun", strNamaPelanggan));
		}
		List<TrPelanggan> list = c.list();
//		s.close();
		s.getTransaction().commit();
		if (list.size() > 0) {
			return list.get(0); // pastikan unik, kalo lebih diambil record
								// pertama
		} else {
			return null;
		}
	}
	
	public static Boolean updateDataPelanggan(String kdPelanggan, String nmAkun, String nmPemilik
			, String email, String telp, String alamat
			, String lineId, String instag, String ket, Integer disRapid, Integer disJne
			, Object nmSales, Object nmRef, Date dtMulaiDiskon, Date dtMulaiGabung, Object cbJabatan1
			, Object cbJabatan2) {
		Session sess = HibernateUtil.openSession();
		String sql = "update tr_pelanggan a "
				+ "set a.kode_pelanggan = :pKdPelanggan "
				+ ", a.nama_akun = :pNmAkun "
				+ ", a.nama_pemilik = :pNmPemilik "
				+ ", a.email = :pEmail "
				+ ", a.telp = :pTelp "
				+ ", a.alamat = :pAlamat "
				+ ", a.line = :pLineId "
				+ ", a.instagram = :pInstag "
				+ ", a.keterangan = :pKet "
				+ ", a.diskon_rapid = :pDisRapid "
				+ ", a.diskon_jne = :pDisJne "
				+ ", a.nama_sales = :pNmSales "
				+ ", a.referensi = :pNmRef "
				+ ", a.tgl_mulai_diskon = :pDtMulaiDiskon "
				+ ", a.tgl_gabung = :pDtMulaiGabung "
				+ ", a.tgl_update = :pUpdated "
				+ ", a.jabatan1 = :pJabatan1 "
				+ ", a.jabatan2 = :pJabatan2 "
				+ "where a.kode_pelanggan = :pKdPelanggan";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pKdPelanggan", kdPelanggan)
				.setParameter("pNmAkun", nmAkun)
				.setParameter("pNmPemilik", nmPemilik)
				.setParameter("pEmail", email)
				.setParameter("pTelp", telp)
				.setParameter("pAlamat", alamat)
				.setParameter("pLineId", lineId)
				.setParameter("pInstag", instag)
				.setParameter("pKet", ket)
				.setParameter("pDisRapid", disRapid)
				.setParameter("pDisJne", disJne)
				.setParameter("pNmSales", nmSales)
				.setParameter("pNmRef", nmRef)
				.setParameter("pDtMulaiDiskon", dtMulaiDiskon)
				.setParameter("pDtMulaiGabung", dtMulaiGabung)
				.setParameter("pUpdated", DateUtil.getNow())
				.setParameter("pJabatan1", cbJabatan1)
				.setParameter("pJabatan2", cbJabatan2);
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
//		sess.close();
		return true;
	}
	
	public static List<TrPelanggan> getDataPelangganByID(String id) {
		Session s=HibernateUtil.openSession();
		
		Criteria c=s.createCriteria(TrPelanggan.class);
		c.add(Restrictions.eq("kodePelanggan", id));
		
		List<TrPelanggan> list = c.list();
		
//		s.close();
		s.getTransaction().commit();
		
		return list;
	}

	public static BigInteger getBigInteger(Object value) {
		BigInteger ret = null;
		if (value != null) {
			if (value instanceof BigInteger) {
				ret = (BigInteger) value;
			} else if (value instanceof String) {
				ret = new BigInteger((String) value);
			} else if (value instanceof BigDecimal) {
				ret = ((BigDecimal) value).toBigInteger();
			} else if (value instanceof Number) {
				ret = BigInteger.valueOf(((Number) value).longValue());
			} else {
				throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass()
						+ " into a BigInteger.");
			}
		}
		return ret;
	}

	// chris
	public static TrPelanggan getPelangganById(String kodePelanggan) {
		Session s = HibernateUtil.openSession();
		System.out.println("--> kodePelanggan : " + kodePelanggan);
		Criteria c = s.createCriteria(TrPelanggan.class);
		c.add(Restrictions.eq("kodePelanggan", kodePelanggan));
		List<TrPelanggan> data = c.list();
//		s.close();
		s.getTransaction().commit();
		return data.get(0);

	}

	// chris
	public static List<TagihanVO> getNotifikationEmailPelanggan(String kodePelanggan, Date awal, Date akhir) {
		Session session = HibernateUtil.openSession();

		String strQuery = 
				"select " +
			    "	c.awb_data, " +
			    "   b.pengirim, " +
//			    "   b.tujuan, " +
			    "	e.kecamatan, " +
			    "   b.penerima, " +
			    "   b.telp_penerima, " +
			    "   a.resi_jne, " +
			    "   b.pbclose, " +
			    "   b.harga, " +
			    "   b.asuransi, " +

			    "   case " +
			    "   when a.resi_jne is null then d.diskon_rapid " +
			    "   else d.diskon_jne " +
			    "   end as diskon, " +

			    "   d.diskon_rapid, " +
			    "   d.diskon_jne, " +

			    "   case " +
			    "   when a.resi_jne is null then ((diskon_rapid/100) * b.harga) " +
			    "   else ((diskon_jne/100) * b.harga) " +
			    "   end as diskon_pelanggan, " +

			    "  case " +
			    "  when a.resi_jne is null then (b.harga - ((diskon_rapid/100) * b.harga)) " +
			    "  else (b.harga - ((diskon_jne/100) * b.harga)) " +
			    "  end as total_biaya, " +

			    "  (select count(*) from tt_header a, tt_data_entry b, tt_poto_timbang c, tr_pelanggan d " +
			    "  where a.awb_header = b.awb_data_entry " +
			    "  and a.awb_header = c.awb_poto_timbang " +
			    "  and b.pengirim = d.nama_akun " +
			    "  and d.kode_pelanggan = '" + kodePelanggan + "' " +
			    "  and a.flag=0 and date(a.tgl_create) between '"+awal+"' and '"+akhir+"') jumlah_paket, c.kode_pickup, a.tgl_create " +

			" from tt_header a, tt_data_entry b, tt_poto_timbang c, tr_pelanggan d,  tr_perwakilan e" +
			" where " +
			"      a.awb_header = b.awb_data_entry " +
			"      and a.awb_header = c.awb_poto_timbang " +
			"      and b.pengirim = d.nama_akun " +
			"	   and e.kode_zona = b.tujuan " +
			"      and d.kode_pelanggan = '" + kodePelanggan + "' " +
			"      and a.flag=0 and date(a.tgl_create) between '"+awal+"' and '"+akhir+"' " + 
			"	   order by penerima";
		
		System.out.println("--> "+kodePelanggan+" strQuery : " + strQuery);
		SQLQuery query = session.createSQLQuery(strQuery);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
//		session.close();
		session.getTransaction().commit();

		List<TagihanVO> returnList = new ArrayList<TagihanVO>();
		for (Object obj : result) {
			Map row = (Map) obj;
			Integer diskon = 0;
			if(row.get("diskon") instanceof BigInteger){
				BigInteger val = (BigInteger) row.get("diskon");
				diskon = val.intValue();
			}else if(row.get("diskon") instanceof Integer){
				diskon = (Integer) row.get("diskon");
			}
			
			TagihanVO everyRow = new TagihanVO(
							(String) row.get("AWB_DATA"),
							(String) row.get("PENGIRIM"),
							(String) row.get("KECAMATAN"),
							(String) row.get("PENERIMA"),
							(String) row.get("TELP_PENERIMA"),
							(String) row.get("RESI_JNE"),
							(String) row.get("PBCLOSE"),
							(Integer) row.get("HARGA"),
							(Integer) row.get("ASURANSI"),
							diskon,
							(Integer) row.get("DISKON_RAPID"),
							(Integer) row.get("DISKON_JNE"),
							(BigDecimal) row.get("diskon_pelanggan"),
							(BigDecimal) row.get("total_biaya"),
							(BigInteger) row.get("jumlah_paket"),
							(String) row.get("KODE_PICKUP"),
							(Date) row.get("TGL_CREATE")
						);
			returnList.add(everyRow);
		}
		return returnList;
	}

	// chris
	public static List<TrPelanggan> getSampleData() {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrPelanggan.class);
		c.setMaxResults(2);
		List<TrPelanggan> result = c.list();
//		s.close();
		s.getTransaction().commit();
		return result;
	}

	public static List<TrPelanggan> getDataPelangganForTagihan(Date awal, Date akhir) {
		Session s = HibernateUtil.openSession();
		String sql = "select distinct b.kode_pelanggan, c.nama_akun, c.email "
				+ " from tt_header a, tt_poto_timbang b, tr_pelanggan c " + "where a.awb_header = b.awb_poto_timbang "
				+ "and b.kode_pelanggan = c.kode_pelanggan " + "and a.flag=0 and date(a.tgl_create) between :pDAwl and :pDAkhir ";
		Query query = s.createSQLQuery(sql)
				.setParameter("pDAwl", awal)
				.setParameter("pDAkhir", akhir);

		List<TrPelanggan> returnList = new ArrayList<TrPelanggan>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			TrPelanggan en = new TrPelanggan();
			 en.setKodePelanggan(objects[0] != null ? (String) objects[0] : "");
			 en.setNamaAkun(objects[1] != null ? (String) objects[1] : "");
			 en.setEmail(objects[2] != null ? (String) objects[2] : "");
			returnList.add(en);
		}
//		s.close();
		s.getTransaction().commit();
		return returnList;
	}
	
	public static List<SMSExportVO> getSMSRapid() {
		Session session = HibernateUtil.openSession();
		String nativeSql = 

//			"	select b.telp_penerima, b.penerima, date(a.created) tanggal, c.awb_data, 'www.rapid.co.id' web" +
//			"	from tt_data_poto a, tt_data_paket b, tt_poto_paket c " +
//			"	where a.id_data_poto = b.id_data_paket " +
//			"	and a.id_data_poto = c.id_poto_paket " +
//			"	and substr(a.created,1,10) = curdate() ";
		
			"	select b.telp_penerima, b.penerima, b.pengirim, date(a.tgl_create) tanggal, c.awb_data, 'www.rapid.co.id' web " +
			"	from tt_header a, tt_data_entry b, tt_poto_timbang c " +
			"	where a.awb_header = b.awb_data_entry " +
			"	and a.awb_header = c.awb_poto_timbang " +
			"	and penerima not like '%xxx%' " +
			"	and date(a.tgl_create) = date(date_sub(now(), interval 6 hour))";
				
				
		System.out.println(nativeSql);
		SQLQuery query = session.createSQLQuery(nativeSql);
		List<Object[]> list = query.list();
//		session.close();
		
		System.out.println("--> sms Rapid " + list.size());
		session.getTransaction().commit();
		
		List<SMSExportVO> returnRes = new ArrayList<SMSExportVO>();
		
		for (Object[] everyRow : list) {
//			Map row = (Map) everyRow;
			SMSExportVO vo = new SMSExportVO();
			vo.setTelpPenerima((String) everyRow[0]);
			vo.setPenerima((String) everyRow[1]);
			vo.setPengirim((String) everyRow[2]);
			vo.setTanggal((Date) everyRow[3]);
			vo.setAWB((String)everyRow[4]);
			vo.setWeb((String)everyRow[5]);
			
			returnRes.add(vo);
		}
		
		return returnRes;
	}
	
	// chris
	public static List<SMSExportVO> getSMSJNE() {
		Session session = HibernateUtil.openSession();
		String nativeSql = 

//			"	select b.telp_penerima telp, b.penerima, date(a.created) tanggal, a.resi_jne awb, 'www.jne.co.id' web " +
//			"	from tt_data_poto a, tt_data_paket b, tt_poto_paket c " +
//			"	where a.id_data_poto = b.id_data_paket " +
//			"	and a.id_data_poto = c.id_poto_paket " +
//			"	and substr(a.created,1,10) = curdate() " +
//			"	and a.resi_jne is not null";
				
			"	select b.telp_penerima, b.penerima, b.pengirim, date(a.tgl_create) tanggal, a.resi_jne, 'www.jne.co.id' web" +
			"	from tt_header a, tt_data_entry b, tt_poto_timbang c " +
			"	where a.awb_header = b.awb_data_entry " +
			"	and a.awb_header = c.awb_poto_timbang " +
			"	and a.resi_jne is not null " +
			"	and penerima not like '%xxx%' " +
			"	and date(a.tgl_create) = date(date_sub(now(), interval 6 hour))";
		
		SQLQuery query = session.createSQLQuery(nativeSql);
//		List result = query.list();
		List<Object[]> list = query.list();
		
		System.out.println("--> sms JNE " + list.size());
//		session.close();
		session.getTransaction().commit();
		
		List<SMSExportVO> returnRes = new ArrayList<SMSExportVO>();
		
		for (Object[] everyRow : list) {
//			Map row = (Map) everyRow;
			SMSExportVO vo = new SMSExportVO();
			vo.setTelpPenerima((String) everyRow[0]);
			vo.setPenerima((String) everyRow[1]);
			vo.setPengirim((String) everyRow[2]);
			vo.setTanggal((Date) everyRow[3]);
			vo.setAWB((String)everyRow[4]);
			vo.setWeb((String)everyRow[5]);
			
			returnRes.add(vo);
		}
		
		return returnRes;
	}
	
	public static List<TrPelanggan> getPelangganForCabang(Date dtPeriode) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrPelanggan.class);
		c.add(Restrictions.gt("tglCreate", new Timestamp(dtPeriode.getTime ())));
		@SuppressWarnings("unchecked")
		List<TrPelanggan> data = c.list();
		s.getTransaction().commit();
		return data;
	}
	
	public static void saveAll(List<TrPelanggan> dataPelanggan) {
		Session s = HibernateUtil.openSession();
		
		for (TrPelanggan data : dataPelanggan) {
			Criteria c = s.createCriteria(TrPelanggan.class);
			c.add(Restrictions.eq("kodePelanggan", data.getKodePelanggan()));
			List<TrPelanggan> listPelanggan = c.list();
			if(listPelanggan.isEmpty()){
				s.save(data);
			}
		}
		s.getTransaction().commit();	
	}
	
	public static List<TrSales> getDataSales2(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select a.nama "
			  + " from tr_kurir a where id_jabatan='Sales' and a.flag=0";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
//		session.close();
		session.getTransaction().commit();
		
		List<TrSales> returnList = new ArrayList<TrSales>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrSales everyRow = new TrSales();
					
			//get data WILAYAH
			everyRow.setNamaSales((String) row.get("NAMA"));
				
			returnList.add(everyRow);
		}
		return returnList;
		
	}
	
	public static List<TrSales> getDataAgen(){
		Session session=HibernateUtil.openSession();
		String nativeSql = 
				" select a.nama "
			  + " from tr_kurir a where id_jabatan='Agen' and a.flag=0";
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
//		session.close();
		session.getTransaction().commit();
		
		List<TrSales> returnList = new ArrayList<TrSales>();	
		for (Object obj : result) {
			Map row = (Map) obj;
			TrSales everyRow = new TrSales();
					
			//get data WILAYAH
			everyRow.setNamaSales((String) row.get("NAMA"));
				
			returnList.add(everyRow);
		}
		return returnList;
		
	}

	// chris nambah
	public static List<TrPelanggan> getAllPelanggan() {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TrPelanggan.class);
		c.add(Restrictions.eq("flag", 0));

		List<TrPelanggan> list = c.list();
		s.getTransaction().commit();
		return list;
	}

	// chris nambah
	public static List<TrDiskon> getDataDiskonByPelangganID(String kode) {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TrDiskon.class);
		c.add(Restrictions.eq("flag", 0));
		c.add(Restrictions.eq("kodePelanggan", kode));

		List<TrDiskon> list = c.list();
		s.getTransaction().commit();
		return list;
	}

	public static void setDiskonFlag(String id, int flag) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TrDiskon.class);
		c.add(Restrictions.eq("idDiskon", id));
		List<TrDiskon> data = c.list();
		s.getTransaction().commit();
		TrDiskon willProcess = data.get(0);
		willProcess.setFlag(flag);
		GenericService.save(TrDiskon.class, willProcess, false);
	}

	// chris
	public static List<TrPelanggan> getPelangganByKodePerwakilan(String t1) {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TrPelanggan.class);
		c.add(Restrictions.eq("flag", 0));

		List<TrPelanggan> list = c.list();
		s.getTransaction().commit();
		return list;
	}
}
