package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class TicketBean {
    private List<TikmodelListBean> tikmodel_list;

    public List<TikmodelListBean> getTikmodel_list() {
        return tikmodel_list;
    }

    public void setTikmodel_list(List<TikmodelListBean> tikmodel_list) {
        this.tikmodel_list = tikmodel_list;
    }

    public static class TikmodelListBean {
        /**
         * ride_time : 2020-10-30 18:00
         * total_seats : 21
         * leave_seats : 21
         * tikmodel_id : 4069
         * tikmodel_detail_id : 28352
         * from_station_id : 1501
         * to_station_id : 1502
         * tik_price : 0.01
         */

        private String ride_time;
        private int total_seats;
        private int leave_seats;
        private int tikmodel_id;
        private int tikmodel_detail_id;
        private int from_station_id;
        private int to_station_id;
        private double tik_price;

        public String getRide_time() {
            return ride_time;
        }

        public void setRide_time(String ride_time) {
            this.ride_time = ride_time;
        }

        public int getTotal_seats() {
            return total_seats;
        }

        public void setTotal_seats(int total_seats) {
            this.total_seats = total_seats;
        }

        public int getLeave_seats() {
            return leave_seats;
        }

        public void setLeave_seats(int leave_seats) {
            this.leave_seats = leave_seats;
        }

        public int getTikmodel_id() {
            return tikmodel_id;
        }

        public void setTikmodel_id(int tikmodel_id) {
            this.tikmodel_id = tikmodel_id;
        }

        public int getTikmodel_detail_id() {
            return tikmodel_detail_id;
        }

        public void setTikmodel_detail_id(int tikmodel_detail_id) {
            this.tikmodel_detail_id = tikmodel_detail_id;
        }

        public int getFrom_station_id() {
            return from_station_id;
        }

        public void setFrom_station_id(int from_station_id) {
            this.from_station_id = from_station_id;
        }

        public int getTo_station_id() {
            return to_station_id;
        }

        public void setTo_station_id(int to_station_id) {
            this.to_station_id = to_station_id;
        }

        public double getTik_price() {
            return tik_price;
        }

        public void setTik_price(double tik_price) {
            this.tik_price = tik_price;
        }
    }
}
