package com.escort.carriage.android.utils.mes;

import android.content.Context;
import android.content.Intent;

import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.ui.activity.web.VueActivity;

/**
 * 消息小红点 显示 使用工具类   全局配置  全局设置
 */
public class MesNumUtils {

    private static volatile MesNumUtils utils;

    private MesNumUtils(){}

    public static MesNumUtils getMesNumUtils(){
        if(utils == null){
            synchronized (MesNumUtils.class){
                if(utils == null){
                    utils = new MesNumUtils();
                }
            }
        }
        return utils;
    }

    /**
     * 打开我的信息列表
     * @param context
     */
    public void openMesAct(Context context){
        Intent intentMes = new Intent(context, VueActivity.class);
        intentMes.putExtra("url", VueUrl.myNews);
        context.startActivity(intentMes);
    }

}
