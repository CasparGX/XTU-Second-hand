package com.sky31.buy.second_hand.ui;

import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

import static com.sky31.buy.second_hand.R.id.iv_share;


/**
 * Created by Caspar on 2015/7/21.
 */
public class GoodsShowActivity extends SwipeBackActivity implements View.OnClickListener {

    private SwipeBackLayout mSwipeBackLayout;
    private SwipeBackActivityHelper mHelper;

    private ImageView imgPic;
    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvDec;
    private TextView tvPhone;
    private TextView tvQq;
    private TextView tvSeller;

    private ImageView ivBack;
    private ImageView ivShare;

    private ViewPager imgPicViewPager;
    private ArrayList<ImageView> imgList;
    private PagerAdapter imgListAdapter;
    private ImageView imgPic01;
    private ImageView imgPic03;
    private ImageView imgPic02;
    private GoodsData goods;

    private LinearLayout llMinImage;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_show);

        Log.i("goodsShowActivity", this.getPackageName().toString());
        //设置状态栏颜色
        BuyApp.setStatusBarColor(GoodsShowActivity.this);

        /*mSwipeBackLayout = getSwipeBackLayout();
        //设置可以滑动的区域，推荐用屏幕像素的一半来指定
        mSwipeBackLayout.setEdgeSize(200);
        //设定滑动关闭的方向，SwipeBackLayout.EDGE_ALL表示向下、左、右滑动均可。EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);*/
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        this.imageLoader = BuyApp.getInstance().getImageLoader();
        this.displayImageOptions = BuyApp.getInstance().getDisplayImageOptions();

        Intent intent = this.getIntent();
        goods = intent.getParcelableExtra("goodsInfo");
        //init ImageView btn
        ivBack = (ImageView) findViewById(R.id.iv_back_btn);
        ivBack.setOnClickListener(this);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivShare.setOnClickListener(this);
        ivShare.setVisibility(View.VISIBLE);

        //init TextView
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(goods.title);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvPrice.setText(goods.price+"");
        tvDec = (TextView) findViewById(R.id.tv_dec);
        tvDec.setText(goods.dec);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        if (goods.phone.equals("0")) {
            tvPhone.setVisibility(View.GONE);
        } else {
            tvPhone.setText(goods.phone);
            tvPhone.setOnClickListener(this);
        }
        tvQq = (TextView) findViewById(R.id.tv_qq);
        if (goods.qq.equals("0")) {
            tvQq.setVisibility(View.GONE);
        } else {
            tvQq.setText(goods.qq);
            tvQq.setOnClickListener(this);
        }
        tvSeller = (TextView) findViewById(R.id.tv_seller);
        tvSeller.setText(goods.seller);

        //start-> get window's width && height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windowWidth = displayMetrics.widthPixels;
        //end-> get window's width && height

        imgPicViewPager = (ViewPager) findViewById(R.id.imgPicViewPager);
        final LayoutParams linearParams = imgPicViewPager.getLayoutParams();
        linearParams.height = windowWidth;
        linearParams.width = windowWidth;
        imgPicViewPager.setLayoutParams(linearParams);
        imgPicViewPager.requestLayout();

        imgPicViewPager.setOffscreenPageLimit(3);

        imgListAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return goods.imgUrlArray.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(View container, int position) {

                ImageView imageView = new ImageView(GoodsShowActivity.this);
                ImageView imageViewMin = new ImageView(GoodsShowActivity.this);
                llMinImage = (LinearLayout) findViewById(R.id.ll_minImage);

                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(30,30);
                float densityMargin = getResources().getDisplayMetrics().density;
                int finalDimensMargin = (int)(30 * densityMargin);

                linearParams.width = finalDimensMargin;
                linearParams.height = finalDimensMargin;

                finalDimensMargin = (int)(10 * densityMargin);
                linearParams.setMargins(finalDimensMargin,finalDimensMargin,finalDimensMargin,finalDimensMargin);


                imageViewMin.setLayoutParams(linearParams);

                llMinImage.addView(imageViewMin);

                imageLoader.displayImage(goods.imgUrl
                        + Constants.Values.VALUE_MIDDLE_SIZE_PRE
                        + goods.imgUrlArray.get(position)
                        , imageView
                        , displayImageOptions);

                imageLoader.displayImage(goods.imgUrl
                        + Constants.Values.VALUE_MIN_SIZE_PRE
                        + goods.imgUrlArray.get(position)
                        , imageViewMin
                        , displayImageOptions);

                ((ViewPager) container).addView(imageView, 0);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                object = null;
            }
        };

        imgPicViewPager.setAdapter(imgListAdapter);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_out);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_btn:
                onBackPressed();
                break;

            case R.id.iv_share:
                share();
                break;

            case R.id.tv_qq:
                String qq = (String) tvQq.getText();
                ClipboardManager cmb = (ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(qq.trim());
                Toast.makeText(GoodsShowActivity.this,"已复制QQ号，可直接粘贴",Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_phone:
                //取得电话号码串
                String phone = tvPhone.getText().toString();
                //如果输入不为空创建打电话的Intent
                if(phone.trim().length()>=5)
                {
                    Intent phoneIntent = new Intent("android.intent.action.DIAL",
                            Uri.parse("tel:" + phone));
                    //启动
                    startActivity(phoneIntent);
                } else {
                    Toast.makeText(GoodsShowActivity.this,"号码为空不能拨号",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void share() {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "湘大二手街");
        String msgText = "我在湘大二手街上看到了一个没人要的东西 -「"
                +goods.title
                +"」,给你看看。\n"
                +"http://buy.sky31.com/show.html?gid="
                +goods.id
                +"\n分享来自「湘大二手街APP」";
        intent.putExtra(Intent.EXTRA_TEXT, msgText);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "湘大二手街"));

        /*Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("image*//*");
        Resources r = this.getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.icon) + "/"
                + r.getResourceTypeName(R.drawable.icon) + "/"
                + r.getResourceEntryName(R.drawable.icon));
        //Uri uri = Uri.fromFile(getResources().getDrawable(R.drawable.icon));

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, getTitle()));*/
    }
}
