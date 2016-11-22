package com.jju.yuxin.cinews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.adapter.FavorList_Adapter;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.bean.VedioInfoBean;
import com.jju.yuxin.cinews.db.DbUtils;
import com.jju.yuxin.cinews.db.Favors;
import com.jju.yuxin.cinews.db.Users;
import com.jju.yuxin.cinews.utils.LoginPlatformUtil;
import com.jju.yuxin.cinews.utils.ActivityCollector;
import com.jju.yuxin.cinews.utils.LoginPlatformUtil;

import java.util.HashMap;
import java.util.List;

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
 * ClassName FavoriteActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:01.
 * Describe :收藏夹Activity
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */

public class FavoriteActivity extends BaseActivity {

    private Button bt_top_right;
    private Button bt_top_left;
    private ListView mListView;
    private List<Favors> mFavorsList;
    private FavorList_Adapter adapter;
    private boolean isDelete = false; //删除状态
    private TextView tv_favor;
    private Button btn_favor;
    private static final int REQUEST_CODE = 100;
    private static final String TAG = FavoriteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        init();
    }

    public void init() {
        bt_top_right = (Button) findViewById(R.id.bt_top_right);
        bt_top_right.setVisibility(View.VISIBLE);
        bt_top_right.setBackgroundResource(R.drawable.bt_delete_selector);
        bt_top_right.setOnClickListener(mOnClickListener);


        mListView = (ListView) findViewById(R.id.lv_favor);

        tv_favor = (TextView) findViewById(R.id.tv_favor);
        btn_favor = (Button) findViewById(R.id.btn_favor);

        mListView.setOnItemClickListener(mOnItemClickListener);
        String loginUserid = LoginPlatformUtil.getLoginUserid();
        //如果用户已经登录
        if (loginUserid != null) {

            tv_favor.setVisibility(View.GONE);
            btn_favor.setVisibility(View.GONE);

            mFavorsList = DbUtils.searchFavor(loginUserid);
            //用户还没有登陆
        } else {
            mFavorsList = null;
            tv_favor.setVisibility(View.VISIBLE);
            btn_favor.setVisibility(View.VISIBLE);
            btn_favor.setOnClickListener(mOnClickListener);
        }
        if (mFavorsList != null && mFavorsList.size() > 0) {
            adapter = new FavorList_Adapter(FavoriteActivity.this, mFavorsList);
            mListView.setAdapter(adapter);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                //删除按钮
                case R.id.bt_top_right:

                    //判断是否处于删除状态
                    if (isDelete) {
                        updata(false);
                        isDelete = false;
                    } else {
                        updata(true);
                        isDelete = true;
                    }
                    break;
                case R.id.btn_favor:
                    Toast.makeText(FavoriteActivity.this, "登录", Toast.LENGTH_SHORT).show();
                    login_();
                    break;

                default:
                    break;
            }

        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView
            .OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Favors favors = mFavorsList.get(position);
            if (favors.getType().equals("video")) {
                VedioInfoBean vedioInfoBean = new VedioInfoBean();

                vedioInfoBean.setId(Integer.parseInt(favors.getNews_id()));
                vedioInfoBean.setImg_src(favors.getImg_src());
                vedioInfoBean.setVideo_src(favors.getVideo_src());
                vedioInfoBean.setNews_info(favors.getTitle());
                vedioInfoBean.setNews_date(favors.getDate());

                Intent intent = new Intent(FavoriteActivity.this, VedioNewsDetailsActivity.class);
                intent.putExtra("vedio_news", vedioInfoBean);
                startActivity(intent);
            } else if (favors.getType().equals("new")) {
                NewsBean newsBean = new NewsBean();

                newsBean.setId(favors.getNews_id());
                newsBean.setUrl(favors.getImg_src());
                newsBean.setKey(favors.getKey());
                newsBean.setName(favors.getTitle());
                newsBean.setSummary(favors.getSummary());
                newsBean.setTime(favors.getDate());

                Intent intent2 = new Intent(FavoriteActivity.this, NewsDetailsActivity.class);
                intent2.putExtra("news", newsBean);
                startActivity(intent2);
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        isDelete = false;
        updata(isDelete);
    }

    //更新listview
    private void updata(Boolean isDelete) {
        //多次判断是否已经登录,防止从生命周期从onPause->onResume
        String loginUserids = LoginPlatformUtil.getLoginUserid();
        //如果已经登陆了
        if (loginUserids != null) {
            //将提示信息隐藏
            tv_favor.setVisibility(View.GONE);
            btn_favor.setVisibility(View.GONE);
            //从数据库中获取当前用户收藏的内容
            mFavorsList = DbUtils.searchFavor(loginUserids);
            hlog.e(mFavorsList.toString());
        } else {
            //如果没有登录
            mFavorsList = null;

            //如果是登陆了,但是注销了,本次验证为loginUserids=null,adapter!=null
            if (adapter != null) {
                //移除全部已经加载的item
                adapter.removeallItem();
                adapter.notifyDataSetChanged();
                //设置提示信息
                tv_favor.setVisibility(View.VISIBLE);
                btn_favor.setVisibility(View.VISIBLE);
                btn_favor.setOnClickListener(mOnClickListener);
            }

        }
        if (mFavorsList != null && mFavorsList.size() > 0) {
            if (adapter != null) {
                adapter.replaceList(mFavorsList, isDelete);
                adapter.notifyDataSetChanged();
            } else {
                adapter = new FavorList_Adapter(FavoriteActivity.this, mFavorsList);
                mListView.setAdapter(adapter);
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
            startActivityForResult(new Intent(FavoriteActivity.this, LoginActivity.class),
                    REQUEST_CODE);
        }

    }

    //根据登录成功的返回信息更新数据库信息
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


    /**
     * 双击退出
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return MainActivity.doubleExit(FavoriteActivity.this);
        }
        return super.onKeyUp(keyCode, event);
    }
}
