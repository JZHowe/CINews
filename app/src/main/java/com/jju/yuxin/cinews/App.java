package com.jju.yuxin.cinews;


import com.jju.yuxin.cinews.utils.CauchExceptionHandler;

import org.litepal.LitePalApplication;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2016/11/14.
 */

public class App extends LitePalApplication{


    @Override
    public void onCreate() {
        super.onCreate();


        //SharedSDK的初始化
        ShareSDK.initSDK(this);

//        //应用开始就设置全局捕获异常器没有设置就会用系统默认的
//        CauchExceptionHandler crashHandler = CauchExceptionHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }
}
