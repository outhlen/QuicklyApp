package com.androidybp.basics.utils.bitmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import com.androidybp.basics.R;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * 图片处理工具类
 */
public class ProjectBitmapUtils {
    private int px = 800;
    /**--------------------------------------------------------------------------------- Bitmap和 Base64进行转换----*/
    /**传入位图文件，将bitmap转为base64
     * @param bitmap  要转换的bitmap 对象
     * @return 当前bitmap对象对应的Base64对象
     */
    /**
     * bitmap转为base64
     *
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
                bitmap.recycle();
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
                if (bitmap != null) {
                    bitmap.recycle();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 传入字符串，将字符串转换成Bitmap类型
     * base64转为bitmap
     *
     * @param base64Data base64字符串
     * @return 缩放好的bitmap对象
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * @param bitmap bitmap 对象
     * @param w      要缩放到的最小宽度px  宽高相同
     * @return 缩放好的bitmap对象
     */
    public Bitmap setBitmapSize(Bitmap bitmap, int w) {
        return setBitmapSize(bitmap, w, w);
    }

    public Bitmap setBitmapSize(Bitmap bitmap, int w, int h) {
        if (bitmap == null)
            return null;
        if (w <= 0 && h <= 0)
            return bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        if (scaleHeight > scaleWidth) {
            scaleHeight = scaleWidth;
        } else if (scaleHeight < scaleWidth) {
            scaleWidth = scaleHeight;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true);
        return bitmap;
    }

    public Bitmap setAssignBitmapSize(Bitmap bitmap, int w, int h) {
        if (bitmap == null)
            return null;
        if (w <= 0 && h <= 0)
            return bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true);
        return bitmap;
    }

    /**
     * 使用先加载尺寸在去真正加载图片
     *
     * @param srcPath 图片路径
     * @param x       宽度
     * @param y       高度
     * @return bitmap对象
     */
    public Bitmap compressImageFromFile(String srcPath, float x, float y) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && w > x) {
            be = (int) (newOpts.outWidth / x);
        } else if (w < h && h > y) {
            be = (int) (newOpts.outHeight / y);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //其实是无效的,大家尽管尝试
        return bitmap;
    }

    /**
     * 将Bitmap添加到内存
     *
     * @param bitmap bitmap 对象
     * @param path   要保存的路径
     * @return 保存完成的路径  return null 说明保存失败
     */
    public String setCameraBitmap(Bitmap bitmap, String path) {
        if (TextUtils.isEmpty(path))
            return null;
//        bitmap = addBitmapFlagText(setBitmapSize(bitmap, px));
        bitmap = setBitmapSize(bitmap, px);
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            if (compress)
                return path;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @param path 文件的路径
     * @param w    缩放的最小宽度
     * @param h    缩放到的最小高度
     * @return 根据给定的最小高度和宽度将当前的图片进行缩放  缩放规则是  按照当前最小的比例进行缩放   不是指定的缩放
     */
    public Bitmap setBitmapPath(String path, int w, int h) {

        if (TextUtils.isEmpty(path))
            return null;
        int degree = readPictureDegree(path);
        if (w == -1 && h == -1) {
            try {
                return BitmapFactory.decodeFile(path);
            } catch (Exception e) {

            }
        } else if (w <= 0 && h <= 0) {
            return null;
        }
//        return setBitmapSize( rotate(BitmapFactory.decodeFile(path), degree) , w, h);
        return rotate(compressImageFromFile(path, w, h), degree);
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 调整图片的位置
     *
     * @param b       需要旋转的图片bitmap对象
     * @param degrees 旋转的角度
     * @return 旋转完成之后的bitmap对象
     */
    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees == 0) {
            return b;
        }
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
//            m.setRotate(degrees, (float) b.getWidth() ,
//                    (float) b.getHeight() );
            m.preRotate(degrees);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {

            }
        }
        return b;
    }

    /**
     * 高质量将图片进行压缩
     *
     * @param path    图片的路径
     * @param newPath 新的路径  可以为空   如果为空 将原来的图片进行压缩
     * @param flag    true -- 添加水印   false -- 不添加水印
     * @return 新图片的地址
     */
    public String zoomPhoto(String path, String newPath, boolean flag) {
        if (TextUtils.isEmpty(path))
            return null;
        Bitmap bitmap = setBitmapPath(path, px, px);
        ////////////图片添加水印
        if (flag)
            bitmap = addBitmapFlagText(bitmap);
//        Bitmap bitmap = compressImage(path);
        File file = new File(TextUtils.isEmpty(newPath) ? path : newPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            if (compress)
                return newPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 图片添加水印
     *
     * @param bitmap 要添加水印的图片
     * @return 添加水印之后的bitmap对象
     */
    private Bitmap addBitmapFlagText(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap watermark = BitmapFactory.decodeResource(ResourcesTransformUtil.getResources(), R.drawable.ic_loading);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(bitmap, 0, 0, null);
        if (w >= ww && h >= wh) {
            cv.drawBitmap(watermark, (w / 2 - ww / 2), (h / 2 - wh / 2), null);// 将水印图片居中放
        } else {
            watermark = setAssignBitmapSize(watermark, w, h);
            cv.drawBitmap(watermark, (w / 2 - watermark.getWidth() / 2), (h / 2 - watermark.getHeight() / 2), null);// 先将水印图片缩放之后再来添加   现在是直接从0开始了
        }
//        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.save();// 保存
        cv.restore();// 存储

        //释放资源
        bitmap.recycle();
        watermark.recycle();
        return newb;
    }

    /**
     * 压缩图片大小不压缩尺寸   压缩到100kb以内
     *
     * @param path
     * @return
     */
    private Bitmap compressImage(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        int degree = readPictureDegree(path);
        Bitmap image = rotate(BitmapFactory.decodeFile(path), degree);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        image = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        try {
            isBm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * 将uri转换成path路径
     *
     * @param context 当前上下文
     * @param uri     从Uri获取文件绝对路径
     * @return 将uri转换成的path路径
     */
    @SuppressLint("NewApi")
    public static String getPathByUri4kitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * 得到这个Uri的数据列的值
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       当前上下文
     * @param uri           URI查询.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 存储器权限
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * 下载权限
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 多媒体权限
     *
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*
    获取video的路径uri
     */
    //return the id of URI
    public static int getVideoUriByPath(Uri resultUri, Activity activity, String path) {
        resultUri = null;
        int id = -1;
        if (activity == null || path == null)
            return id;
        Uri preUri = Uri.parse("content://media/external/video/media/");
        @SuppressWarnings("deprecation")
        Cursor cursor = activity.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            if (path.equals(data)) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                resultUri = Uri.withAppendedPath(preUri, "" + id);
                break;
            }
            cursor.moveToNext();
        }
        return id;
    }

    /**
     * 获取网络视频缩略图作为封面
     *
     * @param
     * @created by sll date 2018/1/8 16:34
     */
    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
            if (width == 0) {
                width = bitmap.getWidth();
                height = bitmap.getHeight();
            }
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    retriever.setDataSource(filePath, new Hashtable<String, String>());
                }
            } else {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap == null)
            return null;

//        if (kind== MediaStore.Images.Thumbnails.MINI_KIND) {
//            // Scale down the bitmap if it's too large.
//            int width= bitmap.getWidth();
//            int height= bitmap.getHeight();
//            int max =Math.max(width, height);
//            if(max >512) {
//                float scale=512f / max;
//                int w =Math.round(scale * width);
//                int h =Math.round(scale * height);
//                bitmap = Bitmap.createScaledBitmap(bitmap,w, h, true);
//            }
//        } else if (kind== MediaStore.Images.Thumbnails.MICRO_KIND) {
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                96,
                96,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        }
        return bitmap;
    }

    /***********************************************************************************************************
     * 一下是 分享 图片压缩方法   和上边的方法会冗余   后期记得来删除了    压缩方式是 大小 32k以内  大小 100 X 100
     ***********************************************************************************************************/


    public static byte[] compressShareImage(Drawable drawable) {
        if (drawable != null) {
            //将图片裁剪成方的
            Bitmap centerBitmap = centerSquareScaleBitmap(drawableToBitmap(drawable), 100);
            //将图片压缩到32K以内
            return bitmap2Bytes(centerBitmap, 30);
        }
        return null;

    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     * @param bitmap
     * @param maxkb
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb&& options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }

    /**
     * 将 drawable 转换成bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        try {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof NinePatchDrawable) {
                // 取 drawable 的长宽
                int w = drawable.getIntrinsicWidth();
                int h = drawable.getIntrinsicHeight();

                // 取 drawable 的颜色格式
                Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
                // 建立对应 bitmap
                Bitmap bitmap = Bitmap.createBitmap(w, h, config);
                // 建立对应 bitmap 的画布
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, w, h);
                // 把 drawable 内容画到画布中
                drawable.draw(canvas);
                return bitmap;
            }
        } catch (Exception e) {

        }

        return null;
    }


    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }
}
