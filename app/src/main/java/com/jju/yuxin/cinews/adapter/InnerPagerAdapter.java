package com.jju.yuxin.cinews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.activity.NewsDetailsActivity;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.volleyutils.ImageCacheManager;

import java.util.ArrayList;
import java.util.List;

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
	private List<NewsBean> picList=new ArrayList<>();
	private TextView textView;

	/**
	 * 构造函数picList传入的是图片的resid,可以更改为包含了bitmap或者resid其中一个的对象
	 * @param context
	 * @param picList
     */
	public InnerPagerAdapter(Context context, List<NewsBean> picList,TextView textView) {
		this.context = context;
		this.picList = picList;
		this.textView = textView;
	}

	//获取总数
	@Override
	public int getCount() {
		return picList.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		textView.setText(picList.get(position).getName());
	}

	//当前对象的比较
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	//为下一个即将加载的item初始化
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		// 实例化控件
		ImageView imageView = new ImageView(context);
		//给图片添加点击事件
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				String id_ = picList.get(position).getKey();
//				MyLogger.lLog().e("&**&&**&*&"+id_);
				Intent intent = new Intent(context, NewsDetailsActivity.class);
//				intent.putExtra("id",id_);
				intent.putExtra("news",picList.get(position));
				context.startActivity(intent);
			}
		});
		//设置图片拉伸填充整个控件
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		//使用图片三级缓存
		ImageCacheManager.loadImage(context, picList.get(position).getUrl(), imageView, R.drawable.picture_loading, R.drawable.picture_failure,0,0, ImageView.ScaleType.FIT_XY);
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
