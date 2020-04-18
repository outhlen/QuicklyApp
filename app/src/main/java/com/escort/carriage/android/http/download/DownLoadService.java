package com.escort.carriage.android.http.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * 文件下载服务类
 */
public class DownLoadService extends Service{


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
