package com.jju.yuxin.cinews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity implements PlatformActionListener, Handler.Callback {

    private ImageView iv_login_qq;
    private ImageView iv_login_wechat;
    private ImageView iv_login_weinbo;


    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private ImageView iv_login_out;

    private Handler handler;
    private Button bt_top_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler = new Handler(this);

        LoginClickListener listener = new LoginClickListener();

        iv_login_qq = (ImageView) findViewById(R.id.iv_login_qq);
        iv_login_wechat = (ImageView) findViewById(R.id.iv_login_wechat);
        iv_login_weinbo = (ImageView) findViewById(R.id.iv_login_weinbo);
        bt_top_left = (Button) findViewById(R.id.bt_top_left);
        bt_top_left.setVisibility(View.VISIBLE);
        bt_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        });

        iv_login_qq.setOnClickListener(listener);
        iv_login_wechat.setOnClickListener(listener);
        iv_login_weinbo.setOnClickListener(listener);
    }

    //登录按钮
    private class LoginClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //QQ登录
                case R.id.iv_login_qq:
                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
                    authorize(qq);
                    break;
                //微信登录
                case R.id.iv_login_wechat:
//                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//                    authorize(wechat);
                    Toast.makeText(LoginActivity.this, "微信登录暂未开通...", Toast.LENGTH_SHORT).show();
                    break;
                //微博登录
                case R.id.iv_login_weinbo:
                    Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                    authorize(sina);
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 用户授权操作
     *
     * @param plat
     */
    private void authorize(Platform plat) {
        //如果传入的平台为空,提示暂未支持登录
        if (plat == null) {
            Toast.makeText(this, "暂不支持其他第三方登录!", Toast.LENGTH_SHORT).show();
            return;
        }

        //因为在进入的时候已经验证是否已经登录,所以这段被废弃
//        //判断指定平台是否已经完成授权
//        if (plat.isAuthValid()) {
//            //获取已经登陆的用户的id
//            String userId = plat.getDb().getUserId();
//            if (userId != null) {
//                //发送消息
//                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
//                //执行登录操作
//                login(plat.getName(), userId, null);
//                return;
//            }
//        }
        //设置平台登录监听
        plat.setPlatformActionListener(this);
        // true不使用SSO授权，false使用SSO授权,优先调用客户端登录授权
        plat.SSOSetting(false);
        //获取用户资料
        plat.showUser(null);

    }

//    /**
//     * 需要执行的登录操作,如保存用户id,头像,获取相关信息等
//     *
//     * @param plat
//     * @param userId
//     * @param userInfo
//     */
//    private void login(String plat, String userId, HashMap<String, Object> userInfo) {
//
//        hlog.e("login");
//        hlog.e("----------plat" + plat + "----------userId" + userId + "----------userInfo" + userInfo);
//        Message msg = new Message();
//        msg.what = MSG_LOGIN;
//        msg.obj = plat;
//        UIHandler.sendMessage(msg, this);
//    }



    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {

        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[]{platform.getName(), hashMap};
            hlog.e("------------"+hashMap);

            //将信息存储在平台对象中
            if ("QQ".equals(platform.getName())){
                //QQ登录返回的内容
                platform.getDb().put("nickname", (String) hashMap.get("nickname"));
                platform.getDb().put("figureurl_qq_2", (String) hashMap.get("figureurl_qq_2"));
                hlog.e((String) hashMap.get("figureurl_qq_2"));

            }else if ("SinaWeibo".equals(platform.getName())){
                //新浪微博登录
                platform.getDb().put("screen_name", (String) hashMap.get("screen_name"));
                platform.getDb().put("avatar_large", (String) hashMap.get("avatar_large"));

                //微信登录
            }else if ("Wechat".equals(platform.getName())){


            }

            handler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {

        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {

        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {

            //取消授权
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
//			System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            //授权失败
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
//			System.out.println("-------MSG_AUTH_ERROR--------");
            }
            break;
            //授权成功
            case MSG_AUTH_COMPLETE: {
                //授权成功
              //  Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
                Object[] objs = (Object[]) msg.obj;
                String platform = (String) objs[0];
                HashMap<String, Object> userinfo = (HashMap<String, Object>) objs[1];

                Intent intent = new Intent();
                intent.putExtra("platform", platform);
                intent.putExtra("userinfo", userinfo);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
        return false;

    }
}
