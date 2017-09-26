package com.elong.mt.llz.data.crawler.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class TimeUtil {
    /**
     * 耗时字符串(ms)
     **/
    public static String getTimeCost(long startTime) {
        return String.valueOf(System.currentTimeMillis() - startTime) + "ms";
    }

    public static long getCostTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * 计算执行时间, 指从当前流逝时间到开始时间的耗时。返回长度为2的long数组, long[0]是执行时间, long[1]是流逝时间
     *
     * @param stopwatch 计时器
     * @param startTime 开始时间
     * @return 长度为2的long数组, long[0]是执行时间, long[1]是流逝时间
     */
    public static long[] costTime(Stopwatch stopwatch, long startTime) {
        Preconditions.checkNotNull(stopwatch);
        long[] times = new long[2];
        times[1] = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        times[0] = times[1] - startTime;
        return times;
    }
}