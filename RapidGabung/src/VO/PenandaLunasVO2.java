package VO;

import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PenandaLunasVO2 {

	//property
	public BooleanProperty checked;
	public StringProperty tanggal;
	public StringProperty nmPelanggan;
	public StringProperty noPickUp;
	public IntegerProperty totalPiutang;
	public StringProperty tglBayar;
	public StringProperty bank;	
	
	public PenandaLunasVO2(String tanggal, String nmPelanggan, String noPickUp, Integer totalPiutang){	
		this.tanggal = new SimpleStringProperty(tanggal);
		this.nmPelanggan = new SimpleStringProperty(nmPelanggan);
		this.noPickUp = new SimpleStringProperty(noPickUp);
		this.totalPiutang = new SimpleIntegerProperty(totalPiutang);		
	}

	public PenandaLunasVO2(Boolean checked, String tanggal, String nmPelanggan, String noPickUp, Integer totalPiutang,
			String tglBayar, String bank) {
		this.checked = new SimpleBooleanProperty(checked);
		this.tanggal = new SimpleStringProperty(tanggal);
		this.nmPelanggan = new SimpleStringProperty(nmPelanggan);
		this.noPickUp = new SimpleStringProperty(noPickUp);
		this.totalPiutang = new SimpleIntegerProperty(totalPiutang);
		this.tglBayar = new SimpleStringProperty(tglBayar);
		this.bank = new SimpleStringProperty(bank);
	}

	public BooleanProperty getChecked() {
		return checked;
	}

	public void setChecked(BooleanProperty checked) {
		this.checked = checked;
	}

	public StringProperty getTanggal() {
		return tanggal;
	}

	public void setTanggal(StringProperty tanggal) {
		this.tanggal = tanggal;
	}

	public StringProperty getNmPelanggan() {
		return nmPelanggan;
	}

	public void setNmPelanggan(StringProperty nmPelanggan) {
		this.nmPelanggan = nmPelanggan;
	}

	public StringProperty getNoPickUp() {
		return noPickUp;
	}

	public void setNoPickUp(StringProperty noPickUp) {
		this.noPickUp = noPickUp;
	}

	public IntegerProperty getTotalPiutang() {
		return totalPiutang;
	}

	public void setTotalPiutang(IntegerProperty totalPiutang) {
		this.totalPiutang = totalPiutang;
	}

	public StringProperty getTglBayar() {
		return tglBayar;
	}

	public void setTglBayar(StringProperty tglBayar) {
		this.tglBayar = tglBayar;
	}

	public StringProperty getBank() {
		return bank;
	}

	public void setBank(StringProperty bank) {
		this.bank = bank;
	}

}
