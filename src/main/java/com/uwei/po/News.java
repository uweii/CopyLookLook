package com.uwei.po;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by uwei on 2018/1/18.
 */

public class News {


        /**
         * images : ["https://pic3.zhimg.com/v2-6412410a5a2b264601e5f4829579e4c6.jpg"]
         * type : 0
         * id : 9666161
         * ga_prefix : 011809
         * title : 一打开窗全是「墓地」，买到这种房，恐怕告开发商也难赢
         * multipic : true
         */

        private String id;
        private String title;
        private Bitmap mBitmap;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


    public Bitmap getBitmap() {
        return mBitmap;
    }

    public News(String id, String title, Bitmap bitmap) {
        this.id = id;
        this.title = title;
        mBitmap = bitmap;
    }

         /* detail news  attribute      */
    private String imgUrl;
    private String bodyCss;
    private String body;
        /* detail news attribute    */

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBodyCss() {
        return bodyCss;
    }

    public void setBodyCss(String bodyCss) {
        this.bodyCss = bodyCss;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
