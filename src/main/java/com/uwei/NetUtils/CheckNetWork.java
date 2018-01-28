package com.uwei.NetUtils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by uwei on 2018/1/21.
 */

public class CheckNetWork {
    //private static Context mContext;
    public static boolean checkNet(Context context){
        //mContext = context;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo()!=null){
            return connectivityManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
