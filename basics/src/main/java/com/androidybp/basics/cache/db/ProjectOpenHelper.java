package com.androidybp.basics.cache.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.db.assist.InitTableAssist;
import com.androidybp.basics.configuration.BaseProjectConfiguration;
import com.androidybp.basics.utils.hint.LogUtils;

/**
 * 数据库的辅助使用类
 */
public class ProjectOpenHelper extends SQLiteOpenHelper {
    public static volatile ProjectOpenHelper mySqliteOpenHelper ;
    private ProjectOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    /**
     * 创建当前的单例对象
     *
     * @return
     */
    public static ProjectOpenHelper getThis() {
        if (mySqliteOpenHelper == null) {
            synchronized (ProjectOpenHelper.class) {
                if (mySqliteOpenHelper == null) {
                    createZSTOpenHelper(ApplicationContext.getInstance().context, BaseProjectConfiguration.DB_NAME, null, BaseProjectConfiguration.DB_VERSION);
                }
            }
        }
        return mySqliteOpenHelper;
    }
    public static void createZSTOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            mySqliteOpenHelper = new ProjectOpenHelper(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createInitTableData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.showE("ProjectOpenHelper","=======" + oldVersion + "===newVersion====" + newVersion);

        upgradeInitTableData(db, oldVersion, newVersion);
    }


    private void createInitTableData(SQLiteDatabase db) {
        new InitTableAssist().createInitTableData(db);
    }

    private void upgradeInitTableData(SQLiteDatabase db, int oldVersion, int newVersion) {
        new InitTableAssist().upgradeInitTableData(db, oldVersion, newVersion);
    }

}
