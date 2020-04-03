package com.androidybp.basics.cache.db.publicdb.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidybp.basics.cache.db.model.SearchHistoryBean;
import com.androidybp.basics.utils.hint.LogUtils;

import java.util.List;

/**
 * 操作 表 search_history 的工具类
 * Created by yangbp on 2018/9/11 19:00.
 */
public class SearchHistoryDB extends LocalDBMessage<SearchHistoryBean> {

    private static final String TABLE = "search_history";//表名

    public SearchHistoryDB() {
    }
    public SearchHistoryDB(Context context) {
    }

    /**
     * 搜索历史添加数据，先判断是否已有相同内容，
     * 是（进入更改数据）
     * 否 新加
     *
     * @param searchHistoryBean  SearchHistoryBean 对象
     * @auther sll @date 2017/6/22 10:40
     */
    public synchronized void addSearchHistory(SearchHistoryBean searchHistoryBean) {
        SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
        List<SearchHistoryBean> list = query(sqLiteDatabase, TABLE, null, "user = ? AND search_text = ?", new String[]{searchHistoryBean.getUser(), searchHistoryBean.getSearch_text()}, null, null, null);
        if (list != null && list.size() > 0) {
            updateSearchHistory(sqLiteDatabase, list.get(0));
        } else {
            ContentValues values = new ContentValues();//创建一个用于存放列的值的对象，底层是封装了一个map集合
            values.put("user", searchHistoryBean.getUser());
            values.put("search_text", searchHistoryBean.getSearch_text());
            values.put("search_times_front", 1);
            values.put("search_times_back", 1);
            values.put("upload_state", 0);
            values.put("clear_state", 0);
            values.put("search_type", searchHistoryBean.getSearch_type());
            add(sqLiteDatabase, TABLE, values);
        }
        closeSQLiteDatabase();
    }

    /**
     * 搜索历史添加数据，先判断是否已有相同内容，
     * 是（进入更改数据）
     * 否 新加
     *
     * @param searchHistoryBean  SearchHistoryBean 对象
     * @auther sll @date 2017/6/22 10:40
     */
    public synchronized void addSearchHistoryType(SearchHistoryBean searchHistoryBean) {
        SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
        List<SearchHistoryBean> list = query(sqLiteDatabase, TABLE, null, "user = ? AND search_text = ? AND search_type = " + searchHistoryBean.getSearch_type(),
                new String[]{searchHistoryBean.getUser(), searchHistoryBean.getSearch_text()}, null, null, null);
        if (list != null && list.size() > 0) {
            updateSearchHistory(sqLiteDatabase, list.get(0));
        } else {
            ContentValues values = new ContentValues();//创建一个用于存放列的值的对象，底层是封装了一个map集合
            values.put("user", searchHistoryBean.getUser());
            values.put("search_text", searchHistoryBean.getSearch_text());
            values.put("search_times_front", 1);
            values.put("search_times_back", 1);
            values.put("upload_state", 0);
            values.put("clear_state", 0);
            values.put("search_type",searchHistoryBean.getSearch_type());
            add(sqLiteDatabase, TABLE, values);
        }
        closeSQLiteDatabase();
    }


    /**
     * 搜索历史更改数据
     * 判断如果两个次数状态
     * 若 search_times_front == 0 修改 clear_state = 0
     * 若 search_times_back == 0 修改 upload_state = 0
     * 增加搜索次数（两个次数都加一）
     * @param bean  SearchHistoryBean 对象
     * @auther sll @date 2017/6/22 10:49
     */
    public synchronized void updateSearchHistory(SQLiteDatabase db, SearchHistoryBean bean) {
        ContentValues cv = new ContentValues();
        if (bean.getSearch_times_front() == 0) {
            cv.put("clear_state", 0);
        }
        if (bean.getSearch_times_back() == 0) {
            cv.put("upload_state", 0);
        }
        cv.put("search_times_front", bean.getSearch_times_front() + 1);
        cv.put("search_times_back", bean.getSearch_times_back() + 1);

        update(db, TABLE, cv, "user = ? AND search_text = ?", new String[]{bean.getUser(), bean.getSearch_text()});
    }

    /**
     * 查询数据库用于展示历史搜索，按照search_times_front降序取前 条
     * LIMIT 0,$3
     *
     * @param user 登录用户user
     * @auther sll @date 2017/6/22 15:31
     */
    public synchronized List<SearchHistoryBean> querySearchHistory(String user) {
        return query(TABLE, null, "user = ? AND clear_state = ? ", new String[]{user, "0"}, null, null, "search_times_front DESC ");
    }
    /**
     * 查询数据库用于展示历史搜索，按照search_times_front降序取前 条
     * LIMIT 0,$3
     *
     * @param user 登录用户user
     * @param type
     * @auther sll @date 2017/6/22 15:31
     */
    public synchronized List<SearchHistoryBean> querySearchHistoryType(String user,int type) {
        return query(TABLE, null, "user = ? AND clear_state = ? AND search_type = " + type,
                new String[]{user, "0"}, null, null, "search_times_front DESC ");
    }

    /**
     * 实现查找条件变化后 从本地数据库中查找相关联记录 方便查找
     *
     * @param user       用户的user
     * @param searchText 模糊查询的字段
     * @auther sll @date 2017/6/22 15:39
     */
    public synchronized List<SearchHistoryBean> querySearchHistory(String user, String searchText) {
        return query(TABLE, null, "user = ? AND clear_state = ? AND search_text LIKE '%" + searchText + "%' ",
                new String[]{user, "0"}, null, null, "search_times_front DESC ");
    }

    /**
     * 实现查找条件变化后 从本地数据库中查找相关联记录 方便查找
     *
     * @param user       用户的user
     * @param searchText 模糊查询的字段
     * @param type       需要查询的类型 1、信息搜索  2、周边设备信息搜索
     * @auther sll @date 2017/6/22 15:39
     */
    public synchronized List<SearchHistoryBean> querySearchHistoryType(String user, String searchText,int type) {
        String sql = "user = ? AND clear_state = ? AND search_type = "+ type + " AND search_text LIKE '%" + searchText + "%' ";
        LogUtils.showE("=====sql======",sql);
        return query(TABLE, null, sql,
                new String[]{user, "0",}, null, null, "search_times_front DESC ");
    }

    /**
     * 搜索历史清空
     * search_times_front 都致空 =0
     * clear_state 修改为 =1
     *
     * @param user 用户的user
     * @auther sll @date 2017/6/22 10:53
     */
    public synchronized void clearSearchHistory(String user) {
        ContentValues cv = new ContentValues();
        cv.put("search_times_front", 0);
        cv.put("clear_state", 1);
        update(TABLE, cv, "user = ? ", new String[]{user});
    }

    /**
     * 搜索历史清空
     * search_times_front 都致空 =0
     * clear_state 修改为 =1
     *
     * @param user 用户的user
     * @param type
     * @auther sll @date 2017/6/22 10:53
     */
    public synchronized void clearSearchHistoryType(String user,int type) {
        ContentValues cv = new ContentValues();
        cv.put("search_times_front", 0);
        cv.put("clear_state", 1);
        update(TABLE, cv, "user = ? AND search_type = " + type, new String[]{user});
    }

    /**
     * 上传后
     * search_times_back=0
     * upload_state=1
     *
     * @param user 登录用户的user
     * @auther sll @date 2017/6/22 10:57
     */
    public void afterUpload(String user) {
        ContentValues cv = new ContentValues();
        cv.put("search_times_back", 0);
        cv.put("upload_state", 1);
        update(TABLE, cv, "user = ? ", new String[]{user});
    }

    @Override
    public void setQueryData(List<SearchHistoryBean> list, Cursor cursor) {
        SearchHistoryBean bean = new SearchHistoryBean();
        bean.setUser(cursor.getString(cursor.getColumnIndex("user")));
        bean.setSearch_text(cursor.getString(cursor.getColumnIndex("search_text")));
        bean.setSearch_times_front(cursor.getInt(cursor.getColumnIndex("search_times_front")));
        bean.setSearch_times_back(cursor.getInt(cursor.getColumnIndex("search_times_back")));
        bean.setUpload_state(cursor.getInt(cursor.getColumnIndex("upload_state")));
        bean.setClear_state(cursor.getInt(cursor.getColumnIndex("clear_state")));
        list.add(bean);
    }

    /**
     * 删除数据库中多余的数据   只保留 50条数据   排序根据 search_times_front  倒叙排列
     * @param db
     */
    public void deleteRedundancyData(SQLiteDatabase db) {
        String[] sqlD = {"50", "50"};
        String sql = "delete from search_history where (select count(search_text) from search_history )> ? and search_text in (select search_text from search_history order by search_times_front desc limit (select count(search_text) from search_history) offset ? ) ";
        try {
            db.execSQL(sql, sqlD);
        }catch (Exception e){

        }
    }
}
