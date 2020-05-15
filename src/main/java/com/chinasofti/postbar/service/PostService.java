package com.chinasofti.postbar.service;

import java.util.List;

import com.chinasofti.postbar.pojo.AppDto;
import com.chinasofti.postbar.pojo.PostDto;

public interface PostService {
//获得所有文章
int getPostAllNum(String postTitle, String userUUID);
//获得当前页
List<PostDto> selectPostList(String postTitle, Integer pageIndex, Integer everyPageDataCount, String userUUID);
//删除文章
void deletePost(String[] postUUID);
//选择密钥
AppDto selectApp();

void addPost(PostDto postDto);
//查询发帖信息
	PostDto getPostByuuid(String postUUID);
	//点击文章阅读+1
	int getPageViews(String postUUID);

    List<PostDto> getLastPostList();
}
