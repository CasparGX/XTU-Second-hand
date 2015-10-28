package com.sky31.buy.second_hand.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AboutActivity extends SwipeBackActivity implements View.OnClickListener {

    /* Widget */
    private TextView tvTitle;
    private TableRow trCallMe;
    private TableRow trCheckUpdate;
    private TableRow trDevelop;
    private TableRow trDesigner;

    /*滑动返回*/
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        //设置状态栏颜色
        BuyApp.setStatusBarColor(AboutActivity.this);
        /*滑动返回*/
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        initWidget();
    }

    private void initWidget() {
        tvTitle = (TextView) findViewById(R.id.textView);
        //tvTitle.setText("关于");
        trCallMe = (TableRow) findViewById(R.id.tr_call_me);
        trCheckUpdate = (TableRow) findViewById(R.id.tr_update);
        trDevelop = (TableRow) findViewById(R.id.tr_develop);
        trDesigner = (TableRow) findViewById(R.id.tr_design);
        trCallMe.setOnClickListener(this);
        trCheckUpdate.setOnClickListener(this);
        trDevelop.setOnClickListener(this);
        trDesigner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
