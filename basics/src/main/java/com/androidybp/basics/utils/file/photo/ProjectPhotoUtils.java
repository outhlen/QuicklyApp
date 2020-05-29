package com.androidybp.basics.utils.file.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.utils.file.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理工具类
 */
public class ProjectPhotoUtils {
    /**
     * 压缩通用
     *
     * @param uri
     */
    public static File compressImage(Uri uri) {
        return compressImage(uri, 960, 400);
    }

    public static File compressImage(String path) {
        return compressImageCopy(path, 960, 400);
    }

    /**
     *
     * @param uri
     * @param width   最大宽度
     * @param height  最大高度
     * @param size    最大占用空间  kb单位
     * @return file对象
     */
    public static File compressImage(Uri uri, int width, int height, int size) {
        File file = null;
        try {
            InputStream input = ApplicationContext.getInstance().application.getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            int originalWidth = onlyBoundsOptions.outWidth;
            int originalHeight = onlyBoundsOptions.outHeight;
            if ((originalWidth == -1) || (originalHeight == -1))
                return null;
            //图片分辨率以480x800为标准
            float hh = height;//这里设置高度为800f
            float ww = width;//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (originalWidth / ww);
            } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
                be = (int) (originalHeight / hh);
            }
            if (be <= 0)
                be = 1;
            //比例压缩
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = be;//设置缩放比例
            bitmapOptions.inDither = true;//optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
            input = ApplicationContext.getInstance().application.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            file = new File(FileUtil.getProjectPicturesPath() + File.separator + System.currentTimeMillis() + ".png");
            compressBmpToFile(bitmap, file, size);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     *
     * 只通过宽度来压缩图片
     * @param uri
     * @param width   最大宽度
     * @param size    最大占用空间  kb单位
     * @return file对象
     */
    public static File compressImage(Uri uri, int width, int size) {
        File file = null;
        try {
            InputStream input = ApplicationContext.getInstance().application.getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            int originalWidth = onlyBoundsOptions.outWidth;
            int originalHeight = onlyBoundsOptions.outHeight;
            if ((originalWidth == -1) || (originalHeight == -1))
                return null;
            //图片分辨率以480x800为标准
            float ww = width;//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (originalWidth / ww);
            }
            if (be <= 0)
                be = 1;
            //比例压缩
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = be;//设置缩放比例
            bitmapOptions.inDither = true;  //optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
            input = ApplicationContext.getInstance().application.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            file = new File(FileUtil.getProjectPicturesPath() + File.separator + System.currentTimeMillis() + ".png");
            compressBmpToFile(bitmap, file, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static File compressImageCopy(String url, int width, int size) {
        File file = null;
        try {
            InputStream input  = new FileInputStream(new File(url));
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            //input.close();
            int originalWidth = onlyBoundsOptions.outWidth;
            int originalHeight = onlyBoundsOptions.outHeight;
            if ((originalWidth == -1) || (originalHeight == -1))
                return null;
            //图片分辨率以480x800为标准
            float ww = width;//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (originalWidth / ww);
            }
            if (be <= 0)
                be = 1;
            //比例压缩
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = be;//设置缩放比例
            bitmapOptions.inDither = true;  //optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
            input = new FileInputStream(new File(url));
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            file = new File(FileUtil.getProjectPicturesPath() + File.separator + System.currentTimeMillis() + ".png");
            compressBmpToFile(bitmap, file, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 压缩图片 通过宽高来压缩
     *
     * @param bmp
     * @param file
     * @param num
     */
    public static void compressBmpToFile(Bitmap bmp, File file, int num) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;// 个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > num) {
            baos.reset();
            options -= 10;
            if (options == 0) {
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
                break;
            }
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        //回收图片，清理内存

        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
            System.gc();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}
