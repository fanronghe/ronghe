package com.fan.picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 本类用来准备游戏显示的图片数据
 * 在本类加载时,会自动创建静态对象(类对象),然后执行静态代码块,类似于对象中的构造方法,对静态属性赋初值,
 * 完成对图片数据的准备
 * @author fanronghe
 * @Date 21.9.16 下午 7:56
 */
public class Picture {

    /**
     * 天空图片对象
     */
    public static BufferedImage sky;

    /**
     * 英雄机图片对象 数量为2实现抖动效果
     */
    public static BufferedImage heros[] = new BufferedImage[2];

    /**
     * 子弹图片对象
     */
    public static BufferedImage bullet;

    /**
     * 小蜜蜂图片对象  后面4个是小蜜蜂爆炸后的动态图像
     */
    public static BufferedImage airs0[] = new BufferedImage[5];

    /**
     * 小敌机图片对象  后面4个是小敌机爆炸后的动态图像
     */
    public static BufferedImage airs1[] = new BufferedImage[5];

    /**
     * 大敌机图片对象  后面4个是大敌机爆炸后的动态图像
     */
    public static BufferedImage airs2[] = new BufferedImage[5];

    /**
     * 游戏状态图片对象  分别包括游戏开始 游戏暂停 和游戏结束
     */
    public static BufferedImage state[] = new BufferedImage[3];

    static {
        initPicture();
    }

    /**
     * 分别对静态属性赋初值
     */
    private static void initPicture() {
        sky = readPicture( "/images/0.png" );
        heros[0] = readPicture( "/images/1_1.png" );
        heros[1] = readPicture( "/images/1_2.png" );
        bullet = readPicture( "/images/2.png" );
        airs0[0] = readPicture( "/images/3_3.png" );
        airs1[0] = readPicture( "/images/3_1.png" );
        airs2[0] = readPicture( "/images/3_2.png" );
        for ( int i = 0; i < airs0.length-1; i++ ) {
            airs2[i+1] = airs1[i+1] =airs0[i+1] = readPicture( "/images/3_" + (i+4) + ".png" );
        }
        for ( int i = 0; i < state.length; i++ ) {
            state[i] = readPicture( "/images/4_" + (i+1) + ".png" );
        }
    }

    /**
     * 输入图片的路径得到该图片的图片对象
     * @param s 图片的相对路径
     * @return 图片对象
     */
    private static BufferedImage readPicture( String s ) {
        BufferedImage a = null;
        try {
            a = ImageIO.read( Picture.class.getResource( s ) );
        } catch ( IOException e ) {
            e.printStackTrace(); //如果在获取图片时抛出异常,则a为null,返回为null
        }
        return a;
    }

}
