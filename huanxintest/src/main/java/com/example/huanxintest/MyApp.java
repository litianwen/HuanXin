package com.example.huanxintest;

import android.app.Application;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by Administrator on 2017/3/5.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //UI
        EMOptions options = new EMOptions();
        EaseUI.getInstance().init(this, options);
    }
}
