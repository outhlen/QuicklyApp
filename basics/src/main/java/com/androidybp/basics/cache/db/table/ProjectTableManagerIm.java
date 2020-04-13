package com.androidybp.basics.cache.db.table;

import android.database.sqlite.SQLiteDatabase;

public class ProjectTableManagerIm implements ProjectTableManager {


    @Override
    public void searchHistoryTable(SQLiteDatabase db) {
        //创建信息关键字保存历史缓存表
        db.execSQL("CREATE TABLE IF NOT EXISTS search_history  (sid  integer primary key autoincrement ,user varchar(40),search_text varchar(40),search_times_front integer,search_times_back integer,upload_state integer,clear_state integer,search_type integer)");
    }


    @Override
    public void dataCacheTable(SQLiteDatabase db) {
        //创建数据缓存表  所有的用户数据缓存都以字符串格式保存起来
        db.execSQL("CREATE TABLE IF NOT EXISTS data_cache (cid  integer primary key autoincrement, cname varchar(20), value varchar(2048),state integer,preserve_time integer,create_date varchar(40), update_date varchar(40),priority integer,assist_1 varchar(40),assist_2 varchar(40))");
    }
}
