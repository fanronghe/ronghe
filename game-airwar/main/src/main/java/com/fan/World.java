package com.fan;

import com.fan.common.Common;
import com.fan.common.DrawImpl;
import com.fan.common.StepImpl;
import com.fan.common2.AwardTypeImpl;
import com.fan.common2.ScoreImpl;
import com.fan.entity.*;
import com.fan.picture.Picture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.*;

/**
 * 整合项目中的主启动类
 * @author fanronghe
 * @Date 21.9.17 上午 6:49
 */
public class World {
    /**
     * 定义游戏状态  包括4个状态  闲置  运行  暂停  游戏结束
     */
    private State state = State.IDLE;


    /**
     * 准备游戏对象:天空对象,英雄机对象,子弹对象,敌机对象
     */
    private Sky sky = new Sky();
    private Hero hero = new Hero();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Common> enemys = new ArrayList<>();

    /**
     * 主启动方法
     * @param args
     */
    public static void main( String[] args ) {
        new World().start();
    }

    private void start() {
        JPanel jPanel = getJPanel();
        mouse( jPanel );
        timer( jPanel );
        display( jPanel ); //在游戏画面上载入画板数据
    }

    private JPanel getJPanel() {
        return new JPanel(){
            @Override
            public void paint( Graphics g ) {
                draw( g, sky );
                draw1( g, sky );
                draw( g, hero );
                for( DrawImpl a : bullets ){
                    draw( g, a );
                }
                for( DrawImpl a : enemys ){
                    draw( g, a );
                }
                g.drawString( "得分:" + hero.getScore(), 10, 25 );
                g.drawString( "生命:" + hero.getLife(), 10, 45 );
                g.drawString( "火力:" + hero.getFire(), 10, 65 );
                switch ( state ){
                    case IDLE: g.drawImage( Picture.state[0], 0, 0, null ); break;
                    case Pause: g.drawImage( Picture.state[1], 0, 0, null ); break;
                    case GameOver: g.drawImage( Picture.state[2], 0, 0, null ); break;
                    default:
                }
            }
            public void draw( Graphics g, DrawImpl d ){ g.drawImage( d.getPicture(), d.getX(), d.getY(), null ); }
            public void draw1( Graphics g, Sky sky ){ g.drawImage( sky.getPicture(), sky.getX(), sky.getY1(), null ); }
        };
    }


    /**
     * 在画板对象上挂载鼠标事件,让鼠标操作作为画板的数据输入
     * @param jp 传入画板对象
     */
    private void mouse( JPanel jp ) {
        MouseAdapter m = new MouseAdapter() {
            /**
             * 鼠标点击输入给画板
             * @param e
             */
            @Override
            public void mouseClicked( MouseEvent e ) {
                if( state == State.IDLE ){ //如果游戏初始状态下,鼠标点击输入会进入游戏运行状态
                    state = State.Running;
                }else if( state == State.GameOver ) { //如果游戏在结束状态下,点击鼠标会进入游戏复位状态
                    state = State.IDLE;
                    sky = new Sky(); //复位天空对象
                    hero = new Hero(); //复位英雄机对象:包括位置,火力,生命值,得分
                    bullets.clear(); //清空子弹对象库
                    enemys.clear(); //清空敌机对象库
                }
            }

            /**
             * 鼠标进入游戏画面事件触发
             * @param e
             */
            @Override
            public void mouseEntered( MouseEvent e ) {
                if( state == State.Pause ){
                    state = State.Running;
                }
            }

            /**
             * 鼠标移出游戏画面事件触发
             * @param e
             */
            @Override
            public void mouseExited( MouseEvent e ) {
                if( state == State.Running ){
                    state = State.Pause;
                }
            }

            /**
             * 鼠标在游戏画面中移动事件触发
             * @param e
             */
            @Override
            public void mouseMoved( MouseEvent e ) {
                if( state == State.Running ){
                    hero.step(e.getX(), e.getY() );
                }
            }
        };
        jp.addMouseListener( m ); //为画板添加鼠标事件
        jp.addMouseMotionListener( m ); //为画板添加鼠标侦听器,来侦听画板
    }


    /**
     * 游戏画面的显示
     * 将画板对象传入进来,显示在屏幕上
     * 即将画板与游戏画面关联起来
     */

    private void display( JPanel jp ) {
        JFrame jf = new JFrame();
        jf.add( jp );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jf.setSize( com.fan.world.World.WIDTH, com.fan.world.World.HEIGHT ); //为了避免循环依赖
        jf.setLocationRelativeTo( null );
        jf.setVisible( true );
    }


    /**
     * 为画板添加定时器事件
     * @param jp
     */
    private void timer( JPanel jp ) {
        new Timer().schedule( new TimerTask() {
            @Override
            public void run() {
                if( state == State.Running ){
                    EnemysEnter(); //敌机入场
                    bulletsEnter(); //子弹入场
                    clearEnemysAndBullets(); //敌机和子弹出场
                    step(); //飞行物移动,除英雄机手动鼠标移动
                    EnemysBangHeros(); //敌机撞英雄机
                    EnemysBangBullets(); //敌机撞子弹
                    gameOver(); //游戏结束触发检测
                    System.out.println( "子弹对象数量:" + bullets.size() + "-敌机对象数量:" + enemys.size() );
                }
                jp.repaint(); //刷新画板
            }

            /**
             * 敌机入场:小蜜蜂,小敌机,大敌机
             */
            private int zzEnter = 0; //蜜蜂,敌机出现的分频计数器
            private void EnemysEnter() {
                if( zzEnter++ % 30 == 0 ){ //定时器每执行30次这个生成蜜蜂,敌机对象的方法体会执行一次
                    enemys.add( next() ); //把按照一定概率生成的蜜蜂,敌机对象添加到敌人库中,然后在画板中将其画出显示
                }
            }

            /**
             * 按照相同的概率0.33返回蜜蜂和敌机对象
             * @return 返回小蜜蜂,小敌机,大敌机对象
             */
            private Common next(){
                int a = new Random().nextInt(30);
                if( a < 10 ){ //0-9 返回小蜜蜂
                    return new Bee();
                }else if( a < 20 ){ //10-19 返回小敌机
                    return new SmallEnemyPlane();
                }else { //20-29 返回大敌机
                    return new BigEnemyPlane();
                }
            }

            /**
             * 子弹入场
             */
            private int z2Enter = 0; //子弹入场的分频计数器
            private void bulletsEnter() {
                if( z2Enter++ % 20 == 0 ){ //子弹入场的分频系数
                    bullets.addAll( Arrays.asList( hero.shoot() ) ); //把子弹对象数组转为list集合添加到子弹集合中
                }
            }

            /**
             * 有敌机和子弹的入场
             * 就会有敌机和子弹的出场 否则内存一直会增加
             * 清除越界的敌机和子弹对象
             */
            private void clearEnemysAndBullets() {
                Iterator<Common> it_enemy = enemys.iterator();
                while ( it_enemy.hasNext() ){ //如果想在迭代过程中删除元素,只能用迭代器中的remove方法,而不可以使用集合的remove方法
                    if( it_enemy.next().isOut() ){
                        it_enemy.remove(); //必须是迭代器中的remove方法
                    }
                }

                Iterator<Bullet> it_bullet = bullets.iterator();
                while ( it_bullet.hasNext() ){
                    if( it_bullet.next().isOut() ){
                        it_bullet.remove(); //必须是迭代器中的remove方法  新循环和foreach都不可以使用
                    }
                }
            }

            /**
             * 天空,英雄机,子弹,小蜜蜂,小敌机,大敌机,
             * 这六个飞行物都需要移动
             * 英雄机的移动在鼠标挂载中进行实现了,在这里无需自动移动
             * 而是手动移动
             */
            private void step() {
                sky.step(); //天空的移动
                for( StepImpl a : bullets ){ //子弹的移动
                    a.step();
                }
                for( StepImpl a : enemys ){ //敌机的移动
                    a.step();
                }
            }


            /**
             * 敌机撞英雄机
             */
            private void EnemysBangHeros() {
                for( Common enemy : enemys ){
                    //在敌机存活,英雄机存活的前提下,敌机撞向英雄机
                    if( enemy.state == Common.LIVE && hero.state == Common.LIVE && enemy.isHit( hero ) ){
                        enemy.state = Common.DEAD; //敌机爆炸
                        hero.subLife(); //英雄机减命
                        hero.clearFire(); //英雄机火力清零
                    }
                }
            }

            /**
             * 敌机碰到子弹
             */
            private void EnemysBangBullets() {
                for( Common bullet : bullets ){
                    for( Common enemy : enemys ){
                        //在子弹存活,敌机存活的前提下,敌机碰到子弹
                        if( bullet.state == Common.LIVE && enemy.state == Common.LIVE && enemy.isHit( bullet ) ){
                            bullet.state = Common.DEAD; //子弹消失
                            enemy.state = Common.DEAD; //敌机爆炸后消失
                            if( enemy instanceof AwardTypeImpl ){ //敌机若是奖励类型,这个奖励类型只有小蜜蜂有
                                switch ( ( ( AwardTypeImpl ) enemy ).getAwardType() ){
                                    case AwardTypeImpl.FIRE: hero.addFire(); break; //火力奖励
                                    case AwardTypeImpl.LIFE: hero.addLife(); break; //生命值奖励
                                    default:
                                }
                            }
                            if( enemy instanceof ScoreImpl ){ //敌机值多少分
                                hero.setScore( hero.getScore() + ( ( ScoreImpl ) enemy ).getScore() );
                            }
                        }
                    }
                }
            }


            /**
             * 游戏结束触发检测
             */
            private void gameOver() {
                if( hero.getLife() <= 0 ){ //如果英雄机在命数为0时,还死了一次,则触发游戏结束策略
                    state = State.GameOver;
                }
            }

        }, 10, 11 ); //定时器会在延迟时间10ms后开始启动,执行一次初始动作,之后执行的周期是11ms
    }



}
