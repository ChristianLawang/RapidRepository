package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import entity.TrJabatan;
import entity.TtDataEntry;
import entity.TtGabungPaket;
import entity.TtHeader;
import entity.TtPotoTimbang;
import entity.TtStatusKurirIn;
import util.HibernateUtil;

public class AWBHistoryService {
	
	public TtHeader getHeaderByAwb(String awb){
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtHeader.class);
		c.add(Restrictions.eq("awbHeader", awb));
		List<TtHeader> data = c.list();
		s.getTransaction().commit();
		if(data.size()>0){
			return data.get(0);
		}else{
			return null;
		}
	}
	
	public TtPotoTimbang getPotoTimbangByAwb(String awb){
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtPotoTimbang.class);
		c.add(Restrictions.eq("awbPotoTimbang", awb));
		List<TtPotoTimbang> data = c.list();
		s.getTransaction().commit();
		if(data.size()>0){
			return data.get(0);
		}else{
			return null;
		}
	}
	
	public TtDataEntry getDataEntryByAwb(String awb){
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtDataEntry.class);
		c.add(Restrictions.eq("awbDataEntry", awb));
		List<TtDataEntry> data = c.list();
		s.getTransaction().commit();
		if(data.size()>0){
			return data.get(0);
		}else{
			return null;
		}
	}
	
	public TtGabungPaket getGabungPaketByAwb(String awb){
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtGabungPaket.class);
		c.add(Restrictions.eq("awb", awb));
		List<TtGabungPaket> data = c.list();
		s.getTransaction().commit();
		if(data.size()>0){
			return data.get(0);
		}else{
			return null;
		}
	}

	public List<TtStatusKurirIn> getStatusKurirInByAWB(String awb) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtStatusKurirIn.class);
		c.add(Restrictions.eq("idBarang", awb));
		List<TtStatusKurirIn> data = c.list();
		s.getTransaction().commit();
		return data;
	}

	public List<Map<String, Object>> getStatusKurirByAWB(String awb) {
		
		Session session=HibernateUtil.openSession();
		String nativeSql = 

				"select DT.TANGGAL, DT.NAMA_KURIR, DT.PENERIMA, DT.MASALAH from ( " +
				"	    select  " +
				"	           c.tgl_create TANGGAL, " +
				"	           d.nama NAMA_KURIR, " +
				"	           IF(c.masalah!= '-1 - Diterima', '', c.penerima) PENERIMA, " +
				"	           c.masalah MASALAH " +
				"	    from  " +
				"	           tt_status_kurir_out c, " + 
				"	           tr_kurir d " +
				"	    where  " +
				"	          c.id_barang = '"+awb+"' " +
				"	          and c.id_kurir = d.nik " +
				"	    UNION " +
				"	    select  " +
				"	           a.tgl_create TANGGAL, " + 
				"	           b.nama NAMA_KURIR,  " +
				"	           IF(a.masalah!= '-1 - Diterima', '', a.penerima) PENERIMA, " + 
				"	           a.masalah MASALAH " +
				"	    from  " +
				"	           tt_status_kurir_in a, " + 
				"	           tr_kurir b  " +
				"	    where a.id_barang = '"+awb+"' " + 
				"	          and a.id_kurir = b.nik " +
				"	    ) DT " +
				"	order by 1";
				
		SQLQuery  query = session.createSQLQuery(nativeSql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> result = query.list();
		session.getTransaction().commit();
				
		return result;
	}
}
