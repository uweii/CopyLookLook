package com.uwei.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copylooklook.NewsInfoActivity;
import com.example.copylooklook.R;
import com.uwei.NetUtils.CheckNetWork;
import com.uwei.NetUtils.Connect;
import com.uwei.po.News;
import com.uwei.utils.CalculateInSampleSize;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by uwei on 2018/1/16.
 */

public class ZhihuFragment extends Fragment {
    private Bitmap mBitmap;
    private List<News> mNews;
    private RecyclerView mRecyclerView;
    private ZhihuAdapter mAdapter;
    private boolean isGettingPre = false;
    private ProgressDialog mDialog;
    public static String[][] mDataSet = {
            {"樱花落下的速度是每秒五厘米，我该用怎样的速度，才能与你相遇", "秒速五厘米"},
            {"雨滴降落的速度是每秒十米，我该用怎样的速度，才能将你挽留", "言叶之庭"},
            {"陨石坠落的速度是每秒十千米，我该用怎样的速度，才能将你拯救", "你的名字"},
            {"把所有的 '我不会' 都变成 '我可以学'", "2fab4u"},
            {"天天敲码身体棒", "xxx"},
            {"天天吃肉长不胖", "xxx"},
            {"徒手写一千行代码测试无bug", "xxx"},
            {"第一次见一个人，体温在38.6°，就叫一见钟情", "网络句"},
            {"生当复来归，死当长相思", "苏轼"},
            {"那两颗靠得最近星星就是我和你呢。", "网络句"},
            {"who do you have a crush on", "----"},
            {"All that i care about is you.", "----"}
    };

    public static Fragment newInstance() {
        return new ZhihuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new ProgressDialog(getActivity());
        mDialog.setTitle("加载中");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        Log.d("ZhihuFragmwnt","onCreate()");
        if(!CheckNetWork.checkNet(getActivity())){
            Toast.makeText(getActivity(),"网络连接失败...",Toast.LENGTH_SHORT).show();

        }
        mDialog.show();
        mAdapter = new ZhihuAdapter();
        final AsyncTask newsTask = new AsyncTask() {
            @Override
            protected List<News> doInBackground(Object[] objects) {
               mNews = Connect.getLatestNews(getActivity(),Connect.LATEST_URI);
               return mNews;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                mRecyclerView.setAdapter(mAdapter);
                mDialog.dismiss();
                Log.d("mNews length===>", ""+mNews.size());
            }
        };
       // newsTask.execute();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("ZhihuFragment","计时器在执行===");
                if(CheckNetWork.checkNet(getActivity())){
                    newsTask.execute();
                    this.cancel();
                }
            }
        },0,1000);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ZhihuFragment","onCreateView()");
        View v = inflater.inflate(R.layout.zhihufragment, container, false);
        mRecyclerView = v.findViewById(R.id.recyler_view);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        BitmapFactory.Options options = new BitmapFactory.Options();
        mBitmap = CalculateInSampleSize.decodeSampleBitmapFromRes(getResources(),R.drawable.g,240,200);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL)); //设置显示item的分割线
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isGettingPre){
                    return;
                }
                Log.d("item的长度",""+ manager.getItemCount()+"\n 当前itemindex: " + manager.findLastVisibleItemPosition());
               if(manager.getItemCount() -manager.findLastVisibleItemPosition() < 2){
                 isGettingPre = true;
                   mDialog.show();
                   Log.d("Zhihu","只剩下4个item了");
                   AsyncTask task = new AsyncTask() {
                       @Override
                       protected List<News> doInBackground(Object[] objects) {
                           mNews = Connect.getPreNews();
                           //mAdapter.notifyDataSetChanged()
                           return mNews;
                       }
                       @Override
                       protected void onPostExecute(Object o) {
                           super.onPostExecute(o);
                           Log.d("重新加载mnews==：",""+mNews.size());
                           Log.d("count===> " ,""+manager.getItemCount()+"////当前item下标===> " +manager.findLastVisibleItemPosition());
                           mAdapter.notifyDataSetChanged();
                           isGettingPre = false;
                            mDialog.dismiss();
                       }
                   };
                   task.execute();
               }
            }
        });
        return v;
    }

    private class ZhihuAdapter extends RecyclerView.Adapter<ZhihuAdapter.ViewHolder> {
        @Override
        public ZhihuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity())
                    .inflate(R.layout.zhihu_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.mImageView.setImageBitmap(mNews.get(position).getBitmap());
            holder.mTitle.setText(mNews.get(position).getTitle());
            holder.mFrom.setText(mNews.get(position).getId());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(),"点击了recylerview",Toast.LENGTH_SHORT).show();
                   // News n = Connect.newsDetail(mNews.get(position));
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected String doInBackground(Object[] objects) {
                            String data = Connect.newsDetail(mNews.get(position));
                            return data;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                            Intent intent = new Intent(getActivity(), NewsInfoActivity.class);
                            intent.putExtra("data",String.valueOf(o));
                            startActivity(intent);
                        }
                    };
                    task.execute();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mNews.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            private TextView mTitle;
            private TextView mFrom;

            public ViewHolder(View itemView) {
                //super();
                super(itemView);
                mImageView = itemView.findViewById(R.id.zhihu_img);
                mTitle = itemView.findViewById(R.id.zhihu_title);
                mFrom = itemView.findViewById(R.id.zhihu_from);
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"点击了recylerview",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getActivity(), NewsInfoActivity.class);
//                        startActivity(intent);
//
//                    }
//                });
            }
        }

    }


}
