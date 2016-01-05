package com.sky31.buy.second_hand.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.ui.HomeActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsShowFragment extends Fragment {

    int windowWidth;

    //private ImageView imgPic;
    private TextView tvTitle;

    private ViewPager imgPicViewPager;
    //private ArrayList<ImageView> imgList;
    private PagerAdapter imgListAdapter;
    //private ImageView imgPic01;
    //private ImageView imgPic03;
    //private ImageView imgPic02;
    private GoodsData goods;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;


    public GoodsShowFragment() {
        // Required empty public constructor
        this.imageLoader = BuyApp.getInstance().getImageLoader();
        this.displayImageOptions = BuyApp.getInstance().getDisplayImageOptions();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        goods = intent.getParcelableExtra("goodsInfo");

        //set ImageView's width and height as windows' width
        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windowWidth = displayMetrics.widthPixels;
        imgPic = (ImageView) findViewById(R.id.imgPic);
        LayoutParams linearParams = imgPic.getLayoutParams();
        linearParams.height = windowWidth;
        linearParams.width = windowWidth;
        imgPic.setLayoutParams(linearParams);
        imgPic.requestLayout();
        app.getImageLoad(goods.imgUrlArray.get(0), imgPic);*/

        //init tvTitle
        tvTitle = (TextView) getActivity().findViewById(R.id.title);
        tvTitle.setText(goods.title);

        this.windowWidth = HomeActivity.windowWidth;

        imgPicViewPager = (ViewPager) getActivity().findViewById(R.id.imgPicViewPager);
        LayoutParams linearParams = imgPicViewPager.getLayoutParams();
        linearParams.height = windowWidth;
        linearParams.width = windowWidth;
        imgPicViewPager.setLayoutParams(linearParams);
        imgPicViewPager.requestLayout();

        imgListAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return goods.imgUrlArray.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(View container, int position) {

                ImageView imageView = new ImageView(getActivity());
                imageLoader.displayImage(goods.imgUrl
                                + Constants.Values.VALUE_MIN_SIZE_PRE
                                + goods.imgUrlArray.get(position)
                        , imageView
                        , displayImageOptions);

                ((ViewPager) container).addView(imageView, 0);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                object = null;
            }
        };

        imgPicViewPager.setAdapter(imgListAdapter);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goods_show, container, false);
    }


}
