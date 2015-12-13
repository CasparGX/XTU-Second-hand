package com.sky31.buy.second_hand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.ui.HomeActivity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private BuyApp app = new BuyApp();
    private ArrayList<GoodsData> mGoodsData = app.getGoodsData();
    private JSONArray mGoodsArray = null;

    private long delayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuyApp.getInstance().isOpen()) {
            delayTime = 0;
        } else {
            delayTime = 2000;
        }

        /*splashscreen*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);  //从启动动画ui跳转到主ui
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                MainActivity.this.finish();    // 结束启动动画界面
            }
        }, delayTime);    //启动动画

        /*
        GoodsApi goodsApi = new GoodsApi();
        RequestParams params = new RequestParams(); // 绑定参数
        params.put(Constants.Keys.KEY_LIMITID, 0);
        HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET, params, mJsonHttpResponseHandler);*/

    }

    JsonHttpResponseHandler mJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Log.i("GoodsApi: ", "onSuccess ");
            Log.i("GoodsApi", response + "");
            mGoodsArray = response;
            app.addGoodsData(mGoodsArray);
            Log.i("MainAty : ", mGoodsData.get(0).imgUrl + "");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            //if return JSONObject, it's have no data
            Log.i("GoodsApi", "onSuccess ");
            Log.i("GoodsApi", response + "");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.e("GoodsApi", " onFailure" + responseString.toString());
        }

        public void onFinish() {
            Log.i("GoodsApi", "onFinish");
        }
    };
}
