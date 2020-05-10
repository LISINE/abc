package com.chinasofti.postbar.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.postbar.basic.controller.BasicController;
import com.chinasofti.postbar.pojo.RegisterDto;
import com.chinasofti.postbar.service.PerSetUpService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/perSetUpController")
public class PerSetUpController extends BasicController{
	@Autowired
	private PerSetUpService perSetUpService;

	@RequestMapping("/selectPerSetUp")
	public void selectPerSetUp(HttpServletResponse response,HttpServletRequest request,String userUUID) {
		
		JSONObject json=new JSONObject();
		json.put("message", "");
		//先判断是否过期
		             
				if(sessionTimeout(request)) {
					json.put("message", "页面过期，请重新登录");
				}else {
					//先显示原来的用户信息
					
					 HttpSession session=request.getSession();
					  userUUID= (String) session.getAttribute("id");
					 RegisterDto reg=perSetUpService.getRegid(userUUID);
                     json.put("registerDto", reg);
//	                 loginService.getRegisterByUuid(userUUID);
				}
				this.writeJson(json.toString(), response);
	}
	
	@RequestMapping("/updatePerSetUp")
	public void updatePerSetUp(HttpServletResponse response,HttpServletRequest request,String regUUID,
			String userName,String regSex,int regAge,String regEmial,String oldName) {
		
		JSONObject json=new JSONObject();
		json.put("message", "");
		//先判断是否过期
				if(sessionTimeout(request)) {
					json.put("message", "页面过期，请重新登录");
				}else {
					//更新个人信息    先获得当前id
					HttpSession session=request.getSession();
//					 oldName= (String) session.getAttribute("userName");
					String userUUID=(String) session.getAttribute("id");
					RegisterDto registerDto=perSetUpService.getRegid(userUUID);
					//得到注册id和当前用户名
					regUUID=registerDto.getRegUUID();
					oldName=registerDto.getUserName();
//					System.out.println("用户名"+oldName);
//                  
					perSetUpService.updateUser(regUUID,userName,regSex,regAge,regEmial,oldName);
					
//                    loginService.updateUser(registerDto);
				}
				this.writeJson(json.toString(), response);
	}
}
