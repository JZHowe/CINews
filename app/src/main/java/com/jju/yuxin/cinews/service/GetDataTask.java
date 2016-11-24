package com.jju.yuxin.cinews.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.adapter.Lv_content_Adapter;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.utils.Ksoap2Util;
import com.jju.yuxin.cinews.utils.MyLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/18.
 */
public class GetDataTask extends AsyncTask<Void, Void, String> {
    private PullToRefreshListView listView;
    private Lv_content_Adapter adapter;
    private List<NewsBean> olist;


    Map<String,Object> oMap = new HashMap<>();
    private Context context;
    private String viewtag;
    private int page;





    public GetDataTask(Lv_content_Adapter adapter, List<NewsBean> olist, PullToRefreshListView listView, String name, Context context,int page) {
        this.adapter = adapter;
        this.olist = olist;
        this.listView = listView;
        this.context = context;
        this.viewtag = name;
        this.page = page;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        //获取ViewPager对应的导航栏的文字信息
        String[] stringArray = context.getResources().getStringArray(R.array.new_title_list);


        //加载产业新闻的ListViewn内容
        if (stringArray[0].equals(viewtag)) {
            oMap.put("id","69e8ad24-76f8-40c3-9e6c-f09c4eb39839");
            oMap.put("page",page);

            //加载动漫游戏的ListViewn内容
        } else if (stringArray[1].equals(viewtag)) {
            oMap.put("id","d2ce6ce0-d55c-46ce-9b9e-0df3bb726877");
            oMap.put("page",page);
            //加载工艺美术的ListViewn内容
        } else if (stringArray[2].equals(viewtag)) {
            oMap.put("id","c0bb5dab-2120-4e5b-885c-e17dd227ec3e");
            oMap.put("page",page);
            //加载休闲旅游的ListViewn内容
        } else if (stringArray[3].equals(viewtag)) {
            oMap.put("id","5b5b5cb2-898a-4899-b51a-6a4e684f860e");
            oMap.put("page",page);

            //加载园区基地的ListViewn内容
        } else if (stringArray[4].equals(viewtag)) {
            oMap.put("id","a4ce81c3-727d-45c9-856f-5e7813b05166");
            oMap.put("page",page);

            //加载演艺传媒的ListViewn内容
        } else if (stringArray[5].equals(viewtag)) {
            oMap.put("id","e14512f4-f384-4575-8982-1df4f3ea96ea");
            oMap.put("page",page);

            //加载文化金融的ListViewn内容
        } else if (stringArray[6].equals(viewtag)) {
            oMap.put("id","68332b3d-846f-4712-9511-a2467f541550");
            oMap.put("page",page);

            //加载协会动态的ListViewn内容
        } else if (stringArray[7].equals(viewtag)) {
            oMap.put("id","218617c3-539b-4fe6-9023-a891750590da");
            oMap.put("page",page);

        }
        String info = Ksoap2Util.connect("getlistartile", oMap);

        return info;
    }

    @Override
    protected void onPostExecute(String result) {
        List<NewsBean> olist1 = new ArrayList<>();
        MyLogger.lLog().e("&**&^&^"+result);
         olist1 = JsonUtil.parseJSON(result);
        if (olist1!=null){
            olist.addAll(olist1);
            MyLogger.lLog().e("&**&^&^");
        }else {
            //没有数据
            Toast.makeText(context,"已无更多数据",Toast.LENGTH_SHORT).show();
        }
        adapter.replaceList(olist);
        adapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        listView.onRefreshComplete();
    }
}
