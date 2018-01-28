package com.uwei.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.copylooklook.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uwei.NetUtils.Connect;
import com.uwei.NetUtils.OnDoubleClickListener;

import com.uwei.utils.SaveImg;

import java.util.List;

/**
 * Created by uwei on 2018/1/17.
 */

public class MeiriFragment extends Fragment {
    private final String TAG = "MeiriFragment";
    private RecyclerView mRecyclerView;
    private int cur = 1;
    private MeiriAdapter mMeiriAdapter;
    private boolean isFirstEnter = true;  //标志是否第一次进入
    boolean isGettingPre = false;  //标识是否正在获取之前的图片
    boolean isEnd = false;  //标志是否图片到底
    private List<String> imgUrls;
    //  private ThumbnailDownloader<MeiriAdapter.MeiriHolder> mThumbnailDownloader;

    public static Fragment newInstance() {
        return new MeiriFragment();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                Log.d("MeiriFragment", "Handler接受到消息");
                String url = (String) msg.obj;
                Log.d("message===>", url);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMeiriAdapter = new MeiriAdapter();
        AsyncTask<Object, Integer, List<String>> task = new AsyncTask<Object, Integer, List<String>>() {
            @Override
            protected List<String> doInBackground(Object... objects) {
                return Connect.getMeiriImgUrls(getActivity(), String.valueOf(cur));
            }

            @Override
            protected void onPostExecute(List<String> imgs) {
                super.onPostExecute(imgs);
                imgUrls = imgs;
                mRecyclerView.setAdapter(mMeiriAdapter);
            }
        };
        task.execute();
       /* Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setTThumbnailDownloadListener(new ThumbnailDownloader.ThumbnailDownloadListener<MeiriAdapter.MeiriHolder>() {
            @Override
            public void onThumbnailDownloaded(MeiriAdapter.MeiriHolder target, Bitmap thumbnail) {
             // target.mImageView.setImageBitmap(thumbnail);
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG,"background thread started");*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wangyifragmennt, container, false);
        mRecyclerView = v.findViewById(R.id.recyler_view);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        BitmapFactory.Options options = new BitmapFactory.Options();
        //mBitmap = CalculateInSampleSize.decodeSampleBitmapFromRes(getResources(),R.drawable.wangyiimg,600,600);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "itemCount==> " + manager.getItemCount() + "\n current item position==> " + manager.findLastVisibleItemPosition());
                //  Log.i(TAG,"isEnd==> "+isEnd);

                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                        >= recyclerView.computeVerticalScrollRange() && isEnd) {
                    Toast.makeText(getActivity(), "没有更多的图片啦", Toast.LENGTH_SHORT).show();
                }


                if (isGettingPre) {
                    return;
                }
                if (manager.getItemCount() - manager.findLastVisibleItemPosition() < 3) {
                    final int preLength = imgUrls.size();
                    isGettingPre = true;
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {
                            return Connect.getPreMeiriUrls(String.valueOf(++cur));
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                            imgUrls = (List<String>) o;
                            if (preLength == imgUrls.size()) {
                                isEnd = true;
                                return;
                            }
                            mMeiriAdapter.notifyDataSetChanged();
                            isGettingPre = false;
                        }
                    };
                    task.execute();
                }
            }
        });
        return v;
    }

    private class MeiriAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.meiri_layout, parent, false);
            return new MeiriHolder(v);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            //((MeiriHolder)holder).mImageView.setImageBitmap(mBitmap);
//            Message msg = mHandler.obtainMessage(0x123,imgUrls.get(position));
//            mHandler.sendMessage(msg);
            //  mThumbnailDownloader.queueThumbnail((MeiriHolder) holder,imgUrls.get(position));
            // Picasso.with(getActivity()).setIndicatorsEnabled(true);  //用于显示图片从哪里加载【网络，内存，磁盘】
            Picasso.with(getActivity()).load(Uri.parse(imgUrls.get(position)))
                    .fit().centerCrop().into(((MeiriHolder) holder).mImageView);
            /*
             * 为什么在这加了fit(),和centerCrop()
             *
            */
            ((MeiriHolder) holder).mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getActivity(),"点击照片",Toast.LENGTH_SHORT).show();
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.bigimg, null);
                    ImageView imageView = view.findViewById(R.id.img_container);
                    Log.i(TAG, "拿到的URI是==》：" + imgUrls.get(position));
                    Picasso.with(getActivity()).load(Uri.parse(imgUrls.get(position))).into(imageView);
                    AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.Dialog_Fullscreen).setView(view)
                            .setPositiveButton("保存", null)
                            .setNegativeButton("返回", null).create();

                    //这里如果加了.fit()和centerCrop()会不显示图片
                    imageView.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
                        @Override
                        public void onDoubleClcik() {
                            saveImg(position);
                        }
                    }));

                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Log.i(TAG, "url is ==> " + imgUrls.get(position));
                            saveImg(position);
                            return true;
                        }
                    });

                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getActivity(),"保存",Toast.LENGTH_SHORT).show();
                            saveImg(position);
                        }
                    });
                    if (isFirstEnter) {
                        Toast.makeText(getActivity(), "长按图片可以保存哟", Toast.LENGTH_SHORT).show();
                        isFirstEnter = false;
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return imgUrls.size();
        }

        public class MeiriHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;

            public MeiriHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.iv_meiri);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // mThumbnailDownloader.clearQueue();
    }

    public void saveImg(final int position) {
        if (SaveImg.hasPermission(getActivity())) {
            Picasso.with(getActivity()).load(imgUrls.get(position)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.i(TAG, "load from==>" + from);
                    boolean hasSaved = SaveImg.saveImg(bitmap, "每日看看-" + String.valueOf(position) + ".jpeg", getActivity());
                    if (hasSaved) {
                        Toast.makeText(getActivity(), "已保存至相册^.^", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        } else {
            Log.i(TAG,"无权限");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "已获取权限,可以保存图片", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "您拒绝了写文件权限，无法保存图片", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
