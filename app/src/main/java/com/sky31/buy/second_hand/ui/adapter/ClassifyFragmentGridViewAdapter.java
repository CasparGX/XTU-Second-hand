package com.sky31.buy.second_hand.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.model.ClassifyInfo;
import com.sky31.buy.second_hand.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 63024 on 2015/8/10 0010.
 */
public class ClassifyFragmentGridViewAdapter extends BaseAdapter {

    private String TAG = ClassifyFragmentGridViewAdapter.class.getName();

    private LayoutInflater inflater;
    private ArrayList<ClassifyInfo> data;

    private TextView tvTitle;
    private ImageView ivClassify;

    private List<Integer> classifyIcon = new ArrayList<>();

    public ClassifyFragmentGridViewAdapter(LayoutInflater inflater, ArrayList<ClassifyInfo> data) {
        this.inflater = inflater;
        this.data = data;
        classifyIcon.add(R.drawable.classify_icon1);
        classifyIcon.add(R.drawable.classify_icon2);
        classifyIcon.add(R.drawable.classify_icon3);
        classifyIcon.add(R.drawable.classify_icon4);
        classifyIcon.add(R.drawable.classify_icon5);
        classifyIcon.add(R.drawable.classify_icon6);
        classifyIcon.add(R.drawable.classify_icon7);
        classifyIcon.add(R.drawable.classify_icon8);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ClassifyInfo getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_classify_gridview,viewGroup ,false);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            ivClassify = (ImageView) view.findViewById(R.id.iv_classify);
            final LinearLayout.LayoutParams iconParams = (LinearLayout.LayoutParams) ivClassify.getLayoutParams();
            iconParams.height = HomeActivity.screenWidth / 6;
            ivClassify.setLayoutParams(iconParams);
        }
        tvTitle.setText(data.get(i).getTitle());
        ivClassify.setImageResource(
                classifyIcon.get(
                        Integer.parseInt(
                                data.get(i).getId()
                        ) - 1
                )
        );
        Log.i("ClassifyFragmentGridViewAdapter",data.get(i).getTitle() + " # " + i + " # " + getItem(i).getId() + " # " + getCount());
        return view;
    }
}
