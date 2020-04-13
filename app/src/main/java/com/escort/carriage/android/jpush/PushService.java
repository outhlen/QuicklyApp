package com.escort.carriage.android.jpush;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import cn.jpush.android.service.JCommonService;

public class PushService extends JCommonService {
    private final static String TAG = PushService.class.getSimpleName();
    // 核心进程 Service ID
    private final static int CORE_SERVICE_ID = -5120;
    public static class CoreInnerService extends Service {
        @Override
        public void onCreate() {
            Log.i(TAG, "CoreInnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i(TAG, "CoreInnerService -> onStartCommand");
            startForeground(CORE_SERVICE_ID, new Notification());
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "CoreInnerService -> onDestroy");
            super.onDestroy();
        }
    }
}
