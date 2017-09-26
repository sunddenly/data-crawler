package com.elong.mt.llz.data.crawler.processor;

import com.elong.mt.llz.data.crawler.pojo.HotelDetailPo;
import com.elong.mt.llz.data.crawler.pojo.HotelPo;
import com.elong.mt.llz.data.crawler.service.ProcessorService;
import com.elong.mt.llz.data.crawler.utils.PropertiesUtil;
import com.elong.mt.llz.data.crawler.utils.TimeUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 详情页面爬取，采用BatchCrawProcessor
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-22 15:30:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */

public class HotelDetailProcessor extends BatchCrawProcessor<Integer> {
    private static final String url = "http://m.ctrip.com/html5/hotel/hoteldetail/";

    private volatile AtomicInteger hotelCount = new AtomicInteger(0);

    public HotelDetailProcessor(String taskName, int parallelCount, int batchCount) {
        super(taskName, parallelCount, batchCount);
    }

    @Override
    public void craw(List<Integer> sourceList, int batchCount, String crawlerName) throws Exception {

        WebDriver webDriver = null;
        try {
            List<List<Integer>> batchSources = Lists.partition(sourceList, batchCount);
            for (int i = 0, size = batchSources.size(); i < size; i++) {
                String batchName = crawlerName + " [batch-" + i + "]";
                if (Thread.currentThread().isInterrupted() || isStopped()) {
                    logger.info(batchName + " stopped and thread end.");
                    return;
                }

                Stopwatch stopwatch = Stopwatch.createStarted();
                List<Integer> batchSource = batchSources.get(i);
                logger.info(batchName + " start. batch data count={}, startHotelId={}, endHotelId={}", batchSource.size(), batchSource.get(0), batchSource.get(batchSource.size() - 1));

                webDriver = getWebDriver(webDriver);
                long[] times = TimeUtil.costTime(stopwatch, 0);
                logger.info("{} get getWebDriver time consuming : {}", batchName, times[0]);

                List<HotelDetailPo> batchHotelDetailPos = Lists.newArrayList();
                for (Integer hotelId : batchSource) {
                    List<HotelDetailPo> hotelDetailPos = crawHotelDetail(webDriver, hotelId);
                    updateHotelFloorPrice(hotelId, hotelDetailPos);
                    hotelCount.incrementAndGet();
                    batchHotelDetailPos.addAll(hotelDetailPos);
                }
                times = TimeUtil.costTime(stopwatch, times[1]);
                logger.info("{} craw {} data , time consuming : {}", batchName, batchHotelDetailPos.size(), times[0]);


                if (CollectionUtils.isNotEmpty(batchHotelDetailPos)) {
                    ProcessorService.saveHotelDetails(batchHotelDetailPos);
                    times = TimeUtil.costTime(stopwatch, times[1]);
                    logger.info("{} insert {} data to db, time consuming : {}", batchName, batchHotelDetailPos.size(), times[0]);
                }
                batchHotelDetailPos.clear();
                times = TimeUtil.costTime(stopwatch, times[1]);
                logger.info(batchName + " end. batch data count={}, startHotelId={}, endHotelId={}, time consuming : {}", batchSources.size(), batchSource.get(0), batchSource.get(batchSource.size() - 1), stopwatch.elapsed(TimeUnit.MILLISECONDS));

            }
        } finally {
            webDriver.quit();
        }

    }

    /**
     * 更新列表页最低价
     * @param hotelId
     * @param hotelDetailPos
     */
    private void updateHotelFloorPrice(Integer hotelId, List<HotelDetailPo> hotelDetailPos) {
        if (CollectionUtils.isEmpty(hotelDetailPos)) {
            return;
        }
        Collections.sort(hotelDetailPos, new Comparator<HotelDetailPo>() {
            @Override
            public int compare(HotelDetailPo o1, HotelDetailPo o2) {
                return o1.getPrice() - o2.getPrice();
            }
        });
        HotelPo hotelPo = new HotelPo();
        hotelPo.setHotelId(hotelId);
        hotelPo.setFloorPrice(hotelDetailPos.get(0).getPrice());
        ProcessorService.updateHotelFloorPrice(hotelPo);
    }

    @Override
    protected List<Integer> getAllCrawSources() {
        List<String> cityIds = getCityIds();
        List<Integer> cityHotelIds = new ArrayList<>();
        for (String cityId : cityIds) {
            cityHotelIds.addAll(ProcessorService.getCityHotelIds(NumberUtils.toInt(cityId)));
        }
        List<Integer> detailHotelIds = ProcessorService.getDetailHotelIds();
        Set<Integer> detailHotelIdSet = Sets.newHashSet(detailHotelIds);
        Iterator<Integer> iterator = cityHotelIds.iterator();
        while (iterator.hasNext()) {
            Integer hotelId = iterator.next();
            if (detailHotelIdSet.contains(hotelId)) {
                iterator.remove();
            }
        }
        return cityHotelIds;
    }

    protected List<String> getCityIds() {
        String cityIds = PropertiesUtil.getStringValue("craw.detail.cityIds");
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        return splitter.splitToList(cityIds);
    }

    /**
     * 爬取单个酒店的详情页面
     * @param webDriver
     * @param hotelId
     * @return
     */
    private List<HotelDetailPo> crawHotelDetail(WebDriver webDriver, int hotelId) {
        List<HotelDetailPo> HotelDetailPos = Lists.newArrayList();
        try {
            String crawUrl = url + ".html";
            webDriver.get(crawUrl);
            List<WebElement> lis = webDriver.findElements(By.cssSelector(".m-room.m-room--tile > li"));
            for (int i=0; i < lis.size(); i++) {
                WebElement li = lis.get(i);
                try {
                    String roomId = li.getAttribute("data-roomid");
                    WebElement h3 = li.findElement(By.cssSelector(".room-bd :first-child"));
                    WebElement span = li.findElement(By.className("js-cas-p"));
                    WebElement roomSpace = li.findElement(By.cssSelector(".room-bd :nth-child(2)"));
                    List<WebElement> elements = roomSpace.findElements(By.tagName("em"));
                    StringBuilder memo = new StringBuilder();
                    String bedType = "无";
                    String roomSize = "无";
                    for (WebElement element : elements) {
                        String info = element.getText();
                        if (info.indexOf("m²") > 0) {
                            roomSize = info;
                            continue;
                        }
                        if (info.indexOf("床") > 0) {
                            bedType = info;
                            continue;
                        }
                        memo.append(info + ",");
                    }
                    HotelDetailPo hotelDetailPo = new HotelDetailPo();
                    hotelDetailPo.setHotelId(hotelId);
                    hotelDetailPo.setBedType(bedType);
                    hotelDetailPo.setRoomSize(roomSize);
                    hotelDetailPo.setMemo(memo.toString().substring(0,memo.length()));
                    hotelDetailPo.setRoomName(h3.getText());
                    hotelDetailPo.setPrice(NumberUtils.toInt(span.getText()));
                    HotelDetailPos.add(hotelDetailPo);
                } catch (Exception e) {
                    logger.error("hotelId={} and liIndex={} crawling error", hotelId, i, e);
                }
            }
        } catch (Exception e) {
            logger.error("hotelId={} crawling error", hotelId, e);
        }
        return HotelDetailPos;
    }

    /**
     * 获取浏览器驱动
     * @return WebDriver
     */
    private WebDriver getWebDriver() {
        DesiredCapabilities dcaps = new DesiredCapabilities();//设置必要参数
        dcaps.setCapability("cssSelectorsEnabled", true);//css搜索支持
        dcaps.setJavascriptEnabled(true);//js支持
        dcaps.setCapability("takesScreenshot", false);
        ArrayList<String> cliArgsCap = new ArrayList<String>();
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        cliArgsCap.add("--load-images=false");
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PropertiesUtil.getStringValue("phantomjs.driver.path"));//驱动支持
        return new PhantomJSDriver(dcaps);
    }

    private WebDriver getWebDriver(WebDriver webDriver) {
        if (webDriver != null) {
            webDriver.quit();
        }
        return getWebDriver();
    }

    public int getCrawlingCount() {
        return hotelCount.get();
    }

    protected void add(int num) {
        hotelCount.getAndAdd(num);
    }
}
