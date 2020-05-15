package com.chinasofti.postbar.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinasofti.postbar.basic.controller.BasicController;
import com.chinasofti.postbar.pojo.RegisterDto;
import com.chinasofti.postbar.pojo.UserDto;
import com.chinasofti.postbar.service.LoginService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/userManageController")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserManageController extends BasicController{
		@Autowired
		private LoginService loginService;

		// 1.获得用户列表的方法 可能接收的参数 条件 分页参数 起始索引 每一页数据大小
		@RequestMapping("/getUserList")
		public void getUserList(HttpServletResponse response, HttpServletRequest request, String userName,
				Integer pageIndex, Integer everyPageDataCount) {
			JSONObject json = new JSONObject();
			json.put("message", "");
			// 判断页面是否过期
			if (this.sessionTimeout(request)) {
				json.put("message", "页面过期,请重新登录!");
			} else {
				// 去数据库查询数据 并对数据进行分页
	
				json.put("userAllNum", 0);// postAllNum 放置文章的总记录数
				json.put("allPage", 0);// all 总页数 是根据 总记录数和每一页大小计算出来
				json.put("pageIndex", 0);// 当前页的起始索引
				json.put("registerList", "");// 当前页的数据列表
				// 获得用户总记录数 并覆盖postAllNum值
				int userAllNum = loginService.getUserAllNum(userName, null);
				json.put("userAllNum", userAllNum);
				// 根据用户总数 计算总页数
				if (userAllNum > 0) {
					// 计算总页数方法
					// 总页数变量
					int allPage = 1;
					if ((userAllNum % everyPageDataCount) == 0) {
						allPage = userAllNum / everyPageDataCount;
					} else {
						allPage = userAllNum / everyPageDataCount + 1;
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

					// 查询用户列表 所有用户 所以用户id 设置为null
					List<RegisterDto> list = loginService.selectPostList(userName, pageIndex * everyPageDataCount,
							everyPageDataCount, null);
					json.put("registerList",  list);
				}

			}
			// 把json对象写会到浏览器
			this.writeJson(json.toString(), response);
		}
//		删除用户
		@RequestMapping("/deleteUser")
		public void deleteUser(HttpServletResponse response, HttpServletRequest request,@RequestParam("userUUID[]")String[] userUUID) {
			// 1.构建一个json对象
			JSONObject json = new JSONObject();
			json.put("message", "");
			// 判断页面是否过期
			if (sessionTimeout(request)) {
				json.put("message", "页面过期,请重新登录!");
			}else {
				//执行删除用户
				loginService.deleteUser(userUUID);
			}
			this.writeJson(json.toString(), response);

		}
//		编辑用户
		@RequestMapping("/editUser")
		public void editUser(HttpServletResponse response,HttpServletRequest request,String userUUID,
				String userName,String regsex,int regAge,String regEmial,String oldName,String admin,String password) {			
			JSONObject json=new JSONObject();
			json.put("message", "");
			json.put("error", "");
			//先判断是否过期
					if(sessionTimeout(request)) {
						json.put("message", "页面过期，请重新登录");
					}else {
						if(password==null) {
							UserDto userDto=loginService.getUserByUuid(userUUID);
							password=this.md5(userDto.getPassword());
						}
						//更新个人信息    先获得当前id
						loginService.editUser(userUUID,userName,regsex,regAge,regEmial,admin,password);
                         System.out.println(json.toString());
					}

					this.writeJson(json.toString(), response);
		}
}
