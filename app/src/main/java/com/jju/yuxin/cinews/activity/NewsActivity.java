package com.jju.yuxin.cinews.activity;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.adapter.News_Pageadapter;
import com.jju.yuxin.cinews.views.OuterViewPager;

import java.util.ArrayList;
import java.util.List;
/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName NewsActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:02.
 * Describe :新闻Activity
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */

public class NewsActivity extends BaseActivity {

    private PagerTabStrip pt_new_content;
    private String[] new_title;
    private OuterViewPager vp_new_content;
    private List<View> viewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        viewList = new ArrayList<>();

        //外层的Viewpager
        vp_new_content = (OuterViewPager) findViewById(R.id.vp_new_content);
        //外层Viewpager对应的PagerTabStrip导航栏,如果要自定义,应该将其替换
        pt_new_content = (PagerTabStrip) findViewById(R.id.pt_new_content);

        //ViewPageritem的item数据模拟初始化
        datainit(vp_new_content);


    }

    /**
     *ViewPageritem的item数据模拟初始化,需要在这里实现对ViewPager的增删
     */
    private void datainit(OuterViewPager vp_new_content) {

        //获取ViewPager对应的导航栏的文字信息
        new_title = getResources().getStringArray(R.array.new_title_list);
        //初始化item布局个数
        for (int i = 0; i < new_title.length; i++) {

            //新闻模块所有子模块对应的相同的布局
            LinearLayout view1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.news_item, null);

            //将当前的标题的hashcode设置为view的tag
            view1.setTag(new_title[i]);

            //获取当前item对应的顶栏
            LinearLayout ll_top= (LinearLayout) view1.findViewById(R.id.ll_top);

            //设置所有视图的标记为不可见,.标记的是里层viewpages的状态
            ll_top.setTag(View.GONE);

            viewList.add(view1);
        }
        //设置第一个可见
        viewList.get(0).findViewById(R.id.ll_top).setTag(View.VISIBLE);


        //给外层Viewpager设置适配器
        vp_new_content.setAdapter(new News_Pageadapter(this, new_title, viewList));

    }


}
