package com.myweather.android.json;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 生活指数实体类
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.json
 */

public class LifeIndex extends Base {

    private Suggestion suggestion;

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public static class Suggestion{
        @JSONField(name = "car_washing")
        private Suggest carWashing;    //洗车
        private Suggest dressing;       //穿衣
        private Suggest flu;            //感冒
        private Suggest sport;          //运动
        private Suggest travel;         //旅游
        private Suggest uv;             //紫外线

        public Suggest getCarWashing() {
            return carWashing;
        }

        public void setCarWashing(Suggest carWashing) {
            this.carWashing = carWashing;
        }

        public Suggest getDressing() {
            return dressing;
        }

        public void setDressing(Suggest dressing) {
            this.dressing = dressing;
        }

        public Suggest getFlu() {
            return flu;
        }

        public void setFlu(Suggest flu) {
            this.flu = flu;
        }

        public Suggest getSport() {
            return sport;
        }

        public void setSport(Suggest sport) {
            this.sport = sport;
        }

        public Suggest getTravel() {
            return travel;
        }

        public void setTravel(Suggest travel) {
            this.travel = travel;
        }

        public Suggest getUv() {
            return uv;
        }

        public void setUv(Suggest uv) {
            this.uv = uv;
        }
    }

    public static class Suggest{
        private String brief;
        private String details;

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }


}
