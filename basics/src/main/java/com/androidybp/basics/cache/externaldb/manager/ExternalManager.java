package com.androidybp.basics.cache.externaldb.manager;

import android.database.sqlite.SQLiteDatabase;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.externaldb.ExternalHelper;
import com.androidybp.basics.cache.externaldb.model.FileNameModel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程并发使用一个管理工具类   目前测试使用
 */

public class ExternalManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();
    private static volatile ExternalManager instance;
    private static ExternalHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;


    private ExternalManager(){
        FileNameModel model = new FileNameModel();
        String path = model.getExternalFilePath();
        mDatabaseHelper = new ExternalHelper(ApplicationContext.getInstance().context, path);
    }


    /**
     * 创建当前的单例对象
     *
     * @return
     */
    public static ExternalManager getInstance() {
        if (instance == null) {
            synchronized (ExternalManager.class) {
                if (instance == null) {
                    createExternalManager();
                }
            }
        }
        return instance;
    }

    public static synchronized void createExternalManager() {
        instance = new ExternalManager();
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
//        if (mDatabase == null) {
            // Opening new database
            try {
                mDatabase = mDatabaseHelper.getWritableDatabase();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return mDatabase;
    }

    public synchronized void close() {
        if (mOpenCounter.decrementAndGet() <= 0 && mDatabase != null) {
            // Closing database
            mDatabase.close();
        }
    }
}
