package com.jju.yuxin.cinews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.db.DbUtils;
import com.jju.yuxin.cinews.db.Favors;
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

public class FavorList_Adapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Favors> oList;
    private boolean isDelete;
    private int i;


    public FavorList_Adapter(Context context, List<Favors> oList) {
        this.context = context;
        this.oList = oList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return oList.size();
    }

    public void replaceList(List<Favors> favorsList, boolean isDelete) {
        oList = favorsList;
        this.isDelete = isDelete;
    }

    public void removeitem(int position){
        oList.remove(position);
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
            convertView = inflater.inflate(R.layout.lv_favor_item, null);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_itme_image);
            holder.name = (TextView) convertView.findViewById(R.id.tv_itme_name);
            holder.summary = (TextView) convertView.findViewById(R.id.tv_itme_summary);
            holder.time = (TextView) convertView.findViewById(R.id.tv_itme_time);
            holder.delete = (ImageButton) convertView.findViewById(R.id.ib_itme_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //三级缓存图片
        if (oList.get(position).getImg_src() != null) {
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageCacheManager.loadImage(context, oList.get(position).getImg_src(), holder.image, R
                    .drawable.defaut_pic, R.drawable.fail_pic, 0, 0, ImageView.ScaleType.FIT_XY);
            holder.name.setText(oList.get(position).getTitle());
            holder.summary.setText(oList.get(position).getSummary());
            holder.time.setText(oList.get(position).getDate());
        } else {
            holder.image.setVisibility(View.GONE);
            holder.name.setText(oList.get(position).getTitle());
            holder.summary.setText(oList.get(position).getSummary());
            holder.time.setText(oList.get(position).getDate());

        }

        //将当前位置作为holder.delete.的tag
        holder.delete.setTag(position);

        if (isDelete) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }

        //删除按钮的点击事件
        holder.delete.setOnClickListener(mOnClickListener);
        return convertView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DbUtils.delete(oList.get((Integer) v.getTag()).getId());
            FavorList_Adapter.this.removeitem((Integer) v.getTag());
            FavorList_Adapter.this.notifyDataSetChanged();
        }
    };

    class ViewHolder {
        ImageView image;
        TextView name, summary, time;
        ImageButton delete;
    }

}
