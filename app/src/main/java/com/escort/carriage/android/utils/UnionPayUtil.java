package com.escort.carriage.android.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.escort.carriage.android.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UnionPayUtil {

  public String TAG  = "UnionPayUtil";
    public static  Context context;
    private final int ENV_TEST_ONE = 0;
    private final int ENV_TEST_TWO = 1;
    private final int ENV_NATIVE = 2;
    private final int ENV_PRODUCT = 3;
    private int typetag = 0;

    private int mCurrentEnvironment = ENV_PRODUCT;
    public static   UnionPayUtil  unionPayUtil;

    JSONArray divisionInfosArray = new JSONArray();
    String param = null;
    String url = "https://qr.chinaums.com/netpay-route-server/api/";

    public static UnionPayUtil getUnionPayUtil(Context ct){
        context  = ct;
        if(unionPayUtil == null){
            unionPayUtil  = new UnionPayUtil();
        }
        return unionPayUtil;
    }

    public void initUnionPay(int payType,String param){
        this.typetag  = payType;
        this.param = param;
      //  new GetPrepayIdTask().execute();
   }

//    static public String signWithMd5(String originStr, String md5Key, String charset) {
//        String text = originStr + md5Key;
//        Log.d("zhangxiulu", "signStr:" + text);
//        return UnifyMd5.md5Hex(getContentBytes(text, charset)).toUpperCase();
//    }

    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
                    + charset);
        }
    }

    static public String buildSignString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.size());
        for (String key : params.keySet()) {
            if ("sign".equals(key) || "sign_type".equals(key))
                continue;
            if (params.get(key) == null || params.get(key).equals(""))
                continue;
            keys.add(key);
        }

        Collections.sort(keys);
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {  // 拼接时，不包括最后一个&字符
                buf.append(key + "=" + value);
            } else {
                buf.append(key + "=" + value + "&");
            }
        }
        return buf.toString();

    }

    private String getCommonOrder(String preFix){
        Random random = new Random();
        StringBuilder sb = new StringBuilder(preFix); //支付宝、微信生产环境
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sb.append(df.format(new Date()));
        for(int i=0; i < 7; i++){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }



//    /**
//     * 微信
//     * @param parms
//     */
//    public void payWX(String parms){
//        UnifyPayRequest msg = new UnifyPayRequest();
//        msg.payChannel = UnifyPayRequest.CHANNEL_WEIXIN;
//        msg.payData = parms;
//        UnifyPayPlugin.getInstance(context).sendPayRequest(msg);
//    }
//
//    /**
//     * 支付宝
//     * @param parms
//     */
//    public void payAliPay(String parms){
//        UnifyPayRequest msg = new UnifyPayRequest();
//        msg.payChannel = UnifyPayRequest.CHANNEL_ALIPAY;
//        msg.payData = parms;
//        UnifyPayPlugin.getInstance(context).sendPayRequest(msg);
//    }
//
//    /**
//     * 云闪付
//     * @param appPayRequest
//     */
//    public void payCloudQuickPay(String appPayRequest) {
//        String tn = "空";
//        try {
//            JSONObject e = new JSONObject(appPayRequest);
//            tn = e.getString("tn");
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//        UPPayAssistEx.startPay (context, null, null, tn, "00");
//        Log.d("test","云闪付支付 tn = " + tn);
//    }

//    public static byte[] httpPost(String var0, String var1) {
//        if (var0 != null && var0.length() != 0) {
//            HttpClient var2 = v();
//            HttpPost var4 = new HttpPost(var0);
//            try {
//                var4.setEntity(new StringEntity(var1, "utf-8"));
//                var4.setHeader("Content-Type", "text/xml;charset=UTF-8");
//                HttpResponse var5;
//                if ((var5 = var2.execute(var4)).getStatusLine().getStatusCode() != 200) {
//                    Log.e("SDK_Sample.Util", "httpGet fail, status code = " + var5.getStatusLine().getStatusCode());
//                    return null;
//                } else {
//                    return EntityUtils.toByteArray(var5.getEntity());
//                }
//            } catch (Exception var3) {
//                Log.e("SDK_Sample.Util", "httpPost exception, e = " + var3.getMessage());
//                var3.printStackTrace();
//                return null;
//            }
//        } else {
//            Log.e("SDK_Sample.Util", "httpPost, url is null");
//            return null;
//        }
//    }

//    private static HttpClient v() {
//        try {
//            KeyStore var0;
//            (var0 = KeyStore.getInstance(KeyStore.getDefaultType())).load((InputStream) null, (char[]) null);
//            SocketFactory var4;
//            (var4 = new SocketFactory(var0)).setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            BasicHttpParams var1;
//            HttpProtocolParams.setVersion(var1 = new BasicHttpParams(), HttpVersion.HTTP_1_1);
//            HttpProtocolParams.setContentCharset(var1, "UTF-8");
//            SchemeRegistry var2;
//            (var2 = new SchemeRegistry()).register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//            var2.register(new Scheme("https", var4, 443));
//            ThreadSafeClientConnManager var5 = new ThreadSafeClientConnManager(var1, var2);
//            return new DefaultHttpClient(var5, var1);
//        } catch (Exception var3) {
//            return new DefaultHttpClient();
//        }
//    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
