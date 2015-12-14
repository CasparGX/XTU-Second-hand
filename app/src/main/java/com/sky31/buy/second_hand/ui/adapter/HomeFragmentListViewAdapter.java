package com.sky31.buy.second_hand.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;

import java.util.ArrayList;

/**
 * Created by Caspar on 2015/7/31.
 */
public class HomeFragmentListViewAdapter extends BaseAdapter {

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

    private LayoutInflater inflater;

    private ArrayList<GoodsData> mGoodsData = new ArrayList<>();

    private TextView title;
    private TextView price;
    private TextView seller;
    private TextView dec;
    private ImageView icon;

    private class ViewHolder {
        public ImageView image;
    }

    public ArrayList<GoodsData> getmGoodsData() {
        return mGoodsData;
    }

    public void setmGoodsData(ArrayList<GoodsData> mGoodsData) {
        this.mGoodsData = mGoodsData;
    }

    public void setGoodsDataEmpty() {
        this.mGoodsData.clear();
    }

    public void addAll(ArrayList<GoodsData> mGoodsData) {
        this.mGoodsData.addAll(mGoodsData);
    }

    public HomeFragmentListViewAdapter(ArrayList<GoodsData> mGoodsData) {
        this.mGoodsData = mGoodsData;
    }

    public HomeFragmentListViewAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        this.imageLoader = BuyApp.getInstance().getImageLoader();
        this.displayImageOptions = BuyApp.getInstance().getDisplayImageOptions();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GoodsData data;
        data = getItem(i);
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_home_goods, viewGroup, false);

            //icon = (ImageView) view.findViewById(R.id.icon);
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.icon);
            view.setTag(holder);
            /*final LayoutParams iconParams = icon.getLayoutParams();
            ViewTreeObserver vto2 = icon.getViewTreeObserver();
            vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    icon.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    iconParams.height = icon.getWidth();
                }
            });
            icon.setLayoutParams(iconParams);*/

        } else {
            holder = (ViewHolder) view.getTag(); // 把数据取出来
        }
        title = (TextView) view.findViewById(R.id.title);
        price = (TextView) view.findViewById(R.id.price);
        icon = (ImageView) view.findViewById(R.id.icon);
        seller = (TextView) view.findViewById(R.id.seller);
        dec = (TextView) view.findViewById(R.id.dec);

        imageLoader.displayImage(data.imgUrl
                + Constants.Values.VALUE_MIN_SIZE_PRE
                + data.imgUrlArray.get(0)
                , holder.image
                , this.displayImageOptions);


        dec.setText(data.dec);
        title.setText(data.title);
//        title.setText(Build.VERSION.RELEASE);
        price.setText(data.price + "");
//        price.setText(Build.VERSION.SDK);
        seller.setText(data.seller);
//        seller.setText(Build.MODEL);


        return view;
    }

    @Override
    public int getCount() {
        return mGoodsData.size();
    }

    @Override
    public GoodsData getItem(int i) {
        return mGoodsData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
