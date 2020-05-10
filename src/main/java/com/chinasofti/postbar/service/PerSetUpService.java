package com.chinasofti.postbar.service;

import com.chinasofti.postbar.pojo.RegisterDto;

public interface PerSetUpService {

	//个人设置，根据id获得原始信息
		RegisterDto getRegid(String userUUID);
		
		//更新个人信息
		void updateUser(String regUUID, String userName, String regSex, int regAge, String regEmial,String oldName);


}
