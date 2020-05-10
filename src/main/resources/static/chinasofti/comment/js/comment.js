var everyPageDataCount=7;
var postPageIndex=0;
var postAllPage=0;
var postUUID="";
var returnpage=""
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}
$(function(){
  
	KindEditor.options.cssData = 'body {font-family:微软雅黑;}',
	editor = KindEditor.create('textarea[id="COM_ADD_DES"]', {
		allowUpload : true,
	    uploadJson: '/postbar/postController/kindEditorImgInput',
	    allowFileManager: false,
	    width:'900px',
	    height: '300px',
	    items: [ 'fullscreen','|','undo', 'redo', '|', 'preview', 'print', 'cut', 'copy', 'paste',
	            'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
	            'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
	            'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
	            'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image',
	             'table', 'hr', 'emoticons', ]
	});
	
	postUUID=GetQueryString("postid");
	returnpage=GetQueryString("page");
	getPostList(postUUID,true,"/postbar/commentController/getInit");
});


function getPostList(postUUID,SynOrAsyn,url){
	ajaxLoading();
	$.ajax({
	  type: "POST",
	  url: url,
	  async: SynOrAsyn,
	  data:{
		postUUID:postUUID,
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
	  		showPostlist(json.post,json.user,json.register,json.postPraise,json.myUserUUID,json.myAdmin);
	  		hotsCommentlist(json.hotsCommentlist,json.myAdmin);
	  		allCommentlist(json.allCommentlist,json.myAdmin)
	  		//showPostlist(json.admin,json.postList,json.postAllNum,json.allPage,json.pageIndex);
	  		//alert(JSON.stringify(json));
	  	}
	  	
	  }
	});
}
function showPostlist(post,user,register,postPraise,myUserUUID,myAdmin){
	var html="";
	html+='<button class="btn  btn-success btn-sm" type="button" onclick="ADD_COM()">评论</button>';
	if(myAdmin=="1" && returnpage.trim()!="myPost" && returnpage.trim()!="myCom"){
		html+='<button class="btn  btn-danger btn-sm" type="button" onclick="DELETE_COM()">删除</button>';
	}
	html+='<button class="btn btn-default btn-sm" type="button" onclick="returnPostList()">返回</button>';
		
	$("#comAddAndDeleteDiv").html(html);
	html="";
	html+='<img src="'+register.regPhoto+'" style="whith:80px;height:80px"/>';
	
	
	
	$("#postPhoto").html(html);
	
	html="";
	html+='<table>';
	html+=' <tr>';
	html+='  <td>主题：'+post.postTitle+'</td>';
	html+=' </tr>';
	html+=' <tr>';
	html+='  <td>发帖人：'+user.userName+'</td>';
	html+='	</tr>';
	html+=' <tr>';
	html+='  <td>发帖人注册时间:'+register.regTime.substring(0,19)+'</td>';
	html+=' </tr>';
	html+=' <tr>';
	html+='  <td>发帖时间：'+post.postTime.substring(0,19)+'</td>';
	html+=' </tr>';
	html+='</table>';

	$("#postInfo").html(html);
	
	html="";
	html+=post.postText;
	$("#postText").html(html);
	
	html="";
	var pr=0;
	for(var i=0;i<postPraise.length;i++){
		if(myUserUUID==postPraise[i].userUUID){
			pr=1;
			break;
		}
	}


	html+='<audio src="'+post.postAudio+'" controls="controls" style="height:20px"></audio>&nbsp;|&nbsp;<a id="praisecNum" href="javascript:void(0);" onclick="praiseclick(\''+post.postUUID+'\',\''+pr+'\',\''+post.postAudio+'\')">赞：</a> '+postPraise.length;

	
	$("#postAtt").html(html);

}

function allCommentlist(allCommentlist,admin){
	var html="";
	
	html+='<div class="card-header" style="background-color:#17a2b8">';
	html+='<h3 class="card-title">全部评论</h3>';
	html+='</div>';

	for(var i=0;i<allCommentlist.length;i++){
		

	html+='<div class="row" >';
	html+=	'<div class="form-inline col-sm-12">';
	html+=		'<div>';
	html+=		'<img src="'+allCommentlist[i].regPhoto+'" style="whith:80px;height:80px"/>';
	html+=		'</div>';
	html+=		'<div>';
	html+=			'<table>';
	html+=	 			'<tr>';
	html+=	  				'<td>评论人：'+allCommentlist[i].userName+'</td>';
	html+=				'</tr>';
	html+=				'<tr>';
	html+=					'<td>评论人注册时间:'+allCommentlist[i].regTime.substring(0,19)+'</td>';
	html+=				'</tr>';
	html+=				'<tr>';
	html+=					'<td>评论时间：'+allCommentlist[i].cmTime.substring(0,19)+'</td>';
	html+=	 			'</tr>';
	if(admin=="1" && returnpage.trim()!="myPost" && returnpage.trim()!="myCom"){
		html+=	 			'<tr>';
		html+=					'<td>删除选中：<input  name="DELETE_CHECK_NAME" type="checkbox" value="'+allCommentlist[i].cmUUID+'"></td>';
		html+=	 			'</tr>';
	}

	html+=			'</table>';
	html+=		'</div>';
	html+=	'</div>';
	html+=	'<div class="col-sm-12">';
			
	html+=	allCommentlist[i].cmText;
	html+=	'</div>';

	html+=	'<div class="col-sm-12" >';

	html+=		'<audio src="'+allCommentlist[i].cmAudio+'" controls="controls" style="height:20px"></audio>&nbsp;|&nbsp;<a id="praisecNum" href="javascript:void(0);" onclick="hotsPraiseClick(\''+allCommentlist[i].postUUID+'\',\''+allCommentlist[i].cmUUID+'\')">赞：</a> '+allCommentlist[i].cmPrNum;
	html+=	'</div>';
	html+='</div>';
	html+='<hr>';
	}
		$("#example3_wrapper").html(html);	
}
function hotsCommentlist(hotsCommentlist,admin){
	var html="";
	
html+='<div class="card-header" style="background-color:#17a2b8">';
html+='<h3 class="card-title">热门评论</h3>';
html+='</div>';

for(var i=0;i<hotsCommentlist.length;i++){
	

html+='<div class="row" >';
html+=	'<div class="form-inline col-sm-12">';
html+=		'<div>';
html+=		'<img src="'+hotsCommentlist[i].regPhoto+'" style="whith:80px;height:80px"/>';
html+=		'</div>';
html+=		'<div>';
html+=			'<table>';
html+=	 			'<tr>';
html+=	  				'<td>评论人：'+hotsCommentlist[i].userName+'</td>';
html+=				'</tr>';
html+=				'<tr>';
html+=					'<td>评论人注册时间:'+hotsCommentlist[i].regTime.substring(0,19)+'</td>';
html+=				'</tr>';
html+=				'<tr>';
html+=					'<td>评论时间：'+hotsCommentlist[i].cmTime.substring(0,19)+'</td>';
html+=	 			'</tr>';
if(admin=="1" && returnpage.trim()!="myPost" && returnpage.trim()!="myCom"){
	html+=	 			'<tr>';
	html+=					'<td>删除选中：<input  name="DELETE_CHECK_NAME" type="checkbox" value="'+hotsCommentlist[i].cmUUID+'"></td>';
	html+=	 			'</tr>';
}

html+=			'</table>';
html+=		'</div>';
html+=	'</div>';
html+=	'<div class="col-sm-12">';
		
html+=	hotsCommentlist[i].cmText;
html+=	'</div>';

html+=	'<div class="col-sm-12">';

html+=		'<audio src="'+hotsCommentlist[i].cmAudio+'" controls="controls" style="height:20px"></audio>&nbsp;|&nbsp;<a id="praisecNum" href="javascript:void(0);" onclick="hotsPraiseClick(\''+hotsCommentlist[i].postUUID+'\',\''+hotsCommentlist[i].cmUUID+'\')">赞：</a> '+hotsCommentlist[i].cmPrNum;
html+=	'</div>';
html+='</div>';
html+='<hr>';
}
	$("#example2_wrapper").html(html);
}

function hotsPraiseClick(postUUID,cmUUID){
	
	$.ajax({
	    type: "POST",
	    url: "/postbar/commentController/commentPraise",
	    async: true,
	    data:{
	    	postUUID:postUUID,
	    	cmUUID:cmUUID
	   	 },
	    dataType: "json",
	    error: function (XMLHttpRequest, textStatus, errorThrown) {
	    	$.MsgBox.Alert("消息","出错了，请和管理员联系");
	    },
	    success: function (json) {
	    
	    	if(json.message!=""){
	    		window.parent.location.replace("/postbar/");
	    	}else{
	    		if(json.praiseMessage!=""){
	    			$.MsgBox.Alert("消息",json.praiseMessage)
	    		}else{
	    			hotsCommentlist(json.hotsCommentlist,json.myAdmin)
	    	  		allCommentlist(json.allCommentlist,json.myAdmin)
	    		}
	    		
	    	}
	    }
	});
}
function addComCheck(){
var cmText=editor.html().trim();

	if(cmText==""){
		$("#tishi").html("评论内容不能为空");
		return;
	}

	ajaxLoading();
	$.ajax({
	    type: "POST",
	    url: "/postbar/commentController/addCom",
	    async: true,
	    data:{
	    	cmText:cmText,
	    	postUUID:postUUID
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
	    		window.parent.layui.use(["jqtab"],function(){
	    			var funTab =  window.parent.$("#funTab");
	    			menutab = funTab.btab();
	    			menutab.removeOther();
	    		});
	    		window.location.replace("/postbar/chinasofti/comment/index.html?page="+returnpage+"&postid="+postUUID);
	    		
	    	}
	    }
	});
	
}
function returnComList(){
	editor.html("");
	$("#tishi").html("");
	$("#COM_LIST_DIV_ID").attr("style","display:block;");//隐藏div
	$("#COM_ADD_DIV_ID").attr("style","display:none;");//隐藏div
}
function ADD_COM(){
	 $("#COM_LIST_DIV_ID").attr("style","display:none;");//隐藏div
	 $("#COM_ADD_DIV_ID").attr("style","display:block;");//隐藏div
}
function praiseclick(postUUID,pr,postAudio){
	if(pr=="0"){
		$.ajax({
			  type: "POST",
			  url: "/postbar/commentController/commentPostPraise",
			  async: true,
			  data:{
				postUUID:postUUID
			  },
			  dataType: "json",
			  error: function (XMLHttpRequest, textStatus, errorThrown) {
				  $.MsgBox.Alert("消息","出错了，请于管理员联系");
			  },
			  success: function (json) {
			
			  	if(json.message!=""){
			  		window.parent.location.replace("/postbar/");
			  	}else{
			  		var html='<audio src="'+postAudio+'" controls="controls" style="height:20px"></audio>&nbsp;|&nbsp;<a id="praisecNum" href="javascript:void(0);" onclick="praiseclick(\''+postUUID+'\',\''+1+'\',\''+postAudio+'\')">赞：</a> '+json.praiseNum;

			  		
			  		$("#postAtt").html(html);
			  		//showPostlist(json.admin,json.postList,json.postAllNum,json.allPage,json.pageIndex);
			  		//alert(JSON.stringify(json));
			  	}
			  	
			  }
			});
		
		
	}else if(pr==1){
		$.MsgBox.Alert("消息","您已点过了赞");
	}
}
function DELETE_COM(){
	var chk_value =[]; 
    $('input[name="DELETE_CHECK_NAME"]:checked').each(function(){ 
        chk_value.push($(this).val()); 
    }); 
    if(chk_value.length==0){
    	$.MsgBox.Alert("消息","请先选择需要删除的评论！");
    	return;
    }
    
    $.ajax({
	    type: "POST",
	    url: "/postbar/commentController/deleteComment",
	    async: true,
	    data:{
	    	cmUUID:chk_value
	   	 },
	    dataType: "json",
	    error: function (XMLHttpRequest, textStatus, errorThrown) {
	    	$.MsgBox.Alert("消息","出错了，请和管理员联系");
	    },
	    success: function (json) {
	    
	    	if(json.message!=""){
	    		window.parent.location.replace("/postbar/");
	    	}else{
	    		window.parent.layui.use(["jqtab"],function(){
	    			var funTab =  window.parent.$("#funTab");
	    			menutab = funTab.btab();
	    			menutab.removeOther();
	    		});
	    		window.location.replace("/postbar/chinasofti/comment/index.html?page="+returnpage+"&postid="+postUUID);
	    		
	    	}
	    }
	});
}
function returnPostList(){
	if(returnpage.trim()=="post"){
		window.location.replace("/postbar/chinasofti/post/index.html");
	}else if(returnpage.trim()=="myPost"){
		window.location.replace("/postbar/chinasofti/myPost/index.html");
	}else if(returnpage.trim()=="myCom"){
		window.location.replace("/postbar/chinasofti/myComment/index.html");
	}
}