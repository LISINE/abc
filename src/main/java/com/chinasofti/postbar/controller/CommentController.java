package com.chinasofti.postbar.controller;

import com.baidu.aip.speech.TtsResponse;
import com.chinasofti.postbar.basic.controller.BasicController;
import com.chinasofti.postbar.pojo.*;
import com.chinasofti.postbar.service.AudioService;
import com.chinasofti.postbar.service.CommentService;
import com.chinasofti.postbar.service.LoginService;
import com.chinasofti.postbar.service.PostService;
import com.chinasofti.postbar.util.AudioSynthesis;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/commentController")
public class CommentController extends BasicController {
	@Autowired
  private CommentService commentservice;
	@Autowired
	private LoginService loginService;
	@Autowired
	private PostService postService;
	@Autowired
	private AudioService audioService;
	
	@RequestMapping("/getInit")
	public void getInit(String postUUID,HttpServletResponse response,HttpServletRequest request) {
		HttpSession session=request.getSession();
		JSONObject json=new JSONObject();
		json.put("message", "");
		json.put("myAdmin",(String)session.getAttribute("admin"));
		json.put("myUserUUID", (String)session.getAttribute("id"));
		json.put("postPraise", "");
		json.put("register", "");
		json.put("user", "");
		json.put("post", "");
		json.put("hotsCommentlist", "");
		json.put("allCommentlist", "");
		if(sessionTimeout(request)){
			json.put("message", "页面过期，请重新登录");
		}else{
			PostDto post=postService.getPostByuuid(postUUID);
			json.put("post", post);
			//阅读数+1
			int pageviews=postService.getPageViews(postUUID);
			json.put("pageviews", pageviews);
			UserDto user=loginService.getUserByUuid((String)session.getAttribute("id"));
			json.put("user", user);
			RegisterDto register=loginService.getRegisterByUuid((String)session.getAttribute("id"));
			json.put("register", register);
			List<PraiseDto> postPraise=commentservice.getPostpraise(postUUID);
			json.put("postPraise",postPraise);
			List<CommentDto> hotsCommentlist=commentservice.getHotlistByPostuuid(postUUID);
			json.put("hotsCommentlist",hotsCommentlist);
			List<CommentDto> allCommentlist=commentservice.getAllListByuuid(postUUID);
			json.put("allCommentlist", allCommentlist);
			System.out.println("查询评论全部参数："+json.toString());
		}

		 this.writeJson(json.toString(), response);
	}
	//删除评论
	@RequestMapping("/deleteComment")
	public void deleteComment(HttpServletResponse response, HttpServletRequest request,
			@RequestParam("cmUUID[]") String[] cmUUID) {
		JSONObject json = new JSONObject();
		json.put("message", "");
		if (sessionTimeout(request)) {
			json.put("message", "页面过期，请重新登录");
		} else {
			commentservice.deleteComment(cmUUID);
		}
		this.writeJson(json.toString(), response);
	}
	//点赞
	@RequestMapping("/commentPraise")
     public void commentPraise(HttpServletResponse response, HttpServletRequest request,String postUUID,String cmUUID) {
			HttpSession session=request.getSession();
			JSONObject json=new JSONObject();
			json.put("message", "");
			json.put("praiseMessage", "");
			json.put("hotsCommentlist", "");
			json.put("allCommentlist", "");
			json.put("myAdmin",(String)session.getAttribute("admin"));
			if(sessionTimeout(request)){
				json.put("message", "页面过期，请重新登录");
			}else{
				System.out.println("添加评论点赞的参数postUUID"+postUUID);
				String  userUUID=(String)session.getAttribute("id");
				int isCommentPraise=commentservice.getCommentPraise(postUUID,cmUUID,userUUID);
				if(isCommentPraise>0) {
					json.put("praiseMessage", "您已点过赞了");
				}else {
					String cmPrUUID=this.getUUID();
					commentservice.addCommentPraise(cmPrUUID,postUUID,cmUUID,userUUID);
					List<CommentDto> hotsCommentlist=commentservice.getHotlistByPostuuid(postUUID);
				    json.put("hotsCommentlist",hotsCommentlist); 
				    List<CommentDto> allCommentlist=commentservice.getAllListByuuid(postUUID);
				    json.put("allCommentlist", allCommentlist);
				    System.out.println("点赞参数："+json.toString());
				}
			}
		 this.writeJson(json.toString(), response);
	}
	//添加评论
	@RequestMapping("/addCom")
	public void addCom(HttpServletResponse response, HttpServletRequest request, String postUUID, String cmText) {
				JSONObject json = new JSONObject();
				json.put("message", "");
				HttpSession session = request.getSession();
				if (sessionTimeout(request)) {
					json.put("message", "页面过期,请重新登录!");
				} else {
					String cmUUID = this.getUUID();
					String userUUID = (String) session.getAttribute("id");
					String cmTime = this.getStringDate(new Date());
					CommentDto commentDto=new CommentDto();
					commentDto.setCmTime(cmTime);
					commentDto.setPostUUID(postUUID);
					commentDto.setCmUUID(cmUUID);
					commentDto.setUserUUID(userUUID);
					commentDto.setCmText(cmText);
					System.out.println("得到的评论数据："+commentDto.toString());
					// 合成语音 并向页面写数据 removeHtml() 里面使用正则表达式 去除不符合规则的标签返回文本
					String text = this.removeHtmlTag(cmText);
					if (text.length() > 1024) {
						text = text.substring(0, 1024);
					}
					// 去数据库查询当前用户的语音设置
					AudioDto audioDto = audioService.getAudioByUserUuid(userUUID);

					int spd = audioDto.getAuSetSpd();
					int pit = audioDto.getAuSetPit();
					int vol = audioDto.getAuSetVol();
					int per = audioDto.getAuSetVoiPer();
					// 封装百度公司提供的语音合成参数
					AppDto appDto = postService.selectApp();
					String APP_ID = appDto.getAppID();
					String API_KEY = appDto.getApiKey();
					String SECRET_KEY = appDto.getSecretKey();

					// 调用百度api合成语音
					AudioSynthesis audioSynthesis = AudioSynthesis.getInstance(APP_ID, API_KEY, SECRET_KEY);
					TtsResponse res = audioSynthesis.synthesis(text, spd, pit, vol, per);
					// 百度服务器响应合成的语音字节流
					byte[] data = res.getData();

					String path = request.getServletContext().getRealPath("/") + "audio\\";

					String urlPath = request.getContextPath() + "/audio/" + postUUID + ".mp3";
					System.out.println("语音合成的地址：" + urlPath);
					File uploadDir = new File(path);
					if (!uploadDir.exists() && !uploadDir.isDirectory()) {// 检查目录
						uploadDir.mkdirs();
					}
					path += postUUID + ".mp3";

					OutputStream out = null;
					InputStream is = null;
					try {
						out = new FileOutputStream(path);
						is = new ByteArrayInputStream(data);
						byte[] buff = new byte[1024];
						int len = 0;
						while ((len = is.read(buff)) != -1) {
							out.write(buff, 0, len);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (is != null) {
							try {
								is.close();

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (out != null) {
							try {
								out.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
					commentDto.setCmAudio(urlPath);
					// 添加文章方法
					commentservice.addCom(commentDto);

				}

				this.writeJson(json.toString(), response);
	}
	//给文章点赞
	@RequestMapping("/commentPostPraise")
	public void commentPostPraise(HttpServletResponse response, HttpServletRequest request,String postUUID) {
		HttpSession session=request.getSession();
		JSONObject json=new JSONObject();
		json.put("message", "");
		json.put("praiseNum", "");
		if(sessionTimeout(request)){
			json.put("message", "页面过期，请重新登录");
		}else{
			System.out.println("给文章点赞的参数"+postUUID);
			String  userUUID=(String)session.getAttribute("id");
			int isCommentPraise=commentservice.getPostCommentPraise(postUUID,userUUID);
			if(isCommentPraise>0) {
				json.put("message", "您已点过赞了");
			}else {
				String poPrUUID=this.getUUID();
				commentservice.addPostCommentPraise(poPrUUID,postUUID,userUUID);
              int praiseNum=commentservice.getPostPraiseNum(postUUID);
          	json.put("praiseNum", praiseNum);
			    System.out.println("点赞参数："+json.toString());
			}
		}
	 this.writeJson(json.toString(), response);
	}
	}
