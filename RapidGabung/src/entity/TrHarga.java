package entity;

// default package
// Generated Jun 22, 2016 9:21:28 PM by Hibernate Tools 5.1.0.Alpha1

import java.util.Date;

/**
 * TrHarga generated by hbm2java
 */
public class TrHarga implements java.io.Serializable {
	
	private TrHargaPK pk;
//	private String kodeZona;
	private String propinsi;
//	private String kodeAsal;
	private String kabupaten;
	private String kecamatan;
	private String kodePerwakilan;
	private String zona;
	private Integer reg;
	private String etd;
	private Integer one;
	private Date tglCreate;
	private Date tglUpdate;
	private Integer flag;

	public TrHarga() {
		this.pk = new TrHargaPK("","");
	}

//	public TrHarga(String kodeZona) {
//		this.kodeZona = kodeZona;
//	}

	public TrHarga(TrHargaPK pk, String propinsi, String kabupaten, String kecamatan,
			String kodePerwakilan, String zona, Integer reg, String etd, Integer one, Date tglCreate, Date tglUpdate,
			Integer flag) {
		this.pk = pk;
//		this.kodeZona = kodeZona;
		this.propinsi = propinsi;
//		this.kodeAsal = kodeAsal;
		this.kabupaten = kabupaten;
		this.kecamatan = kecamatan;
		this.kodePerwakilan = kodePerwakilan;
		this.zona = zona;
		this.reg = reg;
		this.etd = etd;
		this.one = one;
		this.tglCreate = tglCreate;
		this.tglUpdate = tglUpdate;
		this.flag = flag;
	}

//	public String getKodeZona() {
//		return this.kodeZona;
//	}
//
//	public void setKodeZona(String kodeZona) {
//		this.kodeZona = kodeZona;
//	}

	public void setPk(TrHargaPK pk){
		this.pk = pk;
	}
	
	public TrHargaPK getPk(){
		return this.pk;
	}
	
	public String getPropinsi() {
		return this.propinsi;
	}

	public void setPropinsi(String propinsi) {
		this.propinsi = propinsi;
	}

//	public String getKodeAsal() {
//		return this.kodeAsal;
//	}
//
//	public void setKodeAsal(String kodeAsal) {
//		this.kodeAsal = kodeAsal;
//	}

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

	public Integer getReg() {
		return this.reg;
	}

	public void setReg(Integer reg) {
		this.reg = reg;
	}

	public String getEtd() {
		return this.etd;
	}

	public void setEtd(String etd) {
		this.etd = etd;
	}

	public Integer getOne() {
		return this.one;
	}

	public void setOne(Integer one) {
		this.one = one;
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
		
	public void print(){
		System.out.println("--------");
//		System.out.println(kodeZona);
		System.out.println(propinsi);
//		System.out.println(kodeAsal);
		System.out.println(kabupaten);
		System.out.println(kecamatan);
		System.out.println(kodePerwakilan);
		System.out.println(zona);
		System.out.println(reg);
		System.out.println(etd);
		System.out.println(one);
		System.out.println(tglCreate);
		System.out.println(tglUpdate);
		System.out.println(flag);
	}

}
