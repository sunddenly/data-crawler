package com.elong.mt.llz.data.crawler.pojo.entity;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 15:51:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public class HotelPage extends Page {
    private Integer cityId;

    public HotelPage(Integer page, Integer pageSize, Integer cityId) {
        super(page, pageSize);
        this.cityId = cityId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
