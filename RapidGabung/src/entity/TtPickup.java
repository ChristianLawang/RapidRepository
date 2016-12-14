package entity;

import java.util.Date;

public class TtPickup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String kodePelanggan;
	private String kodeHari;
	private String jamPickup;
	private Date tglCreate;
	private Date tglUpdate;
	private String flag;
	
	public TtPickup(){
		
	}
	public TtPickup(String id){
		this.id = id;
	}
	public TtPickup(
			String id, 
			String kodePelanggan, 
			String kodeHari, 
			String jamPickup, 
			Date tglCreate, 
			Date tglUpdate, 
			String flag){
		this.id = id;
		this.kodePelanggan = kodePelanggan;
		this.kodeHari = kodeHari;
		this.jamPickup = jamPickup;
		this.tglCreate = tglCreate;
		this.tglUpdate = tglUpdate;
		this.flag = flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
