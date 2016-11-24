package com.jju.yuxin.cinews.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jju.yuxin.cinews.R;
/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName AboutActivity
 * Created by yuxin.
 * Created time 24-11-2016 08:44.
 * Describe :关于界面
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */

public class AboutActivity extends BaseActivity {
    private Button bt_top_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //将导航栏的左按钮设置为可见并为其设置背景资源
        bt_top_left = (Button) findViewById(R.id.bt_top_left);
        bt_top_left.setVisibility(View.VISIBLE);
        bt_top_left.setBackgroundResource(R.drawable.bt_return_selector);
        //给左侧按键设置点击事件,点击左侧按键将当前activity销毁
        bt_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
