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
        classifyTest.put( "0" , "ȫ����Ʒ");
        classifyTest.put( "1" , "ͼ��̲�");
        classifyTest.put( "2" , "�����Ʒ");
        classifyTest.put( "3" , "��������");
        classifyTest.put( "4" , "�˶�����");
        classifyTest.put( "5" , "������ñ");
        classifyTest.put("6" , "���õ���");
        classifyTest.put("7" , "����");
        classifyTest.put("8" , "����");
        classifyTest.put("9" , "���Խ�");
        classifyTest.put("10", "��������");
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