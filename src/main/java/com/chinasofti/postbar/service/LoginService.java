package com.chinasofti.postbar.service;


import java.util.List;

import com.chinasofti.postbar.pojo.AudioDto;
import com.chinasofti.postbar.pojo.RegisterDto;
import com.chinasofti.postbar.pojo.UserDto;

public interface LoginService {
//根据用户id查询一个用户对象 
	UserDto getUserByUuid(String uuid);
//根据账号和密码获得用户
	UserDto getUserByNameAndPwd(String userName, String password);
	//修改登录时间
	void changeLoginTimeByUsername(String userName,String loginTime);
	int getUserNumByUserName(String userName);
	void addUserRegister(RegisterDto registerDto, UserDto userDto, AudioDto audioDto);
	void updatePassword(String newPassword,String username);
	RegisterDto getRegisterByUuid(String uuid);
	//获得用户列表
	public abstract List<RegisterDto> selectPostList(String userName, Integer pageIndex, Integer everyPageDataCount,String userUUID);
	//上传头像：根据id查询头像
	RegisterDto getPhotoById(String userUUID);
	//上传头像
	void updatePath(String userUUID, String urlPath);
	//获得用户列表
	int getUserAllNum(String userName, String userUUID);
	//删除用户
	void deleteUser(String[] userUUID);
	//更新用户
	void editUser(String userUUID, String userName, String regSex, int regAge, String regEmial,
			String admin, String password);
}
