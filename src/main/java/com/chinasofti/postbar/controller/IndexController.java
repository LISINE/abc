package com.chinasofti.postbar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    //实现根目录跳转页面
    @RequestMapping("/")
    public ModelAndView toIndex(){
        ModelAndView md=new ModelAndView();
        //设置视图
        md.setViewName("chinasofti/login/login.html");
        return  md;
    }
}
