package com.fan.servlet;

import com.fan.http.HttpServletRequest;
import com.fan.http.HttpServletResponse;

/**
 * 本LILI服务器在接收到客户端请求后,
 * 一定会响应一个完整的带有状态行,响应头,响应正文,都不为空的响应
 * 本接口中的每个回调方法都有一个id,是请求方式method和请求路径uri拼接而成的字符串
 * 由id(请求方式和请求路径)锁定的请求 其 响应数据源一定为pw
 * @author fanronghe
 * @Date 21.9.18 下午 7:17
 */
@FunctionalInterface
public interface HttpServlet {
    /**
     * 响应正文的数据源分为两种:printwrite数据源和文件数据源
     * 只要不通过printwrite写数据到内存,服务器就会自动执行文件数据源
     * 但一旦通过printwrite写数据到内存,就不会再次执行文件数据源了
     * @param request 请求对象
     * @param response 响应对象
     */
    void service( HttpServletRequest request, HttpServletResponse response );
}
