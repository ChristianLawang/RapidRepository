package entity;

import java.util.Date;

public class TtJadwalPickup {
	private Integer id;
	private String kodePelanggan;
	private String kodeHari;
	private String jamPickup;
	private String nikKurir;
	private String assignBy;
	private Integer sentMail;
	private Date tglCreate;
	private Date tglUpdate;
	private String flag;
	private Integer posisi;
	private String idPickup;
	private Integer flagJalan;
	private String sourcePickup;
	
	public String getSourcePickup() {
		return sourcePickup;
	}
	public void setSourcePickup(String sourcePickup) {
		this.sourcePickup = sourcePickup;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKodePelanggan() {
		return kodePelanggan;
	}
	public void setKodePelanggan(String kodePelanggan) {
		this.kodePelanggan = kodePelanggan;
	}
	public String getKodeHari() {
		return kodeHari;
	}
	public void setKodeHari(String kodeHari) {
		this.kodeHari = kodeHari;
	}
	public String getJamPickup() {
		return jamPickup;
	}
	public void setJamPickup(String jamPickup) {
		this.jamPickup = jamPickup;
	}
	public String getNikKurir() {
		return nikKurir;
	}
	public void setNikKurir(String nikKurir) {
		this.nikKurir = nikKurir;
	}
	public String getAssignBy() {
		return assignBy;
	}
	public void setAssignBy(String assignBy) {
		this.assignBy = assignBy;
	}
	public Integer getSentMail() {
		return sentMail;
	}
	public void setSentMail(Integer sentMail) {
		this.sentMail = sentMail;
	}
	public Date getTglCreate() {
		return tglCreate;
	}
	public void setTglCreate(Date tglCreate) {
		this.tglCreate = tglCreate;
	}
	public Date getTglUpdate() {
		return tglUpdate;
	}
	public void setTglUpdate(Date tglUpdate) {
		this.tglUpdate = tglUpdate;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public String getIdPickup() {
		return idPickup;
	}
	public void setIdPickup(String idPickup) {
		this.idPickup = idPickup;
	}
	public Integer getFlagJalan() {
		return flagJalan;
	}
	public void setFlagJalan(Integer flagJalan) {
		this.flagJalan = flagJalan;
	}
}
