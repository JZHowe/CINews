package com.jju.yuxin.cinews.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.weibo.utils
 * Created by yuxin.
 * Created time 2016/9/24 0024 上午 12:20.
 * Version   1.0;
 * Describe : 一个自定义的活动管理器，用于管理活动
 * History:
 * ==============================================================================
 */

public class ActivityCollector {

    //初始化一个list用于放置当前的活动的activity
    public static List<Activity> activities=new ArrayList<>();

    /**
     * 向活动管理器添加Activity
     * @param activity
     */
    public  static  void addActivity(Activity activity){
        activities.add(activity);
    }

    /**
     * 从活动管理器中移除Activity
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activities.remove(activity);
        activity.finish();
    }
    /**
     * 移除活动管理器中的所有Activity
     * @param
     */
    public static void finishAll(){
        for (Activity activity:activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
