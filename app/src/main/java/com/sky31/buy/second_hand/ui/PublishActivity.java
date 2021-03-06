package com.sky31.buy.second_hand.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.ClassifyInfo;
import com.sky31.buy.second_hand.ui.activity.SwipeBackActivity2;
import com.sky31.buy.second_hand.ui.fragment.ClassifyFragment;
import com.sky31.buy.second_hand.util.CompImageUtil;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class PublishActivity extends SwipeBackActivity2 implements View.OnClickListener {
    /*TAG*/
    private String TAG = PublishActivity.class.getName();
    /*网络参数*/
    RequestParams params = new RequestParams();
    boolean isOnUpLoad = false;

    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static String SAVED_IMAGE_DIR_PATH = null;
    private String capturePath = null;
    private File tempfile;

    /*diaLog*/
    //private AlertDialog.Builder builderLoading;
    //private AlertDialog dialog;
    private ProgressDialog mProgressDialog;

    /*Intent*/
    private Intent mIntent;
    private String uid = null;

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
    private Button btnPublish;

    /*图片file*/
    private ImageView ivFile1;
    private ImageView ivFile2;
    private ImageView ivFile3;
    private int imgFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        //Intent
        mIntent = getIntent();

        //设置状态栏颜色
        //BuyApp.setStatusBarColor(PublishActivity.this);

        /*滑动返回*/
        /*mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);*/

        /*header*/
        ivBackBtn = (ImageView) findViewById(R.id.iv_back_btn);
        ivBackBtn.setOnClickListener(this);
        tvHeaderTitle = (TextView) findViewById(R.id.tv_header_title);
        setTvHeaderTitle(); //修改header标题

        //ImageMenu
        View.OnCreateContextMenuListener addImageMenuListener = new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, getResources().getString(R.string.album));
                menu.add(0, 1, 0, getResources().getString(R.string.camera));
            }
        };
        /*图片file*/
        ivFile1 = (ImageView) findViewById(R.id.iv_file1);
        ivFile2 = (ImageView) findViewById(R.id.iv_file2);
        ivFile3 = (ImageView) findViewById(R.id.iv_file3);
        ivFile2.setVisibility(View.INVISIBLE);
        ivFile3.setVisibility(View.INVISIBLE);
        ivFile1.setOnClickListener(this);
        ivFile2.setOnClickListener(this);
        ivFile3.setOnClickListener(this);
        ivFile1.setOnCreateContextMenuListener(addImageMenuListener);
        ivFile2.setOnCreateContextMenuListener(addImageMenuListener);
        ivFile3.setOnCreateContextMenuListener(addImageMenuListener);


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
        for (int i = 0; i < mClassifyInfo.size(); i++) {
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
        mTime.add("90");
        mTime.add("60");
        mTime.add("30");
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

        /*发布按钮*/
        btnPublish = (Button) findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int width = ivFile1.getWidth();
        ViewGroup.LayoutParams params1 = ivFile1.getLayoutParams();
        ViewGroup.LayoutParams params2 = ivFile2.getLayoutParams();
        ViewGroup.LayoutParams params3 = ivFile3.getLayoutParams();
        if (width<getResources().getDimension(R.dimen.publish_good_image_size)) {
            params1.height = width;
            params2.height = width;
            params3.height = width;
        } else {
            params1.width = params1.height;
            params2.width = params2.height;
            params3.width = params3.height;
        }
        ivFile1.setLayoutParams(params1);
        ivFile2.setLayoutParams(params2);
        ivFile3.setLayoutParams(params3);
    }

    /*返回键*/
    @Override
    public void onBackPressed() {
        if (isOnUpLoad) {
            cancleUpLoad();
        } else {
            super.onBackPressed();
            //overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_out);
        }

    }

    /*修改header标题*/
    private void setTvHeaderTitle() {
        String title = mIntent.hasExtra("headerTitle")
                ? mIntent.getStringExtra("headerTitle") : getResources().getString(R.string.app_title);
        tvHeaderTitle.setText(title);
    }

    /*填充卖家信息*/
    private void setSellerInfo() {
        uid = mIntent.getStringExtra("id");
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

            case R.id.btn_publish:
                //发布按钮
                publishGoods();
                break;

            case R.id.iv_file1:
                //图片文件
                addImageFile(1);
                break;
            case R.id.iv_file2:
                //图片文件
                addImageFile(2);
                break;
            case R.id.iv_file3:
                //图片文件
                addImageFile(3);
                break;
        }
    }

    /*添加图片文件*/
    private void addImageFile(int imgFlag) {
        this.imgFlag = imgFlag;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String timeStr = formatter.format(curDate);
        SAVED_IMAGE_DIR_PATH = Environment.getExternalStorageDirectory()
                + "/" + getResources().getString(R.string.app_name_en) + "_pic"
                + "/" + timeStr;
        openContextMenu(ivFile1);
    }

    private void getImageFromAlbum() {
        Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    protected void getImageFromCamera() {

        File DatalDir = Environment.getExternalStorageDirectory();
        File myDir = new File(DatalDir, "/DCIM/Camera");
        myDir.mkdirs();
        String mDirectoryname = DatalDir.toString() + "/DCIM/Camera";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hhmmss",
                Locale.SIMPLIFIED_CHINESE);
        tempfile = new File(mDirectoryname, sdf.format(new Date())
                + ".jpg");
        //if (tempfile.isFile())
            //tempfile.delete();
        Uri Imagefile = Uri.fromFile(tempfile);
        capturePath = tempfile.toString();
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Imagefile);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_CAMEIA);

        /*String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            String out_file_path = SAVED_IMAGE_DIR_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            capturePath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".jpg";
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
            //getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            getImageByCamera.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String picturePath = null;
        String tmpImagePath = null;
        Uri selectedImage;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            picturePath = getImageFilePath(selectedImage);
            tmpImagePath = CompImageAndShow(picturePath);
            UploadImage(tmpImagePath);
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA && resultCode == RESULT_OK) {
            Log.i("ImageRsult",requestCode+" "+resultCode+" "+data);
            if (data!=null && data.getData()!=null) {
                selectedImage = data.getData();
                picturePath = getImageFilePath(selectedImage);
            } else {
                picturePath = capturePath;
            }
            tmpImagePath = CompImageAndShow(picturePath);
            UploadImage(tmpImagePath);
        }

    }

    private String getImageFilePath(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    private void UploadImage(String tmpImagePath) {
    /*上传图片测试样例*/
        File file = new File(tmpImagePath);
        if (file.exists() && file.length() > 0) {
            setParamsFile("file" + this.imgFlag, file);
        } else {
            Log.i(TAG, "文件不存在");
        }
    }

    @NonNull
    private String CompImageAndShow(String picturePath) {
        //图片压缩工具类
        CompImageUtil compImage = new CompImageUtil();
        Bitmap bitmapImage = compImage.getimage(picturePath, 1500f, 1500f, 500);
        String tmpImagePath = getCacheDir() + "tmp" + this.imgFlag + ".jpg";
        compImage.saveBitmapFile(bitmapImage, tmpImagePath);
        switch (this.imgFlag) {
            case 1:
                ivFile1.setImageBitmap(bitmapImage);
                ivFile2.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivFile2.setImageBitmap(bitmapImage);
                ivFile3.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivFile3.setImageBitmap(bitmapImage);
                break;
        }
        return tmpImagePath;
    }

    /* cancle upload and publish goods*/
    private void cancleUpLoad() {
        HttpUtil.getClient().cancelAllRequests(true);
        isOnUpLoad = false;
    }

    /**
     * 发布商品
     * spinner选中的值在spinnerSelectListener中设置setParams
     */
    private void publishGoods() {
        isOnUpLoad = true;
        //showLoadingDialog();
        if (etGoodsTitle.getText().toString().equals("")) {
            Toast.makeText(PublishActivity.this, "不写标题不给发！哼！", Toast.LENGTH_SHORT).show();
        } else if (etGoodsDec.getText().toString().equals("")) {
            Toast.makeText(PublishActivity.this, "写个描述介绍一下嘛", Toast.LENGTH_SHORT).show();
        } else if (etGoodsPrice.getText().toString().equals("")) {
            Toast.makeText(PublishActivity.this, "价格都不告诉我，怎么帮你卖嘛", Toast.LENGTH_SHORT).show();
        } else if (etPhone.getText().toString().equals("") && etQq.getText().toString().equals("")) {
            Toast.makeText(PublishActivity.this, "留个联系方式给我嘛，讨厌，还要人家这么主动", Toast.LENGTH_SHORT).show();
        } else if (!params.has("file1")) {
            Toast.makeText(PublishActivity.this, "请选择一张图片", Toast.LENGTH_SHORT).show();
        } else {
            showLoadingDialog();
            setParams(Constants.Keys.KEY_TITLE, etGoodsTitle.getText() + "");//title
            setParams(Constants.Keys.KEY_DESCRIBE, etGoodsDec.getText() + "");//dec
            setParams(Constants.Keys.KEY_PRICE, etGoodsPrice.getText() + "");//price

            setParams(Constants.Keys.KEY_SELLER, etNickname.getText() + "");//seller
            setParams(Constants.Keys.KEY_UID, uid);//uid
            setParams(Constants.Keys.KEY_PHONE, etPhone.getText() + "");//phone
            setParams(Constants.Keys.KEY_QQ, etQq.getText() + "");//qq

            params.setForceMultipartEntityContentType(true);
            HttpUtil.post(Constants.Apis.API_GOODS_APPINSERT_POST
                    , params
                    , mInsertHandler);
        }

    }

    /*合成网络请求参数*/
    public void setParams(String key, String value) {
        if (params.has(key)) {
            params.remove(key);
        }
        params.add(key, value);
    }

    /*合成网络请求所附带文件*/
    public void setParamsFile(String key, File file) {
        try {
            if (params.has(key)) {
                params.remove(key);
            }
            params.put(key, file, "image/jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insert() {
        /*上传图片测试样例*/
        File file = new File("/mnt/sdcard/", "home.jpg");
        RequestParams params = new RequestParams();
        if (file.exists() && file.length() > 0) {
            try {
                params.put("file1", file);
                params.put("file2", file);
                params.put("file3", file);
                Log.i(TAG, Environment.getExternalStorageDirectory().toString());
                HttpUtil.post(Constants.Apis.API_GOODS_TESTINSERT_POST
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
    JsonHttpResponseHandler mInsertHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getString("result").equals("error")) {
                    //上传出错
                    Toast.makeText(PublishActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                } else if (response.getString("result").equals("success")) {
                    //上传成功
                    Toast.makeText(PublishActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                    isOnUpLoad = false;
                    onBackPressed();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
            mProgressDialog.setMessage("正在发布信息 " + Math.floor(bytesWritten * 1.0 / totalSize * 100) + "%");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.i(TAG, statusCode + "\n" + responseString + " \n" + throwable);
            HttpUtil.onFailureErrorToast(PublishActivity.this);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            isOnUpLoad = false;
            hideLoadingDialog();
        }
    };

    /*分类信息spinner*/
    class ClassifySpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            //Toast.makeText(PublishActivity.this,mClassifyInfo.get(arg2).getId()+"",Toast.LENGTH_SHORT).show();
            setParams(Constants.Keys.KEY_TYPE, mClassifyInfo.get(arg2).getId() + "");//type
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /*trading spinner*/
    private class TradingSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            //Toast.makeText(PublishActivity.this,(arg2+1)+"",Toast.LENGTH_SHORT).show();
            setParams(Constants.Keys.KEY_TRADING, (arg2 + 1) + "");//trading
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /*bargain spinner*/
    private class BargainSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            if (mBargain.get(arg2).equals("是")) {
                //Toast.makeText(PublishActivity.this,1+"",Toast.LENGTH_SHORT).show();
                setParams(Constants.Keys.KEY_BARGAIN, 1 + "");//bargain
            } else if (mBargain.get(arg2).equals("否")) {
                //Toast.makeText(PublishActivity.this,0+"",Toast.LENGTH_SHORT).show();
                setParams(Constants.Keys.KEY_BARGAIN, 0 + "");//bargain
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /*time spinner*/
    private class TimeSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            //Toast.makeText(PublishActivity.this,mTime.get(arg2),Toast.LENGTH_SHORT).show();
            setParams(Constants.Keys.KEY_INTERVAL, mTime.get(arg2) + "");//interval
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                getImageFromAlbum();
                return true;
            case 1:
                getImageFromCamera();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

