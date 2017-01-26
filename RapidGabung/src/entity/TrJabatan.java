package entity;

// default package
// Generated Jun 22, 2016 9:21:28 PM by Hibernate Tools 5.1.0.Alpha1

import java.util.Date;

/**
 * TrJabatan generated by hbm2java
 */
public class TrJabatan implements java.io.Serializable {

	private String idJabatan;
	private String jabatan;
	private String kodeCabang;
	private Date tglCreate;
	private Date tglUpdate;
	private Integer flag;
	private Integer user;

	public TrJabatan() {
	}

	public TrJabatan(String idJabatan) {
		this.idJabatan = idJabatan;
	}

	public TrJabatan(String idJabatan, String jabatan, String kodeCabang, Date tglCreate, Date tglUpdate,
			Integer flag, Integer user) {
		this.idJabatan = idJabatan;
		this.jabatan = jabatan;
		this.kodeCabang = kodeCabang;
		this.tglCreate = tglCreate;
		this.tglUpdate = tglUpdate;
		this.flag = flag;
		this.user = user;
	}

	public String getIdJabatan() {
		return this.idJabatan;
	}

	public void setIdJabatan(String idJabatan) {
		this.idJabatan = idJabatan;
	}

	public String getJabatan() {
		return this.jabatan;
	}

	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}

	public String getKodeCabang() {
		return this.kodeCabang;
	}

	public void setKodeCabang(String kodeCabang) {
		this.kodeCabang = kodeCabang;
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
	
	public Integer getUser() {
		return this.user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

}
