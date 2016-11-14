package com.jju.yuxin.cinews.activity;


import android.animation.ObjectAnimator;
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

    }

    @Override
    protected void onResume() {
        super.onResume();


        //检查联网状态
        if (NetUtils.isConnected(LoadingActivity.this) == NetUtils.NO_CONNECTED) {
            hlog.e("联网失败");

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
            //网络连接正常
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    //网络连接正常进入主界面
                    loadMainActivity();
                }
            }, DALAY_TIME);

        }
    }

    /**
     * 跳转到用户主界面
     */
    public void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
