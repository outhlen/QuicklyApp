package com.androidybp.basics.glide;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;
import com.androidybp.basics.ApplicationContext;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class GlideManager {
    private static volatile GlideManager glideManager;

    private GlideManager() {
    }

    /**
     * 单例获取 当前类的实例
     *
     * @return
     */
    public static GlideManager getGlideManager() {
        if (glideManager == null) {
            synchronized (GlideManager.class) {
                if (glideManager == null) {
                    createGlideManager();
                }
            }
        }
        return glideManager;
    }

    private static void createGlideManager() {
        glideManager = new GlideManager();
    }


/********************************************************************************************************************
 *                自定义方法
 ********************************************************************************************************************/
    /**
     * 清空Glide内存占用
     */
    public void clearMemory() {
        if (ApplicationContext.getInstance().context != null) {
            Glide.get(ApplicationContext.getInstance().context).clearMemory();
        }
    }

    /**
     * 根据给定的级别，以准确的数量清除一些内存。 Glide自己判断操作
     *
     * @param level
     */
    public void trimMemory(int level) {
        if (ApplicationContext.getInstance().context != null) {
            Glide.get(ApplicationContext.getInstance().context).trimMemory(level);
        }
    }

    /**
     * 开始加载图片
     */
    public void resumeRequests() {
        if (ApplicationContext.getInstance().context != null) {
            Glide.with(ApplicationContext.getInstance().context).resumeRequests();
        }
    }

    /**
     * 停止加载图片
     */
    public void pauseRequests() {
        Glide.with(ApplicationContext.getInstance().context).pauseRequests();
    }


    /**
     * 在Activity中使用图片 加载   跟当前Activity进行绑定
     * 设置当前加载的图片  不做任何缓存（内存和硬件）
     *
     * @param act       Activity对象
     * @param path      图片加载地址
     * @param imageView 展示图片的控件
     * @param placeid   加载时候的占位图片 资源地址
     */
    public void loadActImageIsNoCache(Activity act, String path, ImageView imageView, int placeid) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(placeid) //占位图
                .error(placeid);       //错误图

        Glide.with(act)
                .asDrawable()
                .load(path)
                .apply(options)
                .into(imageView);
    }

    /**
     * 临时方法 项目优化临时使用
     * @param file
     * @param imageView
     */
    @Deprecated
    public void loadImageFileCenterCrop(Activity activity, File file, ImageView imageView){
        RequestOptions options = new RequestOptions()
                .centerCrop();
        Glide.with(activity).asBitmap().load(file).apply(options).into(imageView);
    }

    /**
     * 临时方法 项目优化临时使用
     * @param file
     * @param imageView
     */
    @Deprecated
    public void loadImageFileCenterCrop(File file, ImageView imageView, int placeid){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeid) //占位图
                .error(placeid);       //错误图
        Glide.with(ApplicationContext.getInstance().context).asBitmap().load(file).apply(options).into(imageView);
    }

    /**
     * 临时方法 项目优化临时使用
     */
    @Deprecated
    public void loadImageErrorRes(String path, ImageView imageView, int errorRes){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(errorRes);       //错误图
        Glide.with(ApplicationContext.getInstance().context).asBitmap().load(path).apply(options).into(imageView);
    }



    /**
     * 临时方法 项目优化临时使用
     * @param imageView
     */
    @Deprecated
    public void loadImageUriCenterCrop(Activity activity, Uri uri, ImageView imageView){
        RequestOptions options = new RequestOptions()
                .centerCrop();
        Glide.with(activity).asBitmap().load(uri).apply(options).into(imageView);
    }

    /**
     * 临时方法 项目优化临时使用
     * @param imageView
     */
    @Deprecated
    public void loadImageCenterCrop(Activity activity, String path, ImageView imageView, int placeid, int errRes){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeid) //占位图
                .error(errRes);       //错误图
        Glide.with(activity).load(path).apply(options).into(imageView);

    }
    /**
     * 临时方法 项目优化临时使用
     */
    @Deprecated
    public void loadImageCrossFadePath(String path, ImageView imageView, int duration, int errRes){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(errRes);       //错误图
        Glide.with(ApplicationContext.getInstance().context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade(duration)).into(imageView);

    }

    /**
     * @param path      图片加载地址
     * @param imageView 展示图片的控件
     */
    public void loadImage(String path, ImageView imageView) {
        Glide.with(ApplicationContext.getInstance().context).asDrawable().load(path).into(imageView);
    }

    /**
     * @param path      图片加载地址
     * @param imageView 展示图片的控件
     * @param placeid   加载时候的占位图片 资源地址
     */
    public void loadImage(String path, ImageView imageView, int placeid) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeid) //占位图
                .error(placeid);       //错误图
        Glide.with(ApplicationContext.getInstance().context).asDrawable().load(path).apply(options).into(imageView);
    }

    /**
     * 临时方法 项目优化临时使用
     */
    @Deprecated
    public void loadImageFile(File file, ImageView imageView) {
        Glide.with(ApplicationContext.getInstance().context).load(file).into(imageView);

    }


    public void loadImageRoundFitCrop(String headPictureUrl, ImageView ivHeadImg, int num, int defaultRes) {
        RequestOptions options = new RequestOptions()
                .error(defaultRes)
                .transform(new RoundFitCropTransform(num));
        Glide.with(ApplicationContext.getInstance().context).load(headPictureUrl).apply(options)
                .into(ivHeadImg);
    }
}
