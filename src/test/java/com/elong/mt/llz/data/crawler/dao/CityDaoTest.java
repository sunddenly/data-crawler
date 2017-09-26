package com.elong.mt.llz.data.crawler.dao;

import com.elong.mt.llz.data.crawler.pojo.CityPo;
import com.elong.mt.llz.data.crawler.pojo.entity.Page;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 10:01:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:conf/core/applicationContext*.xml"})
public class CityDaoTest {
    @Resource
    private CityDao cityDao;

    @Test
    public void getAllCityDataTest() throws Exception {
        Page page = new Page(2, 1);
        List<CityPo> allCityData = cityDao.getAllCityInfo();
        System.out.println(allCityData);
    }

    @Test
    public void insertCityDataTest() throws Exception {
        CityPo cityPo = new CityPo();
        cityPo.setCityId(1);
        cityPo.setCityName("北京");
        cityPo.setPinYin("beijing");
        cityPo.setUrl("http://hotels.ctrip.com/hotel/beijing1");
        cityDao.insertCityData(cityPo);
    }

    @Test
    public void insertCityDatasTest() throws Exception {
        CityPo cityPo = new CityPo();
        cityPo.setCityId(59);
        cityPo.setCityName("澳门");
        cityPo.setPinYin("macau");
        cityPo.setUrl("http://hotels.ctrip.com/hotel/macau59");

        CityPo cityPo1 = new CityPo();
        cityPo1.setCityId(178);
        cityPo1.setCityName("鞍山");
        cityPo1.setPinYin("anshan");
        cityPo1.setUrl("http://hotels.ctrip.com/hotel/anshan178");

        ArrayList<CityPo> cityPos = Lists.newArrayList(cityPo, cityPo1);

        cityDao.insertCityDatas(cityPos);
    }

    @Test
    public void getCityIdTest() throws Exception {
        Integer cityId = cityDao.getCityId(1);
        System.out.println(cityId);
    }

    @Test
    public void getCityIdsTest() throws Exception {
        CityPo cityPo1 = new CityPo();
        cityPo1.setCityId(1);
        CityPo cityPo2 = new CityPo();
        cityPo2.setCityId(2);
        Set<Integer> cityIds = cityDao.getCityIds(Lists.newArrayList(cityPo1,cityPo2));
        System.out.println(cityIds);
    }
}
