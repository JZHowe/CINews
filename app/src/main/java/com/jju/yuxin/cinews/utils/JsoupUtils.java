package com.jju.yuxin.cinews.utils;

import android.os.Handler;
import android.os.Message;

import com.jju.yuxin.cinews.bean.VedioInfoBean;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.utils
 * Created by yuxin.
 * Created time 2016/11/16 0016 上午 9:49.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class JsoupUtils {

    //加载成功
    private static final int SUCCESS_LOAD = 0;
    //加载失败
    private static final int FAIL_LOAD = 1;

    public  static  void getNewPaper(final String path, final Handler mhandler){
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
                    //遍历出新闻
                    for (Element element : news_items) {
                        String img_src = element.select("div.pic").first().getElementsByTag("img").first().attr("src");
                        Element info_ele = element.select("div.info").first();
                        String video_src = info_ele.getElementsByTag("a").first().attr("href");
                        String news_info = info_ele.getElementsByTag("a").text();
                        if (news_info.contains("]")){
                            String[] split = news_info.split("]");
                            news_info=split[1];
                        }
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

    public static void getNewsDetails(String video_src, Handler mhandler) {

    }
}
