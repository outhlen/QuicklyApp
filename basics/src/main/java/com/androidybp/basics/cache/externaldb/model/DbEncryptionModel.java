package com.androidybp.basics.cache.externaldb.model;

import android.text.TextUtils;

import com.androidybp.basics.utils.encryption.AESOperator;

/**
 * 外部储存缓存数据库  对应key 集中管理类
 *
 */
public class DbEncryptionModel {

    private static DbEncryptionModel model;


    /**
     * 生成AES秘钥的配置参数    这里切记不要修改
     */
    private String key = "qazxswedcvfrtgbn";

    /**
     * 全局通用加密令牌，在不区分用户的情况下 通一用一种令牌  这个绝对不能修改
     */
    public final String PROJECTTOKEN = "cctin";

    /**
     * 1、
     * 用于保存通讯录的类型  因为存在多个user同时登录一个手机问题  所以只保留一个类型 具体key是用user和当前的type进行拼接后返回
     * 这样就保存了通讯录跟账号绑定不在跟手机绑定了
     */
    private final String TELEPHONE_TYPE = "telephones";

    /**
     * 2、
     * 用于保存首页最新买卖信息的  方便快速显示的
     */
    public final String HOME_TRADE_INFORMATION = "homeTradeInformation";


    private DbEncryptionModel(){

    }

    public static DbEncryptionModel getInstance() {

        if (model == null){
            synchronized (DbEncryptionModel.class){
                if(model == null){
                    model = new DbEncryptionModel();
                }
            }
        }

        return model;
    }


    /**
     * ★ 加密   使用全局参数教你学加密
     * @param str     需要加密的内容
     * @return 密文
     */
    public String zstEncryption(String str){
        if(str == null || str.trim().equals("")){
            return "";
        } else {
            return AESOperator.getInstance().encrypt(getAesKey(PROJECTTOKEN), str);
        }
    }

    /**
     * ★ 加密   使用全局参数教你学加密
     * @param str     需要加密的内容
     * @return 密文
     */
    public String zstDecrypt(String str){
        return AESOperator.getInstance().decrypt(getAesKey(PROJECTTOKEN), str);
    }



    /**
     * 根据指定的令牌  将密文加密
     * @param userId  用户的userId
     * @param str     需要加密的内容
     * @return 密文
     */
    public String getValue(String userId, String str){
        return AESOperator.getInstance().encrypt(getAesKey(userId), str);
    }

    /**
     * 返回保存用户通讯录的key的方法 该方法已经通过AES加密处理过了
     * 令牌就是用户的user
     * @param userId
     * @return
     */
    public String getUserTelephoneKey(String userId){
       return AESOperator.getInstance().encrypt(getAesKey(userId), userId + TELEPHONE_TYPE);
    }

    /**
     * 给定指定字符串 生成加密秘钥
     * @param str 需要加密的令牌
     * @return
     */
    private String getAesKey(String str){
        if(TextUtils.isEmpty(str)){
            return key;
        } else if(str.length() < 16){
            int maxLen = 16;
            int len = maxLen - str.length();
            str = str + key.substring(0, len);
            return str;
        }else if(str.length() < 32){
            return str;
        }else{
            return key;
        }
    }

}
