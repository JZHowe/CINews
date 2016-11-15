package com.jju.yuxin.cinews.volleyutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.volleydome
 * Created by yuxin.
 * Created time 2016/11/8 0008 上午 10:28.
 * Version   1.0;
 * Describe : Volley封装好的工具类
 * History:
 * ==============================================================================
 */

public class VolleyUtils {

    private RequestQueue queue;
    private ReusltListener listener;

    //构造方法
    public VolleyUtils(Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
    }

    /**
     * 封装好的Volley的get请求
     * url是请求地址
     * tag是请求的标志,可以为null
     */
    public VolleyUtils doGetRequest(String url, String tag) {
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.Result(response, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.Result(null, error);
                    }
                });
        if (tag != null) {
            stringRequest.setTag(tag);
        }
        queue.add(stringRequest);
        return this;
    }

    /**
     * Volley的post请求
     *  url是请求地址
     *  omap是要传入的数据,键值对的形式
     *  tag是请求的标志,可以为null
     */

    public VolleyUtils doPostRequest(String url, final Map<String, String> omap, String tag) {

        StringRequest post_requst = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.Result(response, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.Result(null, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return omap;

            }
        };
        if (tag != null) {
            post_requst.setTag(tag);
        }
        queue.add(post_requst);
        return this;
    }


    /**
     * 图片请求
     *  url的请求地址
     *  maxWidth最大宽度,默认值为0
     *  maxHeight最大高度,默认值为0
     *  scaleType图片填充方式  如:ImageView.ScaleType.CENTER_INSIDE
     *  decodeConfig 图片格式 如:Bitmap.Config.ARGB_8888
     *  tag是请求的标志,可以为null
     * @param url
     * @param tag
     * @return
     */
    public VolleyUtils doImageRequest(String url, int maxWidth, int maxHeight
            , ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, String tag) {

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        listener.Result(response, null);
                    }
                }, maxWidth, maxHeight, scaleType, decodeConfig,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.Result(null, error);
                    }
                });
        if (tag != null) {
            imageRequest.setTag(tag);
        }
        queue.add(imageRequest);
        return this;
    }

    /**
     * 图片加载操作  <推荐的加载方式>
     * 将图片加载到指定的imageview
     * url 图片地址
     * view 控件
     * defaultImageResId 默认图片
     * errorImageResId 加载错误的图片
     * @param url
     * @param view
     * @param defaultImageResId
     * @param errorImageResId
     */
    public void doImageLoaderToImageView(String url,ImageView view,int defaultImageResId,int errorImageResId){

        final Map<String, Bitmap> omap = new HashMap<>();

        ImageLoader imageLoader = new ImageLoader(queue,
               new  BitmapCache());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(view,defaultImageResId,errorImageResId);
        imageLoader.get(url, imageListener);

    }

    /**
     * <请不要修改这段代码>
     * 用于实现图片的三级缓存!
     */

    public class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = (int) (Runtime.getRuntime().maxMemory()/1024/8);
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }





    //接口回调
    public interface ReusltListener {
        void Result(Object sucessinfo, VolleyError error);
    }


    public void getReusltListener(ReusltListener listener) {
        this.listener = listener;
    }


}
