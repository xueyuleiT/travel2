package com.jtcxw.glcxw.listeners

interface CityCallback {
    fun onCityCallback(provinceCode:Int,cityCode:Int,city: String,address:String)
}