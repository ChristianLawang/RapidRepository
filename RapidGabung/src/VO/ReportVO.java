package VO;

import java.math.BigDecimal;
import java.math.BigInteger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReportVO {
	public  StringProperty awbData;
	public  StringProperty pengirim;
	public  StringProperty telp;
	public  StringProperty tujuan;
	public  StringProperty penerima;
	public  StringProperty bFinal;
	public  StringProperty resiJNE;
	public  IntegerProperty asuransi;
	public  IntegerProperty diskon;
	public  IntegerProperty diskonJne;
	public  IntegerProperty diskonRapid;
	public  IntegerProperty diskonPelanggan;
	public  StringProperty harga;
	public  IntegerProperty no;
	public  StringProperty biaya;
	public StringProperty kdPerwakilan;
	public IntegerProperty jumlahBarang;
	public IntegerProperty sumBerat;
	public  StringProperty nmSales;
	public  StringProperty sumBeratAsli;
	public  StringProperty hargaSetelahDiskon;
	public  StringProperty totalDiskon;
	public  StringProperty resiKardusTerakhir;
	

	public ReportVO(Integer no, String awbData, String pengirim, String tujuan, String penerima, String telp, String resiJNE,
			String bFinal, String harga, Integer asuransi, Integer diskon, Integer diskonJne, Integer diskonRapid,
			Integer diskonPelanggan, String biaya) {
		this.no = new SimpleIntegerProperty(no);
		this.awbData = new SimpleStringProperty(awbData);
		this.pengirim = new SimpleStringProperty(pengirim);
		this.tujuan = new SimpleStringProperty(tujuan);
		this.penerima = new SimpleStringProperty(penerima);
		this.telp = new SimpleStringProperty(telp);
		this.resiJNE = new SimpleStringProperty(resiJNE);
		this.bFinal = new SimpleStringProperty(bFinal);
		this.harga = new SimpleStringProperty(harga);
		this.asuransi = new SimpleIntegerProperty(asuransi);
		this.diskon = new SimpleIntegerProperty(diskon);
		this.diskonJne = new SimpleIntegerProperty(diskonJne);
		this.diskonRapid = new SimpleIntegerProperty(diskonRapid);
		this.diskonPelanggan = new SimpleIntegerProperty(diskonPelanggan);
		this.biaya = new SimpleStringProperty(biaya);
	}
	
	public ReportVO(Integer no, String nmSales, String pengirim, Integer jumlahBarang, Integer sumBerat, String sumBeratAsli
			,String biaya, String hargaSetelahDiskon, String totalDiskon){
		this.no = new SimpleIntegerProperty(no);
		this.nmSales = new SimpleStringProperty(nmSales);
		this.pengirim = new SimpleStringProperty(pengirim);
		this.jumlahBarang = new SimpleIntegerProperty(jumlahBarang);//awb
		this.sumBerat = new SimpleIntegerProperty(sumBerat);
		this.sumBeratAsli = new SimpleStringProperty(sumBeratAsli);
		this.biaya = new SimpleStringProperty(biaya);
		this.hargaSetelahDiskon = new SimpleStringProperty(hargaSetelahDiskon);
		this.totalDiskon = new SimpleStringProperty(totalDiskon);
	}
	
	public ReportVO(Integer no, String kdPerwakilan, Integer jumlahBarang, Integer sumBerat, String biaya, String resiKardusTerakhir){
		this.no = new SimpleIntegerProperty(no);
		this.kdPerwakilan= new SimpleStringProperty(kdPerwakilan);
		this.jumlahBarang = new SimpleIntegerProperty(jumlahBarang);
		this.sumBerat = new SimpleIntegerProperty(sumBerat);
		this.biaya = new SimpleStringProperty(biaya);
		this.resiKardusTerakhir= new SimpleStringProperty(resiKardusTerakhir);
	}
	
	
	public String getTelp(){
		return this.telp.get();
	}
	public void setTelp(String telp){
		this.telp.set(telp);
	}
	
	public String getResiKardusTerakhir() {
		return resiKardusTerakhir.get();
	}

	public void setResiKardusTerakhir(String resiKardusTerakhir) {
		this.resiKardusTerakhir.set(resiKardusTerakhir);
	}
	
	public String getTotalDiskonDiskon() {
		return totalDiskon.get();
	}

	public void setTotaDiskon(String totalDiskon) {
		this.totalDiskon.set(totalDiskon);
	}
	
	public String getHargaSetelahDiskon() {
		return hargaSetelahDiskon.get();
	}

	public void setHargaSetelahDiskon(String hargaSetelahDiskon) {
		this.hargaSetelahDiskon.set(hargaSetelahDiskon);
	}
	
	public String getSumBeratAsli() {
		return sumBeratAsli.get();
	}

	public void setSumBeratAsli(String sumBeratAsli) {
		this.sumBeratAsli.set(sumBeratAsli);
	}
	
	public String getNmSales() {
		return nmSales.get();
	}

	public void setNmSales(String nmSales) {
		this.nmSales.set(nmSales);
	}
	
	
	public String getKdPerwakilan() {
		return kdPerwakilan.get();
	}

	public void setKdPerwakilan(String kdPerwakilan) {
		this.kdPerwakilan.set(kdPerwakilan);
	}
	
	public Integer getJumlahBarang() {
		return jumlahBarang.get();
	}

	public void setJumlahBarang(Integer jumlahBarang) {
		this.jumlahBarang.set(jumlahBarang);
	}
	
	public Integer getSumBerat() {
		return sumBerat.get();
	}

	public void setSumBerat(Integer sumBerat) {
		this.sumBerat.set(sumBerat);
	}
	
	public Integer getNo() {
		return no.get();
	}

	public void setNo(Integer no) {
		this.no.set(no);
	}
	
	public String getHarga() {
		return harga.get();
	}

	public void setHarga(String harga) {
		this.harga.set(harga);
	}

	public String getAwbData() {
		return awbData.get();
	}

	public void setAwbData(String awbData) {
		this.awbData.set(awbData);
	}

	public String getPengirim() {
		return pengirim.get();
	}

	public void setPengirim(String pengirim) {
		this.pengirim.set(pengirim);
	}

	public String getTujuan() {
		return tujuan.get();
	}

	public void setTujuan(String tujuan) {
		this.tujuan.set(tujuan);
	}

	public String getPenerima() {
		return penerima.get();
	}

	public void setPenerima(String penerima) {
		this.penerima.set(penerima);
	}

	public String getBfinal() {
		return bFinal.get();
	}

	public void setBfinal(String bFinal) {
		this.bFinal.set(bFinal);
	}

	public String getBiaya() {
		return biaya.get();
	}

	public void setBiaya(String biaya) {
		this.biaya.set(biaya);
	}

	public String getResiJNE() {
		return resiJNE.get();
	}

	public void setResiJNE(String resiJNE) {
		this.resiJNE.set(resiJNE);
	}

	public Integer getAsuransi() {
		return asuransi.get();
	}

	public void setAsuransi(Integer asuransi) {
		this.asuransi.set(asuransi);
	}

	public Integer getDiskon() {
		return diskon.get();
	}

	public void setDiskon(Integer diskon) {
		this.diskon.set(diskon);
	}

	public Integer getDiskonJne() {
		return diskonJne.get();
	}

	public void setDiskonJne(Integer diskonJne) {
		this.diskonJne.set(diskonJne);
	}

	public Integer getDiskonRapid() {
		return diskonJne.get();
	}

	public void setDiskonRapid(Integer diskonRapid) {
		this.diskonRapid.set(diskonRapid);
	}

	public Integer getDiskonPelanggan() {
		return diskonPelanggan.get();
	}

	public void setDiskonPelanggan(Integer diskonPelanggan) {
		this.diskonPelanggan.set(diskonPelanggan);
	}


	public StringProperty awbDataProperty() {
		return awbData;
	}

	public StringProperty pengirimProperty() {
		return pengirim;
	}

	public StringProperty tujuanProperty() {
		return tujuan;
	}

	public StringProperty bFinalProperty() {
		return bFinal;
	}

	public StringProperty biayaProperty() {
		return biaya;
	}

	public StringProperty resiJNEProperty() {
		return resiJNE;
	}

	public IntegerProperty asuransiProperty() {
		return asuransi;
	}

	public IntegerProperty diskonProperty() {
		return diskon;
	}

	public IntegerProperty disonRapidProperty() {
		return diskonRapid;
	}

	public IntegerProperty diskonPelangganProperty() {
		return diskonPelanggan;
	}


	public StringProperty penerimaProperty() {
		return penerima;
	}

	public StringProperty tlpProperty() {
		return telp;
	}

	public StringProperty hargaProperty() {
		return harga;
	}
	
	public IntegerProperty noProperty() {
		return no;
	}
	
	public IntegerProperty jumlahBarangProperty() {
		return jumlahBarang;
	}
	
	public IntegerProperty sumBeratProperty() {
		return sumBerat;
	}
	
	public StringProperty kdPerwakilanProperty() {
		return kdPerwakilan;
	}
	
	public StringProperty nmSalesProperty() {
		return nmSales;
	}
	
	public StringProperty sumBeratAsliProperty() {
		return sumBeratAsli;
	}
	
	public StringProperty hargaSetelahDiskonProperty() {
		return hargaSetelahDiskon;
	}
	public StringProperty totalDiskonproperty() {
		return totalDiskon;
	}
	
	public StringProperty resiKardusTerakhir() {
		return resiKardusTerakhir;
	}

}
