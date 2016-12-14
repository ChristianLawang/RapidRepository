package VO;

import java.util.Date;

public class MapingJneVO {
	Date dpAwalJne, dpAkhirJne, dpAwalRapid, dpAkhirRapid;
	String penerima, pengirim, tujuan;
	Boolean data;
	
	
	public Boolean getData() {
		return data;
	}
	public void setData(Boolean data) {
		this.data = data;
	}
	public Date getDpAwalJne() {
		return dpAwalJne;
	}
	public void setDpAwalJne(Date dpAwalJne) {
		this.dpAwalJne = dpAwalJne;
	}
	public Date getDpAkhirJne() {
		return dpAkhirJne;
	}
	public void setDpAkhirJne(Date dpAkhirJne) {
		this.dpAkhirJne = dpAkhirJne;
	}
	public Date getDpAwalRapid() {
		return dpAwalRapid;
	}
	public void setDpAwalRapid(Date dpAwalRapid) {
		this.dpAwalRapid = dpAwalRapid;
	}
	public Date getDpAkhirRapid() {
		return dpAkhirRapid;
	}
	public void setDpAkhirRapid(Date dpAkhirRapid) {
		this.dpAkhirRapid = dpAkhirRapid;
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
	public String getTujuan() {
		return tujuan;
	}
	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}
	
}
