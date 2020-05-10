package com.chinasofti.postbar.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.chinasofti.postbar.pojo.RegisterDto;
import com.chinasofti.postbar.pojo.UserDto;
@Mapper
public interface PbLoginMapper {
	UserDto getUserByUuid(@Param("uuid")String uuid);

	UserDto getgetUserByNameAndPwd(@Param("userName")String userName, @Param("password")String password);
	//更改最后一次登录时间
	void changeLoginTimeByUsername( @Param("userName")String userName, @Param("loginTime")String loginTime);
	//查询存不存用户名
	int getUserNumByUserName(@Param("userName")String userName);

	void insertUser(UserDto userDto);
	void updatePassword(@Param("password")String newPassword,@Param("username")String username);

	//更新个人信息
	void updateUser(@Param("regUUID")String regUUID, @Param("userName")String userName, @Param("regSex")String regSex, @Param("regAge")int regAge, @Param("regEmial")String regEmial,@Param("oldName")String oldName);

	int selectUserAllNum(@Param("userName")String userName, @Param("userUUID")String userUUID);

	
	void deleteUser(@Param("userUUID")String[] UUID);
	//编辑用户
	void editUser(@Param("userUUID")String userUUID, @Param("userName")String userName, @Param("regSex")String regSex, @Param("regAge")int regAge,
				  @Param("regEmial")String regEmial, @Param("admin")String admin,
				  @Param("password")String password);

}


