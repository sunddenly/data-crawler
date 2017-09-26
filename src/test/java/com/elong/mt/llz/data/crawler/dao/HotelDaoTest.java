package com.elong.mt.llz.data.crawler.dao;

import com.elong.mt.llz.data.crawler.pojo.CityPo;
import com.elong.mt.llz.data.crawler.pojo.HotelPo;
import com.elong.mt.llz.data.crawler.pojo.entity.Page;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 11:27:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:conf/core/applicationContext*.xml"})
public class HotelDaoTest {
    @Resource
    private HotelDao hotelDao;

    @Test
    public void getHotelDataTest() throws Exception {
        List<HotelPo> allHotelData = hotelDao.getHotelByPage(new Page(1,1));
        System.out.println(allHotelData);
    }

    @Test
    public void insertHotelDataTest() throws Exception {
        HotelPo hotelPo = new HotelPo();
        hotelPo.setHotelId(452197);
        hotelPo.setCityId(1);
        hotelPo.setHotelName("北京");
        hotelPo.setHotelType("舒适型");
        hotelPo.setScore(4.0);
        hotelPo.setAddress("朝阳区东四环十里堡北里28号，近朝阳北路农民日报社往南");
        hotelPo.setFloorPrice(1538);
        hotelPo.setUrl("http://hotels.ctrip.com/hotel/452197.html");
        hotelDao.insertHotelData(hotelPo);
    }

    @Test
    public void insertHotelDatasTest() throws Exception {
        HotelPo hotelPo = new HotelPo();
        hotelPo.setHotelId(452198);
        hotelPo.setCityId(1);
        hotelPo.setHotelName("北京");
        hotelPo.setHotelType("舒适型");
        hotelPo.setScore(4.0);
        hotelPo.setAddress("朝阳区东四环十里堡北里28号，近朝阳北路农民日报社往南");
        hotelPo.setFloorPrice(1538);
        hotelPo.setUrl("http://hotels.ctrip.com/hotel/452198.html");

        HotelPo hotelPo1 = new HotelPo();
        hotelPo1.setHotelId(452199);
        hotelPo1.setCityId(2);
        hotelPo1.setHotelName("上海");
        hotelPo1.setHotelType("舒适型");
        hotelPo1.setScore(4.0);
        hotelPo1.setAddress("上海东四环十里堡北里28号，近朝阳北路农民日报社往南");
        hotelPo1.setFloorPrice(538);
        hotelPo1.setUrl("http://hotels.ctrip.com/hotel/452199.html");

        hotelDao.insertHotelDatas(Lists.newArrayList(hotelPo, hotelPo1));
    }

    @Test
    public void updateHotelFloorPriceTest() throws Exception {
        HotelPo hotelPo = new HotelPo();
        hotelPo.setHotelId(215);
        hotelPo.setFloorPrice(313);
        hotelDao.updateHotelFloorPrice(hotelPo);
    }

    @Test
    public void getHotelIdTest(){
        Integer hotelId = hotelDao.getHotelId(452198);
        System.out.println(hotelId);
    }

    @Test
    public void getHotelIdsTest(){
        HotelPo hotelPo = new HotelPo();
        hotelPo.setHotelId(452198);

        HotelPo hotelPo1 = new HotelPo();
        hotelPo1.setHotelId(452199);

        HashSet<Integer> hotelIds = hotelDao.getHotelIds(Lists.<HotelPo>newArrayList(hotelPo, hotelPo1));
        System.out.println(hotelIds);
    }

    @Test
    public void getHotelCountTest(){
        List<CityPo> cityHotelCount = hotelDao.getCityHotelCount(1);
        System.out.println(cityHotelCount);
    }


}
