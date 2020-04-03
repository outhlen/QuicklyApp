package com.escort.carriage.android.http;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.utils.hint.LogUtils;
import com.escort.carriage.android.utils.HashUtil;

import java.io.File;
import java.util.Date;

public class UploadHelper {

    //与个人的存储区域有关
    private String ENDPOINT = "http://oss-cn-qingdao.aliyuncs.com";
    private String token = "CAIS8wF1q6Ft5B2yfSjIr5aEMfbEi+t39LOhc2L/00FtZuVN24/+sTz2IHBNfnBpAuAdtfU0lWtW6P0clqUqEcQcHxedN5AptM8PI5djgXKH6aKP9rUhpMCPOwr6UmzWvqL7Z+H+U6muGJOEYEzFkSle2KbzcS7YMXWuLZyOj+wMDL1VJH7aCwBLH9BLPABvhdYHPH/KT5aXPwXtn3DbATgD2GM+qxsms/TknZDDukuG0QalmrdKnemrfMj4NfsLFYxkTtK40NZxcqf8yyNK43BIjvwm1fYdoGyX5YrMWAgKvUvdbfC0+9FqLQl+fbMqvFSALDJmXpcagAFjzHEgNyrKjKBC4gABQPZgO1BfD16jxLkDZGwICp5GZL4O+VO12NwUAGuwsUw8XTGlSaBz2UAVFJPM/t09bnUnOAZ+L7T1uwKV/dG3Mb4jRae9b/wawA8LqgBET5dvJC5iyHAlcKtrZdAI82WuWZRTbdCHmqx+EO0vjXKxRb0gkg==";
    private String accessKeyID = "STS.NU1zLpf4VCqJqDN7A8jja4JUS";//"LTAIbsGio1aJI2Rp";
    private String accessKeySecret = "DkJnAvQp5m2PCNmmWpueuy9DaFW5W5acBp2416MBESxy";
    //上传仓库名
    private String BUCKET_NAME = "xeryb";

    public UploadHelper(String endpoint, String keyId, String secret, String token, String bucketName) {
        this.ENDPOINT = endpoint;
        this.accessKeyID = keyId;
        this.accessKeySecret = secret;
        this.token = token;
        this.BUCKET_NAME = bucketName;
    }

    private OSS getOSSClient() {
// 在移动端建议使用STS的方式初始化OSSClient。
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyID, accessKeySecret, token);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒。
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒。
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个。
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次。

        OSS oss = new OSSClient(ApplicationContext.getInstance().application, ENDPOINT, credentialProvider, conf);

        return oss;
    }

    /**
     * 上传方法
     *
     * @param objectKey 标识
     * @param path      需上传文件的路径
     * @return 外网访问的路径
     */
    private String upload(String objectKey, String path) {
        // 构造上传请求
        PutObjectRequest request =
                new PutObjectRequest(BUCKET_NAME,
                        objectKey, path);
        try {
            //得到client
            OSS client = getOSSClient();
            //上传获取结果
            PutObjectResult result = client.putObject(request);
            //获取可访问的url
            String url = client.presignPublicObjectURL(BUCKET_NAME, objectKey);
            //格式打印输出
            Log.e(String.format("PublicObjectURL:%s", url), "");
            return url;
        } catch (ClientException e) {
            // 本地异常，如网络异常等。
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常。
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.showE("UploadHelper", "LHF" + " = 上传错误 = " + e.getMessage());

        }
        return null;
    }

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public String uploadImage(String path) {
        String key = getObjectImageKey(path);
        return upload(key, path);
    }

    /**
     * 上传图片  一级目录 undertakeAndriod
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public String uploadPortrait(String path) {
        String key = getPortraitKey(path);
//        key = "userIdHaHa";
        return upload(key, path);
    }

    /**
     * 上传audio
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public String uploadAudio(String path) {
        String key = getObjectAudioKey(path);
        return upload(key, path);
    }

    /**
     * 获取时间
     *
     * @return 时间戳 例如:201805
     */
    private String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    /**
     * 返回key
     *
     * @param path 本地路径
     * @return key
     */
    //格式: image/201805/sfdsgfsdvsdfdsfs.jpg
    private String getObjectImageKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    //格式: undertakeAndriod/sfdsgfsdvsdfdsfs.jpg
    private String getPortraitKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        return String.format("%s/%s.jpg", "undertakeAndriod", fileMd5);
    }

    //格式: portrait/201805/sfdsgfsdvsdfdsfs.jpg
    private String getObjectPortraitKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    //格式: audio/201805/sfdsgfsdvsdfdsfs.mp3
    private String getObjectAudioKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }
}
