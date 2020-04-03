package com.androidybp.basics.cache.db.table;


import android.database.sqlite.SQLiteDatabase;

/**
 * 创建表的方法  一张表对应一个方法
 */
public interface ProjectTableManager {

     /**
     * 创建关键字保存历史缓存表   search_history  表对应方法
     */
    void searchHistoryTable(SQLiteDatabase db);

     /**
     * 创建数据缓存表  所有的用户数据缓存都以字符串格式保存起来   data_cache  表对应方法
     */
    void dataCacheTable(SQLiteDatabase db);



}
