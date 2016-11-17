package com.jju.yuxin.cinews.bean;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.bean
 * Created by yuxin.
 * Created time 2016/11/17 0017 上午 8:42.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class MessageBean {
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
}
