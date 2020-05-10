var everyPageDataCount=7;
var postPageIndex=0;
var postAllPage=0;
$(document).ready(function () {
	KindEditor.options.cssData = 'body {font-family:微软雅黑;}',
	editor = KindEditor.create('textarea[id="POST_ADD_DES"]', {
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
	
	
	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,0,everyPageDataCount,true,"/postbar/postController/getPostList");
	
});
function getPostList(postTitle,pageIndex,everyPageDataCount,SynOrAsyn,url){
	$.ajax({
	  type: "POST",
	  url: url,
	  async: SynOrAsyn,
	  data:{
		postTitle:postTitle,
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
	  		showPostlist(json.admin,json.postList,json.postAllNum,json.allPage,json.pageIndex);
	  		//alert(JSON.stringify(json));
	  	}
	  	
	  }
	});
}

function returnPostList(){
	$("#POST_ADD_TITLE").val("");
	editor.html("");
	$("#tishi").html("");
	$("#POST_LIST_DIV_ID").attr("style","display:block;");//隐藏div
	$("#POST_ADD_DIV_ID").attr("style","display:none;");//隐藏div
}

function ADD_POST(){
	 $("#POST_LIST_DIV_ID").attr("style","display:none;");//隐藏div
	 $("#POST_ADD_DIV_ID").attr("style","display:block;");//隐藏div
}



function addPostCheck(){
	var title=$("#POST_ADD_TITLE").val().trim();
	var text=editor.html().trim();
	
	if(title==""){
		$("#tishi").html("文章标题不能为空");
		return;
	}
	if(title.length>16){
		$("#tishi").html("文章标题最多16个字符");
		return;
	}
	if(text==""){
		$("#tishi").html("文章内容不能为空");
		return;
	}
	ajaxLoading();
	$.ajax({
	    type: "POST",
	    url: "/postbar/postController/addPost",
	    async: true,
	    data:{
	    	postTitle:title,
	    	postText:text
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
	    		returnPostList();
	    		var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	    		getPostList(searchNameVal,postPageIndex,everyPageDataCount,true,"/postbar/postController/getPostList");
	    		window.parent.layui.use(["jqtab"],function(){
	    			var funTab =  window.parent.$("#funTab");
	    			menutab = funTab.btab();
	    			menutab.removeOther();
	    		});
	    	}
	    }
	});

}

function showPostlist(admin,postList,postAllNum,allPage,pageIndex){
	postPageIndex=pageIndex;
	postAllPage=allPage;
	var html=""
	html+='<button class="btn  btn-success btn-sm" type="button" onclick="ADD_POST()">新增</button>&nbsp;&nbsp';
	if(admin=="1"){
		html+='<button class="btn  btn-danger btn-sm" type="button" onclick="DELETE_POST()">删除</button>'
	}
	
	
	$("#postAddAndDeleteDiv").html(html);
	html=""
	for(var i=0;i<postList.length;i++){
			html+='<tr bgcolor="#FFFFFF">';
			html+='		<td align="center" width="20">';
			if(admin=="1"){
				html+='			<input name="DELETE_CHECK_NAME" type="checkbox" value="'+postList[i].postUUID+'">';
			}
			html+='		</td>';
		
		
		
		html+='		<td valign="center" align="center" width="30">';
		html+=			postList[i].postPageviews;
		html+='		</td>';
		html+='		<td valign="center" align="center" width="30">';
		html+=			postList[i].commentNum;
		html+='		</td>';
		html+='		<td valign="center" align="center" width="110">';
		html+='			<a href="" onclick="post_detailed(\''+postList[i].postUUID+'\'); return false;">'+postList[i].postTitle+'</a> ';
		html+='		</td>';
		html+='		<td valign="center" align="center" width="110">';
		html+=			postList[i].userName;
		html+='		</td>';
		html+='		<td valign="center" align="center" width="100">';
		html+=			postList[i].postTime.substring(0,19) ;
		html+='		</td>';
		html+='		<td valign="center" align="center" width="100">';
		html+=          postList[i].cmTime.substring(0,19) ;
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
	getPostList(searchNameVal,postPageIndex+1,everyPageDataCount,true,"/postbar/postController/getPostList");
}

function GOTO_POST_TAIL_PAGE(){
	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,postAllPage-1,everyPageDataCount,true,"/postbar/postController/getPostList");
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
	getPostList(searchNameVal,jumpVal-1,everyPageDataCount,true,"/postbar/postController/getPostList");
}


function GOTO_POST_HOME_PAGE(){
	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,0,everyPageDataCount,true,"/postbar/postController/getPostList");
}

function GOTO_POST_PREVIOUS_PAGE(){
	var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	getPostList(searchNameVal,postPageIndex-1,everyPageDataCount,true,"/postbar/postController/getPostList");
	 
}
function searchByPostName(){
	var searchNameVal=$("#SEARCH_POST_NAME").val().trim();
	getPostList(searchNameVal,0,everyPageDataCount,true,"/postbar/postController/getPostList");
}

function post_detailed(postUUID){

	window.location.replace("/postbar/chinasofti/comment/index.html?page=post&postid="+postUUID);

}

function DELETE_POST(){
	var chk_value =[]; 
    $('input[name="DELETE_CHECK_NAME"]:checked').each(function(){ 
        chk_value.push($(this).val()); 
    }); 
    if(chk_value.length==0){
    	$.MsgBox.Alert("消息","请先选择需要删除的文章");
    	return;
    }
    
    $.ajax({
	    type: "POST",
	    url: "/postbar/postController/deletePost",
	    async: true,
	    data:{
	    	postUUID:chk_value
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
	    		var searchNameVal=$("#SEARCH_POST_NAME_HIDDEN").val().trim();
	    		getPostList(searchNameVal,0,everyPageDataCount,true,"/postbar/postController/getPostList");
	   
	    	}
	    }
	});
}



