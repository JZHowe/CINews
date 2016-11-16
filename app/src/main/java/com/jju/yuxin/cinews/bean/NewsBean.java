package com.jju.yuxin.cinews.bean;

/**
 * Created by Administrator on 2016/11/15.
 * hahha
 */

public class NewsBean {
    private String url;
    private String key;
    private String name;
    private String summary;
    private String time;

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
}
