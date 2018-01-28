package com.myweather.android.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.myweather.android.util.LogUtil;

/**
 * Activity基类
 * Created by kaler-chen on 2018/1/21.
 * com.myweather.android.base
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, getClass().getSimpleName(), null);
        ActivityColletor.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityColletor.removeActivity(this);
    }


}
