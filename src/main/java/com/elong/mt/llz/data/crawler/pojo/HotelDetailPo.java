package com.elong.mt.llz.data.crawler.pojo;

import java.util.Date;

/**
 * @Author　　　　xinyu.jiang
 * @Date　　　　　2017-09-22 16:38:00
 * @Department　　HotelProduct
 * @Copyright (c) 1999 - 2017 艺龙网信息技术（北京）有限公司
 */
public class HotelDetailPo {
    private int hotelId;
    private String roomName;
    private String bedType;
    private String roomSize;
    private int price;
    private String memo;
    private Date operateTime;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public String toString() {
        return "HotelDetailPo{" + "hotelId=" + hotelId + ", roomName='" + roomName + '\'' + ", bedType='" + bedType + '\'' + ", roomSize='" + roomSize + '\'' + ", price=" + price + ", memo='" + memo + '\'' + ", operateTime=" + operateTime + '}';
    }
}


