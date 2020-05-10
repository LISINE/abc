package com.chinasofti.postbar.service;

import java.util.List;

import com.chinasofti.postbar.pojo.CommentDto;

import com.chinasofti.postbar.pojo.PraiseDto;





public interface CommentService {
//获得点赞情况
	List<PraiseDto> getPostpraise(String postUUID);
//取得全评
	List<CommentDto> getHotlistByPostuuid(String postUUID);

	List<CommentDto> getAllListByuuid(String uuid);
//删除评论
	void deleteComment(String[] cmUUID);
//获得点赞情况
	int getCommentPraise(String postUUID, String cmUUID, String userUUID);
//增加点赞
	void addCommentPraise(String cmPrUUID, String postUUID, String cmUUID, String userUUID);
	//增加评论
	void addCom(CommentDto commentDto);
	//是否给文章点赞
	int getPostCommentPraise(String postUUID, String userUUID);
	//增加文章点赞记录
	void addPostCommentPraise(String poPrUUID, String postUUID, String userUUID);
	//统计赞数
	int getPostPraiseNum(String postUUID);
  //评论总数
	int getAllNum(String userUUID);
	//获得当前页
	List<CommentDto> selectComList(Integer pageIndex, Integer everyPageDataCount, String userUUID);
	//查找评论
	CommentDto getCommentOne(String cmUUID, String userUUID);
	//更新评论
	void editCom(String userUUID, String cmUUID, String cmText);


}
