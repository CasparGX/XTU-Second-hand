package com.sky31.buy.second_hand.util;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Caspar on 2015/7/29.
 */
public class HttpUtil {
    private static AsyncHttpClient client = new AsyncHttpClient();
    static {
        //client.setTimeout();  //设置连接超时, 默认为10s
    }
    
    public static void onFailureErrorToast(Context context){
        Toast.makeText(context,"网络异常，无法连接",Toast.LENGTH_SHORT).show();
    }

    public static void get(String urlString,AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象
    {
        client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url里面带参数
    {
        client.get(urlString, params,res);
    }
    public static void post(String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url里面带参数
    {
        client.post(urlString, params,res);
    }
    public static void get(String urlString,JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
        client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
        client.get(urlString, params,res);
    }
    public static void get(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {
        client.get(uString, bHandler);
    }
    public static void post(String urlString,JsonHttpResponseHandler res) {
        client.post(urlString, res);
    }
    public static void post(String urlString,RequestParams params,JsonHttpResponseHandler res) {  //带参数，获取json对象或者数组{
        client.post(urlString, params, res);
    }
    public static AsyncHttpClient getClient()
    {
        return client;
    }
}
