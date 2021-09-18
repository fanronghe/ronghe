package com.fan.http.impl;

import com.fan.http.HttpServletRequest;
import com.fan.http.exception.EmptyRequestException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求接口的实现类
 * @author fanronghe
 * @Date 21.9.18 上午 11:15
 */
public class HttpServletRequestImpl implements HttpServletRequest {

    /**
     * 该实现类需要在new时接收一个socket对象,用于与客户端连接的基础
     */
    private Socket socket;
    public HttpServletRequestImpl( Socket socket ) throws EmptyRequestException { //空请求传出去,为了扩大回滚范围
        /**
         * 解析时发生的socket中的异常,就在本类中处理了
         * 构造方法内的方法会伴随着对象的创建而自动执行,无需对象创建完成后二次手动调用,
         * 将两步合为了一步,
         * 主要对请求对象中的数据进行初始化
         */
        try {
            this.socket = socket;
            parseRequestLine(); //解析当前连接的请求行
            parseHeaders(); //解析当前连接的消息头
            parseContent(); //解析当前连接的消息正文
        } catch ( IOException e ) {
            e.printStackTrace(); //执行到这里会导致请求对象中的数据全部为默认值null,不代表当前请求的有效数据
        }
    }


    /**
     * 读取客户端发送过来的数据中的一行
     * @return
     * @throws IOException
     */
    private String readLine() throws IOException {
        StringBuilder builder = new StringBuilder(); //可变长度字符串工具类,对字符串的修改操作时String的替代字符串类
        InputStream in = socket.getInputStream(); //获取客户端连接的输入流 不用特意关闭该输入流,只要最后关闭socket即可
        int d;
        char pre = 'a';
        char cur = 'a';
        while ( ( d = in.read() ) != -1 ){ //流中有数据就读出来  直到把流中的数据读完
            cur = ( char ) d; //由于请求行和消息头中的数据都是iso-8859-1编码的,故直接可以用unicode字符集完成字节到字符的解码
            if( pre == 13 && cur == 10 ){ //13是回车的int值  10是换行的int值
                break; //遇到行结束标识,停止读取,代表一行数据完整读出
            }
            pre = cur;
            builder.append( cur ); //可变字符串工具的追加,StringBuilder字符容器初始状态字符为长度为0
        }
        return builder.toString().trim(); //去除10 13中的空白字符,是无效数据
    }


    /**
     * 专门对GET和DELETE请求中的参数(数据)格式(?&格式)进行解析
     * 解析完后存入Map< String, String>的参数散列表map_parameters中
     * @param para
     * @throws UnsupportedEncodingException
     */
    private void putParameter( String para ) throws UnsupportedEncodingException {
        para = URLDecoder.decode( para, "UTF-8" ); //把iso-8859-1中的转义字符按照utf-8解译码,生成unicode字符串,
                                                        //可以表示中文
        String[] data = para.split( "&" ); //按"&"把切成n个数据
        for( String a : data ){
            String[] b = a.split( "=" ); //对于一个数据"k=v"而言,"k="这个索引一定有的,但"v"可能有也可能没有
            if( b.length > 1 ){
                map_parameters.put( b[0], b[1] ); //k=v
            }else {
                map_parameters.put( b[0], null ); //k=
            }
        }
    }


    /**
     * 解析请求行
     * 把请求参数数据map化 --> map_parameters< String, String >
     */
    private String method;
    private String uri;
    private Map<String, String> map_parameters = new HashMap<>();
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readLine(); //读取请求行
        if( line.isEmpty() ){ //滤除空请求
            throw new EmptyRequestException( "报告!捕获空请求!" );
        }
        String[] data = line.split( "\\s" ); //遵循http协议,按照空白字符拆分,得到三部分
        method = data[0]; //请求方法
        uri = data[1]; //带有参数的请求路径
        //String protocol = data[2]; //协议 这个没有利用价值,丢弃其
        data = uri.split( "\\?" ); //分为两种情况:1.如果没有?即没有携带参数 2.有?且后面携带参数
        uri = data[0]; //去掉请求参数的请求路径
        if( data.length > 1 ){ //请求路径中携带了请求参数
            putParameter( data[1] ); //请求行中请求参数的map化
        }
    }

    /**
     * 解析消息头
     * 把消息头数据map化--> map_headers< String, String >
     */
    private Map<String, String> map_headers = new HashMap<>();
    private void parseHeaders() throws IOException {
        while ( true ){
            String line = readLine();
            if( line.isEmpty() ){
                break; //在请求行之后一行一行读取数据时,如果再次读取到空数据,则表明为消息头结束标识
            }
            String[] data = line.split( ":\\s" ); //消息头按照 冒号空格 的方式拆分 索引和数据
            map_headers.put( data[0], data[1] ); //索引和数据均不为空
        }
    }


    /**
     * 解析消息正文
     * 如果客户端通过post或put发送了json数据,则会把数据存入map_body中
     * @throws IOException
     */
    private Map<String, String> map_body = new HashMap<>();
    private void parseContent() throws IOException {
        //只有post和put请求才有消息正文,否则本方法体就是空方法体,如对get和delete请求而言
        if( "post".equalsIgnoreCase( method ) || "put".equalsIgnoreCase( method ) ){
            int len = Integer.parseInt( map_headers.get( "Content-Length" ) ); //把字符串解析为int数值,得到数据字节大小
            byte[] contentData = new byte[len]; //直接为消息正文量身定制一个等容量的字节数组空间
            InputStream in = socket.getInputStream(); //得到输入流
                                                      //在请求行和消息头依次读取完毕后,此时读取流的指针恰好在消息正文的起始位置
            in.read( contentData ); //在刚开辟的字节数组空间中填入数据,即消息正文的数据

            //如果消息正文中的格式是?&数据则调用处理请求行参数的方法对其解析,结果保存在map_parameters中
            if( "application/x-www-form-urlencoded".equals( map_headers.get( "Content-Type" ) ) ){
                //这里的解码方式对于?&格式数据用ISO_8859_1足够了,而json数据不适用,因为json中有中文
                String para = new String( contentData, StandardCharsets.ISO_8859_1 ); //字节数组转为字符串
                putParameter( para );
            }

            //如果消息正文是json类型的数据,结果保存在
            if( "application/json".equals( map_headers.get( "Content-Type" ) ) ){
                //这里的解码方式必须为UTF_8
                String json = new String( contentData, StandardCharsets.UTF_8 ); //字节数组转为json字符串

                //使用TypeReference对象来传入Map集合的k和v的类型,其中v是还可以嵌套一个map的
                //形成  new TypeReference<HashMap<String, HashMap<String, String>>>(){} 的结构
                map_body = new ObjectMapper().readValue( json, new TypeReference<HashMap<String, String>>() {} );
            }
        }
    }


    /**
     * =================================================================================================================
     * ============================以下为请求接口的实现方法===============================================================
     * =================================================================================================================
     */
    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getRequestURI() {
        return uri;
    }

    @Override
    public String getParameter( String name ) {
        return map_parameters.get( name );
    }

    @Override
    public String getHeader( String name ) {
        return map_headers.get( name );
    }

    @Override
    public String getBody( String name ) {
        return map_body.get( name );
    }
}
