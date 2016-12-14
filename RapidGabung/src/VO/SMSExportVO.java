package VO;

import java.util.Date;

public class SMSExportVO {

	String telpPenerima;
	String penerima;
	String pengirim;
	Date tanggal;
	String AWB;
	String web;
	public String getTelpPenerima() {
		return telpPenerima;
	}
	public void setTelpPenerima(String telpPenerima) {
		this.telpPenerima = telpPenerima;
	}
	public String getPenerima() {
		return penerima;
	}
	public void setPenerima(String penerima) {
		this.penerima = penerima;
	}
	public String getPengirim() {
		return pengirim;
	}
	public void setPengirim(String pengirim) {
		this.pengirim = pengirim;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	public String getAWB() {
		return AWB;
	}
	public void setAWB(String aWB) {
		AWB = aWB;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	
	
	
}
