package com.chinasofti.postbar.service;
//语音接口

import com.chinasofti.postbar.pojo.AudioDto;

public interface AudioService {
	//根据用户id获得用户语音
	public abstract AudioDto getAudioByUserUuid(String userUuid);
	   //更新语音设置
		public abstract void updateAudio(String auSetUUID, int auSetVoiPer, int auSetSpd, int auSetPit, int auSetVol,String userUUID);
		//查询语音记录是否存在
		public abstract int getAudioByUid(String userUUID);
		//查询存在的记录
		public abstract AudioDto findAudioByUid(String userUUID);
		public abstract void insertAudio(String auSetUUID, int auSetVoiPer, int auSetSpd, int auSetPit, int auSetVol,
				String userUUID);
}