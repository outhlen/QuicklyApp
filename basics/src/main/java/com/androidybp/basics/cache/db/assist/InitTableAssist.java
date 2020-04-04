package com.androidybp.basics.cache.db.assist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidybp.basics.cache.db.table.ProjectTableManager;
import com.androidybp.basics.cache.db.table.ProjectTableManagerIm;

/**
 * 这就是一个用于辅助  初始化数据使用的类
 */

public class InitTableAssist {

    private ProjectTableManager tableManager;

    public InitTableAssist(){
        tableManager = new ProjectTableManagerIm();
    }

    /**
     * 只有在数据库第一次创建的时候 才会执行这个方法
     *
     * @param db
     */
    public void createInitTableData(SQLiteDatabase db) {
        if (db != null) {
            tableManager.dataCacheTable(db);
            tableManager.searchHistoryTable(db);
        }
    }


    /*

    /**
     * 数据库 从 版本1 到目前为止需要添加的表
     * @param db
     */
    public void upgradeInitTableData(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db != null) {
            tableManager.dataCacheTable(db);
            tableManager.searchHistoryTable(db);
        }
    }



    /**
     * 删除表
     * @param db
     */
    private void dropTable(SQLiteDatabase db, String tableName) {
        if(tableIsExist(db, tableName)) {
            db.execSQL("DROP TABLE " + tableName);
        }
    }

    private boolean tableIsExist(SQLiteDatabase db, String tabName){
        boolean result = false;
        if(tabName == null){
            return result;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*)  from sqlite_master where type='table' and name = ?";
            cursor = db.rawQuery(sql, new String[]{tabName});
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }
            cursor.close();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 检查某表列是否存在
     * @param db
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    private boolean checkColumnExist(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            //查询一行
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0"
                    , null);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return result;
    }

}
