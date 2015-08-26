package com.sky31.buy.second_hand.ui.fragment;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

public class MeFragment extends Fragment implements View.OnClickListener {

    private String TAG = this.getTag();

    /*用户登录信息*/
    private JSONObject userInfo = new JSONObject();

    /*登录状态*/
    private boolean isLogin;

    /*控件*/
    private TableRow trEditInfo;
    private TableRow trPublish;
    private TableRow trSelling;
    private TextView tvNickname;
    private TextView tvEmail;
    private ImageView ivUsericon;
    private TextView tvLoginLink;

    private AlertDialog.Builder builderEditInfo;
    private EditText etNickName;
    private EditText etPhoneNum;
    private EditText etQq;



    private AlertDialog.Builder builderLogin;
    private EditText mEtPassWord;
    private EditText mEtUserName;

    private RequestParams params = new RequestParams();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builderLogin = new AlertDialog.Builder(getActivity());
        builderEditInfo = new AlertDialog.Builder(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        /*userinfo*/
        tvLoginLink = (TextView) view.findViewById(R.id.tv_login_link);
        tvLoginLink.setOnClickListener(this);
        ivUsericon = (ImageView) view.findViewById(R.id.iv_usericon);
        tvNickname = (TextView) view.findViewById(R.id.tv_nickname);

        /*TableRow*/
        trEditInfo = (TableRow) view.findViewById(R.id.tr_edit_info);
        trEditInfo.setOnClickListener(this);
        trPublish = (TableRow) view.findViewById(R.id.tr_publish);
        trPublish.setOnClickListener(this);
        trSelling = (TableRow) view.findViewById(R.id.tr_selling);
        trSelling.setOnClickListener(this);

        //TODO 将用户信息存入缓存或每次启动进行一次登录
        return view;
    }

    /*登录handler*/
    JsonHttpResponseHandler mLogInHandler = new JsonHttpResponseHandler() {
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
            try {
                if (response.get("result").equals("error")) {
                    Toast.makeText(getActivity(),"登录失败:"+response.get("msg")+"",Toast.LENGTH_SHORT).show();
                } else if (response.get("result").equals("success")) {
                    //Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
                    response = (JSONObject) response.get("msg");
                    logInSuccess(response);
                } else{
                    Toast.makeText(getActivity(),"服务器无响应",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "-----------------结束: 接受到JSONObject数据-----------------");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

            Toast.makeText(getActivity(), Constants.Values.VALUE_SERVICE_NO_RESPONSE, Toast.LENGTH_LONG).show();
            /*throwable.printStackTrace();
            Log.i(TAG, " onFailure" + statusCode + " \n" + responseString.toString());*/
        }

        public void onFinish() {
            Log.i(TAG, "onFinish");
        }
    };

    /*登录成功*/
    private void logInSuccess(JSONObject response) {
        try {
            isLogin = true;
            userInfo = response;
            tvLoginLink.setText(getActivity().getResources().getString(R.string.logOut));
            tvNickname.setText(response.get("nickname")+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*退出账号成功*/
    private void logOutSuccess() {
        isLogin = false;
        tvLoginLink.setText(getActivity().getResources().getString(R.string.clickLogin));
        tvNickname.setText(getActivity().getResources().getString(R.string.isNotLogin));
    }


    /*点击事件*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*修改用户信息*/
            case R.id.tr_edit_info:
                if (isLogin) {
                    //已登陆进行操作
                    showEditInfoDialog();
                } else {
                    //未登录
                    showLoginDialog();
                }
                break;

            /*正在出售商品*/
            case R.id.tr_selling:
                if (isLogin) {
                    //已登陆进行操作

                } else {
                    //未登录
                    showLoginDialog();
                }
                break;

            /*发布商品*/
            case R.id.tr_publish:
                if (isLogin) {
                    //已登陆进行操作

                } else {
                    //未登录
                    showLoginDialog();
                }
                /*上传图片测试样例*/
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
                                , mLogInHandler);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    Log.i(TAG, "文件不存在");
                }
                break;

            /*登录链接*/
            case R.id.tv_login_link:
                if (isLogin) {
                    logOutSuccess();
                }else {
                    showLoginDialog();
                }
                break;

        }
    }

    /*修改信息handler*/
    JsonHttpResponseHandler mEditInfoHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.i(TAG, "-----------------开始: 接受到JSONObject数据-----------------");
            Log.i(TAG, response + "");
            try {
                if (response.get("result").equals("error")) {
                    Toast.makeText(getActivity(),"修改失败:"+response.get("msg")+"",Toast.LENGTH_SHORT).show();
                } else if (response.get("result").equals("success")) {
                    HttpUtil.post(Constants.Apis.API_USER_LOGIN_POST
                            , null
                            , mLogInHandler);
                } else{
                    Toast.makeText(getActivity(),Constants.Values.VALUE_SERVICE_NO_RESPONSE,Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "-----------------结束: 接受到JSONObject数据-----------------");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

            Toast.makeText(getActivity(), Constants.Values.VALUE_SERVICE_NO_RESPONSE, Toast.LENGTH_LONG).show();
            /*throwable.printStackTrace();
            Log.i(TAG, " onFailure" + statusCode + " \n" + responseString.toString());*/
        }

        public void onFinish() {
            Log.i(TAG, "onFinish");
        }
    };

    /*显示修改用户信息dialog*/
    private void showEditInfoDialog() {
        try {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_edit_info, null);

            etNickName  = (EditText) dialogView.findViewById(R.id.et_nickname);
            etPhoneNum  = (EditText) dialogView.findViewById(R.id.et_phone_num);
            etQq        = (EditText) dialogView.findViewById(R.id.et_qq);
            etNickName.setText(userInfo.getString("nickname"));
            etPhoneNum.setText(userInfo.getString("phone"));
            etQq.setText(userInfo.getString("qq"));

            builderEditInfo
                    .setTitle(R.string.editInfo)
                    .setView(dialogView)
                    .setPositiveButton("确认修改",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //删除已有参数,防止参数过多
                                    if (params.has(Constants.Keys.KEY_NICKNAME))
                                        params.remove(Constants.Keys.KEY_NICKNAME);
                                    if (params.has(Constants.Keys.KEY_QQ))
                                        params.remove(Constants.Keys.KEY_QQ);
                                    if (params.has(Constants.Keys.KEY_PHONE))
                                        params.remove(Constants.Keys.KEY_PHONE);
                                    params.add(Constants.Keys.KEY_NICKNAME, etNickName.getText().toString());
                                    params.add(Constants.Keys.KEY_PHONE, etPhoneNum.getText().toString());
                                    params.add(Constants.Keys.KEY_QQ, etQq.getText().toString());
                                    HttpUtil.post(Constants.Apis.API_USER_CHANGE_INFO_POST
                                            , params
                                            , mEditInfoHandler);

                                }
                            })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /*显示登陆dialog*/
    public void showLoginDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_signin, null);
        builderLogin
                .setTitle(R.string.login)
                .setView(dialogView)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mEtUserName = (EditText) dialogView.findViewById(R.id.username);
                                mEtPassWord = (EditText) dialogView.findViewById(R.id.password);

                                //登录
//                                        params.add(Constants.Keys.KEY_USERNAME,"630248976@qq.com");
                                //params.add(Constants.Keys.KEY_PASSWORD,"Caspar1995.");
                                //删除已有参数,防止参数过多
                                if (params.has(Constants.Keys.KEY_USERNAME))
                                    params.remove(Constants.Keys.KEY_USERNAME);
                                if (params.has(Constants.Keys.KEY_PASSWORD))
                                    params.remove(Constants.Keys.KEY_PASSWORD);
                                params.add(Constants.Keys.KEY_USERNAME, mEtUserName.getText().toString());
                                params.add(Constants.Keys.KEY_PASSWORD, mEtPassWord.getText().toString());
                                HttpUtil.post(Constants.Apis.API_USER_LOGIN_POST
                                        , params
                                        , mLogInHandler);

                                Log.i(TAG, String.valueOf(mEtUserName.getText().toString().equals("630248976@qq.com")));
                                Log.i(TAG, params + "");

                            }
                        }).setNegativeButton("取消", null).create()
                .show();
    }



}