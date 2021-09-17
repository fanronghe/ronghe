package com.fan.common2;

/**
 * 获取奖励的一类对象  奖励分为增加生命值 和增加火力值
 * @author fanronghe
 * @Date 21.9.16 下午 10:08
 */
public interface AwardTypeImpl {
    /**
     * 火力值奖励类型
     */
    int FIRE = 0;

    /**
     * 生命值奖励类型
     */
    int LIFE = 1;

    /**
     * 获取一类对象的奖励类型
     * 可以用 AwardTypeImpl.FIRE 和 AwardTypeImpl.LIFE分别表示火力值奖励
     * 和生命值奖励
     * @return 奖励的类型
     */
    int getAwardType();


}
