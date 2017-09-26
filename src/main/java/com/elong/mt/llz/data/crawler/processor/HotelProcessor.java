package com.elong.mt.llz.data.crawler.processor;

import com.elong.mt.llz.data.crawler.pojo.HotelPo;
import com.elong.mt.llz.data.crawler.service.ProcessorService;
import com.google.common.collect.Lists;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HotelProcessor implements PageProcessor {
    private volatile boolean startProcess = true;
    private Site site = Site.me()
            //设置域名，需设置域名后，addCookie才可生效，此例子无用
            .setDomain("m.ctrip.com/").setSleepTime(10000)
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
     *
     * @param page
     */
    @Override
    public void process(Page page) {
        Selectable hotelInfoSection = page.getHtml().xpath("//div[@id=\"hotel_list\"]");
        List<String> hotelNames = hotelInfoSection.xpath("//h2[@class=\"hotel_name\"]/a/text()").all();
        List<String> hotelUrls = hotelInfoSection.xpath("//h2[@class=\"hotel_name\"]/a").links().all();
        List<String> hotelAddressTemp = hotelInfoSection.xpath("//p[@class=\"hotel_item_htladdress\"]/text()").all();
        List<String> hotelLowPrice = hotelInfoSection.xpath("//div[@class=\"hotel_price \"]/a/span/text()|" +
                "div[@class=\"hotel_price price_out\"]/a/span/text()").all();
        List<String> hotelStartSection = hotelInfoSection.xpath("//li[@class=\"hotel_item_name\"]/span").all();
        List<String> hotelScores = hotelInfoSection.xpath("//div[@class=\"hotelitem_judge_box\"]/a" +
                "/span[@class=\"hotel_value\"]/text()|span[@class=\"no_grade\"]/text()").all();
        List<Integer> hotelIds = getHotelIds(hotelUrls);
        List<String> hotelAddress = getAddress(hotelAddressTemp);
        List<String> hotelScales = getHotelScales(hotelStartSection);
        List<Integer> floorPrice = changeToInteger(hotelLowPrice);
        List<Double> score = changeToDouble(hotelScores);

        try {
            insertIntoDatabase(hotelIds, 1, hotelNames, hotelScales, score, hotelAddress, floorPrice, hotelUrls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (startProcess) {
            //解析第一页中的页码
            int pageNum = getPageNum(page);
            String pageLink = getPageLink(page);
            //解析第一页之后各页的酒店信息
            List<String> pageUrls = Lists.newArrayList();
            for (int i = 2; i <= pageNum; ++i) {
                pageUrls.add(getPageUrl(i, pageLink));
            }
            page.addTargetRequests(pageUrls);
            startProcess = false;
        }
    }

    public  int sleepTime(){
        double d = Math.random();
        int i = (int) d*6000 + 1000;
        return i;
    }

    /**
     * 将List<String>转换为List<Integer>
     * @param stringList
     * @return
     */
    public List<Integer> changeToInteger(List<String> stringList){
        List<Integer> integerList= new ArrayList<Integer>();
        for (String str:stringList){
            integerList.add(Integer.parseInt(str));
        }
        return integerList;
    }

    /**
     * 将List<String>转换为List<Double>
     * @param stringList
     * @return
     */
    public List<Double> changeToDouble(List<String> stringList){
        List<Double> doubleList= new ArrayList<Double>();
        for (String str:stringList){
            if (str.contains("暂无评分")){
                doubleList.add(0.0);
            }else {
                doubleList.add(Double.valueOf(str));
            }
        }
        return doubleList;
    }

    /**
     * 解析酒店id
     * @param urls
     * @return
     */
    public List<Integer> getHotelIds(List<String> urls){
        List<Integer> hotelIds = new LinkedList<Integer>();
        Iterator<String> urlIterator = urls.iterator();
        while (urlIterator.hasNext()){
            String url = urlIterator.next();
            int start = url.lastIndexOf("/");
            int end = url.indexOf(".html");
            String idString = url.substring(start+1, end);
            Integer id = Integer.parseInt(idString);
            hotelIds.add(id);
        }

        return hotelIds;
    }

    /**
     * 解析每个酒店的规模
     * @param hotelStartSection
     * @return
     */
    public List<String> getHotelScales (List<String> hotelStartSection){
        List<String> hotelScales = new LinkedList<String>();
        Iterator<String> hotelScaleIterator = hotelStartSection.iterator();
        while (hotelScaleIterator.hasNext()){
            String hotelScaleSelection = hotelScaleIterator.next();
            if (hotelScaleSelection.startsWith("<span title=")){
                int start = hotelStartSection.indexOf("<span title=\"");
                int end = hotelScaleSelection.indexOf("\" class=");
                if (end< 15){
                    String hotelScale = "  ";
                    hotelScales.add(hotelScale);
                }else {
                    String hotelScale = hotelScaleSelection.substring(13, end);
                    hotelScales.add(hotelScale);
                }
            }
        }
        return hotelScales;
    }

    /**
     * 对酒店地址进行解析
     * @param addressTemp
     * @return
     */
    public List<String> getAddress(List<String> addressTemp){
        Iterator<String> addressIterator = addressTemp.iterator();
        List<String> returnList = new LinkedList<>();
        while (addressIterator.hasNext()){
            String temp = addressIterator.next();
            if (temp.startsWith("【") || temp.startsWith(" 【")){
                int end = temp.indexOf("】");
                temp = temp.substring(end+1,temp.length());
            }
            returnList.add(temp);
        }
        return returnList;
    }


    /**
     * 解析页码信息
     *
     * @param page
     */
    private int getPageNum(Page page) {
        Selectable hotelPageSection = page.getHtml().xpath("//div[@class=\"c_page_list layoutfix\"]");
        String pageNume = hotelPageSection.xpath("////a[@rel=\"nofollow\"]/text()").toString();
        return Integer.parseInt(pageNume);
    }


    /**
     * 解析页码信息
     *
     * @param page
     */
    private String getPageLink(Page page) {
        Selectable hotelPageSection = page.getHtml().xpath("//div[@class=\"c_page_list layoutfix\"]");
        return hotelPageSection.xpath("//a[@rel=\"nofollow\"]").links().toString();
    }


    /**
     * 得到各个页面的URL
     *
     * @param num
     * @param url
     * @return
     */
    private String getPageUrl(int num, String url) {
        int last = url.lastIndexOf("/p");
        return url.substring(0, last + 2) + String.valueOf(num);
    }

    public void insertIntoDatabase(List<Integer> hotelIds, int cityId, List<String> hotelNames,
                                   List<String> hotelTypes, List<Double> scores, List<String> address,
                                   List<Integer> floorPrice, List<String> urls) throws Exception {

        if (hotelIds.size() != hotelNames.size() || hotelNames.size() != hotelTypes.size() ||
                hotelTypes.size() != floorPrice.size() || floorPrice.size() != scores.size() ||
                scores.size() != address.size() || address.size() != urls.size()){
            System.out.println("酒店解析错误");
            return;
        }

        for (int i =0; i<hotelIds.size(); ++i){
            HotelPo hotelPo = new HotelPo();
            hotelPo.setHotelId(hotelIds.get(i));
            hotelPo.setCityId(cityId);
            hotelPo.setHotelName(hotelNames.get(i));
            hotelPo.setHotelType(hotelTypes.get(i));
            hotelPo.setScore(scores.get(i));
            hotelPo.setAddress(address.get(i));
            hotelPo.setFloorPrice(floorPrice.get(i));
            hotelPo.setUrl(urls.get(i));
            ProcessorService.saveHotel(hotelPo);
        }
    }


}

