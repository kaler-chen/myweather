package com.myweather.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.LogTime;
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

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forcastLayout;
    private TextView carWashingText;
    private TextView dressingText;
    private TextView fluText;
    private TextView sportText;
    private TextView travelText;
    private TextView uvText;
    private ImageView bingPicImg;

    private boolean[] finishFlags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forcastLayout = (LinearLayout) findViewById(R.id.forcast_layout);
        carWashingText = (TextView) findViewById(R.id.carWashing_text);
        dressingText = (TextView) findViewById(R.id.dressing_text);
        fluText = (TextView) findViewById(R.id.flu_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        travelText = (TextView) findViewById(R.id.travel_text);
        uvText = (TextView) findViewById(R.id.uv_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        finishFlags = new boolean[]{false, false, false};

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nowWeatherString = sharedPreferences.getString(ConstUtil.NOW_WEATHER, null);
        LogUtil.d(TAG, "nowWeatherString=" + nowWeatherString, null);
        if (nowWeatherString != null){
            NowWeather nowWeather = Utility.handleNowWeatherResponse(nowWeatherString);
            showNowWeatherInfo(nowWeather);
        }else{
            finishFlags[0] = false;
            String locationName = getIntent().getStringExtra(ConstUtil.LOCATION_NAME);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestNowWeather(locationName);
        }

        String dailyWeatherString = sharedPreferences.getString(ConstUtil.DAILY_WEATHER, null);
        LogUtil.d(TAG, "dailyWeatherString=" + dailyWeatherString, null);
        if (dailyWeatherString != null){
            ForcastWeather forcastWeather = Utility.handleForcastWeatherResponse(dailyWeatherString);
            showForcastWeatherInfo(forcastWeather);
        }else{
            finishFlags[1] = false;
            String locationName = getIntent().getStringExtra(ConstUtil.LOCATION_NAME);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestForcastWeather(locationName);
        }

        String lifeIndexString = sharedPreferences.getString(ConstUtil.LIFE_INDEX, null);
        LogUtil.d(TAG, "lifeIndexString=" + lifeIndexString, null);
        if (lifeIndexString != null){
            LifeIndex lifeIndex = Utility.handleLifeIndexResponse(lifeIndexString);
            showLifeIndexInfo(lifeIndex);
        }else{
            finishFlags[2] = false;
            String locationName = getIntent().getStringExtra(ConstUtil.LOCATION_NAME);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestLifeIndex(locationName);
        }

        String bingPic = sharedPreferences.getString(ConstUtil.BING_PIC, null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }

    }

    private void loadBingPic() {
        String bingPicUrl = PropertyUtil.getProp(ConstUtil.BING_PIC_URL);
        HttpUtil.sendOkHttpRequest(bingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取必应每日一图失败", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final String bingPic = Utility.handleBingPicResponse(responseText);
                SharedPreferences.Editor editor =
                        PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                        .edit();
                editor.putString(ConstUtil.BING_PIC, bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    private void requestNowWeather(String locationName) {
        String nowWeatherUrl = PropertyUtil.getProp(ConstUtil.NOW_WEATHER_URL);
        nowWeatherUrl = nowWeatherUrl.replace("#location#", locationName);
        HttpUtil.sendOkHttpRequest(nowWeatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取天气实况失败",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final NowWeather nowWeather = Utility.handleNowWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (nowWeather != null){
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this)
                                    .edit();
                            editor.putString(ConstUtil.NOW_WEATHER, responseText);
                            editor.apply();
                            showNowWeatherInfo(nowWeather);
                        }else{
                            Toast.makeText(WeatherActivity.this, "获取天气实况失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        loadBingPic();
    }

    private void requestForcastWeather(String locationName) {
        String forcastWeatherUrl = PropertyUtil.getProp(ConstUtil.DAILY_WEATHER_URL);
        forcastWeatherUrl = forcastWeatherUrl.replace("#location#",locationName);
        HttpUtil.sendOkHttpRequest(forcastWeatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取未来天气失败",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final ForcastWeather forcastWeather = Utility
                        .handleForcastWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (forcastWeather != null){
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this)
                                    .edit();
                            editor.putString(ConstUtil.DAILY_WEATHER, responseText);
                            editor.apply();
                            showForcastWeatherInfo(forcastWeather);
                        }else{
                            Toast.makeText(WeatherActivity.this, "获取未来天气失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void requestLifeIndex(String locationName) {
        String lifeIndexUrl = PropertyUtil.getProp(ConstUtil.LIFE_INDEX_URL);
        lifeIndexUrl = lifeIndexUrl.replace("#location#",locationName);
        HttpUtil.sendOkHttpRequest(lifeIndexUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取生活指数失败",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final LifeIndex lifeIndex = Utility.handleLifeIndexResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lifeIndex != null){
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this)
                                    .edit();
                            editor.putString(ConstUtil.LIFE_INDEX, responseText);
                            editor.apply();
                            showLifeIndexInfo(lifeIndex);
                        }else{
                            Toast.makeText(WeatherActivity.this, "获取生活指数失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showForcastWeatherInfo(ForcastWeather forcastWeather) {
        forcastLayout.removeAllViews();
        View view;
        TextView dateText,infoText,maxText,minText;
        for (ForcastWeather.Daily daily : forcastWeather.getDailyList()){
            view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.forcast_item, forcastLayout, false);
            dateText = view.findViewById(R.id.date_text);
            infoText = view.findViewById(R.id.info_text);
            maxText = view.findViewById(R.id.max_text);
            minText = view.findViewById(R.id.min_text);
            dateText.setText(daily.getDate());
            infoText.setText(daily.getTextDay());
            maxText.setText(daily.getHigh());
            minText.setText(daily.getLow());
            forcastLayout.addView(view);
        }
        finishFlags[1] = true;
        showWeatherLayout();
    }

    private void showNowWeatherInfo(NowWeather nowWeather) {
        String locationName = nowWeather.getLocation().getName();
        String updateTime = nowWeather.getUpdateTime().substring(11,16);
        String degree = nowWeather.getNow().getTemperature() + "℃";
        String weatherInfo = nowWeather.getNow().getText();
        titleCity.setText(locationName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        finishFlags[0] = true;
        showWeatherLayout();
    }

    private void showLifeIndexInfo(LifeIndex lifeIndex) {
        String carWashing = lifeIndex.getSuggestion().getCarWashing().getBrief();
        String dressing = lifeIndex.getSuggestion().getDressing().getBrief();
        String flu = lifeIndex.getSuggestion().getFlu().getBrief();
        String sport = lifeIndex.getSuggestion().getSport().getBrief();
        String travel = lifeIndex.getSuggestion().getTravel().getBrief();
        String uv = lifeIndex.getSuggestion().getUv().getBrief();
        carWashingText.setText("洗车指数    " + carWashing);
        dressingText.setText("穿衣指数    " + dressing);
        fluText.setText("感冒指数    " + flu);
        sportText.setText("运动指数    " + sport);
        travelText.setText("旅游指数    " + travel);
        uvText.setText("紫外线指数    " + uv);
        finishFlags[2] = true;
        showWeatherLayout();
    }

    private void showWeatherLayout() {
        for(int i = 0; i < finishFlags.length; i++){
            if(finishFlags[i] == false){
                return;
            }
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }

    public static void startAction(Context context, String locationName){
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(ConstUtil.LOCATION_NAME, locationName);
        context.startActivity(intent);
    }


}
