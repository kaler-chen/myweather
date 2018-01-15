package com.myweather.android.json;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * 当前天气实体类
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.json
 */

public class NowWeather extends Base{

    private NowDaily now;       //天气实况

    public NowDaily getNow() {
        return now;
    }

    public void setNow(NowDaily now) {
        this.now = now;
    }

    public static class NowDaily{
        private String text;        //天气现象文字
        private String code;        //天气现象代码
        private String temperature;        //温度，单位为c摄氏度或f华氏度

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }
    }



}
