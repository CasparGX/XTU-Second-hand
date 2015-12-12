package com.sky31.buy.second_hand.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.sky31.buy.second_hand.R;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by root on 15-12-7.
 */
public class BaseSwipeBackActivity extends SwipeBackActivity {

    private float x = 0;
    private float startX = 0;
    private float y = 0;
    private long time = 0;
    private int flag = 0;

    private int maxSwipeY = 180;
    private int maxStartX = 150;
    private double swipeCoefficient = 2.2;

    /*滑动返回*/
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*滑动返回*/
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        //mSwipeBackLayout.setEdgeSize(HomeActivity.screenWidth);

        this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = x = event.getX();
            y = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x = event.getX() - x;
            y = Math.abs(event.getY() - y);
            time = event.getEventTime()-event.getDownTime();
            Log.i("ontouch",time/x+"");
            if (y<maxSwipeY && x>0 && (time/x)<swipeCoefficient && flag==0 && startX > maxStartX) {
                onBackPressed();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}