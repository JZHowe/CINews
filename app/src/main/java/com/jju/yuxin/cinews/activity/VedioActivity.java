package com.jju.yuxin.cinews.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.adapter.VedioList_Adapter;
import com.jju.yuxin.cinews.bean.VedioInfoBean;
import com.jju.yuxin.cinews.utils.ActivityCollector;
import com.jju.yuxin.cinews.utils.JsoupUtils;

import java.util.List;

/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName VedioActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:02.
 * Describe :视频activity
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */
public class VedioActivity extends BaseActivity {
    //视频新闻爬取地址
    private static final String path = "http://www.jxntv.cn/";
    //加载成功
    private static final int SUCCESS_LOAD = 0;
    //加载失败
    private static final int FAIL_LOAD = 1;

    private int mItemCount = 9;
    //加载完成的新闻集合
    private List<VedioInfoBean> vedioinfos;

    private PullToRefreshListView lv_vedio_news;
    //加载动画
    private LinearLayout pb_loading;
    private VedioList_Adapter vedioList_adapter;

    Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //加载成功
                case SUCCESS_LOAD:
                    //第一次adapter没有初始化直接进入赋值
                    if (vedioList_adapter == null) {
                        vedioinfos = (List<VedioInfoBean>) msg.obj;
                        //将内容填充到ListView
                        vedioList_adapter = new VedioList_Adapter(VedioActivity.this, vedioinfos,
                                volleyUtils);
                        lv_vedio_news.setAdapter(vedioList_adapter);
                        //adapter初始化完成直接更新里面的list集合内容
                    } else {
                        vedioinfos = (List<VedioInfoBean>) msg.obj;
                        vedioList_adapter.replaceList(vedioinfos);
                        vedioList_adapter.notifyDataSetChanged();
                    }
                    //刷新完成标志
                    lv_vedio_news.onRefreshComplete();
                    break;
                //加载失败
                case FAIL_LOAD:
                    Exception e = (Exception) msg.obj;
                    hlog.e(e.getMessage());
                    break;
                default:
                    break;
            }
            //不管是否加载成功,让加载动画消失
            pb_loading.setVisibility(View.GONE);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio);
        //新闻列表
        lv_vedio_news = (PullToRefreshListView) findViewById(R.id.lv_vedio_news);

        lv_vedio_news.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//向下拉刷新

        //加载动画
        pb_loading = (LinearLayout) findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);

        //列表点击事件
        lv_vedio_news.setOnItemClickListener(listener);

        //刷新监听
        lv_vedio_news.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //下拉刷新操作
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
                //这里写下拉刷新的任务
                //获取新闻
                JsoupUtils.getNewPaper(path, mhandler);

            }

            //上拉加载操作
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

    }

    //列表项点击事件
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //获取当前点击的item的对象
            //因为存在顶部刷新栏,所以点击的位置要相对于adapter里面的list的position后一位
            VedioInfoBean vedioInfoBean = vedioinfos.get(position - 1);
            //跳转至新闻详情页面
            Intent intent = new Intent(VedioActivity.this, VedioNewsDetailsActivity.class);
            intent.putExtra("vedio_news", vedioInfoBean);
            startActivity(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //获取新闻
        JsoupUtils.getNewPaper(path, mhandler);
    }

    /**
     * 双击退出
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return MainActivity.doubleExit(VedioActivity.this);
        }
        return super.onKeyUp(keyCode, event);
    }
}
