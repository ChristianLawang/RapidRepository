package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.Timestamp;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.ochafik.swing.syntaxcoloring.InputHandler.next_char;

import entity.TtHeader;
import util.HibernateUtil;

public class DataPotoService {

	public static void save(TtHeader dPoto) {
		GenericService.save(TtHeader.class, dPoto, true);
	}

	// chris
	public static TtHeader getDataPotoByPotoPaketID(Integer idPotoPaket) {
		Session s = HibernateUtil.openSession();
		Criteria c = s.createCriteria(TtHeader.class);
		c.add(Restrictions.eq("awbHeader", idPotoPaket));
		List<TtHeader> data = c.list();
		s.getTransaction().commit();
		return data.get(0);
	}

	// chriss Update Rivan
	public static List<TtHeader> getDataPotoForCabang(Date dtPeriode) {
		Session s = HibernateUtil.openSession();

		Criteria c = s.createCriteria(TtHeader.class);
		c.add(Restrictions.gt("tglCreate", new Timestamp(dtPeriode.getTime())));
		List<TtHeader> data = c.list();
		s.getTransaction().commit();
		List<TtHeader> dataUpdate = new ArrayList<TtHeader>();
		int no=1;
		for (TtHeader t : data) {
			TtHeader th = new TtHeader();
			Integer flag;

			th.setAwbHeader(t.getAwbHeader());
			th.setIdKeranjang(t.getIdKeranjang());
			th.setIdUpload(t.getIdUpload());
			th.setWkSortir(t.getWkSortir());
			th.setSortirFlag(t.getSortirFlag());
			System.out.println("------------------------------>>> Awb  : " + t.getAwbHeader()+ " / "+t.getGabungPaketFlag());
			if (t.getIdKardus() != null) {
				if (t.getIdKardus().substring(0, 3).equals("RPD")) {
					System.out.println("-----------------Awb Masuk IF");
					t.setGabungPaketFlag(0);
				}
			}
			
			System.out.println("-------------------Fleg : "+t.getGabungPaketFlag());
			System.out.println("-------------------No : "+no++);
			th.setGabungPaketFlag(t.getGabungPaketFlag());
			th.setWkGabungPaket(t.getWkGabungPaket());
			th.setIdKardus(t.getIdKardus());
			th.setInboundFlag(t.getInboundFlag());
			th.setWkInbound(th.getWkInbound());
			th.setBarangTerkirimFlag(t.getBarangTerkirimFlag());
			th.setIdKurir(t.getIdKurir());
			th.setSubmitFlag(t.getSubmitFlag());
			th.setWaitingPendingFlag(t.getWaitingPendingFlag());
			th.setUserCreate(t.getUserCreate());
			th.setUserUpdate(t.getUserUpdate());
			th.setResiJne(t.getResiJne());
			th.setTglCreate(t.getTglCreate());
			th.setTglUpdate(t.getTglUpdate());
			th.setFlag(t.getFlag());
			dataUpdate.add(th);
		}
		return dataUpdate;
	}

	// chriss Update Rivan
	public static void saveAll(List<TtHeader> header) {
		Session s = HibernateUtil.openSession();
		for (TtHeader data : header) {
			Criteria c = s.createCriteria(TtHeader.class);
			c.add(Restrictions.eq("awbHeader", data.getAwbHeader()));
			List<TtHeader> listDataFoto = c.list();
			if (listDataFoto.isEmpty()) {
				System.out.println("---------------MELAKUKAN SAVE DATA FOTO");
				s.save(data);
			}
		}
		s.getTransaction().commit();
	}
}
