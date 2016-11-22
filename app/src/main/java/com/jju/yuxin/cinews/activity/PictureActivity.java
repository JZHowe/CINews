package com.jju.yuxin.cinews.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.adapter.PAdapter;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.service.JsonUtil;
import com.jju.yuxin.cinews.utils.ActivityCollector;
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
    private static final String name = "getlistartile";
    private Map<String, Object> oMap = new HashMap<>();   //发送数据是的omap
    private List<NewsBean> olist = new ArrayList<>();
    private PullToRefreshListView listView;
    private int mItemCount = 9;
    private LinearLayout pb_loading;   //进来时加载的动画

    private PAdapter pAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.id.text4:
                    String info = (String) msg.obj;
                    MyLogger.lLog().e(info);
                    olist = JsonUtil.parseJSON(info);
                    if (olist != null) {
                        pAdapter = new PAdapter(PictureActivity.this, olist);
                        listView.setAdapter(pAdapter);
                    }

                    break;
            }
            pb_loading.setVisibility(View.GONE);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        listView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

        //加载动画
        pb_loading = (LinearLayout) findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(listener);

        get_Data();

        //listview设置下拉刷新 上拉加载
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                llog.e("onPullDownToRefresh");
                //这里写下拉刷新的任务
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh");
                //这里写上拉加载更多的任务
                new GetDataTask().execute();
            }
        });

    }

    private void get_Data() {
        oMap.put("id", ID);
        oMap.put("page", 1);
        Ksoap2Util.doBackgroud(handler, R.id.text4, name, oMap);
    }

    //列表项点击事件
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(PictureActivity.this, NewsDetailsActivity.class);
            intent.putExtra("news", olist.get(position - 1));
            startActivity(intent);
        }
    };


    //这里执行刷新操作
    private class GetDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return "" + (mItemCount++);
        }

        @Override
        protected void onPostExecute(String result) {
            //刷新时再次获取数据
            get_Data();
            pAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            listView.onRefreshComplete();
        }
    }

    /**
     * 双击退出
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return MainActivity.doubleExit(PictureActivity.this);
        }
        return super.onKeyUp(keyCode, event);
    }
}
