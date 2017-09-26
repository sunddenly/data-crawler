package com.elong.mt.llz.data.crawler.service;

import com.elong.mt.llz.data.crawler.dao.HotelDao;
import com.elong.mt.llz.data.crawler.dao.HotelDetailDao;
import com.elong.mt.llz.data.crawler.pojo.HotelDetailPo;
import com.elong.mt.llz.data.crawler.pojo.HotelPo;
import com.elong.mt.llz.data.crawler.pojo.entity.HotelPage;
import com.elong.mt.llz.data.crawler.pojo.entity.Page;
import com.elong.mt.llz.data.crawler.pojo.vo.HotelDetailVo;
import com.elong.mt.llz.data.crawler.pojo.vo.HotelItemVo;
import com.elong.mt.llz.data.crawler.pojo.vo.HotelVo;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-20 15:48:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
@Service
public class HotelService {
    @Resource
    HotelDao hotelDao;

    @Resource
    HotelDetailDao hotelDetailDao;

    public List<HotelDetailVo> getHotelDetailInfos(int hotelId) {
        List<HotelDetailPo> hotelDetails = hotelDetailDao.getHotelDetails(hotelId);
        if (hotelDetails == null) {
            return Collections.emptyList();
        }
        return hotelDetailPo2Vo(hotelDetails);
    }

    private List<HotelDetailVo> hotelDetailPo2Vo(List<HotelDetailPo> hotelDetails) {
        if (CollectionUtils.isEmpty(hotelDetails)) {
            Collections.emptyList();
        }

        List<HotelDetailVo> hotelDetailVos = Lists.newArrayList();
        for (HotelDetailPo hotelDetail : hotelDetails) {
            HotelDetailVo hotelDetailVo = new HotelDetailVo();
            hotelDetailVo.setHotelId(hotelDetail.getHotelId());
            hotelDetailVo.setRoomName(hotelDetail.getRoomName());
            hotelDetailVo.setBedType(hotelDetail.getBedType());
            hotelDetailVo.setPrice(hotelDetail.getPrice());
            hotelDetailVo.setRoomSize(hotelDetail.getRoomSize() == null ? "" : hotelDetail.getRoomSize());
            hotelDetailVo.setMemo(hotelDetail.getMemo());
            hotelDetailVos.add(hotelDetailVo);
        }
        return hotelDetailVos;
    }

    public HotelVo getHotelInfos(Integer cityId, Integer pageNumber) {
        HotelVo hotelVo = new HotelVo();
        Integer totalRows = getCityHotelCount(cityId);
        if (totalRows == 0) {
            hotelVo.setToalPage(0);
            hotelVo.setNewUpdateTime("");
            hotelVo.setTotalCount(0);
            hotelVo.setHotelItemVos(new ArrayList<HotelItemVo>());
            return hotelVo;
        }

        int totalPage = Page.getTotalPage(totalRows, 15);
        HotelPage page = new HotelPage(pageNumber, 15, cityId);
        page.setPage(pageNumber);
        page.setTotalRow(totalRows);
        page.setTotalPage(totalPage);

        List<HotelPo> hotelPos = hotelDao.getHotelByPage(page);
        hotelVo.setToalPage(totalPage);
        hotelVo.setTotalCount(totalRows);
        hotelPo2Vo(hotelPos, hotelVo);

        return hotelVo;
    }

    public Integer getCityHotelCount(Integer cityId) {
        return hotelDao.getHotelCountByCityId(cityId);
    }

    private void hotelPo2Vo(List<HotelPo> hotelPos, HotelVo hotelVo) {
        String newUpdateTime = DateUtils.formatDate(hotelPos.get(0).getOperateTime(), "yyyy-MM-dd HH:mm:ss");
        hotelVo.setNewUpdateTime(newUpdateTime);
        if (CollectionUtils.isEmpty(hotelPos)) {
            return;
        }
        List<HotelItemVo> hotelItemVos = Lists.newArrayList();
        for (HotelPo hotelPo : hotelPos) {
            HotelItemVo hotelItemVo = new HotelItemVo();
            hotelItemVo.setHotelId(hotelPo.getHotelId());
            hotelItemVo.setCityId(hotelPo.getCityId());
            hotelItemVo.setHotelName(hotelPo.getHotelName());
            hotelItemVo.setHotelType(hotelPo.getHotelType());
            hotelItemVo.setScore(hotelPo.getScore());
            hotelItemVo.setAddress(hotelPo.getAddress());
            hotelItemVo.setFloorPrice(hotelPo.getFloorPrice());
            hotelItemVo.setUrl(hotelPo.getUrl());
            hotelItemVo.setOperateTime(hotelPo.getOperateTime());
            hotelItemVos.add(hotelItemVo);
        }
        hotelVo.setHotelItemVos(hotelItemVos);
    }
}
