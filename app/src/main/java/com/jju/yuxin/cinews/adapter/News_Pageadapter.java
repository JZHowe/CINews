package com.jju.yuxin.cinews.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.service.JsonUtil;
import com.jju.yuxin.cinews.service.PagerDateInit;
import com.jju.yuxin.cinews.utils.MyLogger;
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
    private List<String> list;
    private LayoutInflater inflater;
    private TextView textView;
    private InnerViewPager new_inner_vp;   //里层的ViewPager

    /**
     * viewList是需要加载的item集合（OuterViewPager的集合）
     * 传入的new_title是item对应的标题
     *
     * @param context
     * @param list
     * @param viewList
     */
    public News_Pageadapter(Context context, List<String> list, List<View> viewList) {
        this.context = context;
        this.list = list;
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
        RelativeLayout ll_top = (RelativeLayout) viewList.get(position).findViewById(R.id.ll_top);
        if (textView == null) {
            textView = (TextView) viewList.get(position).findViewById(R.id.tv_vp_content);

        }
        final LinearLayout ll= (LinearLayout) viewList.get(position).findViewById(R.id.ll);

        //获取当前item的listview
        final ListView lv_content = (ListView) viewList.get(position).findViewById(R.id.lv_content);


        //判断顶栏的viewpages是否是需要显示
        if (ll_top.getTag().equals(View.GONE)) {

            //设置隐藏
            ll_top.setVisibility(View.GONE);
        } else {

            //设置可见
            ll_top.setVisibility(View.VISIBLE);

            //加载里层数viewpages
            new_inner_vp = (InnerViewPager) viewList.get(position).findViewById(R.id.new_inner_vp);
            new_inner_vp.setOnPageChangeListener(listener);

            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case R.id.text1:
                            String info = (String) msg.obj;
                            MyLogger.lLog().e(info);
                            List<NewsBean> olist1 = JsonUtil.parseJSON(info);

                            for (int i = 0; i< olist1.size(); i++){
                                TextView textView = new TextView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
                                params.setMargins(0, 5, 15, 0);
                                textView.setLayoutParams(params);
                                textView.setBackgroundResource(R.drawable.text_white);
                                textView.setTag(i);
                                textviewLists.add(textView);
                                textviewLists.get(0).setBackgroundResource(R.drawable.text_red);
                                ll.addView(textView);
                            }
                            new_inner_vp.setAdapter(new InnerPagerAdapter(context, olist1, textView));

                            //开启定时器，图片轮播
                            image_thread();
                            break;
                    }
                }
            };

            //根据view当前位置,解析对应的viewpager 图片
            PagerDateInit.getInnerPagerdata(context, (String) viewList.get(position).getTag(), handler);

        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case R.id.text2:
                        String info = (String) msg.obj;
                        List<NewsBean> olist2 = JsonUtil.parseJSON(info);
                        //listview的数据初始化
                        if (olist2 != null) {
                            lv_content.setAdapter(new Lv_content_Adapter(context, olist2));
                        }
                        break;
                }
            }
        };

        //根据view当前位置,对应ListView的内容
        PagerDateInit.getItemListdata(context, (String) viewList.get(position).getTag(), handler);

        //将当前的的页面添加至最外层的viewpages
        container.addView(viewList.get(position));
        return position;
    }

    private int index=0;
    private boolean isstop = false;
    private Handler image_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.id.image_thread:
                    new_inner_vp.setCurrentItem(index % textviewLists.size());
                    break;
            }
        }
    };

    //图片轮播
    private void image_thread() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    while (!isstop) {
                        try {
                            index++;
                            image_handler.sendEmptyMessage(R.id.image_thread);
                            sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();

    }


    //移除除了自己本身和自己左右的的item
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

//        container.removeView((View)viewList.get((Integer) object));
        container.removeView(viewList.get(position));
    }


    //获取导航栏的标题
    @Override
    public CharSequence getPageTitle(int position) {
        //返回当前TabStrip的title
        return list.get(position);
    }


    List<TextView> textviewLists = new ArrayList<>();
    //里层ViewPager的滑动监听事件
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {
            for (TextView textView:textviewLists){
                textView.setBackgroundResource(R.drawable.text_white);
            }
            textviewLists.get(position).setBackgroundResource(R.drawable.text_red);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
