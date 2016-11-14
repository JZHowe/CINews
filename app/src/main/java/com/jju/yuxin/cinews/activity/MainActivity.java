package com.jju.yuxin.cinews.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.jju.yuxin.cinews.R;
/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName MainActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:01.
 * Describe : 主Activity
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */

public class MainActivity extends TabActivity {

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取当前TabActivity的tabhost
        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //声明四个Intent的跳转

        //新闻
        intent=new Intent(MainActivity.this,NewsActivity.class);
        spec = tabHost.newTabSpec("news").setIndicator("news").setContent(intent);
        tabHost.addTab(spec);

        //图片
        intent=new Intent(MainActivity.this,PictureActivity.class);
        spec = tabHost.newTabSpec("picture").setIndicator("picture").setContent(intent);
        tabHost.addTab(spec);

        //视频
        intent=new Intent(MainActivity.this,VedioActivity.class);
        spec = tabHost.newTabSpec("vedio").setIndicator("vedio").setContent(intent);
        tabHost.addTab(spec);

        //收藏夹
        intent=new Intent(MainActivity.this,FavoriteActivity.class);
        spec = tabHost.newTabSpec("favorite").setIndicator("favorite").setContent(intent);
        tabHost.addTab(spec);


        //设置tabhost的初始位置
        tabHost.setCurrentTab(0);

        //主页的底栏用RadioGroup实现
        RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
        //给radiogroup设置点击事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //切换到新闻模块
                    case R.id.main_tab_news:
                        tabHost.setCurrentTabByTag("news");
                        break;
                    //切换到图片模块
                    case R.id.main_tab_picture:
                        tabHost.setCurrentTabByTag("picture");
                        break;
                    //切换到视频模块
                    case R.id.main_tab_vedio://视频
                        tabHost.setCurrentTabByTag("vedio");
                        break;
                    //切换到收藏夹模块
                    case R.id.main_tab_favorite://收藏夹
                        tabHost.setCurrentTabByTag("favorite");
                        break;
                    //默认为新闻模块
                    default:
                        tabHost.setCurrentTabByTag("news");
                        break;
                }
            }
        });


    }
}
