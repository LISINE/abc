var everyPageDataCount=7;
var postPageIndex=0;
var postAllPage=0;
$(document).ready(function () {
	 
	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,0,everyPageDataCount,true,"/postbar/userManageController/getUserList");
	
	var regage=$("#regAge");
	var selectPotion=$("#regAge option");
	
	if(selectPotion.length=="0"){
		for(var i=14;i<100;i++){
    		regage.append("<option value='"+i+"'>"+i+"</option>");
    	}
	}
	
});
function getPostList(postTitle,pageIndex,everyPageDataCount,SynOrAsyn,url){
	$.ajax({
	  type: "POST",
	  url: url,
	  async: SynOrAsyn,
	  data:{
		userName:postTitle,
		pageIndex:pageIndex,
	  	everyPageDataCount:everyPageDataCount
	  },
	  dataType: "json",
	  error: function (XMLHttpRequest, textStatus, errorThrown) {
		  $.MsgBox.Alert("消息","出错了，请于管理员联系");
	  },
	  success: function (json) {
	  	if(json.message!=""){
	  		window.parent.location.replace("/postbar/");
	  	}else{
	  		$("#SEARCH_POST_NAME_HIDDEN").val(postTitle.trim());
	  		showPostlist(json.registerList,json.userAllNum,json.allPage,json.pageIndex);
	  	}
	  	
	  }
	});
}


function showPostlist(registerList,postAllNum,allPage,pageIndex){
	postPageIndex=pageIndex;
	postAllPage=allPage;
	var html=""

	html+='<button class="btn  btn-danger btn-sm" type="button" onclick="DELETE_POST()">删除</button>'

	$("#postAddAndDeleteDiv").html(html);
	html=""
	for(var i=0;i<registerList.length;i++){
		html+='<tr bgcolor="#FFFFFF">';
		html+='		<td align="center" width="20">';
		html+='			<input name="DELETE_CHECK_NAME" type="checkbox" value="'+registerList[i].userUUID+'">';

		html+='		</td>';
		
		
		
		html+='		<td valign="center" align="center" width="30">';
		html+='			<a href="" onclick="EDIT_USER(\''+registerList[i].userUUID+'\',\''+registerList[i].userName+'\',\''+registerList[i].regSex+'\',\''+registerList[i].regAge+'\',\''+registerList[i].regEmial+'\',\''+registerList[i].admin+'\'); return false;">'+registerList[i].userName+'</a> ';
		html+='		</td>';
		html+='		<td valign="center" align="center" width="30">';
		html+=			registerList[i].regAge;
		html+='		</td>';
		html+='		<td valign="center" align="center" width="110">';
		if(registerList[i].regSex=="0"){
			html+="男"
		}else{
			html+="女"
		}
		html+='		</td>';
		html+='		<td valign="center" align="center" width="110">';
		html+=			registerList[i].regTime.substring(0,19) ;
		html+='		</td>';
		html+='		<td valign="center" align="center" width="100">';
		html+=			registerList[i].loginTime.substring(0,19) ;
		html+='		</td>';
		html+='		<td valign="center" align="center" width="100">';
		if(registerList[i].admin=="1"){
			html+="管理员";
		}else{
			html+="普通用户";
		}
		
		html+='		</td>';
		html+='</tr>';
	
	}
	$("#POST_LIST_TBODY_ID").html(html);
	
	html="";
	if(postAllNum!=0){
		if(allPage==1){
			html+='<li style="margin-left: 30px">';
			html+='		<div class="dataTables_info" style="margin-top: 6px;margin-left: 100px"><span>1/1 页</span> <span>共'+postAllNum+'条</span></div>';
			html+='</li>';
		}else if(pageIndex==0){
			html+='<li style="margin-left: 30px">';
			html+='		<button class="btn btn-primary" type="button" onclick="GOTO_POST_NEXT_PAGE(\''+(pageIndex+1)+'\')">后一页</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<button class="btn btn-primary" type="button" onclick="GOTO_POST_TAIL_PAGE(\''+(allPage-1)+'\')">末页</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<input id="JUMP_INPUT_ID" type="text" style="display:inline;width:80px" size="6" >';
			html+='		<button class="btn btn-sm btn-outline-primary" onclick="GOTO_POST_PAGE();return false;">跳转</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<div class="dataTables_info" style="margin-top: 6px;margin-left: 100px"><span>'+(pageIndex+1)+'/'+allPage+' 页</span> <span>共'+postAllNum+'条</span></div>';
			html+='</li>';
		}else if(pageIndex==allPage-1){
			html+='<li style="margin-left: 30px">';
			html+='		<button class="btn btn-primary" type="button" onclick="GOTO_POST_HOME_PAGE(\''+0+'\')">首页</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<button class="btn btn-primary" type="button" onclick="GOTO_POST_PREVIOUS_PAGE(\''+(pageIndex-1)+'\')">前一页</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<input id="JUMP_INPUT_ID" type="text" style="display:inline;width:80px" size="6" >';
			html+='		<button class="btn btn-sm btn-outline-primary" onclick="GOTO_POST_PAGE();return false;">跳转</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<div class="dataTables_info" style="margin-top: 6px;margin-left: 100px"><span>'+(pageIndex+1)+'/'+allPage+' 页</span> <span>共'+postAllNum+'条</span></div>';
			html+='</li>';
		}else{
			html+='<li style="margin-left: 30px">';
			html+='		<button class="btn btn-primary" type="button" onclick="GOTO_POST_HOME_PAGE(\''+0+'\')">首页</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<button class="btn btn-primary" type="button" onclick="GOTO_POST_PREVIOUS_PAGE(\''+(pageIndex-1)+'\')">前一页</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<button class="btn btn-primary" type="button" onclick="GOTO_POST_NEXT_PAGE(\''+(pageIndex+1)+'\')">后一页</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<button class="btn btn-primary" type="button" onclick="GOTO_POST_TAIL_PAGE(\''+(allPage-1)+'\')">末页</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<input id="JUMP_INPUT_ID" type="text" style="display:inline;width:80px" size="6" >';
			html+='		<button class="btn btn-sm btn-outline-primary" onclick="GOTO_POST_PAGE();return false;">跳转</button>';
			html+='</li>';
			html+='<li style="margin-left: 30px">';
			html+='		<div class="dataTables_info" style="margin-top: 6px;margin-left: 100px"><span>'+(pageIndex+1)+'/'+allPage+' 页</span> <span>共'+postAllNum+'条</span></div>';
			html+='</li>';
		}
	}
	
	$("#PAGE_ID").html(html);
}

function GOTO_POST_NEXT_PAGE(){

	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,postPageIndex+1,everyPageDataCount,true,"/postbar/userManageController/getUserList");
}

function GOTO_POST_TAIL_PAGE(){
	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,postAllPage-1,everyPageDataCount,true,"/postbar/userManageController/getUserList");
}

function GOTO_POST_PAGE(){
	var jumpVal=$("#JUMP_INPUT_ID").val().trim();
	if(jumpVal==""){
		$.MsgBox.Alert("消息","跳转页不能为空");
		return;
	}
	if(!(/^[0-9]+$/.test(jumpVal))){
		$.MsgBox.Alert("消息","页码必须为数字");
		return;
	}
	if(jumpVal<=0){
		$.MsgBox.Alert("消息","页码必须大于等于1");
		return;
	}
	if(jumpVal>postAllPage){
		$.MsgBox.Alert("消息","页码超出上限");
		return;
	}
	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,jumpVal-1,everyPageDataCount,true,"/postbar/userManageController/getUserList");
}


function GOTO_POST_HOME_PAGE(){
	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,0,everyPageDataCount,true,"/postbar/userManageController/getUserList");
}

function GOTO_POST_PREVIOUS_PAGE(){
	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,postPageIndex-1,everyPageDataCount,true,"/postbar/userManageController/getUserList");
	 
}
function searchByPostName(){
	var searchNameVal=$("#SEARCH_POST_NAME").val().trim();
	getPostList(searchNameVal,0,everyPageDataCount,true,"/postbar/userManageController/getUserList");
}
function DELETE_POST(){
	var chk_value =[]; 
    $('input[name="DELETE_CHECK_NAME"]:checked').each(function(){ 
        chk_value.push($(this).val()); 
    }); 
    if(chk_value.length==0){
    	$.MsgBox.Alert("消息","请先选择需要删除的用户");
    	return;
    }
    
    $.ajax({
	    type: "POST",
	    url: "/postbar/userManageController/deleteUser",
	    async: true,
	    data:{
	    	userUUID:chk_value
	   	 },
	    dataType: "json",
	    error: function (XMLHttpRequest, textStatus, errorThrown) {
	    	$.MsgBox.Alert("消息","出错了，请和管理员联系");
	    },
	    success: function (json) {
	    
	    	if(json.message!=""){
	    		window.parent.location.replace("/postbar/");
	    	}else{
	    		var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	    		getPostList(searchNameVal,0,everyPageDataCount,true,"/postbar/userManageController/getUserList");
	    		//window.parent.removeOther();
	    		window.parent.layui.use(["jqtab"],function(){
	    			var funTab =  window.parent.$("#funTab");
	    			menutab = funTab.btab();
	    			menutab.removeOther();
	    		});
	    	}
	    }
	});
}
function editUserCheck(){
	var userUUID=$("#userUUID").val();
	var userName=$("#userName").val();
	var regsex=$("#regsex").val();
	var regAge=$("#regAge").val();
	var regEmial=$("#regEmial").val();
	var admin=$("#admin").val();
	var password=$("#password").val();
	var oldName= $("#oldUserName").val();
	if(typeof (userName) == 'undefined' || userName.trim()=="" ){
		$("#tishi").html("用户名不能为空");
		return;
	}
	if(userName.trim().length>20){
		$("#tishi").html("用户名不能大于20个字符");
		return;
	}

	if(password.trim()!="" && password.trim().length!=6){
		$("#tishi").html("密码必须为6位");
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
	
	  $.ajax({
		    type: "POST",
		    url: "/postbar/userManageController/editUser",
		    async: true,
		    data:{
		    	userUUID:userUUID.trim(),
		    	userName:userName.trim(),
		    	regsex:regsex.trim(),
		    	regAge:regAge.trim(),
		    	regEmial:regEmial.trim(),
		    	admin:admin.trim(),
		    	password:password.trim(),
		    	oldName:oldName.trim()
		   	 },
		    dataType: "json",
		    error: function (XMLHttpRequest, textStatus, errorThrown) {
		    	$.MsgBox.Alert("消息","出错了，请和管理员联系");
		    },
		    success: function (json) {
		    
		    	if(json.message!=""){
		    		window.parent.location.replace("/postbar/");
		    	}else{
		    		if(json.error!=""){
		    			$("#tishi").html(json.error)
		    		}else{
			    		 $("#userUUID").val("");
			    		 $("#userName").val("");
			    		 $("#regsex").val("");
			    		 $("#regAge").val("");
			    		 $("#regEmial").val("");
			    		 $("#admin").val("");
			    		 $("#password").val("");
			    		 $("#oldUserName").val("");
			    		 $("#tishi").html("");
			    		 $("#POST_LIST_DIV_ID").attr("style","display:block;");//隐藏div
			    		 $("#POST_ADD_DIV_ID").attr("style","display:none;");//隐藏div
			    		var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
			    		getPostList(searchNameVal,0,everyPageDataCount,true,"/postbar/userManageController/getUserList");
			    		window.parent.layui.use(["jqtab"],function(){
			    			var funTab =  window.parent.$("#funTab");
			    			menutab = funTab.btab();
			    			menutab.removeOther();
			    		});
		    		}
		    		
		    	}
		    }
		});
	
	
}
function returnPostList(){
	 $("#userUUID").val("");
	 $("#userName").val("");
	 $("#regsex").val("");
	 $("#regAge").val("");
	 $("#regEmial").val("");
	 $("#admin").val("");
	 $("#password").val("");
	 $("#oldUserName").val("");
	 $("#tishi").html("");
	$("#POST_LIST_DIV_ID").attr("style","display:block;");//隐藏div
	$("#POST_ADD_DIV_ID").attr("style","display:none;");//隐藏div
}

function EDIT_USER(userUUID,userName,regsex,regAge,regEmial,admin){
	
	 $("#POST_LIST_DIV_ID").attr("style","display:none;");//隐藏div
	 $("#POST_ADD_DIV_ID").attr("style","display:block;");//隐藏div
	 
	 $("#userUUID").val(userUUID);
	 $("#userName").val(userName);
	 $("#regsex").val(regsex);
	 $("#regAge").val(regAge);
	 $("#regEmial").val(regEmial);
	 $("#admin").val(admin);
	 $("#oldUserName").val(userName);
}