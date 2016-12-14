package service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Map;

import VO.BrowseSemuaDataVO;
import VO.EntryDataShowVO;
import VO.KurirInVO;
import entity.TtDataEntry;
import entity.TtHeader;
import entity.TtPotoTimbang;
import util.DateUtil;
import util.HibernateUtil;

public class BrowseSemuaDataService {
	
	//OK
	public static List<BrowseSemuaDataVO> getBrowseSemuaData(Date TglMulai, Date TglAkhir, Boolean Flag){
		Session session = HibernateUtil.openSession();
		String nativeSql = 
				"select distinct c.awb_data, a.tgl_create, c.layanan, b.pengirim, b.telp_penerima, b.asal_paket "
				+ ", b.kode_perwakilan, b.tujuan, d.zona, b.penerima "
				+ ", c.bclose, c.bpclose, c.bvol, b.harga, b.total_biaya, a.resi_jne, b.reseller, a.id_kardus "
				+ "from tt_header a, tt_data_entry b, tt_poto_timbang c, tr_harga d, tr_pelanggan e "
				+ "where "
			    +  "a.awb_header = b.awb_data_entry "
			    +  "and a.awb_header = c.awb_poto_timbang " 
			    +  "and b.tujuan = d.kode_zona "
			    +  "and b.pengirim = e.nama_akun ";
		if(Flag){
			nativeSql += "and a.flag = 0 and date(a.tgl_create) between :pTglMulai and :pTglAkhir";
		}else{
			nativeSql += "and a.flag = '1' and date(a.tgl_create) between :pTglMulai and :pTglAkhir";
		}
				
		System.out.println("--> " + nativeSql);
		Query query = session.createSQLQuery(nativeSql).setParameter("pTglMulai", TglMulai)
				.setParameter("pTglAkhir", TglAkhir);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List result = query.list();
		session.getTransaction().commit();
		
		List<BrowseSemuaDataVO> returnList = new ArrayList<BrowseSemuaDataVO>();	
		for (Object obj : result) {
			java.util.Map row = (java.util.Map) obj;
			BrowseSemuaDataVO everyRow = new BrowseSemuaDataVO(nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql, nativeSql);
			
			//get data resi
			everyRow.setAwbData((String) row.get("AWB_DATA")!=null?(String) row.get("AWB_DATA"):"");
			
			//get data tanggal created
			Date created = (Date) row.get("TGL_CREATE");
			
			everyRow.setCreated(DateUtil.dateToStdDateLiteral(created));
			
			//get data layanan
			everyRow.setLayanan((String) row.get("LAYANAN")!=null?(String) row.get("LAYANAN"):"");

			//get data pengirim
			everyRow.setPengirim((String) row.get("PENGIRIM")!=null?(String) row.get("PENGIRIM"):"");

			//get data telp
			everyRow.setTelp((String) row.get("TELP_PENERIMA")!=null?(String) row.get("TELP_PENERIMA"):"");
			
			//get data asal_paket
			everyRow.setAsalPaket((String) row.get("ASAL_PAKET")!=null?(String) row.get("ASAL_PAKET"):"");
			
			//get data kode_perwakilan
			everyRow.setKodePerwakilan((String) row.get("KODE_PERWAKILAN")!=null?(String) row.get("KODE_PERWAKILAN"):"");
			
			//get data tujuan
			everyRow.setTujuan((String) row.get("TUJUAN")!=null?(String) row.get("TUJUAN"):"");
			
			//get data zona
			everyRow.setZona((String) row.get("ZONA")!=null?(String) row.get("ZONA"):"");
			
			//get data penerima
			everyRow.setPenerima((String) row.get("PENERIMA")!=null?(String) row.get("PENERIMA"):"");
			
			//get data bfinal
			everyRow.setBFinal((String) row.get("BCLOSE")!=null?(String) row.get("BCLOSE"):"");
			
			//get data bpfinal
			everyRow.setBpFinal((String) row.get("BPCLOSE")!=null?(String) row.get("BPCLOSE"):"");
			
			//get data bvolume
			everyRow.setBVolume((String) row.get("BVOL")!=null?(String) row.get("BVOL"):"");
			
			//get data harga
			everyRow.setHarga((String) row.get("HARGA").toString());
			
			//get data total_biaya
			everyRow.setTotalBiaya((String) row.get("TOTAL_BIAYA").toString());
			
			//get data resi_jne
			everyRow.setResiJNE((String) row.get("RESI_JNE")!=null?(String) row.get("RESI_JNE"):"");
			
			//get data reseller
			everyRow.setReseller((String) row.get("RESELLER")!=null?(String) row.get("RESELLER"):"");
			
			everyRow.setIdKardus((String) row.get("ID_KARDUS")!=null?(String) row.get("ID_KARDUS"):"");
			
			returnList.add(everyRow);
		}
		return returnList;
		
	}
	
	//OK
	public static Boolean showTableSetelahDelete(String awb) {
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE tt_header a "
				+ "SET flag = :pSampah  "
				+ "WHERE a.awb_header =:pAWB";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pSampah", 1)
				.setParameter("pAWB", awb);
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
	
	//Chris
	public static Boolean deleteAwb(String awb, Boolean restore) {
		Session sess = HibernateUtil.openSession();
		TtHeader hh = new TtHeader();
		TtPotoTimbang pt = new TtPotoTimbang();
		TtDataEntry de = new TtDataEntry();
		
		Criteria cHeader = sess.createCriteria(TtHeader.class);
		cHeader.add(Restrictions.eq("awbHeader", awb));
		List<TtHeader> dataHeader = cHeader.list();
		hh = dataHeader.get(0);
		
		Criteria cDataEntry = sess.createCriteria(TtDataEntry.class);
		cDataEntry.add(Restrictions.eq("awbDataEntry", awb));
		List<TtDataEntry> dataDataEntry = cDataEntry.list();
		de = dataDataEntry.get(0);
		
		Criteria cPotoTimbang = sess.createCriteria(TtPotoTimbang.class);
		cPotoTimbang.add(Restrictions.eq("awbPotoTimbang", awb));
		List<TtPotoTimbang> dataPotoTimbang = cPotoTimbang.list();
		pt = dataPotoTimbang.get(0);
		
		hh.setFlag(restore?0:1);		
		pt.setFlag(restore?0:1);		
		de.setFlag(restore?0:1);
		
		sess.update(hh);
		sess.update(pt);
		sess.update(de);
		
		sess.getTransaction().commit();
		return true;
	}
	
	//New
	public static Boolean updateResiJne(String awb, String ResiJne) {
		System.out.println("RESI JNE : "+ResiJne);
		System.out.println("AWB : "+awb);
		Session sess = HibernateUtil.openSession();
		String sql = "UPDATE tt_header a "
				+ "SET resi_jne =:pResiJne  "
				+ "WHERE a.awb_header =:pAWB";
		Query queryUpdate = sess.createSQLQuery(sql)
				.setParameter("pResiJne", ResiJne)
				.setParameter("pAWB", awb);
		int result = queryUpdate.executeUpdate();
		queryUpdate.executeUpdate();
		sess.getTransaction().commit();
		return true;
	}
	// Chris Nambah
	public static void updateResiPengirim(String awb, String kodePelanggan, String noPickup) {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtDataEntry.class);
		c.add(Restrictions.eq("awbDataEntry", awb));
		List<TtDataEntry> data = c.list();
				
		TtDataEntry de = data.get(0);
		System.out.println("--> data : " + data.size());
		de.setPengirim(kodePelanggan);
		
		Criteria c1 = s.createCriteria(TtPotoTimbang.class);
		c1.add(Restrictions.eq("awbPotoTimbang", awb));
		List<TtPotoTimbang> data1 = c1.list();
		
		TtPotoTimbang pt = data1.get(0);
		pt.setKodePelanggan(kodePelanggan);
		pt.setKodePickup(noPickup);
		System.out.println("--> data1 : " + data1.size());
		
		s.update(de);
		s.update(pt);
		
		s.getTransaction().commit();
	}
}