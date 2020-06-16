package com.escort.carriage.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.MapView;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.utils.AmapUtils;
import com.escort.carriage.android.utils.Utils;
import com.tripartitelib.android.amap.ProjectLocationEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapMangerActivity extends CheckPermissionsActivity  {

    @BindView(R.id.map_view)
    MapView mapView;

    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    private AlarmManager alarm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        initLocation();
        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"LOCATION"的intent
        alarmPi = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION");
        registerReceiver(alarmReceiver, filter);
        AmapUtils.getAmapUtils().startLocation(); //初始化定位服务

        if(null != alarm){
            //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 2*1000,
                    5 * 1000, alarmPi);
        }
    }


    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //开始定位
                case Utils.MSG_LOCATION_START:
                    ToastUtil.showToastString("正在定位...");
                    break;
                // 定位完成
                case Utils.MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
                    String result = Utils.getLocationStr(loc);
                    ToastUtil.showToastString(result);
                    break;
                //停止定位
                case Utils.MSG_LOCATION_STOP:
                    ToastUtil.showToastString("定位停止");
                    break;
                default:
                    break;
            }
        };
    };

    private void initLocation() {
        ProjectLocationEntity locationEntity =  AmapUtils.getAmapUtils().getLocation();
        if(locationEntity!=null){
            ToastUtil.showToastString("当前位置："+locationEntity.address);
        }
    }

    // 定位监听
//    @Override
//    public void onLocationChanged(AMapLocation loc) {
//        if (null != loc) {
//            Message msg = mHandler.obtainMessage();
//            msg.obj = loc;
//            msg.what = Utils.MSG_LOCATION_FINISH;
//            mHandler.sendMessage(msg);
//        }
//    }


    private BroadcastReceiver alarmReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("LOCATION")){
                AmapUtils.getAmapUtils().startLocation(); //初始化定位服务
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != alarmReceiver){
            unregisterReceiver(alarmReceiver);
            alarmReceiver = null;
        }
    }


}
