package com.jju.yuxin.cinews.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.VedioInfoBean;
import com.jju.yuxin.cinews.utils.JsoupUtils;

public class NewsDetailsActivity extends BaseActivity {

    private Button bt_top_left;
    private Button bt_top_right;
    private VedioInfoBean vedioInfoBean;

    //当加载数据完毕需要更新界面数据
    Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        //将导航栏的左右按钮设置为可见并为其设置背景资源
        bt_top_left = (Button) findViewById(R.id.bt_top_left);
        bt_top_right = (Button) findViewById(R.id.bt_top_right);
        bt_top_left.setVisibility(View.VISIBLE);
        bt_top_right.setVisibility(View.VISIBLE);
        bt_top_left.setBackgroundResource(R.drawable.bt_return_selector);
        bt_top_right.setBackgroundResource(R.drawable.bt_shoucang_selector);

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
                Toast.makeText(NewsDetailsActivity.this, "收藏操作", Toast.LENGTH_SHORT).show();
            }
        });
        //传过来的视频新闻对象
        vedioInfoBean = getIntent().getParcelableExtra("vedio_news");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取视频详情
        JsoupUtils.getNewsDetails(vedioInfoBean.getVideo_src(),mhandler);
    }
}
