package com.jju.yuxin.cinews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.FavorBean;
import com.jju.yuxin.cinews.bean.MessageBean;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.db.DbUtils;
import com.jju.yuxin.cinews.db.Users;
import com.jju.yuxin.cinews.service.JsonUtil;
import com.jju.yuxin.cinews.utils.Ksoap2Util;
import com.jju.yuxin.cinews.utils.LoginPlatformUtil;
import com.jju.yuxin.cinews.utils.MyLogger;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static android.util.Log.e;

public class NewsDetailsActivity extends BaseActivity {

    private static final String TAG = NewsDetailsActivity.class.getSimpleName();
    private Button bt_top_left;
    private Button bt_top_right;
    private TextView news_title;
    private TextView push_date;
    private TextView reader_count;
    private WebView webView;
    List<MessageBean> olist;
    private FavorBean mFavorBean = new FavorBean();

    //是否收藏
    private boolean isFavor = false;

    //加载成功
    private static final int SUCCESS_LOAD = 0;
    //加载失败
    private static final int FAIL_LOAD = 1;

    private static final int REQUEST_CODE = 100;

    //当加载数据完毕需要更新界面数据
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                //视频新闻加载成功
                case SUCCESS_LOAD:

                    break;
                //视频新闻加载失败
                case FAIL_LOAD:

                    break;
                case R.id.text3:
                    String info = (String) msg.obj;
                    olist = JsonUtil.parseJSON_(info);
                    MyLogger.zLog().e(olist + "%%%%%%%%%%%%%%%%%%%%");
                    //在这里进行非空判断
                    if (olist.size() > 0) {
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadDataWithBaseURL(null, olist.get(0).getContent(), "text/html",
                                "UTF-8", null);
                        news_title.setText(olist.get(0).getName());
                        push_date.setText("发布时间:" + olist.get(0).getTime());
                        reader_count.setText("阅读量:" + olist.get(0).getCount());
                    } else {
                        //如果olist为空，则所有控件不显示
                        webView.setVisibility(View.GONE);
                        news_title.setVisibility(View.GONE);
                        push_date.setVisibility(View.GONE);
                        reader_count.setVisibility(View.GONE);
                        Toast.makeText(NewsDetailsActivity.this, "暂无内容！", Toast.LENGTH_SHORT)
                                .show();
                    }
                default:
                    break;
            }

        }
    };
    private NewsBean newsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        //将导航栏的左右按钮设置为可见并为其设置背景资源
        bt_top_left = (Button) findViewById(R.id.bt_top_left);
        bt_top_right = (Button) findViewById(R.id.bt_top_right);
        webView = (WebView) findViewById(R.id.wb_news_content);
        news_title = (TextView) findViewById(R.id.news_title);
        push_date = (TextView) findViewById(R.id.push_date);
        reader_count = (TextView) findViewById(R.id.reader_count);
        bt_top_left.setVisibility(View.VISIBLE);
        bt_top_right.setVisibility(View.VISIBLE);
        bt_top_left.setBackgroundResource(R.drawable.bt_return_selector);
        bt_top_right.setBackgroundResource(R.drawable.shoucang_new_one);

        //新闻标题
        news_title = (TextView) findViewById(R.id.news_title);
        //新闻发布时间
        push_date = (TextView) findViewById(R.id.push_date);
        //新闻阅读次数,或者视频播放次数
        reader_count = (TextView) findViewById(R.id.reader_count);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
        //给左侧按键设置点击事件,点击左侧按键将当前activity销毁
        bt_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavor) {
                    bt_top_right.setBackgroundResource(R.drawable.shoucang_new_one);
                    DbUtils.deleteFavor(newsBean);
                    isFavor = false;
                    Toast.makeText(NewsDetailsActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                } else {
                    String loginUserid = LoginPlatformUtil.getLoginUserid();
                    if (loginUserid!=null){
                        mFavorBean.setUserid(loginUserid);
                        bt_top_right.setBackgroundResource(R.drawable.shoucang_new_two);
                        DbUtils.saveFavor(mFavorBean);
                        isFavor = true;
                        Toast.makeText(NewsDetailsActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(NewsDetailsActivity.this, "请先登录!", Toast.LENGTH_SHORT).show();
                        login_();
                    }
                }

            }
        });
    }
    //接收的list信息详情
    private void getInfo() {
        Intent intent = getIntent();
        //获取传送过来的新闻对象,方便收藏操作存储
        newsBean = intent.getParcelableExtra("news");
        Map<String, Object> oMap = new HashMap<String, Object>();
        //由于图片滑动部分点击进入时传入的是“key”,“id”为空，因此要在这里进行判断
        if (newsBean.getId() == null) {
            oMap.put("id", newsBean.getKey());
            hlog.e("newsBean.getKey()"+newsBean.getKey());
            Ksoap2Util.doBackgroud(mhandler, R.id.text3, "getartile", oMap);
            //有ID的时候，表示是listview条目的点击传过来的
        } else {
            hlog.e("newsBean.getId()"+newsBean.getId());
            oMap.put("id", newsBean.getId());
            Ksoap2Util.doBackgroud(mhandler, R.id.text3, "getartile", oMap);
        }

        //只有登陆了用户才要判断当前内容是否已经被收藏
        String loginUserid = LoginPlatformUtil.getLoginUserid();
        if (loginUserid!=null) {
            //判断是否已经收藏
            if (DbUtils.searchFavor(newsBean).size() > 0) {
                isFavor = true;
                bt_top_right.setBackgroundResource(R.drawable.shoucang_new_two);
            }
            getFavor();
        }
    }
    //获得要存储的新闻信息
    private void getFavor() {
        mFavorBean.setDate(newsBean.getTime());
        mFavorBean.setTitle(newsBean.getName());
        mFavorBean.setSummary(newsBean.getSummary());
        mFavorBean.setNews_id(newsBean.getId());
        mFavorBean.setImg_src(newsBean.getUrl());
        mFavorBean.setKey(newsBean.getKey());
        mFavorBean.setType("new");

    }




    /**
     * 登录
     */
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
            startActivityForResult(new Intent(NewsDetailsActivity.this, LoginActivity.class), REQUEST_CODE);
        }
    }
    //根据登录成功的返回信息更新数据库信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String platform = data.getStringExtra("platform");
            HashMap<String, Object> userinfo = (HashMap<String, Object>) data.getSerializableExtra("userinfo");
            if ("QQ".equals(platform)) {
                //用户头像地址
                String figureurl_qq_2 = (String) userinfo.get("figureurl_qq_2");
                //用户名称
                String nickname = (String) userinfo.get("nickname");

                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                String userId = qq.getDb().getUserId();
                //在第一次登录操作中将用户信息保存到数据库中
                Users user = new Users(userId, nickname, figureurl_qq_2);
                DbUtils.saveUser(user);
            } else if ("SinaWeibo".equals(platform)) {
                //用户头像地址
                String avatar_large = (String) userinfo.get("avatar_large");
                //用户名称
                String screen_name = (String) userinfo.get("screen_name");
                e(TAG, "onActivityResult" + "screen_name:" + screen_name);

                Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                String userId = sinaWeibo.getDb().getUserId();
                //在第一次登录操作中将用户信息保存到数据库中
                Users user = new Users(userId, screen_name, avatar_large);
                DbUtils.saveUser(user);
            }
            e(TAG, "onActivityResult" + "platform:" + platform + "userinfo" + userinfo);
        }
    }
}
