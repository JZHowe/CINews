package com.jju.yuxin.cinews.volleyutils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.jju.yuxin.cinews.utils.MD5Utils;
import com.jju.yuxin.cinews.utils.MyLogger;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName ImageCacheUtil
 * Created by javen.
 * Created time 15-11-2016 16:57.
 * Describe : * 图片缓存帮助类
 * 包含内存缓存LruCache和磁盘缓存DiskLruCache
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */
public class ImageCacheUtil implements ImageCache {

    private String TAG = ImageCacheUtil.this.getClass().getSimpleName();

    //缓存类
    private static LruCache<String, Bitmap> mLruCache;

    private static DiskLruCache mDiskLruCache;
    //磁盘缓存大小
    private static final int DISKMAXSIZE = 20 * 1024 * 1024;

    private Context context;

    private WindowManager manager;

    private int width,height;

    public ImageCacheUtil(Context context) {
        this.context = context;
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        // 获取应用可占内存的1/8作为缓存
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        // 实例化LruCaceh对象
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
        try {
            // 获取DiskLruCahce对象
//            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(MyApplication.newInstance(), "xxxxx"), getAppVersion(MyApplication.newInstance()), 1, DISKMAXSIZE);
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(context.getApplicationContext(), "CINews"), getAppVersion(context), 1, DISKMAXSIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从缓存（内存缓存，磁盘缓存）中获取Bitmap
     */
    @Override
    public Bitmap getBitmap(String url) {
        if (mLruCache.get(url) != null) {
            // 从LruCache缓存中取
            MyLogger.hLog().i("从LruCahce获取");
            return mLruCache.get(url);
        } else {
            String key = MD5Utils.md5(url);
            try {
                if (mDiskLruCache.get(key) != null) {
                    // 从DiskLruCahce取
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    Bitmap bitmap = null;
                    if (snapshot != null) {
                        bitmap = decodeImage(snapshot,key);
                        // 存入LruCache缓存
                        mLruCache.put(url, bitmap);
                        MyLogger.hLog().i("从DiskLruCahce获取");
                    }
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //解析图片像素大小
    public Bitmap decodeImage(DiskLruCache.Snapshot snapshot,String key){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //获取图片信息但不解析
        opt.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0),null,opt);
        //重新打开一次inputstream
        snapshot.close();
        try {
            snapshot = mDiskLruCache.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取网络图片高宽
        int bit_width = opt.outWidth;
        int bit_height = opt.outHeight;
        //计算网络图片与屏幕的高宽比
        int x = bit_width/(width/3);
        int y = bit_height/(height/8);
        //缩放比例 默认为1
        int p = 1;
        if(x>1){
            if(y>1){
                if(x>y){
                    p=x;
                }else {
                    p=y;
                }
            }else{
                p=x;
            }
        }else {
            if(y>1){
                p=y;
            }else {
                p=1;
            }
        }
        //设置成解析图片
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = p;
        //解析流资源
        bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0),null,opt);
        MyLogger.zLog().e(p+"^^^"+width+"^^^^"+height+"^^^^"+bit_width+"^^^^"+bit_height+"^^^^^"+bitmap+"^^^^^"+snapshot);
        return bitmap;
    }

    /**
     * 存入缓存（内存缓存，磁盘缓存）
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        // 存入LruCache缓存
        mLruCache.put(url, bitmap);
        // 判断是否存在DiskLruCache缓存，若没有存入
        String key = MD5Utils.md5(url);
        try {
            if (mDiskLruCache.get(key) == null) {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (bitmap.compress(CompressFormat.JPEG, 100, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 该方法会判断当前sd卡是否存在，然后选择缓存地址
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}