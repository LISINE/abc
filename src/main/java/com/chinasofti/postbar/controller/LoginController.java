package com.chinasofti.postbar.controller;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.postbar.basic.controller.BasicController;
import com.chinasofti.postbar.pojo.AudioDto;
import com.chinasofti.postbar.pojo.RegisterDto;
import com.chinasofti.postbar.pojo.UserDto;
import com.chinasofti.postbar.service.LoginService;

import net.sf.json.JSONObject;

/**
 * @author 付懿玲 2020年2月28日 下午2:36:24
 *处理登录注册等请求,这里是处理请求的最后一层，不能再扔给用户了
 */
@Controller
@RequestMapping("/loginController")
public class LoginController extends BasicController{
	@Autowired
	private  LoginService loginService ;
	@RequestMapping("/addRegister")
	private void addRegister(HttpServletResponse response,HttpServletRequest request,String userName,String password,String regSex,int regAge,String regEmial) {
		JSONObject json=new JSONObject();
		try {
			json.put("message", "");
			//判断注册的用户是否已经存在
			if(loginService.getUserNumByUserName(userName)!=0){
				json.put("message", "用户名已存在");
			}else{
				
				String userUUID=this.getUUID();
				String md5Password=this.md5(password);
				String regTime=this.getStringDate(new Date());
				String regUUID=this.getUUID();
				String admin="0";
				String photoAir="/postbar/headPhoto/default/default.jpg";
				String auSetUUID=this.getUUID();
				int auSetPit=5;
				int auSetSpd=5;
				int auSetVol=5;
				int auSetVoiPer=0;
				RegisterDto registerDto=new RegisterDto();
				registerDto.setRegAge(regAge);
				registerDto.setRegEmial(regEmial);
				registerDto.setRegSex(regSex);
				registerDto.setRegTime(regTime);
				registerDto.setRegUUID(regUUID);
				registerDto.setUserUUID(userUUID);
				registerDto.setRegPhoto(photoAir);
				json.put("registerDto", registerDto.toString());
				UserDto userDto=new UserDto();
				userDto.setPassword(md5Password);
				userDto.setUserName(userName);
				userDto.setUserUUID(userUUID);
				userDto.setAdmin(admin);
				json.put("userDto", userDto.toString());
				AudioDto audioDto=new AudioDto();
				audioDto.setAuSetPit(auSetPit);
				audioDto.setAuSetSpd(auSetSpd);
				audioDto.setAuSetUUID(auSetUUID);
				audioDto.setAuSetVoiPer(auSetVoiPer);
				audioDto.setAuSetVol(auSetVol);
				audioDto.setUserUUID(userUUID);
				json.put("audioDto", audioDto.toString());
				loginService.addUserRegister(registerDto, userDto,audioDto);
			}
			this.writeJson(json.toString(), response);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			json.put("message", "服务器忙请稍后重试");
		}
	
	}

	@RequestMapping(value="/login")
	public void loginUser(HttpServletResponse response,HttpServletRequest request,String userName,String password) {
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("message", "");
		HttpSession session=request.getSession();
		password=this.md5(password);
		UserDto userDto=loginService.getUserByNameAndPwd(userName,password);
		System.out.println(userDto);
		String date=this.getStringDate(new Date());
		System.out.println("转换后的date"+date);
		if(userDto!=null) {
			/*把用户信息放入session中进入回显
			 * 更新用户的登录时间
			 * */
			session.setAttribute("username", userDto.getUserName());
			session.setAttribute("admin", userDto.getAdmin());
			session.setAttribute("id", userDto.getUserUUID());
			//如果登录时间是空  说明用户是第一次登录   登录时间是当前系统时间
			if(userDto.getLoginTime()==null){
				session.setAttribute("dateTiem", date);
			}else{
				
				session.setAttribute("dateTime", userDto.getLoginTime());
			}
			loginService.changeLoginTimeByUsername(userName, date);
			jsonObject.put("admin", userDto.getAdmin());
			System.out.println("登陆成功"+userDto.getUserName());
		}else {
			session.setAttribute("dateTiem", date);
			jsonObject.put("message", "用户名或密码错误");
			System.out.println("登录失败");
		}
		//把要传递的对象写回
		this.writeJson(jsonObject.toString(), response);
	}	
	//以json形式返回数据
	@RequestMapping("/getUserJson")
	private void writeObjiectToJson(String uuid,HttpServletResponse response) {
		//JSONObject 本质大的mapper集合，放置我们要转换的数据
		JSONObject jsonObject=new JSONObject();
		try {
			// TODO Auto-generated method stub
			UserDto ud=loginService.getUserByUuid(uuid);
			System.out.println("查询到"+ud);
			jsonObject.put("msg", "success");
			jsonObject.put("jsonUser", ud);
		} catch (Exception e) {
			// TODO: handle exception
			jsonObject.put("msg", "fail");
			jsonObject.put("jsonUser", "服务器忙");
		}
		this.writeJson(jsonObject.toString(), response);
	}
	@RequestMapping("/loginOut")
	public void loginOut(HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsonObject=new JSONObject();
		//清空session,获得，判断，清
		HttpSession session=request.getSession();
		if(session.getAttribute("id")!=null||session.getAttribute("id")!="") {
			session.invalidate();
		}
		this.writeJson(jsonObject.toString(),response);
	}
	@RequestMapping("/editPassword")
	public void editPassword(String oldPassword,String newPassword,HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("message", "");
		jsonObject.put("error", "");
		oldPassword=this.md5(oldPassword);
		newPassword=this.md5(newPassword);
		//先判断是否过期
		if(sessionTimeout(request)) {
			jsonObject.put("message", "页面过期，请重新登录");
		}else {
			//查询的旧密码是否是当前用户的
			HttpSession session=request.getSession();
			String username=(String)session.getAttribute("username");
			System.out.println("修改密码的用户名"+username);
			//检查用户是否存在
			UserDto uDto=loginService.getUserByNameAndPwd(username, oldPassword);
			System.out.println("查询到的user"+uDto);
		   if(uDto==null) {
			   jsonObject.put("error", "密码输入错误，请重新输入");
		   }else {
			   //执行更新密码
			   loginService.updatePassword(newPassword,username);
		   }
		}
		this.writeJson(jsonObject.toString(), response);
	}
	//用户权限
	@RequestMapping("/getAdmin")
	public void getUserRole(HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("message", "");
		jsonObject.put("admin", "");
		//判断页面是否过期
		if(this.sessionTimeout(request)) {
			jsonObject.put("message", "");
		}else {
			String admin=(String) request.getSession().getAttribute("admin");
			System.out.println("此人权限："+admin);
			jsonObject.put("admin", admin);
		}
		this.writeJson(jsonObject.toString(), response);
	}
}
