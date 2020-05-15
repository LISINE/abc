package com.chinasofti.postbar.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.postbar.basic.controller.BasicController;
import com.chinasofti.postbar.pojo.RegisterDto;
import com.chinasofti.postbar.service.LoginService;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

//上传头像
@Controller
@RequestMapping("/upLoadHeadController")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UpLoadHeadController extends BasicController{
	@Autowired 
	private LoginService loginService;

	@RequestMapping("/selectHeadURL")
	public void selectHeadURL(HttpServletResponse response,HttpServletRequest request) {
		// 1.构建一个json对象
				JSONObject json = new JSONObject();
				json.put("message", "");
				// 创建一个session对象
				HttpSession session = request.getSession();
				// 判断页面是否过期
				if (sessionTimeout(request)) {
					json.put("message", "页面过期,请重新登录!");
				} else {
					String userUUID = (String) session.getAttribute("id");
                    RegisterDto registerDto=loginService.getPhotoById(userUUID);
                    json.put("headURL", registerDto.getRegPhoto());
				}
             this.writeJson(json.toString(), response);
	}
	
	@RequestMapping("/editHead")
    public void editHead(String file,HttpServletRequest request,HttpServletResponse response ) {
        JSONObject json = new JSONObject();
        json.put("message", "");
        // 创建一个session对象
        HttpSession session = request.getSession();
        // 判断页面是否过期
        if (sessionTimeout(request)) {
            json.put("message", "页面过期,请重新登录!");
        } else {
            String userUUID = (String) session.getAttribute("id");
//			ServletContext context=session.getServletContext();
//			json.put("file", file);
            RegisterDto registerDto=new RegisterDto();

            String path = request.getServletContext().getRealPath("/") + "headphoto\\";

            String urlPath = request.getContextPath() + "/headphoto/" + userUUID + ".jpg";
            System.out.println("图片上传的地址：" + urlPath);
            File uploadDir = new File(path);
            if (!uploadDir.exists() && !uploadDir.isDirectory()) {// 检查目录
                uploadDir.mkdirs();
            }
            path += userUUID + ".jpg";

            OutputStream out = null;
            InputStream is = null;
            try {
                byte[] img  = new BASE64Decoder().decodeBuffer(file);
                for (int i = 0; i < img.length; ++i) {
                    if (img[i] < 0) {// 调整大小
                        img[i] += 256;

                    }}
                out = new FileOutputStream(path);
                is = new ByteArrayInputStream(img);
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = is.read(buff)) != -1) {
                    out.write(buff, 0, len);
                }
            } catch (IOException e) {
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

            loginService.updatePath(userUUID,urlPath);

        }this.writeJson(json.toString(), response);
    }}
