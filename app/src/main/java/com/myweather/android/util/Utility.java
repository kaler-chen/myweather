package com.myweather.android.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.myweather.android.db.City;
import com.myweather.android.db.County;
import com.myweather.android.db.Province;
import com.myweather.android.json.ForcastWeather;
import com.myweather.android.json.LifeIndex;
import com.myweather.android.json.NowWeather;

/**
 * json解析工具
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.util
 */

public class Utility {
    private static final String TAG = "Utility";

    /**
     * 解析省列表
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray provinceArray = JSON.parseArray(response);
                Province province = null;
                JSONObject provinceObj = null;
                for (int i = 0; i < provinceArray.size(); i++) {
                    provinceObj = (JSONObject) provinceArray.get(i);
                    province = new Province();
                    province.setProvinceCode(provinceObj.getString("id"));
                    province.setProvinceName(provinceObj.getString("name"));
                    province.save();
                }
                return true;
            } catch (Exception e) {
                LogUtil.e(TAG, "解析省列表失败：" + e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * 解析市列表
     * @param response
     * @return
     */
    public static boolean handleCityResponse(String response, String provinceCode){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray cityArray = JSON.parseArray(response);
                City city = null;
                JSONObject cityObj = null;
                for (int i = 0; i < cityArray.size(); i++) {
                    cityObj = (JSONObject) cityArray.get(i);
                    city = new City();
                    city.setCityCode(cityObj.getString("id"));
                    city.setCityName(cityObj.getString("name"));
                    city.setProvinceCode(provinceCode);
                    city.save();
                }
                return true;
            } catch (Exception e) {
                LogUtil.e(TAG, "解析市列表：" + e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * 解析县列表
     * @param response
     * @return
     */
    public static boolean handleCountyResponse(String response, String cityCode){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray countyArray = JSON.parseArray(response);
                County county = null;
                JSONObject countyObj = null;
                for (int i = 0; i < countyArray.size(); i++) {
                    countyObj = (JSONObject) countyArray.get(i);
                    county = new County();
                    county.setCountyCode(countyObj.getString("id"));
                    county.setCountyName(countyObj.getString("name"));
                    county.setCityCode(cityCode);
                    county.save();
                }
                return true;
            } catch (Exception e) {
                LogUtil.e(TAG, "解析县列表:" + e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * 解析天气实况数据
     * @param response
     * @return
     */
    public static NowWeather handleNowWeatherResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject responseObj = JSON.parseObject(response);
                if(responseObj.containsKey("status_code")){
                    LogUtil.w(TAG, "天气实况请求失败：" + responseObj.getString("status_code")
                            + responseObj.getString("status"), null);
                    return null;
                }
                JSONObject nowWeatherObj = responseObj.getJSONArray("results").getJSONObject(0);
                NowWeather nowWeather = JSON.parseObject(nowWeatherObj.toString(), NowWeather.class);
                return nowWeather;
            } catch (Exception e) {
                LogUtil.e(TAG, "解析天气实况数据失败：" + e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 解析天气预报数据
     * @param response
     * @return
     */
    public static ForcastWeather handleForcastWeatherResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject responseObj = JSON.parseObject(response);
                if(responseObj.containsKey("status_code")){
                    LogUtil.w(TAG, "天气预报请求失败：" + responseObj.getString("status_code")
                            + responseObj.getString("status"), null);
                    return null;
                }
                JSONObject forcastWeatherObj = responseObj.getJSONArray("results").getJSONObject(0);
                ForcastWeather forcastWeather = JSON.parseObject(forcastWeatherObj.toString(), ForcastWeather.class);
                return forcastWeather;
            } catch (Exception e) {
                LogUtil.e(TAG, "解析天气预报数据失败：" + e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 解析生活指数数据
     * @param response
     * @return
     */
    public static LifeIndex handleLifeIndexResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject responseObj = JSON.parseObject(response);
                if(responseObj.containsKey("status_code")){
                    LogUtil.w(TAG, "生活指数请求失败：" + responseObj.getString("status_code")
                            + responseObj.getString("status"), null);
                    return null;
                }
                JSONObject lifeIndexObj = responseObj.getJSONArray("results").getJSONObject(0);
                LifeIndex lifeIndex = JSON.parseObject(lifeIndexObj.toString(), LifeIndex.class);
                return lifeIndex;
            } catch (Exception e) {
                LogUtil.e(TAG, "解析生活指数数据失败：" + e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 解析Bing每日一图数据
     * @param response
     * @return
     */
    public static String handleBingPicResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject imgObj = JSON.parseObject(response).getJSONArray("images").getJSONObject(0);
                String bingUrl = PropertyUtil.getProp(ConstUtil.BING_URL);
                return bingUrl + imgObj.getString("url");
            } catch (Exception e) {
                LogUtil.e(TAG, "解析Bing每日一图数据失败：" + e.getMessage(), e);
            }
        }
        return null;
    }

}
