package com.chinasofti.postbar.pojo;

import java.io.Serializable;
//文章实体类
public class PostDto  implements Serializable{

	private static final long serialVersionUID = 1L;
	private String postUUID;
	private String userUUID;
	private String postTitle;
	private String postText;
	private int postPageviews;
	private String postAudio;

	private String postTime;
	private String userName;
	private int commentNum;
	private String cmTime;

	public String getPostAudio() {
		return postAudio;
	}

	public void setPostAudio(String postAudio) {
		this.postAudio = postAudio;
	}

	public int getPostPageviews() {
		return postPageviews;
	}

	public void setPostPageviews(int postPageviews) {
		this.postPageviews = postPageviews;
	}

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}

	public String getPostUUID() {
		return postUUID;
	}

	public void setPostUUID(String postUUID) {
		this.postUUID = postUUID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getCmTime() {
		return cmTime;
	}

	public void setCmTime(String cmTime) {
		this.cmTime = cmTime;
	}

}