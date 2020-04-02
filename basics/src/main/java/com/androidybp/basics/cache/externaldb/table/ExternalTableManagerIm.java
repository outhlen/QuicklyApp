package com.androidybp.basics.cache.externaldb.table;

import android.database.sqlite.SQLiteDatabase;

public class ExternalTableManagerIm implements ExternalTableManager {
    @Override
    public void dataCacheAllTable(SQLiteDatabase db) {
        //创建数据缓存表  所有的数据缓存都以字符串格式保存起来
        db.execSQL("CREATE TABLE IF NOT EXISTS data_cache_all (cid  integer primary key autoincrement, cname varchar(20), value varchar(2048),state integer,preserve_time integer,create_date varchar(40), update_date varchar(40),priority integer,assist_1 varchar(40),assist_2 varchar(40))");
    }
}
