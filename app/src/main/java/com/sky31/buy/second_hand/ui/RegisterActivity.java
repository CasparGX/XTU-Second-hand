package com.sky31.buy.second_hand.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sky31.buy.second_hand.R;
import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.context.values.Constants;
import com.sky31.buy.second_hand.ui.activity.SwipeBackActivity2;
import com.sky31.buy.second_hand.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends SwipeBackActivity2 implements View.OnClickListener {

    private Intent mIntent;

    private Context context;

    private ImageView iv_back_btn;
    private TextView tvHeaderTitle;

    private EditText etNickName;
    private EditText etPhoneNum;
    private EditText etQq;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;
        mIntent = getIntent();

        //设置状态栏颜色
        //BuyApp.setStatusBarColor(RegisterActivity.this);
        /*滑动返回*/
        /*mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);*/

        initWidget();
        setTvHeaderTitle();
    }

    private void initWidget() {

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        etNickName = (EditText) findViewById(R.id.et_nickname);
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        etQq = (EditText) findViewById(R.id.et_qq);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        iv_back_btn = (ImageView) findViewById(R.id.iv_back_btn);
        iv_back_btn.setOnClickListener(this);

        tvHeaderTitle = (TextView) findViewById(R.id.tv_header_title);
    }

    /*修改header标题*/
    private void setTvHeaderTitle() {
        String title = mIntent.hasExtra("headerTitle")
                ? mIntent.getStringExtra("headerTitle") : getResources().getString(R.string.app_title);
        tvHeaderTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /* register */
            case R.id.btn_register:
                if (etNickName.getText().toString().equals("") || etPassword.getText().toString().equals("")
                        || etEmail.getText().toString().equals("") || (etPhoneNum.getText().toString().equals("") || etQq.getText().toString().equals(""))) {
                    Toast.makeText(this, "信息填写不完整", Toast.LENGTH_SHORT).show();
                } else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    Toast.makeText(this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                } else if (etQq.getText().toString().equals("") && etPhoneNum.getText().toString().equals("")) {
                    Toast.makeText(this, "至少填一项联系方式", Toast.LENGTH_SHORT).show();
                } else {
                    register(etEmail.getText().toString()
                            , etPassword.getText().toString()
                            , etNickName.getText().toString()
                            , etPhoneNum.getText().toString()
                            , etQq.getText().toString());
                }
                break;

            /* back */
            case R.id.iv_back_btn:
                onBackPressed();
                break;
        }
    }


    private void register(String email, String password, String nickName, String phoneNum, String qq) {
        RequestParams params = new RequestParams();
        params.add(Constants.Keys.KEY_EMAIL, email);
        params.add(Constants.Keys.KEY_PASSWORD, password);
        params.add(Constants.Keys.KEY_NICKNAME, nickName);
        params.add(Constants.Keys.KEY_PHONE, phoneNum);
        params.add(Constants.Keys.KEY_QQ, qq);
        HttpUtil.post(Constants.Apis.API_USER_REGISTER
                , params
                , mRegisterHandler);
    }

    JsonHttpResponseHandler mRegisterHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

            try {
                if (response.get("result").equals("error")) {
                    Toast.makeText(RegisterActivity.this, "注册失败:" + response.get("msg") + "", Toast.LENGTH_SHORT).show();
                } else if (response.get("result").equals("success")) {
                    //register success
                    Intent in = new Intent();
                    in.putExtra(Constants.Keys.KEY_USERNAME, etEmail.getText().toString());
                    in.putExtra(Constants.Keys.KEY_PASSWORD, etPassword.getText().toString());
                    setResult(Constants.Values.RESULT_REGISTER, in);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, Constants.Values.VALUE_SERVICE_NO_RESPONSE, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
