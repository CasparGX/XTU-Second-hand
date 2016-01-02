package com.sky31.buy.second_hand.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by root on 16-1-2.
 */
public class SwipeBackActivity2 extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
