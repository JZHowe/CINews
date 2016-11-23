package com.jju.yuxin.cinews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.adapter.News_Pageadapter;
import com.jju.yuxin.cinews.utils.SharedUtil;
import com.jju.yuxin.cinews.views.MultiDirectionSlidingDrawer;
import com.jju.yuxin.cinews.views.OuterViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName NewsActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:02.
 * Describe :新闻Activity
 * History:
 * Version   1.0.
 * hhaha
 * ==============================================================================
 */

public class NewsActivity extends BaseActivity {
    private Context mContext;
    private String[] new_title;             //导航栏文字
    private OuterViewPager vp_new_content;  //外层的ViewPager
    private List<View> viewList;
    private TabLayout tl_new_content;
    private ImageButton ib_new, ib_item_add;
    private MultiDirectionSlidingDrawer drawer;
    private TextView tv_new_content;
    private GridView gv_add, gv_sub;
    private List<String> list_add, list_sub;
    private List<String> list_isadd, list_issub;
    private ArrayAdapter<String> adapter, adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mContext = NewsActivity.this;
        init();
        viewList = new ArrayList<>();
        list_add = new ArrayList<>();
        list_sub = new ArrayList<>();
        list_isadd = new ArrayList<>();
        list_issub = new ArrayList<>();
        //ViewPageritem的item数据模拟初始化
        datainit(vp_new_content);
    }

    /**
     * ViewPageritem的item数据模拟初始化,需要在这里实现对ViewPager的增删
     */
    private void datainit(OuterViewPager vp_new_content) {
        //获取ViewPager对应的导航栏的全部文字信息
        new_title = getResources().getStringArray(R.array.new_title_list);

        //判断是否为首次进入,是则给导航栏添加前四个栏目,并添加到首选项中;否则从首选项中获得
        if (SharedUtil.getInstance(mContext).getBoolean("first", true)) {
            for (int i = 0; i < new_title.length; i++) {
                if (i < 4) {
                    list_add.add(new_title[i]);
                    SharedUtil.getEditor(mContext).putBoolean("add" + i, true).commit();
                } else {
                    list_sub.add(new_title[i]);
                    SharedUtil.getEditor(mContext).putBoolean("add" + i, false).commit();
                }
            }
            SharedUtil.getEditor(mContext).putBoolean("first", false).commit();

        } else {
            for (int i = 0; i < new_title.length; i++) {
                if (SharedUtil.getInstance(mContext).getBoolean("add" + i, false)) {
                    list_add.add(new_title[i]);
                } else {
                    list_sub.add(new_title[i]);
                }
            }
        }
        //初始化item布局个数
        for (int i = 0; i < list_add.size(); i++) {
            //新闻模块所有子模块对应的相同的布局
            RelativeLayout view1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout
                    .news_item, null);

            //将当前的标题设置为view的tag
            view1.setTag(list_add.get(i));

            //为导航栏添加内容
            tl_new_content.addTab(tl_new_content.newTab().setText(list_add.get(i)));

            //获取当前item对应的顶栏（InnerViewPager）
            RelativeLayout ll_top = (RelativeLayout) view1.findViewById(R.id.ll_top);

            //设置所有视图的标记为不可见,.标记的是里层viewpages的状态
            ll_top.setTag(View.GONE);

            viewList.add(view1);
        }
        if (new_title[0].equals(list_add.get(0))) {
            //设置产业新闻可见
            viewList.get(0).findViewById(R.id.ll_top).setTag(View.VISIBLE);
        }


        //给外层Viewpager设置适配器
        vp_new_content.setAdapter(new News_Pageadapter(this, list_add, viewList));

        // 给已添加栏目的GridView设置适配器
        adapter = new ArrayAdapter<String>(this, R.layout.arrayadapter_item,
                list_add);
        gv_add.setAdapter(adapter);
        // 给添加栏目的GridView设置适配器
        adapter2 = new ArrayAdapter<String>(this, R.layout.arrayadapter_item,
                list_sub);
        gv_sub.setAdapter(adapter2);

    }

    /**
     * 控件初始化
     */
    public void init() {
        //外层的Viewpager
        vp_new_content = (OuterViewPager) findViewById(R.id.vp_new_content);

        //导航栏
        tl_new_content = (TabLayout) findViewById(R.id.tabLayout);

        //将导航栏和ViewPager联动起来
        tl_new_content.setupWithViewPager(vp_new_content);

        //打开下拉抽屉
        ib_new = (ImageButton) findViewById(R.id.ib_new_content);
        ib_new.setOnClickListener(mOnClickListener);

        //对导航栏进行增删操作的下拉抽屉
        drawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);

        //关闭抽屉
        ib_item_add = (ImageButton) findViewById(R.id.ib_item_add);
        ib_item_add.setOnClickListener(mOnClickListener);
        //完成导航栏的操作
        tv_new_content = (TextView) findViewById(R.id.tv_item_add);
        tv_new_content.setOnClickListener(mOnClickListener);

        gv_add = (GridView) findViewById(R.id.addItem);
        gv_add.setOnItemClickListener(mOnItemClickListener);
        gv_sub = (GridView) findViewById(R.id.subItem);
        gv_sub.setOnItemClickListener(mOnItemClickListener);
    }

    /**
     * 单击时的监听事件
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                //打开抽屉
                case R.id.ib_new_content:
                    if (!drawer.isOpened()) {
                        drawer.animateOpen();
                    }
                    break;

                //关闭抽屉
                case R.id.ib_item_add:
                    drawer.animateClose();
                    for (String s : list_issub) {
                        list_add.add(s);
                        list_sub.remove(s);
                    }
                    for (String s : list_isadd) {
                        list_sub.add(s);
                        list_add.remove(s);
                    }
                    adapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    list_isadd.clear();
                    list_issub.clear();
                    break;

                //保存修改导航栏的操作
                case R.id.tv_item_add:
                    //判断该栏目是否添加在导航栏,是则置为true
                    for (int i = 0; i < new_title.length; i++) {
                        if (list_add.contains(new_title[i])) {
                            SharedUtil.getEditor(mContext).putBoolean("add" + i, true).commit();
                        } else if (list_sub.contains(new_title[i])) {
                            SharedUtil.getEditor(mContext).putBoolean("add" + i, false).commit();
                        }
                    }
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * GridView的点击事件
     */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView
            .OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {
                //已添加栏目
                case R.id.addItem:

                    //已添加栏目,不能少于四个
                    if (list_add.size() <= 4) {
                        Toast.makeText(mContext, "不能少于四个", Toast.LENGTH_SHORT).show();
                    } else {
                        //添加要删除的项到未添加栏目的list中
                        list_sub.add(list_add.get(position));
                        if (!list_issub.contains(list_add.get(position))) {
                            list_issub.add(list_add.get(position));
                        }
                        adapter2.notifyDataSetChanged();
                        //删除已添加栏目中的该项
                        list_add.remove(position);
                        adapter.notifyDataSetChanged();
                    }

                    break;

                //未添加栏目
                case R.id.subItem:
                    //添加要删除的项到已添加栏目的list中
                    list_add.add(list_sub.get(position));
                    if (list_isadd.contains(list_sub.get(position))) {
                        list_isadd.add(list_sub.get(position));
                    }
                    adapter.notifyDataSetChanged();
                    //删除未添加栏目中的该项
                    list_sub.remove(position);
                    adapter2.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

}
