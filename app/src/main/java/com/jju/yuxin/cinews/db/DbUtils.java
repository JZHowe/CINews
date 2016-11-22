package com.jju.yuxin.cinews.db;

import android.util.Log;

import com.jju.yuxin.cinews.bean.FavorBean;
import com.jju.yuxin.cinews.bean.NewsBean;
import com.jju.yuxin.cinews.bean.VedioInfoBean;

import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Howe on 2016/11/17.
 */

public class DbUtils {

    /**
     * 根据新闻id查找
     */
    public static List<Favors> searchFavor(NewsBean newsBean) {
        List<Favors> mFavorsList;
        if (newsBean.getKey() == null) {
            mFavorsList = DataSupport.where("news_id=?", newsBean.getId()).find
                    (Favors.class);
        } else {
            mFavorsList = DataSupport.where("key=?", newsBean.getKey()).find
                    (Favors.class);
        }
        return mFavorsList;
    }

    /**
     * 根据视频id查找
     */
    public static List<Favors> searchVideoFavor(VedioInfoBean vedioInfoBean) {
        List<Favors> mFavorsList;

        mFavorsList = DataSupport.where("news_id=?", String.valueOf(vedioInfoBean.getId())).find
                (Favors.class);

        return mFavorsList;
    }

    /**
     * 存储新闻
     */
    public static void saveFavor(FavorBean mFavorBean) {
        Favors mFavors = new Favors();
        mFavors.setImg_src(mFavorBean.getImg_src());
        mFavors.setTitle(mFavorBean.getTitle());
        mFavors.setSummary(mFavorBean.getSummary());
        mFavors.setNews_id(mFavorBean.getNews_id());
        mFavors.setDate(mFavorBean.getDate());
        mFavors.setKey(mFavorBean.getKey());
        mFavors.setVideo_src(mFavorBean.getVideo_src());
        mFavors.setType(mFavorBean.getType());
        mFavors.setUserid(mFavorBean.getUserid());
        mFavors.save();
    }

    /**
     * 删除视频收藏
     */
    public static void deleteVideoFavor(VedioInfoBean vedioInfoBean) {

        DataSupport.deleteAll(Favors.class, "news_id = ?", String.valueOf(vedioInfoBean.getId()));

    }

    /**
     * 根据id删除项
     */
    public static void delete(int id){
        DataSupport.delete(Favors.class,id);
    }

    /**
     * 删除新闻收藏
     */
    public static void deleteFavor(NewsBean newsBean) {
        if (newsBean.getKey() == null) {
            DataSupport.deleteAll(Favors.class, "news_id = ?", newsBean.getId());
        } else {
            DataSupport.deleteAll(Favors.class, "key = ?", newsBean.getKey());
        }
    }

    /**
     * 查询所有
     */
    public static List<Favors> searchFavor(String userid) {

        List<Favors> mFavorsList;

        mFavorsList = DataSupport.where("userid=?", String.valueOf(userid)).find
                (Favors.class);

        return mFavorsList;
    }
    public static List<Favors> searchAllFavor() {
        List<Favors> mFavorsList = DataSupport.findAll(Favors.class);
        return mFavorsList;
    }

    public static void saveUser(Users user){
        //之前是否已经登录过了
        ClusterQuery clusterQuery = DataSupport.select("*").where("userid=?",user.getUserid());
        //如果存在,那么将其删除
        if (clusterQuery!=null||clusterQuery.count(Users.class)>0) {
            DataSupport.deleteAll(Users.class,"userid=?",user.getUserid());
        }
        //最新的用户信息更新到数据库中去
        user.save();
    }


}
