package com.sky31.buy.second_hand.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.ui.adapter.ClassifyFragmentGridViewAdapter;

import java.util.HashMap;

public class ClassifyFragment extends Fragment {

    private HashMap<String, String> classifyTest = new HashMap<String, String>();

    private GridView mGvClassify;
    private BaseAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classifyTest.put( "0" , "全部商品");
        classifyTest.put( "1" , "图书教材");
        classifyTest.put( "2" , "数码产品");
        classifyTest.put( "3" , "代步工具");
        classifyTest.put( "4" , "运动器材");
        classifyTest.put( "5" , "衣物衣帽");
        classifyTest.put("6" , "家用电器");
        classifyTest.put("7" , "租赁");
        classifyTest.put("8" , "其他");
        classifyTest.put("9" , "爱淘节");
        classifyTest.put("10", "爱心义卖");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        mGvClassify = (GridView) view.findViewById(R.id.gv_classify);
        adapter = new ClassifyFragmentGridViewAdapter(inflater, classifyTest);
        mGvClassify.setAdapter(adapter);
        return view;
    }

}