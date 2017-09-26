package com.elong.mt.llz.data.crawler.dao;

import com.elong.mt.llz.data.crawler.pojo.CityPo;
import com.elong.mt.llz.data.crawler.pojo.HotelPo;
import com.elong.mt.llz.data.crawler.pojo.entity.Page;
import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 11:04:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public interface HotelDao {
    Integer getHotelCountByCityId(Integer cityId);

    List<HotelPo> getHotelByPage(Page page);

    Integer getHotelId(int hotelId);

    HashSet<Integer> getHotelIds(@Param("hotels") List<HotelPo> hotels);

    void insertHotelData(HotelPo hotel);

    void insertHotelDatas(@Param("hotels") List<HotelPo> hotels);

    void updateHotelFloorPrice(HotelPo hotel);

    List<CityPo> getCityHotelCount(int cityId);

    List<Integer> getCityHotelIds(int cityId);
}
