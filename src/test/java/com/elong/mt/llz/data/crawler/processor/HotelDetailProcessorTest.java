package com.elong.mt.llz.data.crawler.processor;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-22 18:04:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:conf/core/applicationContext*.xml"})
public class HotelDetailProcessorTest {
    @Before
    public void init() {
        String path = this.getClass().getResource("/conf/core/log4j.xml").getPath();
        DOMConfigurator.configure(path);

    }

    @Test
    public void test() {
        HotelDetailProcessor crawlerTask = new HotelDetailProcessor("CrawlerTask", 3, 100);
        try {
            crawlerTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
