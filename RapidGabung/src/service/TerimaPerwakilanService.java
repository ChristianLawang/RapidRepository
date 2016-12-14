package service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;

import VO.EntryDataShowVO;
import entity.TrPickup;
import javafx.scene.control.TreeItem;
import util.DateUtil;
import util.HibernateUtil;

public class TerimaPerwakilanService {
		public static List<EntryDataShowVO> getListDatPaket(String idKardus) {
			Session s = HibernateUtil.openSession();
			String sql = 
//					"select b.kode_perwakilan, b.awb_data_entry, b.tujuan, a.inbound_flag "
//					+ "from tt_header a, tt_data_entry b "
//					+ "where a.awb_header = b.awb_data_entry "
//					+ "and a.id_kardus=:pIdKardus and a.flag=0 "
//					+ "order by b.awb_data_entry";
			
					"select "
					+ "c.id_kardus_sub, "
					+ "b.kode_perwakilan, "
					+ "a.awb_header, "
					+ "b.tujuan, "
					+ "a.inbound_flag from tt_header a " + 
					"inner join tt_data_entry b on a.awb_header = b.awb_data_entry " + 
					"left join tt_gabung_paket c on a.awb_header = c.awb " + 
					"where a.id_kardus = :pIdKardus and a.flag=0";
			
			Query query = s.createSQLQuery(sql).setParameter("pIdKardus", idKardus);
			List<EntryDataShowVO> returnList = new ArrayList<EntryDataShowVO>();
			List<Object[]> list = query.list();
			for (Object[] objects : list) {
				EntryDataShowVO en = new EntryDataShowVO();
				en.setIdKardusSub(objects[0] != null ? (String) objects[0] : "");
				en.setKdPerwakilan(objects[1] != null ? (String) objects[1] : "");
				en.setAwbData(objects[2] != null ? (String) objects[2] : "");
				en.setTujuan(objects[3] != null ? (String) objects[3] : "");
				en.setInboundFlag(objects[4] != null ? (Integer) objects[4] : 0);
				returnList.add(en);
			}
			s.getTransaction().commit();
			return returnList;
		}
		
		public static Boolean updateTerimaPerwakilan(Boolean isCheck, LocalDateTime localDateTime, String user, String awb) {
			Instant instans = localDateTime.toInstant(ZoneOffset.UTC);
			Date date = Date.from(instans);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, localDateTime.getHour());
			cal.set(Calendar.MINUTE, localDateTime.getMinute());
			
			Session sess = HibernateUtil.openSession();
				String sql = "UPDATE tt_header set inbound_flag=:pIFlag, wk_inbound=:pWkInbound, user_inbound=:pUser WHERE awb_header=:pAwb";
				if(isCheck){
					Query query = sess.createSQLQuery(sql)
							.setParameter("pIFlag", 1)
							.setParameter("pWkInbound", cal.getTime())
							.setParameter("pUser", user)
							.setParameter("pAwb", awb);
					query.executeUpdate();
				}else{
					Query query = sess.createSQLQuery(sql)
							.setParameter("pIFlag", null)
							.setParameter("pWkInbound", null)
							.setParameter("pUser", null)
							.setParameter("pAwb", awb);
					query.executeUpdate();
				}
			sess.getTransaction().commit();
			return true;
		}
		
		public static Boolean updateTerimaPerwakilanBermasalah(LocalDateTime localDateTime, String user, String awb) {
			Instant instans = localDateTime.toInstant(ZoneOffset.UTC);
			Date date = Date.from(instans);
			Session sess = HibernateUtil.openSession();
				String sql = "UPDATE tt_header set inbound_flag=0, wk_inbound=:pWkInbound, user_inbound=:pUser WHERE awb_header=:pAwb";
				Query query = sess.createSQLQuery(sql)
						.setParameter("pWkInbound", date)
						.setParameter("pUser", user)
						.setParameter("pAwb", awb);
				query.executeUpdate();
			sess.getTransaction().commit();
			return true;
		}
		public static Integer cekFlag(String awb) {
			Session sess = HibernateUtil.openSession();
				String sql = "select a.inbound_flag from tt_header a where a.awb_header=:pAwb";
				Query query = sess.createSQLQuery(sql)
						.setParameter("pAwb", awb);
				Integer flag = (Integer) query.uniqueResult() !=null ?  (Integer) query.uniqueResult() :0;
			return flag;
		}

		public static List<String> getTreeViewParent(LocalDateTime localDateTime) {
			Instant instans = localDateTime.toInstant(ZoneOffset.UTC);
			String date = DateUtil.getDateRipTimeForSQL(Date.from(instans));
			
			Session session = HibernateUtil.openSession();
			String sql =
					"select distinct id_kardus from tt_gabung_paket where date(tgl_create) between '"+date+" 00:00:00' and '"+date+" 23:23:59'";			
			SQLQuery query = session.createSQLQuery(sql);
			query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			List result = query.list();
			session.getTransaction().commit();

			List<String> returnList = new ArrayList<String>();
			for (Object obj : result) {
				Map row = (Map) obj;
				String str = new String((String) row.get("ID_KARDUS"));
				returnList.add(str);
			}
			
			return returnList;
		}

		public static List<String> getTreeViewSub(String idKardus) {
			Session session = HibernateUtil.openSession();
			String sql =
					"select distinct id_kardus_sub from tt_gabung_paket where id_kardus = '"+idKardus+"'";			
			SQLQuery query = session.createSQLQuery(sql);
			query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			List result = query.list();
			session.getTransaction().commit();

			List<String> returnList = new ArrayList<String>();
			for (Object obj : result) {
				Map row = (Map) obj;
				String str = new String((String) row.get("ID_KARDUS_SUB"));
				returnList.add(str);
			}
			
			return returnList;
		}

		public static List<String> getTreeViewDetail(String idSub) {
			Session session = HibernateUtil.openSession();
			String sql =
					"select awb from tt_gabung_paket where id_kardus = '"+idSub+"'";			
			SQLQuery query = session.createSQLQuery(sql);
			query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			List result = query.list();
			session.getTransaction().commit();

			List<String> returnList = new ArrayList<String>();
			for (Object obj : result) {
				Map row = (Map) obj;
				String str = new String((String) row.get("AWB"));
				returnList.add(str);
			}
			
			return returnList;
		}

}
