package VO;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TerimaPerwakilanVO {
	public IntegerProperty no;
	public StringProperty kdPerwakilan;
	public StringProperty kdKardusSub;
	public StringProperty awb;
	public StringProperty tujuan;
	private BooleanProperty checked;
	
	public TerimaPerwakilanVO(
			Integer no, 
			String kdPerwakilan,
			String kdKardusSub,
			String awb, 
			String tujuan, 
			boolean checked){
		 this.no = new SimpleIntegerProperty(no);
		 this.kdPerwakilan = new SimpleStringProperty(kdPerwakilan);
		 this.kdKardusSub = new SimpleStringProperty(kdKardusSub);
		 this.awb = new SimpleStringProperty(awb);
		 this.tujuan = new SimpleStringProperty(tujuan);
		 this.checked = new SimpleBooleanProperty(checked);
	 }
	public TerimaPerwakilanVO() {
		// TODO Auto-generated constructor stub
	}
	public IntegerProperty noProperty() {
		return no;
	}
	
	public StringProperty kdPerwakilanProperty() {
		return kdPerwakilan;
	}
	
	public StringProperty kdKardusSubProperty(){
		return kdKardusSub;
	}
	
	public StringProperty awbProperty() {
		return awb;
	}
	
	public StringProperty tujuanProperty() {
		return tujuan;
	}
	
	public String getTujuan() {
		return tujuan.get();
	}

	public void setTujuan(String tujuan) {
		this.tujuan.set(tujuan);
	}
	
	public String getAwb() {
		return awb.get();
	}

	public void setAwb(String awb) {
		this.awb.set(awb);
	}
	
	public String getKdPerwakilan() {
		return kdPerwakilan.get();
	}

	public void setKdPerwakilan(String kdPerwakilan) {
		this.kdPerwakilan.set(kdPerwakilan);
	}
	public String getKdKardusSub() {
		return kdKardusSub.get();
	}

	public void setKdKardusSub(String kdKardusSub) {
		this.kdKardusSub.set(kdKardusSub);
	}
	
	public Integer getNo() {
		return no.get();
	}

	public void setNo(Integer no) {
		this.no.set(no);
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
 	
 	
 	
 	
	public void setNo(IntegerProperty no) {
		this.no = no;
	}
	public void setKdPerwakilan(StringProperty kdPerwakilan) {
		this.kdPerwakilan = kdPerwakilan;
	}
	public void setAwb(StringProperty awb) {
		this.awb = awb;
	}
	public void setTujuan(StringProperty tujuan) {
		this.tujuan = tujuan;
	}
	public void setChecked(BooleanProperty checked) {
		this.checked = checked;
	}
 	
 	
 	
 	

}
