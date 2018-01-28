package com.example.sharepres;

import android.content.Context;
import android.content.SharedPreferences;

import com.uwei.NetUtils.Connect;

/**
 * Created by uwei on 2018/1/17.
 */

public class SharePres {
    public static final String Pre_MENU = "menu";
    public static final String Pre_MENUID = "menuId";
    public static final String LATESTNEWS = "latestnews";
    public static final String LATESTNEWSID= "id";
    public static final String LATESTIMGID = "imgId";

    //记录之前菜单选项
    public static void setMenuId(Context context, int id){
        SharedPreferences preferences = context.getSharedPreferences(Pre_MENU,Context.MODE_PRIVATE);
        preferences.edit().putInt(Pre_MENUID,id).apply();
    }
    public static int getMenuId(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Pre_MENU,Context.MODE_PRIVATE);
        return preferences.getInt(Pre_MENUID,0);
    }

    //记录最新的news id
    public static void setLatestNewsId(Context context, String id){
        SharedPreferences preferences = context.getSharedPreferences(LATESTNEWS,Context.MODE_PRIVATE);
        preferences.edit().putString(LATESTNEWSID,id).apply();
    }
    public static String getLatestNewsId(Context context){
        SharedPreferences preferences = context.getSharedPreferences(LATESTNEWS,Context.MODE_PRIVATE);
        return preferences.getString(LATESTNEWSID,"0");
    }

    public static void setLatestImgId(Context context, String id){
        SharedPreferences preferences = context.getSharedPreferences(LATESTNEWS,Context.MODE_PRIVATE);
        preferences.edit().putString(LATESTIMGID,id).apply();
    }
    public static String getLatestImgId(Context context){
        SharedPreferences preferences = context.getSharedPreferences(LATESTNEWS,Context.MODE_PRIVATE);
        return preferences.getString(LATESTIMGID,"0");
    }

}
