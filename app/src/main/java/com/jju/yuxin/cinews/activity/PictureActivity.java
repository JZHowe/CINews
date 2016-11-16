package com.jju.yuxin.cinews.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private Map<String, Object> oMap = new HashMap<>();
    private String name = "getlistartile";
    private List<NewsBean> olist = new ArrayList<>();
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

        }
    };
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        listView = (ListView) findViewById(R.id.p_listView);

        oMap.put("id", "404d560a-442c-46a5-b1cf-c44fe459f893");
        oMap.put("page", 1);
        Ksoap2Util.doBackgroud(handler, R.id.text4, name, oMap);

    }



}
