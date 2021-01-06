package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class ScenicBean {
    private List<ScenicInfoBean> ScenicInfoList;

    public List<ScenicInfoBean> getScenicInfoList() {
        return ScenicInfoList;
    }

    public void setScenicInfoList(List<ScenicInfoBean> scenicInfoList) {
        ScenicInfoList = scenicInfoList;
    }

    public static class ScenicInfoBean{
        private String ScenicId;
        private String ScenicName;
        private String Address;
        private String ScenicLevel;
        private String TrafficMsg;
        private double Lon;
        private double Lat;
        private int HotFlag;
        private long OrderNum;
        private long Distance;
        private String DefaultPhotoUrl;
        private String Price;

        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            Price = price;
        }

        public String getScenicId() {
            return ScenicId;
        }

        public void setScenicId(String scenicId) {
            ScenicId = scenicId;
        }

        public String getScenicName() {
            return ScenicName;
        }

        public void setScenicName(String scenicName) {
            ScenicName = scenicName;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getScenicLevel() {
            return ScenicLevel;
        }

        public void setScenicLevel(String scenicLevel) {
            ScenicLevel = scenicLevel;
        }

        public String getTrafficMsg() {
            return TrafficMsg;
        }

        public void setTrafficMsg(String trafficMsg) {
            TrafficMsg = trafficMsg;
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

        public long getOrderNum() {
            return OrderNum;
        }

        public void setOrderNum(long orderNum) {
            OrderNum = orderNum;
        }

        public long getDistance() {
            return Distance;
        }

        public void setDistance(long distance) {
            Distance = distance;
        }

        public String getDefaultPhotoUrl() {
            return DefaultPhotoUrl;
        }

        public void setDefaultPhotoUrl(String defaultPhotoUrl) {
            DefaultPhotoUrl = defaultPhotoUrl;
        }
    }
}
