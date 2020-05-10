package com.chinasofti.postbar.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.postbar.basic.controller.BasicController;
import com.chinasofti.postbar.pojo.AudioDto;
import com.chinasofti.postbar.service.AudioService;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.ResponseBody;

//语音设置
@Controller
@RequestMapping("/audioSetUpController")
public class AudioSetUpController extends BasicController{
	@Autowired
	private AudioService audioService;
	@RequestMapping("/selectAudioSetUp")
	public void selectAudioSetUp(HttpServletResponse response,HttpServletRequest request) {
		JSONObject json=new JSONObject();
		json.put("message", "");
		//先判断是否过期
				if(sessionTimeout(request)) {
					json.put("message", "页面过期，请重新登录");
				}else {
					//先显示原来的用户信息
					 HttpSession session=request.getSession();
                     String userUUID= (String) session.getAttribute("id");
                   int isExist=  audioService.getAudioByUid(userUUID);
                   System.out.println("是否存在："+isExist);
                   AudioDto audioDto=new AudioDto();
                     if(isExist==0) {
                    	 audioDto=new AudioDto();
                    	 audioDto.setAuSetUUID(this.getUUID());
                     }else {
                    	 audioDto=audioService.findAudioByUid(userUUID);
					}
                     session.setAttribute("auSetUUID", audioDto.getAuSetUUID());
         			 json.put("audioDto", audioDto);
				}
				this.writeJson(json.toString(), response);
	}
	@RequestMapping("/updateAudioSetUp")
	public void updateAudioSetUp(HttpServletResponse response,HttpServletRequest request ,String auSetUUID,int auSetVoiPer,int auSetSpd,int auSetPit,int auSetVol) {
		JSONObject json=new JSONObject();
		json.put("message", "");
		//先判断是否过期
				if(sessionTimeout(request)) {
					json.put("message", "页面过期，请重新登录");
				}else {
					//更新个人信息    先获得当前id
					HttpSession session=request.getSession();
                    String userUUID= (String) session.getAttribute("id");
                     auSetUUID=(String) session.getAttribute("auSetUUID");
                     int isExist=  audioService.getAudioByUid(userUUID);
                     if(isExist==0) {
                    	 audioService.insertAudio(auSetUUID,auSetVoiPer,auSetSpd,auSetPit,auSetVol,userUUID); 
                     }else {
                    	 audioService.updateAudio(auSetUUID,auSetVoiPer,auSetSpd,auSetPit,auSetVol,userUUID); 
					}
				}
				this.writeJson(json.toString(), response);
	}
}
