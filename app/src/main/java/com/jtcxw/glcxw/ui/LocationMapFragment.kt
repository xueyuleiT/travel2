package com.jtcxw.glcxw.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentLocationMapBinding
import com.jtcxw.glcxw.utils.ThreeMapUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.SupportFragment

class LocationMapFragment:BaseFragment<FragmentLocationMapBinding,CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
       return R.layout.fragment_location_map
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val locationMapFragment = LocationMapFragment()
            locationMapFragment.arguments = bundle
            fragment.start(locationMapFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(arguments!!.getString(BundleKeys.KEY_TITLE,""))

        mBinding.vMap.onCreate(savedInstanceState)
        mBinding.vMap.map.mapType = AMap.MAP_TYPE_NORMAL


        val myLocationStyle =  MyLocationStyle()//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
        myLocationStyle.showMyLocation(true)
        mBinding.vMap.map.myLocationStyle = myLocationStyle//设置定位蓝点的Style
        mBinding.vMap.map.isMyLocationEnabled = true
        mBinding.vMap.map.addOnMyLocationChangeListener {
            UserUtil.getUser().latitude = it.latitude.toString()
            UserUtil.getUser().longitude = it.longitude.toString()
        }


        val lat = arguments!!.getDouble(BundleKeys.KEY_LAT,0.0)
        val lon = arguments!!.getDouble(BundleKeys.KEY_LON,0.00)


        var icon: BitmapDescriptor? = if (arguments!!.getString(BundleKeys.KEY_LOCATION_TYPE,"") == "hotel") {
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory
                    .decodeResource(resources, R.mipmap.icon_position_location)
            )
        } else {
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory
                    .decodeResource(resources, R.mipmap.icon_position_location)
            )
        }
        val latLng = LatLng(lat,lon)



        mBinding.vMap.map.animateCamera(CameraUpdateFactory.changeLatLng(latLng))
        mBinding.vMap.map.animateCamera(CameraUpdateFactory.zoomTo(14f))

        mBinding.vMap.map.addMarker(MarkerOptions().position(latLng).icon(icon).setFlat(true))

        mBinding.tvNavigation.setOnClickListener {
            ThreeMapUtil.showDialog(context,lat, lon,arguments!!.getString(BundleKeys.KEY_TITLE,""))
        }
    }

    override fun doAfterAnim() {
    }


    override fun onResume() {
        super.onResume()
        mBinding.vMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.vMap.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding.vMap.onDestroy()
    }
}