package com.jju.yuxin.cinews.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName NetUtils
 * Created by yuxin.
 * Created time 2016/7/19 0019.
 * Describe : 网络连接检测工具类
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class NetUtils {
        public static int NO_CONNECTED=0;  //没有网络连接
        public static int  MOBILE_CONNECTED=1;  //当前是手机数据连接
        public static int WIFI_CONNECTED=2;   //当前是WIFI连接
    /**
     * 检查联网状态，如果返回是0，则没有网络连接，返回时1，表示当前是手机数据连接，返回2，表示当前是WIFI连接
     * @param context
     * @return
     */
    public static int isConnected(Context context){
        //获取连接管理器
        ConnectivityManager cm=  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取联网信息封装类
        NetworkInfo activeNetworkInfo =cm.getActiveNetworkInfo();
        //判断联网状态
        if (activeNetworkInfo==null){
            return NO_CONNECTED;
        }else if (activeNetworkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
            return MOBILE_CONNECTED;
        }else if (activeNetworkInfo.getType()==ConnectivityManager.TYPE_WIFI){
            return WIFI_CONNECTED;
        }else{
            return NO_CONNECTED;
        }

    }
}

