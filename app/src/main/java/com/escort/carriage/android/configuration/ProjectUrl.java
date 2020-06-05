package com.escort.carriage.android.configuration;

import com.androidybp.basics.configuration.BaseProjectUrl;

public class ProjectUrl {
    private static String baseUrl = BaseProjectUrl.getRequestUrl();
    /**
     * 货源大厅推送列表
     */
    public static String ORDERVEHICLE_LISTMATCHING = baseUrl + "undertake/orderVehicle/listMatching";



    //获取幻灯片 (其他) 2:APP首页 3:商城 4:俱乐部 5:直播 6:启动图
    public static String PUBLIC_GETSLIDE = "$baseUrl?g=Cliapi&m=Public&a=getSlide";

    /**
     * 微信登录第一个接口
     */
    public static String GETUNIONID = baseUrl + "undertake/wechat/getUnionId";

    /**
     * 微信登录第二个接口
     */
    public static String LOGINWXUNIONID = baseUrl + "undertake/login/loginWxUnionid";

    /**
     * 用户名密码登陆
     */
    public static String LOGIN = baseUrl + "undertake/login/login";

    /**
     * 短信登录-发送验证码
     */
    public static String LOGIN_SENDSMS = baseUrl + "undertake/login/sendSms";
    /**
     * 修改手机号发送短信验证码
     */
    public static String USERLOGIN_UPDATEPHONECODE = baseUrl + "undertake/userLogin/updatePhoneCode";

    /**
     * 转车验证码
     */
    public static String ORDERVEHICLE_SENDSMSAPPLYTURN = baseUrl + "undertake/orderVehicle/sendSmsApplyTurn";

    /**
     * 版本控制
     */
    public static String CONFIG_GETVERSION = baseUrl + "undertake/config/getVersion";
//    public static String CONFIG_GETVERSION = "https://cy.xeyb56.com/a/api/undertake/config/getVersion";


    /**
     * 短信登录
     */
    public static String LOGIN_LOGINPHONE = baseUrl + "undertake/login/loginPhone";

    /**
     * 忘记密码发送短信验证码
     */
    public static String LOGIN_SENDSMSFORGETPASSWORD = baseUrl + "undertake/login/sendSmsForgetPassword";

    /**
     * 忘记密码 设置密码
     */
    public static String LOGIN_FORGETPASSWORD = baseUrl + "undertake/login/forgetPassword";
    /**
     * 修改登陆密码
     */
    public static String LOGIN_UPDATELOGINPASSWORDCHECK = baseUrl + "undertake/login/updateLoginPasswordCheck";


    /**
     * 注册发送短信验证码
     */
    public static String LOGIN_SENDSMSREGISTER = baseUrl + "undertake/login/sendSmsRegister";

    /**
     * 注册
     */
    public static String LOGIN_REGISTER = baseUrl + "undertake/login/register";

    /**
     * 注册后设置密码
     */
    public static String LOGIN_UPDATELOGINPASSWORD = baseUrl + "undertake/login/updateLoginPassword";

    /**
     * 获取用户信息
     */
    public static String USERINFO_GETUSERINFO = baseUrl + "undertake/userInfo/getUserInfo";

    /**
     * 头条标题列表
     */
    public static String SYSNEW_GETTITLELIST = baseUrl + "undertake/sysnew/getTitleList";
    /**
     * 头条列表
     */
    public static String SYSNEW_GETSYSNEWLIST = baseUrl + "undertake/sysnew/getSysNewList";


    /**
     * 头条详情
     */
    public static String SYSNEW_GETSYSNEWINFO = baseUrl + "undertake/sysnew/getSysNewInfo";



    /**
     * 订单详情
     */
    public static String ORDER_GETORDERDETAIL = baseUrl + "undertake/order/getOrderDetail";



    /**
     * 司机端设备获取
     */
    public static String GETDEVICEINFO = baseUrl + "undertake/order/getGoldFalcon";

    /**
     *  司机历史轨迹查询
     */

    public static String DRIVER_HISTORY_INFO = baseUrl + "undertake/order/getHistoryTrace";


    /**
     * 竞标
     */
    public static String ORDERVEHICLE_ADD_VEHICLE = baseUrl + "undertake/orderVehicle/add/vehicle";

    /**
     * 查看货运单
     */
    public static String GOODS_TRANSPORT_RECEIPT = baseUrl + "undertake/attestation/getAssumeRole";

    /**
     * 线路列表查询
     */
    public static String DRIVELINE_GETLIST = baseUrl + "undertake/driveline/getList";
    /**
     * 修改线路 状态
     */
    public static String DRIVELINE_UPDATESTATUS = baseUrl + "undertake/driveline/updateStatus";

    /**
     * 保存城市配送信息
     */
    public static String VEHICLE_INFO_SAVECITYDISTRIBUTE = baseUrl + "undertake/vehicle/info/saveCityDistribute";

    /**
     *  删除专线
     */
    public static String DELETE_DRIVER_LIN = baseUrl + "undertake/driveline/delete";


    /**
     * 新增线路
     */
    public static String DRIVELINE_ADD = baseUrl + "undertake/driveline/add";
    /**
     * 修改线路
     */
    public static String DRIVELINE_UPDATE = baseUrl + "undertake/driveline/update";

    /**
     * 设置押金和推单
     */
    public static String DRIVELINE_SETTING = baseUrl + "undertake/driveline/setting";

    /**
     * 获取 省
     */
    public static String REGION_GETPROVINCE = baseUrl + "undertake/region/getProvince";

    /**
     * 获取 市
     */
    public static String REGION_GETCITY = baseUrl + "undertake/region/getCity";

    /**
     * 获取 区
     */
    public static String REGION_GETAREA = baseUrl + "undertake/region/getArea";

    /**
     * 修改手机号
     */
    public static String USERLOGIN_UPDATEUSERPHONE = baseUrl + "undertake/userLogin/updateUserPhone";


    /**
     * 认证状态查询
     */
    public static String UNDERTAKE_USERAUTH_JUDGEAUTH = baseUrl + "undertake/userauth/judgeAuth";


    /**
     *  环信登录注册
     */
    public static String HUANXIN_REGISTER_URL = baseUrl + "undertake/login/addHuanXinUser";

    /**
     * 获取个人认证信息
     */
    public static String USERINFO_GETPERSONALAUTHENTICATION = baseUrl + "undertake/userInfo/getPersonalAuthentication";
    /**
     * 获取公司信息
     */
    public static String COMPANY_COMPANY_INFO = baseUrl + "undertake/company/company/info";

    /**
     * 个人认证
     */
    public static String USERINFO_PERSONALAUTHENTICATION = baseUrl + "undertake/userInfo/personalAuthentication";
    /**
     * 企业认证
     */
    public static String COMPANY_AUTH_AUTHUNDERTAKE = baseUrl + "undertake/company/auth/authUndertake";

    /**
     * 文件上传 获取 阿里云 oss 对应配置参数
     */
    public static String CLOUDAUTH_GETASSUMEROLE = baseUrl + "undertake/cloudAuth/getAssumeRole";

    /**
     *  货运端企业认证 (新增)
      */
    public static String CLOUDAUTH_COMMPANY_AUTHOR = baseUrl + "undertake/company/company/businessLicense";


    /**
     * 文件上传
     */
    public static String OSS_UPLOAD_FILE = baseUrl + "undertake/oss/upload/file";

    /**
     * 修改昵称
     */
    public static String USERINFO_UPDATENICKNAME = baseUrl + "undertake/userInfo/updateNickName";
    /**
     * 修改头像
     */
    public static String USERINFO_UPDATEHEADPORTRAIT = baseUrl + "undertake/userInfo/updateHeadPortrait";

    /**
     * 获取司机认证信息 和 审核状态
     */
    public static String USER_USERAUTHDRIVER_GETDRIVERAUTHENTICATIONINFO = baseUrl + "undertake/user/userAuthDriver/getDriverAuthenticationInfo";
    /**
     * 司机认证
     */
    public static String USER_USERAUTHDRIVER_DRIVERAUTHENTICATION = baseUrl + "undertake/user/userAuthDriver/driverAuthentication";

    /**
     * 行驶证列表
     */
    public static String VEHICLE_INFO_GETLIST = baseUrl + "undertake/vehicle/info/getList";


    /**
     * 设置默认行驶证（修改状态）
     */
    public static String VEHICLE_INFO_UPDATEUSAGE = baseUrl + "undertake/vehicle/info/updateUsage";

    /**
     * 上传行驶证正反面
     */
    public static String VEHICLE_INFO_ADD = baseUrl + "undertake/vehicle/info/add";

    /**
     * 修改行驶证正反面
     */
    public static String VEHICLE_INFO_UPDATE = baseUrl + "undertake/vehicle/info/update";

    /**
     * 人脸识别 获取Token
     */
    public static String CLOUDAUTH_GETAUTHKEY = baseUrl + "undertake/cloudAuth/getAuthKey";

    /**
     * 人脸识别-获取FaceImg
     */
    public static String CLOUDAUTH_GETFACEIMG = baseUrl + "undertake/cloudAuth/getFaceImg";


    /**
     * 订单列表
     */
    public static String ORDER_GETORDERLIST = baseUrl + "undertake/orderVehicle/getOrderList";

    /**
     *  追踪列表
     */
    public static String ORDER_TARCE_LIST = baseUrl + "undertake/orderVehicle/getTurnLocationList";

    /**
     * 我的投标
     */
    public static String ORDERVEHICLE_VEHICLELIST = baseUrl + "undertake/orderVehicle/vehicleList";


    /**
     * 前往装货
     */
    public static String ORDER_UPDATAORDERDETAILSTATUS = baseUrl + "undertake/order/updataOrderDetailStatus";

    /**
     * 请求撤单
     */
    public static String ORDER_GETORDERDELETE = baseUrl + "undertake/order/getOrderDelete";
      /**
     * 处理撤单
     */
    public static String ORDER_UPDATEORDERDELETE = baseUrl + "undertake/order/updateOrderDelete";

    /**
     * 装卸列表
     */
    public static String ORDER_SELECTORDERRELATIONSTATUS = baseUrl + "undertake/order/selectOrderRelationStatus";

    /**
     * 修改订单装卸货信息
     */
    public static String ORDERVEHICLE_ADD_STATUS = baseUrl + "undertake/orderVehicle/add/status";

    /**
     * 评论-详情
     */
    public static String EVALUATION_GETINFO = baseUrl + "undertake/evaluation/getInfo";

    /**
     * 新增评论
     */
    public static String EVALUATION_ADD = baseUrl + "undertake/evaluation/add";
    /**
     * 评论（列表）-订单号
     */
    public static String EVALUATION_GETEVALUATIONINFOBYORDERNUMBER = baseUrl + "undertake/evaluation/getInfotype";

    /**
     * 承运商发起申诉
     */
    public static String HANDLE_ADDAPPEAL = baseUrl + "undertake/handle/addAppeal";

    /**
     *  投诉
     */
    public static String    TOUSUINFO_URL = baseUrl + "undertake/order/reportOrder";

    /**
     * 申请转车
     */
    public static String ORDERVEHICLE_ADD_EARNESTTURN = baseUrl + "undertake/orderVehicle/add/earnestTurn";

    /**
     * 催付运费
     */
    public static String ORDERVEHICLE_URGE_PAYMENT = baseUrl + "undertake/orderVehicle/urge/payment";
    /**
     * 总资产
     */
    public static String ASSETS_PAYUSER_GETUSERASSETS = baseUrl + "undertake/assets/payUser/getUserAssets";

    /**
     * 车主支付押金
     */
    public static String ORDER_ADD_EARNEST = baseUrl + "undertake/order/add/earnest";

    /**
     * 特惠充值列表
     */
    public static String PAYRECORD_PREFERENTIALLIST = baseUrl + "undertake/payRecord/preferentialList";

    /**
     *  支付宝、微信支付状态获取
     */

    public static String GETPAYSTATUS_URL = baseUrl + "undertake/pay/unionPay/query";

    /**
     * 银联撤销订单
     */
    public static String DELETEORDERPAY_URL = baseUrl + "undertake/pay/unionPay/close";

    /**
     * 充值-发起支付
     */
    public static String PAYRECORD_RECHARGE = baseUrl + "undertake/payRecord/recharge";


    /**
     *  银联商户发起支付
     */
    public static String CHINAUNION_PAY_URL = baseUrl + "undertake/pay/unionPay";

    /**
     * 轨迹标识
     */
    public static String ORDER_GETGOLDFALCON = baseUrl + "undertake/order/getGoldFalcon";

    /**
     * 处理线下支付
     */
    public static String ORDER_UPDATEOFFPAY= baseUrl + "undertake/orderVehicle/updateOffPay";
    /**
     * 获取banner
     */
    public static String GET_BANNER= baseUrl + "undertake/ads/list";

    /**
     *  回单处理
     */
    public static String ORDER_RETURNBACK = baseUrl + "undertake/orderVehicle/switchBack";

    /**
     *  报告错误信息
     */

    public static String REPORT_ERROR_INFO = baseUrl + "undertake/order/reportErrorOrder";
    /**
     *  字典
     */
    public static String QUERY_DICT_INFO = baseUrl + "undertake/dictionary/getChildList";

}
