package com.fan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fanronghe
 * @Date 21.9.18 下午 11:48
 */
//前后端未分离版本的处理器的注解@Controller   响应数据源是文件
//前后端分离版本的处理器的注解@RestController 响应数据源是json串
@Controller
public class ItemController {

    /**
     * 仅匹配get请求且请求路径为/item的请求
     * @param request 请求对象
     * @param response 响应对象
     * @return static(库目录)下的文件路径
     */
    @GetMapping( "/item" )
    public String item( HttpServletRequest request, HttpServletResponse response ){
        return "/item.html"; //相当于response.setEntity( "/item.html" ); 数据源为一个文件
    }

}
