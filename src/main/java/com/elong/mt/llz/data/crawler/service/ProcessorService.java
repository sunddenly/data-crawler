package com.elong.mt.llz.data.crawler.service;

import com.elong.mt.llz.data.crawler.dao.CityDao;
import com.elong.mt.llz.data.crawler.dao.HotelDao;
import com.elong.mt.llz.data.crawler.dao.HotelDetailDao;
import com.elong.mt.llz.data.crawler.pojo.CityPo;
import com.elong.mt.llz.data.crawler.pojo.HotelDetailPo;
import com.elong.mt.llz.data.crawler.pojo.HotelPo;
import com.elong.mt.llz.data.crawler.pojo.entity.DuplicateKey;
import com.elong.mt.llz.data.crawler.utils.SpringContextUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * 爬取处理器服务
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 13:08:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public class ProcessorService {
    private static final int BATCH_COUNT = 50;

    public static List<Integer> getDetailHotelIds(){
        HotelDetailDao hotelDetailDao = getHotelDetailDao();
        return hotelDetailDao.getHotelIds();
    }

    public static void saveHotelDetails(List<HotelDetailPo> hotelDetailPos) {
        HotelDetailDao hotelDetailDao = getHotelDetailDao();
        hotelDetailDao.insertHotelDetails(hotelDetailPos);
    }

    public static List<Integer> getCityHotelIds(int cityId) {
        HotelDao hotelDao = getHotelDao();
        return hotelDao.getCityHotelIds(cityId);
    }

    public static List<CityPo> getAllCitys() {
        CityDao cityDao = getCityDao();
        return cityDao.getAllCityInfo();
    }

    public static void saveCity(CityPo cityPo) {
        CityDao cityDao = getCityDao();
        Integer cityId = cityDao.getCityId(cityPo.getCityId());
        if (cityId == null) {
            cityDao.insertCityData(cityPo);
        }
    }

    public static void batchSaveCitys(List<CityPo> cityPos) {
        CityDao cityDao = getCityDao();
        DuplicateKey<CityPo> key = new DuplicateKey<CityPo>() {
            @Override
            public Integer getKey(CityPo cityPo) {
                return cityPo.getCityId();
            }
        };
        cityPos = removeDuplicates(cityPos, key);
        List<List<CityPo>> partitions = Lists.partition(cityPos, BATCH_COUNT);
        for (List<CityPo> partition : partitions) {
            removeExistedCitys(partition);
            if (CollectionUtils.isNotEmpty(partition)) {
                cityDao.insertCityDatas(partition);
            }
        }
    }

    public static void saveHotel(HotelPo hotelPo) {
        HotelDao hotelDao = getHotelDao();
        Integer hotelId = hotelDao.getHotelId(hotelPo.getHotelId());
        if (hotelId != null) {
            hotelDao.updateHotelFloorPrice(hotelPo);
            return;
        }
        hotelDao.insertHotelData(hotelPo);
    }

    public static void batchSaveHotels(List<HotelPo> hotelPos) {
        HotelDao hotelDao = getHotelDao();
        DuplicateKey<HotelPo> key = new DuplicateKey<HotelPo>() {
            @Override
            public Integer getKey(HotelPo hotelPo) {
                return hotelPo.getHotelId();
            }
        };
        hotelPos = removeDuplicates(hotelPos, key);
        List<List<HotelPo>> partitions = Lists.partition(hotelPos, BATCH_COUNT);
        for (List<HotelPo> partition : partitions) {
            List<HotelPo> hotels = Lists.newArrayList(partition);
            List<HotelPo> existedHotels = getExistedHotels(hotels);
            if (CollectionUtils.isNotEmpty(existedHotels)) {
                for (HotelPo hotelPo : existedHotels) {
                    hotelDao.updateHotelFloorPrice(hotelPo);
                }
            }
            if (CollectionUtils.isNotEmpty(hotels)) {
                hotelDao.insertHotelDatas(hotels);
            }
        }
    }

    private static List<HotelPo>  getExistedHotels(List<HotelPo> hotelPos) {
        HotelDao hotelDao = getHotelDao();
        List<HotelPo> hotels = Lists.newArrayList();
        HashSet<Integer> hotelIds = hotelDao.getHotelIds(hotelPos);
        Iterator<HotelPo> iterator = hotelPos.iterator();
        while (iterator.hasNext()) {
            HotelPo hotelPo = iterator.next();
            if (hotelIds.contains(hotelPo.getHotelId())) {
                hotels.add(hotelPo);
                iterator.remove();
            }
        }
        return hotels;
    }

    private static void removeExistedCitys(List<CityPo> cityPos) {
        CityDao cityDao = getCityDao();
        HashSet<Integer> cityIds = cityDao.getCityIds(cityPos);
        Iterator<CityPo> iterator = cityPos.iterator();
        while (iterator.hasNext()) {
            CityPo cityPo = iterator.next();
            if (cityIds.contains(cityPo.getCityId())) {
                iterator.remove();
            }
        }
    }

    private static <T> List<T> removeDuplicates(List<T> datas, DuplicateKey<T> key) {
        Map<Integer, T> map = new HashMap<>();
        for (T data : datas) {
            map.put(key.getKey(data), data);
        }
        return Lists.newArrayList(map.values());
    }

    public static void updateHotelFloorPrice(HotelPo hotelPo) {
        HotelDao hotelDao = getHotelDao();
        hotelDao.updateHotelFloorPrice(hotelPo);
    }

    private static CityDao getCityDao() {
        return SpringContextUtil.getBean("cityDao");
    }

    private static HotelDao getHotelDao() {
        return SpringContextUtil.getBean("hotelDao");
    }

    private static HotelDetailDao getHotelDetailDao() {
        return SpringContextUtil.getBean("hotelDetailDao");
    }


}
