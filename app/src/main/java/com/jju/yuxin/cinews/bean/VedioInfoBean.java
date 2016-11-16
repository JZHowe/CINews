package com.jju.yuxin.cinews.bean;

import android.os.Parcel;
import android.os.Parcelable;

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

public class VedioInfoBean implements Parcelable {

    //加载列表部分加载的信息
    private int id;        //视频新闻id
    private String img_src;   //视频新闻摘要图片
    private String video_src;   //视频所在网页链接
    private String news_info;    //视频新闻信息
    private String news_date;     //日期

    //进入详情页加载的信息

    private String news_title;
    private String push_date;
    private String play_count;
    private String play_src;



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
    public VedioInfoBean(String news_title,int id, String push_date, String play_count, String play_src) {
        this.id = id;
        this.news_title = news_title;
        this.push_date = push_date;
        this.play_count = play_count;
        this.play_src = play_src;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getPlay_src() {
        return play_src;
    }

    public void setPlay_src(String play_src) {
        this.play_src = play_src;
    }

    public String getPlay_count() {
        return play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public String getPush_date() {
        return push_date;
    }

    public void setPush_date(String push_date) {
        this.push_date = push_date;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.img_src);
        dest.writeString(this.video_src);
        dest.writeString(this.news_info);
        dest.writeString(this.news_date);
        dest.writeString(this.news_title);
        dest.writeString(this.push_date);
        dest.writeString(this.play_count);
        dest.writeString(this.play_src);
    }

    protected VedioInfoBean(Parcel in) {
        this.id = in.readInt();
        this.img_src = in.readString();
        this.video_src = in.readString();
        this.news_info = in.readString();
        this.news_date = in.readString();
        this.news_title = in.readString();
        this.push_date = in.readString();
        this.play_count = in.readString();
        this.play_src = in.readString();
    }

    public static final Parcelable.Creator<VedioInfoBean> CREATOR = new Parcelable.Creator<VedioInfoBean>() {
        @Override
        public VedioInfoBean createFromParcel(Parcel source) {
            return new VedioInfoBean(source);
        }

        @Override
        public VedioInfoBean[] newArray(int size) {
            return new VedioInfoBean[size];
        }
    };

    @Override
    public String toString() {
        return "VedioInfoBean{" +
                "id=" + id +
                ", img_src='" + img_src + '\'' +
                ", video_src='" + video_src + '\'' +
                ", news_info='" + news_info + '\'' +
                ", news_date='" + news_date + '\'' +
                ", news_title='" + news_title + '\'' +
                ", push_date='" + push_date + '\'' +
                ", play_count='" + play_count + '\'' +
                ", play_src='" + play_src + '\'' +
                '}';
    }
}
