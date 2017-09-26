package com.elong.mt.llz.data.crawler.pojo.vo;

import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 19:26:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public class HotelVo {
    private String newUpdateTime;
    private Integer totalCount;
    private Integer toalPage;
    private List<HotelItemVo> hotelItemVos;

    public String getNewUpdateTime() {
        return newUpdateTime;
    }

    public void setNewUpdateTime(String newUpdateTime) {
        this.newUpdateTime = newUpdateTime;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getToalPage() {
        return toalPage;
    }

    public void setToalPage(Integer toalPage) {
        this.toalPage = toalPage;
    }

    public List<HotelItemVo> getHotelItemVos() {
        return hotelItemVos;
    }

    public void setHotelItemVos(List<HotelItemVo> hotelItemVos) {
        this.hotelItemVos = hotelItemVos;
    }
}
