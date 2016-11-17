package com.jju.yuxin.cinews.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jju.yuxin.cinews.bean.MessageBean;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.utils.MyLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */

public class JsonUtil {

    public static List<NewsBean> parseJSON(String info) {
        List<NewsBean> olist = new ArrayList<>();
        try {
            Gson gson = new Gson();
            olist = gson.fromJson(info, new TypeToken<List<NewsBean>>() {
            }.getType());

        } catch (Exception e) {
            return null;
        }

        return olist;
    }

    public static List<MessageBean> parseJSON_(String info) {
        List<MessageBean> olist = new ArrayList<>();
        try {
            Gson gson = new Gson();
            olist = gson.fromJson(info, new TypeToken<List<MessageBean>>() {
            }.getType());

        } catch (Exception e) {
            return null;
        }

        return olist;
    }



}
