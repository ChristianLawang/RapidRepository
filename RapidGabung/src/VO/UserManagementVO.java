package VO;

import javafx.beans.property.SimpleBooleanProperty;

public class UserManagementVO {
	private SimpleBooleanProperty checkedMnUser;
	private SimpleBooleanProperty checkedMnCabang;

	public UserManagementVO(boolean checkedMnUser, boolean checkedMnCabang) {
		this.checkedMnUser = new SimpleBooleanProperty(checkedMnUser);
		this.checkedMnCabang = new SimpleBooleanProperty(checkedMnCabang);
	}

	public boolean getCheckedMnUser() {
		return checkedMnUser.get();
	}
	
	public void setCheckedMnUser(boolean checkedMnUser) {
		this.checkedMnUser.set(checkedMnUser);
	}
	
	public boolean getCheckedMnCabang() {
		return checkedMnCabang.get();
	}
	
	public void setCheckedMnCabang(boolean checkedMnCabang) {
		this.checkedMnCabang.set(checkedMnCabang);
	}
	
	public SimpleBooleanProperty checkedMnUserProperty() {
		return checkedMnUser;
	}
	
	public SimpleBooleanProperty checkedMnCabangProperty() {
		return checkedMnCabang;
	}
}
