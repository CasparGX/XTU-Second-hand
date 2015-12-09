package com.sky31.buy.second_hand.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by root on 15-12-7.
 */
public class BaseSwipeBackActivity extends SwipeBackActivity {
    /*滑动返回*/
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*滑动返回*/
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        //mSwipeBackLayout.setEdgeSize(HomeActivity.screenWidth);
    }


    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        float x = 0;
        float y = 0;

        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
            x = arg0.getX();
            y = arg0.getY();
            Log.i("onTouchEvent","down x:"+arg0.getX()+" y:"+arg0.getY());
        }

        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {

            Log.i("onTouchEvent","move x:"+arg0.getX()+" y:"+arg0.getY());
        }

        if (arg0.getAction() == MotionEvent.ACTION_UP) {

            Log.i("onTouchEvent","up x:"+arg0.getX()+" y:"+arg0.getY());
            x -= arg0.getX();
            if (x>0) {
                onBackPressed();
            }
        }

        if (arg0.getAction() == MotionEvent.ACTION_CANCEL) {

            Log.i("onTouchEvent","cancel x:"+arg0.getX()+" y:"+arg0.getY());
        }

        Log.i("onTouchEvent","default x:"+arg0.getX()+" y:"+arg0.getY());
        return super.onTouchEvent(arg0);
    }
}
