package com.myweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myweather.android.WeatherActivity;
import com.myweather.android.json.ForcastWeather;
import com.myweather.android.json.LifeIndex;
import com.myweather.android.json.NowWeather;
import com.myweather.android.util.ConstUtil;
import com.myweather.android.util.HttpUtil;
import com.myweather.android.util.LogUtil;
import com.myweather.android.util.PropertyUtil;
import com.myweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    private static final String TAG = "AutoUpdateService";
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        updateNowWeatherInfo();
        updateForcastWeatherInfo();
        updateLifeIndexInfo();
        updateBingPic();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int millis = 24 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + millis;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        alarmManager.cancel(pi);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {
        String bingPicUrl = PropertyUtil.getProp(ConstUtil.BING_PIC_URL);
        HttpUtil.sendOkHttpRequest(bingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, "更新必应每日一图失败:" + e.getMessage(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final String bingPic = Utility.handleBingPicResponse(responseText);
                SharedPreferences.Editor editor =
                        PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this)
                                .edit();
                editor.putString(ConstUtil.BING_PIC, bingPic);
                editor.apply();
            }
        });
    }

    /**
     * 更新天气实况
     */
    private void updateNowWeatherInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String locationName = sharedPreferences.getString(ConstUtil.LOCATION_NAME, null);
        if(locationName == null){
            return;
        }
        String nowWeatherUrl = PropertyUtil.getProp(ConstUtil.NOW_WEATHER_URL);
        nowWeatherUrl = nowWeatherUrl.replace("#location#", locationName);
        HttpUtil.sendOkHttpRequest(nowWeatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, "更新天气实况失败:" + e.getMessage(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                NowWeather nowWeather = Utility.handleNowWeatherResponse(responseText);
                if (nowWeather != null){
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(AutoUpdateService.this)
                            .edit();
                    editor.putString(ConstUtil.NOW_WEATHER, responseText);
                    editor.apply();
                }else{
                    LogUtil.e(TAG, "更新天气实况失败", null);
                }
            }
        });
    }

    /**
     * 更新天气预报
     */
    private void updateForcastWeatherInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String locationName = sharedPreferences.getString(ConstUtil.LOCATION_NAME, null);
        if(locationName == null){
            return;
        }
        String forcastWeatherUrl = PropertyUtil.getProp(ConstUtil.DAILY_WEATHER_URL);
        forcastWeatherUrl = forcastWeatherUrl.replace("#location#",locationName);
        HttpUtil.sendOkHttpRequest(forcastWeatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, "更新天气预报失败:" + e.getMessage(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                ForcastWeather forcastWeather = Utility  .handleForcastWeatherResponse(responseText);
                if (forcastWeather != null){
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(AutoUpdateService.this)
                            .edit();
                    editor.putString(ConstUtil.DAILY_WEATHER, responseText);
                    editor.apply();
                }else{
                    LogUtil.e(TAG, "更新天气预报失败", null);
                }
            }
        });
    }

    /**
     * 更新生活指数
     */
    private void updateLifeIndexInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String locationName = sharedPreferences.getString(ConstUtil.LOCATION_NAME, null);
        if(locationName == null){
            return;
        }
        String lifeIndexUrl = PropertyUtil.getProp(ConstUtil.LIFE_INDEX_URL);
        lifeIndexUrl = lifeIndexUrl.replace("#location#",locationName);
        HttpUtil.sendOkHttpRequest(lifeIndexUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, "更新生活指数失败:" + e.getMessage(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                LifeIndex lifeIndex = Utility.handleLifeIndexResponse(responseText);
                if (lifeIndex != null){
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(AutoUpdateService.this)
                            .edit();
                    editor.putString(ConstUtil.LIFE_INDEX, responseText);
                    editor.apply();
                }else{
                    LogUtil.e(TAG, "更新生活指数失败" , null);
                }
            }
        });
    }
}
