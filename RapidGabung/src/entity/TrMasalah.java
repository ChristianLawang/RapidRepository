package entity;

// default package
// Generated Jun 22, 2016 9:21:28 PM by Hibernate Tools 5.1.0.Alpha1

import java.util.Date;

/**
 * TrMasalah generated by hbm2java
 */
public class TrMasalah implements java.io.Serializable {

	private int idMasalah;
	private String namaMasalah;
	private Date tglCreate;
	private Date tglUpdate;
	private Integer flag;

	public TrMasalah() {
	}

	public TrMasalah(int idMasalah) {
		this.idMasalah = idMasalah;
	}

	public TrMasalah(int idMasalah, String namaMasalah, Date tglCreate, Date tglUpdate, Integer flag) {
		this.idMasalah = idMasalah;
		this.namaMasalah = namaMasalah;
		this.tglCreate = tglCreate;
		this.tglUpdate = tglUpdate;
		this.flag = flag;
	}

	public int getIdMasalah() {
		return this.idMasalah;
	}

	public void setIdMasalah(int idMasalah) {
		this.idMasalah = idMasalah;
	}

	public String getNamaMasalah() {
		return this.namaMasalah;
	}

	public void setNamaMasalah(String namaMasalah) {
		this.namaMasalah = namaMasalah;
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

}
