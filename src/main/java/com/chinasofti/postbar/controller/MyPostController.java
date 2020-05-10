package com.chinasofti.postbar.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.postbar.basic.controller.BasicController;

import com.chinasofti.postbar.pojo.PostDto;

import com.chinasofti.postbar.service.PostService;


import net.sf.json.JSONObject;

@Controller
@RequestMapping("/myPostController")
public class MyPostController extends BasicController{
	// 注入文章的Service
	@Autowired
	private PostService postService;


	// 1.获得文章列表的方法 可能接收的参数 条件 分页参数 起始索引 每一页数据大小
	@RequestMapping("/getMyPostList")
	public void getPostList(HttpServletResponse response, HttpServletRequest request, String postTitle,
			Integer pageIndex, Integer everyPageDataCount) {
		HttpSession session = request.getSession();
		String uuid=(String) session.getAttribute("id");
		JSONObject json = new JSONObject();
		json.put("message", "");
		// 判断页面是否过期
		if (this.sessionTimeout(request)) {
			json.put("message", "页面过期,请重新登录!");
		} else {
			json.put("postAllNum", 0);// postAllNum 放置文章的总记录数
			json.put("allPage", 0);// all 总页数 是根据 总记录数和每一页大小计算出来
			json.put("pageIndex", 0);// 当前页的起始索引
			json.put("postList", "");// 当前页的数据列表
			json.put("admin", ((String) session.getAttribute("admin")));// 角色
			// 获得文章记录数 并覆盖postAllNum值
			int postAllNum = postService.getPostAllNum(postTitle, uuid);
			json.put("postAllNum", postAllNum);
			// 根据用户文章数 计算总页数
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

				// 查询文章列表 该用户的所有文章
				List<PostDto> list = postService.selectPostList(postTitle, pageIndex * everyPageDataCount,
						everyPageDataCount, uuid);

				json.put("postList", list);

			}

		}
		// 把json对象写会到浏览器
		this.writeJson(json.toString(), response);

	}

}
