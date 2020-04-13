package com.androidybp.basics.cache.db.publicdb.localdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.androidybp.basics.cache.db.manager.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个操作本地数据的一个 管理类
 */

public abstract class LocalDBMessage<T> {

    protected DatabaseManager mDatabaseManager = DatabaseManager.getInstance();
    protected java.util.concurrent.Semaphore semaphoreTransaction = new java.util.concurrent.Semaphore(1);
    protected ThreadLocal<Boolean> isQuery = new ThreadLocal<>();

    /**
     * 子类使用 获取 SQLiteDatabase对象
     * @return
     */
    protected synchronized SQLiteDatabase getSQLiteDatabase(){
        return mDatabaseManager.getWritableDatabase();
    }

    /**
     * 子类使用 关闭SQLiteDatabase对象
     */
    protected synchronized void closeSQLiteDatabase(){
        mDatabaseManager.close();
    }

    /**
     * 数据库单条数据 添加方法
     *
     * @param table  表明
     * @param values 需要添加的数据对象
     * @return 是否添加成功
     */
    public boolean add(String table, ContentValues values) {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        try {
            semaphoreTransaction.acquire();
            db.beginTransaction();
            long result = db.insert(table, null, values);//底层都是在拼装sql语句
            db.setTransactionSuccessful();
            db.endTransaction();
            semaphoreTransaction.release();
            if (result != -1) {
                return true;
            }
        } catch (Exception e) {
            db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
            semaphoreTransaction.release();
        }
        mDatabaseManager.close();
        return false;
    }
    /**
     * 数据库单条数据 添加方法
     *
     * @param table  表明
     * @param values 需要添加的数据对象
     * @return 是否添加成功
     */
    public boolean add(SQLiteDatabase db, String table, ContentValues values) {
        try {
            long result = db.insert(table, null, values);//底层都是在拼装sql语句
            if (result != -1) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 数据库批量 添加方法
     *
     * @param table  表明
     * @param values 需要添加的数据对象
     * @return 是否添加成功
     */
    public int addListData(String table, List<ContentValues> values) {
        if (values != null && values.size() > 0 && !TextUtils.isEmpty(table)) {
            SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
            int number = 0;
            try {
                semaphoreTransaction.acquire();
                db.beginTransaction();
                for (ContentValues cv : values) {
                    long result = db.insert(table, null, cv);//底层都是在拼装sql语句
                    if (result != -1) {
                        number++;
                    }
                }
                db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
                db.endTransaction();
                semaphoreTransaction.release();
            } catch (Exception e) {
                number = 0;
                db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
                semaphoreTransaction.release();
            }
            mDatabaseManager.close();
            return number;
        }

        return 0;
    }

    /**
     * 数据库批量 添加方法
     *
     * @param table  表明
     * @param values 需要添加的数据对象
     * @return 是否添加成功
     */
    public synchronized int addListData(SQLiteDatabase db, String table, List<ContentValues> values) {
        int number = 0;
        if (values != null && values.size() > 0 && !TextUtils.isEmpty(table)) {

            try {
                for (ContentValues cv : values) {
                    long result = db.insert(table, null, cv);//底层都是在拼装sql语句
                    if (result != -1) {
                        number++;
                    }
                }
            } catch (Exception e) {
            }
            return number;
        }
        return number;
    }



    /**
     * 删除方法   单条件删除数据
     *
     * @param table       表明
     * @param whereClause 查询条件
     * @param whereArgs   占位符
     * @return 删除的条数
     */
    public int delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        int number;
        try {
            semaphoreTransaction.acquire();
            db.beginTransaction();
            number = db.delete(table, whereClause, whereArgs);
            db.setTransactionSuccessful();
            db.endTransaction();
            semaphoreTransaction.release();
        } catch (Exception e) {
            number = 0;
            db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
            semaphoreTransaction.release();
        }
        mDatabaseManager.close();
        return number;
    }


    /**
     * @param table       表明
     * @param values      要更改的列的值
     * @param whereClause 更新条件
     * @param whereArgs   条件占位符的值
     * @return 更新了多少行
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {

        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        int number;
        try {
            semaphoreTransaction.acquire();
            db.beginTransaction();
            number = db.update(table, values, whereClause, whereArgs);
            db.setTransactionSuccessful();
            db.endTransaction();
            semaphoreTransaction.release();
        } catch (Exception e) {
            number = 0;
            db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
            semaphoreTransaction.release();
        }
        mDatabaseManager.close();
        return number;
    }

    /**
     * @param table       表明
     * @param values      要更改的列的值
     * @param whereClause 更新条件
     * @param whereArgs   条件占位符的值
     * @return 更新了多少行
     */
    public synchronized int update(SQLiteDatabase db, String table, ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(table, values, whereClause, whereArgs);
    }

    /**
     *
     * @param table             表名
     * @param columns           查询哪些列  如果传null代表查询所有列
     * @param selection         查询条件
     * @param selectionArgs     条件占位符的值
     * @param groupBy           按什么分组查询
     * @param having            分组的条件
     * @param orderBy           按哪个字段排序 desc 降序排序  asc 升序排序
     */
    public synchronized List<T> query(String table, String[] columns, String selection,
                      String[] selectionArgs, String groupBy, String having,
                      String orderBy) {
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        List<T> arr = null;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            if (cursor != null && cursor.getCount() > 0) {
                arr = creatQueryData();
                while (cursor.moveToNext()) {
                    setQueryData(arr, cursor);
                }
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabaseManager.close();
        }
        return arr;
    }

    /**
     * @param db                数据库操作对象
     * @param table             表名
     * @param columns           查询哪些列  如果传null代表查询所有列
     * @param selection         查询条件
     * @param selectionArgs     条件占位符的值
     * @param groupBy           按什么分组查询
     * @param having            分组的条件
     * @param orderBy           按哪个字段排序 desc 降序排序  asc 升序排序
     */
    public synchronized List<T> query(SQLiteDatabase db, String table, String[] columns, String selection,
                      String[] selectionArgs, String groupBy, String having,
                      String orderBy) {
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        List<T> arr = null;
        if (cursor != null && cursor.getCount() > 0) {
            arr = creatQueryData();
            while (cursor.moveToNext()) {
                setQueryData(arr, cursor);
            }
        }
        cursor.close();
        return arr;
    }

    /**
     * 创建一个新的保存集合  子类可以选择性重写
     * @return
     */
    private List<T> creatQueryData() {
        return new ArrayList<>();
    }

    /**
     * 子类需要重写方法
     * @param list    保存数据的列表
     * @param cursor  Cursor对象
     */
    public abstract void setQueryData(List<T> list, Cursor cursor);


}
