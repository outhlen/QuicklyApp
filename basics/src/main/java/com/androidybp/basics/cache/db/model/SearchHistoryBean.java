package com.androidybp.basics.cache.db.model;

/**
 * 搜索历史的对象
 * Created by sll on 2017/6/22 11:22.
 */

public class SearchHistoryBean {

    /**
     * user        登录人账号
     * search_text 搜索的内容
     * search_times_front 搜索次数-前台使用
     * search_times_back 搜索次数-后台使用
     * upload_state 上传状态 0 未上传 1 已上传
     * clear_state 清空状态 0 未清空 1 已清空
     * type
     */
    private String user;
    private String search_text;
    private int search_times_front;
    private int search_times_back;
    private int upload_state;
    private int clear_state;
    private int search_type;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }

    public int getSearch_times_front() {
        return search_times_front;
    }

    public void setSearch_times_front(int search_times_front) {
        this.search_times_front = search_times_front;
    }

    public int getSearch_times_back() {
        return search_times_back;
    }

    public void setSearch_times_back(int search_times_back) {
        this.search_times_back = search_times_back;
    }

    public int getUpload_state() {
        return upload_state;
    }

    public void setUpload_state(int upload_state) {
        this.upload_state = upload_state;
    }

    public int getClear_state() {
        return clear_state;
    }

    public void setClear_state(int clear_state) {
        this.clear_state = clear_state;
    }

    public int getSearch_type() {
        return search_type;
    }

    public void setSearch_type(int search_type) {
        this.search_type = search_type;
    }
}
