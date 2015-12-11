package com.sky31.buy.second_hand.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by root on 15-12-7.
 */
public class ImgViewPager extends ViewPager {
    public ImgViewPager(Context context) {
        super(context);
    }

    public ImgViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // 当拦截触摸事件到达此位置的时候，返回true，
        // 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {

        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
            // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
            // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (arg0.getAction() == MotionEvent.ACTION_UP) {
            // 在up时判断是否按下和松手的坐标为一个点
            // 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        if (arg0.getAction() == MotionEvent.ACTION_CANCEL) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return super.onTouchEvent(arg0);
    }*/
}
