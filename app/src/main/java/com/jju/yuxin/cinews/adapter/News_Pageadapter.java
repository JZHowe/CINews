package com.jju.yuxin.cinews.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.service.PagerDateInit;
import com.jju.yuxin.cinews.views.InnerViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.adapter
 * Created by yuxin.
 * Created time 2016/11/12 0012 下午 9:58.
 * Version   1.0;
 * Describe : 新闻页的适配器
 * History:
 * ==============================================================================
 */

public class News_Pageadapter extends PagerAdapter {
    private Context context;
    private List<View> viewList;
    private String[] new_title;
    private LayoutInflater inflater;

    /**
     *
     * viewList是需要加载的item集合
     * 传入的new_title是item对应的标题
     *
     * @param context
     * @param new_title
     * @param viewList
     */
    public News_Pageadapter(Context context, String[] new_title, List<View> viewList) {
        this.context = context;
        this.new_title = new_title;
        this.viewList = viewList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    //key值的对比
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == viewList.get((Integer) object);
    }

    //为下一个即将加载的item初始化

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //获取当前item是否隐藏的视图
        LinearLayout ll_top = (LinearLayout) viewList.get(position).findViewById(R.id.ll_top);

        //获取当前item的listview
        ListView lv_content = (ListView) viewList.get(position).findViewById(R.id.lv_content);

        //判断顶栏的viewpages是否是需要显示
        if (ll_top.getTag().equals(View.GONE)) {

            //设置隐藏
            ll_top.setVisibility(View.GONE);
        } else {

            //设置可见
            ll_top.setVisibility(View.VISIBLE);

            //加载里层数viewpages
            InnerViewPager new_inner_vp = (InnerViewPager) viewList.get(position).findViewById(R.id.new_inner_vp);

            //根据view当前位置,解析对应的viewpager
            ArrayList<Integer> innerPagerdata = PagerDateInit.getInnerPagerdata(context, (String) viewList.get(position).getTag());

            //viewList.get(position).getTag()
            new_inner_vp.setAdapter(new InnerPagerAdapter(context, innerPagerdata));

        }

        //根据view当前位置,对应ListView的内容
        String[] new_contents= PagerDateInit.getItemListdata(context, (String) viewList.get(position).getTag());

        //listview的数据初始化
        lv_content.setAdapter(new Lv_content_Adapter(context, new_contents));

        //将当前的的页面添加至最外层的viewpages
        container.addView(viewList.get(position));
        return position;
    }

    //移除除了自己本身和自己左右的的item
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }


    //获取导航栏的标题
    @Override
    public CharSequence getPageTitle(int position) {
        //返回当前TabStrip的title
        return new_title[position];
    }
}