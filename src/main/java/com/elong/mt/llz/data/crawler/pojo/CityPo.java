package com.elong.mt.llz.data.crawler.pojo;

import java.util.Date;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 09:39:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public class CityPo {
    private Integer cityId;
    private String cityName;
    private String pinYin;
    private String url;
    private Date operateTime;
    private Integer hotelCount;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getHotelCount() {
        return hotelCount;
    }

    public void setHotelCount(Integer hotelCount) {
        this.hotelCount = hotelCount;
    }
}
