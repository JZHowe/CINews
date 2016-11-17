package com.jju.yuxin.cinews.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.bean
 * Created by yuxin.
 * Created time 2016/11/17 0017 上午 8:42.
 * Version   1.0;
 * Describe : 新闻详情的Bean
 * History:
 * ==============================================================================
 */

public class MessageBean implements Parcelable {
    private String name;
    private String time;
    private String Source;
    private String Content;
    private String Count;

    @Override
    public String toString() {
        return "MessageBean{" +
                "Content='" + Content + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", Source='" + Source + '\'' +
                ", Count='" + Count + '\'' +
                '}';
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.time);
        dest.writeString(this.Source);
        dest.writeString(this.Content);
        dest.writeString(this.Count);
    }

    public MessageBean() {
    }

    protected MessageBean(Parcel in) {
        this.name = in.readString();
        this.time = in.readString();
        this.Source = in.readString();
        this.Content = in.readString();
        this.Count = in.readString();
    }

    public static final Parcelable.Creator<MessageBean> CREATOR = new Parcelable.Creator<MessageBean>() {
        @Override
        public MessageBean createFromParcel(Parcel source) {
            return new MessageBean(source);
        }

        @Override
        public MessageBean[] newArray(int size) {
            return new MessageBean[size];
        }
    };
}
