package com.androidybp.basics.utils.file;

import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.configuration.BaseProjectConfiguration;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 文件操作相关辅助方法
 */
public class FileOperationUtils {


    /**
     * 检测是否正常
     *
     * @return sdcard 是否正常
     */
    public static boolean avaiableMedia() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 这个方法只是一个 辅助创建二级目录的方法   没有返回体
     * @param name 二级文件夹名称
     */
    public static void getTwoFolder(String name){
        if (!TextUtils.isEmpty(name) && avaiableMedia()) {
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/" + BaseProjectConfiguration.BASE_FOLDER);
            if (!file.exists())
                file.mkdirs();
            File file2 = new File(file, name);
            if (!file2.exists())
                file2.mkdirs();
            file = null;
            file2 = null;
        }
    }






    /**
     * 删除文件夹和文件夹里面的文件
     *
     * @param fileName 传入文件夹名（包含路径）
     */

    public static void deleteDir(String fileName) {
        File dir = new File(fileName);
        if (dir == null || !dir.exists())
            return;
        if (!dir.isDirectory()) {
            delFile(fileName);
            return;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length < 1)
            return;
        for (File file : files) {
            if (file == null && !file.exists())
                return;
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file.getAbsolutePath()); // 递规的方式删除文件夹
        }
//        dir.delete();// 删除目录本身
    }

    /**
     * 删除文件
     *
     * @param fileName 入文件名（包含路径）
     */

    public static void delFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    /**
     * 这种方法只能实用于 SD卡等可以上G的内存
     *
     * @param lon byte字节
     * @param str 要显示什么格式的 G  MB  KB  byte    G--表示可以到达的最高单位
     * @return 指定格式的String
     */
    public static String transformMemoryFormat(long lon, String str) {
        switch (str) {
            case "G":
                return Formatter.formatFileSize(ApplicationContext.getInstance().getContext(), lon);
            case "MB":
                return (int) (lon / (1024 * 1024)) + " MB";
            case "KB":
                return (int) (lon / 1024) + " KB";
            case "B":
                return lon + " B";
        }
        return null;
    }

    /**
     * 将字节直接转换成对应的数值   保留一位小数
     *
     * @param size
     * @param type
     * @return
     */
    public static float getPrintSize(float size, String type) {
        switch (type) {
            case "G":
                return getFloatTowDecimals(size / 1024 / 1024 / 1024);
            case "MB":
                return getFloatTowDecimals(size / 1024 / 1024);
            case "KB":
                return getFloatTowDecimals(size / 1024);
            case "B":
                return getFloatTowDecimals(size);
        }
        return -1;
    }

    /**
     * 将float保留两位小数
     *
     * @param size float数
     * @return
     */
    private static float getFloatTowDecimals(float size) {
        DecimalFormat decimalFormatTow = new DecimalFormat(".00");
        return Float.parseFloat(decimalFormatTow.format(size));
    }

}
