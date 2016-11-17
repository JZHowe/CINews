package com.jju.yuxin.cinews.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.activity.NewsDetailsActivity;
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
    private List<NewsBean> olist1;       //里层viewPager的信息集合
    private List<NewsBean> olist2 = new ArrayList<>();
    private LinearLayout ll;

    private int index = 0;
    private boolean isstop = false;
    private MyThread thread = new MyThread();
    //初始化图片轮播的线程
    private MyThread threads = new MyThread();
    //标志位,放置信息的重复加载
    private  Object object_flag=100;

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
        //在viewpager的构造中将初始化了的图片轮播线程开启
        thread.start();
    }
    //获取消息,加载当前页面视图
    Handler handler_text5 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==R.id.text5) {
                String info = (String) msg.obj;
                olist2 = JsonUtil.parseJSON(info);
            }
        }
    };
    //切换页面到当前,开启线程加载当前页面的信息发送给适配器,更新页面
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        //第一次进来,不相等,直到切换页面才会再次的加载页面,有效的放置了,数据不断重复加载
        if (!object_flag.equals(object)){
        PagerDateInit.getItemListdata(context, (String) viewList.get(position).getTag(), handler_text5,R.id.text5);
            object_flag=object;
        }

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
        //当第一个页面被初始化的时候，将线程从暂停状态转为运行状态
        if (position == 0) {
            isstop = false;
        }
        //获取当前item是否隐藏的视图
        RelativeLayout ll_top = (RelativeLayout) viewList.get(position).findViewById(R.id.ll_top);
        if (textView == null) {
            textView = (TextView) viewList.get(position).findViewById(R.id.tv_vp_content);
            ll = (LinearLayout) viewList.get(position).findViewById(R.id.ll);

        }


        //获取当前item的listview
        final ListView lv_content = (ListView) viewList.get(position).findViewById(R.id.lv_content);
        //加载动画
        final LinearLayout pb_loading = (LinearLayout)viewList.get(position).findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);


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


            Handler handler_text1 = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case R.id.text1:
                            String info = (String) msg.obj;
                            MyLogger.lLog().e("*&*&*"+info);
                            olist1 = JsonUtil.parseJSON(info);
                            //通过判断当前视图的子视图的个数,放置viewpager的小圆点重复创建
                            if (ll.getChildCount() == 0) {
                                textviewLists = new ArrayList<>();
                                for (int i = 0; i < olist1.size(); i++) {
                                    TextView textView = new TextView(context);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                            (10, 10);
                                    params.setMargins(0, 5, 15, 0);
                                    textView.setLayoutParams(params);
                                    textView.setBackgroundResource(R.drawable.text_white);
                                    textView.setTag(i);
                                    textviewLists.add(textView);
                                    textviewLists.get(0).setBackgroundResource(R.drawable.text_red);
                                    ll.addView(textView);
                                }
                            }
                            new_inner_vp.setAdapter(new InnerPagerAdapter(context, olist1,
                                    textView));

                            break;
                    }
                }
            };

            //根据view当前位置,解析对应的viewpager 图片
            PagerDateInit.getInnerPagerdata(context, (String) viewList.get(position).getTag(),
                    handler_text1);

        }

        Handler handler_text2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case R.id.text2:
                        String info = (String) msg.obj;
                        List<NewsBean> olist = new ArrayList<NewsBean>();
                        //获取加载了视图的的内容
                        olist = JsonUtil.parseJSON(info);
                        //listview的数据初始化
                        if (olist != null) {
                            lv_content.setAdapter(new Lv_content_Adapter(context, olist));
                            lv_content.setOnItemClickListener(itmeListener);

                        }
                        break;
                }
                pb_loading.setVisibility(View.GONE);
            }
        };

        //根据view当前位置,对应ListView的内容
        PagerDateInit.getItemListdata(context, (String) viewList.get(position).getTag(), handler_text2,R.id.text2);

        //将当前的的页面添加至最外层的viewpages
        container.addView(viewList.get(position));
        return position;
    }


    //在子线程定时中发送空消息达到图片轮播的效果
    private Handler image_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.id.image_thread:
                    if (olist1 != null) {
                        new_inner_vp.setCurrentItem(index % olist1.size());
                    }
                    break;
            }
        }
    };


    //图片轮播的线程
    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                while (!isstop) {
                    try {
                        sleep(2000);
                        index++;
                        image_handler.sendEmptyMessage(R.id.image_thread);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    //移除除了自己本身和自己左右的的item
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //当地一个页面被注销的时候，将线程停止
        if (position == 0) {
            isstop = true;
            index = 0;
        }

        container.removeView(viewList.get(position));
    }


    //获取导航栏的标题
    @Override
    public CharSequence getPageTitle(int position) {
        //返回当前TabStrip的title
        return list.get(position);
    }


    List<TextView> textviewLists;

    /*
    *里层ViewPager的滑动监听事件
     */
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //获取当前页面id,将右下角小圆点全部设置为白色
            for (TextView textView : textviewLists) {
                textView.setBackgroundResource(R.drawable.text_white);
            }
            //将当前页面对应位置的小圆点设置为红色
            textviewLists.get(position).setBackgroundResource(R.drawable.text_red);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    //列表项点击事件
    private AdapterView.OnItemClickListener itmeListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, NewsDetailsActivity.class);
            //将新闻列表对象传入方便收藏操作存储
            intent.putExtra("news",olist2.get(position));
            context.startActivity(intent);
        }
    };
}
