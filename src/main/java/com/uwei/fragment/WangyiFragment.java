package com.uwei.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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


import com.example.copylooklook.R;
import com.uwei.utils.CalculateInSampleSize;

/**
 * Created by uwei on 2018/1/17.
 */

public class WangyiFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private Context mContext;
    private Bitmap mBitmap;
    public static Fragment newInstance() {
        return new WangyiFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wangyifragmennt, container, false);
        mRecyclerView = v.findViewById(R.id.recyler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        BitmapFactory.Options options = new BitmapFactory.Options();
        mBitmap = CalculateInSampleSize.decodeSampleBitmapFromRes(getResources(),R.drawable.wangyiimg,240,200);
        mRecyclerView.setAdapter(new WangyiAdapter());
        return v;
    }

    private class WangyiAdapter extends RecyclerView.Adapter<WangyiAdapter.WangyiHolder> {

        @Override
        public WangyiAdapter.WangyiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.wangyi_list_item, parent, false);
            return new WangyiHolder(v);
        }

        @Override
        public void onBindViewHolder(WangyiHolder holder, int position) {
            holder.mImageView.setImageBitmap(mBitmap);
            holder.mTitle.setText(ZhihuFragment.mDataSet[position][0]);
            holder.mFrom.setText(ZhihuFragment.mDataSet[position][1]);
        }


        @Override
        public int getItemCount() {
            return ZhihuFragment.mDataSet.length;
        }

        public class WangyiHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            public TextView mTitle, mFrom;

            public WangyiHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.zhihu_img);
                mTitle = itemView.findViewById(R.id.zhihu_title);
                mFrom = itemView.findViewById(R.id.zhihu_from);
            }
        }
    }
}
