package com.jju.yuxin.cinews.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName InnerPagerAdapter
 * Created by yuxin.
 * Created time 13-11-2016 00:03.
 * Describe :自定义的新闻模块里层的viewpager
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */

public class InnerPagerAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<Integer> picList;

	/**
	 * 构造函数picList传入的是图片的resid,可以更改为包含了bitmap或者resid其中一个的对象
	 * @param context
	 * @param picList
     */
	public InnerPagerAdapter(Context context, ArrayList<Integer> picList) {
		this.context = context;
		this.picList = picList;
	}

	//获取总数
	@Override
	public int getCount() {
		return picList.size();
	}

	//当前对象的比较
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	//为下一个即将加载的item初始化
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 实例化控件
		ImageView imageView = new ImageView(context);
		//设置图片拉伸填充整个控件
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		//设置图片资源
		imageView.setImageResource(picList.get(position));
		//将控件放置到容器中
		container.addView(imageView);
		return imageView;
	}

	//移除除了自己本身和自己左右的的item
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// super.destroyItem(container, position, object);
		container.removeView((View) object);
	}
}
