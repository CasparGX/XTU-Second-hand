package com.sky31.buy.second_hand.ui.fragment;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.model.GoodsData;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class MeFragment extends Fragment {

    private String TAG = HomeFragment.class.getName();

    private RequestParams params = new RequestParams();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        params.add(Constants.Keys.KEY_USERNAME,"630248976@qq.com");
        params.add(Constants.Keys.KEY_PASSWORD,"Caspar1995.");
        HttpUtil.post(Constants.Apis.API_USER_LOGIN_POST
                , params
                , mJsonHttpResponseHandler);

        return inflater.inflate( R.layout.fragment_me, container, false);
    }

    JsonHttpResponseHandler mJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "-----------------接受到JSONArray数据-----------------");
            Log.i(TAG,response+"");
            Log.i(TAG,headers+"");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "-----------------接受到JSONObject数据-----------------");
            Log.i(TAG,response+"");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.e(TAG, " onFailure" + responseString.toString());
        }

        public void onFinish() {
            Log.i(TAG, "onFinish");
        }
    };
}