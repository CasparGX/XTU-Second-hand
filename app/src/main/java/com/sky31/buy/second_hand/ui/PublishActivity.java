package com.sky31.buy.second_hand.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.ClassifyInfo;
import com.sky31.buy.second_hand.ui.fragment.ClassifyFragment;
import com.sky31.buy.second_hand.util.HttpUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class PublishActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = PublishActivity.class.getName();


    private Intent mIntent;

    /*header*/
    private ImageView ivBackBtn;
    private TextView tvHeaderTitle;

    /*滑动返回*/
    private SwipeBackLayout mSwipeBackLayout;

    /*填写内容部分*/
    private Spinner spnClassify;
    private Spinner spnTrading;
    private Spinner spnBargain;
    private Spinner spnTime;
    private ArrayList<ClassifyInfo> mClassifyInfo = new ArrayList<>();
    private ArrayList<String> mClassifyInfoTitle = new ArrayList<>();
    private ArrayList<String> mTrading = new ArrayList<>();
    private ArrayList<String> mBargain = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private EditText etGoodsTitle;
    private EditText etGoodsDec;
    private EditText etGoodsPrice;
    private EditText etNickname;
    private EditText etPhone;
    private EditText etQq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        //Intent
        mIntent = getIntent();

        //设置状态栏颜色
        BuyApp.setStatusBarColor(this);

        /*滑动返回*/
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        /*header*/
        ivBackBtn = (ImageView) findViewById(R.id.iv_back_btn);
        ivBackBtn.setOnClickListener(this);
        tvHeaderTitle = (TextView) findViewById(R.id.tv_header_title);
        setTvHeaderTitle(); //修改header标题

        /*EditText*/
        etGoodsDec = (EditText) findViewById(R.id.et_goods_dec);
        etGoodsTitle = (EditText) findViewById(R.id.et_goods_title);
        etGoodsPrice = (EditText) findViewById(R.id.et_goods_price);
        etNickname = (EditText) findViewById(R.id.et_nickname);
        etPhone = (EditText) findViewById(R.id.et_phone_num);
        etQq = (EditText) findViewById(R.id.et_qq);
        setSellerInfo();

        /* start - 分类spinner */
        mClassifyInfo = ClassifyFragment.mClassifyInfo;
        for (int i = 0; i< mClassifyInfo.size(); i++) {
            mClassifyInfoTitle.add(mClassifyInfo.get(i).getTitle());
        }
        spnClassify = (Spinner) findViewById(R.id.spn_classify);
        //将可选内容与ArrayAdapter连接起来
        ArrayAdapter<String> classifySpnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mClassifyInfoTitle);
        //设置下拉列表的风格
        classifySpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spnClassify.setAdapter(classifySpnAdapter);
        //添加事件Spinner事件监听
        spnClassify.setOnItemSelectedListener(new ClassifySpinnerSelectedListener());
        //设置默认值
        spnClassify.setVisibility(View.VISIBLE);
        /* end - 分类spinner */

        /* start - trading spinner */
        mTrading.add("自取");
        mTrading.add("送货上门");
        spnTrading = (Spinner) findViewById(R.id.spn_goods_trading);
        //将可选内容与ArrayAdapter连接起来
        ArrayAdapter<String> tradingSpnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTrading);
        //设置下拉列表的风格
        tradingSpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spnTrading.setAdapter(tradingSpnAdapter);
        //添加事件Spinner事件监听
        spnTrading.setOnItemSelectedListener(new TradingSpinnerSelectedListener());
        //设置默认值
        spnTrading.setVisibility(View.VISIBLE);
        /* end - trading spinner */

        /* start - bargain spinner */
        mBargain.add("是");
        mBargain.add("否");
        spnBargain = (Spinner) findViewById(R.id.spn_goods_bargain);
        //将可选内容与ArrayAdapter连接起来
        ArrayAdapter<String> bargainSpnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mBargain);
        //设置下拉列表的风格
        bargainSpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spnBargain.setAdapter(bargainSpnAdapter);
        //添加事件Spinner事件监听
        spnBargain.setOnItemSelectedListener(new BargainSpinnerSelectedListener());
        //设置默认值
        spnBargain.setVisibility(View.VISIBLE);
        /* end - bargain spinner */

        /* start - time spinner */
        mTime.add("30");
        mTime.add("15");
        mTime.add("7");
        spnTime = (Spinner) findViewById(R.id.spn_goods_time);
        //将可选内容与ArrayAdapter连接起来
        ArrayAdapter<String> timeSpnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTime);
        //设置下拉列表的风格
        timeSpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spnTime.setAdapter(timeSpnAdapter);
        //添加事件Spinner事件监听
        spnTime.setOnItemSelectedListener(new TimeSpinnerSelectedListener());
        //设置默认值
        spnTime.setVisibility(View.VISIBLE);
        /* end - time spinner */
    }

    /*返回键*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_out);
    }

    /*修改header标题*/
    private void setTvHeaderTitle() {
            String title = mIntent.hasExtra("headerTitle")
                    ? mIntent.getStringExtra("headerTitle") : getResources().getString(R.string.app_title);
            tvHeaderTitle.setText(title);
    }

    /*填充卖家信息*/
    private void setSellerInfo() {
        String seller = mIntent.getStringExtra("seller");
        String phone = mIntent.getStringExtra("phone");
        String qq = mIntent.getStringExtra("qq");
        etNickname.setText(seller);
        etPhone.setText(phone);
        etQq.setText(qq);
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

    private void insert() {
        /*上传图片测试样例*/
        File file = new File("/mnt/sdcard/","home.jpg");
        RequestParams params = new RequestParams();
        if(file.exists() && file.length()>0) {
            try {
                params.put("file1", file);
                params.put("file2", file);
                params.put("file3", file);
                Log.i(TAG, Environment.getExternalStorageDirectory().toString());
                HttpUtil.post(Constants.Apis.API_GOODS_APPINSERT_POST
                        , params
                        , mInsertHandler);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            Log.i(TAG, "文件不存在");
        }
    }

    /*insert商品handler*/
    JsonHttpResponseHandler mInsertHandler = new JsonHttpResponseHandler();

    /*分类信息spinner*/
    class ClassifySpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            Toast.makeText(PublishActivity.this,mClassifyInfo.get(arg2).getId()+"",Toast.LENGTH_SHORT).show();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /*trading spinner*/
    private class TradingSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            Toast.makeText(PublishActivity.this,(arg2+1)+"",Toast.LENGTH_SHORT).show();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /*bargain spinner*/
    private class BargainSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            if (mBargain.get(arg2).equals("是")) {
                Toast.makeText(PublishActivity.this,1+"",Toast.LENGTH_SHORT).show();
            } else if (mBargain.get(arg2).equals("否")) {
                Toast.makeText(PublishActivity.this,0+"",Toast.LENGTH_SHORT).show();
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /*time spinner*/
    private class TimeSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
                Toast.makeText(PublishActivity.this,mTime.get(arg2),Toast.LENGTH_SHORT).show();

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}

