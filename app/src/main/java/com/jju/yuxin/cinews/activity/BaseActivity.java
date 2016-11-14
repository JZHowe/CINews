package com.jju.yuxin.cinews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.jju.yuxin.cinews.utils.ActivityCollector;
import com.jju.yuxin.cinews.utils.MyLogger;

/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName BaseActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:00.
 * Describe : baseactivity
 * History:
 * Version   1.0.
 * 添加一行注释
 * chao
 * ==============================================================================
 */

public class BaseActivity extends Activity {

    private MyLogger hlog;
    private MyLogger jlog;
    private MyLogger llog;
    private MyLogger zlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        hlog = MyLogger.hLog();
        jlog = MyLogger.jLog();
        llog = MyLogger.lLog();
        zlog = MyLogger.zLog();
        //打印输出当前所在activity
        hlog.i("--------------" + getClass().getSimpleName() + "--------------");
        //将当前activity添加置活动管理器
        ActivityCollector.addActivity(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当执行onDestroy()方法，将activity从活动管理器中移除
        ActivityCollector.removeActivity(this);
    }
}
