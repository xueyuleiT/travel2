package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class ScenicDetailBean {

    private  String ScenicName;
    private  String ScenicLevel;
    private  String Address;
    private  double Lon;
    private  double Lat;
    private  int HotFlag;
    private long Distance;
    private String TrafficMsg;
    private String Tel;
    private String ScenicIntroduce;
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

    public String getScenicIntroduce() {
        return ScenicIntroduce;
    }

    public void setScenicIntroduce(String scenicIntroduce) {
        ScenicIntroduce = scenicIntroduce;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getTrafficMsg() {
        return TrafficMsg;
    }

    public void setTrafficMsg(String trafficMsg) {
        TrafficMsg = trafficMsg;
    }

    private List<ScenicPhotoListBean> ScenicPhotoList;

    public String getScenicName() {
        return ScenicName;
    }

    public void setScenicName(String scenicName) {
        ScenicName = scenicName;
    }

    public String getScenicLevel() {
        return ScenicLevel;
    }

    public void setScenicLevel(String scenicLevel) {
        ScenicLevel = scenicLevel;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public int getHotFlag() {
        return HotFlag;
    }

    public void setHotFlag(int hotFlag) {
        HotFlag = hotFlag;
    }

    public long getDistance() {
        return Distance;
    }

    public void setDistance(long distance) {
        Distance = distance;
    }

    public List<ScenicPhotoListBean> getScenicPhotoList() {
        return ScenicPhotoList;
    }

    public void setScenicPhotoList(List<ScenicPhotoListBean> scenicPhotoList) {
        ScenicPhotoList = scenicPhotoList;
    }



    public static class ScenicPhotoListBean {
        private long OrdNo;
        private String PhotoUrl;

        public long getOrdNo() {
            return OrdNo;
        }

        public void setOrdNo(long ordNo) {
            OrdNo = ordNo;
        }

        public String getPhotoUrl() {
            return PhotoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            PhotoUrl = photoUrl;
        }
    }
}
