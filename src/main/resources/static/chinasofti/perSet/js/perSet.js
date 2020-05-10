$(document).ready(function () {
	
	ajaxLoading();
	$.ajax({
	    type: "POST",
	    url: "/postbar/perSetUpController/selectPerSetUp",
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
	    		
	            var reg=json.registerDto

	         	var regage=$("#regAge");
	         	
	         	for(var i=14;i<100;i++){
	             	regage.append("<option value='"+i+"'>"+i+"</option>");
	             }
	         	 $("#regUUID").val(reg.regUUID);
	             $("#userName").val(reg.userName);
	    		 $("#regSex option[value='" + reg.regSex + "']").attr("selected", true);
	    		 $("#regAge option[value='" + reg.regAge + "']").attr("selected", true);
	    		 $("#regEmial").val(reg.regEmial);
	    		 $("#oldUserName").val(reg.userName);
	    	}
	    }
	});

});


function subReg(){
	var regUUID=$("#regUUID").val();
	var userName=$("#userName").val();
	var regSex=$("#regSex").val();
	var regAge=$("#regAge").val();
	var regEmial=$("#regEmial").val();
	var oldName=$("#oldUserName").val();
	if(typeof (userName) == 'undefined' || userName.trim()=="" ){
		$("#tishi").html("用户名不能为空");
		return;
	}
	if(userName.trim().length>20){
		$("#tishi").html("用户名不能大于20个字符");
		return;
	}
	

	if(typeof (regEmial) == 'undefined' || regEmial.trim()==""){
		$("#tishi").html("邮箱地址不能为空");
		return;
	}

	if(!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(regEmial.trim()))){
		$("#tishi").html("邮箱地址格式不正确");
		return false;
	}
	
	ajaxLoading();
	
	$.ajax({
        type: "POST",
        url: "/postbar/perSetUpController/updatePerSetUp",
        async: true,
        data:{
        	regUUID:regUUID.trim(),
        	userName:userName.trim(),
        	regSex:regSex.trim(),
        	regAge:regAge.trim(),
        	regEmial:regEmial.trim(),
        	oldName:oldName.trim()
       	 },
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	$.MsgBox.Alert("消息","出错了，请于管理员联系");
        },
        success: function (json) {
        	ajaxLoadEnd();
        	if(json.message!=""){
        		window.parent.location.replace("/postbar/");
        	}else{
        		if(json.error!=""){
        			$("#tishi").html(json.error)
        		}else{
            		window.parent.layui.use(["jqtab"],function(){
    	    			var funTab =  window.parent.$("#funTab");
    	    			menutab = funTab.btab();
    	    			menutab.removeOther();
    	    		});
            		window.parent.location.replace("/postbar/chinasofti/login/login.html");
        		}
        		
        		//alert(JSON.stringify(json));
        	}
        	
        }
    });
}