package com.sky31.buy.second_hand.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sky31.buy.second_hand.R;

import java.util.HashMap;
import java.util.zip.Inflater;

/**
 * Created by 63024 on 2015/8/10 0010.
 */
public class ClassifyFragmentGridViewAdapter extends BaseAdapter {

    private String TAG = ClassifyFragmentGridViewAdapter.class.getName();

    private LayoutInflater inflater;
    private HashMap<String, String> data;

    private TextView tvTitle;

    public ClassifyFragmentGridViewAdapter(LayoutInflater inflater, HashMap<String, String> data) {
        this.inflater = inflater;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    public Object getItem(String i) {
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
        }
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText("123");
        //Log.i(TAG,this.data.get(i).toString());
        return view;
    }
}
