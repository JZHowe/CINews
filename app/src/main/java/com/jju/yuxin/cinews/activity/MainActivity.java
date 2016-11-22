package com.jju.yuxin.cinews.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.utils.MyLogger;
import com.jju.yuxin.cinews.views.CircleImageView;
import com.jju.yuxin.cinews.volleyutils.ImageCacheManager;

import java.io.Serializable;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static android.util.Log.e;

/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName MainActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:01.
 * Describe : 主Activity
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */

public class MainActivity extends TabActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE = 100;
    private TabHost tabHost;
    private CircleImageView iv_user_head;
    private TextView tv_user_name;
    private ListView lv_sliding;
    private Button bt_login_out;

    Platform plat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainClickListener listener = new MainClickListener();

        //用户头像
        iv_user_head = (CircleImageView) findViewById(R.id.iv_user_head);

        //用户昵称
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);

        //取消授权
        bt_login_out = (Button) findViewById(R.id.bt_login_out);

        //判断是否已经登录
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);

        //如果QQ已经授权了
        if (qq.isAuthValid()) {
            plat = qq;
            String nickname = plat.getDb().get("nickname");
            String figureurl_qq_2 = plat.getDb().get("figureurl_qq_2");
            loaduserinfo(figureurl_qq_2, nickname);
            //如果微信已经授权了
        } else if (wechat.isAuthValid()) {
            plat = wechat;

            //如果新浪微博已经授权了
        } else if (sinaWeibo.isAuthValid()) {
            plat = sinaWeibo;
            //用户头像地址
            String profile_image_url = plat.getDb().get("avatar_large");
            //用户名称
            String screen_name = plat.getDb().get("screen_name");

            loaduserinfo(profile_image_url, screen_name);
        }


        iv_user_head.setOnClickListener(listener);

        tv_user_name.setOnClickListener(listener);

        bt_login_out.setOnClickListener(listener);

        //功能List
        lv_sliding = (ListView) findViewById(R.id.lv_sliding);
        lv_sliding.setOnItemClickListener(mOnItemClickListener);


        //获取当前TabActivity的tabhost
        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //声明四个Intent的跳转
        //新闻
        intent = new Intent(MainActivity.this, NewsActivity.class);
        spec = tabHost.newTabSpec("news").setIndicator("news").setContent(intent);
        tabHost.addTab(spec);

        //图片
        intent = new Intent(MainActivity.this, PictureActivity.class);
        spec = tabHost.newTabSpec("picture").setIndicator("picture").setContent(intent);
        tabHost.addTab(spec);

        //视频
        intent = new Intent(MainActivity.this, VedioActivity.class);
        spec = tabHost.newTabSpec("vedio").setIndicator("vedio").setContent(intent);
        tabHost.addTab(spec);

        //收藏夹
        intent = new Intent(MainActivity.this, FavoriteActivity.class);
        spec = tabHost.newTabSpec("favorite").setIndicator("favorite").setContent(intent);
        tabHost.addTab(spec);


        //设置tabhost的初始位置
        tabHost.setCurrentTab(0);

        //主页的底栏用RadioGroup实现
        RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.main_tab_group);
        //给radiogroup设置点击事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //切换到新闻模块
                    case R.id.main_tab_news:
                        tabHost.setCurrentTabByTag("news");
                        break;
                    //切换到图片模块
                    case R.id.main_tab_picture:
                        tabHost.setCurrentTabByTag("picture");
                        break;
                    //切换到视频模块
                    case R.id.main_tab_vedio://视频
                        tabHost.setCurrentTabByTag("vedio");
                        break;
                    //切换到收藏夹模块
                    case R.id.main_tab_favorite://收藏夹
                        tabHost.setCurrentTabByTag("favorite");
                        break;
                    //默认为新闻模块
                    default:
                        tabHost.setCurrentTabByTag("news");
                        break;
                }
            }
        });

    }


    /**
     * listview item 点击事件
     */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView
            .OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                //离线视频
                case 0:

                    break;
                //夜间模式
                case 1:

                    break;
                //关于
                case 2:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 点击事件
     */
    private class MainClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_user_head:
                    login_();
                    break;
                case R.id.bt_login_out:
                    login_out();

                    break;
                default:
                    break;
            }


        }
    }

    private void login_() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        //只要其中一种授权方式已经授权了
        if (qq.isAuthValid() || wechat.isAuthValid() || sinaWeibo.isAuthValid()) {

            e(TAG, "login_" + "你已经登陆了");
        } else {
            //做登录操作
            //startActivity(new Intent(MainActivity.this,LoginActivity.class));
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),
                    REQUEST_CODE);
        }

    }


    /**
     * 用户已经登录,执行取消授权操作
     */
    private void login_out() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        if (qq.isAuthValid()) {

            MyLogger.hLog().e("已经取消QQ的授权!");
            qq.removeAccount(true);
        }
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (wechat.isAuthValid()) {
            MyLogger.hLog().e("已经取消微信的授权!");
            wechat.removeAccount(true);
        }
        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        if (sinaWeibo.isAuthValid()) {
            MyLogger.hLog().e("已经取消新浪微博的授权!");
            sinaWeibo.removeAccount(true);
        }
        tv_user_name.setText(R.string.denglu);
        iv_user_head.setImageResource(R.mipmap.ic_launcher);

        Toast.makeText(this, "退出成功!", Toast.LENGTH_SHORT).show();
    }


    //根据登录成功的返回信息设置控件信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String platform = data.getStringExtra("platform");
            HashMap<String, Object> userinfo = (HashMap<String, Object>) data
                    .getSerializableExtra("userinfo");
            if ("QQ".equals(platform)) {
                //用户头像地址
                String figureurl_qq_2 = (String) userinfo.get("figureurl_qq_2");
                //用户名称
                String nickname = (String) userinfo.get("nickname");
                //加载用户信息
                loaduserinfo(figureurl_qq_2, nickname);
            } else if ("SinaWeibo".equals(platform)) {
                //用户头像地址
                String avatar_large = (String) userinfo.get("avatar_large");
                //用户名称
                String screen_name = (String) userinfo.get("screen_name");
                e(TAG, "onActivityResult" + "screen_name:" + screen_name);

                loaduserinfo(avatar_large, screen_name);
            }

            e(TAG, "onActivityResult" + "platform:" + platform + "userinfo" + userinfo);

        }
    }

    private void loaduserinfo(String imageuel, String username) {
        ImageCacheManager.loadImage(this, imageuel, iv_user_head, R
                .drawable.defaut_pic, R.drawable.fail_pic, 0, 0, ImageView.ScaleType.CENTER_CROP);
        tv_user_name.setText(username + "");
    }
}
