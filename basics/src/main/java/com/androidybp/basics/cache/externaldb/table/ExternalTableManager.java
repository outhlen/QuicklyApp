package com.androidybp.basics.cache.externaldb.table;


import android.database.sqlite.SQLiteDatabase;

/**
 * 创建表的方法  一张表对应一个方法
 */
public interface ExternalTableManager {

     /**
     * 创建发布页面使用的缓存 交货地缓存表   data_cache_all  表对应方法
     */
    void dataCacheAllTable(SQLiteDatabase db);



}
