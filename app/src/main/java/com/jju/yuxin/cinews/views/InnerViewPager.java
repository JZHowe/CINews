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
 * Created time 2016/11/12 0012 下午 9:40.
 * Version   1.0;
 * Describe :里层的viewpages
 * History:
 * ==============================================================================
 */


public class InnerViewPager extends ViewPager {

    public InnerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerViewPager(Context context) {
        super(context);
    }

    /**
     * 重写触摸事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 假设vp少于一个子空间，直接返回原来的处理方式
        //viewpager多个子view初始化存在两个子view,这里判断的是只有一个或未初始化的状态
        if (getChildCount() <= 1) {
            return super.onTouchEvent(event);
        }
        // 获取事件类型
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取父控件，请求不拦截触摸事件,让当前viewpager处理触摸事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                // 获取父控件，请求不拦截触摸事件,让当前viewpager处理触摸事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                // 获取父控件，请求拦截触摸事件,让上层控件处理触摸事件
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                // 获取父控件，请求拦截触摸事件,让上层控件处理触摸事件
                getParent().requestDisallowInterceptTouchEvent(false);
                break;

            default:
                break;
        }
        // 执行父控件的onTouch事件
        super.onTouchEvent(event);
        return true;
    }
}
