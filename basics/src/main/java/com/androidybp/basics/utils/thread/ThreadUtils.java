package com.androidybp.basics.utils.thread;

import android.os.Looper;

import com.androidybp.basics.ApplicationContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程相关工具类
 */
public class ThreadUtils {
    private static ExecutorService executorService;
    private static final int CORE_POOL_SIZE = 10;//	核心线程池大小
    private static final int MAXIMUM_POOL_SIZE = 20;//最大线程池大小
    private static final long KEEP_ALIVE_TIME = 60L;//线程池中超过corePoolSize数目的空闲线程最大存活时间
    /**
     * 在主线程执行任务
     *
     * @param r
     */
    public static void runOnUIThread(Runnable r) {
        ApplicationContext.getInstance().getMainHandler().post(r);
    }

    /**
     * 判断当前线程是否为主线程
     *
     * @return 主线程返回true   否则返回false
     */
    public static boolean isMainThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 获取 线程池对象
     * @return
     */
    private static ExecutorService getExecutorService(){
        if (executorService == null) {
            synchronized (ThreadUtils.class) {
                if (executorService == null) {
                    createExcecutor();
                }
            }
        }
        return executorService;
    }

    /**
     * 创建线程池的方法
     */
    private static void createExcecutor(){
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
        executorService = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                taskQueue);
    }

    /**
     * 异步线程执行操作
     * @param run 要执行的Runnable对象
     */
    public static void openSonThread(Runnable run){
        if(run == null)
            return;
        getExecutorService().execute(run);
    }
}
