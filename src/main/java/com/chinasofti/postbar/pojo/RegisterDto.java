package com.chinasofti.postbar.pojo;

public class RegisterDto {
	private static final long serialVersionUID = 1L;
	//注册记录的uuid
	private String regUUID;
	private String userUUID;
	private String regSex;
	private int regAge;
	private String regEmial;
	private String regPhoto;
	private String userName;
	private String regTime;
	private String loginTime;
	private String admin;

	public String getRegUUID() {
		return regUUID;
	}
	public void setRegUUID(String regUUID) {
		this.regUUID = regUUID;
	}
	public String getUserUUID() {
		return userUUID;
	}
	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}
	public String getRegSex() {
		return regSex;
	}
	public void setRegSex(String regSex) {
		this.regSex = regSex;
	}
	public int getRegAge() {
		return regAge;
	}
	public void setRegAge(int regAge) {
		this.regAge = regAge;
	}
	public String getRegEmial() {
		return regEmial;
	}
	public void setRegEmial(String regEmial) {
		this.regEmial = regEmial;
	}
	public String getRegPhoto() {
		return regPhoto;
	}
	public void setRegPhoto(String regPhoto) {
		this.regPhoto = regPhoto;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}

}
