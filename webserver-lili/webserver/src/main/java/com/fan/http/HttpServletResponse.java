package com.fan.http;

import java.io.PrintWriter;

/**
 * http协议的响应接口
 * @author fanronghe
 * @Date 21.9.18 上午 10:32
 */
public interface HttpServletResponse {

    /**
     * 这个一般不调用,服务器会根据情况添加200还是404,无需用户干预
     * @param sc 设置http状态码 默认为200,可以不用设置
     */
    void setStatus(int sc);

    /**
     * 设定响应文件或数据的类型
     * 该类型时http协议类型
     * @param type 响应正文的类型
     */
    void setContentType(String type);

    /**
     * 仅仅数据源为printwrite时使用,数据源如果时文件,则不调用该方法
     * 必须和setContentType()方法连用,指定PrintWriter对象中将字符流转为字节流的编码格式
     * 会在ContentType 的value后面追加字符串 ;charset=UTF-8
     * @param charset 手动指定响应正文的编码方式
     */
    void setCharacterEncoding(String charset);

    /**
     * 设置响应头数据
     * @param name 设置头索引
     * @param value 设置头数据
     */
    void setHeader(String name, String value);


    /**
     * 获取可以响应json类型或其他类型数据的对象
     * @return 得到一个PrintWriter 带有自动行刷新的高级字符缓冲输出流
     */
    PrintWriter getWriter();

    /**
     * 设定响应文件
     * 注意:响应数据比响应文件的优先级高,
     * 如果写入了响应数据,则该方法不生效
     * 响应数据和响应文件不会同时生效
     * @param filePath 响应文件的路径
     */
    void setEntity( String filePath );

    /**
     * 注意这个方法不提供给用户,作为ClientHandler客户端处理器处理完毕最终必须执行的语句
     * 将字符缓冲流中的数据刷到内存中,并把响应对象中的所有数据响应给客户端
     * 注意在发送响应之前,在sendContent()方法中,
     * 会自动执行PrintWrite.flush操作,随之把流中的数据转移到字节数组临时内存中,释放PrintWrite高级写出流
     * 会执行setContentLength(int len)方法,会自动计算响应正文数据的字节大小,添加到响应头中
     * 如果是选择响应数据源不是printwrite而是文件,则会自动执行setContentType方法,参数为文件后缀名对应的http响应类型,
     * 确保客户端正确成功解析响应数据
     */
    void start();


}
