package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class SiteOrLineBean {
    private List<SiteDataBean> SiteData;

    public List<SiteDataBean> getSiteData() {
        return SiteData;
    }

    private List<LineDateBean> LineData;

    public List<LineDateBean> getLineData() {
        return LineData;
    }

    public void setLineData(List<LineDateBean> lineData) {
        LineData = lineData;
    }

    public void setSiteData(List<SiteDataBean> SiteData) {
        this.SiteData = SiteData;
    }

    public static class LineDateBean {

        public String getLineId() {
            return LineId;
        }

        public void setLineId(String lineId) {
            LineId = lineId;
        }

        public int getFollowFlag() {
            return FollowFlag;
        }

        public void setFollowFlag(int followFlag) {
            FollowFlag = followFlag;
        }

        public String getDirectionLineName() {
            return DirectionLineName;
        }

        public void setDirectionLineName(String directionLineName) {
            DirectionLineName = directionLineName;
        }

        /**
         * LineNo : 100
         * LineName : 100路
         * LineDirection : [{"LineId":"4cd51337-2236-11eb-916a-00155d37fc0f","DirectionLineName":"和平村","StartTime":"06:00","LastTime":"22:30"},{"LineId":"4cd52da6-2236-11eb-916a-00155d37fc0f","DirectionLineName":"桂林北站","StartTime":"06:00","LastTime":"22:30"}]
         */

        private String LineId;
        private int FollowFlag;
        private String DirectionLineName;
        private String LineNo;
        private String LineName;
        private List<LineDirectionBean> LineDirection;

        public String getLineNo() {
            return LineNo;
        }

        public void setLineNo(String LineNo) {
            this.LineNo = LineNo;
        }

        public String getLineName() {
            return LineName;
        }

        public void setLineName(String LineName) {
            this.LineName = LineName;
        }

        public List<LineDirectionBean> getLineDirection() {
            return LineDirection;
        }

        public void setLineDirection(List<LineDirectionBean> LineDirection) {
            this.LineDirection = LineDirection;
        }

        public static class LineDirectionBean {
            /**
             * LineId : 4cd51337-2236-11eb-916a-00155d37fc0f
             * DirectionLineName : 和平村
             * StartTime : 06:00
             * LastTime : 22:30
             */

            private String LineId;
            private String DirectionLineName;
            private String StartTime;
            private String LastTime;

            public String getLineId() {
                return LineId;
            }

            public void setLineId(String LineId) {
                this.LineId = LineId;
            }

            public String getDirectionLineName() {
                return DirectionLineName;
            }

            public void setDirectionLineName(String DirectionLineName) {
                this.DirectionLineName = DirectionLineName;
            }

            public String getStartTime() {
                return StartTime;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public String getLastTime() {
                return LastTime;
            }

            public void setLastTime(String LastTime) {
                this.LastTime = LastTime;
            }
        }
    }

    public static class SiteDataBean {

        /**
         * StationId : bc967724-222d-11eb-916a-00155d37fc0f
         * StationName : 166路口
         * Distance : 1230642
         * Lon : 110.655665
         * Lat : 25.660792
         * StationLineInfo : [{"LineNo":"712","LineName":"712路","LineDirection":[{"LineId":"4cd42a65-2236-11eb-916a-00155d37fc0f","DirectionLineName":"蒋家山","StartTime":"07:00","LastTime":"18:30"}]}]
         */

        private String StationName;
        private int Distance;
        private List<StationLineInfoBean> StationLineInfo;
        private List<StopListBean> StopList;


        public List<StopListBean> getStopList() {
            return StopList;
        }

        public void setStopList(List<StopListBean> stopList) {
            StopList = stopList;
        }

        public String getStationName() {
            return StationName;
        }

        public void setStationName(String StationName) {
            this.StationName = StationName;
        }

        public int getDistance() {
            return Distance;
        }

        public void setDistance(int Distance) {
            this.Distance = Distance;
        }

        public List<StationLineInfoBean> getStationLineInfo() {
            return StationLineInfo;
        }

        public void setStationLineInfo(List<StationLineInfoBean> StationLineInfo) {
            this.StationLineInfo = StationLineInfo;
        }

        public static class StopListBean {

            /**
             * StopId : xxx
             * StopName : xxx
             * Lon : xxx
             * Lat : xxx
             * Distance : 100
             */

            private String StopId;
            private String StopName;
            private double Lon;
            private double Lat;
            private int Distance;
            private int IsCollection = 0;
            private String CollectionId;

            public int getIsCollection() {
                return IsCollection;
            }

            public String getCollectionId() {
                return CollectionId;
            }

            public void setCollectionId(String collectionId) {
                CollectionId = collectionId;
            }

            public void setIsCollection(int isCollection) {
                IsCollection = isCollection;
            }

            public String getStopId() {
                return StopId;
            }

            public void setStopId(String StopId) {
                this.StopId = StopId;
            }

            public String getStopName() {
                return StopName;
            }

            public void setStopName(String StopName) {
                this.StopName = StopName;
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

            public int getDistance() {
                return Distance;
            }

            public void setDistance(int Distance) {
                this.Distance = Distance;
            }
        }

        public static class StationLineInfoBean {

            private String StopId;

            private int UpDown;
            private int Level;
            private String LineId;
            private String LineName;
            private String DirectionLineName;

            private String StartTime;
            private String LastTime;

            public String getStopId() {
                return StopId;
            }

            public void setStopId(String stopId) {
                StopId = stopId;
            }

            public int getUpDown() {
                return UpDown;
            }

            public void setUpDown(int upDown) {
                UpDown = upDown;
            }

            public int getLevel() {
                return Level;
            }

            public void setLevel(int level) {
                Level = level;
            }

            public String getLineId() {
                return LineId;
            }

            public void setLineId(String lineId) {
                LineId = lineId;
            }

            public String getLineName() {
                return LineName;
            }

            public void setLineName(String lineName) {
                LineName = lineName;
            }

            public String getDirectionLineName() {
                return DirectionLineName;
            }

            public void setDirectionLineName(String directionLineName) {
                DirectionLineName = directionLineName;
            }

            public String getStartTime() {
                return StartTime;
            }

            public void setStartTime(String startTime) {
                StartTime = startTime;
            }

            public String getLastTime() {
                return LastTime;
            }

            public void setLastTime(String lastTime) {
                LastTime = lastTime;
            }
        }
    }
}
