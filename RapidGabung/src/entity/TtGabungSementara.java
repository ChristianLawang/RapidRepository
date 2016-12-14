package entity;

// default package
// Generated Jun 22, 2016 9:21:28 PM by Hibernate Tools 5.1.0.Alpha1

import java.util.Date;

/**
 * TtGabungSementaraId generated by hbm2java
 */
public class TtGabungSementara implements java.io.Serializable {

	private String id;
	private String idKardus;
	private String idKardusSub;
	private String awb;
	private Integer bclose;
	private Integer bpclose;
	private String tujuan;
	private String session;
	private Date tglCreate;
	private Date tglUpdate;
	private Integer flag;

	public TtGabungSementara() {
	}

	public TtGabungSementara(
			String id, 
			String idKardus, 
			String idKardusSub, 
			String awb, 
			Integer bclose, 
			Integer bpclose, 
			String tujuan,
			String session, 
			Date tglCreate, 
			Date tglUpdate, 
			Integer flag) {
		this.id = id;
		this.idKardus = idKardus;
		this.idKardusSub = idKardusSub;
		this.awb = awb;
		this.bclose = bclose;
		this.bpclose = bpclose;
		this.tujuan = tujuan;
		this.session = session;
		this.tglCreate = tglCreate;
		this.tglUpdate = tglUpdate;
		this.flag = flag;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getIdKardus() {
		return this.idKardus;
	}

	public void setIdKardus(String idKardus) {
		this.idKardus = idKardus;
	}

	public String getIdKardusSub() {
		return this.idKardusSub;
	}

	public void setIdKardusSub(String idKardusSub) {
		this.idKardusSub = idKardusSub;
	}

	public String getAwb() {
		return this.awb;
	}

	public void setAwb(String awb) {
		this.awb = awb;
	}

	public Integer getBclose() {
		return this.bclose;
	}

	public void setBclose(Integer bclose) {
		this.bclose = bclose;
	}

	public Integer getBpclose() {
		return this.bpclose;
	}

	public void setBpclose(Integer bpclose) {
		this.bpclose = bpclose;
	}

	public String getTujuan() {
		return this.tujuan;
	}

	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}

	public String getSession() {
		return this.session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public Date getTglCreate() {
		return this.tglCreate;
	}

	public void setTglCreate(Date tglCreate) {
		this.tglCreate = tglCreate;
	}

	public Date getTglUpdate() {
		return this.tglUpdate;
	}

	public void setTglUpdate(Date tglUpdate) {
		this.tglUpdate = tglUpdate;
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

		public int hashCode() {
		int result = 17;

		result = 37 * result + (getIdKardus() == null ? 0 : this.getIdKardus().hashCode());
		result = 37 * result + (getAwb() == null ? 0 : this.getAwb().hashCode());
		result = 37 * result + (getBclose() == null ? 0 : this.getBclose().hashCode());
		result = 37 * result + (getBpclose() == null ? 0 : this.getBpclose().hashCode());
		result = 37 * result + (getTujuan() == null ? 0 : this.getTujuan().hashCode());
		result = 37 * result + (getSession() == null ? 0 : this.getSession().hashCode());
		result = 37 * result + (getTglCreate() == null ? 0 : this.getTglCreate().hashCode());
		result = 37 * result + (getTglUpdate() == null ? 0 : this.getTglUpdate().hashCode());
		result = 37 * result + (getFlag() == null ? 0 : this.getFlag().hashCode());
		return result;
	}

}
