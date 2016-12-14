package VO;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class MasterZonaVO {
	public final StringProperty kdZona;
	public final StringProperty propinsi;
	public final StringProperty kabupaten;
	public final StringProperty kecamatan;
	public final StringProperty perwakilan;
	public final StringProperty zona;
	public final StringProperty reg;
	public final StringProperty one;
	
	
	public MasterZonaVO(String kdZona, String propinsi, String kabupaten, String kecamatan, String perwakilan, String zona, String reg, String one) {
		this.kdZona = new SimpleStringProperty(kdZona);
		this.propinsi = new SimpleStringProperty(propinsi);
		this.kabupaten = new SimpleStringProperty(kabupaten);
		this.kecamatan = new SimpleStringProperty(kecamatan);
		this.perwakilan = new SimpleStringProperty(perwakilan);
		this.zona = new SimpleStringProperty(zona);
		this.reg = new SimpleStringProperty(reg);
		this.one = new SimpleStringProperty(one);
	}
	
	public String getKdZona() {
		return kdZona.get();
	}
	
	public void setKdZona(String kdZona) {
		this.kdZona.set(kdZona);
	}

	public String getPropinsi() {
		return propinsi.get();
	}
	
	public void setPropinsi(String propinsi) {
		this.propinsi.set(propinsi);
	}
		
	public String getKabupaten() {
		return kabupaten.get();
	}
	
	public void setKabupaten(String kabupaten) {
		this.kabupaten.set(kabupaten);
	}

	public String getKecamatan() {
		return kecamatan.get();
	}
	
	public void setKecamatan(String kecamatan) {
		this.kecamatan.set(kecamatan);
	}

	public String getPerwakilan() {
		return perwakilan.get();
	}
	
	public void setPerwakilan(String perwakilan) {
		this.perwakilan.set(perwakilan);
	}

	public String getZona() {
		return zona.get();
	}
	
	public void setZona(String zona) {
		this.zona.set(zona);
	}

	public String getReg() {
		return reg.get();
	}
	
	public void setReg(String reg) {
		this.reg.set(reg);
	}

	public String getOne() {
		return one.get();
	}
	
	public void setOne(String one) {
		this.one.set(one);
	}

	

	
	public StringProperty kdZonaProperty() {
		return kdZona;
	}
	public StringProperty propinsiProperty() {
		return propinsi;
	}
	public StringProperty kabupatenProperty() {
		return kabupaten;
	}
	public StringProperty kecamatanProperty() {
		return kecamatan;
	}
	public StringProperty perwakilanProperty() {
		return perwakilan;
	}
	public StringProperty zonaProperty(){
		return zona;
	}
	public StringProperty regProperty(){
		return reg;
	}
	public StringProperty oneProperty(){
		return one;
	}
}
