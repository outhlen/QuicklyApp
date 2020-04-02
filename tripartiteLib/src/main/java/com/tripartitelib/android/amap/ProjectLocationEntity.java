package com.tripartitelib.android.amap;

public class ProjectLocationEntity {
    public int errorCode;//errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
    public double latitude;//纬度
    public double longitude;//经度
    public int locationType;//定位类型
    public float accuracy;//精度
    public String provider;//提供者
    public float speed;//速    度
    public float bearing;//角    度
    public int satellites;//星    数
    public String country;//国    家
    public String province;//省
    public String city;//市
    public String cityCode;//城市编码
    public String district;//区
    public String adCode;//区域 码
    public String address;//地    址
    public String poiName;//兴趣点
    public long createTime;//创建时间
    public String errorInfo;//错误信息
    public String locationDetail;//错误描述

}
