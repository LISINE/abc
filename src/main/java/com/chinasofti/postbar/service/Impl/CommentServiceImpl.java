package com.chinasofti.postbar.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinasofti.postbar.pojo.CommentDto;
import com.chinasofti.postbar.pojo.PraiseDto;
import com.chinasofti.postbar.mapper.CommentMapper;

import com.chinasofti.postbar.service.CommentService;
@Service
public class CommentServiceImpl implements CommentService {
@Autowired
private CommentMapper commentMapper;
//获取点赞列表
@Override
public List<PraiseDto> getPostpraise( String postUUID) {
	// TODO Auto-generated method stub
	return commentMapper.getPostpraise(postUUID);
}
//获取热评
@Override
public List<CommentDto> getHotlistByPostuuid(String postUUID) {
	// TODO Auto-generated method stub
	return commentMapper. getHotlistByPostuuid(postUUID);
}
//获取所有列表
@Override
public List<CommentDto> getAllListByuuid(String uuid) {
	// TODO Auto-generated method stub
	return commentMapper. getAllListByuuid(uuid);
}
//删除评论
@Override
public void deleteComment(String[] cmUUID) {
	commentMapper.deleteComment(cmUUID);
	
}
//获得点赞情况
@Override
public int getCommentPraise(String postUUID, String cmUUID, String userUUID) {
	// TODO Auto-generated method stub
	return commentMapper.getCommentPraise(postUUID,cmUUID,userUUID);
}
//增加点赞
@Override
public void addCommentPraise(String cmPrUUID, String postUUID, String cmUUID, String userUUID) {
	commentMapper.addCommentPraise(cmPrUUID,postUUID,cmUUID,userUUID);
}
//添加评论
@Override
public void addCom(CommentDto commentDto) {
	// TODO Auto-generated method stub
	commentMapper.addCom(commentDto);
}
//是否给文章点赞
@Override
public int getPostCommentPraise(String postUUID, String userUUID) {
	// TODO Auto-generated method stub
	return commentMapper.getPostCommentPraise(postUUID, userUUID);
}
//增加文章点赞记录
@Override
public void addPostCommentPraise(String poPrUUID, String postUUID, String userUUID) {
	// TODO Auto-generated method stub
	commentMapper.addPostCommentPraise(poPrUUID, postUUID, userUUID);
}
//获得文章赞数
@Override
public int getPostPraiseNum(String postUUID) {
	// TODO Auto-generated method stub
	return commentMapper.getPostPraiseNum(postUUID) ;
}
//评论列表总数
@Override
public int getAllNum(String userUUID) {
	// TODO Auto-generated method stub
	return commentMapper.getAllNum(userUUID);
}
//
//获取我的评论列表
@Override
public List<CommentDto> selectComList(Integer pageIndex, Integer everyPageDataCount, String userUUID) {
	// TODO Auto-generated method stub
	return commentMapper.selectComList(pageIndex,everyPageDataCount,userUUID);
}
//查找评论
@Override
public CommentDto getCommentOne(String cmUUID, String userUUID) {
	// TODO Auto-generated method stub
	return commentMapper.getCommentOne(cmUUID, userUUID);
}
//更新评论
@Override
public void editCom(String userUUID, String cmUUID, String cmText) {
	// TODO Auto-generated method stub
	 commentMapper.editCom(cmUUID, userUUID,cmText);
}

	@Override
	public List<CommentDto> getLastCommentList() {
		return commentMapper.getLastCommentList();
	}

}
