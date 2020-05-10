package com.chinasofti.postbar.pojo;

public class AudioDto {
	private static final long serialVersionUID = 1L;
	private String auSetUUID;
	private String userUUID;
	private int auSetSpd;
	private int auSetPit;
	private int auSetVol;
	private int auSetVoiPer;
	public String getAuSetUUID() {
		return auSetUUID;
	}
	public void setAuSetUUID(String auSetUUID) {
		this.auSetUUID = auSetUUID;
	}
	public String getUserUUID() {
		return userUUID;
	}
	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}
	public int getAuSetPit() {
		return auSetPit;
	}
	public void setAuSetPit(int auSetPit) {
		this.auSetPit = auSetPit;
	}
	public int getAuSetSpd() {
		return auSetSpd;
	}
	public void setAuSetSpd(int auSetSpd) {
		this.auSetSpd = auSetSpd;
	}
	public int getAuSetVol() {
		return auSetVol;
	}
	public void setAuSetVol(int auSetVol) {
		this.auSetVol = auSetVol;
	}
	public int getAuSetVoiPer() {
		return auSetVoiPer;
	}
	public void setAuSetVoiPer(int auSetVoiPer) {
		this.auSetVoiPer = auSetVoiPer;
	}
}
