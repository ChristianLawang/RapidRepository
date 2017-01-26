package VO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LaporanKurirVO {
	
	//Header
	private StringProperty date;
	private StringProperty totalDeliver;
	private StringProperty totalReceive;
	private StringProperty totalRemaining;
	private StringProperty totalPercentage;
	
	//Detail
	private StringProperty name;
	private StringProperty jumlahKirim;
	private StringProperty jumlahTerima;
	
	//Footer
	private StringProperty au;
	private StringProperty ba;
	private StringProperty coda;
	private StringProperty nth;
	private StringProperty cnee;
	private StringProperty miss;
	private StringProperty jne;
	private StringProperty total;
	
	//Header
	public LaporanKurirVO (String date, String totalDeliver, String totalReceive, String totalRemaining, String totalPercentage) {
		this.date = new SimpleStringProperty(date);
		this.totalDeliver = new SimpleStringProperty(totalDeliver);
		this.totalReceive = new SimpleStringProperty(totalReceive);
		this.totalRemaining = new SimpleStringProperty(totalRemaining);
		this.totalPercentage = new SimpleStringProperty(totalPercentage);
	}
	
	//Detail
//	public LaporanKurirVO (String name, String totalDeliver, String totalReceive, String totalRemaining, String jumlahKirim, String jumlahTerima, String totalPercentage) {
//		this.name = new SimpleStringProperty(name);
//		this.totalDeliver = new SimpleStringProperty(totalDeliver);
//		this.totalReceive = new SimpleStringProperty(totalReceive);
//		this.totalRemaining = new SimpleStringProperty(totalRemaining);
//		this.jumlahKirim = new SimpleStringProperty(jumlahKirim);
//		this.jumlahTerima = new SimpleStringProperty(jumlahTerima);
//		this.totalPercentage = new SimpleStringProperty(totalPercentage);
//	}
	
	public LaporanKurirVO (String name, String totalDeliver, String totalReceive, String totalRemaining, String jumlahTerima, String totalPercentage) {
		this.name = new SimpleStringProperty(name);
		this.totalDeliver = new SimpleStringProperty(totalDeliver);
		this.totalReceive = new SimpleStringProperty(totalReceive);
		this.totalRemaining = new SimpleStringProperty(totalRemaining);
		this.jumlahTerima = new SimpleStringProperty(jumlahTerima);
		this.totalPercentage = new SimpleStringProperty(totalPercentage);
	}
	
	public LaporanKurirVO (String name, String totalPercentage) {
		this.name = new SimpleStringProperty(name);
		this.totalPercentage = new SimpleStringProperty(totalPercentage);
	}
	
	//Footer
	public LaporanKurirVO (String au, String ba, String coda, String nth, String cnee, String miss, String jne, String total) {
		this.au = new SimpleStringProperty(au);
		this.ba = new SimpleStringProperty(ba);
		this.coda = new SimpleStringProperty(coda);
		this.nth = new SimpleStringProperty(nth);
		this.cnee = new SimpleStringProperty(cnee);
		this.miss = new SimpleStringProperty(miss);
		this.jne = new SimpleStringProperty(jne);
		this.total = new SimpleStringProperty(total);
	}
	
	public String getDate() {
		return date.get();
	}

	public void setDate(String date) {
		this.date.set(date);
	}

	public StringProperty getDateProperty() {
		return date;
	}
	
	public String getTotalDeliver() {
		return totalDeliver.get();
	}
	
	public void setTotalDeliver(String totalDeliver) {
		this.totalDeliver.set(totalDeliver);
	}

	public StringProperty getTotalDeliverProperty() {
		return totalDeliver;
	}
	
	public String getTotalReceive() {
		return totalReceive.get();
	}
	
	public void setTotalReceive(String totalReceive) {
		this.totalReceive.set(totalReceive);
	}
	
	public StringProperty getTotalReceiveProperty() {
		return totalReceive;
	}
	
	public String getTotalRemaining() {
		return totalRemaining.get();
	}
	
	public void setTotalRemaining(String totalRemaining) {
		this.totalRemaining.set(totalRemaining);
	}

	public StringProperty getTotalRemainingProperty() {
		return totalRemaining;
	}
	
	public String getTotalPercentage() {
		return totalPercentage.get();
	}
	
	public void setTotalPercentage(String totalPercentage) {
		this.totalPercentage.set(totalPercentage);
	}

	public StringProperty getTotalPercentageProperty() {
		return totalPercentage;
	}
	
	//Detail
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty getNameProperty() {
		return name;
	}
	
	public String getJumlahKirim() {
		return jumlahKirim.get();
	}
	
	public void setJumlahKirim(String jumlahKirim) {
		this.jumlahKirim.set(jumlahKirim);
	}

	public StringProperty getJumlahKirimProperty() {
		return jumlahKirim;
	}
	
	public String getJumlahTerima() {
		return jumlahTerima.get();
	}
	
	public void setJumlahTerima(String jumlahTerima) {
		this.jumlahTerima.set(jumlahTerima);
	}

	public StringProperty getJumlahTerimaProperty() {
		return jumlahTerima;
	}
	
	//Footer
	
	public String getAu() {
		return au.get();
	}
	
	public void setAu(String totalPercentage) {
		this.au.set(totalPercentage);
	}

	public StringProperty getAuProperty() {
		return au;
	}
	
	public String getBa() {
		return ba.get();
	}
	
	public void setBa(String ba) {
		this.ba.set(ba);
	}

	public StringProperty getBaProperty() {
		return ba;
	}
	
	public String getCoda() {
		return coda.get();
	}
	
	public void setCoda(String coda) {
		this.coda.set(coda);
	}

	public StringProperty getCodaProperty() {
		return coda;
	}
	
	public String getNth() {
		return nth.get();
	}
	
	public void setNth(String nth) {
		this.nth.set(nth);
	}

	public StringProperty getNthProperty() {
		return nth;
	}
	
	public String getCnee() {
		return cnee.get();
	}
	
	public void setCnee(String cnee) {
		this.cnee.set(cnee);
	}

	public StringProperty getCneeProperty() {
		return cnee;
	}
	
	public String getMiss() {
		return miss.get();
	}
	
	public void setMiss(String miss) {
		this.miss.set(miss);
	}

	public StringProperty getMissProperty() {
		return miss;
	}
	
	public String getJne() {
		return jne.get();
	}
	
	public void setJne(String jne) {
		this.jne.set(jne);
	}

	public StringProperty getJneProperty() {
		return jne;
	}
	
	public String getTotal() {
		return total.get();
	}
	
	public void setTotal(String total) {
		this.total.set(total);
	}

	public StringProperty getTotalProperty() {
		return total;
	}
}