package com.chinasofti.postbar.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chinasofti.postbar.pojo.AppDto;
import com.chinasofti.postbar.pojo.PostDto;
import com.chinasofti.postbar.mapper.PostMapper;
import com.chinasofti.postbar.service.PostService;
@Service
public class PostServiceImpl implements PostService {
	@Autowired
private PostMapper postmapper;
//文章列表
	@Override
	public int getPostAllNum(String postTitle,  String userUUID) {
		// TODO Auto-generated method stub
		return postmapper.getPostAllNum( postTitle, userUUID);
	}
//文章列表
	@Override
	public List<PostDto> selectPostList(String postTitle, Integer pageIndex, Integer everyPageDataCount, String userUUID) {
		// TODO Auto-generated method stub
		return postmapper.selectPostList( postTitle, pageIndex, everyPageDataCount, userUUID);
	}
	//删除文章还可能牵连评论、点赞，所以涉及事务安全性
	//spring aop知识：事务配置需要指定三个参数
	/*
	 * propagation 传播 现在的事务在service层，而是实际事务的控制在dao层，所以我们把事务从service传播到dao层
	 * 只要是操作改变了数据库，事务默认传播
	 * rollbackFor 指定[]回滚的原因，默认程序异常回滚
	 * isolation   隔离级别  使用数据库默认的隔离级别
	 * */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor= {RuntimeException.class,Exception.class},isolation=Isolation.DEFAULT)
	@Override
	public void deletePost(String[] postUUID) {
		postmapper.deletePost(postUUID);
		
	}
	@Override
	public AppDto selectApp() {
		
		return postmapper.selectApp();
	}
	//添加文章实现
		@Override
		public void addPost(PostDto postDto) {
			postmapper.addPost(postDto);
		}
		@Override
		public PostDto getPostByuuid(String postUUID) {
			// TODO Auto-generated method stub
			return postmapper.getPostByuuid(postUUID);
		}
	//阅读数+1
	@Override
	public int getPageViews(String postUUID) {
		// TODO Auto-generated method stub
		return postmapper.getPageViews(postUUID);
	}

	

}
