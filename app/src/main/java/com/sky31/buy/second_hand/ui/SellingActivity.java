package com.sky31.buy.second_hand.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.ui.adapter.HomeFragmentListViewAdapter;
import com.sky31.buy.second_hand.util.ACacheUtil;

import org.json.JSONArray;

import java.util.ArrayList;

import static in.srain.cube.views.ptr.util.PtrLocalDisplay.dp2px;

public class SellingActivity extends Activity {
    private String TAG = SellingActivity.class.getName();

    /*listview*/
    private SwipeMenuListView listView;
    private HomeFragmentListViewAdapter adapter;
    private ArrayList<GoodsData> mGoodsData = new ArrayList<>();

    //Cache
    private ACacheUtil mCache;
    private String mCheckCache;
    private JSONArray mCacheContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);

        //初始化控件
        initView();

        //设置侧滑菜单
        setSwipeMenu();

        //缓存
        mCache = ACacheUtil.get(this);
        mCheckCache = mCache.getAsString(Constants.Keys.KEY_CACHE_HOME_CHECK);
        //判断是否有缓存
        if (mCheckCache != null) {
            getCacheData();
        }
    }

    /*初始化控件*/
    private void initView() {
        //listView
        listView = (SwipeMenuListView) findViewById(R.id.smlv_selling_goods);
        adapter = new HomeFragmentListViewAdapter(SellingActivity.this.getLayoutInflater());
        adapter.setmGoodsData(mGoodsData);
        listView.setAdapter(adapter);
    }

    /*设置侧滑菜单*/
    private void setSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                /*// create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);*/
            }
        };

        // set creator
        listView.setMenuCreator(creator);
    }

    public void getCacheData() {

        mCacheContent = mCache.getAsJSONArray(Constants.Keys.KEY_CACHE_HOME_FIRST_CONTENT);

        mGoodsData = GoodsData.JSONArrayToGoodsData(mCacheContent);
        adapter.setmGoodsData(mGoodsData);
        adapter.notifyDataSetChanged();

        Log.i(TAG, "--------onRefreshBegin : 获取到缓存信息, 显示缓存数据---------");

    }
}
