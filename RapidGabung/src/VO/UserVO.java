package VO;

public class UserVO {
	String userName;
	String password;
	Integer role;
	
	public UserVO(){
		this.userName = "";
		this.password = "";
		this.role = 99;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}
	
	
}
