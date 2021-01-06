package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class OrderListBean {
    private List<OrderBean> order_list;

    public List<OrderBean> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(List<OrderBean> order_list) {
        this.order_list = order_list;
    }

    public static class OrderBean {
        /**
         * order_id : 1000211349
         * order_type : 32
         * order_state : 0
         * state_name : 未支付
         * order_name : 定制巴士 磨盘山码头~鼎富大厦中心
         * order_price : 0.01
         * coupon_fee : 0
         * order_time : 2020/10/29 16:42:46
         * expire_time : 2020/10/29 17:02:46
         * order_details : [{"schedule_no":null,"seats_no":null,"tik_count":1,"ride_dates":["2020-10-14"],"order_id":"1000211349","ride_time":null,"ride_station":"磨盘山码头","reach_station":"鼎富大厦中心"}]
         */

        private String order_id;
        private int order_type;
        private int order_state;
        private String state_name;
        private String order_name;
        private double order_price;
        private String coupon_fee;
        private String order_time;
        private String expire_time;
        private List<OrderDetailsBean> order_details;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public int getOrder_state() {
            return order_state;
        }

        public void setOrder_state(int order_state) {
            this.order_state = order_state;
        }

        public String getState_name() {
            return state_name;
        }

        public void setState_name(String state_name) {
            this.state_name = state_name;
        }

        public String getOrder_name() {
            return order_name;
        }

        public void setOrder_name(String order_name) {
            this.order_name = order_name;
        }

        public double getOrder_price() {
            return order_price;
        }

        public void setOrder_price(double order_price) {
            this.order_price = order_price;
        }

        public String getCoupon_fee() {
            return coupon_fee;
        }

        public void setCoupon_fee(String coupon_fee) {
            this.coupon_fee = coupon_fee;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }

        public List<OrderDetailsBean> getOrder_details() {
            return order_details;
        }

        public void setOrder_details(List<OrderDetailsBean> order_details) {
            this.order_details = order_details;
        }

        public static class OrderDetailsBean {
            /**
             * schedule_no : null
             * seats_no : null
             * tik_count : 1
             * ride_dates : ["2020-10-14"]
             * order_id : 1000211349
             * ride_time : null
             * ride_station : 磨盘山码头
             * reach_station : 鼎富大厦中心
             */

            private Object schedule_no;
            private Object seats_no;
            private int tik_count;
            private String order_id;
            private Object ride_time;
            private String ride_station;
            private String reach_station;
            private List<String> ride_dates;

            public Object getSchedule_no() {
                return schedule_no;
            }

            public void setSchedule_no(Object schedule_no) {
                this.schedule_no = schedule_no;
            }

            public Object getSeats_no() {
                return seats_no;
            }

            public void setSeats_no(Object seats_no) {
                this.seats_no = seats_no;
            }

            public int getTik_count() {
                return tik_count;
            }

            public void setTik_count(int tik_count) {
                this.tik_count = tik_count;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public Object getRide_time() {
                return ride_time;
            }

            public void setRide_time(Object ride_time) {
                this.ride_time = ride_time;
            }

            public String getRide_station() {
                return ride_station;
            }

            public void setRide_station(String ride_station) {
                this.ride_station = ride_station;
            }

            public String getReach_station() {
                return reach_station;
            }

            public void setReach_station(String reach_station) {
                this.reach_station = reach_station;
            }

            public List<String> getRide_dates() {
                return ride_dates;
            }

            public void setRide_dates(List<String> ride_dates) {
                this.ride_dates = ride_dates;
            }
        }
    }
}
