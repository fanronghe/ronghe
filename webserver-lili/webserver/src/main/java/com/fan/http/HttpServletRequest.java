package com.fan.http;

/**
 * http协议的请求接口
 * @author fanronghe
 * @Date 21.9.18 上午 10:16
 */
public interface HttpServletRequest {
    /**
     * 获取客户端的对数据读写方式 读GET  写DELETE POST PUT
     * @return 客户端的请求方式:GET DELETE POST PUT
     */
    String getMethod();

    /**
     * 获取客户端的地址
     * @return 客户端的请求路径(地址)
     */
    String getRequestURI();

    /**
     * 获取客户端的数据
     * @param name 数据索引
     * @return 客户端的请求数据内容
     */
    String getParameter(String name);

    /**
     * 获取客户端的附加数据
     * @param name 附加索引
     * @return 附加数据
     */
    String getHeader(String name);

    /**
     * 只有消息正文数据是json格式数据,该方法的返回值才是有效的
     * 获取客户端的消息正文中的数据,已转为map集合中
     * @param name 正文数据索引
     * @return 正文数据
     */
    String getBody(String name);

}
