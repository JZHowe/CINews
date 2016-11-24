package com.jju.yuxin.cinews.activity;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Window;
import android.view.WindowManager;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.utils.ActivityCollector;
import com.jju.yuxin.cinews.utils.MyLogger;
import com.jju.yuxin.cinews.volleyutils.VolleyUtils;

import cn.jpush.android.api.JPushInterface;

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

public class BaseActivity extends AppCompatActivity {

    public MyLogger hlog;
    public MyLogger jlog;
    public MyLogger llog;
    public MyLogger zlog;
    public VolleyUtils volleyUtils;
    private MyBroad broad;
    private static final String TAG=BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

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
        //将所有的activity跳转都带动画
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down);



        //极光推送的初始化
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);
        //给设备设置别名
//        JPushInterface.setAlias(this, "hu", new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//
//            }
//        });

        //自定义普通推送
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(BaseActivity.this);
//        builder.statusBarDrawable = R.mipmap.ic_launcher;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
//                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
//        builder.notificationDefaults = Notification.DEFAULT_SOUND
//                | Notification.DEFAULT_VIBRATE
//                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
//        JPushInterface.setPushNotificationBuilder(1, builder);

        broad = new MyBroad();
        IntentFilter filter = new IntentFilter("MyBroad");
        registerReceiver(broad,filter);
    }

    //夜日间模式广播接收
    class MyBroad extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isDark = intent.getBooleanExtra("isDark",false);
            if(!isDark){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
//            recreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broad!=null){
            unregisterReceiver(broad);
        }
        //当执行onDestroy()方法，将activity从活动管理器中移除
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //用于极光推送的统计
        JPushInterface.onPause(this);
    }

}
