package com.elong.mt.llz.data.crawler.controller;

import com.elong.mt.llz.data.crawler.pojo.entity.CrawStartInfo;
import com.elong.mt.llz.data.crawler.pojo.entity.Response;
import com.elong.mt.llz.data.crawler.pojo.vo.CrawProcessVo;
import com.elong.mt.llz.data.crawler.service.SpiderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-19 20:03:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/spider/crawling/")
public class SpiderController {

    @Resource
    private SpiderService spiderService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Response<Boolean> crawlingStart(@RequestBody CrawStartInfo cityInfo) {
        Response<Boolean> response = new Response<Boolean>();
        if (spiderService.crawlingStart(cityInfo.getCityId())) {
            response.setCode(200);
            response.setMessage("启动成功!");
            response.setData(true);
            return response;
        }
        response.setCode(1);
        response.setMessage("启动失败!");
        response.setData(false);
        return response;
    }

    @RequestMapping(value = "/status/{cityId}", method = RequestMethod.GET)
    @ResponseBody
    public Response<CrawProcessVo> getCrawlingStatus(@PathVariable("cityId") Integer cityId) {
        CrawProcessVo processVo = spiderService.getCrawlingProcess(cityId);
        Response<CrawProcessVo> response = new Response<CrawProcessVo>();
        response.setCode(200);
        response.setMessage("查询成功!");
        response.setData(processVo);
        return response;
    }

}
