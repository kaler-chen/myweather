package com.myweather.android.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件工具类
 * Created by kaler-chen on 2018/1/15.
 * com.myweather.android.util
 */

public class PropertyUtil {
    private static final String TAG = "PropertyUtil";

    private static Properties properties;

    static {
        properties = new Properties();
        try {
            InputStream is = MApplication.getContext().getAssets().open("appConfig");
            properties.load(is);
        } catch (IOException e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }

    public static String getProp(String propName){
        return properties.getProperty(propName);
    }

}
