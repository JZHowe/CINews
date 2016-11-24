package com.jju.yuxin.cinews.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.activity.NewsDetailsActivity;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.service.GetDataTask;
import com.jju.yuxin.cinews.service.GetDataTask2;
import com.jju.yuxin.cinews.service.JsonUtil;
import com.jju.yuxin.cinews.service.PagerDateInit;
import com.jju.yuxin.cinews.utils.MyLogger;
import com.jju.yuxin.cinews.views.InnerViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author liuhouchao
 * Describe : 新闻页的适配器
 */

public class News_Pageadapter extends PagerAdapter {
    private Context context;
    private List<View> viewList;
    private List<String> list;
    private LayoutInflater inflater;
    private TextView textView;
    private InnerViewPager new_inner_vp;   //里层的ViewPager
    private List<NewsBean> olist1;       //里层图片viewPager的信息集合
    private List<NewsBean> olist2 = new ArrayList<>();  //点击详细内容传入的olist
    private LinearLayout ll;

    List<TextView> textviewLists;   //小圆点的集合
    private int index = 0;     //小圆点的位置
    private boolean isstop = false;
    private MyThread thread = new MyThread();
    //初始化图片轮播的线程
    private MyThread threads = new MyThread();
    //标志位,放置信息的重复加载
    private Object object_flag = 100;

    private Lv_content_Adapter adapter;         //新闻条目的adapter
    //    private int page=2;
    int[] pageArray = new int[]{2, 2, 2, 2, 2, 2, 2, 2};
    private Map<String, Object> pagenumber = new HashMap<>();
    private PullToRefreshListView lv_content;
    private LinearLayout pb_loading;    //动画

    //获取消息,加载当前页面视图
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //产业新闻的图片信息
                case R.id.text1:
                    String info = (String) msg.obj;
                    MyLogger.lLog().e("*&*&*" + info);
                    olist1 = JsonUtil.parseJSON(info);
                    //通过判断当前视图的子视图的个数,放置viewpager的小圆点重复创建
                    if (ll.getChildCount() == 0 && olist1 != null) {
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
                    if (olist1 != null) {
                        new_inner_vp.setAdapter(new InnerPagerAdapter(context, olist1,
                                textView));
                    }
                    break;
                case R.id.text5:
                    //获取新闻条目
                    String info2 = (String) msg.obj;
                    olist2 = JsonUtil.parseJSON(info2);
                    if (olist2 != null) {
                        adapter = new Lv_content_Adapter(context, olist2);
                        lv_content.setAdapter(adapter);
                        lv_content.setOnItemClickListener(itmeListener);
                    }
                    pb_loading.setVisibility(View.GONE);  //让动画消失
                    break;
                case R.id.image_thread:
                    //在子线程定时中发送空消息达到图片轮播的效果
                    if (olist1 != null) {
                        new_inner_vp.setCurrentItem(index % olist1.size());
                    }
                    break;
            }
        }
    };

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


    //切换页面到当前,开启线程加载当前页面的信息发送给适配器,更新页面
    @Override
    public void setPrimaryItem(ViewGroup container, final int position, Object object) {
        //setPrimaryItem会不停的执行
        //第一次进来或者切换页面进来,不相等,取反，执行if里面的，防止了数据不断重复加载
        if (!object_flag.equals(object)) {
            MyLogger.lLog().i("**%&setPrimaryItem" + position);

            //获取当前item是否隐藏的视图
            RelativeLayout ll_top = (RelativeLayout) viewList.get(position).findViewById(R.id.ll_top);
            if (textView == null) {
                textView = (TextView) viewList.get(position).findViewById(R.id.tv_vp_content);
                ll = (LinearLayout) viewList.get(position).findViewById(R.id.ll);

            }

            //获取当前item的listview
            lv_content = (PullToRefreshListView) viewList.get(position).findViewById(R.id.pull_refresh_list);

            //加载动画
            pb_loading = (LinearLayout) viewList.get(position).findViewById(R.id.pb_loading);
            //加载动画
            // pb_loading.setVisibility(View.VISIBLE);

            //判断顶栏的viewpager是否是需要显示
            if (ll_top.getTag().equals(View.GONE)) {

                //设置隐藏
                ll_top.setVisibility(View.GONE);
            } else {
                //设置可见
                ll_top.setVisibility(View.VISIBLE);

                //加载里层数viewpages
                new_inner_vp = (InnerViewPager) viewList.get(position).findViewById(R.id.new_inner_vp);
                new_inner_vp.setOnPageChangeListener(listener);



                //根据view当前位置,解析对应的viewpager 图片
                PagerDateInit.getInnerPagerdata(context, (String) viewList.get(position).getTag(),
                        handler);

            }

            final PullToRefreshBase.OnRefreshListener2<ListView> refreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    String label = DateUtils.formatDateTime(
                            context,
                            System.currentTimeMillis(),
                            DateUtils.FORMAT_SHOW_TIME
                                    | DateUtils.FORMAT_SHOW_DATE
                                    | DateUtils.FORMAT_ABBREV_ALL);
                    // 显示最后更新的时间
                    refreshView.getLoadingLayoutProxy()
                            .setLastUpdatedLabel(label);

                    Log.e("TAG", "onPullDownToRefresh");
                    //这里写下拉刷新的任务
                    new GetDataTask2(lv_content, adapter).execute();
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    Log.e("TAG", "onPullUpToRefresh");
                    //这里写上拉加载更多的任务
                    pagenumber.put((String) viewList.get(position).getTag(), pageArray[position]);

                    int chuanpage = (int) pagenumber.get((String) viewList.get(position).getTag());
                    new GetDataTask(adapter, olist2, lv_content, (String) viewList.get(position).getTag(), context, chuanpage).execute();
                    pageArray[position]++;
                    MyLogger.lLog().e("page" + pageArray[position]);

                }
            };

            //设置刷新监听
            lv_content.setOnRefreshListener(refreshListener);


            //根据view当前位置,对应ListView的内容
            PagerDateInit.getItemListdata(context, (String) viewList.get(position).getTag(), handler, R.id.text5);


            object_flag = object;  //赋值为相等
        }
    }

    //获取外层viewpager的页数
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
    public Object instantiateItem(ViewGroup container, final int position) {
        MyLogger.lLog().i("**%&instantiateItem" + position);
        //当第一个页面被初始化的时候，将线程从暂停状态转为运行状态
        if (position == 0) {
            isstop = false;
        }

        //将当前的的页面添加至最外层的viewpages
        container.addView(viewList.get(position));
        return position;
    }


    //图片轮播的线程
    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                while (!isstop) {
                    try {
                        sleep(4500);
                        index++;
                        handler.sendEmptyMessage(R.id.image_thread);

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
        pageArray[position] = 2;
        MyLogger.lLog().i("**%&destroyItem" + position);
        container.removeView(viewList.get(position));
    }


    //获取导航栏的标题
    @Override
    public CharSequence getPageTitle(int position) {
        //返回当前TabStrip的title
        return list.get(position);
    }




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
    //PullToRefreshListView的Position问题:
    // 在OnItemClickListener方法参数position，从1开始，
    // 而不是像ListView那般从0开始。原因是因为PullToRefreshListView有Header
    private AdapterView.OnItemClickListener itmeListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, NewsDetailsActivity.class);
            //将新闻列表对象传入方便收藏操作存储
            intent.putExtra("news", olist2.get(position - 1));
            context.startActivity(intent);
        }
    };
}
