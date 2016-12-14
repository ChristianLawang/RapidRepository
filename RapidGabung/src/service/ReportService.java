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

public class ReportService {
	// chris
	public static List<String> getIfPelangganExistForLapPerPelanggan(Date dtAwal, Date dtAkhir){
		Session s = HibernateUtil.openSession();
		Query query = s.createSQLQuery(
			"	select " +
			"    distinct b.pengirim  " +
			"from  " +
			"    tt_header a  " +
			"inner join  " +
			"    tt_data_entry b on b.awb_data_entry = a.awb_header  " +
			"where  " +
			"      b.pengirim not in ( " +
			"                 select x.kode_pelanggan from tr_pelanggan x " +
			"                 ) " +
			"      and a.flag = '0'  " +
			"      and date(a.tgl_create) between :dtAwal and :dtAkhir "
				)
				.setParameter("dtAwal", dtAwal)
				.setParameter("dtAkhir", dtAkhir);
		List<String> returnList = new ArrayList<String>();
		List<String> list=query.list();
		for (String objects : list) {
			returnList.add(objects!= null ? objects : null);
		}
		s.getTransaction().commit();
		return returnList;
	}
	
	public static List<EntryDataShowVO> dataPerpelanggan(Date dtAwal, Date dtAkhir, String kdPelanggan) {
		Session s = HibernateUtil.openSession();
			Query query = s.createSQLQuery("Call reportPerPelanggan(:dtAwal, :dtAkhir, :kdPelanggan)")
					.setParameter("dtAwal", dtAwal)
					.setParameter("dtAkhir", dtAkhir)
					.setParameter("kdPelanggan", kdPelanggan);
			List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		    List<Object[]> list=query.list();
		    for (Object[] objects : list) {
		    	EntryDataShowVO en = new EntryDataShowVO();
		    	en.setAwbData(objects[0] != null ? (String) objects[0] : "");
				en.setPengirim(objects[1] != null ? (String) objects[1] : "");
				en.setTujuan(objects[2] != null ? (String) objects[2] : "");
				en.setPenerima(objects[3] != null ? (String) objects[3] : "");
				en.setNoTlpn(objects[4] != null ? (String) objects[4] : "");
				en.setResiJne(objects[5] != null ? (String) objects[5] : "");
				en.setbFinal(objects[6] != null ? (String) objects[6] : "");
				en.setHarga(objects[7] != null ? (Integer) objects[7] : 0);
				en.setAsuransi(objects[8] != null ? (Integer) objects[8] : 0);
				en.setDiskon(objects[9] != null ? (Integer) objects[9] : 0);
				en.setDiskonRapid(objects[10] != null ? (Integer) objects[10] : 0);
				en.setDiskonJne(objects[11] != null ? (Integer) objects[11] : 0);
				en.setDiskonPel((BigDecimal) objects[12] != null ? (BigDecimal) objects[12] : BigDecimal.ZERO);
				en.settBiaya((BigDecimal) objects[13] != null ? (BigDecimal) objects[13] : BigDecimal.ZERO);
				en.setJmlhPelanggan((BigInteger) objects[14] != null ? (BigInteger) objects[14] : BigInteger.ZERO);
				returnList.add(en);
		    }
		    s.getTransaction().commit();
			return returnList;
		}
	
	public static List<EntryDataShowVO> dataPerperwakilan(
			Date dtAwal, 
			Date dtAkhir, 
			String kodePerwakilan, 
			String kodePelanggan) {
		System.out.println("kodePelanggan : " + kodePelanggan);
		Session s = HibernateUtil.openSession();
		String sql="select z.kode_perwakilan, count(z.kode_perwakilan) AWB, "
				+ "sum(z.pbclose) BERAT, sum(z.total_biaya) TOTAL_BIAYA, maxKardus(z.kode_perwakilan) "
				+ "from tt_data_entry z, tt_header a "
				+ "where a.awb_header = z.awb_data_entry and a.flag = 0  "
				+ "and date(z.tgl_create) between :pTglMulai and :pTglAkhir ";
				if(!kodePerwakilan.equals("All Perwakilan")){
					sql += "and left(z.asal_paket,3) = '"+kodePerwakilan+"'";
				}
				if(kodePelanggan!=null){
				System.out.println("masuk if");
				sql += "and z.pengirim = '"+kodePelanggan+"'";
				}
				sql += "and z.kode_perwakilan is not null group by z.kode_perwakilan";
	
		Query query = s.createSQLQuery(sql).setParameter("pTglMulai", dtAwal).setParameter("pTglAkhir", dtAkhir);
		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
	    List<Object[]> list=query.list();
	    for (Object[] objects : list) {
	    	EntryDataShowVO en = new EntryDataShowVO();
	    	en.setKdPerwakilan(objects[0] != null ? (String) objects[0] : "");
	    	en.setCount((BigInteger) objects[1] != null ? (BigInteger) objects[1] : BigInteger.ZERO);
	    	en.setSumBerat((Double) objects[2] != null ? (Double) objects[2] : 0);
	    	en.settBiaya((BigDecimal) objects[3] != null ? (BigDecimal) objects[3] : BigDecimal.ZERO);
	    	en.setIdDataPaket((String) objects[4] != null ? (String) objects[4] : "-");
	    	returnList.add(en);
	    }
	    s.getTransaction().commit();
		return returnList;
	}
	
	public static List<EntryDataShowVO> dataWakilan(Date dtAwal, Date dtAkhir) {
		Session s = HibernateUtil.openSession();
			Query query = s.createSQLQuery("Call reportPerPerwakilan(:dtAwal, :dtAkhir)")
					.setParameter("dtAwal", dtAwal)
					.setParameter("dtAkhir", dtAkhir);
//					.setParameter("kdPelanggan", kdPelanggan);
		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
	    List<Object[]> list=query.list();
	    for (Object[] objects : list) {
	    	EntryDataShowVO en = new EntryDataShowVO();
	    	en.setKdPerwakilan(objects[0] != null ? (String) objects[0] : "");
	    	en.setCount((BigInteger) objects[1] != null ? (BigInteger) objects[1] : BigInteger.ZERO);
	    	en.setSumBerat((Double) objects[2] != null ? (Double) objects[2] : 0);
	    	en.settBiaya((BigDecimal) objects[3] != null ? (BigDecimal) objects[3] : BigDecimal.ZERO);
	    	returnList.add(en);
	    }
	    s.getTransaction().commit();
		return returnList;
	}
	
	// chris
	public static List<EntryDataShowVO> dataPerPelangganNativeSQL(
			Date dtAwal, 
			Date dtAkhir, 
			String kdPelanggan, 
			String kodePerwakilan,
			Boolean isAllPelanggan){
		
		Session s = HibernateUtil.openSession();
		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		String sql = "";
		if(!isAllPelanggan){
		sql = 
				"select " +
			    "   c.awb_data, " +  // 0
			    "   b.pengirim,  " + // 1
			    "   b.tujuan,  " + // 2
			    " 	b.penerima, " + // 3
			    "   CONVERT(b.telp_penerima, CHAR(50)) telp_penerima,  " +	// 4		    
			    "   a.resi_jne,  " + // 5
			    "   b.pbclose,  " + // 6
			    "   CONVERT(b.harga, CHAR(50)) harga,  " + // 7
			    "   CONVERT(b.asuransi, CHAR(50)) asuransi," + // 8
			    "   CONVERT(case " + 
			    "      when b.jne_flag=0 then diskon_rapid " +
			    "      else diskon_jne " + //
			    "      end, CHAR(50)) diskon, " + // 9
			    "   CONVERT(d.diskon_rapid, CHAR(50)) diskon_rapid, " + // 10
			    "   CONVERT(d.diskon_jne, CHAR(50)) diskon_jne, " + // 11
			          
			    "   case " +
			    "      when b.jne_flag=0 then CONVERT(((diskon_rapid/100) * b.harga), CHAR(50)) " +
			    "      else CONVERT(((diskon_jne/100) * b.harga), CHAR(50)) " +
			    "      end as 'diskon_pelanggan', " + // 12
			          
			    "   case " +
			    "      when b.jne_flag=0 then CONVERT((b.harga - ((diskon_rapid/100) * b.harga)), CHAR(50)) " +
			    "      else CONVERT((b.harga - ((diskon_jne/100) * b.harga)), CHAR(50)) " +
			    "      end as 'total_biaya' " + // 13
				"from tt_header a " +
				"inner join tt_data_entry b on a.awb_header = b.awb_data_entry " +
				"inner join tt_poto_timbang c on a.awb_header = c.awb_poto_timbang " +
				"inner join tr_pelanggan d on b.pengirim = d.nama_akun " +
				"inner join tr_cabang e on b.asal_paket = e.kode_cabang " +
				"where a.flag = 0 " +
				"and date(a.tgl_create) between :dtAwal and :dtAkhir " +
				"and d.nama_akun = '"+kdPelanggan+"' " +
				"and e.kode_perwakilan = :kodePerwakilan";
		
				Query query = s.createSQLQuery(sql)
						.setParameter("dtAwal", dtAwal)
						.setParameter("dtAkhir", dtAkhir)
						.setParameter("kodePerwakilan", kodePerwakilan);
		
				
				List<Object[]> list = query.list();
				
				for (Object[] objects : list) {
					EntryDataShowVO en = new EntryDataShowVO();
					en.setAwbData(objects[0] != null ? (String) objects[0] : "");
					en.setPengirim(objects[1] != null ? (String) objects[1] : "");
					en.setTujuan(objects[2] != null ? (String) objects[2] : "");
					en.setPenerima(objects[3] != null ? (String) objects[3] : "");
					en.setNoTlpn(objects[4] != null ? (String) objects[4] : "");
					en.setResiJne(objects[5] != null ? (String) objects[5] : "");
					en.setbFinal(objects[6] != null ? (String) objects[6] : "");
					en.setHarga(objects[7] != null ? Integer.parseInt((String) objects[7]) : 0);
					en.setAsuransi(objects[8] != null ? Integer.parseInt((String) objects[8]) : 0);
					en.setDiskon(objects[9] != null ? Integer.parseInt((String) objects[9]) : 0);
					en.setDiskonRapid(objects[10] != null ? Integer.parseInt((String) objects[10]) : 0);
					en.setDiskonJne(objects[11] != null ? Integer.parseInt((String) objects[11]) : 0);
					en.setDiskonPel(objects[12] != null ? new BigDecimal((String) objects[12]) : BigDecimal.ZERO);
					en.settBiaya(objects[13] != null ? new BigDecimal((String) objects[13]) : BigDecimal.ZERO);
					returnList.add(en);
				}
				s.getTransaction().commit();
				return returnList;
		}else{
			sql = 
				"	select " +
				"       b.nama_sales,  " +
				"       a.pengirim,  " +
				"       count(d.awb_data) AWB,  " +
				"       sum(a.pbclose) BERAT,  " +
				"       TRUNCATE(sum(a.bclose),2) BERAT_ASLI,  " +
				"       sum(a.harga) TOTAL_BIAYA, " +
				"       case " +
				"           when c.resi_jne is null then sum( (a.harga - ((b.diskon_rapid/100) * a.harga)) ) " +
				"           else sum( (a.harga - ((b.diskon_jne/100) * a.harga)) ) " +
				"           end as HARGA_SETELAH_DISKON, " +
				"       case " +
				"           when c.resi_jne is null then sum( ((diskon_rapid/100) * a.harga) ) " +
				"           else sum( ((diskon_jne/100) * a.harga) ) " +
				"           end as TOTAL_DISKON " +
				"from tt_data_entry a, tr_pelanggan b, tt_header c, tt_poto_timbang d, tr_cabang e " +
				"where a.awb_data_entry = c.awb_header " +
				"and c.awb_header = d.awb_poto_timbang " +
				"and a.pengirim = b.nama_akun " +
				"and a.kode_perwakilan is not null and c.flag=0 " +
				"and a.asal_paket = e.kode_cabang " +
				"and date(a.tgl_create) between :dtAwal and :dtAkhir " +
				"and e.kode_perwakilan = :kodePerwakilan " +
				"group by a.pengirim";
			Query query = s.createSQLQuery(sql)
					.setParameter("dtAwal", dtAwal)
					.setParameter("dtAkhir", dtAkhir)
					.setParameter("kodePerwakilan", kodePerwakilan);
			
			List<Object[]> list = query.list();
			
			for (Object[] objects : list) {
		    	EntryDataShowVO en = new EntryDataShowVO();
		    	en.setNmSales(objects[0] != null ? (String) objects[0] : "");
				en.setPengirim(objects[1] != null ? (String) objects[1] : "");
				en.setCount((BigInteger) objects[2] != null ? (BigInteger) objects[2] : BigInteger.ZERO);
				en.setSumBerat((Double) objects[3] != null ? (Double) objects[3] : 0);
				en.setSumBeratAsli((Double) objects[4] != null ? (Double) objects[4] : 0);
				en.settBiaya((BigDecimal) objects[5] != null ? (BigDecimal) objects[5] : BigDecimal.ZERO);
				en.setHargaSetelahDiskon((BigDecimal) objects[6] != null ? (BigDecimal) objects[6] : BigDecimal.ZERO);
				en.setTotalDiskon((BigDecimal) objects[7] != null ? (BigDecimal) objects[7] : BigDecimal.ZERO);
			
				returnList.add(en);
			}
			s.getTransaction().commit();
			return returnList;
		}
	}
	
	public static List<EntryDataShowVO> dataPerpelangganAll(Date dtAwal, Date dtAkhir) {
		Session s = HibernateUtil.openSession();
			Query query = s.createSQLQuery("Call reportPerPelangganAll(:dtAwal, :dtAkhir)")
					.setParameter("dtAwal", dtAwal)
					.setParameter("dtAkhir", dtAkhir);
			List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		    List<Object[]> list=query.list();
		    for (Object[] objects : list) {
		    	EntryDataShowVO en = new EntryDataShowVO();
		    	en.setNmSales(objects[0] != null ? (String) objects[0] : "");
				en.setPengirim(objects[1] != null ? (String) objects[1] : "");
				en.setCount((BigInteger) objects[2] != null ? (BigInteger) objects[2] : BigInteger.ZERO);
				en.setSumBerat((Double) objects[3] != null ? (Double) objects[3] : 0);
				en.setSumBeratAsli((Double) objects[4] != null ? (Double) objects[4] : 0);
				en.settBiaya((BigDecimal) objects[5] != null ? (BigDecimal) objects[5] : BigDecimal.ZERO);
				en.setHargaSetelahDiskon((BigDecimal) objects[6] != null ? (BigDecimal) objects[6] : BigDecimal.ZERO);
				en.setTotalDiskon((BigDecimal) objects[7] != null ? (BigDecimal) objects[7] : BigDecimal.ZERO);
			
				returnList.add(en);
		    }
		    s.getTransaction().commit();
			return returnList;
		}
	
	
	//-----------------------------------
	public static List<EntryDataShowVO> getDataTagihan(Date dateNow) {
		Session s = HibernateUtil.openSession();
		String sql = "select 'CGK001' as ASAL, a.pengirim, b.kode_pickup, "
				+ "b.awb_data, a.tgl_create, "
				+ "a.tujuan, a.penerima, a.telp_penerima, "
				+ "c.resi_jne,a.bclose,a.pbclose, "
				+ "a.harga,a.asuransi,a.total_diskon, "
				
				+ "case when a.jne_flag=0 then ((d.diskon_rapid/100) * a.harga) "
				+ "else ((diskon_jne/100) * a.harga) end as DISKON_PEL, a.biaya "
				
				+ "from tt_data_entry a, tt_poto_timbang b, tt_header c, tr_pelanggan d "
				+ "where a.awb_data_entry=b.awb_poto_timbang and c.awb_header=a.awb_data_entry "
				+ "and a.pengirim=d.nama_akun and date(a.tgl_create)=:pDate and c.flag=0 order by a.pengirim";
		Query query = s.createSQLQuery(sql).setParameter("pDate", dateNow);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setAsalPaket(objects[0] != null ? (String) objects[0] : "");
			en.setPengirim(objects[1] != null ? (String) objects[1] : "");
			en.setKdPickup(objects[2] != null ? (String) objects[2] : "");
			en.setAwbData(objects[3] != null ? (String) objects[3] : "");
			en.setCreated((Date) objects[4]);
			en.setTujuan(objects[5] != null ? (String) objects[5] : "");
			en.setPenerima(objects[6] != null ? (String) objects[6] : "");
			en.setNoTlpn(objects[7] != null ? (String) objects[7] : "");
			en.setResiJne(objects[8] != null ? (String) objects[8] : "");
			en.setbFinal(objects[9] != null ? (String) objects[9] : "");
			en.setBpFinal(objects[10] != null ? (String) objects[10] : "");
			en.setHarga(objects[11] != null ? (Integer) objects[11] : 0);
			en.setAsuransi(objects[12] != null ? (Integer) objects[12] : 0);
			en.setDiskon(objects[13] != null ? (Integer) objects[13] : 0);
			en.setDiskonPel((BigDecimal) objects[14] != null ? (BigDecimal) objects[14] : BigDecimal.ZERO);
			en.setBiayaSblmDiskon(objects[15] != null ? (Integer) objects[15] : 0);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
	
	

	public static List<EntryDataShowVO> getDataResi(Date dateNow) {
		Session s = HibernateUtil.openSession();
		String sql = "select c.awb_data, b.telp_penerima, b.penerima, b.reseller, a.resi_jne, a.tgl_create "
				+ "from tt_header a, tt_data_entry b, tt_poto_timbang c, tr_perwakilan d "
				+ "where a.awb_header = b.awb_data_entry  "
				+ "and a.awb_header = c.awb_poto_timbang  "
				+ "and b.tujuan = d.kode_zona  and date(a.tgl_create)=:pDate order by a.tgl_create";
		Query query = s.createSQLQuery(sql).setParameter("pDate", dateNow);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setAwbData(objects[0] != null ? (String) objects[0] : "");
			en.setNoTlpn(objects[1] != null ? (String) objects[1] : "");
			en.setPenerima(objects[2] != null ? (String) objects[2] : "");
			en.setResseler(objects[3] != null ? (String) objects[3] : "");
			en.setResiJne(objects[4] != null ? (String) objects[4] : "");
			en.setCreated((Date) objects[5]);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
	
	//TODO LIST
	public static List<EntryDataShowVO> getHistory(String strAwb) {
		Session s = HibernateUtil.openSession();
		String sql = "select b.user user_poto, b.tgl_create wk_poto, "
				+ "c.user user_entry, c.tgl_create wk_entry, "
				+ "d.oleh user_manifest, d.tgl_create tgl_manifest, d.id_kardus, "
				+ "d.oleh user_terima, d.tgl_create tgl_terima, "
				+ "d.oleh user_gabung, d.tgl_create tgl_gabung, "
				+ "a.user_create user_perwakilan, a.wk_inbound tgl_perwakilan, "
				+ "e.tgl_create tgl_kirim, f.nama nama_kurir, e.masalah "
				+ "from tt_header a, tt_poto_timbang b, tt_data_entry c, tt_gabung_paket d, tt_status_kurir_in e, tr_kurir f "
				+ "where a.awb_header = b.awb_poto_timbang "
				+ "and a.awb_header = c.awb_data_entry "
				+ "and a.awb_header = d.awb "
				+ "and a.awb_header = e.id_barang "
				+ "and e.id_kurir = f.nik "
				+ "and a.awb_header =:pAwb";
		Query query = s.createSQLQuery(sql).setParameter("pAwb", strAwb);

		List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
		List<Object[]> list = query.list();
		for (Object[] objects : list) {
			EntryDataShowVO en = new EntryDataShowVO();
			en.setUserPoto(objects[0] != null ? (String) objects[0] : "");
			
			en.setNoTlpn(objects[1] != null ? (String) objects[1] : "");
			en.setPenerima(objects[2] != null ? (String) objects[2] : "");
			en.setResseler(objects[3] != null ? (String) objects[3] : "");
			en.setResiJne(objects[4] != null ? (String) objects[4] : "");
			en.setCreated((Date) objects[5]);
			returnList.add(en);
		}
		s.getTransaction().commit();
		return returnList;
	}
	
	
	
}