package com.jju.yuxin.cinews.broadcast;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.broadcast
 * Created by yuxin.
 * Created time 2016/11/22 0022 下午 5:05.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.activity.LoadingActivity;
import com.jju.yuxin.cinews.activity.NewsDetailsActivity;
import com.jju.yuxin.cinews.bean.NewsBean;

import cn.jpush.android.api.JPushInterface;

import static android.util.Log.e;

public class MyJPushReceiver extends BroadcastReceiver {
    private static String TAG =MyJPushReceiver.class.getSimpleName();
    private static final int NOTIFICATION_FLAG = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        //获取NotificationManager
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        e(TAG, "onReceive - " + intent.getAction());
        Map<String, Object> map = new HashMap<String, Object>();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {


        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            // 自定义消息不会展示在通知栏，完全由开发者写代码去处理
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

            //解析json
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(extra);
                try {
                    //如果是图文新闻
                String id = jsonObject.getString("id");
                map.put("id",id);
                } catch (Exception e) {
                    //如果是图片新闻
                    String key = jsonObject.getString("key");
                    map.put("key",key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("content", content);
            //获取接收到推送时的系统时间
            Calendar rightNow = Calendar.getInstance();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String date = fmt.format(rightNow.getTime());
            map.put("date", date);



            e(TAG, "onReceive" + map);
            NewsBean newsBean=new NewsBean();
            //如果是文字新闻
            newsBean.setId((String) map.get("id"));
            //如果是图片新闻
            newsBean.setKey((String) map.get("key"));
            e(TAG, "onReceive" + newsBean.toString());



            //设置intent跳转
            Intent new_intent = new Intent(context, NewsDetailsActivity.class);
            new_intent.putExtra("news",newsBean);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new_intent, 0);
            // 通过Notification.Builder来创建通知，注意API Level
            // API16之后才支持

            String title="";
            String message="";
            //切割消息
            if (content.contains("/")){
                String[] split = content.split("/");
                title=split[0];
                message=split[1];
            }else{
                title=content;
                message=content;
            }

            //split[0]为消息标题
            //split[1]为消息内容

            Bitmap largeicon= BitmapFactory.decodeResource(context.getResources(),R.drawable.largeicon);

            Notification notify = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.smallicon)
                    .setTicker(message+"")
                    .setContentTitle(title+"")
                    .setContentText(message+"")
                    .setLargeIcon(largeicon)
                    .setContentIntent(pendingIntent).setNumber(1).build(); // 需要注意build()是在API
            // level16及之后增加的，API11可以使用getNotificatin()来替代
            notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
            manager.notify(NOTIFICATION_FLAG, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示

            //**************解析推送过来的json数据并存放到集合中 end******************/
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            e(TAG, "onReceive----------" + "收到了通知");
            // 在这里可以做些统计，或者做些其他工作

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            //在这里执行的不是自定义推送
            e(TAG, "onReceive" + "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, LoadingActivity.class); // 自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
