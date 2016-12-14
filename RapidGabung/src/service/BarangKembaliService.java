package service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import entity.TrPelanggan;
import entity.TtStatusResiBermasalah;
import javafx.beans.property.StringProperty;
import util.HibernateUtil;

public class BarangKembaliService {

	public List<Object[]> getResiBermasalah(String dtAwal, String dtAkhir) {
		Session s = HibernateUtil.openSession();
		String sql = 
				"select a.id_barang, b.penerima, a.masalah from tt_status_resi_bermasalah a, tt_data_entry b where a.id_barang = b.awb_data_entry and a.flag=0 and date(a.tgl_create) between '"+dtAwal+"' and '"+dtAkhir+"'";
		
		System.out.println(sql);
		Query query = s.createSQLQuery(sql);

		List<Object[]> list = query.list();
		s.getTransaction().commit();
		
		return list;
	}	
}
