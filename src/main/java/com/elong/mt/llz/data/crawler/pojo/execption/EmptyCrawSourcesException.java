package com.elong.mt.llz.data.crawler.pojo.execption;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-22 15:53:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public class EmptyCrawSourcesException extends Exception{
    private static final long serialVersionUID = -2908855836449761068L;

    public EmptyCrawSourcesException() {
    }

    public EmptyCrawSourcesException(String message) {
        super(message);
    }

    public EmptyCrawSourcesException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyCrawSourcesException(Throwable cause) {
        super(cause);
    }
}
