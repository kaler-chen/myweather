package com.myweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 县 实体类
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.db
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String countyCode;
    private String cityCode;
    private String countyLevel;

    public County() {
    }

    public County(int id, String countyName, String countyCode, String cityCode, String countyLevel) {
        this.id = id;
        this.countyName = countyName;
        this.countyCode = countyCode;
        this.cityCode = cityCode;
        this.countyLevel = countyLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountyLevel() {
        return countyLevel;
    }

    public void setCountyLevel(String countyLevel) {
        this.countyLevel = countyLevel;
    }
}
