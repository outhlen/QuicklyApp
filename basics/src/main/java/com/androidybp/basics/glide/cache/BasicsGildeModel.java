package com.androidybp.basics.glide.cache;

import android.content.Context;
import androidx.annotation.NonNull;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by yangbp
 * 给Glide 配置 内存占用 类
 */
@GlideModule
public class BasicsGildeModel extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 图片内存20MB
        int diskCacheSizeBytes = 1024 * 1024 * 100;  //磁盘缓存100 MB
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes))
                .setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
                 //将图片格式默认设置成 565
//                .setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.RGB_565).disallowHardwareBitmaps());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        //返回false 不去兼容低版本  去加载 maifest中的配置
        return false;
    }
}
