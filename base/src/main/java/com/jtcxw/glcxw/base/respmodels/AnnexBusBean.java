package com.jtcxw.glcxw.base.respmodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AnnexBusBean {

    private List<StationListBean> StationList;

    public List<StationListBean> getStationList() {
        return StationList;
    }

    public void setStationList(List<StationListBean> StationList) {
        this.StationList = StationList;
    }

    public static class StopListBean implements Parcelable {

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
        private int IsCollection;
        private String CollectionId;

        public String getCollectionId() {
            return CollectionId;
        }

        public void setCollectionId(String collectionId) {
            CollectionId = collectionId;
        }

        public StopListBean(){

        }

        protected StopListBean(Parcel in) {
            StopId = in.readString();
            StopName = in.readString();
            Lon = in.readDouble();
            Lat = in.readDouble();
            Distance = in.readInt();
            IsCollection = in.readInt();
            CollectionId = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(StopId);
            dest.writeString(StopName);
            dest.writeDouble(Lon);
            dest.writeDouble(Lat);
            dest.writeInt(Distance);
            dest.writeInt(IsCollection);
            dest.writeString(CollectionId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<StopListBean> CREATOR = new Creator<StopListBean>() {
            @Override
            public StopListBean createFromParcel(Parcel in) {
                return new StopListBean(in);
            }

            @Override
            public StopListBean[] newArray(int size) {
                return new StopListBean[size];
            }
        };

        public String getStopId() {
            return StopId;
        }

        public void setStopId(String stopId) {
            StopId = stopId;
        }

        public String getStopName() {
            return StopName;
        }

        public void setStopName(String stopName) {
            StopName = stopName;
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

        public int getDistance() {
            return Distance;
        }

        public void setDistance(int distance) {
            Distance = distance;
        }

        public int getIsCollection() {
            return IsCollection;
        }

        public void setIsCollection(int isCollection) {
            IsCollection = isCollection;
        }
    }

    public static class StationListBean implements Parcelable {
        /**
         * StationId : bc96e237-222d-11eb-916a-00155d37fc0f
         * StationName : 三零九
         * Distance : 1217906
         * Lon : 110.690892
         * Lat : 25.649647
         * StationLineInfo : [{"LineNo":"713","LineName":"713路","LineDirection":[{"LineId":"4cd42dda-2236-11eb-916a-00155d37fc0f","DirectionLineName":"三零九","StartTime":"06:30","LastTime":"23:30"}]}]
         */
        private String phoneTip = "";
        private int type = 0;
        private boolean isFold = true;
        private String StationName;
        private int Distance;

        private List<StopListBean> StopList;
        private List<StationLineInfoBean> StationLineInfo;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isFold() {
            return isFold;
        }

        public void setFold(boolean fold) {
            isFold = fold;
        }

        public String getStationName() {
            return StationName;
        }

        public void setStationName(String stationName) {
            StationName = stationName;
        }

        public int getDistance() {
            return Distance;
        }

        public void setDistance(int distance) {
            Distance = distance;
        }

        public List<StopListBean> getStopList() {
            return StopList;
        }

        public void setStopList(List<StopListBean> stopList) {
            StopList = stopList;
        }

        public List<StationLineInfoBean> getStationLineInfo() {
            return StationLineInfo;
        }

        public void setStationLineInfo(List<StationLineInfoBean> stationLineInfo) {
            StationLineInfo = stationLineInfo;
        }

        protected StationListBean(Parcel in) {
            phoneTip = in.readString();
            type = in.readInt();
            isFold = in.readByte() != 0;
            StationName = in.readString();
            Distance = in.readInt();
            StopList = in.createTypedArrayList(StopListBean.CREATOR);
            StationLineInfo = in.createTypedArrayList(StationLineInfoBean.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(phoneTip);
            dest.writeInt(type);
            dest.writeByte((byte) (isFold ? 1 : 0));
            dest.writeString(StationName);
            dest.writeInt(Distance);
            dest.writeTypedList(StopList);
            dest.writeTypedList(StationLineInfo);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<StationListBean> CREATOR = new Creator<StationListBean>() {
            @Override
            public StationListBean createFromParcel(Parcel in) {
                return new StationListBean(in);
            }

            @Override
            public StationListBean[] newArray(int size) {
                return new StationListBean[size];
            }
        };

        public String getPhoneTip() {
            return phoneTip;
        }

        public void setPhoneTip(String phoneTip) {
            this.phoneTip = phoneTip;
        }

        public StationListBean() {

        }



        public static class StationLineInfoBean implements Parcelable {
            public StationLineInfoBean() {

            }
            protected StationLineInfoBean(Parcel in) {
                LineNo = in.readString();
                UpDown = in.readInt();
                Level = in.readInt();
                LineId = in.readString();
                LineName = in.readString();
                DirectionLineName = in.readString();
                StartTime = in.readString();
                LastTime = in.readString();
                forecastTime = in.readString();
                lastUpdTime = in.readString();
                nextLevel = in.readString();
                IsCollection = in.readInt();
                StopId = in.readString();
                CollectionId = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(LineNo);
                dest.writeInt(UpDown);
                dest.writeInt(Level);
                dest.writeString(LineId);
                dest.writeString(LineName);
                dest.writeString(DirectionLineName);
                dest.writeString(StartTime);
                dest.writeString(LastTime);
                dest.writeString(forecastTime);
                dest.writeString(lastUpdTime);
                dest.writeString(nextLevel);
                dest.writeInt(IsCollection);
                dest.writeString(StopId);
                dest.writeString(CollectionId);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<StationLineInfoBean> CREATOR = new Creator<StationLineInfoBean>() {
                @Override
                public StationLineInfoBean createFromParcel(Parcel in) {
                    return new StationLineInfoBean(in);
                }

                @Override
                public StationLineInfoBean[] newArray(int size) {
                    return new StationLineInfoBean[size];
                }
            };

            public String getLineNo() {
                return LineNo;
            }

            public void setLineNo(String lineNo) {
                LineNo = lineNo;
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

            public String getForecastTime() {
                return forecastTime;
            }

            public void setForecastTime(String forecastTime) {
                this.forecastTime = forecastTime;
            }

            public String getLastUpdTime() {
                return lastUpdTime;
            }

            public void setLastUpdTime(String lastUpdTime) {
                this.lastUpdTime = lastUpdTime;
            }

            public String getNextLevel() {
                return nextLevel;
            }

            public void setNextLevel(String nextLevel) {
                this.nextLevel = nextLevel;
            }

            public int getIsCollection() {
                return IsCollection;
            }

            public void setIsCollection(int isCollection) {
                IsCollection = isCollection;
            }

            public String getStopId() {
                return StopId;
            }

            public void setStopId(String stopId) {
                StopId = stopId;
            }

            /**
             * LineNo : 713
             * LineName : 713路
             * LineDirection : [{"LineId":"4cd42dda-2236-11eb-916a-00155d37fc0f","DirectionLineName":"三零九","StartTime":"06:30","LastTime":"23:30"}]
             */

            private String LineNo;//linename
            private int UpDown;
            private int Level;
            private String LineId;
            private String LineName;
            private String DirectionLineName;

            private String StartTime;
            private String LastTime;

            private String forecastTime;
            private String lastUpdTime;
            private String nextLevel;
            private int IsCollection = 0;
            private String StopId;
            private String CollectionId;

            public String getCollectionId() {
                return CollectionId;
            }

            public void setCollectionId(String collectionId) {
                CollectionId = collectionId;
            }
        }
    }
}
