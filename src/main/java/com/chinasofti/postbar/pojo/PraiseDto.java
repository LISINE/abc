package com.chinasofti.postbar.pojo;

public class PraiseDto {
private String poPrUUID;
private String postUUID;
private String userUUID;
@Override
public String toString() {
	return "PraiseDto [poPrUUID=" + poPrUUID + ", postUUID=" + postUUID + ", userUUID=" + userUUID + "]";
}
public String getPoPrUUID() {
	return poPrUUID;
}
public void setPoPrUUID(String poPrUUID) {
	this.poPrUUID = poPrUUID;
}
public String getPostUUID() {
	return postUUID;
}
public void setPostUUID(String postUUID) {
	this.postUUID = postUUID;
}
public String getUserUUID() {
	return userUUID;
}
public void setUserUUID(String userUUID) {
	this.userUUID = userUUID;
}

}
