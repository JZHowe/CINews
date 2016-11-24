package com.jju.yuxin.cinews.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.jju.yuxin.cinews.R;

import static android.util.Log.e;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.activity
 * Created by yuxin.
 * Created time 2016/11/23 0023 上午 8:42.
 * Version   1.0;
 * Describe :视屏全屏播放界面
 * History:
 * ==============================================================================
 */

public class LandScapeVedio extends Activity {

    private VideoView full_vedioview;
    private int per_position = 0;
    private static final String TAG = LandScapeVedio.class.getSimpleName();
    private LinearLayout pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);
        full_vedioview = (VideoView) findViewById(R.id.full_vedioview);

        //重新绘制VideoView,达到全屏的目的
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        full_vedioview.setLayoutParams(layoutParams);


        //加载动画
        pb_loading = (LinearLayout) findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        int currentPosition = intent.getIntExtra("currentPosition", 0);

       // e(TAG, "onCreate" + "currentPosition:"+currentPosition);

        String play_src = intent.getStringExtra("play_src");
        playVedio(play_src, currentPosition);
    }

    /**
     * 播放视频的操作
     *
     * @param play_src
     */
    private void playVedio(String play_src, final int position) {
        Uri uri = Uri.parse(play_src);
        //设置视频控制器
        full_vedioview.setMediaController(new MediaController(this));

        //播放完成回调
        full_vedioview.setOnCompletionListener(new LandScapeVedio.MyPlayerOnCompletionListener());

        //播放错误监听
        full_vedioview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                Toast.makeText(LandScapeVedio.this, R.string.haoxiangchuxianleyidianwenti, Toast.LENGTH_SHORT).show();
                full_vedioview.seekTo(0);
                full_vedioview.pause();
                return true;
            }
        });

        //设置视频路径
        full_vedioview.setVideoURI(uri);

        //准备完成
        full_vedioview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pb_loading.setVisibility(View.GONE);
                full_vedioview.seekTo(position);
                //开始播放视频
                full_vedioview.start();
            }
        });
    }

    /**
     * 播放器播放完成监听
     */
    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(LandScapeVedio.this, R.string.bofangwancheng, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.setAction("sendposition");
            e(TAG, "onCompletion" + 0);
            intent.putExtra("position",0);
            sendBroadcast(intent);
            finish();
        }
    }

    /**
     * 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        e(TAG, "onPause" + "");

        //记录下当前播放的进度
        if (full_vedioview.isPlaying()) {
            full_vedioview.pause();
        }
        per_position = full_vedioview.getCurrentPosition();
    }


    @Override
    protected void onResume() {
        super.onResume();
        full_vedioview.seekTo(per_position);
        full_vedioview.start();
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent=new Intent();
        intent.setAction("sendposition");
        e(TAG, "onDestroy" + per_position);
        intent.putExtra("position",per_position);
        sendBroadcast(intent);
        if (full_vedioview != null) {
            full_vedioview.stopPlayback(); //将VideoView所占用的资源释放掉
        }

        //e(TAG, "onDestroy" + "释放资源");
    }


}
