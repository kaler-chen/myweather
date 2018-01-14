package com.myweather.android.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 网络请求工具类
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.util
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
