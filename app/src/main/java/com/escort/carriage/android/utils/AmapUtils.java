package com.escort.carriage.android.utils;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.cache.db.model.DataCacheKeyModel;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.listener.SimpleOnTrackLifecycleListener;
import com.tripartitelib.android.amap.ProjectLocationEntity;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-24 10:53
 */
public class AmapUtils {

    public final static String GAODE_PKG = "com.autonavi.minimap";//高德地图的包名
    private static AmapUtils amapUtils;
    private TrackParam trackParam;
    OnTrackLifecycleListener onTrackLifecycleListener;
    private AmapUtils() {
    }

    public static AmapUtils getAmapUtils() {
        if (amapUtils == null) {
            synchronized (AmapUtils.class) {
                if (amapUtils == null) {
                    amapUtils = new AmapUtils();
                }
            }
        }
        return amapUtils;
    }

    public void startLocation(){
        AMapLocationClient mLocationClient = new AMapLocationClient(ApplicationContext.getInstance().context);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListenerIm());
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //15s 定位一次
        mLocationOption.setInterval(15000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 获取实时定位的位置
     * @param listener
     */
    public void getLocation(AMapLocationListener listener) {
        AMapLocationClient mLocationClient = new AMapLocationClient(ApplicationContext.getInstance().context);
        mLocationClient.setLocationListener(listener);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }


    /**
     * 获取最后一次定位位置
     * @return
     */
    public ProjectLocationEntity getLocation(){
        ProjectLocationEntity projectDataEntity = CacheDBMolder.getInstance().getProjectDataEntity(DataCacheKeyModel.END_LOCATION, ProjectLocationEntity.class);
        return projectDataEntity;
    }


   public static class AMapLocationListenerIm implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    cacheLocation(aMapLocation);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtils.showI("AmapUtils", "AmapError   location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
        /**
         * 缓存定位信息数据
         * @param amapLocation
         */
        private void cacheLocation(AMapLocation amapLocation) {
            ProjectLocationEntity entity = new ProjectLocationEntity();
            entity.errorCode = amapLocation.getErrorCode();
            entity.latitude = amapLocation.getLatitude();
            entity.longitude = amapLocation.getLongitude();
            entity.locationType = amapLocation.getLocationType();//定位类型
            entity.accuracy = amapLocation.getAccuracy();//精度
            entity.provider = amapLocation.getProvider();//提供者
            entity.speed = amapLocation.getSpeed();//速    度
            entity.bearing = amapLocation.getBearing();//角    度
            entity.satellites = amapLocation.getSatellites();//星    数
            entity.country = amapLocation.getCountry();//国    家
            entity.province = amapLocation.getProvince();//省
            entity.city = amapLocation.getCity();//市
            entity.cityCode = amapLocation.getCityCode();//城市编码
            entity.district = amapLocation.getDistrict();//区
            entity.adCode = amapLocation.getAdCode();//区域 码
            entity.address = amapLocation.getAddress();//地    址
            entity.poiName = amapLocation.getPoiName();//兴趣点
            entity.createTime = System.currentTimeMillis();//创建时间
            entity.errorInfo = amapLocation.getErrorInfo();//错误信息
            entity.locationDetail = amapLocation.getLocationDetail();//错误描述
            String endLocationJson = JsonManager.createJsonString(entity);
            CacheDBMolder.getInstance().setProjectDataEntity(DataCacheKeyModel.END_LOCATION, endLocationJson, 1, -1);
        }

    }


    /**
     * 打开高德地图进行导航
     * @param context
     * @param latitude
     * @param longitude
     */
    public void openGaoDe(Context context, double latitude, double longitude) {
        if(checkMapAppsIsExist(context, GAODE_PKG)){
            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://navi?sourceApplication=" + getAppName(context) + "&lat=" + latitude + "&lon=" + longitude + "&dev=0"));
            intent.setPackage(GAODE_PKG);
            context.startActivity(intent);
        } else {
            ToastUtil.showToastString("请安装高德地图");
        }

    }

    /**
     * 检测地图应用是否安装
     *
     * @param context
     * @param packagename
     * @return
     */
    public boolean checkMapAppsIsExist(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public String getAppName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(context.getApplicationInfo()));
        } catch (Throwable e) {

        }
        return null;
    }


    // -------------------------------------------  猎鹰轨迹 配置 开始

    private AMapTrackClient aMapTrackClient;
    //   private TextureMapView mapView;
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private static final String TAG = "AmapUtils";
    private boolean isServiceRunning;
    private boolean isGatherRunning;
    Notification notification;

    private OnTrackLifecycleListener onTrackListener = new SimpleOnTrackLifecycleListener() {
        @Override
        public void onBindServiceCallback(int status, String msg) {
            LogUtils.showI(TAG, "onBindServiceCallback, status: " + status + ", msg: " + msg);
        }

        @Override
        public void onStartTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_TRACK_SUCEE || status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
                // 成功启动
                LogUtils.showI("AmapUtils", "启动服务成功");
                aMapTrackClient.startGather(onTrackListener);
                isServiceRunning = true;
            } else if (status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 已经启动
                LogUtils.showI("AmapUtils", "服务已经启动");
                aMapTrackClient.startGather(onTrackListener);
                isServiceRunning = true;
            } else {
                LogUtils.showI("AmapUtils", "error onStartTrackCallback, status: " + status + ", msg: " + msg);
            }

        }

        @Override
        public void onStopTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
                // 成功停止
                LogUtils.showI("AmapUtils", "停止服务成功");
                isServiceRunning = false;
                isGatherRunning = false;
            } else {
                LogUtils.showI("AmapUtils", "error onStopTrackCallback, status: " + status + ", msg: " + msg);
            }
        }

        @Override
        public void onStartGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_GATHER_SUCEE) {
                LogUtils.showI("AmapUtils", "定位采集开启成功");
                isGatherRunning = true;
            } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                LogUtils.showI("AmapUtils", "定位采集已经开启");
                isGatherRunning = true;
            } else {
                LogUtils.showI("AmapUtils", "error onStartGatherCallback, status: " + status + ", msg: " + msg);
            }

        }

        @Override
        public void onStopGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
                LogUtils.showI("AmapUtils", "定位采集停止成功 ");
                isGatherRunning = false;
            } else {
                LogUtils.showI("AmapUtils", "error onStopGatherCallback, status: " + status + ", msg: " + msg);
            }
        }
    };



    public void initTrace(Context context, Long sid, Long tid, Long trid,Notification notification) {
        aMapTrackClient = new AMapTrackClient(context.getApplicationContext());
        aMapTrackClient.setInterval(2, 60);//轨迹定位周期60s，上报周期200s
//        猎鹰sdk会在无法正常上报轨迹点时将未成功上报的轨迹点缓存在本地，默认最多缓存50MB数据。
//        可以使用下面的代码修改缓存大小为20MB：
        aMapTrackClient.setCacheSize(20);
        startTrack(notification,sid,tid,trid); //开启通知栏
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListenerIm());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setInterval(5000);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setDeviceModeDistanceFilter(100);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public void startTrack(Notification notification, long serviceId, long terminalId, long trackId) {
        Log.e("startTrack","开启位置上报"+serviceId);
        this.notification  = notification;
        if (!isGatherRunning || !isServiceRunning) {
            if(trackParam == null){
                trackParam = new TrackParam(serviceId, terminalId);
                trackParam.setTrackId(trackId);
            }
            if(trackParam.getTid() != terminalId || trackParam.getTrackId() != trackId){
                stopTrack();
                trackParam = new TrackParam(serviceId, terminalId);
                trackParam.setTrackId(trackId);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                trackParam.setNotification(notification);
            }
            aMapTrackClient.startTrack(trackParam, onTrackListener);
        }
    }

    public void stopTrack(){
        Log.e("stopTrack",">>token失效>>>");
        if(aMapTrackClient != null && trackParam != null){
            aMapTrackClient.stopTrack(trackParam, onTrackListener);
            trackParam = null;
        }
    }

//    private void startTrack() {
//        // 先根据Terminal名称查询Terminal ID，如果Terminal还不存在，就尝试创建，拿到Terminal ID后，
//        // 用Terminal ID开启轨迹服务
//        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constants.SERVICE_ID, Constants.TERMINAL_NAME), new SimpleOnTrackListener() {
//            @Override
//            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
//                if (queryTerminalResponse.isSuccess()) {
//                    if (queryTerminalResponse.isTerminalExist()) {
//                        // 当前终端已经创建过，直接使用查询到的terminal id
//                        terminalId = queryTerminalResponse.getTid();
//                        if (uploadToTrack) {
//                            aMapTrackClient.addTrack(new AddTrackRequest(Constants.SERVICE_ID, terminalId), new SimpleOnTrackListener() {
//                                @Override
//                                public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
//                                    if (addTrackResponse.isSuccess()) {
//                                        // trackId需要在启动服务后设置才能生效，因此这里不设置，而是在startGather之前设置了track id
//                                        trackId = addTrackResponse.getTrid();
//                                        TrackParam trackParam = new TrackParam(Constants.SERVICE_ID, terminalId);
//                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            trackParam.setNotification(createNotification());
//                                        }
//                                        aMapTrackClient.startTrack(trackParam, onTrackListener);
//                                    } else {
//                                        Toast.makeText(TrackServiceActivity.this, "网络请求失败，" + addTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        } else {
//                            // 不指定track id，上报的轨迹点是该终端的散点轨迹
//                            TrackParam trackParam = new TrackParam(Constants.SERVICE_ID, terminalId);
//                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                trackParam.setNotification(createNotification());
//                            }
//                            aMapTrackClient.startTrack(trackParam, onTrackListener);
//                        }
//                    } else {
//                        // 当前终端是新终端，还未创建过，创建该终端并使用新生成的terminal id
//                        aMapTrackClient.addTerminal(new AddTerminalRequest(Constants.TERMINAL_NAME, Constants.SERVICE_ID), new SimpleOnTrackListener() {
//                            @Override
//                            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
//                                if (addTerminalResponse.isSuccess()) {
//                                    terminalId = addTerminalResponse.getTid();
//                                    TrackParam trackParam = new TrackParam(Constants.SERVICE_ID, terminalId);
//                                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                        trackParam.setNotification(createNotification());
//                                    }
//                                    aMapTrackClient.startTrack(trackParam, onTrackListener);
//                                } else {
//                                    Toast.makeText(TrackServiceActivity.this, "网络请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    Toast.makeText(TrackServiceActivity.this, "网络请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//

}
