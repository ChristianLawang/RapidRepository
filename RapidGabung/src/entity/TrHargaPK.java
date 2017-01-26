package entity;

import java.io.Serializable;

public class TrHargaPK implements Serializable {
    private String kodeZona = "";
    private String kodeAsal = "";

    public TrHargaPK() {}

	public TrHargaPK(String kodeZona, String kodeAsal) {
	   this.kodeZona = kodeZona;
       this.kodeAsal = kodeAsal;
    }
	
	public void setKodeZona(String kodeZona){
		this.kodeZona = kodeZona;
	}
	
	public String getKodeZona(){
		return this.kodeZona;
	}
	
	public void setKodeAsal(String kodeAsal){
		this.kodeAsal = kodeAsal;
	}
	
	public String getKodeAsal(){
		return this.kodeAsal;
	}
}
