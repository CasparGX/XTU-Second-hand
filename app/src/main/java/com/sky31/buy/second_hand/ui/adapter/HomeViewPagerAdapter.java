package com.sky31.buy.second_hand.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Caspar on 2015/7/30.
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mDatas;

    public HomeViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> mDatas) {
        super(fragmentManager);
        this.mDatas = mDatas;
    }

    @Override
    public Fragment getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
