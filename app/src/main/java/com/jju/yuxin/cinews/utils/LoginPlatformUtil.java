package com.jju.yuxin.cinews.utils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.utils
 * Created by yuxin.
 * Created time 2016/11/22 0022 上午 9:07.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class LoginPlatformUtil {

    public static Platform getLoginPlatform(){
        Platform plat=null;
        //判断是否已经登录
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);

        //如果QQ已经授权了
        if (qq.isAuthValid()) {
            plat = qq;

            //如果微信已经授权了
        } else if (wechat.isAuthValid()) {
            plat = wechat;

            //如果新浪微博已经授权了
        } else if (sinaWeibo.isAuthValid()) {
            plat = sinaWeibo;
        }
        return plat;
    }


    public static String getLoginUserid(){
        String userid=null;
        Platform loginPlatform = getLoginPlatform();
        if (loginPlatform!=null){
            userid= loginPlatform.getDb().getUserId();
        }
        return userid;
    }





}
