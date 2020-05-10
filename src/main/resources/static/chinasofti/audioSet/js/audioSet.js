$(function(){
  
	ajaxLoading();
	$.ajax({
	    type: "POST",
	    url: "/postbar/audioSetUpController/selectAudioSetUp",
	    async: true,
	    data:{

	   	 },
	    dataType: "json",
	    error: function (XMLHttpRequest, textStatus, errorThrown) {
	    	$.MsgBox.Alert("消息","出错了，请和管理员联系");
	    },
	    success: function (json) {
	    	ajaxLoadEnd();
	    	if(json.message!=""){
	    		window.parent.location.replace("/postbar/");
	    	}else{
	             var auido=json.auidoDto
	             $("#auSetUUID").val(auido.auSetUUID);
	    		 $("#auSetVoiPer option[value='" + auido.auSetVoiPer + "']").attr("selected", true);
	    		 $("#auSetSpd option[value='" + auido.auSetSpd + "']").attr("selected", true);
	    		 $("#auSetPit option[value='" + auido.auSetPit + "']").attr("selected", true);
	    		 $("#auSetVol option[value='" + auido.auSetVol + "']").attr("selected", true);
	    		
	    	}
	    }
	});
});

function updateAudioSetUp(){
	var auSetUUID=$("#auSetUUID").val();
	var auSetVoiPer=$("#auSetVoiPer").val();
	var auSetSpd=$("#auSetSpd").val();
	var auSetPit=$("#auSetPit").val();
	var auSetVol=$("#auSetVol").val();
	$.ajax({
	    type: "POST",
	    url: "/postbar/audioSetUpController/updateAudioSetUp",
	    async: true,
	    data:{
	    	auSetUUID:auSetUUID,
	    	auSetVoiPer:auSetVoiPer,
	    	auSetSpd:auSetSpd,
	    	auSetPit:auSetPit,
	    	auSetVol:auSetVol
	   	 },
	    dataType: "json",
	    error: function (XMLHttpRequest, textStatus, errorThrown) {
	    	$.MsgBox.Alert("消息","出错了，请和管理员联系");
	    },
	    success: function (json) {
	    	ajaxLoadEnd();
	    	if(json.message!=""){
	    		window.parent.location.replace("/postbar/");
	    	}else{
	    		$.MsgBox.Alert("消息","修改成功");
	    	}
	    }
	});
}