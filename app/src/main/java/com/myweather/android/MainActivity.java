package com.myweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myweather.android.util.ConstUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nowWeatherString = sharedPreferences.getString(ConstUtil.NOW_WEATHER, null);
        if (nowWeatherString != null){
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra(ConstUtil.NOW_WEATHER,nowWeatherString);
            startActivity(intent);
            finish();
        }
    }
}
