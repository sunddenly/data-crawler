package com.elong.mt.llz.data.crawler.controller;

import com.elong.mt.llz.data.crawler.pojo.entity.Response;
import com.elong.mt.llz.data.crawler.pojo.vo.CityVo;
import com.elong.mt.llz.data.crawler.service.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 15:44:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/spider/city/")
public class CityController {
    @Resource
    private CityService cityService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Response<List<CityVo>> getCityList() {
        List<CityVo> allCityInfo = cityService.getAllCityInfo();
        Response<List<CityVo>> response = new Response<>();
        response.setCode(200);
        response.setMessage("获取成功!");
        response.setData(allCityInfo);
        return response;
    }
}
