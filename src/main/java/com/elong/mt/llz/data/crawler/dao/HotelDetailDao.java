package com.elong.mt.llz.data.crawler.dao;

import com.elong.mt.llz.data.crawler.pojo.HotelDetailPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 11:04:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public interface HotelDetailDao {
    List<HotelDetailPo> getHotelDetails(int hotelId);

    void insertHotelDetails(@Param("hotels") List<HotelDetailPo> hotelDetailPos);

    void deleteHotelDetails(@Param("hotelIds") List<Integer> hotelIds);

    List<Integer> getHotelIds();
}
