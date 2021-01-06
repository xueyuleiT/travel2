package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class BusArriveListBean {

    private List<StationLineListBean> StationLineList;

    public List<StationLineListBean> getStationLineList() {
        return StationLineList;
    }

    public void setStationLineList(List<StationLineListBean> stationLineList) {
        StationLineList = stationLineList;
    }

    public static class StationLineListBean {
        private String StationId;
        private String LineId;
        private String LastUpdTime;
        private List<ForcastArriveVehsBean> ForcastArriveVehs;

        public String getStationId() {
            return StationId;
        }

        public void setStationId(String stationId) {
            StationId = stationId;
        }

        public String getLineId() {
            return LineId;
        }

        public void setLineId(String lineId) {
            LineId = lineId;
        }

        public String getLastUpdTime() {
            return LastUpdTime;
        }

        public void setLastUpdTime(String lastUpdTime) {
            LastUpdTime = lastUpdTime;
        }

        public List<ForcastArriveVehsBean> getForcastArriveVehs() {
            return ForcastArriveVehs;
        }

        public void setForcastArriveVehs(List<ForcastArriveVehsBean> forcastArriveVehs) {
            ForcastArriveVehs = forcastArriveVehs;
        }

        public static class ForcastArriveVehsBean{

            /**
             * ForecastTime : 2
             * NextLevel : 8
             * GpsTime : 2020-11-12 20:18:18
             * DeviceId : xxxx
             * Lon : 110.257127
             * Lat : 25.260566
             */

            private String ForecastTime;
            private String NextLevel;
            private String GpsTime;
            private String DeviceId;
            private double Lon;
            private double Lat;
            private int Angle;

            public int getAngle() {
                return Angle;
            }

            public void setAngle(int angle) {
                Angle = angle;
            }

            public String getForecastTime() {
                return ForecastTime;
            }

            public void setForecastTime(String ForecastTime) {
                this.ForecastTime = ForecastTime;
            }

            public String getNextLevel() {
                return NextLevel;
            }

            public void setNextLevel(String NextLevel) {
                this.NextLevel = NextLevel;
            }

            public String getGpsTime() {
                return GpsTime;
            }

            public void setGpsTime(String GpsTime) {
                this.GpsTime = GpsTime;
            }

            public String getDeviceId() {
                return DeviceId;
            }

            public void setDeviceId(String DeviceId) {
                this.DeviceId = DeviceId;
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
        }

    }

}
