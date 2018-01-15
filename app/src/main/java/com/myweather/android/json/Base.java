package com.myweather.android.json;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 天气预报基类
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.json
 */

public class Base {
    @JSONField(name = "last_update")
    private String updateTime;    //数据更新时间（该城市的本地时间）
    private Location location;  //城市信息

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static class Location{
        private String id;
        private String name;
        private String country;
        private String path;
        private String timezone;
        @JSONField(name = "timezone_offset")
        private String timezoneOffset;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(String timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }
    }
}
