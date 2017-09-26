package com.elong.mt.llz.data.crawler.pojo.entity;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 15:02:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public interface DuplicateKey<V> {
    Integer getKey(V data);
}
