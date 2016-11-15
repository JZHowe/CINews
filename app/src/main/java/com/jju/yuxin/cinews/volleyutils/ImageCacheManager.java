package com.jju.yuxin.cinews.volleyutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;


/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName ImageCacheManager
 * Created by javen.
 * Created time 15-11-2016 16:57.
 * Describe : 图片缓存管理类 获取ImageLoader对象
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class ImageCacheManager {
    private static String TAG = ImageCacheManager.class.getSimpleName();

    /**
     * 获取ImageListener
     *
     * @param view
     * @param defaultImage
     * @param errorImage
     * @return
     */
    public static ImageListener getImageListener(final ImageView view, final Bitmap defaultImage, final Bitmap errorImage) {

        return new ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // 回调失败
                if (errorImage != null) {
                    view.setImageBitmap(errorImage);
                }
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                // 回调成功
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                } else if (defaultImage != null) {
                    view.setImageBitmap(defaultImage);
                }
            }
        };

    }

    /**
     * 提供给外部调用方法
     * @param context  //上下文
     * @param url   //图片地址
     * @param view   //imageview
     * @param defaultImageResId   //正在加载的图片id
     * @param errorImageResId    //加载失败的图片id
     */
    public static void loadImage(Context context,String url, ImageView view,int defaultImageResId,int errorImageResId) {
        Bitmap defaultImage= BitmapFactory.decodeResource(context.getResources(), defaultImageResId);
        Bitmap errorImage=BitmapFactory.decodeResource(context.getResources(), errorImageResId);
        VolleyController.getInstance(context).getImageLoader().get(url, ImageCacheManager.getImageListener(view, defaultImage, errorImage), 0, 0);
    }


    /**
     * 提供给外部调用方法
     * @param context  //上下文
     * @param url      //图片地址
     * @param view    //控件
     * @param defaultImageResId  //正在加载的图片id
     * @param errorImageResId   //加载失败的图片id
     * @param maxWidth   //图片宽高,默认值0
     * @param maxHeight   //图片宽高,默认值0
     */
    public static void loadImage(Context context,String url, ImageView view,int defaultImageResId,int errorImageResId, int maxWidth, int maxHeight) {
        Bitmap defaultImage= BitmapFactory.decodeResource(context.getResources(), defaultImageResId);
        Bitmap errorImage=BitmapFactory.decodeResource(context.getResources(), errorImageResId);
        VolleyController.getInstance(context).getImageLoader().get(url, ImageCacheManager.getImageListener(view, defaultImage, errorImage), maxWidth, maxHeight);
    }

    /**
     * 提供给外部调用方法
     * @param context  //上下文
     * @param url      //图片地址
     * @param view    //控件
     * @param defaultImageResId  //正在加载的图片id
     * @param errorImageResId   //加载失败的图片id
     * @param maxWidth    //图片宽高,默认值0
     * @param maxHeight //图片宽高,默认值0
     * @param scaleType   //图片填充方式
     */
    public static void loadImage(Context context, String url, ImageView view, int defaultImageResId, int errorImageResId, int maxWidth, int maxHeight, ImageView.ScaleType scaleType) {
        Bitmap defaultImage= BitmapFactory.decodeResource(context.getResources(), defaultImageResId);
        Bitmap errorImage=BitmapFactory.decodeResource(context.getResources(), errorImageResId);
        VolleyController.getInstance(context).getImageLoader().get(url, ImageCacheManager.getImageListener(view, defaultImage, errorImage), maxWidth, maxHeight,scaleType);
    }
}
