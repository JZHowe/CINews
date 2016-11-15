package com.jju.yuxin.cinews.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.VedioInfoBean;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
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

    private static final String path = "http://www.jxntv.cn/";
    private static final int SUCCESS_LOAD = 0;
    private static final int FAIL_LOAD = 1;

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //加载成功
                case SUCCESS_LOAD:
                    List<VedioInfoBean> vedioinfos = (List<VedioInfoBean>) msg.obj;
                    hlog.e(vedioinfos.toString());

                    break;
                //加载失败
                case FAIL_LOAD:
                    Exception e = (Exception) msg.obj;
                    hlog.e(e.getMessage());
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取新闻
        getNewPaper(path);
    }

    private void getNewPaper(final String path) {
        //联网是耗时操作,开启一个线程来加载新闻资源
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //用于存储视频新闻
                    List<VedioInfoBean> vedioinfos = new ArrayList<VedioInfoBean>();

                    //获取连接
                    Connection connect = Jsoup.connect(path);
                    //设置超时时间
                    connect.timeout(10000);
                    //获取整张网页
                    Document document = connect.get();
                    //通过查询语句,获取指定模块
                    Element news_select = document.select("[class$=secjxdslb]").first();
                    //获取视频新闻列表
                    Element new_list = news_select.select("div.m-bd").first();
                    Elements news_items = new_list.getElementsByTag("li");
                    for (Element element : news_items) {
                        String img_src = element.select("div.pic").first().getElementsByTag("img").first().attr("src");
                        Element info_ele = element.select("div.info").first();
                        String video_src = info_ele.getElementsByTag("a").first().attr("href");
                        String news_info = info_ele.getElementsByTag("a").text();
                        String news_date = info_ele.select("div.date").first().text();
                        vedioinfos.add(new VedioInfoBean(news_info, img_src, video_src, news_date));
                    }
                    //加载成功向UI线程发送消息
                    Message msg = Message.obtain();
                    msg.what = SUCCESS_LOAD;
                    msg.obj = vedioinfos;
                    mhandler.sendMessage(msg);

                } catch (Exception e) {
                    //加载失败向UI线程发送消息
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = FAIL_LOAD;
                    msg.obj = e;
                    mhandler.sendMessage(msg);
                }
            }
        }.start();

    }
}
