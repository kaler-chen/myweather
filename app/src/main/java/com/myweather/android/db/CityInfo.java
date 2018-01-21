package com.myweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 所有城市信息(数据来源：心知天气)
 * Created by kaler-chen on 2018/1/21.
 * com.myweather.android.db
 */

public class CityInfo extends DataSupport {
    private int id;
    private String cityId;    //  城市ID
    private String cityName;    //  城市名
    private String cityEnName;    //  城市英文名
    private String countryName;    //  国家名
    private String countryCode;    //  国家代码
    private String firstLevelCityName;    //  行政归属1
    private String firstLevelCityEnName;    //  行政归属1(英文）
    private String secondLevelCityName;    //  行政归属2
    private String secondLevelCityEnName;    //  行政归属2(英文）
    private String timeZone;    //  时区
    private String cityLevel;    //  城市级别

    public CityInfo() {
    }

    public CityInfo(int id, String cityId, String cityName, String cityEnName, String countryName, String countryCode, String firstLevelCityName, String firstLevelCityEnName, String secondLevelCityName, String secondLevelCityEnName, String timeZone, String cityLevel) {
        this.id = id;
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityEnName = cityEnName;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.firstLevelCityName = firstLevelCityName;
        this.firstLevelCityEnName = firstLevelCityEnName;
        this.secondLevelCityName = secondLevelCityName;
        this.secondLevelCityEnName = secondLevelCityEnName;
        this.timeZone = timeZone;
        this.cityLevel = cityLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFirstLevelCityName() {
        return firstLevelCityName;
    }

    public void setFirstLevelCityName(String firstLevelCityName) {
        this.firstLevelCityName = firstLevelCityName;
    }

    public String getFirstLevelCityEnName() {
        return firstLevelCityEnName;
    }

    public void setFirstLevelCityEnName(String firstLevelCityEnName) {
        this.firstLevelCityEnName = firstLevelCityEnName;
    }

    public String getSecondLevelCityName() {
        return secondLevelCityName;
    }

    public void setSecondLevelCityName(String secondLevelCityName) {
        this.secondLevelCityName = secondLevelCityName;
    }

    public String getSecondLevelCityEnName() {
        return secondLevelCityEnName;
    }

    public void setSecondLevelCityEnName(String secondLevelCityEnName) {
        this.secondLevelCityEnName = secondLevelCityEnName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(String cityLevel) {
        this.cityLevel = cityLevel;
    }
}
