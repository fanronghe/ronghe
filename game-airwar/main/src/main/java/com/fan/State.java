package com.fan;

/**
 * 枚举类型State 包括4个状态
 * @author fanronghe
 * @Date 21.9.17 下午 12:42
 */
public enum State {
    IDLE, //会被编译为 等价于  public static final State IDLE 修饰的常量
    Running,
    Pause,
    GameOver
}
