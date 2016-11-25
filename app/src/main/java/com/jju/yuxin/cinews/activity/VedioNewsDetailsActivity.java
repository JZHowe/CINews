package com.jju.yuxin.cinews.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.FavorBean;
import com.jju.yuxin.cinews.adapter.VedioList_Adapter;
import com.jju.yuxin.cinews.bean.VedioInfoBean;
import com.jju.yuxin.cinews.db.DbUtils;
import com.jju.yuxin.cinews.db.Users;
import com.jju.yuxin.cinews.utils.JsoupUtils;
import com.jju.yuxin.cinews.utils.LoginPlatformUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE;
import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;
import static android.util.Log.e;


public class VedioNewsDetailsActivity extends BaseActivity {

    private static final String TAG = VedioNewsDetailsActivity.class.getSimpleName();

    private Button bt_top_left;
    private Button bt_top_right;
    private VedioInfoBean vedioInfoBean;
    private FavorBean mFavorBean = new FavorBean();
    //是否收藏
    private boolean isFavor = false;
    //加载完成的新闻集合
    private List<VedioInfoBean> vedioinfos;


    //视频新闻爬取地址
    private static final String path = "http://www.jxntv.cn/";

    //加载成功
    private static final int SUCCESS_LOAD = 0;
    //加载失败
    private static final int FAIL_LOAD = 1;
    //加载失败
    private static final int SUCCESS_LOAD_DETAIL = 2;

    private static final int REQUEST_CODE = 100;

    //当加载数据完毕需要更新界面数据
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //加载成功
                case SUCCESS_LOAD:
                    vedioinfos = (List<VedioInfoBean>) msg.obj;
                    //将内容填充到ListView
                    lv_more_vedio.setAdapter(new VedioList_Adapter(VedioNewsDetailsActivity.this, vedioinfos, volleyUtils));
                    break;
                //视频新闻加载成功
                case SUCCESS_LOAD_DETAIL:
                    vedioInfoBean = (VedioInfoBean) msg.obj;
                    //设置新闻内容
                    news_title.setText(vedioInfoBean.getNews_title());
                    push_date.setText(vedioInfoBean.getPush_date());
                    reader_count.setText(vedioInfoBean.getPlay_count());
                    playVedio(vedioInfoBean.getPlay_src());
                    break;
                //视频新闻加载失败
                case FAIL_LOAD:

                    break;
                default:
                    break;
            }

        }
    };


    private TextView news_title;
    private TextView push_date;
    private TextView reader_count;
    private VideoView vedio_paly;
    private ImageView iv_shard;
    private int per_position = 0;
    private ListView lv_more_vedio;
    private ImageView iv_download;
    private DownloadManager downManager;
    private RelativeLayout rl_vv;
    private LandScapeVedio landScapeVedio;
    private getPositionBroadcast positionBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        e(TAG, "onCreate" + "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_news_details);
        //将导航栏的左右按钮设置为可见并为其设置背景资源
        bt_top_left = (Button) findViewById(R.id.bt_top_left);
        bt_top_right = (Button) findViewById(R.id.bt_top_right);
        bt_top_left.setVisibility(View.VISIBLE);
        bt_top_right.setVisibility(View.VISIBLE);
        bt_top_left.setBackgroundResource(R.drawable.bt_return_selector);
        bt_top_right.setBackgroundResource(R.drawable.shoucang_new_one);

        SharedClickListener sharedClickListener = new SharedClickListener();


        downManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        //分享按钮
        iv_shard = (ImageView) findViewById(R.id.iv_shard);
        //点击事件
        iv_shard.setOnClickListener(sharedClickListener);


        //新闻标题
        news_title = (TextView) findViewById(R.id.news_title);
        //新闻发布时间
        push_date = (TextView) findViewById(R.id.push_date);
        //新闻阅读次数,或者视频播放次数
        reader_count = (TextView) findViewById(R.id.reader_count);
        //视屏播放器
        vedio_paly = (VideoView) findViewById(R.id.vedio_paly);

        //视屏播放器上面嵌套的布局
        rl_vv = (RelativeLayout) findViewById(R.id.rl_vv);

        //更多视频
        lv_more_vedio = (ListView) findViewById(R.id.lv_more_vedio);

        iv_download = (ImageView) findViewById(R.id.iv_download);

        //注册一个广播用来接收播放位置
        positionBroadcast = new getPositionBroadcast();
        IntentFilter filter=new IntentFilter();
        filter.addAction("sendposition");
        registerReceiver(positionBroadcast, filter);

        //下载视频到本地
        iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadVedio(vedioInfoBean);

            }
        });

        JsoupUtils.getNewPaper(path, mhandler);

        lv_more_vedio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前点击的item的对象
                VedioInfoBean vedioInfoBean = vedioinfos.get(position);
                //跳转至新闻详情页面
                Intent intent = new Intent(VedioNewsDetailsActivity.this, VedioNewsDetailsActivity.class);
                intent.putExtra("vedio_news", vedioInfoBean);
                startActivity(intent);
                finish();
            }
        });


    }

    /**
     * 验证操作放置在onResume中放置从onpause跳转
     */
    @Override
    protected void onResume() {
        e(TAG, "onResume" + "");
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
                    DbUtils.deleteVideoFavor(vedioInfoBean);
                    isFavor = false;
                    Toast.makeText(VedioNewsDetailsActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                } else {
                    String loginUserid = LoginPlatformUtil.getLoginUserid();
                    if (loginUserid != null) {
                        mFavorBean.setUserid(loginUserid);
                        bt_top_right.setBackgroundResource(R.drawable.shoucang_new_two);
                        DbUtils.saveFavor(mFavorBean);
                        isFavor = true;
                        Toast.makeText(VedioNewsDetailsActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VedioNewsDetailsActivity.this, "请先登录!", Toast.LENGTH_SHORT).show();
                        login_();
                    }
                }
            }
        });

        //根据当前进度是否为零判断之前是否播放过
        if (per_position == 0) {
            //如果没有播放过那么加载视频资源
            if (vedioInfoBean != null) {
                //获取视频详情
                JsoupUtils.getNewsDetails(vedioInfoBean, mhandler);
            }
            //如果已经播放过那么跳转到上次的播放进度之后
        } else {
            if (!vedio_paly.isPlaying()){
                vedio_paly.seekTo(per_position);
                vedio_paly.start();
            }
        }

        //给最外层设置长按事件
        rl_vv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentPosition = vedio_paly.getCurrentPosition();
                Intent fullintent = new Intent(VedioNewsDetailsActivity.this, LandScapeVedio.class);
                fullintent.putExtra("currentPosition", currentPosition);
                fullintent.putExtra("play_src", vedioInfoBean.getPlay_src());
                startActivity(fullintent);
                return false;
            }
        });
    }




    //接收list的信息
    private void getInfo() {
        //传过来的视频新闻对象
        vedioInfoBean = getIntent().getParcelableExtra("vedio_news");
        getFavor();
        //只有登陆了用户才要判断当前内容是否已经被收藏
        String loginUserid = LoginPlatformUtil.getLoginUserid();
        if (loginUserid != null) {
            //判断是否已经收藏
            if (DbUtils.searchVideoFavor(vedioInfoBean).size() > 0) {
                isFavor = true;
                bt_top_right.setBackgroundResource(R.drawable.shoucang_new_two);
            }
        }
    }

    //获得要存储的新闻信息
    private void getFavor() {
        mFavorBean.setDate(vedioInfoBean.getNews_date());
        mFavorBean.setTitle(vedioInfoBean.getNews_info());
        mFavorBean.setNews_id(String.valueOf(vedioInfoBean.getId()));
        mFavorBean.setImg_src(vedioInfoBean.getImg_src());
        mFavorBean.setVideo_src(vedioInfoBean.getVideo_src());
        mFavorBean.setType("video");
    }

    /**
     * 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();


        e(TAG, "onPause" + "");
        //当切换到分享界面，需要暂停当前的的视频播放
        //记录下当前播放的进度
        if (vedio_paly.isPlaying()) {
            vedio_paly.pause();
            per_position = vedio_paly.getCurrentPosition();
        }
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vedio_paly != null) {
            vedio_paly.pause();
            vedio_paly.stopPlayback(); //将VideoView所占用的资源释放掉
        }

        unregisterReceiver(positionBroadcast);

        e(TAG, "onDestroy" + "");
    }

    /**
     * 分享方法的实现，需要注意的是，对应每一个平台，他能分享的信息都不尽相同
     * 可以参考  http://wiki.mob.com/%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E/
     */
    private void ShareMessage() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法

        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 播放视频的操作
     *
     * @param play_src
     */
    private void playVedio(String play_src) {


        Uri uri = Uri.parse(play_src);


        //设置视频控制器
        vedio_paly.setMediaController(new MediaController(this));

        //播放完成回调
        vedio_paly.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //播放错误监听
        vedio_paly.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                hlog.e("what" + what + "extra" + extra);
                Toast.makeText(VedioNewsDetailsActivity.this, "视频好像出现了一点问题!", Toast.LENGTH_SHORT).show();
                vedio_paly.seekTo(0);
                return true;
            }
        });

        //设置视频路径
        vedio_paly.setVideoURI(uri);

        //准备完成
        vedio_paly.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //开始播放视频
                vedio_paly.start();
            }
        });

    }

    //分享按钮点击事件
    private class SharedClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //分享按钮
                case R.id.iv_shard:
                    ShareMessage();
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 播放器播放完成监听
     */
    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VedioNewsDetailsActivity.this, "播放完成.", Toast.LENGTH_SHORT).show();
            vedio_paly.seekTo(0);
            vedio_paly.start();
        }
    }

    /**
     * 分享方法的实现，需要注意的是，对应每一个平台，他能分享的信息都不尽相同
     * 可以参考  http://wiki.mob.com/%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E/
     */
    private class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {

        @Override
        public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

            hlog.e(platform.getName());
            //微信分享
            if (Wechat.NAME.equals(platform.getName())) {

                // text是分享文本，所有平台都需要这个字段
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                paramsToShare.setImageUrl(vedioInfoBean.getImg_src() + "");
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                paramsToShare.setUrl(vedioInfoBean.getPlay_src() + "");

                //QQ分享
            } else if (QQ.NAME.equals(platform.getName())) {
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                paramsToShare.setTitle(vedioInfoBean.getNews_title());
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                paramsToShare.setTitleUrl(vedioInfoBean.getPlay_src() + "");
                // text是分享文本，所有平台都需要这个字段
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                paramsToShare.setImageUrl(vedioInfoBean.getImg_src() + "");

                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                paramsToShare.setComment(vedioInfoBean.getPlay_count());
                // site是分享此内容的网站名称，仅在QQ空间使用
                paramsToShare.setSite("CINews");
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                paramsToShare.setSiteUrl(vedioInfoBean.getPlay_src() + "");

                //新浪微博分享
            } else if (SinaWeibo.NAME.equals(platform.getName())) {

                // text是分享文本，所有平台都需要这个字段
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                paramsToShare.setImageUrl(vedioInfoBean.getImg_src() + "");

                //微信朋友圈分享
            } else if (WechatMoments.NAME.equals(platform.getName())) {

                paramsToShare.setTitle(vedioInfoBean.getNews_title());
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                paramsToShare.setUrl(vedioInfoBean.getVideo_src() + "");
                paramsToShare.setImagePath(vedioInfoBean.getImg_src() + "");

                //微信收藏
            } else if (WechatFavorite.NAME.equals(platform.getName())) {
                paramsToShare.setTitle(vedioInfoBean.getNews_title());
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                paramsToShare.setUrl(vedioInfoBean.getVideo_src() + "");
                paramsToShare.setImagePath(vedioInfoBean.getImg_src() + "");
            }
        }
    }

    /**
     * 下载视频到本地
     *
     * @param play_info
     */
    private void downloadVedio(VedioInfoBean play_info) {
        //判断是否存在SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.DIRECTORY_DOWNLOADS, play_info.getNews_title().replace(" ", "") + ".mp4");
            e(TAG, "downloadVedio" + file.getAbsolutePath() + file.exists());
            if (!file.exists()) {
                Toast.makeText(VedioNewsDetailsActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(play_info.getPlay_src()));
                request.setNotificationVisibility(VISIBILITY_VISIBLE);//用于设置下载时时候在状态栏显示通知信
                request.allowScanningByMediaScanner();//用于设置是否允许本MediaScanner扫描。
                request.setTitle("下载中");
                request.setDescription("视频正在下载中");
                request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, play_info.getNews_title().replace(" ", "") + ".mp4");
                long enqueue = downManager.enqueue(request);
            } else {
                Toast.makeText(this, "您已经下载过了这个视频...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请插入SD卡...", Toast.LENGTH_SHORT).show();
        }
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
            startActivityForResult(new Intent(VedioNewsDetailsActivity.this, LoginActivity.class), REQUEST_CODE);
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

    public class getPositionBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int position = intent.getIntExtra("position", 0);
            e(TAG, "onReceive" + "position"+position);
            vedio_paly.seekTo(position);
                if (position==0){
                    per_position=0; 
                    vedio_paly.seekTo(vedio_paly.getDuration()-1);
                }else{
                    vedio_paly.start();
                }
        }
    }

}
