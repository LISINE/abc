package com.chinasofti.postbar.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.postbar.basic.controller.BasicController;
import com.chinasofti.postbar.pojo.CommentDto;
import com.chinasofti.postbar.service.CommentService;
//import com.sun.xml.internal.bind.v2.model.core.ID;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/myCommentController")
public class MyCommentController extends BasicController{
	
	@Autowired
	private CommentService commentservice;
//查询评论
	@RequestMapping("/selectMyCommentByUserUUID")
	public void selectMyCommentByUserUUID(HttpServletRequest request,HttpServletResponse response,Integer pageIndex,Integer everyPageDataCount) {
		HttpSession session = request.getSession();
		String userUUID=(String) session.getAttribute("id");
		JSONObject json = new JSONObject();
		json.put("message", "");
		json.put("myCommentlist", "");//当前页评论数据列表
		json.put("postAllNum", 0);//总记录数
		json.put("allPage", "");//总页数
		json.put("pageIndex", "");//当前页的起始索引
		// 判断页面是否过期
		if (this.sessionTimeout(request)) {
			json.put("message", "页面过期,请重新登录!");
		} else {
			//根据用户id查询用户评论记录数
			int postAllNum=commentservice.getAllNum(userUUID);
			json.put("postAllNum", postAllNum);
			if (postAllNum > 0) {
				// 计算总页数方法
				// 总页数变量
				int allPage = 1;
				if ((postAllNum % everyPageDataCount) == 0) {
					allPage = postAllNum / everyPageDataCount;
				} else {
					allPage = postAllNum / everyPageDataCount + 1;
				}

				// 防止页码越界
				if (pageIndex < 0) {
					pageIndex = 0;
				} else if (pageIndex >= allPage) {
					pageIndex = allPage - 1;
				}
				// 更新总页数和当前页索引
				json.put("allPage", allPage);
				json.put("pageIndex", pageIndex);
				
				//查询评论列表 该用户的所有评论
				List<CommentDto> list=commentservice.selectComList(pageIndex*everyPageDataCount,
						everyPageDataCount,userUUID);
				System.out.println("所有评论"+list);
				json.put("myCommentlist", list);
                
		}
	}
		this.writeJson(json.toString(), response);
	}
	//获取评论
	@RequestMapping("/getCommentByCmUUID")
	public void getCommentByCmUUID(HttpServletRequest request,HttpServletResponse response,String cmUUID) {
		HttpSession session = request.getSession();
		String userUUID=(String) session.getAttribute("id");
		JSONObject json = new JSONObject();
		json.put("message", "");
		json.put("cmText", "");
		// 判断页面是否过期
				if (this.sessionTimeout(request)) {
					json.put("message", "页面过期,请重新登录!");
				} else {
					CommentDto cDto=commentservice.getCommentOne(cmUUID,userUUID);
					json.put("cmText", cDto.getCmText());
				}
		this.writeJson(json.toString(), response);
	}
	//更改评论
	@RequestMapping("/editCom")
	public void editCom(HttpServletRequest request,HttpServletResponse response,String cmUUID,String cmText) {
	HttpSession session = request.getSession();
	String userUUID=(String) session.getAttribute("id");
	JSONObject json = new JSONObject();
	json.put("message", "");
	// 判断页面是否过期
			if (this.sessionTimeout(request)) {
				json.put("message", "页面过期,请重新登录!");
			} else {
				commentservice.editCom(userUUID,cmUUID,cmText);
			}
	this.writeJson(json.toString(), response);
}
}
