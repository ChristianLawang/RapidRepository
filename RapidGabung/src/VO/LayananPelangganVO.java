package VO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LayananPelangganVO {

	// property
	public StringProperty tanggal;
	public StringProperty noAwb;
	public StringProperty perwakilan;
	public StringProperty statusLaporan;
	public StringProperty namaKurir;
	public StringProperty pelanggaranKurir;
	public StringProperty tglSelesai;
	public StringProperty noLaporan;

	public LayananPelangganVO(String tanggal, String noAwb, String perwakilan, String statusLaporan, String namaKurir,
			String pelanggaranKurir, String tglSelesai, String noLaporan) {
		this.tanggal = new SimpleStringProperty(tanggal);
		this.noAwb = new SimpleStringProperty(noAwb);
		this.perwakilan = new SimpleStringProperty(perwakilan);
		this.statusLaporan = new SimpleStringProperty(statusLaporan);
		this.namaKurir = new SimpleStringProperty(namaKurir);
		this.pelanggaranKurir = new SimpleStringProperty(pelanggaranKurir);
		this.tglSelesai = new SimpleStringProperty(tglSelesai);
		this.noLaporan = new SimpleStringProperty(noLaporan);
	}

	public StringProperty getPelanggaranKurir() {
		return pelanggaranKurir;
	}

	public void setPelanggaranKurir(StringProperty pelanggaranKurir) {
		this.pelanggaranKurir = pelanggaranKurir;
	}

	public StringProperty getNoLaporan() {
		return noLaporan;
	}

	public void setNoLaporan(StringProperty noLaporan) {
		this.noLaporan = noLaporan;
	}

	public StringProperty getTanggal() {
		return tanggal;
	}

	public void setTanggal(StringProperty tanggal) {
		this.tanggal = tanggal;
	}

	public StringProperty getNoAwb() {
		return noAwb;
	}

	public void setNoAwb(StringProperty noAwb) {
		this.noAwb = noAwb;
	}

	public StringProperty getPerwakilan() {
		return perwakilan;
	}

	public void setPerwakilan(StringProperty perwakilan) {
		this.perwakilan = perwakilan;
	}

	public StringProperty getStatusLaporan() {
		return statusLaporan;
	}

	public void setStatusLaporan(StringProperty statusLaporan) {
		this.statusLaporan = statusLaporan;
	}

	public StringProperty getNamaKurir() {
		return namaKurir;
	}

	public void setNamaKurir(StringProperty namaKurir) {
		this.namaKurir = namaKurir;
	}

	public StringProperty getPelangganKurir() {
		return pelanggaranKurir;
	}

	public void setPelangganKurir(StringProperty pelangganKurir) {
		this.pelanggaranKurir = pelangganKurir;
	}

	public StringProperty getTglSelesai() {
		return tglSelesai;
	}

	public void setTglSelesai(StringProperty tglSelesai) {
		this.tglSelesai = tglSelesai;
	}

}
