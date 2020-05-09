package com.escort.carriage.android;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.utils.AmapUtils;

public class LocationService extends Service {

    public PowerManager powerManager = null;
    public String sid,trid,deviceId;
    PowerManager.WakeLock wakeLock;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LocationService>>","服务开启成功"+startId);
        sid  = intent.getStringExtra("sid");
        trid  = intent.getStringExtra("tid");
        deviceId  = intent.getStringExtra("deviceId");
        this.startForeground(startId, createNotification(getApplicationContext()));
        crateAlarmRun();
        //设备信息 上传参数
        AmapUtils.getAmapUtils().initTrace(getApplicationContext(), Long.valueOf(sid), Long.valueOf(deviceId),
           Long.valueOf(trid), createNotification(getApplicationContext()));
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     *  创建定时广播
     */
    private void crateAlarmRun() {
        Intent intent = new Intent("LOCATION_CLOCK");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        // 每五秒唤醒一次
        long second = 5 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), second, pendingIntent);
    }

    /**
     * PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
     * SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
     * SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
     * FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
     * ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
     * ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
     */
    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, getClass()
                    .getCanonicalName());
            if (null != wakeLock) {
                Log.d("33333", "call acquireWakeLock");
                wakeLock.acquire();
            }
        }
    }

    private void setScreenLight() {
        if (powerManager == null) {
            powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = powerManager
                    .newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "xiaoer:bright");
            wl.acquire();
            //点亮屏幕
            wl.release();
            //释放
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
    @SuppressLint("WrongConstant")
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification createNotification(Context context) {
        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID_SERVICE_RUNNING", "app service", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(context.getApplicationContext(), "CHANNEL_ID_SERVICE_RUNNING");
        } else {
            builder = new Notification.Builder(context.getApplicationContext());
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        Intent nfIntent = new Intent(ApplicationContext.getInstance().context, context.getClass());
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(ApplicationContext.getInstance().context, 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(ResourcesTransformUtil.getString(R.string.app_name) + "运行中");
               // .setContentText(ResourcesTransformUtil.getString(R.string.app_name) + " 点击查看详情");
        Notification notification = builder.build();
        return notification;
    }
}
