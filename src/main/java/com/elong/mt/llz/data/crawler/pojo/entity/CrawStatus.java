package com.elong.mt.llz.data.crawler.pojo.entity;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 17:32:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public enum  CrawStatus {
    SUCCESS("成功", 0), FAIL("失败", 1), RUNNING("运行中", 2), UNSTART("未开始", 3);

    private String name;
    private int value;

    CrawStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
