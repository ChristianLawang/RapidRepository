package VO;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LapPODVO {
	public  IntegerProperty no;
	public  StringProperty perwakilan;
	public  StringProperty awbData;
	public  IntegerProperty jumlahPaket;
	public  IntegerProperty sudahReport;
	public  IntegerProperty belumReport;
	public  IntegerProperty masalah;
	public  IntegerProperty stockCabang;
	
	public LapPODVO(Integer no, String Perwakilan, Integer jumlahPaket, 
			Integer sudahReport, Integer belumReport, Integer masalah, Integer stockCabang) {
		this.no = new SimpleIntegerProperty(no);
		this.perwakilan = new SimpleStringProperty(Perwakilan);
		this.jumlahPaket = new SimpleIntegerProperty(jumlahPaket);
		this.sudahReport = new SimpleIntegerProperty(sudahReport);
		this.belumReport = new SimpleIntegerProperty(belumReport);
		this.masalah = new SimpleIntegerProperty(masalah);
		this.stockCabang = new SimpleIntegerProperty(stockCabang);
	}
	
	public LapPODVO(Integer no, String Perwakilan, String awbData) {
		this.no = new SimpleIntegerProperty(no);
		this.perwakilan = new SimpleStringProperty(Perwakilan);
		this.awbData = new SimpleStringProperty(awbData);
	}
	public String getAwbData() {
		return awbData.get();
	}

	public void setAwbData(String awbData) {
		this.awbData.set(awbData);
	}
	
	public IntegerProperty noProperty() {
		return no;
	}
	
	public IntegerProperty jumlahPaketProperty() {
		return jumlahPaket;
	}
	
	public StringProperty awbDataProperty() {
		return awbData;
	}
	
	public StringProperty perwakilanProperty() {
		return perwakilan;
	}
	
	public IntegerProperty sudahReportProperty() {
		return sudahReport;
	}
	
	public IntegerProperty belumReportProperty() {
		return belumReport;
	}
	
	public IntegerProperty masalahProperty() {
		return masalah;
	}
	
	public IntegerProperty stockCabangProperty() {
		return stockCabang;
	}
	
	public Integer getStockCabang() {
		return stockCabang.get();
	}

	public void setStockCabang(Integer stockCabang) {
		this.stockCabang.set(stockCabang);
	}
	
	public Integer getMasalah() {
		return masalah.get();
	}

	public void setMasalah(Integer masalah) {
		this.masalah.set(masalah);
	}
	
	public Integer getBelumReport() {
		return belumReport.get();
	}

	public void setBelumReport(Integer belumReport) {
		this.belumReport.set(belumReport);
	}
	
	public Integer getSudahReport() {
		return sudahReport.get();
	}

	public void setSudahReport(Integer sudahReport) {
		this.sudahReport.set(sudahReport);
	}
	
	public Integer getJumlahPaket() {
		return jumlahPaket.get();
	}

	public void setJumlahPaket(Integer jumlahPaket) {
		this.jumlahPaket.set(jumlahPaket);
	}
	
	public String getPerwakilan() {
		return perwakilan.get();
	}

	public void setPerwakilan(String perwakilan) {
		this.perwakilan.set(perwakilan);
	}
	
	public Integer getNo() {
		return no.get();
	}

	public void setNo(Integer no) {
		this.no.set(no);
	}
}
