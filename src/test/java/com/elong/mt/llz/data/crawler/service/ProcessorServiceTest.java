package com.elong.mt.llz.data.crawler.service;

import com.elong.mt.llz.data.crawler.pojo.CityPo;
import com.elong.mt.llz.data.crawler.pojo.HotelPo;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-21 11:05:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:conf/core/applicationContext*.xml"})
public class ProcessorServiceTest {
    @Test
    public void saveCityTest() {
        CityPo cityPo = new CityPo();
        cityPo.setCityId(12);
        cityPo.setCityName("sd");
        cityPo.setUrl("http");
        cityPo.setPinYin("shfsdh");
        ProcessorService.saveCity(cityPo);
    }

    @Test
    public void batchSaveCityTest() {
        List<CityPo> cityPos = Lists.newArrayListWithCapacity(100);
        for (int i = 1; i < 101; i++) {
            CityPo cityPo = new CityPo();
            cityPo.setCityId(i);
            cityPo.setCityName("城市"+1);
            cityPo.setUrl("http");
            cityPo.setPinYin("shfsdh");
            if (i == 20) {
                cityPos.add(cityPo);
                cityPos.add(cityPo);
            }
            cityPos.add(cityPo);
        }
        ProcessorService.batchSaveCitys(cityPos);
    }

    @Test
    public void saveHotelTest() {
        HotelPo hotelPo = new HotelPo();
        hotelPo.setHotelId(123);
        hotelPo.setHotelName("北京城市"+2);
        hotelPo.setUrl("http");
        hotelPo.setScore(4.5);
        hotelPo.setAddress("qeq");
        hotelPo.setCityId(1);
        hotelPo.setFloorPrice(123);
        hotelPo.setHotelType("type");
        ProcessorService.saveHotel(hotelPo);
    }

    @Test
    public void batchSaveHotelTest() {
        List<HotelPo> hotelPos = Lists.newArrayList();
        for (int i = 1; i < 101; i++) {
            HotelPo hotelPo = new HotelPo();
            hotelPo.setHotelId(123+i);
            hotelPo.setHotelName("city"+1);
            hotelPo.setUrl("http");
            hotelPo.setScore(4.5);
            hotelPo.setAddress("qeq");
            hotelPo.setCityId(1);
            hotelPo.setFloorPrice(123);
            hotelPo.setHotelType("type");
            if (i ==1) {
                hotelPos.add(hotelPo);
                hotelPos.add(hotelPo);
                hotelPos.add(hotelPo);
            }
            hotelPos.add(hotelPo);
        }
        ProcessorService.batchSaveHotels(hotelPos);
    }

    @Test
    public void getAllCityInfo() {
        List<CityPo> allCitys = ProcessorService.getAllCitys();
        System.out.println(allCitys);
    }



}
