package com.jtcxw.glcxw.listeners

import com.amap.api.location.AMapLocation

interface LocationCallback {
    fun onLocationCallback(aMapLocation:AMapLocation)
}