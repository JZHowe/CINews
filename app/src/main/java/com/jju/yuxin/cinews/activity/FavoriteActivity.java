package com.jju.yuxin.cinews.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jju.yuxin.cinews.R;
/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName FavoriteActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:01.
 * Describe :收藏夹Activity
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */

public class FavoriteActivity extends BaseActivity {

    private Button bt_top_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        bt_top_right = (Button) findViewById(R.id.bt_top_right);
        bt_top_right.setVisibility(View.VISIBLE);
        bt_top_right.setBackgroundResource(R.drawable.bt_delete_selector);


    }
}
