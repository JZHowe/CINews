package com.jju.yuxin.cinews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.volleyutils.ImageCacheManager;

import java.util.List;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.adapter
 * Created by yuxin.
 * Created time 2016/11/12 0012 下午 10:44.
 * Version   1.0;
 * Describe :Listview的适配器,这是一个listview的model,
 * 对于每一个模块应该有自己的adapter,在子ListView应该编写自己的ViewHolder
 * History:
 * ==============================================================================
 */

public class Lv_content_Adapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<NewsBean> oList;


    public Lv_content_Adapter(Context context, List<NewsBean> oList) {
        this.context = context;
        this.oList = oList;
        inflater = LayoutInflater.from(context);
    }
    public void replaceList(List<NewsBean> oList) {
        this.oList = oList;
    }

    @Override
    public int getCount() {
        return oList.size();
    }

    @Override
    public Object getItem(int position) {
        return oList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.lv_new_item, null);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_itme_image);
            holder.name = (TextView) convertView.findViewById(R.id.tv_itme_name);
            holder.summary = (TextView) convertView.findViewById(R.id.tv_itme_summary);
            holder.time = (TextView) convertView.findViewById(R.id.tv_itme_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //三级缓存图片
        if (oList.get(position).getUrl()!=null) {
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageCacheManager.loadImage(context, oList.get(position).getUrl(), holder.image, R.drawable.picture_loading, R.drawable.picture_failure, 0, 0, ImageView.ScaleType.FIT_XY);
            holder.name.setText(oList.get(position).getName());
            holder.summary.setText(oList.get(position).getSummary());
            holder.time.setText(oList.get(position).getTime());
        }else {
            holder.image.setVisibility(View.GONE);
            holder.name.setText(oList.get(position).getName());
            holder.summary.setText(oList.get(position).getSummary());
            holder.time.setText(oList.get(position).getTime());

        }
        return convertView;
    }

    class ViewHolder {
        ImageView image;
        TextView name, summary, time;

    }


}
