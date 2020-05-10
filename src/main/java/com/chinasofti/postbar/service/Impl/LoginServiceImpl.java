package com.chinasofti.postbar.service.Impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinasofti.postbar.pojo.AudioDto;
import com.chinasofti.postbar.pojo.RegisterDto;
import com.chinasofti.postbar.pojo.UserDto;
import com.chinasofti.postbar.mapper.PbAudioMapper;
import com.chinasofti.postbar.mapper.PbLoginMapper;
import com.chinasofti.postbar.mapper.PbRegMapper;
import com.chinasofti.postbar.service.LoginService;
@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	private PbLoginMapper  loginMapper ;
	@Autowired
	private PbRegMapper pbRegMapper;
	//注入语音Mapper
	@Autowired
	private PbAudioMapper pbAudioMapper;
	@Override
	public UserDto getUserByUuid(String uuid) {
		// TODO Auto-generated method stub
		UserDto uDto=loginMapper.getUserByUuid( uuid);
		return uDto;
	}
	@Override
	public UserDto getUserByNameAndPwd(String userName, String password) {
		UserDto userDto=loginMapper.getgetUserByNameAndPwd( userName, password);
		return userDto;
	}
	@Override
	public void changeLoginTimeByUsername(String userName, String loginTime) {
		loginMapper.changeLoginTimeByUsername(userName, loginTime);

	}
	//查询数据是否存在
	@Override
	public int getUserNumByUserName(String userName) {
		// TODO Auto-generated method stub
		return loginMapper.getUserNumByUserName( userName);
	}
	@Transactional
	@Override
	public void addUserRegister(RegisterDto registerDto, UserDto userDto, AudioDto audioDto) {
		//添加用户数据
		loginMapper.insertUser(userDto);
		//添加注册用户数据
		pbRegMapper.insertRegister(registerDto);
		//添加语音数据
		pbAudioMapper.addAudio(audioDto);

	}
	@Override
	public void updatePassword(String newPassword, String username) {
		// TODO Auto-generated method stub
		loginMapper.updatePassword(newPassword, username);
	}
	@Override
	public RegisterDto getRegisterByUuid(String uuid) {
		// TODO Auto-generated method stub
		return pbRegMapper.getRegisterByUuid(uuid);
	}
	//查询用户原头像
	@Override
	public RegisterDto getPhotoById(String userUUID) {
		// TODO Auto-generated method stub
		return pbRegMapper.getPhotoById(userUUID);
	}
	//修改头像路径
	@Override
	public void updatePath(String userUUID, String urlPath) {
		// TODO Auto-generated method stub
		pbRegMapper.updatePath(userUUID,urlPath);
	}
	//获得所有用户数
	@Override
	public int getUserAllNum(String username,String userUUID) {
		// TODO Auto-generated method stub
		return loginMapper.selectUserAllNum(username, userUUID);
	}
	//获得当前列表
	@Override
	public List<RegisterDto> selectPostList(String userName, Integer pageIndex, Integer everyPageDataCount,String userUUID) {
		// TODO Auto-generated method stub
		return pbRegMapper.selectPostList(userName,pageIndex,everyPageDataCount,userUUID);
	}
	//删除用户
	@Override
	public void deleteUser(String[] userUUID) {
		// TODO Auto-generated method stub
		loginMapper.deleteUser(userUUID);
	}

	//更新用户
	@Override
	public void editUser(String userUUID, String userName, String regSex, int regAge, String regEmial, String admin,
						 String password) {
		loginMapper.editUser(userUUID,userName,regSex,regAge,regEmial,admin,password);

	}
}