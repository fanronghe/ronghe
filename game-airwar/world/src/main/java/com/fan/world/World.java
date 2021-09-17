package com.fan.world;

/**
 * 防止出现循环依赖
 * 因此在需要使用游戏界面的宽和高时可以依赖这个项目,而不是像原来那样依赖main项目,
 * 那样会导致循环依赖的错误
 * @author fanronghe
 * @Date 21.9.17 下午 3:57
 */
public class World {
    public static final int WIDTH = 400; //游戏画面的宽度
    public static final int HEIGHT = 700; //游戏画面的高度
}
