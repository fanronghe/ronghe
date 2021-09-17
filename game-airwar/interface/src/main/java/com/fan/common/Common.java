package com.fan.common;


import com.fan.world.World;

/**
 * @author fanronghe
 * @Date 21.9.16 下午 9:09
 */
public abstract class Common implements DrawImpl, StepImpl {

    /**
     * 每一个子类对象的状态属性: 活着  死了  清除
     */
    public static final int LIVE = 0;
    public static final int DEAD = 1;
    public static final int REMOVE = 2;

    /**
     * public 修饰的属性和方法,是公共的,一旦提供出去
     * 自己此后就不可对其更改,包括方法名,属性名,方法体,因为会与其调用者紧耦合
     * 而private修饰的属性和方法,是私有的,由于别人用不到,自己可以随意修改
     * 用private是无法传给子类的,对子类是不可见的,因此只能通过公共的构造方法,或get,set方法
     * 在子类中对超类中的属性赋值
     *
     * protected只能在本包中,和子类中使用,是private的变种,
     * 如果做到了protected修饰的资源所在类是单独成包(common包),
     * 则只能在子类中能看到这些属性了,对于其他对象(块)而言是不可见的
     * 避免了使用繁琐的get,set,和construct方法对超类对象中的属性进行操作
     */
    public int state = LIVE;

    /**
     * 每一个对象的坐标和大小的数据
     */
    protected int width = 0;
    protected int height = 0;
    protected int x = 0;
    protected int y = 0;

    /**
     * 判断该对象是否在屏幕外面,如果出了屏幕外则会被销毁
     * 这里实现方法体,是因为这个方法体被几乎大大部分子类对象所共用
     * @return ture则在屏幕外面
     */
    public boolean isOut(){
        return y > World.HEIGHT;
    }

    /**
     * 利用矩形碰撞算法
     * 判断两个飞行物是否发生碰撞
     * 这里实现方法体,是因为所有的飞行物对象都可用这个方法体进行检测
     * @param c 另一个飞行物对象
     * @return ture表示两个飞行物发生碰撞
     */
    public boolean isHit( Common c ){
        int x0 = x - c.width;
        int x1 = x + width;
        int y0 = y - c.height;
        int y1 = y + height;
        int x = c.x;
        int y = c.y;
        return x >= x0 && x <= x1 && y >= y0 && y <= y1;
    }

    /**
     * 获取图片时图片计数器
     */
    protected int count_getP = 0;


    /**
     * 获取x坐标的值
     * @return 对象的x坐标
     */
    @Override
    public int getX() {
        return x;
    }


    /**
     * 获取y坐标的值
     * @return 对象的坐标
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * 横纵坐标的单次移动步长
     */
    protected int xSpeed = 1;
    protected int ySpeed = 1;


}
