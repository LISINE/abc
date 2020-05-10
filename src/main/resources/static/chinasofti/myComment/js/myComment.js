var everyPageDataCount=10;
var postPageIndex=0;
var postAllPage=0;

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
	
	getPostList(true,"/postbar/myCommentController/selectMyCommentByUserUUID",0,everyPageDataCount);
});


function getPostList(SynOrAsyn,url,pageIndex,everyPageDataCount){
	ajaxLoading();
	$.ajax({
	  type: "POST",
	  url: url,
	  async: SynOrAsyn,
	  data:{
		  pageIndex:pageIndex,
		  everyPageDataCount:everyPageDataCount
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

	  		allCommentlist(json.myCommentlist,json.postAllNum,json.allPage,json.pageIndex)

	  	}
	  	
	  }
	});
}


function allCommentlist(allCommentlist,postAllNum,allPage,pageIndex){
	postPageIndex=pageIndex;
	postAllPage=allPage;
	var html="";
	
	
	html+='<button class="btn  btn-danger btn-sm" type="button" onclick="DELETE_COM()">删除</button>';

	
		
	$("#comAddAndDeleteDiv").html(html);
	
	html="";
	html+='<div class="card-header" style="background-color:#17a2b8">';
	html+='<h3 class="card-title">我的评论</h3>';
	html+='</div>';

	for(var i=0;i<allCommentlist.length;i++){
		

	html+='<div class="row" >';
	html+=	'<div class="form-inline col-sm-12">';
	html+=	allCommentlist[i].cmText;

	html+=	'</div>';
	html+=	'<div class="col-sm-12" >';

	html+=		'<audio src="'+allCommentlist[i].cmAudio+'" controls="controls" style="height:20px"></audio>';
	html+=	'</div>';
	html+=	'<div class="col-sm-12">';

	html+=		'<div>';
	html+=			'<table>';
	html+=				'<tr>';
	html+=					'<td>评论时间：'+allCommentlist[i].cmTime.substring(0,19)+'&nbsp;&nbsp;|&nbsp;&nbsp;</td>';
	html+=					'<td>评论文章：<a href="" onclick="post_detailed(\''+allCommentlist[i].postUUID+'\'); return false;">'+allCommentlist[i].postTitle+'</a> &nbsp;&nbsp;|&nbsp;&nbsp;</td>';
	html+=					'<td><a href="" onclick="EDIT_COM(\''+allCommentlist[i].cmUUID+'\'); return false;">评论编辑</a> ：&nbsp;&nbsp;|&nbsp;&nbsp;</td>';
	html+=                  '<td>删除评论：<input  name="DELETE_CHECK_NAME" type="checkbox" value="'+allCommentlist[i].cmUUID+'"></td>';
	html+=	 			'</tr>';
	html+=			'</table>';
	html+=		'</div>';
	html+=	'</div>';


	html+='</div>';
	html+='<hr>';
	html+='<hr>';
	}
	$("#example3_wrapper").html(html);	
	
	
	
	
	
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

function post_detailed(postUUID){

	window.location.replace("/postbar/chinasofti/comment/index.html?page=myCom&postid="+postUUID);

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
	    		getPostList(true,"/postbar/myCommentController/selectMyCommentByUserUUID",0,everyPageDataCount);
	    		
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
function EDIT_COM(cmUUID){
	$.ajax({
	    type: "POST",
	    url: "/postbar/myCommentController/getCommentByCmUUID",
	    async: true,
	    data:{
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
	    		editor.html(json.cmText);
	    		var html="";
	    		html+='<button type="button" class="btn btn-info" onclick="editComCheck(\''+cmUUID+'\')">编辑</button>';
	    		html+='<button type="button" class="btn btn-default" onclick="returnComList()">返回</button>';
	    		$("#editButtion").html(html);
	    		
	    	}
	    }
	});
	
	
    
    
	$("#COM_LIST_DIV_ID").attr("style","display:none;");//隐藏div
	$("#COM_ADD_DIV_ID").attr("style","display:block;");//隐藏div
	
}
function GOTO_POST_NEXT_PAGE(){

	getPostList(true,"/postbar/myCommentController/selectMyCommentByUserUUID",postPageIndex+1,everyPageDataCount);
	
}

function GOTO_POST_TAIL_PAGE(){
	getPostList(true,"/postbar/myCommentController/selectMyCommentByUserUUID",postAllPage-1,everyPageDataCount);

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
	getPostList(true,"/postbar/myCommentController/selectMyCommentByUserUUID",jumpVal-1,everyPageDataCount);
}


function GOTO_POST_HOME_PAGE(){
	getPostList(true,"/postbar/myCommentController/selectMyCommentByUserUUID",0,everyPageDataCount);
}

function GOTO_POST_PREVIOUS_PAGE(){
	getPostList(true,"/postbar/myCommentController/selectMyCommentByUserUUID",postPageIndex-1,everyPageDataCount);
	 
}

function editComCheck(cmUUID){
	var cmText=editor.html().trim();

	if(cmText==""){
		$("#tishi").html("评论内容不能为空");
		return;
	}
	
	ajaxLoading();
	$.ajax({
	    type: "POST",
	    url: "/postbar/myCommentController/editCom",
	    async: true,
	    data:{
	    	cmText:cmText,
	    	cmUUID:cmUUID
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
	    		window.location.replace("/postbar/chinasofti/myComment/index.html?radm="+Math.random());
	    		
	    	}
	    }
	});
	
}
