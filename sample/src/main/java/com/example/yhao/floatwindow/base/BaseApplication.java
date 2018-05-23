package com.example.yhao.floatwindow.base;

import android.app.Application;

/**
 * Created by yhao on 2017/12/18.
 * https://github.com/yhaolpz
 */

public class BaseApplication extends Application {


    private static final String TAG = "FloatWindow";

    private static BaseApplication baseApplication;

    public static BaseApplication getInstant() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
    }
}
