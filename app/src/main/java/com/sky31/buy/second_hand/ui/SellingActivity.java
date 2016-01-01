package com.sky31.buy.second_hand.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.ui.adapter.HomeFragmentListViewAdapter;
import com.sky31.buy.second_hand.util.ACacheUtil;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Handler;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import static in.srain.cube.views.ptr.util.PtrLocalDisplay.dp2px;

public class SellingActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = SellingActivity.class.getName();

    /*Intent*/
    private Intent mIntent;

    /*uid*/
    private String uid;

    /*diaLog*/
    private AlertDialog.Builder builderLoading;
    private ProgressDialog mProgressDialog;

    /*网络参数*/
    private RequestParams params = new RequestParams();
    private RequestParams soldOutParams = new RequestParams();
    private int limitID;

    /*header*/
    private ImageView ivBackBtn;
    private TextView tvHeaderTitle;

    /*滑动返回*/
    private SwipeBackLayout mSwipeBackLayout;

    /*listview*/
    private SwipeMenuListView listView;
    private View listViewFooter;
    private ImageView ivTips;
    private TextView tvTips;
    private boolean isAll;
    private HomeFragmentListViewAdapter adapter;
    private ArrayList<GoodsData> mGoodsData = new ArrayList<GoodsData>();
    private JSONArray mGoodsArray = new JSONArray();
    private int itemPosition;

    private TextView tvTipsLeftSwipe;

    //Cache
    private ACacheUtil mCache;
    private String mCheckCache;
    private JSONArray mCacheContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);

        //Intent
        mIntent = getIntent();
        //获取uid
        uid = mIntent.getStringExtra("uid");
        limitID = 0;

        //设置状态栏颜色
        BuyApp.setStatusBarColor(this);

        /*滑动返回*/
        /*mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);*/

        /*header*/
        ivBackBtn = (ImageView) findViewById(R.id.iv_back_btn);
        ivBackBtn.setOnClickListener(this);
        tvHeaderTitle = (TextView) findViewById(R.id.tv_header_title);
        setTvHeaderTitle(); //修改header标题

        //初始化控件
        initView();

        //设置侧滑菜单
        setSwipeMenu();

        //缓存
        /*mCache = ACacheUtil.get(this);
        mCheckCache = mCache.getAsString(Constants.Keys.KEY_CACHE_HOME_CHECK);
        //判断是否有缓存
        if (mCheckCache != null) {
            getCacheData();
        }*/

        //加载数据
        getUserGoods();
    }

    /*修改header标题*/
    private void setTvHeaderTitle() {
        String title = mIntent.hasExtra("headerTitle")
                ? mIntent.getStringExtra("headerTitle") : getResources().getString(R.string.app_title);
        tvHeaderTitle.setText(title);
    }

    /*初始化控件*/
    private void initView() {
        //tips
        tvTipsLeftSwipe = (TextView) findViewById(R.id.tv_tips_leftswipe);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvTipsLeftSwipe.setVisibility(View.GONE);
            }
        },10000);
        //listView
        listView = (SwipeMenuListView) findViewById(R.id.smlv_selling_goods);
        //listView's footer
        listViewFooter = getLayoutInflater().inflate(R.layout.include_footer_goods_listview, null);
        ivTips = (ImageView) listViewFooter.findViewById(R.id.iv_tips);
        tvTips = (TextView) listViewFooter.findViewById(R.id.tv_tips);
        isAll = false;
        listView.addFooterView(listViewFooter, null, false);
        //listView's adapter
        adapter = new HomeFragmentListViewAdapter(SellingActivity.this.getLayoutInflater());
        adapter.setmGoodsData(mGoodsData);
        listView.setAdapter(adapter);

        //监听listview的滚动事件
        listView.setOnScrollListener(
                new AbsListView.OnScrollListener() {
                    private boolean isBottom = false;   //标记是否到达底部

                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                        if (isBottom && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && !isAll) {

                            if (params.has(Constants.Keys.KEY_LIMITID)) {
                                params.remove(Constants.Keys.KEY_LIMITID);
                                params.add(Constants.Keys.KEY_LIMITID, limitID + "");
                                HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET
                                        , params
                                        , mUserGoodsJsonHandler);
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
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i(TAG, "---------------------ListView: OnItemClick--------------------");

                        i -= listView.getHeaderViewsCount();
                        Intent intentGoodsShow = new Intent();
                        intentGoodsShow.setClass(SellingActivity.this, GoodsShowActivity.class);
                        intentGoodsShow.putExtra("goodsInfo", adapter.getItem(i));
                        intentGoodsShow.putExtra("headerTitle", "商品详情");
                        //intentGoodsShow.putExtra("goodsInfo", mListView.getAdapter().getItem(i));
                        startActivity(intentGoodsShow);
                        SellingActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);


                    }
                }
        );
    }

    /*设置侧滑菜单*/
    private void setSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                editItem.setWidth(dp2px(120));
                // set item title
                editItem.setTitle("修改");
                // set item title fontsize
                editItem.setTitleSize(18);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem cancelItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                cancelItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                cancelItem.setWidth(dp2px(120));
                // set item title
                cancelItem.setTitle("下架");
                // set item title fontsize
                cancelItem.setTitleSize(18);
                // set item title font color
                cancelItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(cancelItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        // listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // 修改
                        editGoodsInfo(position);
                        break;
                    case 1:
                        // 下架
                        goodsSoldOut(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    private void editGoodsInfo(int i) {
        Intent intentEditGoodsInfo = new Intent();
        intentEditGoodsInfo.setClass(SellingActivity.this, EditGoodsInfoActivity.class);
        //intentEditGoodsInfo.putExtra("uid", userInfo.getString("id"));
        intentEditGoodsInfo.putExtra("goodsInfo", adapter.getItem(i));
        intentEditGoodsInfo.putExtra("headerTitle", "修改商品信息");
        startActivity(intentEditGoodsInfo);
        SellingActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    /*下架商品*/
    private void goodsSoldOut(int position) {
        if (soldOutParams.has(Constants.Keys.KEY_GID)) {
            soldOutParams.remove(Constants.Keys.KEY_GID);
        }
        itemPosition = position;
        soldOutParams.add(Constants.Keys.KEY_GID, adapter.getItem(position).id + "");
        HttpUtil.get(Constants.Apis.API_GOODS_SOLDOUT_GET, soldOutParams, mSoldOutJsonHandler);
    }

    /*获取缓存数据*/
    public void getCacheData() {

        mCacheContent = mCache.getAsJSONArray(Constants.Keys.KEY_CACHE_HOME_FIRST_CONTENT);

        mGoodsData = GoodsData.JSONArrayToGoodsData(mCacheContent);
        adapter.setmGoodsData(mGoodsData);
        adapter.notifyDataSetChanged();

        Log.i(TAG, "--------onRefreshBegin : 获取到缓存信息, 显示缓存数据---------");

    }

    /*点击事件*/
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back_btn:
                //返回按钮
                onBackPressed();
                break;
        }
    }

    /*获取网络数据*/
    public void getUserGoods() {
        if (params.has(Constants.Keys.KEY_LIMITID)) {
            params.remove(Constants.Keys.KEY_LIMITID);
        }
        if (params.has(Constants.Keys.KEY_UID)) {
            params.remove(Constants.Keys.KEY_UID);
        }
        params.add(Constants.Keys.KEY_LIMITID, limitID + "");
        params.add(Constants.Keys.KEY_UID, uid);
        HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET, params, mUserGoodsJsonHandler);
        //HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET+"limitID=0", null, mUserGoodsJsonHandler);
    }

    /*用户商品handler*/
    JsonHttpResponseHandler mUserGoodsJsonHandler = new JsonHttpResponseHandler() {
        @Override
        public void onStart() {
            super.onStart();
            BuyApp.setListViewFooter("loading", ivTips, tvTips, SellingActivity.this);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "------------------------------ getGoodsData Handler onSuccess ----------------------");
            Log.i(TAG, response + "");
            //处理数据,刷新显示
            mGoodsData.clear();
            mGoodsArray = response;
            mGoodsData.addAll(GoodsData.JSONArrayToGoodsData(mGoodsArray));
            adapter.notifyDataSetChanged();
            if (response.length() < SellingActivity.this.getResources().getInteger(R.integer.defaultGoodsLoadCount)) {
                //商品数量未达到默认加载数量,判断为加载完毕
                BuyApp.setListViewFooter("isAll", ivTips, tvTips, SellingActivity.this);
            } else {
                //
                BuyApp.setListViewFooter("end", ivTips, tvTips, SellingActivity.this);
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
                adapter.notifyDataSetChanged();
                isAll = true;
                BuyApp.setListViewFooter("isNull", ivTips, tvTips, SellingActivity.this);
            } else {

                isAll = true;
                BuyApp.setListViewFooter("isAll", ivTips, tvTips, SellingActivity.this);
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

    /*商品下架,修改handler*/
    JsonHttpResponseHandler mSoldOutJsonHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, response + "");
            try {

                if (response.getString("result").equals("success")) {
                    //操作成功
                    Toast.makeText(SellingActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    mGoodsData.remove(itemPosition);
                    adapter.notifyDataSetChanged();
                } else if (response.getString("result").equals("error")) {
                    //操作失败
                    Toast.makeText(SellingActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.e(TAG, " onFailure" + responseString.toString());
        }

        @Override
        public void onStart() {
            super.onStart();
            showLoadingDialog();
        }

        public void onFinish() {
            hideLoadingDialog();
            Log.i(TAG, "onFinish");
        }
    };

    /*隐藏加载dialog*/
    private void hideLoadingDialog() {
        mProgressDialog.dismiss();
    }

    /*显示加载dialog*/
    private void showLoadingDialog() {
        /*builderLoading = new AlertDialog.Builder(this);
        builderLoading.setMessage("请稍后...")
                .setCancelable(false)
                .create();
        dialog = builderLoading.show();*/
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("请稍后...");
        //mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }


}
