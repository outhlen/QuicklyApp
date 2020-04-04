package com.tripartitelib.android.wechat.bean;

/**
 * 微信请求服务器 响应 Members对象
 */

public class WXResponseMembers {

    public String sign;//   签名       F5C38F3356178E604881FABD444B9F9A
    public String packages;//  返回状态码  此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    public String appid;//      wxc6e184508b90d834
    public String partnerid;//商户号
    public String noncestr;//随机字符串
    public String prepayid;//wx2017051614142634a7ad08eb0638522655
    public String timestamp;//时间戳

    public WXResponseMembers() {
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
