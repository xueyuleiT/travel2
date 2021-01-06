package com.jtcxw.glcxw.localbean;

import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean;

import java.util.ArrayList;
import java.util.List;

public class BusSiteOrLineHistoryBean {
    private int type;

    private String LineNo;
    private String LineName;

    private SiteOrLineBean.LineDateBean.LineDirectionBean LineDirection;


    private String StationId;
    private String StationName;
    private int Distance;
    private double Lon;
    private double Lat;
    private List<SiteOrLineBean.SiteDataBean.StationLineInfoBean> StationLineInfo;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLineNo() {
        return LineNo;
    }

    public void setLineNo(String lineNo) {
        LineNo = lineNo;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public SiteOrLineBean.LineDateBean.LineDirectionBean getLineDirection() {
        return LineDirection;
    }

    public void setLineDirection(SiteOrLineBean.LineDateBean.LineDirectionBean lineDirection) {
        LineDirection = lineDirection;
    }

    public String getStationId() {
        return StationId;
    }

    public void setStationId(String stationId) {
        StationId = stationId;
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

    public List<SiteOrLineBean.SiteDataBean.StationLineInfoBean> getStationLineInfo() {
        return StationLineInfo;
    }

    public void setStationLineInfo(List<SiteOrLineBean.SiteDataBean.StationLineInfoBean> stationLineInfo) {
        StationLineInfo = stationLineInfo;
    }
}
