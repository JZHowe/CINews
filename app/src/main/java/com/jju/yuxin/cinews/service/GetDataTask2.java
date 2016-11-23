package com.jju.yuxin.cinews.service;

import android.os.AsyncTask;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jju.yuxin.cinews.adapter.Lv_content_Adapter;

/**
 * Created by Administrator on 2016/11/18.
 */

public class GetDataTask2 extends AsyncTask<Void, Void, String> {
    private int mItemCount = 9;
    private PullToRefreshListView listView;
    private Lv_content_Adapter adapter;

    public GetDataTask2(PullToRefreshListView listView, Lv_content_Adapter adapter) {
        this.listView = listView;
        this.adapter = adapter;
    }

    @Override
    protected String doInBackground(Void... params)
    {
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
        }
        return "" + (mItemCount++);
    }

    @Override
    protected void onPostExecute(String result)
    {
        //刷新时再次获取数据
        adapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        listView.onRefreshComplete();
    }
}
