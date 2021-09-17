package com.fan.entity;

import com.fan.common.Common;
import com.fan.picture.Picture;

import java.awt.image.BufferedImage;

/**
 * 含参构造的子弹对象
 * 利用含参构造确定子弹对象的初始坐标位置
 * @author fanronghe
 * @Date 21.9.17 上午 11:19
 */
public class Bullet extends Common {
    /**
     * 利用对象的构造方法,此时已经没有无参构造了,
     * 一个对象可以重载构造方法,提供灵活的创建方式
     * 对子弹对象进行数据初始化
     * @param x 新建子弹对象时,需要给定x坐标,才能创建
     * @param y 新建子弹对象时,选哟给定y坐标,才能创建
     */
    public Bullet( int x, int y ){
        width = 8;
        height = 20;
        this.x = x;
        this.y = y;

        ySpeed = 4; //子弹的步长为4,也就是朝一个方向(这里是y的减少方向:即向屏幕上端)的速度等级
    }

    /**
     * 由于子弹无爆炸或抖动的动态效果,故无需使用图像计数器
     * @return
     */
    @Override
    public BufferedImage getPicture() {
        if( state == LIVE ){
            return Picture.bullet;
        }else {
            return null; // 如果图像对象为null值,不会有异常,只导致屏幕上不会渲染该图片,
                         // 所以在Picture类中读取图片异常不会影响游戏正常运行,但会不停的打印错误日志
        }
    }

    /**
     * 子弹的运行规则
     */
    @Override
    public void step() {
        y -= ySpeed; //方向为: 向上
    }

    /**
     * 由于子弹向上运动则必须更换超类中对本对象定义的越界判断规则
     * 原规则是基于对象向下运动的
     * 但是子弹是所有飞行物对象中的特例,他向上运动
     */
    @Override
    public boolean isOut() {
        //super.isOut(); //return y > World.HEIGHT; 超类中原越界规则不予采用
        return y <= -height;
    }
}
