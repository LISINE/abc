package com.chinasofti.postbar.pojo;

public class AppDto {
private Integer id;
private String appID;
private String apiKey;
private String secretKey;
@Override
public String toString() {
	return "AppDto [id=" + id + ", appID=" + appID + ", apiKey=" + apiKey + ", secretKey=" + secretKey + "]";
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getAppID() {
	return appID;
}
public void setAppID(String appID) {
	this.appID = appID;
}
public String getApiKey() {
	return apiKey;
}
public void setApiKey(String apiKey) {
	this.apiKey = apiKey;
}
public String getSecretKey() {
	return secretKey;
}
public void setSecretKey(String secretKey) {
	this.secretKey = secretKey;
}

}
