package com.jju.yuxin.cinews.activity;


import android.animation.ObjectAnimator;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.utils.ActivityCollector;
import com.jju.yuxin.cinews.utils.NetUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName LoadingActivity
 * Created by yuxin.
 * Created time 14-11-2016 20:48.
 * Describe :启动页面
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */

public class LoadingActivity extends BaseActivity {
    private static final int DALAY_TIME = 1500;//设置启动延时
    private LinearLayout show_message;  //显示提示信息布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //SharedSDK的初始化
        ShareSDK.initSDK(this);



        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(LoadingActivity.this);
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

        //检查联网状态
        if (NetUtils.isConnected(LoadingActivity.this) == NetUtils.NO_CONNECTED) {
            hlog.e("联网失败");
            JPushInterface.onResume(this);
            //开启一个定时器,在1.5秒后跳出提示信息
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    //运行在UI线程的内容
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //提示用户是否跳转到网络设置界面
                            show_message = (LinearLayout) findViewById(R.id.show_message);
                            //设置信息提示界面可见
                            show_message.setVisibility(View.VISIBLE);
                            //属性动画
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                                ObjectAnimator animator = ObjectAnimator.ofFloat(show_message, "translationY", -150, 0);
                                animator.setDuration(500);
                                animator.setInterpolator(new BounceInterpolator());
                                animator.start();
                            }

                            //确定按钮
                            Button bt_left = (Button) findViewById(R.id.bt_left);
                            bt_left.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //跳转到无线和网络界面
                                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            });
                            //取消按钮
                            Button bt_right = (Button) findViewById(R.id.bt_right);
                            bt_right.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //退出程序
                                    ActivityCollector.finishAll();
                                }
                            });

                        }
                    });
                }
            }, DALAY_TIME);
        } else {

            //极光推送的初始化
            JPushInterface.setDebugMode(true);//如果时正式版就改成false
            JPushInterface.init(this);
            //用于极光推送的统计
            JPushInterface.onResume(this);
            //网络连接正常
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    //网络连接正常进入主界面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadMainActivity();
                        }
                    });

                }
            }, DALAY_TIME);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //用于极光推送的统计
        JPushInterface.onPause(this);
    }

    /**
     * 跳转到用户主界面
     */
    public void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        //第一个参数为MainActivity进入的动画效果，第二个为LoadingActivity退出的效果
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down);

    }
}
