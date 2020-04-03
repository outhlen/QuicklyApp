package com.escort.carriage.android.jpush;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-16 14:45
 */
public class JpushConfig {
    // ----- 极光推送相关 start------------
    /**
     * 极光推送的log打印开关
     */
    public static final boolean JPUSH_DEBUG_MODE = true;

    /**
     * 极光推送设置别名序列号
     */
    public static final int JPUSH_SET_ALIAS = 10001;
    /**
     * 极光推送设置标签推序号号
     */
    public static final int JPUSH_SET_TAGS = 10002;
    /**
     * 清除极光推送的所有Tags
     */
    public static final int JPUSH_CLEAN_TAGS = 10003;
    /**
     * 删除极光推送别名
     */
    public static final int JPUSH_DELETE_ALIAS = 10004;

    // ----- 极光推送相关 end------------
}
