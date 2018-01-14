package com.myweather.android.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * 全局获取容器Context
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.util
 */

public class MApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //初始化Litepal
        LitePalApplication.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
