package com.androidybp.basics.cache.externaldb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androidybp.basics.cache.externaldb.assist.ExternalChangeTableAssist;
import com.androidybp.basics.configuration.BaseProjectConfiguration;

/**
 * 创建外部数据库对象实体类
 */
public class ExternalHelper extends SQLiteOpenHelper {

    public ExternalHelper(Context context, String name) {
        super(context, name, null, BaseProjectConfiguration.EXTERNAL_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       new ExternalChangeTableAssist().createInitTableData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        new ExternalChangeTableAssist().upgradeInitTableData(db, oldVersion, newVersion);
    }

}