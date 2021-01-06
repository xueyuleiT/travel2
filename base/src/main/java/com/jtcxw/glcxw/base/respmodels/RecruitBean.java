package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class RecruitBean {
    /**
     * recruit_list : [{"recruit_id":1553,"customer_id":"804918","start_station_name":"娴嬭瘯","end_station_name":"娴嬭瘯","min_user_count":10,"start_time":"16:00","joined_count":0,"join_state":"N","join_time":"2020-11-01 17:01:46","quite_time":"2020-11-01 17:03:30"},{"recruit_id":1534,"customer_id":"1021","start_station_name":"磨盘山码头","end_station_name":"园博园","min_user_count":10,"start_time":"08:00","joined_count":0,"join_state":"","join_time":"2000-01-01 00:00:00","quite_time":"2000-01-01 00:00:00"},{"recruit_id":1531,"customer_id":"1021","start_station_name":"磨盘山码头","end_station_name":"园博园","min_user_count":10,"start_time":"08:00","joined_count":2,"join_state":"Y","join_time":"2020-10-30 17:32:50","quite_time":"2020-10-30 17:32:50"}]
     * planning_info : {"planning_id":341,"_start_time":"2020-10-01 00:00","_end_time":"2020-11-30 00:00","_expire_time":"2020-11-30 00:00"}
     */

    private PlanningInfoBean planning_info;
    private List<RecruitListBean> recruit_list;

    public PlanningInfoBean getPlanning_info() {
        return planning_info;
    }

    public void setPlanning_info(PlanningInfoBean planning_info) {
        this.planning_info = planning_info;
    }

    public List<RecruitListBean> getRecruit_list() {
        return recruit_list;
    }

    public void setRecruit_list(List<RecruitListBean> recruit_list) {
        this.recruit_list = recruit_list;
    }

    public static class PlanningInfoBean {
        /**
         * planning_id : 341
         * _start_time : 2020-10-01 00:00
         * _end_time : 2020-11-30 00:00
         * _expire_time : 2020-11-30 00:00
         */

        private long planning_id;
        private String _start_time;
        private String _end_time;
        private String _expire_time;

        public long getPlanning_id() {
            return planning_id;
        }

        public void setPlanning_id(long planning_id) {
            this.planning_id = planning_id;
        }

        public String get_start_time() {
            return _start_time;
        }

        public void set_start_time(String _start_time) {
            this._start_time = _start_time;
        }

        public String get_end_time() {
            return _end_time;
        }

        public void set_end_time(String _end_time) {
            this._end_time = _end_time;
        }

        public String get_expire_time() {
            return _expire_time;
        }

        public void set_expire_time(String _expire_time) {
            this._expire_time = _expire_time;
        }
    }

    public static class RecruitListBean {
        /**
         * recruit_id : 1553
         * customer_id : 804918
         * start_station_name : 娴嬭瘯
         * end_station_name : 娴嬭瘯
         * min_user_count : 10
         * start_time : 16:00
         * joined_count : 0
         * join_state : N
         * join_time : 2020-11-01 17:01:46
         * quite_time : 2020-11-01 17:03:30
         */

        private long recruit_id;
        private String customer_id;
        private String start_station_name;
        private String end_station_name;
        private int min_user_count;
        private String start_time;
        private int joined_count;
        private String join_state;
        private String join_time;
        private String quite_time;

        public long getRecruit_id() {
            return recruit_id;
        }

        public void setRecruit_id(long recruit_id) {
            this.recruit_id = recruit_id;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getStart_station_name() {
            return start_station_name;
        }

        public void setStart_station_name(String start_station_name) {
            this.start_station_name = start_station_name;
        }

        public String getEnd_station_name() {
            return end_station_name;
        }

        public void setEnd_station_name(String end_station_name) {
            this.end_station_name = end_station_name;
        }

        public int getMin_user_count() {
            return min_user_count;
        }

        public void setMin_user_count(int min_user_count) {
            this.min_user_count = min_user_count;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public int getJoined_count() {
            return joined_count;
        }

        public void setJoined_count(int joined_count) {
            this.joined_count = joined_count;
        }

        public String getJoin_state() {
            return join_state;
        }

        public void setJoin_state(String join_state) {
            this.join_state = join_state;
        }

        public String getJoin_time() {
            return join_time;
        }

        public void setJoin_time(String join_time) {
            this.join_time = join_time;
        }

        public String getQuite_time() {
            return quite_time;
        }

        public void setQuite_time(String quite_time) {
            this.quite_time = quite_time;
        }
    }
}
