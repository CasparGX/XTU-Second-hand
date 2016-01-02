package com.sky31.buy.second_hand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sky31.buy.second_hand.context.BuyApp;
import com.sky31.buy.second_hand.ui.HomeActivity;
import com.sky31.buy.second_hand.ui.activity.BaseActivity;


public class MainActivity extends BaseActivity {

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
                MainActivity.this.finish();    // 结束启动动画界面
                MainActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        }, delayTime);    //启动动画
    }
}
