package com.jju.yuxin.cinews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.adapter.PAdapter;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.service.JsonUtil;
import com.jju.yuxin.cinews.utils.Ksoap2Util;
import com.jju.yuxin.cinews.utils.MyLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName PictureActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:02.
 * Describe :图片Activity
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */
public class PictureActivity extends BaseActivity {
    private static final String ID = "404d560a-442c-46a5-b1cf-c44fe459f893";
    private Map<String, Object> oMap = new HashMap<>();
    private String name = "getlistartile";
    private List<NewsBean> olist = new ArrayList<>();
    private LinearLayout pb_loading;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.id.text4:
                    String info = (String) msg.obj;
                    MyLogger.lLog().e(info);
                    olist = JsonUtil.parseJSON(info);
                    listView.setAdapter(new PAdapter(PictureActivity.this,olist));
                    break;
            }
            pb_loading.setVisibility(View.GONE);

        }
    };
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        listView = (ListView) findViewById(R.id.p_listView);

        //加载动画
        pb_loading = (LinearLayout) findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(listener);

        oMap.put("id", ID);
        oMap.put("page", 1);
        Ksoap2Util.doBackgroud(handler, R.id.text4, name, oMap);

    }
    //列表项点击事件
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(PictureActivity.this, NewsDetailsActivity.class);
            intent.putExtra("news",olist.get(position));
            startActivity(intent);
        }
    };

}
