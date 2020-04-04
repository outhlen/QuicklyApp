package com.tripartitelib.android.iflytek;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.androidybp.basics.utils.file.FileUtil;
import com.androidybp.basics.utils.hint.LogUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.msc.util.log.DebugLog;
import com.tripartitelib.android.EnvironmentConfig;

/**
 * 科大讯飞 播放语音使用工具类
 */
public class IflytekUtils {

    private static volatile IflytekUtils utils;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private IflytekUtils() {
    }

    public static IflytekUtils getIflytekUtils() {
        if (utils == null) {
            synchronized (IflytekUtils.class) {
                if (utils == null) {
                    utils = new IflytekUtils();
                }
            }
        }
        return utils;
    }

    public void initIflytek(Context context) {
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + EnvironmentConfig.iflyteKey);

    }

    /**
     * 开始播放
     *
     * @param str
     */
    public void startSpeaking(Context context, String str) {
        initTts(context);
        // 设置参数
        setParam();
        int code = mTts.startSpeaking(str, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//        String path = FileUtil.getProjectDownloadPath()+"/tts.pcm";
        //	int code = mTts.synthesizeToUri(texts, path, mTtsListener);

        if (code != ErrorCode.SUCCESS) {
            LogUtils.showE("IflytekUtils", "语音合成失败,错误码: " + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }

    // 取消合成
    public void stopSpeaking() {
        if (mTts != null) {
            mTts.stopSpeaking();
        }
    }

    // 暂停播放
    public void pauseSpeaking() {
        if (mTts != null) {
            mTts.pauseSpeaking();
        }
    }

    // 继续播放
    public void resumeSpeaking() {
        if (mTts != null) {
            mTts.resumeSpeaking();
        }
    }

    /**
     * 初始化播放器 对象
     *
     * @param context
     */
    public void initTts(Context context) {
        // 初始化合成对象
        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(context, new TtsInitListenerIm());
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");

            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, FileUtil.getProjectDownloadPath() + "/msc/tts.pcm");
    }


    public void clear() {
        mTts = null;
    }

    /**
     * 初始化监听。
     */
    class TtsInitListenerIm implements InitListener {
        @Override
        public void onInit(int code) {
            LogUtils.showE("IflytekUtils", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                LogUtils.showE("IflytekUtils", "初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    }


    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            LogUtils.showE("IflytekUtils", "开始播放");
        }

        @Override
        public void onSpeakPaused() {
            LogUtils.showE("IflytekUtils", "暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            LogUtils.showE("IflytekUtils", "继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            Log.e("MscSpeechLog_", "percent =" + percent);
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            Log.e("MscSpeechLog_", "percent =" + percent);
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                LogUtils.showE("IflytekUtils", "播放完成");


            } else if (error != null) {
                LogUtils.showE("IflytekUtils", error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                LogUtils.showE("IflytekUtils", "session id =" + sid);
            }

            //当设置SpeechConstant.TTS_DATA_NOTIFY为1时，抛出buf数据
            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                LogUtils.showE("MscSpeechLog_", "bufis =" + buf.length);
            }


        }
    };

}
