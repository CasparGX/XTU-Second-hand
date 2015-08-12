package com.sky31.buy.second_hand.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.ui.adapter.HomeViewPagerAdapter;
import com.sky31.buy.second_hand.ui.fragment.ClassifyFragment;
import com.sky31.buy.second_hand.ui.fragment.HomeFragment;
import com.sky31.buy.second_hand.ui.fragment.MeFragment;
import com.sky31.buy.second_hand.ui.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;


/**
 * Created by Caspar on 2015/7/30.
 */
public class HomeActivity extends FragmentActivity implements View.OnClickListener {

    private SwipeBackLayout mSwipeBackLayout;

    private ViewPager mViewPager;
    private HomeViewPagerAdapter mAdpater;
    private List<Fragment> mDatas;

    private List<TextView> mListTv;
    private TextView tvShouye;
    private TextView tvFenlei;
    private TextView tvSousuo;
    private TextView tvWo;

    private ImageView ivFooterBg;

    private ImageView mTabLine;
    private int mScreen1_3;

    private int mCurrentPageIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //get screen's width 1/4
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetris = new DisplayMetrics();
        display.getMetrics(outMetris);
        mScreen1_3 = outMetris.widthPixels / 3;

        initTabLine();
        initView();
        initFooterBg();
    }

    private void initFooterBg() {
        ivFooterBg = (ImageView) findViewById(R.id.iv_footer_bg);
        final ViewGroup.LayoutParams lp = ivFooterBg.getLayoutParams();

        ViewTreeObserver vto = tvShouye.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvShouye.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                lp.height = tvShouye.getHeight();
            }
        });

        lp.width = mScreen1_3;

        ivFooterBg.setLayoutParams(lp);
        Log.i("HomeActivity", tvShouye.getHeight() + "");
    }

    private void initTabLine() {
        mTabLine = (ImageView) findViewById(R.id.iv_tabline);
        ViewGroup.LayoutParams lp = mTabLine.getLayoutParams();
        lp.width = mScreen1_3;
        mTabLine.setLayoutParams(lp);
    }

    private void initView() {

        tvShouye = (TextView) findViewById(R.id.tv_shouye);
        tvFenlei = (TextView) findViewById(R.id.tv_fenlei);
        //tvSousuo = (TextView) findViewById(R.id.tv_sousuo);
        tvWo = (TextView) findViewById(R.id.tv_wo);
        tvShouye.setOnClickListener(this);
        tvFenlei.setOnClickListener(this);
        //tvSousuo.setOnClickListener(this);
        tvWo.setOnClickListener(this);
        mListTv = new ArrayList<TextView>();
        mListTv.add(tvShouye);
        mListTv.add(tvFenlei);
        //mListTv.add(tvSousuo);
        mListTv.add(tvWo);
        mListTv.get(0).setTextColor(0xffff3621);

        mViewPager = (ViewPager) findViewById(R.id.vp_home);
        mViewPager.setOffscreenPageLimit(4);
        mDatas = new ArrayList<Fragment>();

        ArrayList<String> params = new ArrayList<>();
        //params.add(Constants.Keys.KEY_LIMITID);
        //params.add("0");
        HomeFragment mHomeFragment = HomeFragment.newInstance(params);
        ClassifyFragment mClassifyFragment = new ClassifyFragment();
        SearchFragment mSearchFragment = new SearchFragment();
        MeFragment mMeFragment = new MeFragment();
        mDatas.add(mHomeFragment);
        mDatas.add(mClassifyFragment);
        mDatas.add(mSearchFragment);
        mDatas.add(mMeFragment);
        mAdpater = new HomeViewPagerAdapter(getSupportFragmentManager(), mDatas);
        mViewPager.setAdapter(mAdpater);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPx) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTabLine.getLayoutParams();
                RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) ivFooterBg.getLayoutParams();

                if(mCurrentPageIndex == 0 && position ==0 ){ //0->1
                    lp.leftMargin = (int) (positionOffset * mScreen1_3
                            + mCurrentPageIndex * mScreen1_3);
                    lp2.leftMargin = (int) (positionOffset * mScreen1_3
                            + mCurrentPageIndex * mScreen1_3);
                } else if(mCurrentPageIndex == 1 && position == 0) {    //1->0
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + (positionOffset-1) * mScreen1_3);
                    lp2.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + (positionOffset-1) * mScreen1_3);
                } else if (mCurrentPageIndex == 1 && position == 1) { //1->2
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + positionOffset * mScreen1_3);
                    lp2.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + positionOffset * mScreen1_3);
                } else if (mCurrentPageIndex == 2 && position == 1) { //2->1
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + (positionOffset - 1) * mScreen1_3);
                    lp2.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + (positionOffset - 1) * mScreen1_3);
                } else if (mCurrentPageIndex == 2 && position == 2) { //2->3
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + positionOffset * mScreen1_3);
                    lp2.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + positionOffset * mScreen1_3);
                } else if (mCurrentPageIndex == 3 && position == 2) { //3->2
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + (positionOffset - 1) * mScreen1_3);
                    lp2.leftMargin = (int) (mCurrentPageIndex * mScreen1_3
                            + (positionOffset - 1) * mScreen1_3);
                }
                Log.i("HomeActivity",ivFooterBg.getWidth()
                        + " : "
                        + ivFooterBg.getHeight()
                        + " : "
                        + ivFooterBg.getTop()
                        + " : "
                        +ivFooterBg.getLeft());
                mTabLine.setLayoutParams(lp);
                ivFooterBg.setLayoutParams(lp2);
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPageIndex = position;
                setListTvColorToSame();
                mListTv.get(position).setTextColor(0xffff3621);

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });
    }

    public void setListTvColorToSame() {
        for (int i = 0; i < mListTv.size(); i++) {
            mListTv.get(i).setTextColor(0xa9ee3f1a);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shouye :
                mViewPager.setCurrentItem(0,true);
                break;
            case R.id.tv_fenlei :
                mViewPager.setCurrentItem(1,true);
                break;
            /*case R.id.tv_sousuo :
                mViewPager.setCurrentItem(2,true);
                break;*/
            case R.id.tv_wo :
                mViewPager.setCurrentItem(3,true);
                break;

        }
    }
}
