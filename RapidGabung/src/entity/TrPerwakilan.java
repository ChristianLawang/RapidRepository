package entity;

// default package
// Generated Jun 22, 2016 9:21:28 PM by Hibernate Tools 5.1.0.Alpha1

import java.util.Date;

/**
 * TrPerwakilan generated by hbm2java
 */
public class TrPerwakilan implements java.io.Serializable {

	private String kodeZona;
	private String propinsi;
	private String kabupaten;
	private String kecamatan;
	private String kodePerwakilan;
	private String zona;
	private String regperwakilan;
	private String oneperwakilan;
	private Date tglCreate;
	private Date tglUpdate;
	private Integer flag;

	public TrPerwakilan() {
	}

	public TrPerwakilan(String kodeZona) {
		this.kodeZona = kodeZona;
	}

	public TrPerwakilan(String kodeZona, String propinsi, String kabupaten, String kecamatan, String kodePerwakilan,
			String zona, String regperwakilan, String oneperwakilan, Date tglCreate, Date tglUpdate, Integer flag) {
		this.kodeZona = kodeZona;
		this.propinsi = propinsi;
		this.kabupaten = kabupaten;
		this.kecamatan = kecamatan;
		this.kodePerwakilan = kodePerwakilan;
		this.zona = zona;
		this.regperwakilan = regperwakilan;
		this.oneperwakilan = oneperwakilan;
		this.tglCreate = tglCreate;
		this.tglUpdate = tglUpdate;
		this.flag = flag;
	}

	public String getKodeZona() {
		return this.kodeZona;
	}

	public void setKodeZona(String kodeZona) {
		this.kodeZona = kodeZona;
	}

	public String getPropinsi() {
		return this.propinsi;
	}

	public void setPropinsi(String propinsi) {
		this.propinsi = propinsi;
	}

	public String getKabupaten() {
		return this.kabupaten;
	}

	public void setKabupaten(String kabupaten) {
		this.kabupaten = kabupaten;
	}

	public String getKecamatan() {
		return this.kecamatan;
	}

	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}

	public String getKodePerwakilan() {
		return this.kodePerwakilan;
	}

	public void setKodePerwakilan(String kodePerwakilan) {
		this.kodePerwakilan = kodePerwakilan;
	}

	public String getZona() {
		return this.zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getRegperwakilan() {
		return this.regperwakilan;
	}

	public void setRegperwakilan(String regperwakilan) {
		this.regperwakilan = regperwakilan;
	}

	public String getOneperwakilan() {
		return this.oneperwakilan;
	}

	public void setOneperwakilan(String oneperwakilan) {
		this.oneperwakilan = oneperwakilan;
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
