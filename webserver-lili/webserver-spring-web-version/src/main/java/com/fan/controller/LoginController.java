package com.fan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fanronghe
 * @Date 21.9.18 下午 11:49
 */
@Controller
public class LoginController {

    /**
     * 仅匹配post请求且请求路径为/login的请求
     * @param request 请求对象
     * @param response 响应对象
     */
    @PostMapping( "/login" )
    public void login( HttpServletRequest request, HttpServletResponse response ) {

        System.out.println( "请求方式:" + request.getMethod() );
        System.out.println( "请求路径:" + request.getRequestURI() );

        System.out.println( "请求参数username:" + request.getParameter( "username" ) );
        System.out.println( "请求参数password:" + request.getParameter( "password" ) );

        System.out.println( "消息头session:" + request.getHeader( "session" ) );
        System.out.println( "消息头cookie:" + request.getHeader( "cookie" ) );

        //==================================手动模拟@RquestBody开始工作==================================================
        //下面的过程用来模拟@RquestBody注解,将消息正文中的json字符串,转为Map<String,String>对象的过程
        Map<String, String> map_body = new HashMap<>();
        try {
            //当使用@RequesBody功能后会关闭流(Stream closed),此时获取的流就不可用了,所以二者是互斥的
            InputStream in = request.getInputStream();
            byte[] contentData = new byte[request.getContentLength()]; //直接为消息正文量身定制一个等容量的字节数组空间
            in.read( contentData ); //在刚开辟的字节数组空间中填入数据,即消息正文的数据
            if( "application/json".equals( request.getContentType() ) ){
                //这里的解码方式必须为UTF_8
                String json = new String( contentData, StandardCharsets.UTF_8 ); //字节数组转为json字符串

                //使用TypeReference对象来传入Map集合的k和v的类型,其中v是还可以嵌套一个map的
                //形成  new TypeReference<HashMap<String, HashMap<String, String>>>(){} 的结构
                map_body = new ObjectMapper().readValue( json, new TypeReference<HashMap<String, String>>() {} );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        //====================================@RquestBody结束工作========================================================

        System.out.println( "消息正文price:" + map_body.get( "price" ) );
        System.out.println( "消息正文number:" + map_body.get( "number" ) );


        Map<String, String> map = new HashMap<>();
        map.put( "code", "200" );
        map.put( "message", "ok" );
        map.put( "data", "这里是LILI服务器" );


        //===================================手动模拟@ResponseBody开始工作================================================
        //以下过程模拟@ResponseBody注解将对象转为json字符串的过程
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "utf-8" );
        response.setHeader( "token", "123456" );
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        /**
         * new ObjectMapper().writeValueAsString( Object o ) 该方法是将一切对象转为json,是万能工具
         * 其中Object包括 List Map Student等一切对象
         * 将map对象转为json(String)串
         */
        try {
            pw.println( new ObjectMapper().writeValueAsString( map ) ); //使用json数据写入到响应正文中
        } catch ( JsonProcessingException e ) {
            e.printStackTrace();
        }
        pw.flush();
        //===================================手动模拟@ResponseBody结束工作================================================

        return;

    }

}

/**
 * 测试路径(GET请求)
 * http://localhost:20000/form_post.html
 * http://127.0.0.1:20000/form_post.html
 * http://192.168.3.101:20000/form_post.html
 * http://192.168.65.1:20000/form_post.html
 * 上面四个url均可,因为一台主机可以有多个子网
 *
 * 然后输入username和password
 * 控制台输出如下内容:
 *
 * 请求方式:POST
 * 请求路径:/login
 * 请求参数username:ronghe
 * 请求参数password:123
 * 消息头session:null
 * 消息头cookie:null
 * 消息正文price:null
 * 消息正文number:null
 *
 * ------------------------------------------------------
 * 使用postman测试:
 * 测试路径(POST)请求
 * http://localhost:20000/login?username=fanronghe
 *
 * 添加Headers(消息头):
 * session  123
 * cookie   456
 *
 * 添加Body(消息正文):JSON串(raw-JSON)
 * {"price":13.8,"number":"300"}
 *
 *
 * 控制台输出
 *
 * 请求方式:POST
 * 请求路径:/login
 * 请求参数username:fanronghe
 * 请求参数password:null
 * 消息头session:123
 * 消息头cookie:456
 * 消息正文price:13.8
 * 消息正文number:300
 *
 */
