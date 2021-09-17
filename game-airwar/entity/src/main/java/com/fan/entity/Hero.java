package com.fan.entity;

import com.fan.common.Common;
import com.fan.picture.Picture;

import java.awt.image.BufferedImage;

/**
 * 英雄机对象
 * @author fanronghe
 * @Date 21.9.17 上午 11:02
 */
public class Hero extends Common {
    /**
     * 属性的初始化
     */
    {
        width = 97;
        height = 139;
        x = 140;
        y = 400;
    }

    public void step( int x, int y ){
        this.x = x - width/2;
        this.y = y - height/2;
    }


    /**
     * @return 循环返回英雄机的图片对象
     */
    @Override
    public BufferedImage getPicture() {
        return Picture.heros[count_getP++ % Picture.heros.length]; //调用时循环的对英雄机两张图片进行返回
    }

    /**
     * 只为了实现StepImpl中的接口方法,
     * 由于需要传参,此方法废弃
     * 被含参方法step(int, int)替代
     */
    @Override
    public void step() {

    }

    /**
     * 制定英雄机生命值数据的算法规则
     * 数据属性私有化,方法公开化
     * 禁止用户随意的对数据进行操作
     * 而是调用固定的对数据的算法(方法)实现固定规律的数据操作
     */
    private int life = 3;
    public int getLife(){ return life; }
    public void addLife(){ life++; }
    public void subLife(){ life--; }

    /**
     * 制定火力值数据的算法规则
     */
    private int fire = 10;
    public int getFire(){ return fire; }
    public void addFire(){ fire += 10; }
    public void clearFire(){ fire = 0; }

    /**
     * 制定本局游戏中在所有英雄机生命值全部用完时的得分规则
     * 这里就是典型的属性私有化,且为属性提供get和set方法
     * 就相当于属性从私有化通过方法加持重构为了属性公开化
     */
    private int score = 0; //默认总得分为0
    public int getScore(){ return score; }
    public void setScore( int score ){ this.score = score; }

    /**
     * 调用本方法实现
     * 英雄机自动发射子弹一次
     * 即会产生子弹对象
     */
    public Bullet[] shoot(){
        int x1 = width / 4;
        int y1 = 20;
        Bullet[] a = null; // 这里null的初值执行不到,是因为之后一定会被重新赋值,
                           // 但为了满足定义变量顺便也要给变量初始化的原则就加上了引用变量的默认值null
        if( fire > 0 ){
            fire -= 2;
            a = new Bullet[]{
                    new Bullet( x + 1*x1, y - y1 ),
                    new Bullet( x + 3*x1, y - y1 )
            };
        }else {
            a = new Bullet[]{
                    new Bullet( x + 2*x1, y - y1 )
            };
        }
        return a;
    }


}

































