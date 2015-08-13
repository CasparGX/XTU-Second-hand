package com.sky31.buy.second_hand.context;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Caspar on 2015/7/29.
 */
public class BuyApp extends Application {

    private static final String API_URL      = Constants.Apis.API_URL;
    private static final String KEY_PICNAME  = Constants.Keys.KEY_PICNAME;
    private static final String KEY_PICSRC   = Constants.Keys.KEY_PICSRC;


    //GoodsData
    private ArrayList<GoodsData> mGoodsData = new ArrayList<>();

    //ImageLoader
    private ImageLoader imageLoader;
    private ImageLoaderConfiguration configuration;

    public DisplayImageOptions getDisplayImageOptions() {
        return displayImageOptions;
    }

    private DisplayImageOptions displayImageOptions;

    private static BuyApp instance;

    public static BuyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initImgLoader();
    }

    private void initImgLoader() {
        // 1.获取ImageLoader实例
        imageLoader = ImageLoader.getInstance();
        // 2. 使用默认配置
        // configuration = ImageLoaderConfiguration.createDefault(getContext());
        configuration = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        // 3. 初始化ImageLoader
        imageLoader.init(configuration);
        // 4. 显示图片时的配置
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.loading)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.loading)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                        //.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                        //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                        //设置图片加入缓存前，对bitmap进行设置
                        //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    public void getImageLoad(String imgUrl, ImageView icon) {
        // 5.显示图片
        imageLoader.displayImage(imgUrl, icon, displayImageOptions);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }



    public ArrayList<GoodsData> getGoodsData() {
        return mGoodsData;
    }

    public ArrayList<GoodsData> addGoodsData(JSONArray goodsData) {
        String imgUrl;
        for (int i = 0; i < goodsData.length(); i++) {
            try {
                JSONObject good = goodsData.getJSONObject(i);
                JSONObject imgUrlObj = good.getJSONObject(KEY_PICNAME);
                Iterator x = imgUrlObj.keys();
                List<String> imgUrlArray = new ArrayList<String>();
                //int xi = 0;
                imgUrl = API_URL + good.getString(KEY_PICSRC);
                while (x.hasNext()) {
                    String key = (String) x.next();
                    imgUrlArray.add(imgUrlObj.getString(key));
                    //imgUrlArray.put(imgUrlObj.get(key));
                    System.out.println(key + ":" + imgUrlObj.get(key));
                    //xi++;
                }

                //imgUrl = "http://buy.sky31.com/fleaapi"+good.getString("picsrc")+"m_"+good.getJSONObject("picname").getString("1");
                mGoodsData.add(
                        new GoodsData(
                                good.getInt("id"),
                                good.getString("title"),
                                good.getString("seller"),
                                good.getString("type"),
                                good.getString("describe"),
                                good.getString("phone"),
                                good.getString("qq"),
                                imgUrl,
                                imgUrlArray,
                                good.getInt("price"),
                                good.getString("creat_time")
                        )
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mGoodsData;
    }



}
