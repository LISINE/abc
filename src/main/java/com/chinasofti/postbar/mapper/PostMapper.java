package com.chinasofti.postbar.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.chinasofti.postbar.pojo.AppDto;
import com.chinasofti.postbar.pojo.PostDto;
@Mapper
public interface PostMapper {
	
	int getPostAllNum(@Param("postTitle")String postTitle, @Param("userUUID") String userUUID);
	List<PostDto> selectPostList(@Param("postTitle")String postTitle, @Param("pageIndex")Integer pageIndex, @Param("everyPageDataCount")Integer everyPageDataCount,@Param("userUUID") String userUUID);
	void deletePost(@Param("postUUID")String[] postUUID);
	//添加文章的方法

	void addPost(PostDto postDto);
	//查询密钥
	AppDto selectApp();
	PostDto getPostByuuid(@Param("postUUID")String postUUID);
	//阅读数+1
	int getPageViews(@Param("postUUID")String postUUID);
		

}
