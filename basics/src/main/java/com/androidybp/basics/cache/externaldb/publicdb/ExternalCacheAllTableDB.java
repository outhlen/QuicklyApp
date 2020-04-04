package com.androidybp.basics.cache.externaldb.publicdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.androidybp.basics.cache.externaldb.manager.ExternalManager;
import com.androidybp.basics.cache.externaldb.model.DbEncryptionModel;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.utils.date.ProjectDateUtils;
import com.androidybp.basics.utils.encryption.Base64Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yangbp on 2018/9/14.
 * 截止目前为止  所有外部储存的数据都保存当前表里面
 * 当前使用的地点比较多  创建单例
 * <p>
 * <p>
 * 这个类在使用的时候  需要去获取储存权限 否则会失败的   报错
 */

public class ExternalCacheAllTableDB {
    //增  删  改  查
    public static final String TABLE = "data_cache_all";//表名
    protected ExternalManager mExternalManager;

    /**
     * 子类使用 获取 SQLiteDatabase对象
     *
     * @return
     */
    protected synchronized SQLiteDatabase getSQLiteDatabase() {
        if (mExternalManager == null)
            mExternalManager = ExternalManager.getInstance();
        return mExternalManager.getWritableDatabase();
    }


    /**
     * 子类使用 关闭SQLiteDatabase对象
     */
    protected synchronized void closeSQLiteDatabase() {
        try {
            if (mExternalManager != null) {
                mExternalManager.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 添加数据   先判断当前name是否有数据，  有数据就updata   没有数据 inset
     *
     * @param name     缓存数据的名称
     * @param data     缓存数据的内容
     * @param priority 缓存数据的优先级    -1 优先级很高  0 - 优先级 中   1 - 优先级 低（默认）
     * @param oldTime  缓存数据的失效时间   时间的int 值  -1是没有失效时间  秒为单位
     */
    public synchronized void addData(String name, String data, int priority, int oldTime) {
        if (-1 > priority || priority > 1) {
            priority = 1;
        }
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getSQLiteDatabase();
            int dataExist = isDataExist(sqLiteDatabase, new String[]{name});
            String value = Base64Utils.encode(data);
            //数据已经存在
            if (dataExist > 0) {
                updataData(sqLiteDatabase, name, value, priority, oldTime);
            } else {
                //数据不存在 新添加一条数据
                insetData(sqLiteDatabase, name, value, priority, oldTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSQLiteDatabase();

        }
    }


    /**
     * 添加数据   先判断当前name是否有数据，  有数据就updata   没有数据 inset
     *
     * @param name     缓存数据的名称
     * @param data     缓存数据的内容
     * @param priority 缓存数据的优先级    -1 优先级很高  0 - 优先级 中   1 - 优先级 低（默认）
     * @param oldTime  缓存数据的失效时间   时间的int 值  -1是没有失效时间  秒为单位
     */
    public synchronized void addDataAES(String name, String data, int priority, int oldTime) {
        if (-1 > priority || priority > 1) {
            priority = 1;
        }
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getSQLiteDatabase();
            int dataExist = isDataExist(sqLiteDatabase, new String[]{name});
            String value = DbEncryptionModel.getInstance().zstEncryption(data);
            //数据已经存在
            if (dataExist > 0) {
                updataData(sqLiteDatabase, name, value, priority, oldTime);
            } else {
                //数据不存在 新添加一条数据
                insetData(sqLiteDatabase, name, value, priority, oldTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSQLiteDatabase();

        }
    }

    /**
     * 删除方法
     *
     * @param name  名称
     * @param state -1 真删   其他值为假删  只需要改个状态
     */
    public synchronized void deleteData(String name, int state) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getSQLiteDatabase();
            //数据已经存在
            if (state == -1) {
                deleteTabeData(sqLiteDatabase, name);
            } else {
                //数据不存在 新添加一条数据
                updataData(sqLiteDatabase, name);
            }
        } catch (Exception e) {

        } finally {
            closeSQLiteDatabase();


        }
    }

    /**
     * 数据查询  这方法默认使用base64进行加解密的
     *
     * @param name  名称
     * @param clazz 解析的Class对象
     * @param <T>   要解析对象的泛型
     * @return
     */
    public synchronized <T> T questData(String name, Class<T> clazz) {
        SQLiteDatabase sqLiteDatabase = null;
        T data = null;
        String value = null;
        String update_date = null;
        int preserve_time = -1;
        try {
            sqLiteDatabase = getSQLiteDatabase();
            Cursor cursor = sqLiteDatabase.query(TABLE, null, "cname=?", new String[]{name}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    //判断数据是否已经过了有效期
                    update_date = cursor.getString(cursor.getColumnIndex("update_date"));
                    preserve_time = cursor.getInt(cursor.getColumnIndex("preserve_time"));
                    value = Base64Utils.decode(cursor.getString(cursor.getColumnIndex("value")));

                }
            }
            cursor.close();
            boolean isDataPast = isDataPast(sqLiteDatabase, name, update_date, preserve_time);
            if (isDataPast && !TextUtils.isEmpty(value))
                data = JsonManager.getJsonBean(value, clazz);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSQLiteDatabase();
            return data;
        }
    }

    /**
     * 数据查询  这方法默认使用base64进行加解密的
     *
     * @param name  名称
     * @param clazz 解析的Class对象
     * @param <T>   要解析对象的泛型
     * @return
     */
    public synchronized <T> T questDataAES(String name, Class<T> clazz) {
        T data = null;
        String value = questDataAES(name);
        if (!TextUtils.isEmpty(value))
            data = JsonManager.getJsonBean(value, clazz);

        return data;

    }

    /**
     * 数据查询  这方法默认使用base64进行加解密的
     *
     * @param name 名称
     * @return
     */
    public synchronized String questData(String name) {
        SQLiteDatabase sqLiteDatabase = null;
        String value = null;
        String update_date = null;
        int preserve_time = -1;
        try {
            sqLiteDatabase = getSQLiteDatabase();
            Cursor cursor = sqLiteDatabase.query(TABLE, null, "cname=?", new String[]{name}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    //判断数据是否已经过了有效期
                    update_date = cursor.getString(cursor.getColumnIndex("update_date"));
                    preserve_time = cursor.getInt(cursor.getColumnIndex("preserve_time"));
                    value = Base64Utils.decode(cursor.getString(cursor.getColumnIndex("value")));

                }
            }
            cursor.close();
            boolean isDataPast = isDataPast(sqLiteDatabase, name, update_date, preserve_time);
            if (!isDataPast) {
                value = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSQLiteDatabase();
            return value;
        }
    }

    /**
     * 数据查询  这方法默认使用base64进行加解密的
     *
     * @param name 名称
     * @return
     */
    public synchronized String questDataAES(String name) {
        SQLiteDatabase sqLiteDatabase = null;
        String value = null;
        String update_date = null;
        int preserve_time = -1;
        try {
            sqLiteDatabase = getSQLiteDatabase();
            Cursor cursor = sqLiteDatabase.query(TABLE, null, "cname=?", new String[]{name}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    //判断数据是否已经过了有效期
                    update_date = cursor.getString(cursor.getColumnIndex("update_date"));
                    preserve_time = cursor.getInt(cursor.getColumnIndex("preserve_time"));
                    value = DbEncryptionModel.getInstance().zstDecrypt(cursor.getString(cursor.getColumnIndex("value")));

                }
            }
            cursor.close();
            boolean isDataPast = isDataPast(sqLiteDatabase, name, update_date, preserve_time);
            if (!isDataPast) {
                value = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSQLiteDatabase();
            return value;
        }
    }

    /**
     * 数据查询
     *
     * @param name  名称
     * @param clazz 解析的Class对象
     * @param <T>   要解析对象的泛型
     * @return
     */
    public synchronized <T> List<T> questDataList(String name, Class<T> clazz) {
        SQLiteDatabase sqLiteDatabase = null;
        List<T> data = null;
        String value = null;
        String update_date = null;
        int preserve_time = -1;
        try {
            sqLiteDatabase = getSQLiteDatabase();
            Cursor cursor = sqLiteDatabase.query(TABLE, null, "cname=?", new String[]{name}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    //判断数据是否已经过了有效期
                    update_date = cursor.getString(cursor.getColumnIndex("update_date"));
                    preserve_time = cursor.getInt(cursor.getColumnIndex("preserve_time"));
                    value = Base64Utils.decode(cursor.getString(cursor.getColumnIndex("value")));

                }
            }
            cursor.close();
            boolean isDataPast = isDataPast(sqLiteDatabase, name, update_date, preserve_time);
            if (isDataPast && !TextUtils.isEmpty(value))
                data = JsonManager.getArrayBean(value, clazz);

        } catch (Exception e) {

        } finally {
            closeSQLiteDatabase();
            return data;
        }
    }

    /**
     * 用于判断当前数据是否存在   如果存在判断数据是否已经过期了
     *
     * @param sqLiteDatabase 数据库对象
     * @param name           字段名称
     * @param update_date    最后更新时间
     * @param preserve_time  需要保存的时间  秒为单位
     * @return true - 没有失效   false - 失效了
     */
    private boolean isDataPast(SQLiteDatabase sqLiteDatabase, String name, String update_date, int preserve_time) {
        boolean flag = false;
        try {
            //1、判断数值是否有值
            if (!TextUtils.isEmpty(update_date)) {
                if (preserve_time != -1) {
                    //将时间进行转换 判断时间是否过期
                    Date date = new Date();
                    long time = date.getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date parse = simpleDateFormat.parse(update_date);
                    if (parse != null) {
                        long endTime = parse.getTime() + preserve_time * 1000;
                        long interval = endTime - time;
                        if (interval > 0) {
                            flag = true;
                        } else {
                            //将当前数据改为失效状态
                            updataData(sqLiteDatabase, name);
                        }
                    }
                } else {
                    flag = true;
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 删除 表里面数据
     *
     * @param sqLiteDatabase
     * @param name
     * @return
     */
    private boolean deleteTabeData(SQLiteDatabase sqLiteDatabase, String name) {
        try {
            long result = sqLiteDatabase.delete(TABLE, "cname=?", new String[]{name});//
            if (result != -1) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * 更新一条数据
     *
     * @param sqLiteDatabase 数据库对象
     * @param name           名称
     * @return
     */
    private boolean updataData(SQLiteDatabase sqLiteDatabase, String name) {
        String dayHour = ProjectDateUtils.getDayHour();
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", -1);
        contentValues.put("update_date", dayHour);
        try {
            long result = sqLiteDatabase.update(TABLE, contentValues, "cname=?", new String[]{name});//底层都是在拼装sql语句
            if (result != -1) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 更新一条数据
     *
     * @param sqLiteDatabase 数据库对象
     * @param name           名称
     * @param data           保存的字符串
     * @param priority       当前优先级
     * @param oldTime        数据有效时间
     * @return
     */
    private boolean updataData(SQLiteDatabase sqLiteDatabase, String name, String data, int priority, int oldTime) {
        String dayHour = ProjectDateUtils.getDayHour();
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        contentValues.put("value", data);
        contentValues.put("priority", priority);
        contentValues.put("preserve_time", oldTime);
        contentValues.put("update_date", dayHour);
        try {
            long result = sqLiteDatabase.update(TABLE, contentValues, "cname=?", new String[]{name});//底层都是在拼装sql语句
            if (result != -1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 新插入一条数据
     * <p>
     * <p>
     * cname -- 当前保存数据对应的名称
     * state -- 类型  0- 正在使用   -1 废弃
     * value -- 保存的value  值通过 Base64转译了
     * priority -- 优先级   -1 优先级很高  0 - 优先级 中   1 - 优先级 低（默认）
     * preserve_time -- 时间的int 值  -1是没有失效时间  秒为单位
     * create_date -- 创建时间
     * update_date -- 最后一次更新时间
     * assist_1 -- 辅助字段
     * assist_2 -- 辅助字段
     *
     * @param db       数据库对象
     * @param name     名称
     * @param data     保存的字符串
     * @param priority 当前优先级
     * @param oldTime  数据有效时间
     * @return
     */
    private boolean insetData(SQLiteDatabase db, String name, String data, int priority, int oldTime) {
        String dayHour = ProjectDateUtils.getDayHour();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cname", name);
        contentValues.put("state", 0);
        contentValues.put("value", data);
        contentValues.put("priority", priority);
        contentValues.put("create_date", dayHour);
        contentValues.put("preserve_time", oldTime);
        contentValues.put("update_date", dayHour);
        try {
            long result = db.insert(TABLE, null, contentValues);//底层都是在拼装sql语句
            if (result != -1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 判断给定数据是否存在  如果存在  把使用的次数返回
     *
     * @param db
     * @return
     */
    private int isDataExist(SQLiteDatabase db, String[] selectionArgs) {
        int number = -1;
        Cursor cursor = db.query(TABLE, null, "cname=?", selectionArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToNext())
                number = cursor.getCount();
        }
        cursor.close();
        return number;
    }

    public void clearData() {
        mExternalManager = null;
    }

}
