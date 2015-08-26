package com.sky31.buy.second_hand.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky31.buy.second_hand.R;

public class PublishActivity extends Activity implements View.OnClickListener {

    /*header*/
    private ImageView ivBackBtn;
    private TextView tvHeaderTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        /*header*/
        ivBackBtn = (ImageView) findViewById(R.id.iv_back_btn);
        ivBackBtn.setOnClickListener(this);
        tvHeaderTitle = (TextView) findViewById(R.id.tv_header_title);
        setTvHeaderTitle(); //修改header标题

    }

    /*修改header标题*/
    private void setTvHeaderTitle() {
        Intent mIntent = getIntent();
            String title = mIntent.hasExtra("title") ? mIntent.getStringExtra("title") : getResources().getString(R.string.app_title);
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
}
