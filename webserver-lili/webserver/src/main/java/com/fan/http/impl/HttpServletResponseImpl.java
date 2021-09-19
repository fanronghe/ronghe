package com.fan.http.impl;

import com.fan.http.HttpContext;
import com.fan.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现响应接口
 * @author fanronghe
 * @Date 21.9.18 下午 3:58
 */
public class HttpServletResponseImpl implements HttpServletResponse {
    /**
     * 该实现类需要在new时接收一个socket对象,用于与客户端连接的基础,用于给客户端响应数据的基础
     */
    private Socket socket;
    public HttpServletResponseImpl( Socket socket ){
        this.socket = socket;
    }

    /**
     * 用于向客户端发送一行数据,并以回车换行结尾
     * 只用于发送状态行和响应头
     * 响应正文用带有自动行刷新的高级字符缓冲输出流发送
     * @param str
     * @throws IOException
     */
    private void writeLine( String str ) throws IOException {
        OutputStream out = socket.getOutputStream(); //获取当前客户端的输出流 不用特意关闭该输出流 最后只要关闭当前socket即可
        out.write( str.getBytes(StandardCharsets.ISO_8859_1) ); //状态行和响应头的编码格式的标准为ISO_8859_1 而不涉及响应正文
        out.write( 13 ); //回车
        out.write( 10 ); //换行
    }


    private int statusCode = 200; //http状态码,默认值为200,即通信正常,客户端于服务器之间的一问一答通信通道畅通
    private String statusReason = ""; //由于该状态信息语义不明,现在已经不用了,废弃该用法,统一为空串表示
                                      //故状态行只有一个有效信息,即sc状态码
    private void sendStatusLine() throws IOException {
        String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
        writeLine( line ); //向客户端发送状态行
    }


    /**
     * 把准备好的map_headers< String, String> 响应头数据
     * 在发送完状态行后发送给客户端
     * 注意,该方法调用前,应该提前设置好响应头中的数据,
     * 尤其是Content-Type和Content-Length这两个描述响应正文的必须的头数据
     */
    private Map<String, String> map_headers = new HashMap<>();
    private void sendHeaders() throws IOException {
        map_headers.put( "Server", "LILI" ); //服务器的铭牌为LILI
        map_headers.forEach( ( key, value ) -> { //使用jdk8中新特性foreach而不是新循环对map集合进行遍历
            try {
                writeLine( key + ": " + value ); //遍历过程中对所有的响应头取出并一行一行的发送给客户端
            } catch ( IOException e ) {
                e.printStackTrace(); //在该回调方法中的异常必须在内部进行捕获,因为接口中的方法并没有抛异常且不可以改源码
            }
        } );
        writeLine( "" ); //响应头结束标识
    }


    /**
     * 响应正文的存放需要开辟一个临时内存空间
     * 这里向用户提供一个PrintWriter高级写出流,把响应正文数据先写入内存中临时存放
     */
    private byte[] contentData = new byte[0]; //定义字节数组,存放临时响应数据
    private ByteArrayOutputStream baos = new ByteArrayOutputStream(); //定义内存数据容器(字节数组)
    private String charset = "UTF-8"; //响应正文的默认编码为utf-8,这个会加在Content-Type后面即 ;charset=UTF-8
    private PrintWriter pw = new PrintWriter(
            new BufferedWriter(
                    new OutputStreamWriter( baos, Charset.forName(charset) ) //设置字符转字节参照的编码映射集
            ), true //启用自动行刷新功能,即PrintWriter对象每执行println一次都会把缓冲区的数据刷到baos字节数组输出流中,
            // 即内存中
    );


    /**
     * 向客户端发送响应正文,确保了响应正文一定不为空,因为有404页面来兜底,404页面本身就是一个响应正文
     * 方式有两种,一般二选一,不可同时生效
     * 一是得到PrintWriter对象并通过它来手动写出响应数据
     * 二是自动设定一个文件,把文件中的数据当作数据源,自动发送
     * @throws IOException
     */
    private File entity;
    private void sendContent() throws IOException {
        /**
         * 对开放给用户的printwrite写出工具进行回收并处理
         * 将内存中的数据转到字节数组中
         * 最后关闭,销毁该printwrite写出工具
         */
        pw.flush();
        contentData = baos.toByteArray();
        pw.close();

        /**
         * 下面的分支结构:只要不往contentData中写数据,服务器自动执行文件数据源
         *               但一旦向contentData中写入数据了,就不会再次执行文件数据源了
         */
        if( contentData.length > 1 ){ //如果临时内存中有数据,则响应正文以内存中的临时数据为准
            OutputStream out = socket.getOutputStream(); //获取写出流 不用关闭该流,只需关闭socket即可

            //1. 向当前客户端发送状态行
            sendStatusLine();

            //2. 向当前客户端发送响应头
            //如果是printwrite数据源,则Content-Length会自动添加,还需要手动添加Content-Type
            map_headers.put( "Content-Length", contentData.length + "" ); //加空串把int转为String类型
            map_headers.put( "Content-Type", map_headers.get( "Content-Type" )
                    + (setCharset ? ( ";charset=" + charset.toLowerCase() ) : "") );
            sendHeaders(); //在这里,向当前客户端发送响应头,使得Content-Length响应头生效

            //3. 向当前客户端发送响应正文
            out.write( contentData ); //一次性把内存中contentData字节数组的所有数据写出到客户端

        }else { //在临时内存中没有数据,数据为空的前提下,才会选择文件作为响应数据源
            datasourceAsFile();
        }
    }

    /**
     * 响应数据源不是printwrite而是文件时,
     * 则会把文件中的数据传到响应正文中发送给客户端
     * @throws IOException
     */
    private void datasourceAsFile() throws IOException {
        //确保entity始终不为null,至少又一个404页面兜底,但又不会干扰到正常页面的响应
        if( entity == null ){
            setStatus( 404 ); //只有当无法定位数据源(内存或文件)的情况下,会报404页面
            setEntity( "404.html" ); //这里必须可以定位到该文件,否则entity依然为null
        }


        //FileInputStream fis = new FileInputStream( new File( "" ) ); 文件读取流的原型
        //InputStream in = this.getClass().getResourceAsStream( "" );文件读取流的另一种形式:部署形式 使用场景,该文件在jar包中
        try ( FileInputStream fis = new FileInputStream( entity ) ) { //为该文件接上读取流
            OutputStream out = socket.getOutputStream(); //为客户端接上写出流

            //1. 向当前客户端发送状态行
            sendStatusLine();

            //2. 向当前客户端发送响应头
            //如果是文件数据源,则Content-Type和Content-Length都会自动添加
            String suffix = entity.getName().substring( entity.getName().lastIndexOf( "." ) + 1 );
            //如果setCharset追加标志位有效,则追加字符串;charset=utf-8,如果无效则加空串,相当于没加
            map_headers.put( "Content-Type", HttpContext.getMimeType( suffix )
                    + (setCharset ? ( ";charset=" + charset.toLowerCase() ) : "")  );
            map_headers.put( "Content-Length", entity.length() + "" ); //加空串把long转为String类型
            sendHeaders(); //在这里,向当前客户端发送响应头,使得Content-Length响应头生效

            //3. 向当前客户端发送响应正文
            int len;
            byte[] data = new byte[1024 * 8];
            while ( ( len = fis.read( data ) ) != -1 ){ //块读入(8KB) 块写出  加速数据复制
                //完成数据等字节的从源文件中的数据到客户端响应正文中收到的数据的传输
                out.write( data, 0, len ); //一次相当于写出data字节数组中的从下标为0的开始,一共len个字节的数据
            }
        }
    }


    /**
     * =================================================================================================================
     * ============================以下为响应接口的实现方法===============================================================
     * =================================================================================================================
     */
    @Override
    public void setStatus( int sc ) {
        this.statusCode = sc;
    }

    @Override
    public void setContentType( String type ) {
        map_headers.put( "Content-Type", type );
    }

    /**
     * http响应正文编码方式的标准为"utf-8"
     * @param charset 手动指定响应正文的编码方式
     */
    private boolean setCharset = false; //在响应头Content-Type数据中追加字符串";charset=utf-8"的标识符
    @Override
    public void setCharacterEncoding( String charset ) {
        this.charset = charset; //响应正文用utf-8编码
        setCharset = true; //追加标识符为true,才会追加,会让客户端用utf8去解码
    }

    @Override
    public void setHeader( String name, String value ) {
        map_headers.put( name, value );
    }

    @Override
    public PrintWriter getWriter() {
        return pw;
    }

    /**
     * 必须是一个有效的文件路径,否则是一个无效设置
     * @param filePath 响应文件的路径
     */
    @Override
    public void setEntity( String filePath ) {
        //这里使用相对目录,即在jar包的同级目录下应该准备一个static文件夹,里面存放静态资源文件,就不适用jar包中的文件了
        //相对目录不用前面不用加/
        //相对路径必须配合Working Directory工作,jar包的Working Directory就是jar包所在的目录
        File file = new File( "static/" + filePath ); //最后加上/提供兼容性,也就是filePath前面加不加/都行
        if( file.exists() && file.isFile() ){ //只有在该路径定位的是一个文件,且该文件存在的条件下,entity才不为null
            entity = file;
        }
    }


    /**
     * 本方法的执行过程:
     * 将向客户端响应一个完整的发送过程:
     * 即先会发送状态行,然后发送响应头,最后根据是否设置了响应正文而选择性的发送响应正文
     */
    @Override
    public void start() {
        try {
            //sendStatusLine(); //向当前客户端发送状态行  该方法已经被sendContent()调用了,无需在这里重复调用了
            //sendHeaders(); //向当前客户端发送响应头  该方法已经被sendContent()调用了,无需在这里重复调用了
            sendContent(); //向当前客户端发送响应正文
        } catch ( IOException e ) {
            e.printStackTrace(); //执行到这里说明取消向客户端响应过程
        }
    }

}
