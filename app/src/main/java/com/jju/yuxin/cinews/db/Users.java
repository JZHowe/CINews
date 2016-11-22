package com.jju.yuxin.cinews.db;

import org.litepal.crud.DataSupport;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.cinews.db
 * Created by yuxin.
 * Created time 2016/11/22 0022 上午 8:38.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class Users extends DataSupport {
    private int id;
    private String userid;
    private String name;
    private String img_url;

    public Users() {
    }

    public Users(String userid, String name, String img_url) {
        this.userid = userid;
        this.name = name;
        this.img_url = img_url;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
