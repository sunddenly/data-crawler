package com.elong.mt.llz.data.crawler.processor;

import com.elong.mt.llz.data.crawler.pojo.CityPo;
import com.elong.mt.llz.data.crawler.service.ProcessorService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class CityProcessor implements PageProcessor{
    private Site site = Site
            .me()
            //设置域名，需设置域名后，addCookie才可生效，此例子无用
            .setDomain("m.ctrip.com/")
            .setSleepTime(10000)
            //设置重试次数
            .setRetryTimes(2)
            //设置循环重试次数
            .setCycleRetryTimes(2)
            //设置超时时间，单位是毫秒
            .setTimeOut(2000)
            //设置UserAgent（浏览器）
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

    @Override
    public Site getSite() {
        // TODO Auto-generated method stub
        return site;
    }

    /**
     * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
     * @param page
     */
    @Override
    public void process(Page page) {
        Selectable cityInfoSection = page.getHtml().xpath("//dl[@class=\"pinyin_filter_detail layoutfix\"]/dd");
        List<String> cityUrls = cityInfoSection.xpath("//a").links().all();
        List<String> cityNames =  cityInfoSection.xpath("//a/text()").all();

        if (cityUrls.size() != cityNames.size()){
            return;
        }

        for (int i = 0; i < cityUrls.size(); i++) {
            String url = cityUrls.get(i);
            if (url.contains("xiecheng22249")){
                continue;
            }
            String cityName = cityNames.get(i);
            int cityId = getCityId(url);
            String pinYin = getPinYin(url, String.valueOf(cityId).length());

            CityPo cityPo = new CityPo();
            cityPo.setCityId(cityId);
            cityPo.setCityName(cityName);
            cityPo.setPinYin(pinYin);
            cityPo.setUrl(url);
            ProcessorService.saveCity(cityPo);
        }

    }

    /**
     * 解析cityId
     * @param url
     * @return
     */
    public int getCityId(String url){
        int last = url.lastIndexOf("/");
        String subUrl = url.substring(last+1, url.length());
        String number = subUrl.replaceAll(".*[^\\d](?=(\\d+))","");
        return  Integer.parseInt(number);
    }

    /**
     * 解析拼音
     * @param url
     * @return
     */
    public static String getPinYin(String url, int idNum){
        int last = url.lastIndexOf("/");
        String subUrl = url.substring(last+1, url.length());
        String pinYin = subUrl.substring(0, subUrl.length()-idNum);
        return pinYin;
    }
}
