package com.androidybp.basics.utils.encryption;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.os.Build;

/**
 *
 *
 * @Description: AES加密解密工具类
 * @author yangbp
 * @date 2017-11-05
 *
 */
@SuppressLint("TrulyRandom")
public class AESUtils {
    private  static final String AES = "AES";//AES 加密
    private final static String HEX = "0123456789ABCDEF";
    private final static int JELLY_BEAN_4_2 = 17;

    /**
     * 加密
     *
     * @param key
     *            密钥
     * @param src
     *            加密文本
     * @return
     */
    public static String encrypt(String key, String src) {
        try {
            byte[] rawKey = getRawKey(key.getBytes());
            byte[] result = encrypt(rawKey, src.getBytes());
            return toHex(result);
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 解密
     *
     * @param key
     *            密钥
     * @param encrypted
     *            待揭秘文本
     * @return
     */
    public static String decrypt(String key, String encrypted){
        try {
            byte[] rawKey = getRawKey(key.getBytes());
            byte[] enc = toByte(encrypted);
            byte[] result = decrypt(rawKey, enc);
            return new String(result);
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 获取256位的加密密钥
     *
     * @param seed
     * @return
     */
    @SuppressLint("TrulyRandom")
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        SecureRandom sr = null;
        //Android N上 google去掉了Crypto provider，意味着我们将不能继续像上面那样对数据加密填充
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sr = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
        } else if (Build.VERSION.SDK_INT >= JELLY_BEAN_4_2) {
            // 在4.2以上版本中，SecureRandom获取方式发生了改变
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();//生成一个密钥
        byte[] raw = skey.getEncoded();
        return raw;
    }

    /**
     * 真正的加密过程
     *
     * @param key
     * @param src
     * @return
     */
    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    /**
     * 真正的解密过程
     *
     * @param key
     * @param encrypted
     * @return
     */
    private static byte[] decrypt(byte[] key, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
