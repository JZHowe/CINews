package com.jju.yuxin.cinews.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.VedioInfoBean;
import com.jju.yuxin.cinews.utils.JsoupUtils;


public class VedioNewsDetailsActivity extends BaseActivity {

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
                    VedioInfoBean vedioInfoBean = (VedioInfoBean) msg.obj;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_news_details);
        //将导航栏的左右按钮设置为可见并为其设置背景资源
        bt_top_left = (Button) findViewById(R.id.bt_top_left);
        bt_top_right = (Button) findViewById(R.id.bt_top_right);
        bt_top_left.setVisibility(View.VISIBLE);
        bt_top_right.setVisibility(View.VISIBLE);
        bt_top_left.setBackgroundResource(R.drawable.bt_return_selector);
        bt_top_right.setBackgroundResource(R.drawable.bt_shoucang_selector);

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

    private void playVedio(String play_src) {


        Uri uri = Uri.parse(play_src);


        //设置视频控制器
        vedio_paly.setMediaController(new MediaController(this));

        //播放完成回调
        vedio_paly.setOnCompletionListener(new MyPlayerOnCompletionListener());

        vedio_paly.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                hlog.e("what"+what+"extra"+extra);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (vedioInfoBean != null) {
            //获取视频详情
            JsoupUtils.getNewsDetails(vedioInfoBean, mhandler);
        }

    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VedioNewsDetailsActivity.this, "播放完成!", Toast.LENGTH_SHORT).show();
        }
    }
}
