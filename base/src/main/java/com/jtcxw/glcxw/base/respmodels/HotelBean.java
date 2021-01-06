package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class HotelBean {
    private List<HotelInfoListBean> HotelInfoList;

    public List<HotelInfoListBean> getHotelInfoList() {
        return HotelInfoList;
    }

    public void setHotelInfoList(List<HotelInfoListBean> hotelInfoList) {
        HotelInfoList = hotelInfoList;
    }

    public static class  HotelInfoListBean {
        private String HotelId;
        private String HotelName;
        private String Address;
        private String TrafficMsg;
        private double Lon;
        private double Lat;
        private String Tel;
        private int HotFlag;
        private long OrderNum;
        private String DefaultPhotoUrl;
        private long Distance;
        private String Price;

        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            this.Price = price;
        }

        public String getHotelId() {
            return HotelId;
        }

        public void setHotelId(String hotelId) {
            HotelId = hotelId;
        }

        public String getHotelName() {
            return HotelName;
        }

        public void setHotelName(String hotelName) {
            HotelName = hotelName;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
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

        public String getTel() {
            return Tel;
        }

        public void setTel(String tel) {
            Tel = tel;
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

        public String getDefaultPhotoUrl() {
            return DefaultPhotoUrl;
        }

        public void setDefaultPhotoUrl(String defaultPhotoUrl) {
            DefaultPhotoUrl = defaultPhotoUrl;
        }

        public long getDistance() {
            return Distance;
        }

        public void setDistance(long distance) {
            Distance = distance;
        }
    }
}
