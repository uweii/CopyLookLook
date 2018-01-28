package com.uwei.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by uwei on 2018/1/17.
 */

public class CalculateInSampleSize {
    //计算图片压缩的比例
    public static int getInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        float width = options.outWidth;
        float height = options.outHeight;
       // Log.d("utils","reqWidth ==>" + reqWidth + " ? reqHeight ===>" + reqHeight);
        //Log.d("Utils","我被调用==outWidth is ===>" + width +"\n outHeight is ==>" + height);
        int sampleSize = Math.max(Math.round(width / reqWidth), Math.round(height / reqHeight));
        return sampleSize;
    }
    //压缩图片
    public static Bitmap decodeSampleBitmapFromRes(Resources res, int resId,
                                                   int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId,options);
        int sampleSize = getInSampleSize(options,reqWidth,reqHeight);
        options.inSampleSize = sampleSize;
        Log.d("utils","sampleSize is " + sampleSize);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }


}
