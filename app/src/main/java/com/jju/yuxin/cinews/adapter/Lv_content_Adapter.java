package com.jju.yuxin.cinews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jju.yuxin.cinews.R;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.adapter
 * Created by yuxin.
 * Created time 2016/11/12 0012 下午 10:44.
 * Version   1.0;
 * Describe :Listview的适配器,这是一个listview的model,
 *           对于每一个模块应该有自己的adapter,在子ListView应该编写自己的ViewHolder
 * History:
 * ==============================================================================
 */

public class Lv_content_Adapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] olist;


    public Lv_content_Adapter(Context context, String[] olist) {
        this.context = context;
        this.olist = olist;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return olist.length;
    }

    @Override
    public Object getItem(int position) {
        return olist[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         convertView = inflater.inflate(R.layout.lv_new_item,null);
        return convertView;
    }
}
