package com.jju.yuxin.cinews.activity;

import android.os.Bundle;

import com.jju.yuxin.cinews.R;

/**
 * =============================================================================
 * Copyright (c) 2016 Card All rights reserved.
 * Packname com.jju.yuxin.cinews.activity
 * Created by Card.
 * Created time 2016/11/22 20:29.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class TestActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        finish();
    }
}
