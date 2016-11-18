package com.jju.yuxin.cinews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.adapter.FavorList_Adapter;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.bean.VedioInfoBean;
import com.jju.yuxin.cinews.db.DbUtils;
import com.jju.yuxin.cinews.db.Favors;

import java.util.List;

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
    private ListView mListView;
    private List<Favors> mFavorsList;
    private FavorList_Adapter adapter;
    private boolean isDelete = false; //删除状态

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
        mListView.setOnItemClickListener(mOnItemClickListener);

        mFavorsList = DbUtils.searchAllFavor();

        adapter = new FavorList_Adapter(FavoriteActivity.this, mFavorsList);
        mListView.setAdapter(adapter);
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

                Intent intent = new Intent(FavoriteActivity.this,VedioNewsDetailsActivity.class);
                intent.putExtra("vedio_news",vedioInfoBean);
                startActivity(intent);
            } else if (favors.getType().equals("new")) {
                NewsBean newsBean = new NewsBean();

                newsBean.setId(favors.getNews_id());
                newsBean.setUrl(favors.getImg_src());
                newsBean.setKey(favors.getKey());
                newsBean.setName(favors.getTitle());
                newsBean.setSummary(favors.getSummary());
                newsBean.setTime(favors.getDate());

                Intent intent2 = new Intent(FavoriteActivity.this,NewsDetailsActivity.class);
                intent2.putExtra("news",newsBean);
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
        mFavorsList = DbUtils.searchAllFavor();
        adapter.replaceList(mFavorsList, isDelete);
        adapter.notifyDataSetChanged();
    }

}
