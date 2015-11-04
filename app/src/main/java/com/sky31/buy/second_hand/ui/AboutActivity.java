package com.sky31.buy.second_hand.ui;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
        Uri uri ;
        Intent it ;
        switch (v.getId()) {
            /* call us */
            case R.id.tr_call_me:
                showDialog();
                break;

            /* develop*/
            case R.id.tr_develop:
                uri = Uri.parse("http://github.com/CasparGX");
                it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;

            /*designer's weibo*/
            case R.id.tr_design:
                uri = Uri.parse("http://weibo.com/konokenno");
                it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
        }

    }

    /*显示dialog*/
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("联系我们")
                .setMessage("请联系我们的官方QQ：1643787575")
                .setPositiveButton("复制号码",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                ClipboardManager cmb = (ClipboardManager)AboutActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                                cmb.setText("1643787575");
                                Toast.makeText(AboutActivity.this, "已复制QQ号，可直接粘贴", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("确定", null).create()
                .show();
    }

}
