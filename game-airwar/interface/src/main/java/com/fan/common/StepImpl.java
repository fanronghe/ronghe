package com.fan.common;

/**
 * 一类对象的Step方法提取到接口中
 * 当这一类对象用接口引用表示时,可以取出对象中的方法体
 * 这个接口中的方法体是对象移动,即改变对象内x和y的属性大小操作
 * @author fanronghe
 * @Date 21.9.16 下午 9:01
 */
public interface StepImpl {
    /**
     * 对象按照内在设定好的步长对属性的坐标xy进行改变
     * 步长的设定并没有暴露给参数,无法设定,只能执行默认步长
     */
    void step();
}
