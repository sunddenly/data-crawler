package com.elong.mt.llz.data.crawler.service;

import com.elong.mt.llz.data.crawler.dao.CityDao;
import com.elong.mt.llz.data.crawler.pojo.CityPo;
import com.elong.mt.llz.data.crawler.pojo.vo.CityVo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 城市服务接口
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 15:48:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@Service
public class CityService {
    @Resource
    CityDao cityDao;

    public List<CityVo>  getAllCityInfo() {
        List<CityVo> cityVos = Lists.newArrayList();
        List<CityPo> allCityInfo = cityDao.getAllCityInfo();
        for (CityPo cityPo : allCityInfo) {
            CityVo cityVo = new CityVo();
            cityVo.setCityId(cityPo.getCityId());
            cityVo.setCityName(cityPo.getCityName());
            cityVo.setPinYin(cityPo.getPinYin());
            cityVos.add(cityVo);
        }
        return cityVos;
    }

}
