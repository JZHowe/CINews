package com.jju.yuxin.cinews.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.jju.yuxin.cinews.utils.ActivityCollector;
import com.jju.yuxin.cinews.utils.MyLogger;
import com.jju.yuxin.cinews.volleyutils.VolleyUtils;

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
 * ==============================================================================
 */

public class BaseActivity extends Activity {

    public MyLogger hlog;
    public MyLogger jlog;
    public MyLogger llog;
    public MyLogger zlog;
    public VolleyUtils volleyUtils;

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

        //volley工具类的封装,在构造函数中已经对RequestQueue进行了初始化
        volleyUtils = new VolleyUtils(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当执行onDestroy()方法，将activity从活动管理器中移除
        ActivityCollector.removeActivity(this);
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    ActivityCollector.removeActivity(this);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
