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
 * Created by Administrator on 2016/11/16.
 */

public class PAdapter extends BaseAdapter {
    private List<NewsBean> olist;
    private Context context;
    private LayoutInflater inflater;

    public PAdapter(Context context,List<NewsBean> olist ) {
        this.olist = olist;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return olist.size();
    }

    @Override
    public Object getItem(int position) {
        return olist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.picuture_item, null);
            viewHolder.imageView = (ImageView)view.findViewById(R.id.p_imageView);
            viewHolder.textView = (TextView)view.findViewById(R.id.p_textView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(olist.get(position).getName());
        //设置图片拉伸填充整个控件
        viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageCacheManager.loadImage(context, olist.get(position).getUrl(), viewHolder.imageView, R.drawable.picture_loading, R.drawable.picture_failure, 0, 0, ImageView.ScaleType.FIT_XY);
        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}
