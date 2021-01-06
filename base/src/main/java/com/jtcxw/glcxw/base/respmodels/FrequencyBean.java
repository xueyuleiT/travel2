package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class FrequencyBean {
    private String route_imgs;
    private String route_name;
    private List<scheduleListBean> schedule_list;

    public List<scheduleListBean> getSchedule_list() {
        return schedule_list;
    }

    public void setSchedule_list(List<scheduleListBean> schedule_list) {
        this.schedule_list = schedule_list;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public static class scheduleListBean {

        /**
         * rule_id : 0
         * pre_order_flag : 0
         * pre_order_minute : 0
         * min_user_count : 0
         * rule_start : 2020-01-01 00:00:00
         * _rule_start : 2000-01-01
         * rule_end : 2020-01-01 00:00:00
         * _rule_end : 2000-01-01
         * ride_stations : [{"schedule_id":2809,"station_id":3221,"station_name":"闽运客运西站","order_no":0,"ride_time":"16:30","station_type":"S","stop_type":0,"area_positions":null,"address":null,"station_desc":"闽运客运西站"}]
         */

        private TicketBean.TikmodelListBean ticketBean;
        private int ticketLoadStatus = -2;
        private int rule_id;
        private int pre_order_flag;
        private int pre_order_minute;
        private int min_user_count;
        private int org_id;
        private int schedule_id;
        private int seats;
        private String rule_start;
        private String _rule_start;
        private String rule_end;
        private String _rule_end;
        private String schedule_no;
        private String ride_time;
        private String end_time;
        private String schedule_type;
        private String real_name_flag;
        private boolean selected = false;

        private String realTime;

        public String getRealTime() {
            return realTime;
        }

        public void setRealTime(String realTime) {
            this.realTime = realTime;
        }

        public TicketBean.TikmodelListBean getTicketBean() {
            return ticketBean;
        }

        public void setTicketBean(TicketBean.TikmodelListBean ticketBean) {
            this.ticketBean = ticketBean;
        }

        public int getTicketLoadStatus() {
            return ticketLoadStatus;
        }

        public void setTicketLoadStatus(int ticketLoadStatus) {
            this.ticketLoadStatus = ticketLoadStatus;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getOrg_id() {
            return org_id;
        }

        public void setOrg_id(int org_id) {
            this.org_id = org_id;
        }

        public int getSchedule_id() {
            return schedule_id;
        }

        public void setSchedule_id(int schedule_id) {
            this.schedule_id = schedule_id;
        }

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public String getSchedule_no() {
            return schedule_no;
        }

        public void setSchedule_no(String schedule_no) {
            this.schedule_no = schedule_no;
        }

        public String getRide_time() {
            return ride_time;
        }

        public void setRide_time(String ride_time) {
            this.ride_time = ride_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getSchedule_type() {
            return schedule_type;
        }

        public void setSchedule_type(String schedule_type) {
            this.schedule_type = schedule_type;
        }

        public String getReal_name_flag() {
            return real_name_flag;
        }

        public void setReal_name_flag(String real_name_flag) {
            this.real_name_flag = real_name_flag;
        }

        private List<RideStationsBean> ride_stations;
        private List<RideStationsBean> reach_stations;

        public List<RideStationsBean> getReach_stations() {
            return reach_stations;
        }

        public void setReach_stations(List<RideStationsBean> reach_stations) {
            this.reach_stations = reach_stations;
        }

        public int getRule_id() {
            return rule_id;
        }

        public void setRule_id(int rule_id) {
            this.rule_id = rule_id;
        }

        public int getPre_order_flag() {
            return pre_order_flag;
        }

        public void setPre_order_flag(int pre_order_flag) {
            this.pre_order_flag = pre_order_flag;
        }

        public int getPre_order_minute() {
            return pre_order_minute;
        }

        public void setPre_order_minute(int pre_order_minute) {
            this.pre_order_minute = pre_order_minute;
        }

        public int getMin_user_count() {
            return min_user_count;
        }

        public void setMin_user_count(int min_user_count) {
            this.min_user_count = min_user_count;
        }

        public String getRule_start() {
            return rule_start;
        }

        public void setRule_start(String rule_start) {
            this.rule_start = rule_start;
        }

        public String get_rule_start() {
            return _rule_start;
        }

        public void set_rule_start(String _rule_start) {
            this._rule_start = _rule_start;
        }

        public String getRule_end() {
            return rule_end;
        }

        public void setRule_end(String rule_end) {
            this.rule_end = rule_end;
        }

        public String get_rule_end() {
            return _rule_end;
        }

        public void set_rule_end(String _rule_end) {
            this._rule_end = _rule_end;
        }

        public List<RideStationsBean> getRide_stations() {
            return ride_stations;
        }

        public void setRide_stations(List<RideStationsBean> ride_stations) {
            this.ride_stations = ride_stations;
        }

        public static class RideStationsBean {
            /**
             * schedule_id : 2809
             * station_id : 3221
             * station_name : 闽运客运西站
             * order_no : 0
             * ride_time : 16:30
             * station_type : S
             * stop_type : 0
             * area_positions : null
             * address : null
             * station_desc : 闽运客运西站
             */

            private int type = 0; // 0 上车 1下车
            private boolean checked = false;
            private int schedule_id;
            private int station_id;
            private String station_name;
            private int order_no;
            private String ride_time;
            private String station_type;
            private int stop_type;
            private Object area_positions;
            private Object address;
            private String station_desc;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }

            public int getSchedule_id() {
                return schedule_id;
            }

            public void setSchedule_id(int schedule_id) {
                this.schedule_id = schedule_id;
            }

            public int getStation_id() {
                return station_id;
            }

            public void setStation_id(int station_id) {
                this.station_id = station_id;
            }

            public String getStation_name() {
                return station_name;
            }

            public void setStation_name(String station_name) {
                this.station_name = station_name;
            }

            public int getOrder_no() {
                return order_no;
            }

            public void setOrder_no(int order_no) {
                this.order_no = order_no;
            }

            public String getRide_time() {
                return ride_time;
            }

            public void setRide_time(String ride_time) {
                this.ride_time = ride_time;
            }

            public String getStation_type() {
                return station_type;
            }

            public void setStation_type(String station_type) {
                this.station_type = station_type;
            }

            public int getStop_type() {
                return stop_type;
            }

            public void setStop_type(int stop_type) {
                this.stop_type = stop_type;
            }

            public Object getArea_positions() {
                return area_positions;
            }

            public void setArea_positions(Object area_positions) {
                this.area_positions = area_positions;
            }

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
                this.address = address;
            }

            public String getStation_desc() {
                return station_desc;
            }

            public void setStation_desc(String station_desc) {
                this.station_desc = station_desc;
            }
        }
    }
}
