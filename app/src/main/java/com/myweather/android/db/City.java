package com.myweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 市 实体类
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.db
 */

public class City extends DataSupport {
    private int id;
    private String cityName;
    private String cityCode;
    private String provinceCode;

    public City(int id, String cityName, String cityCode, String provinceCode) {
        this.id = id;
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.provinceCode = provinceCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
