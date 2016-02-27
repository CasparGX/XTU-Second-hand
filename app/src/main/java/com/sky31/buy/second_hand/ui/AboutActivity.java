package com.sky31.buy.second_hand.ui;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.ui.activity.BaseSwipeBackActivity;
import com.sky31.buy.second_hand.util.FileUtil;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class AboutActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    /* Widget */
    private TextView tvTitle;
    private TextView tvVersion;
    private ImageView iv_back_btn;
    private TableRow trCallMe;
    private TableRow trCheckUpdate;
    private TableRow trDevelop;
    private TableRow trDesigner;
    private TextView tvHeaderTitle;
    private TextView tvCheckUpdate;

    private Intent mIntent;

    private static Context context;

    /*滑动返回*/
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        context = AboutActivity.this;
        mIntent = getIntent();

        //设置状态栏颜色
        //BuyApp.setStatusBarColor(AboutActivity.this);
        /*滑动返回*/
        /*mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);*/

        initWidget();
        setTvHeaderTitle();

    }

    private void initWidget() {
        //tvTitle = (TextView) findViewById(R.id.textView);
        //tvTitle.setText("关于");
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本：" + BuyApp.getVersionName(this));
        tvCheckUpdate = (TextView) findViewById(R.id.tv_checkUpdate);
        iv_back_btn = (ImageView) findViewById(R.id.iv_back_btn);
        iv_back_btn.setOnClickListener(this);
        trCallMe = (TableRow) findViewById(R.id.tr_call_me);
        trCheckUpdate = (TableRow) findViewById(R.id.tr_update);
        trDevelop = (TableRow) findViewById(R.id.tr_develop);
        trDesigner = (TableRow) findViewById(R.id.tr_design);
        trCallMe.setOnClickListener(this);
        trCheckUpdate.setOnClickListener(this);
        trDevelop.setOnClickListener(this);
        trDesigner.setOnClickListener(this);

        tvHeaderTitle = (TextView) findViewById(R.id.tv_header_title);
    }

    /*修改header标题*/
    private void setTvHeaderTitle() {
        String title = mIntent.hasExtra("headerTitle")
                ? mIntent.getStringExtra("headerTitle") : getResources().getString(R.string.app_title);
        tvHeaderTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        Uri uri;
        Intent it;
        switch (v.getId()) {
            /* back */
            case R.id.iv_back_btn:
                onBackPressed();
                break;

            /* call us */
            case R.id.tr_call_me:
                showDialog();
                break;

            /*  check update*/
            case R.id.tr_update:
                tvCheckUpdate.setText(getResources().getString(R.string.checkingUpdate));
                trCheckUpdate.setClickable(false);
                trCheckUpdate.setEnabled(false);
                BuyApp.getVersionCode(this);
                BuyApp.getInstance().checkUpdate();
                tvCheckUpdate.setText(getResources().getString(R.string.checkUpdate));
                trCheckUpdate.setClickable(true);
                trCheckUpdate.setEnabled(true);
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
                .setMessage("湘大二手街官方QQ：1643787575")
                .setPositiveButton("复制号码",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                ClipboardManager cmb = (ClipboardManager) AboutActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                                cmb.setText("1643787575");
                                Toast.makeText(AboutActivity.this, "已复制QQ号，可直接粘贴", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("确定", null).create()
                .show();
    }


    /*显示upadte dialog*/
    public static void updateDialog(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("检查更新")
                .setMessage("有新版本，是否更新？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                downloadApp(url);
                            }
                        })
                .setNegativeButton("取消", null).create()
                .show();
    }

    private static void downloadApp(String url) {
        Log.i("downloadApp", "----------downloadApp----------");
        // 指定文件类型
        String[] allowedContentTypes = new String[]{".*"};
        // 获取二进制数据如图片和其他文件
        HttpUtil.get(url, new BinaryHttpResponseHandler(
                allowedContentTypes) {
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                Log.i("downloadApp", bytesWritten + "");
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                // 文件夹地址
                String tempPath = Environment.getDownloadCacheDirectory().toString();
                String apkName = "Second-hand.apk";
                // 文件地址
                String filePath = tempPath + "/" + apkName;
                // 下载成功后需要做的工作
                Log.i("binaryData:", "共下载了：" + bytes.length);


                FileUtil fileutils = new FileUtil();


                // 判断sd卡上的文件夹是否存在
                if (!fileutils.isFileExist(tempPath)) {
                    fileutils.createSDDir(tempPath);
                }


                // 删除已下载的apk
                if (fileutils.isFileExist(filePath)) {
                    fileutils.deleteFile(filePath);
                }


                InputStream inputstream = new ByteArrayInputStream(bytes);
                if (inputstream != null) {
                    File apk = fileutils.write2SDFromInput(filePath, inputstream);
                    try {
                        inputstream.close();
                        installApk(apk);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    /**
     * 安装APK
     */
    private static void installApk(File file) {
        Log.i("installApk", file.toString());
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
