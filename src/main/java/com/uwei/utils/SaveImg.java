package com.uwei.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by uwei on 2018/1/25.
 */

public class SaveImg {

    public static boolean saveImg(Bitmap bitmap, String name, Context context) {
        try {
            String sdcardPath = System.getenv("EXTERNAL_STORAGE");  //获得sd卡路径
            String dir = sdcardPath + "/每日看看/";       //图片保存的文件夹名
            File file = new File(dir);          //已File来构建
            if (!file.exists()) {    //如果不存在  就mkdirs()创建此文件夹
                file.mkdirs();
            }
            Log.i("SaveImg", "file uri==>" + dir);
            File mFile = new File(dir + name);   //将要保存的图片文件
            if (mFile.exists()) {
                Toast.makeText(context, "该图片已存在!", Toast.LENGTH_SHORT).show();
                return false;
            }
            FileOutputStream outputStream = new FileOutputStream(mFile);  //构建输出流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);  //compress到输出outputStream
            Uri uri = Uri.fromFile(mFile);   //获得图片的uri
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)); //发送广播通知更新图库，这样系统图库可以找到这张图片
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasPermission(Context context){   //检测是否有写sd卡权限
        int result = ContextCompat
                .checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
