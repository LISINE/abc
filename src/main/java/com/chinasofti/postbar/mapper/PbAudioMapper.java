package com.chinasofti.postbar.mapper;
//负责处理语音的Mapper

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.chinasofti.postbar.pojo.AudioDto;

// 实际运行的时候是由mybaits框架通过动态代理技术 成了实现类
@Mapper
public interface PbAudioMapper {

	//根据用户uuid查询语音设置数据
	AudioDto getAudioByUserUuid(@Param("uuid")String userUuid);
	//更新语音设置
	void updateAudio(@Param("auSetUUID")String auSetUUID, @Param("auSetVoiPer")int auSetVoiPer, @Param("auSetSpd") int auSetSpd, @Param("auSetPit")int auSetPit, @Param("auSetVol")int auSetVol ,@Param("userUUID")String userUUID);
	//查询初始语音
	int getAudioByUid(@Param("userUUID")String userUUID);
	AudioDto findAudioByUid(@Param("userUUID")String userUUID);

	void insertAudio(@Param("auSetUUID")String auSetUUID, @Param("auSetVoiPer")int auSetVoiPer, @Param("auSetSpd") int auSetSpd, @Param("auSetPit")int auSetPit, @Param("auSetVol")int auSetVol ,@Param("userUUID")String userUUID);

	void addAudio(AudioDto audioDto);
		
}