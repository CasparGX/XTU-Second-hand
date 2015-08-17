package com.sky31.buy.second_hand.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.ClassifyInfo;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.ui.GoodsShowActivity;
import com.sky31.buy.second_hand.ui.adapter.ClassifyFragmentGridViewAdapter;
import com.sky31.buy.second_hand.ui.adapter.HomeFragmentListViewAdapter;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class ClassifyFragment extends Fragment{

    private String TAG = ClassifyFragment.class.getName();


    /*分类信息*/
    private ArrayList<ClassifyInfo> mClassifyInfo = new ArrayList<>();

    /*分类信息布局*/
    private GridView mGvClassify;
    private ClassifyFragmentGridViewAdapter adapter;

    /*下拉刷新布局*/
    private PtrFrameLayout ptrFrame;

    /*商品列表*/
    private ListView mListView;
    private HomeFragmentListViewAdapter mListViewAdapter;

    /*搜索*/
    private EditText mEtSearch;
    private Button mBtnSearch;

    /*获取商品信息*/
    private ArrayList<GoodsData> mGoodsData = new ArrayList<>();
    private JSONArray mGoodsArray = new JSONArray();
    private RequestParams params = new RequestParams();
    private int limitID;
    private String queryUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        /*商品列表布局*/
        mListView = (ListView) view.findViewById(R.id.lv_goods);
        View lvHeader = inflater.inflate(R.layout.include_lv_classify_header, null);
        mListView.addHeaderView(lvHeader, null, false);
        mListViewAdapter = new HomeFragmentListViewAdapter(inflater);
        mListViewAdapter.setmGoodsData(mGoodsData);
        mListView.setAdapter(mListViewAdapter);

        /*搜索框*/
        mEtSearch = (EditText) lvHeader.findViewById(R.id.et_search);
        mBtnSearch = (Button) lvHeader.findViewById(R.id.btn_search);

        /*分类布局*/
        mGvClassify = (GridView) lvHeader.findViewById(R.id.gv_classify);
        adapter = new ClassifyFragmentGridViewAdapter(inflater, mClassifyInfo);

        /*分类GridView点击事件*/
        mGvClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取点击id，传递参数
                //每次点击将limitID置0
                if (params.has("limitID")) {
                    params.remove("limitID");
                }
                limitID = 0;
                //params.add("limitID", String.valueOf(limitID));
                getGoodsData(null, i);
            }
        });
        mGvClassify.setAdapter(adapter);



        //下拉刷新
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
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                getClassify();
                Log.i(TAG, "-------- onRefreshBegin : 刷新 - 请求网络数据 ---------");
            }
        });

        //监听listview的滚动事件
        mListView.setOnScrollListener(
                new AbsListView.OnScrollListener() {
                    private boolean isBottom = false;   //标记是否到达底部

                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                        if (isBottom && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                            if (params.has(Constants.Keys.KEY_LIMITID)) {
                                //params.remove(Constants.Keys.KEY_LIMITID);
                                params.put(Constants.Keys.KEY_LIMITID, limitID);
                                HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET
                                        , params
                                        , mListJsonHttpResponseHandler);
                            } else {

                            }
                            isBottom = false;
                        }
                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount) {
                            isBottom = true;
                        } else {
                            isBottom = false;
                        }
                    }
                }
        );


        //监听listview的点击事件
        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i(TAG, "---------------------ListView: OnItemClick--------------------");

                        Intent intentGoodsShow = new Intent();
                        intentGoodsShow.setClass(getActivity(), GoodsShowActivity.class);
                        //Bundle bundleGoodShow = new Bundle();
                        intentGoodsShow.putExtra("goodsInfo", mListViewAdapter.getItem(i));
                        startActivity(intentGoodsShow);
                        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);


                    }
                }
        );

        return view;
    }

    /*根据参数合成查询URL*/
    public void getGoodsData(String title, int id) {
        if (title != null){
            params.add("title", title);
        }
        if (id != -1) {
            if (params.has("type")) {
                params.remove("type");
            }
            params.add("type", mClassifyInfo.get(id).getId());
        }
        params.add("limitID", String.valueOf(limitID));
        System.out.println(params.toString() + " id = " + id);
        HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET, params, mListJsonHttpResponseHandler);
        Log.i(TAG, "------------getGoodsData URL:" + Constants.Apis.API_GOODS_LIST_GET + "------------------");
    }

    /*获取商品列表的Handler*/
    JsonHttpResponseHandler mListJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "------------------------------ getGoodsData Handler onSuccess ----------------------");
            Log.i(TAG, response + "");
            //处理数据,刷新显示
            mGoodsData.clear();
            mGoodsArray = response;
            mGoodsData.addAll(GoodsData.JSONArrayToGoodsData(mGoodsArray));
            mListViewAdapter.notifyDataSetChanged();

            limitID++;
            Log.i(TAG, mGoodsData.get(0).imgUrl + "");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            //if return JSONObject, it's have no data

            if (limitID == 0) {
                mGoodsData.clear();
                mListViewAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "暂时没有此类商品上架",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), "已加载全部",
                        Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "---------------------- return JSONObject, it's have no data ------------------");
            Log.i(TAG, response + "");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.e(TAG, " onFailure" + responseString.toString());
        }

        public void onFinish() {
            Log.i(TAG, "onFinish");
        }
    };



    /*获取分类信息*/
    private void getClassify() {
        Log.i(TAG, "------------getClassify URL:" + Constants.Apis.API_GOODS_LIST_GET + "------------------");
        HttpUtil.get(Constants.Apis.API_GOODS_CLASSIFY_GET, null, mClassifyJsonHttpResponseHandler);
    }

    /*获取分类信息的Handler*/
    JsonHttpResponseHandler mClassifyJsonHttpResponseHandler = new JsonHttpResponseHandler() {
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
            Log.i(TAG, "------------------------------ getClassify Handler onSuccess ----------------------");
            mClassifyInfo.clear();
            System.out.println(response);
            JsonToClassifyInfo(response);
            adapter.notifyDataSetChanged();
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

    /**
     * 将获取的JSONObject转为自定义的ClassifyInfo对象
     * @param mJsonObject
     */
    public void JsonToClassifyInfo(JSONObject mJsonObject) {
        String key;
        int i = 0;
        for (Iterator iter = mJsonObject.keys(); iter.hasNext();) { //先遍历整个 people 对象
            key = (String)iter.next();
            try {
                ClassifyInfo mObjClassifyInfo = new ClassifyInfo();
                System.out.println(key + "#" + mJsonObject.getString(key));
                mObjClassifyInfo.setTitle(mJsonObject.getString(key));
                mObjClassifyInfo.setId(key);
                mObjClassifyInfo.setIcon(null);
                this.mClassifyInfo.add(mObjClassifyInfo);

                System.out.println(this.mClassifyInfo.get(i).getId() + " # " + this.mClassifyInfo.get(i).getTitle() + " # " + i + " # " + this.mClassifyInfo.size());
                i++;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}