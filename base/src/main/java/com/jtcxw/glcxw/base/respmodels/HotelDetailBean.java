package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class HotelDetailBean {

    /**
     * HotelName : AA
     * Address : 上海市真南路150号B201室
     * TrafficMsg : 距桂林站直线1.3公里，驾车1.9公里，约4分钟
     * Lon : 121.783241
     * Lat : 31.3256732
     * Tel : 18521089065
     * HotFlag : 1
     * Distance : 560
     * HotelPhotoList : [{"PhotoUrl":"https://apis.71ad.com/.../IMG_10002.jpg","OrdNo":1},{"PhotoUrl":"https://apis.71ad.com/.../IMG_10001.jpg","OrdNo":2},{"PhotoUrl":"https://apis.71ad.com/.../IMG_10003.jpg","OrdNo":3}]
     * HotelIntroduce : <!DOCTYPE html><html>....</html>
     */

    private String HotelName;
    private String Address;
    private String TrafficMsg;
    private double Lon;
    private double Lat;
    private String Tel;
    private int HotFlag;
    private int Distance;
    private String HotelIntroduce;
    private List<HotelPhotoListBean> HotelPhotoList;
    private int IsCollection = 0;
    private String CollectionId;

    public String getCollectionId() {
        return CollectionId;
    }

    public void setCollectionId(String collectionId) {
        CollectionId = collectionId;
    }

    public int getIsCollection() {
        return IsCollection;
    }

    public void setIsCollection(int isCollection) {
        IsCollection = isCollection;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String HotelName) {
        this.HotelName = HotelName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getTrafficMsg() {
        return TrafficMsg;
    }

    public void setTrafficMsg(String TrafficMsg) {
        this.TrafficMsg = TrafficMsg;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double Lon) {
        this.Lon = Lon;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double Lat) {
        this.Lat = Lat;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public int getHotFlag() {
        return HotFlag;
    }

    public void setHotFlag(int HotFlag) {
        this.HotFlag = HotFlag;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int Distance) {
        this.Distance = Distance;
    }

    public String getHotelIntroduce() {
        return HotelIntroduce;
    }

    public void setHotelIntroduce(String HotelIntroduce) {
        this.HotelIntroduce = HotelIntroduce;
    }

    public List<HotelPhotoListBean> getHotelPhotoList() {
        return HotelPhotoList;
    }

    public void setHotelPhotoList(List<HotelPhotoListBean> HotelPhotoList) {
        this.HotelPhotoList = HotelPhotoList;
    }

    public static class HotelPhotoListBean {
        /**
         * PhotoUrl : https://apis.71ad.com/.../IMG_10002.jpg
         * OrdNo : 1
         */

        private String PhotoUrl;
        private int OrdNo;

        public String getPhotoUrl() {
            return PhotoUrl;
        }

        public void setPhotoUrl(String PhotoUrl) {
            this.PhotoUrl = PhotoUrl;
        }

        public int getOrdNo() {
            return OrdNo;
        }

        public void setOrdNo(int OrdNo) {
            this.OrdNo = OrdNo;
        }
    }
}
