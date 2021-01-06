package com.jtcxw.glcxw.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jtcxw.glcxw.base.utils.ToastUtil;
import com.jtcxw.glcxw.base.utils.UserUtil;
import com.jtcxw.glcxw.listeners.LocationCallback;

public class LocationUtil {

    public static long sLastUpdateTime = 0L;

    private LocationCallback mLocationCallback = null;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = amapLocation -> {
         if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                UserUtil.Companion.getUser().setLongitude(String.valueOf(amapLocation.getLongitude()));
                UserUtil.Companion.getUser().setCity("桂林");
//                UserUtil.Companion.getUser().setCity(amapLocation.getCity().substring(0,amapLocation.getCity().length() - 1));
                UserUtil.Companion.getUser().setLatitude(String.valueOf(amapLocation.getLatitude()));
//                amapLocation.getLatitude();//获取纬度
//                amapLocation.getLongitude();//获取经度
//                amapLocation.getCity();//城市信息
//                amapLocation.getDistrict();//城区信息
//                amapLocation.getStreet();//街道信息
//                amapLocation.getStreetNum();//街道门牌号信息
//                amapLocation.getCityCode();//城市编码
//                amapLocation.getAdCode();//地区编码
                if (mLocationCallback != null && sLastUpdateTime == 0) {
                    mLocationCallback.onLocationCallback(amapLocation);
                }
                sLastUpdateTime = System.currentTimeMillis();
            }else {
                if (sLastUpdateTime == 0L) {
                    ToastUtil.Companion.toastError("获取位置信息失败,请确保已打开位置权限和信号通畅");
                }
            }
        }
    };
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    public LocationUtil init(Context context,LocationCallback locationCallback) {
        mLocationCallback = locationCallback;
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setInterval(15000);


        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        mLocationClient.setLocationOption(mLocationOption);
        return this;

    }

    public void start() {
        if(null != mLocationClient){
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

    }

    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    public void destroy() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

}
