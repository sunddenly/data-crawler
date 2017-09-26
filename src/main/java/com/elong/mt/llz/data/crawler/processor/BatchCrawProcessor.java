package com.elong.mt.llz.data.crawler.processor;

import com.elong.mt.llz.data.crawler.pojo.execption.EmptyCrawSourcesException;
import com.elong.mt.llz.data.crawler.utils.ListUtil;
import com.elong.mt.llz.data.crawler.utils.TimeUtil;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import com.sun.istack.internal.Nullable;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 批量爬取处理
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-22 14:53:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public abstract class BatchCrawProcessor<T> {
    protected static final Logger logger = LoggerFactory.getLogger(BatchCrawProcessor.class);

    private String taskName;
    private int parallelCount;
    private int batchCount;
    private volatile boolean stopped = false;
    private ListeningExecutorService executor;

    public BatchCrawProcessor(String taskName, int parallelCount, int batchCount) {
        this.taskName = taskName;
        this.parallelCount = parallelCount;
        this.batchCount = batchCount;
        this.executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(this.parallelCount));
    }

    /**
     * 爬取执行入口
     * @throws Exception
     */
    public void execute() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<T> crawSources = getAllCrawSources();
        if (CollectionUtils.isEmpty(crawSources)) {
            throw new EmptyCrawSourcesException("data list is empty.");
        }
        long[] times = TimeUtil.costTime(stopwatch, 0);
        logger.info("get all crawSources size is: {}, consuming Time: {}", crawSources.size(), times[0]);

        List<List<T>> sourceGroup = ListUtil.partitionGroup(crawSources, parallelCount);
        List<ListenableFuture<?>> futures = Lists.newArrayList();
        for (int group = 0, size = sourceGroup.size(); group < size; group++) {
            CrawlerTask crawlerTask = new CrawlerTask();
            crawlerTask.setCrawlerName(getCrawlerName(group));
            crawlerTask.setSourceList(sourceGroup.get(group));
            ListenableFuture<?> future = executor.submit(crawlerTask);
            futures.add(future);
        }
        try {
            waitAllThreadFinish(futures);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * 爬取操作,根据具体场景来实现
     * @param sourceList 要爬取的资源列表
     * @param batchCount 每次爬取的批次大小
     * @param crawlerName 爬取任务名称
     * @throws Exception
     */
    protected abstract void craw(List<T> sourceList, int batchCount ,String crawlerName) throws Exception;

    /**
     * 等待所有线程执行完成(默认行为, 可根据实际情况覆盖)
     * @param futures 所有线程的future列表
     * @throws Exception 异常
     */
    protected void waitAllThreadFinish(List<ListenableFuture<?>> futures) throws Exception {
        waitEvenExceptionThenThrow(futures);
    }

    /**
     * 强制等待所有线程执行完成, 如果有异常在执行完毕后抛出
     * @param futures 所有线程的future列表
     * @throws Exception 异常
     */
    private void waitEvenExceptionThenThrow(List<ListenableFuture<?>> futures) throws Exception {
        final List<Throwable> errors = Collections.synchronizedList(Lists.<Throwable>newArrayList());
        for (int i = 0, futuresSize = futures.size(); i < futuresSize; i++) {
            ListenableFuture<?> future = futures.get(i);
            final String crawName = getCrawlerName(i);
            Futures.addCallback(future, new FutureCallback<Object>() {
                @Override
                public void onSuccess(@Nullable Object o) {

                }

                @Override
                public void onFailure(@Nullable Throwable throwable) {
                    if (throwable == null) {
                        return;
                    }
                    logger.error("{} craw data thread has error.", crawName, throwable);
                    errors.add(throwable);
                }
            });
        }
        ListenableFuture<List<Object>> allResultFuture = Futures.successfulAsList(futures);

        try {
            allResultFuture.get(90, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            logger.error("{} craw data in multi thread time out, and task stop.", taskName);
            throw e;
        }

        if (CollectionUtils.isNotEmpty(errors)) {
            Throwable defError = errors.get(0);
            throw new Exception(defError);
        }
    }

    /**
     * 爬取任务
     */
    private class CrawlerTask implements Runnable{
        private String crawlerName;
        private List<T> sourceList = new ArrayList<>();

        @Override
        public void run() {
            logger.info(crawlerName + " start ... ");
            Stopwatch stopwatch = Stopwatch.createStarted();
            if (Thread.currentThread().isInterrupted() || isStopped()) {
                logger.info(crawlerName + " stopped and thread end.");
                return;
            }
            try {
                craw(sourceList, batchCount, crawlerName);
            } catch (Exception e) {
                logger.error(crawlerName + " error and thread end.", e);
                throw new RuntimeException(e);
            }
            logger.info(crawlerName + " end ... , timeConsuming : {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }

        private void setCrawlerName(String crawlerName) {
            this.crawlerName = crawlerName;
        }

        private void setSourceList(List<T> sourceList) {
            this.sourceList = sourceList;
        }
    }

    /**
     * 获取所有爬取资源
     * @return list 资源列表
     */
    protected abstract List<T> getAllCrawSources();

    String getCrawlerName(int threadIndex) {
        return taskName + " [Thread-" + threadIndex + "]";
    }

    public void stop() {
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }

    public ListeningExecutorService getExecutor() {
        return executor;
    }

}
