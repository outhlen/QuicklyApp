package com.androidybp.basics.utils.resources;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;
import com.androidybp.basics.configuration.BaseProjectConfiguration;

import java.io.File;

/**
 * Created by yangbp.
 * Uri 兼容 7.0
 */

public class Fileprovider {

    public Uri getUri(Context context, File file) {
        Uri uri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // "com.zst.fileprovider"即是在清单文件中配置的authorities
                uri = FileProvider.getUriForFile(context, BaseProjectConfiguration.FILEPROVIDER, file);
            } else {
                uri = Uri.fromFile(file);
            }
        } catch (Exception e) {

        }
        return uri;
    }

    public Uri getUri(Context context, String path) {
        Uri uri = null;
        File file = new File(path);
        if (file != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // "com.zst.fileprovider"即是在清单文件中配置的authorities
                    uri = FileProvider.getUriForFile(context, BaseProjectConfiguration.FILEPROVIDER, file);
                } else {
                    uri = Uri.fromFile(file);
                }
            } catch (Exception e) {

            }
        }
        return uri;
    }

    public Uri getUri(Context context, File file, Intent intent) {
        Uri uri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (intent != null) {
                    // 给目标应用一个临时授权
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                // "com.zst.fileprovider"即是在清单文件中配置的authorities
                uri = FileProvider.getUriForFile(context, BaseProjectConfiguration.FILEPROVIDER, file);
            } else {
                uri = Uri.fromFile(file);
            }
        } catch (Exception e) {

        }
        return uri;

    }

}
