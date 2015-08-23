package com.sky31.buy.second_hand.ui.fragment;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.ui.GoodsShowActivity;
import com.sky31.buy.second_hand.ui.adapter.HomeFragmentListViewAdapter;
import com.sky31.buy.second_hand.util.ACacheUtil;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class HomeFragment extends Fragment {

    private BuyApp app = new BuyApp();

    private String TAG = HomeFragment.class.getName();

    //ListView
    private ListView mListView;
    private View listViewFooter;
    private ImageView ivTips;
    private TextView tvTips;
    private HomeFragmentListViewAdapter adapter;
    private boolean isAll;
    private String loadingFlag;

    private ArrayList<GoodsData> mGoodsData = new ArrayList<>();
    private JSONArray mGoodsArray = new JSONArray();

    private int limitID = 0;
    RequestParams params = new RequestParams();


    //pull refresh
    private PtrFrameLayout ptrFrame;
    private boolean isRefresh;

    //Cache
    private ACacheUtil mCache;
    private String mCheckCache;
    private JSONArray mCacheContent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.i(TAG, "------------onCreateView");

        listViewFooter = inflater.inflate(R.layout.include_footer_goods_listview, null);
        ivTips = (ImageView) listViewFooter.findViewById(R.id.iv_tips);
        tvTips = (TextView) listViewFooter.findViewById(R.id.tv_tips);
        isAll = false;

        mListView = (ListView) view.findViewById(R.id.lv_goods);
        mListView.addFooterView(listViewFooter, null, true);
        adapter = new HomeFragmentListViewAdapter(inflater);
        mListView.setAdapter(adapter);

        //缓存
        mCache = ACacheUtil.get(getActivity());
        mCheckCache = mCache.getAsString(Constants.Keys.KEY_CACHE_HOME_CHECK);
        //mCacheContent = mCache.getAsString(Constants.Keys.KEY_CACHE_HOME_FIRST_CONTENT);
        /*if (mCheckCache!=null) {
            //Cache is not null , show the data
            Log.i(TAG,mCache.getAsString("123"));
        } else {
            //Cache is null , getGoodsData
            Log.i(TAG,"----------------Home Cache is null---------------");
            getGoodsData();
        }*/

        //判断是否有缓存
        if (mCheckCache != null) {
            getCacheData();
        }


        //下拉刷新
        isRefresh = false;
        final MaterialHeader header = new MaterialHeader(getActivity());
        ptrFrame = (PtrFrameLayout) view.findViewById(R.id.store_house_ptr_frame);

        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(30), 0, PtrLocalDisplay.dp2px(10));
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
                limitID = 0;
                isAll = false;
                getGoodsData();
                Log.i(TAG, "-------- onRefreshBegin : 刷新 - 请求网络数据 ---------");
            }
        });


        //监听listview的滚动事件
        mListView.setOnScrollListener(
                new AbsListView.OnScrollListener() {
                    private boolean isBottom = false;   //标记是否到达底部

                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                        if (isBottom && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && !isAll) {

                            if (params.has(Constants.Keys.KEY_LIMITID)) {
                                //params.remove(Constants.Keys.KEY_LIMITID);
                                params.put(Constants.Keys.KEY_LIMITID, limitID);
                                HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET
                                        , params
                                        , mJsonHttpResponseHandler);
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
                        intentGoodsShow.putExtra("goodsInfo", adapter.getItem(i));
                        startActivity(intentGoodsShow);
                        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);


                    }
                }
        );

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "------------onCreate------------------");
    }


    public void getCacheData() {

        mCacheContent = mCache.getAsJSONArray(Constants.Keys.KEY_CACHE_HOME_FIRST_CONTENT);

        mGoodsData = GoodsData.JSONArrayToGoodsData(mCacheContent);
        adapter.setmGoodsData(mGoodsData);
        adapter.notifyDataSetChanged();

        Log.i(TAG, "--------onRefreshBegin : 获取到缓存信息, 显示缓存数据---------");

    }

    /**
     * 将接受到的ArrayList参数转为RequestParams
     *
     * @param argument
     * @return
     */
    public RequestParams ArrayListToParams(ArrayList<String> argument) {

        params.put(Constants.Keys.KEY_LIMITID, limitID);
        for (int i = 0; i < argument.size(); i += 2) {
            params.put(argument.get(i), argument.get(i + 1));
        }
        return params;
    }

    public void getGoodsData() {
        Bundle bundle = getArguments();
        RequestParams params = null;
        ArrayList<String> mArgument = new ArrayList<>();
        if (bundle != null) {
            mArgument = bundle.getStringArrayList("params");
            params = ArrayListToParams(mArgument);
        }
        //RequestParams params = new RequestParams(); // 绑定参数
        //params.put(Constants.Keys.KEY_LIMITID, 0);

        Log.i(TAG, "------------URL:" + Constants.Apis.API_GOODS_LIST_GET + "------------------");
        HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET, params, mJsonHttpResponseHandler);
    }

    JsonHttpResponseHandler mJsonHttpResponseHandler = new JsonHttpResponseHandler() {

        @Override
        public void onStart() {
            super.onStart();
            setListViewFooter("loading");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "------------------------------ getGoodsData Handler onSuccess ----------------------");
            Log.i(TAG, response + "");
            //处理数据,刷新显示
            adapter.setGoodsDataEmpty();
            mGoodsArray = response;
            mGoodsData = GoodsData.JSONArrayToGoodsData(mGoodsArray);
            adapter.addAll(mGoodsData);
            adapter.notifyDataSetChanged();

            //改变缓存
            mCache.put(Constants.Keys.KEY_CACHE_HOME_FIRST_CONTENT, response);
            mCache.put(Constants.Keys.KEY_CACHE_HOME_CHECK, "hasCache");

            //改变footer状态
            setListViewFooter("end");
            limitID++;
            Log.i(TAG, mGoodsData.get(0).imgUrl + "");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            //if return JSONObject, it's have no data
            isAll = true;
            setListViewFooter("isAll");
            Toast.makeText(getActivity(), "已加载全部",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "---------------------- return JSONObject, it's have no data ------------------");
            Log.i(TAG, response + "");
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
     * 传入需要的参数，设置给arguments
     *
     * @param argument
     * @return
     */
    public static HomeFragment newInstance(ArrayList<String> argument) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("params", argument);
        HomeFragment contentFragment = new HomeFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    //设置ListView底部状态
    public void setListViewFooter(String loadingFlag) {
        switch (loadingFlag) {
            case "loading":
                //imageView移动效果
                AnimationSet animationSet = new AnimationSet(true);
                //参数1～2：x轴的开始位置
                //参数3～4：y轴的开始位置
                //参数5～6：x轴的结束位置
                //参数7～8：x轴的结束位置
                TranslateAnimation translateAnimation =
                        new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, 0f,
                                Animation.RELATIVE_TO_SELF, 0f,
                                Animation.RELATIVE_TO_SELF, -0.05f,
                                Animation.RELATIVE_TO_SELF, 0.05f);
                translateAnimation.setDuration(800);
                translateAnimation.setRepeatCount(30);
                animationSet.addAnimation(translateAnimation);
                ivTips.startAnimation(animationSet);
                tvTips.setText(getActivity().getResources().getString(R.string.loadingTips));
                break;

            case "end":
                ivTips.clearAnimation();
                tvTips.setText(getActivity().getResources().getString(R.string.endTips));
                break;

            case "isAll":
                ivTips.clearAnimation();
                tvTips.setText(getActivity().getResources().getString(R.string.isAllTips));
                break;
        }
    }

}
