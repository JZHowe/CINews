package com.jju.yuxin.cinews.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName NewsBean
 * Created by yuxin.
 * Created time 17-11-2016 11:24.
 * Describe : 新闻列表的bean
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */

public class NewsBean implements Parcelable {
    private String id;
    private String url;
    private String key;
    private String name;
    private String summary;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "url='" + url + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.url);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.summary);
        dest.writeString(this.time);
    }

    public NewsBean() {
    }

    protected NewsBean(Parcel in) {
        this.id = in.readString();
        this.url = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.summary = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<NewsBean> CREATOR = new Parcelable.Creator<NewsBean>() {
        @Override
        public NewsBean createFromParcel(Parcel source) {
            return new NewsBean(source);
        }

        @Override
        public NewsBean[] newArray(int size) {
            return new NewsBean[size];
        }
    };
}
