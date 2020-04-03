package com.androidybp.basics.utils.encryption;

import android.content.Context;
import android.text.TextUtils;

import com.androidybp.basics.ApplicationContext;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 * <p>
 * java  Android  通用的  一个工具类
 */
public class AESOperator {

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private String sKey = "smkldospdosldaaa";//key，不要随意修改  这里和热更新的key 是关联的
    private static String ivParameter = "0392039203920300";//偏移量,
    private static AESOperator instance = null;

    private AESOperator() {

    }

    public static AESOperator getInstance() {
        if (instance == null)
            instance = new AESOperator();
        return instance;
    }

    /**
     * 返回tinke热更新秘钥
     * @param context
     * @return
     */
    public String getTinkerKey(Context context){
        String versionName = ApplicationContext.getInstance().getVersionName(context);
        int maxLen = 16;
        int len = maxLen;
        if(!TextUtils.isEmpty(versionName)){
            versionName = versionName.replace(".", "_");
            len = maxLen - versionName.length();
        }
        versionName = versionName + sKey.substring(0, len);
        return versionName;
    }

    /**
     * 加密
     * 使用固定的  key 进行加密  sKey    固定的 偏移量  ivParameter
     *
     * @param sSrc
     * @return
     */
    public String encrypt(String sSrc) {
        String encryptStr;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            encryptStr = Base64Utils.encodeByte(encrypted);// 此处使用BASE64做转码。
        } catch (Exception e) {
            encryptStr = null;
        }
        return encryptStr;
    }

    /**
     * 加密
     * @param key     前面key
     * @param content 加密内容
     * @return
     */
    public String encrypt(String key, String content) {
        return encrypt(key, content, ivParameter);
    }

    /**
     * 加密
     *
     * @param secretKey 秘钥 key  必须是16位  在java要求秘钥必须是16位以上    在个别版本会出现 32位以及以上位数也报错
     *                  这里尽量使用16位秘钥
     * @param encData   需要加密的字符串
     * @param vector    加密偏移量
     * @return
     */
    public String encrypt(String secretKey, String encData, String vector) {
        String encryptStr;
        try {
            if (secretKey == null || secretKey.length() < 16) {
                encryptStr = null;
            } else {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                byte[] raw = secretKey.getBytes();
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
                encryptStr = Base64Utils.encodeByte(encrypted);// 此处使用BASE64做转码。

            }
        } catch (Exception e) {
            encryptStr = null;
        }
        return encryptStr;
    }


    /**
     * 解密
     * 使用固定的  key 进行加密  sKey    固定的 偏移量  ivParameter
     * @param sSrc  解密内容
     * @return
     */
    public String decrypt(String sSrc) {
        String decryptStr;
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64Utils.decodeByte(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            decryptStr = originalString;
        } catch (Exception ex) {
            decryptStr = null;
        }
        return  decryptStr;
    }

    /**
     * 解密
     * @param key     前面key
     * @param content 加密内容
     * @return
     */
    public String decrypt(String key, String content) {
        return decrypt(key, content, ivParameter);
    }

    /**
     *  解密
     * @param key      秘钥  必须是16位
     * @param sSrc     解密内容
     * @param ivs      偏移量
     * @return
     */
    public String decrypt(String key, String sSrc, String ivs){
        String decryptStr;
        try {
            if (TextUtils.isEmpty(key) || key.length() != 16) {
                decryptStr = null;
            } else {
                byte[] raw = key.getBytes("ASCII");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                byte[] encrypted1 = Base64Utils.decodeByte(sSrc);// 先用base64解密
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                decryptStr = originalString;
            }
        } catch (Exception ex) {
            decryptStr = null;
        }
        return decryptStr;
    }

    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }

}
