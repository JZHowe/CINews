package com.jju.yuxin.cinews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.MessageBean;
import com.jju.yuxin.cinews.bean.VedioInfoBean;
import com.jju.yuxin.cinews.service.JsonUtil;
import com.jju.yuxin.cinews.utils.JsoupUtils;
import com.jju.yuxin.cinews.utils.Ksoap2Util;
import com.jju.yuxin.cinews.utils.MyLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NewsDetailsActivity extends BaseActivity {

    private Button bt_top_left;
    private Button bt_top_right;
    private TextView news_title;
    private TextView push_date;
    private TextView reader_count;
    private WebView webView;
    List<MessageBean> olist;
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



                    break;
                //视频新闻加载失败
                case FAIL_LOAD:

                    break;
                case R.id.text3:
                    String info = (String) msg.obj;
                    olist = JsonUtil.parseJSON_(info);
                    MyLogger.zLog().e(olist+"%%%%%%%%%%%%%%%%%%%%");
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadDataWithBaseURL(null,olist.get(0).getContent(),"text/html","UTF-8",null);
                    news_title.setText(olist.get(0).getName());
                    push_date.setText("发布时间:"+olist.get(0).getTime());
                    reader_count.setText("阅读量:"+olist.get(0).getCount());
                default:
                    break;
            }

        }
    };
    
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
        bt_top_right.setBackgroundResource(R.drawable.bt_shoucang_selector);

        //新闻标题
        news_title = (TextView) findViewById(R.id.news_title);
        //新闻发布时间
        push_date = (TextView) findViewById(R.id.push_date);
        //新闻阅读次数,或者视频播放次数
        reader_count = (TextView) findViewById(R.id.reader_count);


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

        getInfo();

    }

    //接收的list信息详情
    private void getInfo(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Map<String,Object> oMap = new HashMap<String,Object>();
        oMap.put("id",id);
        Ksoap2Util.doBackgroud(mhandler,R.id.text3,"getartile",oMap);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
