package com.fan.core;


import com.fan.http.HttpServletRequest;
import com.fan.http.HttpServletResponse;
import com.fan.http.exception.EmptyRequestException;
import com.fan.http.impl.HttpServletRequestImpl;
import com.fan.http.impl.HttpServletResponseImpl;
import com.fan.servlet.HttpServlet;
import com.fan.servlet.ServletContext;

import java.io.IOException;
import java.net.Socket;

/**
 * @author fanronghe
 * @Date 21.9.17 下午 7:19
 */
public class ClientHandler implements Runnable { //实现这个接口,这个类就成为了一个线程任务了

    /**
     * 线程任务中有一个堆属性Socket套接字,并使用含参构造对其赋初值
     * 从服务器中传来的客户端的Socket,可以再线程任务中使用它与客户端通信
     */
    private Socket socket;
    public ClientHandler( Socket socket ) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            HttpServletRequest request = new HttpServletRequestImpl( socket ); //轻松得到客户端请求对象request
            HttpServletResponse response = new HttpServletResponseImpl( socket ); //轻松得到客户端响应对象response

            String uri = request.getRequestURI(); //从请求对象中得到请求路径
            //查看该 请求方式和请求路径 有没有对应的请求处理器
            HttpServlet controller = ServletContext.getController( request.getMethod() + request.getRequestURI() );
            if( controller != null ){ //匹配到相应的处理器,将该请求定义为servlet处理器请求,由servlet处理器进行处理
                controller.service( request, response ); //执行回调方法
            }else if( "get".equalsIgnoreCase( request.getMethod() ) ) {//通过servlet过滤器后,
                                                                       // 服务器会将该请求的目的定义为一个
                                                                       //GET请求来从静态资源库中获取静态资源文件的请求
                response.setEntity( uri ); //会在uri前面补上"/static/"
            }
            response.start(); //这个不交给用户调用,作为ClientHandler客户端处理器的核心工作,必须得到执行
        } catch ( EmptyRequestException e ){
            e.printStackTrace(); //客户端收到空请求,直接跳过本次请求处理
        }catch ( Exception e ) {
            e.printStackTrace(); //异常兜底 可捕获运行时异常,确保不会回滚至JVM,导致整个应用被kill掉,造成闪退,
                                 //有异常兜底,起码还可以维持服务器正常处理别的客户端请求,仅仅放弃本次请求的处理
        } finally {
            try {
                socket.close(); //响应完毕,服务器要切断与客户端的连接,服务器再响应完毕后会立即挂掉电话
            } catch ( IOException e ) {
                e.printStackTrace(); //挂电话失败会执行到这里
            }
        }




    }
}
