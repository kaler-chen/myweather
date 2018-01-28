package com.myweather.android;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myweather.android.base.BaseActivity;

public class SpalshActivity extends BaseActivity {

    private TextView appNameText;
    private TextView appProfText;
    private ViewGroup advLayout;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent intent = new Intent(SpalshActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        appNameText = (TextView) findViewById(R.id.app_name_text);
        appProfText = (TextView) findViewById(R.id.app_prof_text);
        advLayout = (LinearLayout) findViewById(R.id.adv_layout);
        appNameText.setText(R.string.app_name);
        appProfText.setText(R.string.app_prof);

        mHandler.sendEmptyMessageDelayed(1, 3000);
    }
}
