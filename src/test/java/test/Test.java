package test;

import com.elong.mt.llz.data.crawler.processor.HotelProcessor;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Spider;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-26 13:11:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:conf/core/applicationContext*.xml"})
public class Test {

    @org.junit.Test
    public void test() {
       new Spider(new HotelProcessor()).addUrl("http://hotels.ctrip.com/hotel/beijing1/p1").thread(5).run();
    }
}
