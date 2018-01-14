package com.myweather.android.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.myweather.android.db.City;
import com.myweather.android.db.County;
import com.myweather.android.db.Province;

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
                LogUtil.e(TAG, e.getMessage(), e);
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
                LogUtil.e(TAG, e.getMessage(), e);
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
                LogUtil.e(TAG, e.getMessage(), e);
            }
        }
        return false;
    }
}
