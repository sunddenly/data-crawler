package com.elong.mt.llz.data.crawler.dao;

import com.elong.mt.llz.data.crawler.pojo.HotelDetailPo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-25 09:35:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:conf/core/applicationContext*.xml"})
public class HotelDetailDaoTest {
    @Resource
    private HotelDetailDao hotelDetailDao;

    @Test
    public void test() {
        List<HotelDetailPo> hotelDetails = hotelDetailDao.getHotelDetails(215);
        System.out.println(hotelDetails);
    }
}
