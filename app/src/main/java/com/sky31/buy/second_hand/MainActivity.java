package com.sky31.buy.second_hand;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.api.GoodsApi;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private BuyApp app = new BuyApp();
    private ArrayList<GoodsData> mGoodsData = app.getGoodsData();
    private JSONArray mGoodsArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoodsApi goodsApi = new GoodsApi();
        RequestParams params = new RequestParams(); // 绑定参数
        params.put(Constants.Keys.KEY_LIMITID, 0);
        HttpUtil.get(Constants.Apis.API_GOODS_LIST_GET, params, mJsonHttpResponseHandler);

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
