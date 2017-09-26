package com.elong.mt.llz.data.crawler.controller;

import com.elong.mt.llz.data.crawler.pojo.entity.Response;
import com.elong.mt.llz.data.crawler.pojo.vo.HotelDetailVo;
import com.elong.mt.llz.data.crawler.pojo.vo.HotelVo;
import com.elong.mt.llz.data.crawler.service.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 16:13:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/spider/hotel/")
public class HotelController {
    @Resource
    HotelService hotelService;

    @RequestMapping(value = "/{cityId}/{pageNumber}", method = RequestMethod.GET)
    @ResponseBody
    public Response<HotelVo> getHotelInfo(@PathVariable("cityId") Integer cityId, @PathVariable("pageNumber") Integer pageNumber) {
        HotelVo hotelVo = hotelService.getHotelInfos(cityId, pageNumber);
        Response<HotelVo> response = new Response<>();
        response.setCode(200);
        response.setMessage("获取成功!");
        response.setData(hotelVo);
        return response;
    }

    @RequestMapping(value = "/{hotelId}", method = RequestMethod.GET)
    @ResponseBody
    public Response<List<HotelDetailVo>> getHotelDetailInfo(@PathVariable("hotelId") Integer hotelId) {
        List<HotelDetailVo> hotelDetailInfos = hotelService.getHotelDetailInfos(hotelId);
        Response<List<HotelDetailVo>> response = new Response<>();
        response.setCode(200);
        response.setMessage("获取成功!");
        response.setData(hotelDetailInfos);
        return response;
    }


}
