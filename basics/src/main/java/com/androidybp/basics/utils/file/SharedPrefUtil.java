package com.androidybp.basics.utils.file;

import android.content.Context;
import android.content.SharedPreferences;

import com.androidybp.basics.ApplicationContext;
/**
用来操作 SharedPreferences 文件
 */
@Deprecated
public class SharedPrefUtil {
    private SharedPreferences sp ;
    private static volatile SharedPrefUtil spu;

    private SharedPrefUtil(Context context){
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static SharedPrefUtil getSharedPrefUtil(){
        return getSharedPrefUtil(ApplicationContext.getInstance().getContext());
    }

    public static SharedPrefUtil getSharedPrefUtil(Context context){
        if(spu == null){
            synchronized (SharedPrefUtil.class){
                if(spu == null){
                    spu = new SharedPrefUtil(context);
                }
            }
        }
        return spu;
    }

    public SharedPreferences getSp(){
        return sp;
    }

    public  boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public  void putBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    public int getInt(String key,int value) {
    	return sp.getInt(key, value);//0是默认值
    }
    
    public void putInt(String key, int value) {
    	sp.edit().putInt(key, value).commit();
    }
    
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public void putString(String key, String value) {
        sp.edit().putString(key, value).commit();
    }
    
    public void removeString(String key) {
        sp.edit().remove(key).commit();
    }
    public void removeBoolean(String key) {
        removeString(key);
    }
    public void removeInt(String key) {
        removeString(key);
    }

}
