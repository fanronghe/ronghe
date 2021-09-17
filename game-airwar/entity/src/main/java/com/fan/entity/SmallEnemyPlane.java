package com.fan.entity;

import com.fan.common.Common;
import com.fan.common2.ScoreImpl;
import com.fan.picture.Picture;
import com.fan.world.World;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 小敌机对象
 * @author fanronghe
 * @Date 21.9.17 上午 11:58
 */
public class SmallEnemyPlane extends Common implements ScoreImpl {

    /**
     * 属性的初始化
     */
    {
        width = 48;
        height = 50;
        x = new Random().nextInt( World.WIDTH - width );
        y = -height;

        count_getP = 1; //小敌机爆炸时,从第二张图片开始计数显示
        ySpeed = 2; //小敌机向下移动的速度等级为2

    }

    /**
     * 得到小敌机图片对象
     * 以及爆炸时的动态图像
     * @return 小敌机的图片对象
     */
    @Override
    public BufferedImage getPicture() {
        if( state == LIVE ){ //如果小敌机活着
            return Picture.airs1[0];
        }else if( state == DEAD ){
            if( count_getP == Picture.airs1.length-1 ){ //少一张是因为爆炸图时后面的4张,不用小敌机的图片
                state = REMOVE; //在下一个节拍下该小敌机图像就会返回null了
            }
            return Picture.airs1[count_getP++];
        }else {
            return null; //REMOVE状态会执行到这里,小敌机不显示在屏幕上了,但是其对象还在,因为没有触发越界算法
        }
    }

    /**
     * 小敌机的移动规则
     */
    @Override
    public void step() {
        y += ySpeed; //移动方向: 向下  左右不移动
    }

    /**
     * 英雄机击毁小敌机值多少分
     * @return 返回得到的分数
     */
    @Override
    public int getScore() {
        return 1; //小敌机值1分
    }


}
