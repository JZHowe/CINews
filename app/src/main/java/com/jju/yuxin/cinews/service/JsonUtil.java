package com.jju.yuxin.cinews.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jju.yuxin.cinews.bean.NewsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */

public class JsonUtil {

    public static List<NewsBean> parseJSON(String info){
        Gson gson = new Gson();
        List<NewsBean> olist = gson.fromJson(info,new TypeToken<List<NewsBean>>(){}.getType());

        return olist;
    }

}
