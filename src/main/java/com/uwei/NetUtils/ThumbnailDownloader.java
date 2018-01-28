package com.uwei.NetUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by uwei on 2018/1/24.
 */

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private Boolean hasQuit = false;
    private static final int MESSAGE_DOWNLOADER = 0;
    private Handler mRequestHandler;
    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mTThumbnailDownloadListener;

    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setTThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mTThumbnailDownloadListener = listener;
    }

    private ConcurrentHashMap<T, String> mRequestMap = new ConcurrentHashMap<>();

    public ThumbnailDownloader(Handler requestHandler) {
        super(TAG);
        mResponseHandler = requestHandler;
    }

    @Override
    public boolean quit() {
        hasQuit = true;
        return super.quit();
    }

    public void queueThumbnail(T target, String url) {
       // Log.i(TAG, "Got a url==> " + url);
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOADER, target).sendToTarget();
        }
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOADER) {
                    T target = (T) msg.obj;
                 //   Log.i(TAG, "Got a request for URL==>: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    private void handleRequest(final T target) {
        final String url = mRequestMap.get(target);
        if (url == null) {
            return;
        }
        final Bitmap bitmap = getBitmapFromUrl(url);
        Log.i(TAG, "Bitmap created");
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mRequestMap.get(target) != url || hasQuit) {
                    return;
                }
                mRequestMap.remove(target);
                mTThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
            }
        });
    }

    private Bitmap getBitmapFromUrl(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearQueue(){
        mResponseHandler.removeMessages(MESSAGE_DOWNLOADER);
        mRequestMap.clear();
    }
}
