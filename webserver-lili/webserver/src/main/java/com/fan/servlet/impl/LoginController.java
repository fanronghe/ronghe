package com.fan.servlet.impl;

import com.fan.http.HttpServletRequest;
import com.fan.http.HttpServletResponse;
import com.fan.servlet.HttpServlet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 专门处理表单的post提交的登录请求
 * @author fanronghe
 * @Date 21.9.18 下午 8:30
 */
public class LoginController implements HttpServlet {
    @Override
    public void service( HttpServletRequest request, HttpServletResponse response ) {

        System.out.println( "请求方式:" + request.getMethod() );
        System.out.println( "请求路径:" + request.getRequestURI() );

        System.out.println( "请求参数username:" + request.getParameter( "username" ) );
        System.out.println( "请求参数password:" + request.getParameter( "password" ) );

        System.out.println( "消息头session:" + request.getHeader( "session" ) );
        System.out.println( "消息头cookie:" + request.getHeader( "cookie" ) );

        System.out.println( "消息正文price:" + request.getBody( "price" ) );
        System.out.println( "消息正文number:" + request.getBody( "number" ) );


        /**
         * 仅有一个post方式的响应处理器
         * 该处理器的数据源是json数据
         */
        if( "post".equalsIgnoreCase( request.getMethod() ) ){

            response.setContentType( "application/json" );
            response.setCharacterEncoding( "utf-8" );
            response.setHeader( "token", "123456" );
            PrintWriter pw = response.getWriter();

            Map<String, String> map = new HashMap<>();
            map.put( "code", "200" );
            map.put( "message", "ok" );
            map.put( "data", "这里是LILI服务器" );
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
            return; //作为void的返回值,提前结束函数的作用
        }

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
 * 请求方式:GET
 * 请求路径:/item
 * 请求参数:null
 * 消息头:null
 * 消息正文:null
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
