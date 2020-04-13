package com.androidybp.basics.cache.db.manager;

import android.database.sqlite.SQLiteDatabase;

import com.androidybp.basics.cache.db.ProjectOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程并发使用一个管理工具类   目前测试使用
 */

public class DatabaseManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();
    private static volatile DatabaseManager instance;
    private ProjectOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;


    private DatabaseManager(){
        mDatabaseHelper = ProjectOpenHelper.getThis();
    }


    /**
     * 创建当前的单例对象
     *
     * @return
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    createDatabaseManager();
                }
            }
        }
        return instance;
    }

    public static synchronized void createDatabaseManager() {
        instance = new DatabaseManager();
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mOpenCounter != null && mOpenCounter.incrementAndGet() == 1) {
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
        if (mOpenCounter != null && mOpenCounter.decrementAndGet() <= 0) {
            // Closing database
            if (mDatabase != null)
                mDatabase.close();

        }
    }
}
