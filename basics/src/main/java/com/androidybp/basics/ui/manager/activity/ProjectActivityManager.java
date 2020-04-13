package com.androidybp.basics.ui.manager.activity;

import android.app.Activity;

import com.androidybp.basics.ui.manager.listener.LoginActInstener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理工具类 用于缓存以打开所有页面
 */
public class ProjectActivityManager {


    private List<Activity> activityList = new ArrayList<>();

    private static volatile ProjectActivityManager manager;


    private ProjectActivityManager(){}

    public static ProjectActivityManager getManager(){
        if(manager == null){
            synchronized (ProjectActivityManager.class){
                if(manager == null){
                    manager = new ProjectActivityManager();
                }
            }
        }

        return manager;
    }


    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public Activity getOpenAct(){
        if(activityList != null && activityList.size() > 0){
            return activityList.get(activityList.size() - 1);
        }
        return null;
    }

    public void finishAll() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public void finishActsNotLogin(){
        for (Activity activity : activityList) {
            if(activity instanceof LoginActInstener){

            } else {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }

        }
    }
}
