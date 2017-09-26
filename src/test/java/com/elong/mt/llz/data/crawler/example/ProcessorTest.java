package com.elong.mt.llz.data.crawler.example;

import com.elong.mt.llz.data.crawler.utils.PropertiesUtil;
import org.junit.Test;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-18 15:37:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public class ProcessorTest {
    @Test
    public void test() {
        String stringValue = PropertiesUtil.getStringValue("phantomjs.driver.path");
        System.out.println(stringValue);
    }


}
