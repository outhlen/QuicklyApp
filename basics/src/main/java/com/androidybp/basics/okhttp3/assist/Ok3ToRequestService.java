package com.androidybp.basics.okhttp3.assist;

/**
 * 这个类主要就是 请求数据的一个  辅助类  使用的时候 如果不需要处理返回值时   不需要设置回调
 *
 * 这个类没有加载等待动画   会用对应的方法回调回去  回调方法都为主线程
 *
 * 这个类有两种方式 请求服务器（类内部实现方式）
 * 1、主线程去  子线程回
 * 2、全程在子线程中操作
 *
 */
public class Ok3ToRequestService {
    //回调方法
    private Ok3ToRequestServiceCallback callback;
    //网络请求的 标示 用于解析数据使用的
    private int openType;


    /**
     *
     * @param openType      请求标示  用于解析数据使用的
     * @param requestType
     * @param callback
     */
    public Ok3ToRequestService(int openType, int requestType, Ok3ToRequestServiceCallback callback){
        this.openType = openType;
        this.callback = callback;
        if(requestType == 0){

        } else {

        }
    }


    public interface Ok3ToRequestServiceCallback{

    }
}
