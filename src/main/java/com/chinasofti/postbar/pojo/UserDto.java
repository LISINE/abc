package com.chinasofti.postbar.pojo;

import java.io.Serializable;

//Serializable牵涉分布式架构
public class UserDto  implements Serializable{
	//serialVersionUID 保证自己的序列化和反序列化的结果一致
	private static final long serialVersionUID = 1L;
	private String userUUID;
	private String userName;
	private String password;
	private String loginTime;
	private String admin;
	
	@Override
	public String toString() {
		return "UserDto [userName=" + userName + ", password=" + password + ", loginTime=" + loginTime + ", admin="
				+ admin + "]";
	}
	public String getUserUUID() {
		return userUUID;
	}
	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
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

	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
}
