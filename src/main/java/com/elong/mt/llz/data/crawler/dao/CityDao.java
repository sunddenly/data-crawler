package com.elong.mt.llz.data.crawler.dao;

import com.elong.mt.llz.data.crawler.pojo.CityPo;
import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 09:38:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public interface CityDao {
    List<CityPo> getAllCityInfo();

    Integer getCityId(int cityId);

    HashSet<Integer> getCityIds(@Param("citys") List<CityPo> citys);

    void insertCityData(CityPo cityPo);

    void insertCityDatas(@Param("citys") List<CityPo> citys);
}
