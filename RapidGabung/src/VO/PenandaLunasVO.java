package VO;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PenandaLunasVO {
	public IntegerProperty no;
	public StringProperty created;
	public StringProperty kdPickup;
	public StringProperty kdPelanggan;
	public IntegerProperty jumlahBarang;
	public IntegerProperty totalBerat;
	public StringProperty totalTagihan;
	private BooleanProperty checked;
	
 public PenandaLunasVO(Integer no, String created, String kdPickup, 
		 String kdPelanggan, Integer jumlahBarang, Integer totalBerat, String totalTagihan,  boolean checked){
	 this.no = new SimpleIntegerProperty(no);
	 this.created = new SimpleStringProperty(created);
	 this.kdPickup = new SimpleStringProperty(kdPickup);
	 this.kdPelanggan = new SimpleStringProperty(kdPelanggan);
	 this.jumlahBarang = new SimpleIntegerProperty(jumlahBarang);
	 this.totalBerat = new SimpleIntegerProperty(totalBerat);
	 this.totalTagihan = new SimpleStringProperty(totalTagihan);
	 this.checked = new SimpleBooleanProperty(checked);
 }
 
 	public boolean getChecked() {
		return checked.get();
	}
 
 	public void setChecked(boolean checked) {
		this.checked.set(checked);
	}
 
 	public BooleanProperty checkedProperty() {
		return checked;
	}

	public StringProperty totalTagihanProperty() {
		return totalTagihan;
	}
	
	public IntegerProperty totalBeratProperty() {
		return totalBerat;
	}
	
	public IntegerProperty jumlahBarangProperty() {
		return jumlahBarang;
	}
	
	public StringProperty kdPelangganProperty() {
		return kdPelanggan;
	}
	
	public StringProperty kdPickupProperty() {
		return kdPickup;
	}
	
	public IntegerProperty noProperty() {
		return no;
	}
	
	public StringProperty createdProperty() {
		return created;
	}
 
 	public String getTotalTagihan() {
		return totalTagihan.get();
	}

	public void setTotalTagihan(String totalTagihan) {
		this.totalTagihan.set(totalTagihan);
	}
	
	public Integer getTotalBerat() {
		return totalBerat.get();
	}

	public void setTotalBerat(Integer totalBerat) {
		this.totalBerat.set(totalBerat);
	}
 
 	public Integer getJumlahBarang() {
		return jumlahBarang.get();
	}

	public void setJumlahBarang(Integer jumlahBarang) {
		this.jumlahBarang.set(jumlahBarang);
	}
	
	public Integer getNo() {
		return no.get();
	}

	public void setNo(Integer no) {
		this.no.set(no);
	}
	
	public String getCreated() {
		return created.get();
	}

	public void setCreated(String created) {
		this.created.set(created);
	}
	
	public String getKdPickup() {
		return kdPickup.get();
	}

	public void setKdPickup(String kdPickup) {
		this.kdPickup.set(kdPickup);
	}
	
	public String getKdPelanggan() {
		return kdPelanggan.get();
	}

	public void setKdPelanggan(String kdPelanggan) {
		this.kdPelanggan.set(kdPelanggan);
	}
	
}
