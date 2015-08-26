package com.sky31.buy.second_hand.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.util.HttpUtil;

import java.io.File;
import java.io.FileNotFoundException;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class PublishActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = PublishActivity.class.getName();

    /*header*/
    private ImageView ivBackBtn;
    private TextView tvHeaderTitle;

    /*滑动返回*/
    private SwipeBackLayout mSwipeBackLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_out);
    }

    /*修改header标题*/
    private void setTvHeaderTitle() {
        Intent mIntent = getIntent();
            String title = mIntent.hasExtra("headerTitle")
                    ? mIntent.getStringExtra("headerTitle") : getResources().getString(R.string.app_title);
            tvHeaderTitle.setText(title);
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
}
