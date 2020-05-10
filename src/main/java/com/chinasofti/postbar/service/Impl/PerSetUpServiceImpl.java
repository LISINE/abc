package com.chinasofti.postbar.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinasofti.postbar.pojo.RegisterDto;
import com.chinasofti.postbar.mapper.PbLoginMapper;
import com.chinasofti.postbar.mapper.PbRegMapper;
import com.chinasofti.postbar.service.PerSetUpService;
@Service
public class PerSetUpServiceImpl implements PerSetUpService{
	    @Autowired
	   private PbLoginMapper  loginMapper ;
		@Autowired
		private PbRegMapper pbRegMapper;
	//个人设置，获得原始数据
		@Override
		public RegisterDto getRegid(String userUUID) {
			// TODO Auto-generated method stub
			return pbRegMapper.getRegid(userUUID);
		}
		//更新个人信息
		@Transactional
		@Override
		public void updateUser(String regUUID, String userName, String regSex, int regAge, String regEmial,String oldName) {
			// TODO Auto-generated method stub
			loginMapper.updateUser(regUUID,userName,regSex,regAge,regEmial,oldName);
		}

}
