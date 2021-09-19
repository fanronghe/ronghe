package com.fan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fanronghe
 * @Date 21.9.19 上午 11:00
 */
@Controller //这个是处理器的标识
public class LoginController_new {

    /**
     * 仅匹配post请求且请求路径为/login的请求
     * @param request 请求对象
     * @param response 响应对象
     */
    @PostMapping( "/login_new" )
    @ResponseBody //将任意一个复杂的返回值类型转为json串
    public Map<String, String> login( HttpServletRequest request, HttpServletResponse response,
                                      //将消息正文中的JSON字符串存入对象(Map<String, String>)
                                      //@RequesBody使用后会帮我们关闭读取流,
                                      //如果再次获取读取流,该流为不可用的关闭状态(Stream closed)
                                      @RequestBody Map<String, String> map_body ){

        System.out.println( "请求方式:" + request.getMethod() );
        System.out.println( "请求路径:" + request.getRequestURI() );

        System.out.println( "请求参数username:" + request.getParameter( "username" ) );
        System.out.println( "请求参数password:" + request.getParameter( "password" ) );

        System.out.println( "消息头session:" + request.getHeader( "session" ) );
        System.out.println( "消息头cookie:" + request.getHeader( "cookie" ) );

        //@RequestBody帮我们把消息正文从request对象中取出来存入到Map<String,String>对象中了
        //数据源归根结底还是从request对象中取出来的
        System.out.println( "消息正文price:" + map_body.get( "price" ) );
        System.out.println( "消息正文number:" + map_body.get( "number" ) );


        Map<String, String> map = new HashMap<>();
        map.put( "code", "200" );
        map.put( "message", "ok" );
        map.put( "data", "这里是LILI服务器" );

        response.setHeader( "token", "123456" );
        return map; //通过@ResponseBody将任意一个复杂的返回值类型转为json串
    }

}



/**
 * ------------------------------------------------------
 * 使用postman测试:
 * 测试路径(POST)请求
 * http://localhost:20000/login_new?username=fanronghe
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
 请求方式:POST
 请求路径:/login_new
 请求参数username:fanronghe
 请求参数password:null
 消息头session:123
 消息头cookie:456
 消息正文price:13.8
 消息正文number:300
 *
 */
