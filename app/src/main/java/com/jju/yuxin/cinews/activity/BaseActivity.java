package com.jju.yuxin.cinews.activity;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.utils.ActivityCollector;
import com.jju.yuxin.cinews.utils.MyLogger;
import com.jju.yuxin.cinews.volleyutils.VolleyUtils;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

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
        //将所有的activity跳转都带动画
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down);



        //极光推送的初始化
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);


        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(BaseActivity.this);
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);

    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
