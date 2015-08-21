package com.sky31.buy.second_hand.ui.fragment;



import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

public class MeFragment extends Fragment implements View.OnClickListener {

    private String TAG = this.getTag();

    private TableRow tr01;
    private TableRow tr02;

    private RequestParams params = new RequestParams();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        tr01 = (TableRow) view.findViewById(R.id.tr_01);
        tr02 = (TableRow) view.findViewById(R.id.tr_02);
        tr01.setOnClickListener(this);
        tr02.setOnClickListener(this);

        return view;
    }

    JsonHttpResponseHandler mJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "-----------------开始: 接受到JSONArray数据-----------------");
            Log.i(TAG,response+"");
            System.out.println(headers.toString());

            Log.i(TAG, "-----------------结束: 接受到JSONArray数据-----------------");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "-----------------开始: 接受到JSONObject数据-----------------");
            Log.i(TAG, response + "");
            System.out.println(headers + "");
            System.out.print(headers);

            Log.i(TAG, "-----------------结束: 接受到JSONObject数据-----------------");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

            Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_LONG).show();
            /*throwable.printStackTrace();
            Log.i(TAG, " onFailure" + statusCode + " \n" + responseString.toString());*/
        }

        public void onFinish() {
            Log.i(TAG, "onFinish");
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tr_01:
                params.add(Constants.Keys.KEY_USERNAME,"630248976@qq.com");
                params.add(Constants.Keys.KEY_PASSWORD,"Caspar1995.");
                HttpUtil.post(Constants.Apis.API_USER_LOGIN_POST
                        , params
                        , mJsonHttpResponseHandler);
                break;
            case R.id.tr_02:
                File file = new File("/mnt/sdcard/","home.jpg");
                RequestParams params = new RequestParams();
                if(file.exists() && file.length()>0) {
                    try {
                        params.put("file1", file);
                        params.put("file2", file);
                        params.put("file3", file);
                        Log.i(TAG, Environment.getExternalStorageDirectory().toString());
                        //params.add("file", String.valueOf(getActivity().getResources().getDrawable(R.drawable.loading)));
                        HttpUtil.post(Constants.Apis.API_GOODS_APPINSERT_POST
                                , params
                                , mJsonHttpResponseHandler);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    Log.i(TAG, "文件不存在");
                }
                break;
        }
    }

    AsyncHttpResponseHandler mAsyncHttpResponseHandler = new AsyncHttpResponseHandler() {


        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {

            Log.i(TAG, "-----------------AsyncHttp  onSuccess-----------------");
            for (int j = 0; j < bytes.length; j++ ) {
                Log.i(TAG, String.valueOf(bytes[j]));
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            Log.i(TAG, "-----------------AsyncHttp  onFailure-----------------");
            Log.i(TAG, String.valueOf(i));
        }
    };
}