package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class LineBean {

    private List<RouteListBean> route_list;

    public List<RouteListBean> getRoute_list() {
        return route_list;
    }

    public void setRoute_list(List<RouteListBean> route_list) {
        this.route_list = route_list;
    }

    public static class RouteListBean {
        /**
         * route_name : 监狱线路
         * sys_org_id : 0
         * tp_id : 0
         * coupon_type : 0
         * route_type : 1
         * ride_station_list : [{"station_id":3221,"station_name":"闽运客运西站"},{"station_id":3223,"station_name":"葛岐村"},{"station_id":3225,"station_name":"临时停靠点（原动力奥迪门口）"},{"station_id":3241,"station_name":"南屿路口"},{"station_id":3243,"station_name":"南井村口"}]
         * reach_station_list : [{"station_id":3221,"station_name":"闽运客运西站"},{"station_id":3223,"station_name":"葛岐村"},{"station_id":3225,"station_name":"临时停靠点（原动力奥迪门口）"},{"station_id":3241,"station_name":"南屿路口"},{"station_id":3243,"station_name":"南井村口"}]
         * route_id : 2801
         * route_no : DZBS42
         * vis_names : 葛岐村,临时停靠点（原动力奥迪门口）,南屿路口,南井村口,女子监狱
         * vis_list : ["葛岐村","临时停靠点（原动力奥迪门口）","南屿路口","南井村口","女子监狱"]
         * distance : 16
         * start_station_name : 闽运客运西站
         * end_station_name : 闽江监狱
         * is_active : Y
         * route_remark : 车号为闽AYD376，闽AYD373
         * back_route_id : 2802
         */

        private String route_name;
        private int sys_org_id;
        private int tp_id;
        private int coupon_type;
        private int route_type;
        private int route_id;
        private String route_no;
        private String vis_names;
        private String distance;
        private String start_station_name;
        private String end_station_name;
        private String is_active;
        private String route_remark;
        private Boolean openTip = false;
        private Boolean openVisit = false;
        private int back_route_id;
        private List<RideStationListBean> ride_station_list;
        private List<ReachStationListBean> reach_station_list;
        private List<String> vis_list;

        public Boolean getOpenTip() {
            return openTip;
        }

        public void setOpenTip(Boolean openTip) {
            this.openTip = openTip;
        }

        public Boolean getOpenVisit() {
            return openVisit;
        }

        public void setOpenVisit(Boolean openVisit) {
            this.openVisit = openVisit;
        }

        public String getRoute_name() {
            return route_name;
        }

        public void setRoute_name(String route_name) {
            this.route_name = route_name;
        }

        public int getSys_org_id() {
            return sys_org_id;
        }

        public void setSys_org_id(int sys_org_id) {
            this.sys_org_id = sys_org_id;
        }

        public int getTp_id() {
            return tp_id;
        }

        public void setTp_id(int tp_id) {
            this.tp_id = tp_id;
        }

        public int getCoupon_type() {
            return coupon_type;
        }

        public void setCoupon_type(int coupon_type) {
            this.coupon_type = coupon_type;
        }

        public int getRoute_type() {
            return route_type;
        }

        public void setRoute_type(int route_type) {
            this.route_type = route_type;
        }

        public int getRoute_id() {
            return route_id;
        }

        public void setRoute_id(int route_id) {
            this.route_id = route_id;
        }

        public String getRoute_no() {
            return route_no;
        }

        public void setRoute_no(String route_no) {
            this.route_no = route_no;
        }

        public String getVis_names() {
            return vis_names;
        }

        public void setVis_names(String vis_names) {
            this.vis_names = vis_names;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
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

        public String getIs_active() {
            return is_active;
        }

        public void setIs_active(String is_active) {
            this.is_active = is_active;
        }

        public String getRoute_remark() {
            return route_remark;
        }

        public void setRoute_remark(String route_remark) {
            this.route_remark = route_remark;
        }

        public int getBack_route_id() {
            return back_route_id;
        }

        public void setBack_route_id(int back_route_id) {
            this.back_route_id = back_route_id;
        }

        public List<RideStationListBean> getRide_station_list() {
            return ride_station_list;
        }

        public void setRide_station_list(List<RideStationListBean> ride_station_list) {
            this.ride_station_list = ride_station_list;
        }

        public List<ReachStationListBean> getReach_station_list() {
            return reach_station_list;
        }

        public void setReach_station_list(List<ReachStationListBean> reach_station_list) {
            this.reach_station_list = reach_station_list;
        }

        public List<String> getVis_list() {
            return vis_list;
        }

        public void setVis_list(List<String> vis_list) {
            this.vis_list = vis_list;
        }

        public static class RideStationListBean {
            /**
             * station_id : 3221
             * station_name : 闽运客运西站
             */

            private int station_id;
            private String station_name;

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
        }

        public static class ReachStationListBean {
            /**
             * station_id : 3221
             * station_name : 闽运客运西站
             */

            private int station_id;
            private String station_name;

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
        }
    }
}
