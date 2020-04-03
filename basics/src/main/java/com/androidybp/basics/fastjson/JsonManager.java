package com.androidybp.basics.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.androidybp.basics.utils.hint.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2016/3/17.
 * 这是一个JSON生成和解析的类
 */
public class JsonManager {

    /**
     * 将JSON串解析成一个对象
     */
    public static <T> T getJsonBean(String json, Class<T> clazz) {
        if(json == null)
            return null;
        T t = null;
        try {
            t = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.showE("JsonManager", "json----->Bean失败");
        }
        return t;
    }

    /**
     * 将JSON串解析成一个List集合
     */
    public static <T> List<T> getArrayBean(String json, Class<T> clazz) {

        List<T> list = new ArrayList<T>();
        try {
            list = JSON.parseArray(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.showE("JsonManager", "json --->List<Bean>失败");
        }
        return list;
    }

    /**
     * 将对象转换成Json对象
     */
    public static String createJsonString(Object object) {
        String jsonString = "";
        try {
            jsonString = JSON.toJSONString(object);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.showE("JsonManager", "fastJson转换对象失败");
        }

        return jsonString;
    }

    /**
     * 用fastjson 将jsonString 解析成 List<Map<String,Object>>
     *
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> getListMap(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            // 两种写法
            // list = JSON.parseObject(jsonString, new
            // TypeReference<List<Map<String, Object>>>(){}.getType());

            list = JSON.parseObject(jsonString,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.showE("JsonManager", "json ---->List<Map>失败");
        }
        return list;

    }
}
