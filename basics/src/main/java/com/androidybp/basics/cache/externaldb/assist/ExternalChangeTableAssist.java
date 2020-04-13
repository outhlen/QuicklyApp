package com.androidybp.basics.cache.externaldb.assist;

import android.database.sqlite.SQLiteDatabase;

import com.androidybp.basics.cache.externaldb.table.ExternalTableManager;
import com.androidybp.basics.cache.externaldb.table.ExternalTableManagerIm;

/**
 * 这就是一个用于辅助  初始化数据使用的类
 */

public class ExternalChangeTableAssist {

    private ExternalTableManager tableManager;

    public ExternalChangeTableAssist(){
        tableManager = new ExternalTableManagerIm();
    }

    /**
     * 只有在数据库第一次创建的时候 才会执行这个方法
     *
     * @param db
     */
    public void createInitTableData(SQLiteDatabase db) {
        if (db != null) {
            createTable(db);
        }
    }

    private void createTable(SQLiteDatabase db) {
        if(tableManager != null){
            //创建数据缓存 表  目前这个表是共用的
            tableManager.dataCacheAllTable(db);
        }
    }

    /**
     * 由于目前都是强制升级   为了避免用户下载老版本数据  在数据库升级的时候后做必要数据操作
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void upgradeInitTableData(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
