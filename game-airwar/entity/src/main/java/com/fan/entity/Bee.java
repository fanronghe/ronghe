package com.fan.entity;

import com.fan.common.Common;
import com.fan.common2.AwardTypeImpl;
import com.fan.picture.Picture;
import com.fan.world.World;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 小蜜蜂对象
 * @author fanronghe
 * @Date 21.9.17 上午 11:45
 */
public class Bee extends Common implements AwardTypeImpl {
    /**
     * 属性初始化
     */
    {
        width = 60;
        height = 51;
        x = new Random().nextInt( World.WIDTH - width );
        y = -height;

        count_getP = 1; //小蜜蜂爆炸时,从第二张图片开始计数显示
        ySpeed = 2; //小蜜蜂向下移动的速度等级为2
    }


    /**
     * 得到小蜜蜂图片对象
     * 以及爆炸时的动态图像
     * @return 小蜜蜂的图像对象
     */
    @Override
    public BufferedImage getPicture() {
        if( state == LIVE ){ //如果小蜜蜂活着
            return Picture.airs0[0];
        }else if( state == DEAD ){
            if( count_getP == Picture.airs0.length-1 ){ //少一张是因为爆炸图时后面的4张,不用小蜜蜂的图片
                state = REMOVE; //在下一个节拍下该小蜜蜂图像就会返回null了
            }
            return Picture.airs0[count_getP++];
        }else {
            return null; //REMOVE状态会执行到这里,小蜜蜂不显示在屏幕上了,但是其对象还在,因为没有触发越界算法
        }
    }

    /**
     * 小蜜蜂的移动规则
     */
    @Override
    public void step() {
        y += ySpeed; //向下移动
        x += xSpeed; //左右移动
        if( x <= 0 || x >= World.WIDTH -width ){ //小蜜蜂碰到了最左边的墙或碰到了最右边的墙
            xSpeed = -xSpeed; //让小蜜蜂在左右方向上改变方向
        }
    }

    /**
     * 对interface2项目中的接口的实现
     * 小蜜蜂奖励类型的返回
     * @return
     */
    @Override
    public int getAwardType() {
        return new Random().nextInt( 2 );
    }
}
