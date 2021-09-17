package com.fan.entity;

import com.fan.common.Common;
import com.fan.picture.Picture;
import com.fan.world.World;

import java.awt.image.BufferedImage;

/**
 * 天空对象
 * @author fanronghe
 * @Date 21.9.17 上午 7:04
 */
public class Sky extends Common {
    /**
     * 属性初始化
     */
    {
        width = World.WIDTH;
        height = World.HEIGHT;
        x = 0;
        y = 0;
    }

    /**
     * 是对interface中DrawImpl接口的实现
     * @return  返回天空图片对象
     */
    @Override
    public BufferedImage getPicture() {
        return Picture.sky;
    }

    /**
     * 是对interface中stepImpl接口的实现
     */
    @Override
    public void step() {
        y += ySpeed;
        y1 += ySpeed;
        if( y >= World.HEIGHT ){ //如果下面那张图片超出了屏幕就复位到初始状态,最上面
            y = -World.HEIGHT;
        }
        if( y1 >= World.HEIGHT ){ //如果上面那张图片从初始状态到屏幕外了,也需要复位到初始状态
            y1 =  -World.HEIGHT;
        }
    }

    /**
     * 天空的第二张图片的纵坐标属性
     */
    private int y1 = -World.HEIGHT;
    public int getY1(){
        return y1;
    }
}
