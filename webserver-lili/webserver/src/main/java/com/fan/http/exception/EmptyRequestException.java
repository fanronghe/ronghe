package com.fan.http.exception;

/**
 * 空请求异常,
 * 为了浏览器的健壮性
 * 浏览器会尝试发一些空请求
 * 空请求: 请求行,消息头,消息正文都为空,也就是服务器接了电话,对方一句话都不说
 * 处理空请求: 结束该Socket,不作任何响应
 * @author fanronghe
 * @Date 21.7.15 下午 8:46
 */
public class EmptyRequestException extends Exception{
    /**
     * 自定义异常: 1.继承Exception  2.增加所有超类Exception中的构造方法,因为构造方法继承不了
     * 上面两步就是对一个类的复制和重命名的完整操作
     */
    public EmptyRequestException() {
    }

    public EmptyRequestException( String message ) {
        super( message );
    }

    public EmptyRequestException( String message, Throwable cause ) {
        super( message, cause );
    }

    public EmptyRequestException( Throwable cause ) {
        super( cause );
    }

    public EmptyRequestException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
