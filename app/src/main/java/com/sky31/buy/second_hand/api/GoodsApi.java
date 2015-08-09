package com.sky31.buy.second_hand.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Caspar on 2015/7/29.
 */
public class GoodsApi {

    private BuyApp app;

    //private GoodsData mGoodsData = app.getGoodsData();

    private JSONArray mJOGoodsData = new JSONArray();

    private static final String API_GOODS_LIST_GET = Constants.Apis.API_GOODS_LIST_GET;
    private static final String LIMITID = Constants.Keys.KEY_LIMITID;

//    public JSONArray query() {
//
//        final String urlString = API_GOODS_LIST_GET;
//        final RequestParams params = new RequestParams(); // 绑定参数
//        params.put(LIMITID, 0);
//
////        HttpUtil.get(urlString, params, );//HttpUtil.get
//
//        return mJOGoodsData;
//    }//query

    public void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
//        HttpUtil.get(url, par);
    }
}
