package com.fan.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author fanronghe
 * @Date 21.9.17 下午 7:24
 */
public class WebServer {
    /**
     * 定义一个Socket服务器,用来建立Web服务器
     */
    private ServerSocket server;

    /**
     * 定义一个线程池引用
     */
    private ExecutorService threadPool;

    /**
     * 使用无参构造,对属性赋初值
     */
    public WebServer(){
        try {
            server = new ServerSocket( 20000 ); //定义一个端口为10000的服务器 ip自动就是本机: http://localhost:10000
            threadPool = Executors.newFixedThreadPool( 60 ); //创建一个大小为60的线程池,并用线程池引用接收表示
        } catch ( IOException e ) {
            e.printStackTrace(); //创建服务器失败会走到这里
        }
    }

    /**
     * 主启动方法
     * @param args VM options 中的参数可以传到这里来,注意不可以使用空格分隔,空格为有效字符,
     *             是一个字符串数组,用逗号分隔,解析时会自动添加双引号,无需手动加双引号, 如: abc,d,efg 即可
     */
    public static void main( String[] args ) {
        new WebServer().start();
    }

    /**
     * 非静态启动方法,里面可以调用非静态方法
     */
    private void start() {
        System.out.println( "欢迎使用LILI服务器,服务器已启动!" );
        while ( true ){
            try {
                //当服务器接到一个客户端的请求会返回一个Socket,把这个Socket交给ClientHandler处理,成为一个线程任务
                ClientHandler handler = new ClientHandler( server.accept() );
                threadPool.execute( handler ); //把线程任务交给线程池处理
            } catch ( IOException e ) {
                e.printStackTrace(); //服务器接电话失败会执行到这里
            }
        }
    }




}
