package com.sky31.buy.second_hand.ui.activity;

import android.os.Bundle;
import android.view.MotionEvent;

import com.sky31.buy.second_hand.ui.HomeActivity;

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
        }

        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {

        }

        if (arg0.getAction() == MotionEvent.ACTION_UP) {
            x -= arg0.getX();
            if (x>0) {
                onBackPressed();
            }
        }

        if (arg0.getAction() == MotionEvent.ACTION_CANCEL) {

        }

        return super.onTouchEvent(arg0);
    }
}
