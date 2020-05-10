package com.chinasofti.postbar.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.chinasofti.postbar.pojo.CommentDto;
import com.chinasofti.postbar.pojo.PraiseDto;



@Mapper
public interface CommentMapper {
	//获取点赞列表
	public  List<PraiseDto> getPostpraise(@Param("postUUID")String postUUID) ;
	//获取热评
	public List<CommentDto> getHotlistByPostuuid(@Param("postUUID")String postUUID) ;
	//获取全部评价
	public List<CommentDto> getAllListByuuid(@Param("uuid")String uuid);
	//删除评论
	public void deleteComment(@Param("cmUUID")String[] cmUUID);
	//获取点赞状态
	public int getCommentPraise(@Param("postUUID")String postUUID, @Param("cmUUID")String cmUUID,@Param("userUUID") String userUUID);
	//添加点赞
	public void addCommentPraise(@Param("cmPrUUID") String cmPrUUID,@Param("postUUID") String postUUID,@Param("cmUUID") String cmUUID, @Param("userUUID") String userUUID);
	//添加评论

	public void addCom(CommentDto commentDto);
	//是否给文章点赞
	public int getPostCommentPraise(@Param("postUUID") String postUUID, @Param("userUUID") String userUUID);
	//添加文章点赞记录
	public void addPostCommentPraise( @Param("poPrUUID")String poPrUUID, @Param("postUUID") String postUUID,@Param("userUUID")  String userUUID);
	//统计文章点赞数
	public int getPostPraiseNum(@Param("postUUID") String postUUID);
	//查询我的总评数
	public int getAllNum(@Param("userUUID")String userUUID);
	//查询当前页我的评论
	public List<CommentDto> selectComList(@Param("pageIndex")Integer pageIndex, @Param("everyPageDataCount")Integer everyPageDataCount,@Param("userUUID") String userUUID);
	//查找评论
	public CommentDto getCommentOne(@Param("cmUUID")String cmUUID, @Param("userUUID")String userUUID);
	//更新评论
	public void editCom(@Param("cmUUID")String cmUUID, @Param("userUUID")String userUUID, @Param("cmText")String cmText);
}
