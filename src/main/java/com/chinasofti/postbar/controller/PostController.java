package com.chinasofti.postbar.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.aip.speech.TtsResponse;
import com.chinasofti.postbar.basic.controller.BasicController;
import com.chinasofti.postbar.pojo.AppDto;
import com.chinasofti.postbar.pojo.AudioDto;
import com.chinasofti.postbar.pojo.PostDto;
import com.chinasofti.postbar.service.AudioService;
import com.chinasofti.postbar.service.PostService;
import com.chinasofti.postbar.util.AudioSynthesis;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/postController")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class PostController extends BasicController {
	@Autowired
	private PostService postservice;
	// 注入文章的Service
	@Autowired
	private PostService postService;
	// 注入语音service
	@Autowired
	private AudioService audioService;

	// 获取文章列表
	@RequestMapping("/getPostList")
	public void getPostList(String postTitle, Integer pageIndex, Integer everyPageDataCount, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		json.put("message", "");
		if (sessionTimeout(request)) {
			json.put("message", "页面过期，请重新登录");
		} else {
			/*
			 * 0.初始化要传给前端的值 1.查询总记录数，计算总页数，判断当前页数是否合法 2.查询当前页数，返回list集合
			 */
			json.put("postAllNum", 0);
			json.put("allPage", 0);
			json.put("pageIndex", 0);
			json.put("postList", "");
			json.put("admin", ((String) session.getAttribute("admin")));
			int postAllNum = postservice.getPostAllNum(postTitle, null);
			System.out.println("查询出的所有文章" + postAllNum);
			json.put("postAllNum", postAllNum);
			// 根据文章总数计算页数
			if (postAllNum > 0) {
//					// 总页数变量
				int allPage = 1;
				// 计算总页数
				allPage = (postAllNum % everyPageDataCount == 0) ? (postAllNum / everyPageDataCount)
						: (postAllNum / everyPageDataCount + 1);
				// 防止页码越界
				if (pageIndex < 0) {
					pageIndex = 0;
				} else if (pageIndex >= allPage) {
					pageIndex = allPage - 1;
				}
				json.put("allPage", allPage);
				json.put("pageIndex", pageIndex);
				// 查询所有文章所以uuid为null，但是查询我的文章时就需要uuid
				List<PostDto> list = postservice.selectPostList(postTitle, pageIndex * everyPageDataCount,
						everyPageDataCount, null);
				json.put("postList", list);
				System.out.println("当页文章数" + postAllNum + "查到的" + list);
			}
		}

		this.writeJson(json.toString(), response);
	}
	//获取最新的文章列表
	@RequestMapping("/getLastPostList")
	public void getLastPostList( HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		json.put("message", "");
		if (sessionTimeout(request)) {
			json.put("message", "页面过期，请重新登录");
		} else {

			List<PostDto>  lastPost = postservice.getLastPostList();
			System.out.println("查询出的最新文章" + lastPost);
			json.put("postList", lastPost);
			}
		this.writeJson(json.toString(), response);
	}
	// 文章添加
	@RequestMapping("/addPost")
	public void addPost(HttpServletResponse response, HttpServletRequest request, String postTitle, String postText) {
		// 1.构建一个json对象
		JSONObject json = new JSONObject();
		json.put("message", "");
		// 创建一个session对象
		HttpSession session = request.getSession();
		// 判断页面是否过期
		if (sessionTimeout(request)) {
			json.put("message", "页面过期,请重新登录!");
		} else {
			// 1.查询当前登录用户的语音 用于合成
			// 2.需要把富文本编辑器中的文字取出来 去除掉无关标签
			// 3.根据语音 和生成的文本 向百度发送字符串 百度会根据你的发送的字符串会以流的方式返回给你一个语音文件

			String postUUID = this.getUUID();
			String userUUID = (String) session.getAttribute("id");
			// Date postTime=this.getDate();
			String postTime = this.getStringDate(new Date());
			int postPageviews = 0;
			// 定义一个文章对象 并设置值
			PostDto postDto = new PostDto();
			postDto.setPostPageviews(postPageviews);
			postDto.setPostText(postText);
			postDto.setPostTime(postTime);
			postDto.setPostTitle(postTitle);
			postDto.setPostUUID(postUUID);
			postDto.setUserUUID(userUUID);

			// 合成语音 并向页面写数据 removeHtml() 里面使用正则表达式 去除不符合规则的标签返回文本
			String text = this.removeHtmlTag(postText);
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
			postDto.setPostAudio(urlPath);
			// 添加文章方法
			postService.addPost(postDto);

		}

		this.writeJson(json.toString(), response);
	}

	@RequestMapping(value = "/kindEditorImgInput")
	public void kindEditorImgInput(HttpServletResponse response, HttpServletRequest request, String dir,
			String localUrl, MultipartFile imgFile) {

		JSONObject json = new JSONObject();

		boolean strError = true;

		// 文件保存目录路径
		String savePath = request.getServletContext().getRealPath("/") + "kindeditorImage/";
		// 文件保存目录URL
		String saveUrl = request.getContextPath() + "/kindeditorImage/";

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		// extMap.put("flash", "swf,flv");
		// extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		// extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		// 最大文件大小
		long maxSize = 2000000;
		File uploadDir = new File(savePath);
		String dirName = dir;
		if (dirName == null) {
			dirName = "image";
		}
		if (!uploadDir.exists() && !uploadDir.isDirectory()) {// 检查目录
			uploadDir.mkdirs();
		}

		if (!ServletFileUpload.isMultipartContent(request)) {
			json.put("error", 1);
			json.put("message", "请选择文件。");
		} else if (!uploadDir.canWrite()) {// 检查目录写权限
			json.put("error", 1);
			json.put("message", "上传目录没有写权限。");

		} else if (!extMap.containsKey(dirName)) {
			json.put("error", 1);
			json.put("message", "目录名不正确。");
		} else {

			// 创建文件夹
			savePath += dirName + "/";
			saveUrl += dirName + "/";
			File saveDirFile = new File(savePath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());
			savePath += ymd + "/";
			saveUrl += ymd + "/";
			File dirFile = new File(savePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			if (imgFile != null && !imgFile.toString().equals("")) {
				long fileSize = imgFile.getSize();
				if (fileSize > maxSize) {
					json.put("error", 1);
					json.put("message", "上传文件大小超过限制。");
				} else {
					// 检查扩展名
					String fileExt = localUrl.substring(localUrl.lastIndexOf(".") + 1).toLowerCase();
					if (!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)) {
						json.put("error", 1);
						json.put("message", "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
					} else {
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
						String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;

						File uploadedFile = new File(savePath, newFileName);
						// 获取文件输出流
						FileOutputStream fos = null;
						// 获取内存中当前文件输入流
						InputStream in = null;
						byte[] buffer = new byte[1024];
						try {
							// 获取文件输出流
							fos = new FileOutputStream(uploadedFile);
							// 获取内存中当前文件输入流
//					              in = new FileInputStream(imgFile);
							in = imgFile.getInputStream();
							int num = 0;
							while ((num = in.read(buffer)) > 0) {
								fos.write(buffer, 0, num);
							}
						} catch (Exception e) {
							strError = false;
							json.put("error", 1);
							json.put("message", "上传文件失败。");
						} finally {
							try {
								in.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								fos.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (strError) {
							json.put("error", 0);
							json.put("url", saveUrl + newFileName);
						}
					}
				}
			} else {
				strError = false;
				json.put("error", 1);
				json.put("message", "上传文件的文件不存在。");
			}
		}
		this.writeJson(json.toString(), response);
	}

	@RequestMapping(value = "/deletePost")
	// @RequestBody 把传来的的json封装成Java对象 @responsebody 把java对象封装成json
	public void deletePost(HttpServletResponse response, HttpServletRequest request,
			@RequestParam("postUUID[]") String[] postUUID) {
		JSONObject json = new JSONObject();
		json.put("message", "");
		if (sessionTimeout(request)) {
			json.put("message", "页面过期，请重新登录");
		} else {
			postservice.deletePost(postUUID);
		}
		this.writeJson(json.toString(), response);
	}

}
