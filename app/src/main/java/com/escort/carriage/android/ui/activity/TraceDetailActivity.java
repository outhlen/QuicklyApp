package com.escort.carriage.android.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.HistoryTrack;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.TrackPoint;
import com.amap.api.track.query.model.HistoryTrackRequest;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointRequest;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.AddressListBean;
import com.escort.carriage.android.entity.bean.HistoryInfoEntity;
import com.escort.carriage.android.entity.bean.HistoryPoint;
import com.escort.carriage.android.entity.bean.OrderAddressBean;
import com.escort.carriage.android.entity.bean.OrderDirectionBean;
import com.escort.carriage.android.entity.bean.OrderListBean;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.home.DeviceInfoEntity;
import com.escort.carriage.android.entity.response.home.ResponseOrderInfoEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.listener.SimpleOnTrackListener;
import com.escort.carriage.android.utils.AMapUtil;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TraceDetailActivity extends ProjectBaseActivity {

    @BindView(R.id.map_view)
    MapView mMapView;
    //初始化地图控制器对象
    AMap aMap;
    String TAG = getClass().getSimpleName();

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.item_head_bar_iv_back)
    ImageView backButton;
    @BindView(R.id.item_head_bar_tv_title)
    TextView titleTv;

    ImageView car;
    LinearLayout lineLayout, positionLayout;
    TextView lineTv, positionTv;
    ImageView lineIv, positionIv;

    String orderNumber;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private List<Polyline> polylines = new LinkedList<>();
    private List<Marker> endMarkers = new LinkedList<>();
    Timer timer = new Timer();
    private AMapTrackClient aMapTrackClient;
    private String sid, tid, deviceId;
    private Marker locationMarker;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 116.335891);//起点，116.335891,39.942295
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//终点，116.481288,39.995576
    private LatLonPoint carPoint;// = new LatLonPoint(39.995576, 116.481288);//终点，116.481288,39.995576
    // 是否规划路径
    private int isStart = 0;
    private boolean mHistoryFlag = false;
    /**
     * 在地图上添加marker
     */
    private List<Double> latList = new ArrayList<>();
    private List<Double> lonList = new ArrayList<>();
    private List<OrderAddressBean> orderAddressBeans = new ArrayList<>();
    private List<AddressListBean> zhuangList = new ArrayList<>();
    private List<AddressListBean> xieList = new ArrayList<>();
    private List<AddressListBean> zhuangXieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);
    }


    public void initData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_track_detail);
        ButterKnife.bind(this);
        titleTv.setText("追踪详情");
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        car = findViewById(R.id.car);
        lineLayout = findViewById(R.id.line_layout);
        positionLayout = findViewById(R.id.local_layout);

        lineTv = findViewById(R.id.line_text);
        positionTv = findViewById(R.id.lcoal_text);
        lineIv = findViewById(R.id.line_iv);
        positionIv = findViewById(R.id.local_iv);

        lineLayout.setBackgroundResource(R.drawable.white_button_shape);
        lineTv.setTextColor(getResources().getColor(R.color.black));
        lineIv.setImageResource(R.mipmap.his_uncheck_ic);
        positionLayout.setBackgroundResource(R.drawable.blue_button_shape);
        positionTv.setTextColor(getResources().getColor(R.color.white));
        positionIv.setImageResource(R.mipmap.local_uncheck_ic);

        // 地图初始化
        aMap = mMapView.getMap();
        mMapView.onCreate(savedInstanceState);
        aMapTrackClient = new AMapTrackClient(getApplicationContext()); //初始化轨迹查询
        orderNumber = getIntent().getExtras().getString("orderId");

        // 地图相关配置
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点
        aMap.setMyLocationStyle(myLocationStyle);
        UiSettings mUiSettings = null;
        mUiSettings = aMap.getUiSettings();
        //设置logo位置
        mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);//高德地图标志的摆放位置
        mUiSettings.setZoomControlsEnabled(true);//地图缩放控件是否可见
        mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);//地图缩放控件的摆放位置
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//地图的定位标志是否可见
        aMap.setMyLocationEnabled(false);//设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(15);
        aMap.moveCamera(mCameraUpdate);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineLayout.setBackgroundResource(R.drawable.blue_button_shape);
                lineTv.setTextColor(getResources().getColor(R.color.white));
                lineIv.setImageResource(R.mipmap.his_line_ic);
                positionLayout.setBackgroundResource(R.drawable.white_button_shape);
                positionTv.setTextColor(getResources().getColor(R.color.black));
                positionIv.setImageResource(R.mipmap.local_ic);
                if (timer != null) { //停止位置更新
                    timer.cancel();
                    timer = new Timer();
                }
                isStart = 0;
                mHistoryFlag = true;
                getDeviceInfo();
            }
        });

        positionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineLayout.setBackgroundResource(R.drawable.white_button_shape);
                lineTv.setTextColor(getResources().getColor(R.color.black));
                lineIv.setImageResource(R.mipmap.his_uncheck_ic);
                positionLayout.setBackgroundResource(R.drawable.blue_button_shape);
                positionTv.setTextColor(getResources().getColor(R.color.white));
                positionIv.setImageResource(R.mipmap.local_uncheck_ic);
                mHistoryFlag = false;
                getDeviceInfo();
            }
        });
        getData(orderNumber);
        // 首次进入查看轨迹
        getDeviceInfo(); //定位并获取设备信息
    }

    /**
     * 定单详情
     *
     * @param orderNumber
     */
    private void getData(String orderNumber) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> data = new HashMap<>();
        data.put("orderNumber", orderNumber);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDER_GETORDERDETAIL, jsonString).execute(new MyStringCallback<ResponseOrderInfoEntity>() {
            @Override
            public void onResponse(ResponseOrderInfoEntity resp) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                OrderListBean orderListBean = resp.data;
                orderAddressBeans.addAll(orderListBean.getAddr());
                if (orderAddressBeans.size() != 0) {
                    if (!TextUtils.isEmpty(orderAddressBeans.get(0).getStartLatitude()) && !TextUtils.isEmpty(orderAddressBeans.get(0).getStartLongitude())) {
                        mStartPoint = AMapUtil.convertToLatLonPoint(
                                new LatLng(Double.parseDouble(orderAddressBeans.get(0).getStartLatitude()),
                                        Double.parseDouble(orderAddressBeans.get(0).getStartLongitude())));
                    }
                }
                for (int i = 0; i < orderAddressBeans.size(); i++) {
                    if (!TextUtils.isEmpty(orderAddressBeans.get(i).getStartProvinceName())) {
                        AddressListBean addressListBean = new AddressListBean();
                        addressListBean.setName(orderAddressBeans.get(i).getStartLinkman());
                        addressListBean.setAddress(orderAddressBeans.get(i).getStartProvinceName() + orderAddressBeans.get(i).getStartCityName() + orderAddressBeans.get(i).getStartAreaName() +
                                orderAddressBeans.get(i).getStartAddr());
                        addressListBean.setIndex(i);
                        if (!TextUtils.isEmpty(orderAddressBeans.get(i).getStartLatitude())) {
                            addressListBean.setLatitude(Double.parseDouble(orderAddressBeans.get(i).getStartLatitude()));
                        } else {
                            addressListBean.setLatitude(0);
                        }
                        if (!TextUtils.isEmpty(orderAddressBeans.get(i).getStartLongitude())) {
                            addressListBean.setLongitude(Double.parseDouble(orderAddressBeans.get(i).getStartLongitude()));
                        } else {
                            addressListBean.setLongitude(0);
                        }
                        addressListBean.setFlag("zhuang");
                        zhuangList.add(addressListBean);
                    }
                    if (!TextUtils.isEmpty(orderAddressBeans.get(i).getEndProvinceName())) {
                        AddressListBean addressListBean = new AddressListBean();
                        addressListBean.setName(orderAddressBeans.get(i).getEndLinkman());
                        addressListBean.setAddress(
                                orderAddressBeans.get(i).getEndProvinceName() + orderAddressBeans.get(i).getEndCityName() + orderAddressBeans.get(i).getEndAreaName() +
                                        orderAddressBeans.get(i).getEndAddr());
                        if (!TextUtils.isEmpty(orderAddressBeans.get(i).getEndLatitude())) {
                            addressListBean.setLatitude(Double.parseDouble(orderAddressBeans.get(i).getEndLatitude()));
                        } else {
                            addressListBean.setLatitude(0);
                        }
                        if (!TextUtils.isEmpty(orderAddressBeans.get(i).getEndLongitude())) {
                            addressListBean.setLongitude(Double.parseDouble(orderAddressBeans.get(i).getEndLongitude()));
                        } else {
                            addressListBean.setLongitude(0);
                        }
                        addressListBean.setIndex(i);
                        addressListBean.setFlag("xie");
                        xieList.add(addressListBean);
                    }
                }
                zhuangXieList.addAll(zhuangList);
                zhuangXieList.addAll(xieList);
                addMarkersToMap(zhuangXieList);
            }

            @Override
            public Class<ResponseOrderInfoEntity> getClazz() {
                return ResponseOrderInfoEntity.class;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 判断获取当前订单查看历史轨迹还是实时位置
     */
    private void getDeviceInfo() {
        aMap.clear();
        if (mHistoryFlag) {
            setCarPointToMarker();
            isStart = 0;
            // 查询历史轨迹
            getGaodeHistoryPoint(); //历史轨迹
        } else {
            // 根据当前订单获取运输司机设备信息
            UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
            RequestEntity requestEntity = new RequestEntity(0);
            HashMap<String, String> data = new HashMap<>();
            data.put("orderNumber", orderNumber);
            requestEntity.setData(data);
            String jsonString = JsonManager.createJsonString(requestEntity);
            OkgoUtils.post(ProjectUrl.GETDEVICEINFO, jsonString).execute(new MyStringCallback<DeviceInfoEntity>() {
                @Override
                public void onResponse(DeviceInfoEntity resp) {
                    UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                    if (resp != null) {
                        sid = resp.getData().getSid(); //服务ID
                        tid = resp.getData().getTrid();//轨迹ID
                        deviceId = resp.getData().getTid(); //终端ID
                    }
                    timer.schedule(new MyTimerTask(), 1000, 5000);//启动服务，获取当前位置
                }

                @Override
                public Class<DeviceInfoEntity> getClazz() {
                    return DeviceInfoEntity.class;
                }
            });
        }
    }


    // 设置当前位置
    private void setCarPointToMarker() {
        //Log.e("setCarPointToMarker>>", "位置：" + carPoint.getLatitude() + "," + carPoint.getLongitude());
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        if (locationMarker != null) {
            locationMarker.remove();
        }
        if (null != carPoint) {
            LatLng latLng = AMapUtil.convertToLatLng(carPoint);
            locationMarker = aMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.runcar)));
            if(isStart == 0){
                boundsBuilder.include(latLng);
                aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 30));
                isStart +=1;
            }
        }

    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int mode) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, carPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()), 16, 0, 0)));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void addMarkersToMap(List<AddressListBean> addressListBeans) {
        latList.clear();
        lonList.clear();
        for (AddressListBean bean : addressListBeans) {
            if (bean.getLatitude() != 0 && bean.getLongitude() != 0) {
                aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                        .fromView(initView(bean)))
                        .position(new LatLng(bean.getLatitude(), bean.getLongitude()))
                        .draggable(false));
                latList.add(bean.getLatitude());
                lonList.add(bean.getLongitude());
            }
        }
        if (latList.size() != 0 && lonList.size() != 0) {
            LatLng northeast = new LatLng(Collections.max(latList), Collections.max(lonList));//经纬度最大值
            LatLng southwest = new LatLng(Collections.min(latList), Collections.min(lonList));//经纬度最小值
            LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                    .include(southwest).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            aMap.animateCamera(cameraUpdate, 100L, (AMap.CancelableCallback) null);
        }
    }

    private ImageView imageFlag;
    private TextView markTv;

    private View initView(AddressListBean bean) {
        LayoutInflater inflater = LayoutInflater.from(TraceDetailActivity.this);
        View view = inflater.inflate(R.layout.mark_view, null);
        markTv = view.findViewById(R.id.markTv);
        imageFlag = view.findViewById(R.id.imageFlag);
        if (bean.getFlag().equals("zhuang")) {
            imageFlag.setBackgroundResource(R.mipmap.img_addr_view_zhuang1);
            markTv.setText("第" + (bean.getIndex() + 1) + "装货地:" + bean.getAddress());
            markTv.setBackgroundColor(Color.parseColor("#FFDD29"));
        } else if (bean.getFlag().equals("xie")) {
            imageFlag.setBackgroundResource(R.mipmap.img_addr_view_xie1);
            markTv.setBackgroundColor(Color.parseColor("#1e73ff"));
            markTv.setText("第" + (bean.getIndex() + 1) + "卸货地:" + bean.getAddress());
        }
        return view;
    }

    private void setMapPointView(String flag, double latitude, double longitude) {
        if (flag.equals("start")) {
            aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_start_point))
                    .draggable(false));
        } else if (flag.equals("end")) {
            aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.runcar))
                    .draggable(false));
        }
    }


    public class MyTimerTask extends TimerTask {
        public void run() {
            getNewLocalData();
        }
    }

    /**
     * 高德获取终端最新位置，并刷新地图
     */
    private void getNewLocalData() {
        clearTracksOnMap();
        // 查询符合条件的所有轨迹点，并绘制
        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Long.valueOf(sid), deviceId), new SimpleOnTrackListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                if (queryTerminalResponse.isSuccess()) {
                    if (tid != null) {
                        aMapTrackClient.queryLatestPoint(new LatestPointRequest(Long.valueOf(sid), Long.valueOf(deviceId)), new SimpleOnTrackListener() {
                            @Override
                            public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
                                if (latestPointResponse.isSuccess()) {
                                    Point point = latestPointResponse.getLatestPoint().getPoint();
                                    if (null != point) {
                                        double lat = point.getLat();
                                        double lng = point.getLng();
                                        Log.e("queryLatestPoint>>", "查询实时位置成功，实时位置：" + pointToString(point) + "lenth=" + String.valueOf(lat).length());
                                        if ((lat > 0 || lng > 0)) {
                                            carPoint = AMapUtil.convertToLatLonPoint(
                                                    new LatLng(point.getLat(),
                                                            point.getLng()));
                                            aMap.clear();
                                            setCarPointToMarker();
                                            addMarkersToMap(zhuangXieList);
                                        } else {
                                            ToastUtil.showToastString("获取车辆位置信息异常，请联系客服");
                                        }
                                    } else {
                                        aMap.clear();
                                        addMarkersToMap(zhuangXieList);
                                    }
                                } else {
                                    Log.e("queryLatestPoint>>", "查询实时位置失败，" + latestPointResponse.getErrorMsg());
                                }
                            }
                        });
                    } else {
                        Log.e("queryLatestPoint>>", "终端不存在，请先使用轨迹上报示例页面创建终端和上报轨迹");
                    }
                } else {
                    Log.e("queryLatestPoint>>", "终端查询失败，" + queryTerminalResponse.getErrorMsg());
                }
            }
        });

    }

    /**
     * 获取订单历史轨迹列表
     */
    @SuppressLint("CheckResult")
    private void getGaodeHistoryPoint() {
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> data = new HashMap<>();
        data.put("orderNumber", orderNumber);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.DRIVER_HISTORY_INFO, jsonString).execute(new MyStringCallback<HistoryInfoEntity>() {
            @Override
            public void onResponse(HistoryInfoEntity resp) {
                if (resp.isSuccess()) {
                    if (zhuangXieList.size() > 0) {
                        addMarkersToMap(zhuangXieList);
                    }
                    List<HistoryPoint.TimeBean> dateBeans = null;
                    List<HistoryPoint.NodeBean> nodeBeans = null;
                    if (resp.getData().getData() != null) {
                        dateBeans = resp.getData().getData().getTimeSlice().times;
                    }
                    if (resp.getData().getData().getNodeDate() != null) {
                        nodeBeans = resp.getData().getData().getNodeDate().turn;
                    }
                    String workSid = resp.getData().getSid();
                    String workTid = resp.getData().getTid();
                    String workTrid = resp.getData().getTrid();
                    if (dateBeans != null && dateBeans.size() > 0) {
                        for (HistoryPoint.TimeBean data : dateBeans) {
                            long startDate = Long.valueOf(data.getStartDate());
                            long endDate = Long.valueOf(data.getEndDate());
                            HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(
                                    Long.valueOf(workSid),
                                    Long.valueOf(workTid),
                                    startDate,
                                    endDate,
                                    1,      // 不绑路
                                    1,      // 不做距离补偿
                                    5000,   // 距离补偿阈值，只有超过5km的点才启用距离补偿
                                    0,  // 由旧到新排序
                                    1,  // 返回第1页数据
                                    900,    // 一页不超过100条
                                    ""  // 暂未实现，该参数无意义，请留空
                            );
                            aMapTrackClient.queryHistoryTrack(historyTrackRequest, new SimpleOnTrackListener() {
                                @Override
                                public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
                                    if (historyTrackResponse.isSuccess()) {
                                        HistoryTrack historyTrack = historyTrackResponse.getHistoryTrack();
                                        if (historyTrack == null || historyTrack.getCount() == 0) {
                                            ToastUtil.showToastString("未获取到轨迹");
                                            return;
                                        }
                                        List<Point> points = historyTrack.getPoints();
                                        Log.e("onHistoryTrackCallback", "历史轨迹==" + points.size());
                                        drawTrackOnMap(points, historyTrack.getStartPoint(), historyTrack.getEndPoint());
                                        if (isStart == 0) {
                                            isStart += 1;
                                            setMapPointView("start", points.get(0).getLat(), points.get(0).getLng());
                                        }
                                    } else {
                                        ToastUtil.showToastString("未获取到轨迹");
                                    }
                                }
                            });
                        }
                    }

                    if (nodeBeans != null && nodeBeans.size() > 0) {
                        for (HistoryPoint.NodeBean data : nodeBeans) {
                            long startDate = Long.valueOf(data.getStartDate());
                            long endDate = Long.valueOf(data.getEndDate());
                            HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(
                                    Long.valueOf(workSid),
                                    Long.valueOf(data.getTid()),
                                    startDate,
                                    endDate,
                                    1,      // 不绑路
                                    1,      // 不做距离补偿
                                    5000,   // 距离补偿阈值，只有超过5km的点才启用距离补偿
                                    0,  // 由旧到新排序
                                    1,  // 返回第1页数据
                                    900,    // 一页不超过100条
                                    ""  // 暂未实现，该参数无意义，请留空
                            );
                            aMapTrackClient.queryHistoryTrack(historyTrackRequest, new SimpleOnTrackListener() {
                                @Override
                                public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
                                    if (historyTrackResponse.isSuccess()) {
                                        HistoryTrack historyTrack = historyTrackResponse.getHistoryTrack();
                                        if (historyTrack == null || historyTrack.getCount() == 0) {
                                            ToastUtil.showToastString("未获取到轨迹点");
                                            return;
                                        }
                                        List<Point> points = historyTrack.getPoints();
                                        Log.e("onHistoryTrackCallback", "历史轨迹==" + points.size());
                                        drawTrackOnMap(points, historyTrack.getStartPoint(), historyTrack.getEndPoint());
                                    } else {
                                        ToastUtil.showToastString("查询历史轨迹点失败");
                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public Class<HistoryInfoEntity> getClazz() {
                return null;
            }

        });

    }

    private void drawTrackOnMap(List<Point> points, TrackPoint startPoint, TrackPoint endPoint) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE).width(20);

        for (Point p : points) {
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            polylineOptions.add(latLng);
            boundsBuilder.include(latLng);
        }
        Polyline polyline = aMap.addPolyline(polylineOptions);
        polylines.add(polyline);
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 30));
    }


    private String pointToString(Point point) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return "{lat: " + point.getLat() + ", lng: " + point.getLng() +
                ", 上传时间: " + sdf.format(new Date(point.getTime())) +
                ", 定位精度" + point.getAccuracy() + ", 其他属性参考文档...}";
    }


    private void clearTracksOnMap() {
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        for (Marker marker : endMarkers) {
            marker.remove();
        }
        endMarkers.clear();
        polylines.clear();
    }

}
