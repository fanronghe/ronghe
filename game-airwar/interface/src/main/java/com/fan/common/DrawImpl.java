package com.fan.common;

import java.awt.image.BufferedImage;

/**
 * 提供给画图paint方法中用到的参数封装在接口中
 * 一类对象包括:天空对象,子弹对象,小蜜蜂对象,小敌机对象,大敌机对象
 * @author fanronghe
 * @Date 21.9.16 下午 8:51
 */
public interface DrawImpl {
    /**
     * 一类对象(该接口下的)的获取图片对象
     * @return 图片对象
     */
    BufferedImage getPicture();

    /**
     * 该接口下的一类对象获取该对象的x坐标
     * @return 对象的x坐标 也就是该对象中的属性x的值
     */
    int getX();

    /**
     * 该接口下的一类对象获取该对象的y坐标
     * @return 对象的y坐标 也就是该对象中的属性y的值
     */
    int getY();

}
