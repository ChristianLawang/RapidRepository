package VO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TagihanVO {

	String awb;
	String pengirim; 
	String tujuan;
	String penerima; 
	String telpPenerima; 
	String resiJNE;
	String pbFinal; 
	Integer harga;
	Integer asuransi;
	Integer diskon;
	Integer diskonRapid;
	Integer diskonJNE; 
	BigDecimal diskonPelanggan;
	BigDecimal totalBiaya;
	BigInteger jumlahPaket;
	String kodePickup;
	Date created;

	public TagihanVO(
			String awb,
			String pengirim, 
			String tujuan,
			String penerima, 
			String telpPenerima, 
			String resiJNE,
			String pbFinal, 
			Integer harga,
			Integer asuransi,
			Integer diskon,
			Integer diskonRapid,
			Integer diskonJNE,
			BigDecimal diskonPelanggan,
			BigDecimal totalBiaya,
			BigInteger jumlahPaket,
			String kodePickup,
			Date created
			) {

		this.awb = awb;
		this.pengirim = pengirim;
		this.tujuan = tujuan;
		this.penerima = penerima;
		this.telpPenerima = telpPenerima;
		this.resiJNE = resiJNE;
		this.pbFinal = pbFinal; 
		this.harga = harga;
		this.asuransi = asuransi;
		this.diskon = diskon;
		this.diskonRapid = diskonRapid;
		this.diskonJNE = diskonJNE;
		this.diskonPelanggan = diskonPelanggan;
		this.totalBiaya = totalBiaya;
		this.jumlahPaket = jumlahPaket;
		this.kodePickup = kodePickup;
		this.created = created;
		
	}

	public String getAwb() {
		return awb;
	}

	public void setAwb(String awb) {
		this.awb = awb;
	}

	public String getPengirim() {
		return pengirim;
	}

	public void setPengirim(String pengirim) {
		this.pengirim = pengirim;
	}

	public String getTujuan() {
		return tujuan;
	}

	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}

	public String getPenerima() {
		return penerima;
	}

	public void setPenerima(String penerima) {
		this.penerima = penerima;
	}

	public String getTelpPenerima() {
		return telpPenerima;
	}

	public void setTelpPenerima(String telpPenerima) {
		this.telpPenerima = telpPenerima;
	}

	public String getResiJNE() {
		return resiJNE;
	}

	public void setResiJNE(String resiJNE) {
		this.resiJNE = resiJNE;
	}

	public String getPbFinal() {
		return pbFinal;
	}

	public void setPbFinal(String pbFinal) {
		this.pbFinal = pbFinal;
	}

	public Integer getHarga() {
		return harga;
	}

	public void setHarga(Integer harga) {
		this.harga = harga;
	}

	public Integer getAsuransi() {
		return asuransi;
	}

	public void setAsuransi(Integer asuransi) {
		this.asuransi = asuransi;
	}

	public Integer getDiskon() {
		return diskon;
	}

	public void setDiskon(Integer diskon) {
		this.diskon = diskon;
	}
	
	public Integer getDiskonRapid() {
		return diskonRapid;
	}

	public void setDiskonRapid(Integer diskonRapid) {
		this.diskonRapid = diskonRapid;
	}
	
	public Integer getDiskonJNE() {
		return diskonJNE;
	}

	public void setDiskonJNE(Integer diskonJNE) {
		this.diskonJNE = diskonJNE;
	}

	public BigDecimal getDiskonPelanggan() {
		return diskonPelanggan;
	}

	public void setDiskonPelanggan(BigDecimal diskonPelanggan) {
		this.diskonPelanggan = diskonPelanggan;
	}
	
	public BigDecimal getTotalBiaya() {
		return totalBiaya;
	}

	public void setTotalBiaya(BigDecimal totalBiaya) {
		this.totalBiaya = totalBiaya;
	}
	
	public BigInteger getJumlahPaket() {
		return jumlahPaket;
	}

	public void setJumlahPaket(BigInteger jumlahPaket) {
		this.jumlahPaket = jumlahPaket;
	}
	
	public String getKodePickup() {
		return kodePickup;
	}

	public void setJumlahPaket(String kodePickup) {
		this.kodePickup = kodePickup;
	}
	
	public Date getCreated(){
		return this.created;
	}
	
	public void setCreated(Date created){
		this.created = created;
	}
	
	public void print(){
		System.out.println("-- TagihanVO --");
		System.out.println("awb : " + awb);
		System.out.println("pengirim : " + pengirim);
		System.out.println("tujuan : " + tujuan);
		System.out.println("penerima : " + penerima);
		System.out.println("telpPenerima : " + telpPenerima);
		System.out.println("resiJNE : " + resiJNE);
		System.out.println("pbFinal : " + pbFinal);
		System.out.println("harga : " + harga);
		System.out.println("asuransi : " + asuransi);
		System.out.println("diskon : " + diskon);
		System.out.println("diskonRapid : " + diskonRapid);
		System.out.println("diskonJNE : " + diskonJNE);
		System.out.println("diskonPelanggan : " + diskonPelanggan);
		System.out.println("totalBiaya : " + totalBiaya);
		System.out.println("jumlahPaket : " + jumlahPaket);
		System.out.println("kodePickup : " + kodePickup);
		System.out.println("created : " + created);
	}
}
