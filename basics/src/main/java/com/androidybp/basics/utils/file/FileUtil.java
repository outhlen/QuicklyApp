package com.androidybp.basics.utils.file;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;
import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.configuration.BaseProjectConfiguration;
import com.androidybp.basics.utils.thread.ThreadUtils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 文件相关工具类  （SD卡判断等）
 */
public class FileUtil {

    public static final String tempFolder = "temp";//临时文件夹
    private static DecimalFormat decimalFormatTow;


    /**
     * 获取文件跟路径
     * @return
     */
    private static String baseFilePath(String type){
        File externalFilesDir = ApplicationContext.getInstance().application.getExternalFilesDir(type);
        return externalFilesDir.toString();
    }

    /**
     * 获取图片保存地址
     * @return
     */
    public static String getProjectPicturesPath(){
        return baseFilePath(Environment.DIRECTORY_PICTURES);
    }

    /**
     * 获取文件下载 保存地址
     * @return
     */
    public static String getProjectDownloadPath(){
        return baseFilePath(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     * 获取文件下载 保存地址
     * @return
     */
    public static String getProjectDocumentsPath(){
        return baseFilePath("Documents");
    }

    public static void deletePicturesFiles(){
        ThreadUtils.openSonThread(new Runnable() {
            @Override
            public void run() {
                //清理图片文件文件夹
                deleteDirFile(getProjectPicturesPath());
            }
        });
    }


    /**************************************************************************************
     文件 集合  下载路径
     **************************************************************************************/

    /**
     * apk下载的路径
     *
     * @return apk下载的路径
     */

    public static String apkDownloadPath(String apkName) {
        if (TextUtils.isEmpty(apkName)){
            apkName = "apkName";
        }
        return getProjectDownloadPath() + File.separator + apkName + ".apk";
    }

    /**
     * 下载APk  的路径
     * @return
     */
    public static String downloadApkPath(){
        return apkDownloadPath("newapk");
    }



    /***************************************************************************************
     文件操作相关
     ***************************************************************************************/

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
     * 删除 文件夹里面的文件 不删除自己
     * @param fileName
     */
    public static void deleteDirFile(String fileName){
        File dir = new File(fileName);
        if (dir == null || !dir.exists())
            return;
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null || files.length < 1)
                return;
            for (File file : files) {
                if (file.isFile()) {
                    file.delete(); // 删除所有文件
                } else if (file.isDirectory()) {
                    deleteDir(file.getAbsolutePath()); // 递规的方式删除文件夹
                }
            }
        }
    }


}
