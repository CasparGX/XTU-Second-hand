package com.sky31.buy.second_hand.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.ui.adapter.ClassifyFragmentGridViewAdapter;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class ClassifyFragment extends Fragment {

    private String TAG = ClassifyFragment.class.getName();

    private HashMap<String, String> classifyTest = new HashMap<String, String>();
    private ArrayList<JSONObject> classifyData = new ArrayList<>();

    private GridView mGvClassify;
    private ClassifyFragmentGridViewAdapter adapter;

    private PtrFrameLayout ptrFrame;
    private boolean isRefresh;

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

        //下拉刷新
        isRefresh = false;
        final MaterialHeader header = new MaterialHeader(getActivity());
        ptrFrame = (PtrFrameLayout) view.findViewById(R.id.ptr_classify_grid);

        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(ptrFrame);
        ptrFrame.setPinContent(true);
        ptrFrame.setLoadingMinTime(1000);
        ptrFrame.setDurationToCloseHeader(1500);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.autoRefresh(true);
            }
        }, 100);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mGvClassify, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                adapter.setGoodsDataEmpty();
                getClassify();
                Log.i(TAG, "-------- onRefreshBegin : 刷新 - 请求网络数据 ---------");
            }


        });


        return view;
    }

    private void getClassify() {
        Log.i(TAG, "------------URL:" + Constants.Apis.API_GOODS_LIST_GET + "------------------");
        HttpUtil.get(Constants.Apis.API_GOODS_CLASSIFY_GET, null, mJsonHttpResponseHandler);
    }
    JsonHttpResponseHandler mJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Toast.makeText(getActivity(), "加载分类信息出错",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "---------------getClassify Error: response JSONArray, need JSONObject----------------");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "------------------------------ getGoodsData Handler onSuccess ----------------------");
            for (Iterator iter = response.keys(); iter.hasNext();) { //先遍历整个 people 对象
                String key = (String)iter.next();
                try {
                    System.out.println(key + "#" + response.getString(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.e(TAG, " onFailure" + responseString.toString());
        }

        public void onFinish() {
            ptrFrame.refreshComplete();
            Log.i(TAG, "onFinish");
        }
    };

}