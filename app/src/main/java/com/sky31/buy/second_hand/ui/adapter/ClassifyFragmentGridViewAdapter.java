package com.sky31.buy.second_hand.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.model.ClassifyInfo;

import java.util.ArrayList;

/**
 * Created by 63024 on 2015/8/10 0010.
 */
public class ClassifyFragmentGridViewAdapter extends BaseAdapter {

    private String TAG = ClassifyFragmentGridViewAdapter.class.getName();

    private LayoutInflater inflater;
    private ArrayList<ClassifyInfo> data;

    private TextView tvTitle;

    public ClassifyFragmentGridViewAdapter(LayoutInflater inflater, ArrayList<ClassifyInfo> data) {
        this.inflater = inflater;
        this.data = data;
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
        }
        tvTitle.setText(data.get(i).getTitle());
        Log.i("ClassifyFragmentGridViewAdapter",data.get(i).getTitle() + " # " + i + " # " + getItem(i).getId() + " # " + getCount());
        return view;
    }
}
