package com.chinasofti.postbar.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinasofti.postbar.pojo.AudioDto;
import com.chinasofti.postbar.mapper.PbAudioMapper;
import com.chinasofti.postbar.service.AudioService;
@Service
public class AudioServiceImpl implements AudioService {
	//注入语音Mapper对象
	@Autowired
	private PbAudioMapper audioMapper;
	//根据用户id查询用户语音
	@Override
	public AudioDto getAudioByUserUuid(String userUuid) {
		// TODO Auto-generated method stub
		return audioMapper.getAudioByUserUuid(userUuid);
	}
	//更新语音设置
		@Override
		public void updateAudio(String auSetUUID, int auSetVoiPer, int auSetSpd, int auSetPit, int auSetVol,String userUUID) {
			// TODO Auto-generated method stub
			audioMapper.updateAudio(auSetUUID,auSetVoiPer,auSetSpd,auSetPit,auSetVol,userUUID);
		}
		//查询语音
		@Override
		public int getAudioByUid(String userUUID) {
			// TODO Auto-generated method stub
			return audioMapper.getAudioByUid(userUUID);
		}
		@Override
		public AudioDto findAudioByUid(String userUUID) {
			// TODO Auto-generated method stub
			return audioMapper.findAudioByUid(userUUID);
		}
		//插入记录
		@Override
		public void insertAudio(String auSetUUID, int auSetVoiPer, int auSetSpd, int auSetPit, int auSetVol,
				String userUUID) {
			audioMapper.insertAudio(auSetUUID,auSetVoiPer,auSetSpd,auSetPit,auSetVol,userUUID);
			
		}

}
