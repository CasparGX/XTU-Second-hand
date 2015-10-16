package com.sky31.buy.second_hand.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.ClassifyInfo;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.ui.GoodsShowActivity;
import com.sky31.buy.second_hand.ui.adapter.ClassifyFragmentGridViewAdapter;
import com.sky31.buy.second_hand.ui.adapter.HomeFragmentListViewAdapter;
import com.sky31.buy.second_hand.util.ACacheUtil;
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

public class ClassifyFragment extends Fragment implements View.OnClickListener {

    private String TAG = ClassifyFragment.class.getName();

    /*is new classify or add more*/
    private boolean isNew = false;

    /*分类信息*/
    public static ArrayList<ClassifyInfo> mClassifyInfo = new ArrayList<>();

    /*分类信息布局*/
    private GridView mGvClassify;
    private ClassifyFragmentGridViewAdapter adapter;

    /*下拉刷新布局*/
    private PtrFrameLayout ptrFrame;

    /*商品列表*/
    private ListView mListView;
    private HomeFragmentListViewAdapter mListViewAdapter;
    private View listViewFooter;
    private ImageView ivTips;
    private TextView tvTips;
    private boolean isAll;

    /*搜索*/
    private EditText mEtSearch;
    private Button mBtnSearch;

    /*获取商品信息*/
    private ArrayList<GoodsData> mGoodsData = new ArrayList<>();
    private JSONArray mGoodsArray = new JSONArray();
    private RequestParams params = new RequestParams();
    private int limitID;
    private String queryUrl;

    //Cache
    private ACacheUtil mCache;
    private String mCheckCache;
    private JSONObject mCacheContent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        /*商品列表布局*/
        mListView = (ListView) view.findViewById(R.id.lv_goods);
        //listView' header
        View lvHeader = inflater.inflate(R.layout.include_lv_classify_header, null);
        mListView.addHeaderView(lvHeader, null, true);
        //listView's footer
        listViewFooter = inflater.inflate(R.layout.include_footer_goods_listview, null);
        ivTips = (ImageView) listViewFooter.findViewById(R.id.iv_tips);
        tvTips = (TextView) listViewFooter.findViewById(R.id.tv_tips);
        isAll = false;
        mListView.addFooterView(listViewFooter,null,false);
        //listView's adapter
        mListViewAdapter = new HomeFragmentListViewAdapter(inflater);
        mListViewAdapter.setmGoodsData(mGoodsData);
        mListView.setAdapter(mListViewAdapter);

        /*搜索框开始*/
        mEtSearch = (EditText) lvHeader.findViewById(R.id.et_search);
        //检测输入法按键
        mEtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_ENTER){
                    onBtnSearchClick();
                    return true;
                }
                return false;
            }
        });
        mBtnSearch = (Button) lvHeader.findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(this);
        /*搜索框结束*/


        /*分类布局*/
        mGvClassify = (GridView) lvHeader.findViewById(R.id.gv_classify);
        adapter = new ClassifyFragmentGridViewAdapter(inflater, mClassifyInfo);

        /*分类GridView点击事件*/
        mGvClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取点击id，传递参数
                limitID = 0;
                isAll = false;
                isNew = true;
                //params.add("limitID", String.valueOf(limitID));
                getGoodsData(null, i);
                /*Toast.makeText(getActivity()
                        ,"正在搜索: "+mClassifyInfo.get(i).getTitle()
                        ,Toast.LENGTH_SHORT)
                        .show();*/
            }
        });
        mGvClassify.setAdapter(adapter);

        //缓存
        mCache = ACacheUtil.get(getActivity());
        mCheckCache = mCache.getAsString(Constants.Keys.KEY_CACHE_CLASSIFY_CHECK);

        //下拉刷新
        /*final MaterialHeader header = new MaterialHeader(getActivity());
        ptrFrame = (PtrFrameLayout) view.findViewById(R.id.ptr_classify_grid);

        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(ptrFrame);
        ptrFrame.setPinContent(true);
        ptrFrame.setLoadingMinTime(1000);
        ptrFrame.setDurationToCloseHeader(1500);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);*/
        //get classify data
        getClassify();

        /*ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                getClassify();
                Log.i(TAG, "-------- onRefreshBegin : 刷新 - 请求网络数据 ---------");
            }
        });*/

        //监听listview的滚动事件
        mListView.setOnScrollListener(
                new AbsListView.OnScrollListener() {
                    private boolean isBottom = false;   //标记是否到达底部

                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                        if (isBottom && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && !isAll) {

                            if (params.has(Constants.Keys.KEY_LIMITID)) {
                                params.remove(Constants.Keys.KEY_LIMITID);
                                params.add(Constants.Keys.KEY_LIMITID, limitID + "");
                                isNew = false;
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

                        i -= mListView.getHeaderViewsCount();
                        Intent intentGoodsShow = new Intent();
                        intentGoodsShow.setClass(getActivity(), GoodsShowActivity.class);
                        intentGoodsShow.putExtra("goodsInfo", mListViewAdapter.getItem(i));
                        //intentGoodsShow.putExtra("goodsInfo", mListView.getAdapter().getItem(i));
                        startActivity(intentGoodsShow);
                        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);


                    }
                }
        );


        return view;
    }

    /*根据参数合成查询URL*/
    public void getGoodsData(String title, int id) {
        if (params.has(Constants.Keys.KEY_TITLE)) {
            params.remove(Constants.Keys.KEY_TITLE);
        }
        if (params.has(Constants.Keys.KEY_TYPE)) {
            params.remove(Constants.Keys.KEY_TYPE);
        }
        if (params.has(Constants.Keys.KEY_LIMITID)) {
            params.remove(Constants.Keys.KEY_LIMITID);
        }

        if (title != null){
            params.add(Constants.Keys.KEY_TITLE, title);
        }
        if (id != -1) {
            params.add(Constants.Keys.KEY_TYPE, mClassifyInfo.get(id).getId());
            //params.add("type", "2");
        }
        params.add(Constants.Keys.KEY_LIMITID, String.valueOf(limitID));
        System.out.println(params.toString() + " id = " + id);
        HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET, params, mListJsonHttpResponseHandler);
        Log.i(TAG, "------------getGoodsData URL:" + Constants.Apis.API_GOODS_LIST_GET + "------------------");
    }

    /*获取商品列表的Handler*/
    JsonHttpResponseHandler mListJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onStart() {
            super.onStart();
            BuyApp.setListViewFooter("loading", ivTips, tvTips, getActivity());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "------------------------------ getGoodsData Handler onSuccess ----------------------");
            Log.i(TAG, response + "");
            //处理数据,刷新显示
            if (isNew) {
                mGoodsData.clear();
            }
            mGoodsArray = response;
            mGoodsData.addAll(GoodsData.JSONArrayToGoodsData(mGoodsArray));
            mListViewAdapter.notifyDataSetChanged();
            if (response.length()<getActivity().getResources().getInteger(R.integer.defaultGoodsLoadCount)) {
                //商品数量未达到默认加载数量,判断为加载完毕
                BuyApp.setListViewFooter("isAll", ivTips, tvTips, getActivity());
            } else {
                //
                BuyApp.setListViewFooter("end", ivTips, tvTips, getActivity());
            }
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
                isAll = true;
                BuyApp.setListViewFooter("isNull", ivTips, tvTips, getActivity());
            }
            else {

                isAll = true;
                BuyApp.setListViewFooter("isAll", ivTips, tvTips, getActivity());
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
        //HttpUtil.get(Constants.Apis.API_GOODS_CLASSIFY_GET, null, mClassifyJsonHttpResponseHandler);
        JSONObject mClassifyData = null;
        try {
            mClassifyData = new JSONObject(Constants.Values.VALUE_CLASSIFY);
            mClassifyInfo.clear();
            JsonToClassifyInfo(mClassifyData);
            adapter.notifyDataSetChanged();
            //改变缓存
            mCache.put(Constants.Keys.KEY_CACHE_CLASSIFY_CONTENT, mClassifyData);
            mCache.put(Constants.Keys.KEY_CACHE_CLASSIFY_CHECK, "hasCache");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            //改变缓存
            mCache.put(Constants.Keys.KEY_CACHE_CLASSIFY_CONTENT, response);
            mCache.put(Constants.Keys.KEY_CACHE_CLASSIFY_CHECK, "hasCache");
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

                System.out.println(this.mClassifyInfo.get(i).getId()
                        + " # "
                        + this.mClassifyInfo.get(i).getTitle()
                        + " # "
                        + i
                        + " # "
                        + this.mClassifyInfo.size());
                i++;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //监听点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search :
                onBtnSearchClick();
                break;
        }
    }

    //搜索按钮点击事件
    private void onBtnSearchClick() {
        String keyword = String.valueOf(mEtSearch.getText());
        if (!keyword.equals("")) {
            //limitID置0
            limitID = 0;
            getGoodsData(keyword, -1);
            mEtSearch.clearFocus();
            //隐藏输入法
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
            Toast.makeText(getActivity(), "正在搜索 : "+keyword,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "请输入搜索内容",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*获取分类缓存信息*/
    public void getCacheData() {

        mCacheContent = mCache.getAsJSONObject(Constants.Keys.KEY_CACHE_CLASSIFY_CONTENT);

        JsonToClassifyInfo(mCacheContent);
        adapter.notifyDataSetChanged();

        Log.i(TAG,"--------onRefreshBegin : 获取到缓存信息, 显示缓存数据---------");

    }

}