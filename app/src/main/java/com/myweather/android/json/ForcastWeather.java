package com.myweather.android.json;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 天气预报实体类
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.json
 */

public class ForcastWeather extends Base{

    @JSONField(name = "daily")
    private List<Daily> dailyList;   //天气预报列表

    public List<Daily> getDailyList() {
        return dailyList;
    }

    public void setDailyList(List<Daily> dailyList) {
        this.dailyList = dailyList;
    }

    /**
     * 每日天气预报信息
     */
    public static class Daily{
        private String date;            //日期 2018-01-14
        @JSONField(name = "text_day")
        private String textDay;         //白天天气现象文字
        private String codeDay;         //白天天气现象代码
        private String textNight;       //晚间天气现象文字
        private String codeNight;       //晚间天气现象代码
        private String high;            //当天最高温度
        private String low;             //当天最低温度
        private String precip;          //降水概率，范围0~100，单位百分比
        @JSONField(name = "wind_direction")
        private String windDirection;       //风向文字
        @JSONField(name = "wind_direction_degree")
        private String windDirectionDegree; //风向角度，范围0~360
        @JSONField(name = "wind_speed")
        private String windSpeed;        //风速，单位km/h（当unit=c时）、mph（当unit=f时）
        @JSONField(name = "wind_scale")
        private String windScale;       //风力等级

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTextDay() {
            return textDay;
        }

        public void setTextDay(String textDay) {
            this.textDay = textDay;
        }

        public String getCodeDay() {
            return codeDay;
        }

        public void setCodeDay(String codeDay) {
            this.codeDay = codeDay;
        }

        public String getTextNight() {
            return textNight;
        }

        public void setTextNight(String textNight) {
            this.textNight = textNight;
        }

        public String getCodeNight() {
            return codeNight;
        }

        public void setCodeNight(String codeNight) {
            this.codeNight = codeNight;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getWindDirectionDegree() {
            return windDirectionDegree;
        }

        public void setWindDirectionDegree(String windDirectionDegree) {
            this.windDirectionDegree = windDirectionDegree;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }

        public String getWindScale() {
            return windScale;
        }

        public void setWindScale(String windScale) {
            this.windScale = windScale;
        }
    }

}
