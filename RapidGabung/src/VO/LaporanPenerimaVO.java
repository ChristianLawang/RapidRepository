package VO;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LaporanPenerimaVO {
	public  IntegerProperty no;
	public  StringProperty awb;
	public  StringProperty tanggal;
	public  StringProperty penerima;
	public  StringProperty telpPenerima;
	public  StringProperty pengirim;
	public  StringProperty tujuan;
	public  StringProperty zona;
	public  StringProperty berat;
	public  StringProperty Etd;
	public  StringProperty tglPenerima;
	public  StringProperty waktuPenerima;
	public  StringProperty status;
	public  StringProperty penerimaPaket;
	public  StringProperty keterangan;
	
	public LaporanPenerimaVO(
			Integer no, 
			String tanggal, 
			String awb, 
			String penerima, 
			String telpPenerima,
			String pengirim, 
			String tujuan, 
			String zona, 
			String berat,
			String Etd, 
			String tglPenerima, 
			String waktuPenerima,
			String status, 
			String penerimaPaket, 
			String keterangan){
		
		this.no = new SimpleIntegerProperty(no);
		this.tanggal = new SimpleStringProperty(tanggal);
		this.awb = new SimpleStringProperty(awb);
		this.penerima = new SimpleStringProperty(penerima);
		this.telpPenerima = new SimpleStringProperty(telpPenerima);
		this.pengirim = new SimpleStringProperty(pengirim);
		this.tujuan = new SimpleStringProperty(tujuan);
		this.zona = new SimpleStringProperty(zona);
		this.berat = new SimpleStringProperty(berat);
		this.Etd = new SimpleStringProperty(Etd);
		this.tglPenerima = new SimpleStringProperty(tglPenerima);
		this.waktuPenerima = new SimpleStringProperty(waktuPenerima);
		this.status = new SimpleStringProperty(status);
		this.penerimaPaket = new SimpleStringProperty(penerimaPaket);
		this.keterangan = new SimpleStringProperty(keterangan);

	}
	
	public StringProperty penerimaPaketProperty() {
		return penerimaPaket;
	}
	
	public StringProperty tglPenerimaProperty() {
		return tglPenerima;
	}
	
	public StringProperty waktuPenerimaProperty(){
		return waktuPenerima;
	}
	
	public StringProperty telpPenerimaProperty() {
		return telpPenerima;
	}
	
	public StringProperty zonaProperty() {
		return zona;
	}
	
	public StringProperty beratProperty() {
		return berat;
	}
	
	public StringProperty EtdProperty() {
		return Etd;
	}
	
	
	public StringProperty tujuanProperty() {
		return tujuan;
	}
	
	public StringProperty pengirimProperty() {
		return pengirim;
	}
	
	public StringProperty penerimaProperty() {
		return penerima;
	}
	
	public StringProperty statusProperty(){
		return status;
	}
	
	public StringProperty tanggalProperty() {
		return tanggal;
	}
	
	public StringProperty awbProperty() {
		return awb;
	}
	
	public StringProperty keteranganProperty(){
		return keterangan;
	}
	
	public IntegerProperty noProperty() {
		return no;
	}
	
	public String getPenerimaPaket() {
		return penerimaPaket.get();
	}

	public void setPenerimaPaket(String penerimaPaket) {
		this.penerimaPaket.set(penerimaPaket);
	}
	
	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}
	public String getTelpPenerima() {
		return telpPenerima.get();
	}

	public void setTelpPenerima(String telpPenerima) {
		this.telpPenerima.set(telpPenerima);
	}
	public String getTglPenerima() {
		return tglPenerima.get();
	}

	public void setTglPenerima(String tglPenerima) {
		this.tglPenerima.set(tglPenerima);
	}
	public String getWaktuPenerima(){
		return waktuPenerima.get();
	}
	public void setWaktuPenerima(String waktuPenerima){
		this.waktuPenerima.set(waktuPenerima);
	}
	
	public String getZona() {
		return zona.get();
	}

	public void setZona(String zona) {
		this.zona.set(zona);
	}
	public String getBerat() {
		return berat.get();
	}

	public void setBerat(String berat) {
		this.berat.set(berat);
	}
	public String getTujuan() {
		return tujuan.get();
	}

	public void setTujuan(String tujuan) {
		this.tujuan.set(tujuan);
	}
	
	public String getPengirim() {
		return pengirim.get();
	}

	public void setPengirim(String pengirim) {
		this.pengirim.set(pengirim);
	}
	
	public String getPenerima() {
		return penerima.get();
	}

	public void setPenerima(String penerima) {
		this.penerima.set(penerima);
	}
	
	public String getTanggal() {
		return tanggal.get();
	}

	public void setTanggal(String tanggal) {
		this.tanggal.set(tanggal);
	}
	
	public String getAwb() {
		return awb.get();
	}

	public void setAwb(String awb) {
		this.awb.set(awb);
	}
	
	public String getKeterangan(){
		return keterangan.get();
	}
	
	public void setKeterangan(String keterangan){
		this.keterangan.set(keterangan);
	}
	public String getEtd() {
		return Etd.get();
	}

	public void setEtd(String Etd) {
		this.Etd.set(Etd);
	}
	
}
