package com.jju.yuxin.cinews.bean;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.bean
 * Created by yuxin.
 * Created time 2016/11/15 0015 上午 10:33.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class VedioInfoBean {

    private int id;
    private String img_src;
    private String video_src;
    private String news_info;
    private String news_date;


    public VedioInfoBean() {
    }

    public VedioInfoBean( String news_info, String img_src, String video_src, String news_date) {
        this.news_info = news_info;
        this.img_src = img_src;
        this.video_src = video_src;
        this.news_date = news_date;
    }
    public VedioInfoBean(int id, String news_info, String img_src, String video_src, String news_date) {
        this.id = id;
        this.news_info = news_info;
        this.img_src = img_src;
        this.video_src = video_src;
        this.news_date = news_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNews_info() {
        return news_info;
    }

    public void setNews_info(String news_info) {
        this.news_info = news_info;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getVideo_src() {
        return video_src;
    }

    public void setVideo_src(String video_src) {
        this.video_src = video_src;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    @Override
    public String toString() {
        return "VedioInfoBean{" +
                "id=" + id +
                ", img_src='" + img_src + '\'' +
                ", video_src='" + video_src + '\'' +
                ", news_info='" + news_info + '\'' +
                ", news_date='" + news_date + '\'' +
                '}';
    }
}
