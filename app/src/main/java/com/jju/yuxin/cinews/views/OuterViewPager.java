package com.jju.yuxin.cinews.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.views
 * Created by yuxin.
 * Created time 2016/11/12 0012 下午 10:11.
 * Version   1.0;
 * Describe : 外层的viewpages
 * History:
 * ==============================================================================
 */
public class OuterViewPager extends ViewPager {

    public OuterViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public OuterViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /**
     * 设置不拦截触摸事件false 拦截true
     */
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent arg0) {
//        return true;
//    }

}
