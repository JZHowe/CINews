package com.jju.yuxin.cinews.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.VedioInfoBean;
import com.jju.yuxin.cinews.utils.JsoupUtils;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.util.Log.e;


public class VedioNewsDetailsActivity extends BaseActivity {

    private static final String TAG = VedioNewsDetailsActivity.class.getSimpleName();

    private Button bt_top_left;
    private Button bt_top_right;
    private VedioInfoBean vedioInfoBean;

    //加载成功
    private static final int SUCCESS_LOAD = 0;
    //加载失败
    private static final int FAIL_LOAD = 1;

    //当加载数据完毕需要更新界面数据
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                //视频新闻加载成功
                case SUCCESS_LOAD:
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
        bt_top_right.setBackgroundResource(R.drawable.bt_shoucang_selector);

        SharedClickListener sharedClickListener = new SharedClickListener();

        //新浪微博分享按钮
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
                //todo 收藏操作
                Toast.makeText(VedioNewsDetailsActivity.this, "收藏操作", Toast.LENGTH_SHORT).show();
            }
        });

        //传过来的视频新闻对象
        vedioInfoBean = getIntent().getParcelableExtra("vedio_news");
    }


    @Override
    protected void onResume() {

        e(TAG, "onResume" + "");
        super.onResume();
        //根据当前进度是否为零判断之前是否播放过
        if (per_position == 0) {
            //如果没有播放过那么加载视频资源
            if (vedioInfoBean != null) {
                //获取视频详情
                JsoupUtils.getNewsDetails(vedioInfoBean, mhandler);
            }
            //如果已经播放过那么跳转到上次的播放进度之后
        } else {
            vedio_paly.seekTo(per_position);
            vedio_paly.start();
            per_position = 0;
        }


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
        if(vedio_paly!=null){
            vedio_paly.stopPlayback(); //将VideoView所占用的资源释放掉
        }

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
// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(vedioInfoBean.getNews_title());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(vedioInfoBean.getPlay_src()+"");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(vedioInfoBean.getNews_info()+"");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(vedioInfoBean.getImg_src()+"");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(vedioInfoBean.getPlay_src()+"");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(vedioInfoBean.getPlay_count());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("CINews");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(vedioInfoBean.getPlay_src()+"");
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


    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VedioNewsDetailsActivity.this, "播放完成!", Toast.LENGTH_SHORT).show();
        }
    }


}
