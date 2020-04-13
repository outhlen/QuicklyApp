package com.androidybp.basics.utils.encryption;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import java.io.*;
//转译
/**
 *
 * Base64加密和解密
 */
public class Base64Utils {
    /**
    加密
    * @param unencoded  要加密的String对象
     * @return 编码成UTF-8格式的字符串，DEFAULT 这个参数是默认，使用默认的方法来加密
     */
    public static String encode(String unencoded){
        try{
            return new String(Base64.encode(unencoded.getBytes("UTF-8"),Base64.DEFAULT));
        }catch (Exception e){
            return "";
        }

    }

    /**
    加密
    * @param unencoded  要加密的String对象
     * @return 编码成UTF-8格式的字符串，DEFAULT 这个参数是默认，使用默认的方法来加密
     */
    public static String encodeByte(byte[] unencoded){
        try{
            return new String(Base64.encode(unencoded,Base64.DEFAULT));
        }catch (Exception e){
            return "";
        }

    }

    /**
    解密
    * @param encoded  要解密的String对象
     *  @return 解码成字节
     */
    public static String decode(String encoded){
        try{
            return new String(Base64.decode(encoded.getBytes(), Base64.DEFAULT));
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 解密   返回是  数组格式的
     * @param encoded
     * @return
     */
    public static byte[] decodeByte(String encoded){
        try{
            return Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.NO_WRAP);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(File file){
        if(file == null){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(file);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.NO_WRAP);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * 将Base64编码转换为图片
     * @param base64Str
     * @param path
     * @return true
     */
    public static boolean base64ToFile(String base64Str,String path) {
        byte[] data = Base64.decode(base64Str,Base64.NO_WRAP);
        for (int i = 0; i < data.length; i++) {
            if(data[i] < 0){
                //调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
            os.flush();
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}
