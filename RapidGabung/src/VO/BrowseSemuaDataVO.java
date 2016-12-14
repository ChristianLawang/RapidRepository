package VO;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class BrowseSemuaDataVO {
	public final StringProperty awbData;
	public final StringProperty created;
	public final StringProperty layanan;
	public final StringProperty pengirim;
	public final StringProperty telp;
	public final StringProperty asalPaket;
	public final StringProperty kdPerwakilan;
	public final StringProperty tujuan;
	public final StringProperty zona;
	public final StringProperty penerima;
	public final StringProperty bFinal;
	public final StringProperty bpFinal;
	public final StringProperty bVolume;
	public final StringProperty harga;
	public final StringProperty totalBiaya;
	public final StringProperty resiJNE;
	public final StringProperty reseller;
	public final StringProperty idKardus;

	public BrowseSemuaDataVO(String awbData, String created, String layanan, String pengirim, String telp,
			String asalPaket, String kdPerwakilan, String tujuan, String zona, String penerima, String bFinal,
			String bpFinal, String bVolume, String harga, String totalBiaya, String resiJNE, String reseller, String idKardus) {
		this.awbData = new SimpleStringProperty(awbData);
		this.created = new SimpleStringProperty(created);
		this.layanan = new SimpleStringProperty(layanan);
		this.pengirim = new SimpleStringProperty(pengirim);
		this.telp = new SimpleStringProperty(telp);
		this.asalPaket = new SimpleStringProperty(asalPaket);
		this.kdPerwakilan = new SimpleStringProperty(kdPerwakilan);
		this.tujuan = new SimpleStringProperty(tujuan);
		this.zona = new SimpleStringProperty(zona);
		this.penerima = new SimpleStringProperty(penerima);
		this.bFinal = new SimpleStringProperty(bFinal);
		this.bpFinal = new SimpleStringProperty(bpFinal);
		this.bVolume = new SimpleStringProperty(bVolume);
		this.harga = new SimpleStringProperty(harga);
		this.totalBiaya = new SimpleStringProperty(totalBiaya);
		this.resiJNE = new SimpleStringProperty(resiJNE);
		this.reseller = new SimpleStringProperty(reseller);
		this.idKardus = new SimpleStringProperty(idKardus);
	}


	// awb
	public String getIdKardus() {
		return idKardus.get();
	}

	public void setIdKardus(String idKardus) {
		this.idKardus.set(idKardus);
	}
	
	public String getAwbData() {
		return awbData.get();
	}

	public void setAwbData(String awbData) {
		this.awbData.set(awbData);
	}

	// awb
	public String getCreated() {
		return created.get();
	}

	public void setCreated(String created) {
		this.created.set(created);
	}

	// layanan
	public String getLayanan() {
		return layanan.get();
	}

	public void setLayanan(String layanan) {
		this.layanan.set(layanan);
	}

	// pengirim
	public String getPengirim() {
		return pengirim.get();
	}

	public void setPengirim(String pengirim) {
		this.pengirim.set(pengirim);
	}

	// telp
	public String getTelp() {
		return telp.get();
	}

	public void setTelp(String telp) {
		this.telp.set(telp);
	}

	// asal paket
	public String getAsalPaket() {
		return asalPaket.get();
	}

	public void setAsalPaket(String asalPaket) {
		this.asalPaket.set(asalPaket);
	}

	// kode perwakilan
	public String getKodePerwakilan() {
		return kdPerwakilan.get();
	}

	public void setKodePerwakilan(String kdPerwakilan) {
		this.kdPerwakilan.set(kdPerwakilan);
	}

	// tujuan
	public String getTujuan() {
		return tujuan.get();
	}

	public void setTujuan(String tujuan) {
		this.tujuan.set(tujuan);
	}

	// zona
	public String getZona() {
		return zona.get();
	}

	public void setZona(String zona) {
		this.zona.set(zona);
	}

	// penerima
	public String getPenerima() {
		return penerima.get();
	}

	public void setPenerima(String penerima) {
		this.penerima.set(penerima);
	}

	// bfinal
	public String getBFinal() {
		return bFinal.get();
	}

	public void setBFinal(String bFinal) {
		this.bFinal.set(bFinal);
	}

	// bpFinal
	public String getBpFinal() {
		return bpFinal.get();
	}

	public void setBpFinal(String bpFinal) {
		this.bpFinal.set(bpFinal);
	}

	// bVolume
	public String getBVolume() {
		return bVolume.get();
	}

	public void setBVolume(String bVolume) {
		this.bVolume.set(bVolume);
	}

	// harga
	public String getHarga() {
		return harga.get();
	}

	public void setHarga(String harga) {
		this.harga.set(harga);
	}

	// totalBiaya
	public String getTotalBiaya() {
		return totalBiaya.get();
	}

	public void setTotalBiaya(String totalBiaya) {
		this.totalBiaya.set(totalBiaya);
	}

	// resi jne
	public String getResiJNE() {
		return resiJNE.get();
	}

	public void setResiJNE(String resiJNE) {
		this.resiJNE.set(resiJNE);
	}

	// reseller
	public String getReseller() {
		return reseller.get();
	}

	public void setReseller(String reseller) {
		this.reseller.set(reseller);
	}
	
	

	public StringProperty awbDataProperty() {
		return awbData;
	}
	
	public StringProperty createdProperty() {
		return created;
	}

	public StringProperty layananProperty() {
		return layanan;
	}

	public StringProperty pengirimProperty() {
		return pengirim;
	}

	public StringProperty telpProperty() {
		return telp;
	}

	public StringProperty asalPaketProperty() {
		return asalPaket;
	}

	public StringProperty kdPerwakilanProperty() {
		return kdPerwakilan;
	}

	public StringProperty tujuanProperty() {
		return tujuan;
	}

	public StringProperty zonaProperty() {
		return zona;
	}

	public StringProperty penerimaProperty() {
		return penerima;
	}

	public StringProperty bFinalProperty() {
		return bFinal;
	}

	public StringProperty bpFinalProperty() {
		return bpFinal;
	}

	public StringProperty bVolumeProperty() {
		return bVolume;
	}

	public StringProperty hargaProperty() {
		return harga;
	}

	public StringProperty totalBiayaProperty() {
		return totalBiaya;
	}

	public StringProperty resiJNEProperty() {
		return resiJNE;
	}

	public StringProperty resellerProperty() {
		return reseller;
	}
	
	public StringProperty idKardusProperty() {
		return idKardus;
	}

}
