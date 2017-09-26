package com.elong.mt.llz.data.crawler.service;

import com.elong.mt.llz.data.crawler.pojo.entity.CrawStatus;
import com.elong.mt.llz.data.crawler.pojo.vo.CrawProcessVo;
import com.elong.mt.llz.data.crawler.processor.HotelDetailProcessor;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-19 23:42:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@Service
public class SpiderService {
    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    private volatile CrawStatus crawStatus = CrawStatus.UNSTART;

    private volatile HotelDetailProcessor processor = null;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    private final Object lock = new Object();

    @Resource
    private HotelService hotelService;

    public Boolean crawlingStart(Integer cityId) {
        synchronized (lock) {
            if (crawStatus == CrawStatus.RUNNING) {
                return false;
            }
            crawStatus = CrawStatus.RUNNING;
            processor = null;
            executorService.submit(new HotelDetailCrawlerTask(cityId));
            return true;
        }
    }

    public CrawProcessVo getCrawlingProcess(Integer cityId) {
        CrawProcessVo processVo = new CrawProcessVo();
        if (processor == null) {
            processVo.setHotelCount(0);
            processVo.setProcess("0.000");
            return processVo;
        }

        Integer lastCrawlingCount = hotelService.getCityHotelCount(cityId);

        int crawlingCount = processor.getCrawlingCount();

        if (crawStatus == CrawStatus.SUCCESS) {
            processVo.setHotelCount(crawlingCount);
            processVo.setProcess("100.000");
            return processVo;
        }

        if (crawlingCount >= lastCrawlingCount) {
            processVo.setHotelCount(crawlingCount);
            processVo.setProcess("99.000");
            return processVo;
        }

        if (crawlingCount < lastCrawlingCount) {
            DecimalFormat df = new DecimalFormat("00.000");
            String process = df.format(((float) crawlingCount / lastCrawlingCount)*100);
            processVo.setHotelCount(crawlingCount);
            processVo.setProcess(process);
            return processVo;
        }
        return null;
    }

    private int getCrawlingCount() {
        return 1;
    }

    private int getLastCrawlingCount() {
        return 1;
    }

    private class HotelDetailCrawlerTask implements Runnable {
        private Integer cityId;

        private HotelDetailCrawlerTask(Integer cityId) {
            this.cityId = cityId;
        }

        @Override
        public void run() {
            try {
                processor = new HotelDetailProcessor("crawlerTask", 3, 100) {
                    @Override
                    protected List<String> getCityIds() {
                        return Lists.newArrayList(cityId.toString());
                    }
                };
                processor.execute();
            } catch (Exception e) {
                logger.error("hotelDetail craw failed!");
            }
            synchronized (lock) {
                crawStatus = CrawStatus.SUCCESS;
            }
        }
    }


}
