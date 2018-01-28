package com.uwei.NetUtils;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by uwei on 2018/1/26.
 */

public class OnDoubleClickListener implements View.OnTouchListener {
    private int count = 0;
    private long firClick;
    private long secClick;

    private final int interval = 1500;
    private DoubleClickCallback mCallback;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            count++;
            if (count == 1) {
                firClick = System.currentTimeMillis();
            } else if (count == 2) {
                secClick = System.currentTimeMillis();
                if (secClick - firClick < interval) {
                    mCallback.onDoubleClcik();
                    count = 0;
                    firClick = 0;
                }else{
                    firClick = secClick;
                    count = 1;
                }
                secClick = 0;
            }
        }
        return false;
    }

    public interface DoubleClickCallback {
        void onDoubleClcik();
    }

    ;

    public OnDoubleClickListener(DoubleClickCallback clickCallback) {
        super();
        this.mCallback = clickCallback;
    }


}
