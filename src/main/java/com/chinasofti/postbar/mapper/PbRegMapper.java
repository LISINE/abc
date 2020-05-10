package com.chinasofti.postbar.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.chinasofti.postbar.pojo.RegisterDto;
@Mapper
public interface PbRegMapper {
	void insertRegister(RegisterDto registerDto);
	//根据用户id获得register信息
	RegisterDto getRegid(@Param("userUUID")String userUUID);

	RegisterDto getRegisterByUuid(@Param("uuid") String uuid);
	//用户列表查询
	List<RegisterDto> selectPostList(@Param("userName")String userName, @Param("startNo") Integer pageNo, @Param("pageSize") Integer pageSize,@Param("userUUID") String userUUID);


	//根据id查询头像
	RegisterDto getPhotoById(@Param("userUUID")String userUUID);

	//上传头像
	void updatePath(@Param("userUUID")String userUUID, @Param("urlPath")String urlPath);
}

