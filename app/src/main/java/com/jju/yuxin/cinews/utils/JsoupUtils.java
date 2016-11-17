package com.jju.yuxin.cinews.utils;

import android.os.Handler;
import android.os.Message;

import com.jju.yuxin.cinews.bean.VedioInfoBean;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

    //加载成功
    private static final int SUCCESS_LOAD_DETAIL = 2;

    /**
     * 获取视频新闻对象,显示在ListView中
     * @param path
     * @param mhandler
     */
    public static void getNewPaper(final String path, final Handler mhandler) {
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
                        if (news_info.contains("]")) {
                            String[] split = news_info.split("]");
                            news_info = split[1];
                        }
                        //将新闻标题作文新闻的唯一标示,将新闻标题生成的hashcode作为新闻的id
                        int newsid = news_info.hashCode();
                        String news_date = info_ele.select("div.date").first().text();
                        vedioinfos.add(new VedioInfoBean(newsid, news_info, img_src, video_src, news_date));
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

    /**
     * 获取视频新闻详细内容 显示在详情中
     * @param vedioInfoBean
     * @param mhandler
     */
    public static void getNewsDetails(final VedioInfoBean vedioInfoBean, final Handler mhandler) {
        //开启一个线程加载视频信息
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //获取连接
                    Connection connect = Jsoup.connect(vedioInfoBean.getVideo_src());
                    MyLogger.hLog().e("connect:" + vedioInfoBean.getVideo_src());
                    //设置超时时间
                    connect.timeout(10000);
                    //获取整张网页
                    Document document = connect.get();

                    Element select = document.select("[id=cjxtv-html5-player-video]").first();
                    //视频源
                    String play_src = select.attr("src");
                    //发布时间
                    String push_date = "发布时间:" + document.select(".ui-article-infos").first().text();
                    //新闻标题
                    String news_title = document.select(".ui-article-title").first().text();
                    //播放次数
                    String play_count = document.select(".ui-article-info").first().text();
                    if (play_count.contains("播放次数")) {
                        String[] play_counts = play_count.split("播放次数");
                        play_count = "播放次数" + play_counts[1];
                    }
                    vedioInfoBean.setPlay_src(play_src);
                    vedioInfoBean.setPush_date(push_date);
                    vedioInfoBean.setNews_title(news_title);
                    vedioInfoBean.setPlay_count(play_count);
                    MyLogger.hLog().e("play_src:" + play_src + "push_date:" + push_date + "news_title:" + news_title + "play_count" + play_count);
                    //成功获取到视频新闻详情需要显示的内容
                    Message msg=Message.obtain();
                    msg.what=SUCCESS_LOAD_DETAIL;
                    msg.obj=vedioInfoBean;
                    mhandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();

                    //加载失败返回一个失败的标志
                    Message msg=Message.obtain();
                    msg.what=FAIL_LOAD;
                    mhandler.sendMessage(msg);
                }
            }
        }.start();

    }
}
