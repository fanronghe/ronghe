package com.fan.servlet.impl;

import com.fan.http.HttpServletRequest;
import com.fan.http.HttpServletResponse;
import com.fan.servlet.HttpServlet;

/**
 * @author fanronghe
 * @Date 21.9.18 下午 7:54
 */
public class UserController implements HttpServlet {
    @Override
    public void service( HttpServletRequest request, HttpServletResponse response ) {
        System.out.println( "请求方式:" + request.getMethod() );
        System.out.println( "请求路径:" + request.getRequestURI() );
        System.out.println( "请求参数:" + request.getParameter( "" ) );
        System.out.println( "消息头:" + request.getHeader( "" ) );
        System.out.println( "消息正文:" + request.getBody( "" ) );

        if( "get".equalsIgnoreCase( request.getMethod() ) ){

            response.setEntity( "/user.html" ); //数据源为文件

            return;
        }
        if( "delete".equalsIgnoreCase( request.getMethod() ) ){

            return;
        }
        if( "post".equalsIgnoreCase( request.getMethod() ) ){

            return;
        }
        if( "put".equalsIgnoreCase( request.getMethod() ) ){

            return;
        }
    }
}
/**
 * 测试路径
 * http://localhost:20000/user
 */