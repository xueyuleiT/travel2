package com.jtcxw.glcxw.ui

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.core.graphics.scale
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.amap.api.services.busline.BusStationResult
import com.amap.api.services.busline.BusStationSearch
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.BusMapAdapter
import com.jtcxw.glcxw.base.api.ApiCallback
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.localmodels.BusLineItem
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.respmodels.BusArriveListBean
import com.jtcxw.glcxw.base.respmodels.LineDetailBean
import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean
import com.jtcxw.glcxw.base.utils.*
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentBusMapBinding
import com.jtcxw.glcxw.events.CollectEvent
import com.jtcxw.glcxw.listeners.CollectCancelCallback
import com.jtcxw.glcxw.listeners.InnerClickListener
import com.jtcxw.glcxw.listeners.LocationCallback
import com.jtcxw.glcxw.presenters.impl.BusMapPresenter
import com.jtcxw.glcxw.presenters.impl.BusQueryPresenter
import com.jtcxw.glcxw.presenters.impl.CollectionPresenter
import com.jtcxw.glcxw.ui.login.LoginFragment
import com.jtcxw.glcxw.utils.LocationUtil
import com.jtcxw.glcxw.viewmodel.BusModel
import com.jtcxw.glcxw.views.BusMapView
import com.jtcxw.glcxw.views.BusQueryView
import com.jtcxw.glcxw.views.CollectionView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import models.BaseBean
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer
import kotlin.math.PI


class BusMapFragment: BaseFragment<FragmentBusMapBinding, BusModel>(), BusMapView ,CollectionView ,BusQueryView{
    override fun onBusInquiryAnnexBusSucc(siteOrLineBean: SiteOrLineBean) {
    }

    override fun onListHistorySucc(siteOrLineBean: SiteOrLineBean) {
    }

    override fun onClearQueryHistorySucc(jsonObject: JsonObject) {
    }

    override fun onAddCollectionSucc(jsonObject: JsonObject) {
        if (mLineDetailBean != null) {
            if (jsonObject.get("Status").asBoolean) {
                mLineDetailBean!!.isCollection = 1
                ToastUtil.toastSuccess("已收藏")
                mLineDetailBean!!.collectionId = jsonObject.get("CollectionId").asString
            } else {
                mLineDetailBean!!.isCollection = 0
                ToastUtil.toastSuccess("收藏失败")
            }

            if (mLineDetailBean!!.isCollection == 0) {
                mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
            } else {
                mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
            }

            val event = CollectEvent()
            event.id = mLineDetailBean!!.lineId
            event.collectionId = mLineDetailBean!!.collectionId
            event.isCollection = mLineDetailBean!!.isCollection
            RxBus.getDefault().post(event)

        } else {
            if (jsonObject.get("Status").asBoolean) {
                mSelectedData!![0].stopList[mAdapter!!.mSelected].isCollection = 1
                mSelectedData!![0].stopList[mAdapter!!.mSelected].collectionId = jsonObject.get("CollectionId").asString

                ToastUtil.toastSuccess("已收藏")
            } else {
                mSelectedData!![0].stopList[mAdapter!!.mSelected].isCollection = 0
                ToastUtil.toastSuccess("收藏失败")
            }

            mBinding.recyclerView.innerAdapter.notifyAllItems()
        }
    }

    override fun onCancelCollectionSucc(jsonObject: JsonObject) {
        if (mLineDetailBean != null) {
            if (jsonObject.get("Status").asBoolean) {
                mLineDetailBean!!.isCollection = 0
                ToastUtil.toastSuccess("已取消收藏")
            } else {
                mLineDetailBean!!.isCollection = 1
                ToastUtil.toastSuccess("取消失败")
            }

            if (mLineDetailBean!!.isCollection == 0) {
                mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
            } else {
                mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
            }

            val event = CollectEvent()
            event.id = mLineDetailBean!!.lineId
            event.collectionId = mLineDetailBean!!.collectionId
            event.isCollection = mLineDetailBean!!.isCollection
            RxBus.getDefault().post(event)

        }  else {
            if (jsonObject.get("Status").asBoolean) {
                mSelectedData!![0].stopList[mAdapter!!.mSelected].isCollection = 0
                ToastUtil.toastSuccess("已取消收藏")
            } else {
                mSelectedData!![0].stopList[mAdapter!!.mSelected].isCollection = 1
                ToastUtil.toastSuccess("取消失败")
            }
            mBinding.recyclerView.innerAdapter.notifyAllItems()
        }
    }

    override fun onForcastArriveQuerySucc(busArriveListBean: BusArriveListBean) {
        if (busArriveListBean.stationLineList.isNotEmpty()) {

            mBinding.busView.arriveVehs.clear()

                busArriveListBean.stationLineList.forEach { j ->
                    if (mStationId == j.stationId && mLineId == j.lineId) {
                        if (j.forcastArriveVehs != null && j.forcastArriveVehs.isNotEmpty()){
                            j.forcastArriveVehs.forEach {
                                it.nextLevel = (it.nextLevel.toInt() - 1).toString()
                                mBinding.busView.arriveVehs.add(it)
                            }
                        }
                    }
                }
            mBinding.busView.invalidate()
            mBinding.vMap.map.mapScreenMarkers.forEach {
                if (it.zIndex == 500f)  {
                    it.remove()
                }
            }
            val list = ArrayList<MarkerOptions>()
            mBinding.busView.arriveVehs.forEach { vehs ->
                var icon =  BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources,R.mipmap.icon_bus_vertical).scale(DimensionUtil.dpToPx(22).toInt(),DimensionUtil.dpToPx(22).toInt()))
                val latLng = LatLng(vehs.lat,vehs.lon)
                list.add(MarkerOptions().position(latLng).icon(icon).zIndex(500f).setFlat(true))
            }
            mBinding.vMap.map.addMarkers(list,false)
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment,bundle: Bundle?) {
            val busMapFragment = BusMapFragment()
            busMapFragment.arguments = bundle
            fragment.start(busMapFragment,ISupportFragment.SINGLETOP)
        }
    }

    override fun getVariableId(): Int {
        return BR.busModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bus_map
    }
    var mPresenter:BusMapPresenter?= null
    var mCollectionPresenter: CollectionPresenter?= null

    var timer: Timer?= null
    private fun startTimer() {
        timer = fixedRateTimer("", false, 0, 15000) {
            if (mData.isEmpty()) {
                return@fixedRateTimer
            }
            val json = JsonObject()
            val jsonArray = JsonArray()
            val item = JsonObject()
            item.addProperty("StationId",mStationId)
            item.addProperty("LineId", mLineId)
            jsonArray.add(item)
            json.add("StationLineList",jsonArray)
            mPresenter!!.forcastArriveQuery(json)
        }
    }

    private fun stopTimer() {
        timer?.cancel()
    }


    private var mAdapter:BusMapAdapter?= null
    private var mDownY = 0F
    private var mLineDetailBean:LineDetailBean? = null
    private var mLineId = ""
    private var mPolyline:Polyline? = null
    private var mSelectedData = ArrayList<AnnexBusBean.StationListBean>()
    private var mData = ArrayList<AnnexBusBean.StationListBean>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("公交查询")


        mBinding.etSearch.text = (parentFragment as QueryMainFragment).mQueryStr

        val myLocationStyle =  MyLocationStyle()//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
        myLocationStyle.showMyLocation(true)
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(
//            BitmapFactory
//                .decodeResource(resources,R.mipmap.bus_route_back_texture)))
        mBinding.vMap.map.myLocationStyle = myLocationStyle//设置定位蓝点的Style
        mBinding.vMap.map.uiSettings.isTiltGesturesEnabled = false
        mBinding.vMap.map.isMyLocationEnabled = true
        mBinding.vMap.map.addOnMyLocationChangeListener {
            UserUtil.getUser().latitude = it.latitude.toString()
            UserUtil.getUser().longitude = it.longitude.toString()
        }

        mPresenter = BusMapPresenter(this)
        mCollectionPresenter = CollectionPresenter(this)

        mBinding.vMap.onCreate(savedInstanceState)
        mBinding.vMap.map.mapType = AMap.MAP_TYPE_NORMAL

        val latLng = LatLng(UserUtil.getUser().latitude.toDouble(), UserUtil.getUser().longitude.toDouble() )
        //设置中心点和缩放比例
        mBinding.vMap.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))
        mBinding.vMap.map.animateCamera(CameraUpdateFactory.zoomTo(13f))


        mBinding.tvLineChange.setOnClickListener(this)
        mBinding.vHeart.setOnClickListener(this)

        addSearchListener()
        init()

        mBinding.bottomSheet.viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener {
            var minH = DimensionUtil.dpToPx(160).toInt()
            var mTop = 0
            @SuppressLint("ClickableViewAccessibility")
            override fun onGlobalLayout() {
                mBinding.bottomSheet.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val params =
                    mBinding.bottomSheet.layoutParams as RelativeLayout.LayoutParams
                params.topMargin = mBinding.coordinatorLayout.height - minH
                mBinding.bottomSheet.layoutParams = params
                mTop = DimensionUtil.dpToPx(8).toInt()
                mBinding.vDrag.setOnTouchListener { _, motionEvent ->

                    if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                        mDownY = motionEvent.y
                    } else if (motionEvent.action == MotionEvent.ACTION_MOVE) {

                        if ((params.topMargin + (motionEvent.y - mDownY)).toInt() >  mBinding.coordinatorLayout.height - minH) {
                            params.topMargin = mBinding.coordinatorLayout.height - minH
                            mBinding.bottomSheet.layoutParams = params
                           return@setOnTouchListener true
                        }

                        if ((params.topMargin + (motionEvent.y - mDownY)).toInt() < mTop) {
                            params.topMargin = mTop
                            mBinding.bottomSheet.layoutParams = params
                            return@setOnTouchListener true
                        }

                        val params =
                            mBinding.bottomSheet.layoutParams as RelativeLayout.LayoutParams
                        params.topMargin =
                            (params.topMargin + (motionEvent.y - mDownY)).toInt()
                        mBinding.bottomSheet.layoutParams = params
                    }

                    true

                }
            }

        })
    }
    var mStationId = ""
    private fun init() {
        mBinding.vMap.map.clear()
        mData.clear()
        mSelectedData.clear()
        mLineId = arguments!!.getString(BundleKeys.KEY_LINE_ID,"")
        if (TextUtils.isEmpty(mLineId)) {
            mBinding.bottomSheet.visibility = View.GONE
            mBinding.rlRecycler.visibility = View.VISIBLE
            val id = arguments!!.getString(BundleKeys.KEY_STATION_ID)
            val stationList =
                arguments!!.getParcelableArrayList<AnnexBusBean.StationListBean>(BundleKeys.KEY_STATION_BEAN)
//            if (stationList == null) {
//                BusQueryPresenter()
//                return
//            }
            stationList!!.forEach {
                if (it.stopList != null && it.stopList.isNotEmpty()) {
                    if (it.stationLineInfo != null && it.stopList[0].stopId == id) {
                        mSelectedData.add(it)
                        return@forEach
                    }
                }
            }
            mData.addAll(stationList)
            mBinding.recyclerView.layoutManager = LinearLayoutManager(context!!)
            mAdapter = BusMapAdapter(this, mSelectedData, object : InnerClickListener {
                override fun onInnerClickListener(position: Int, outPosition: Int) {
                    val json = JsonObject()
                    json.addProperty(
                        "LineId",
                        mSelectedData[outPosition].stationLineInfo[position].lineId
                    )
                    queryLine(json)
                }

            },object :CollectCancelCallback{
                override fun onDialogCallback(type: Int, id: String) {

                    if (TextUtils.isEmpty(UserUtil.getUserInfoBean().token)) {
                        ToastUtil.toastWaring("如果使用收藏功能，请先登录系统。")
//                        showConfirmDialog("提示","如果使用收藏功能，请先登录系统。","登录","取消",object :DialogCallback{
//                            override fun invoke(p1: MaterialDialog) {
//                                if (parentFragment != null) {
//                                    LoginFragment.newInstance(parentFragment as SupportFragment,null)
//                                } else {
//                                    LoginFragment.newInstance(this@BusMapFragment,null)
//                                }
//                            }
//                        },null)
                        return
                    }

                    if (type == 0) {
                        val json = JsonObject()
                        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                        json.addProperty("Type", 1)
                        json.addProperty("MineId",id)
                        mCollectionPresenter!!.addCollection(json)
                    } else {
                        val json = JsonObject()
                        json.addProperty("CollectionId",id)
                        mCollectionPresenter!!.cancelCollection(json)
                    }
                }

            })
            mBinding.recyclerView.adapter = mAdapter

            mBinding.recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    mBinding.recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    if (mBinding.recyclerView.measuredHeight > ScreenUtil.getScreenHeight(context!!)/2) {
                        mBinding.recyclerView.layoutParams.height = ScreenUtil.getScreenHeight(context!!)/2
                        mBinding.recyclerView.requestLayout()
                    }
                }

            })

            showLocation()
            mAdapter!!.setOnItemClickListener(object :
                BaseRecyclerAdapter.OnItemClickListener<AnnexBusBean.StationListBean> {
                override fun onItemClick(
                    view: View?,
                    model: AnnexBusBean.StationListBean?,
                    position: Int
                ) {

                }

                override fun onItemLongClick(
                    view: View?,
                    model: AnnexBusBean.StationListBean?,
                    position: Int
                ) {

                }

            })
        } else {
            val select = arguments!!.getParcelable<AnnexBusBean.StationListBean>(BundleKeys.KEY_SELECTION_BEAN)
            if (select != null) {
                mSelectedData.add(select)
            }
            mBinding.bottomSheet.visibility = View.VISIBLE
            mBinding.rlRecycler.visibility = View.GONE
            val json = JsonObject()
            json.addProperty("LineId", mLineId)
            queryLine(json)
        }

    }

    override fun onNewBundle(args: Bundle?) {
        super.onNewBundle(args)
        arguments = args

        init()

    }

    private fun queryLine(json: JsonObject) {
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)

        val dialog = DialogUtil.getLoadingDialog(fragmentManager)
        HttpUtil.addSubscription(ApiClient.retrofit().queryLineDetail(json),object : ApiCallback<LineDetailBean, Response<BaseBean<LineDetailBean>>>(){
            override fun onSuccess(model: BaseBean<LineDetailBean>) {
                mBinding.bottomSheet.visibility = View.VISIBLE
                mBinding.rlRecycler.visibility = View.GONE

                mLineDetailBean = (model.Data as LineDetailBean)

                if (TextUtils.isEmpty(mLineDetailBean!!.oppositeLineId)) {
                    mBinding.tvLineChange.visibility = View.GONE
                }

                mStationId = mLineDetailBean!!.lineStations[mLineDetailBean!!.lineStations.size - 1].stationId
                mLineId = json.get("LineId").asString


                mBinding.tvLine.text = mLineDetailBean!!.lineName
                mBinding.tvPrice.text = "票价:" + mLineDetailBean!!.ticketPrice + "元"
                mBinding.tvTo.text = mLineDetailBean!!.directionLineName
                mBinding.tvFrom.text = mLineDetailBean!!.departureLineName
                mBinding.tvTimeStart.text = mLineDetailBean!!.startTime
                mBinding.tvTimeEnd.text = mLineDetailBean!!.lastTime
//                mBinding.tvWaitTime.text = mLineDetailBean!!.serviceInterval + "分钟/趟"
                mBinding.tvLineLength.text = "全程:" + mLineDetailBean!!.lineLength + "公里"
                if (mLineDetailBean!!.isCollection == 0) {
                    mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
                } else {
                    mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
                }

                val lineList = ArrayList<BusLineItem>()
                mData.clear()
                mLineDetailBean!!.lineStations.forEach {
                    val bean = AnnexBusBean.StationListBean()
                    bean.stopList = ArrayList()
                    val stopListBean = AnnexBusBean.StopListBean()
                    stopListBean.lat = it.lat
                    stopListBean.stopId = it.stationId
                    stopListBean.lon = it.lon
                    stopListBean.stopName = it.stationName
                    bean.stopList.add(stopListBean)
                    bean.stationName = it.stationName
                    bean.stationLineInfo = ArrayList<AnnexBusBean.StationListBean.StationLineInfoBean>()
                    val busLineItem = BusLineItem()
                    busLineItem.lat = it.lat
                    busLineItem.lon = it.lon
                    busLineItem.name = it.stationName
                    busLineItem.stationId = it.stationId
                    busLineItem.lines = ArrayList()
                    if (it.transferInfo != null) {
                        it.transferInfo.forEach {it1 ->
                            val stationLineInfoBean = AnnexBusBean.StationListBean.StationLineInfoBean()
                            stationLineInfoBean.lineName = it1.lineName
                            stationLineInfoBean.lineNo = it1.lineID
                            bean.stationLineInfo.add(stationLineInfoBean)
                            busLineItem.lines.add(it1.lineName)
                        }
                    }
                    mData.add(bean)
                    lineList.add(busLineItem)
                }

                mBinding.busView.busLineData = lineList

                stopTimer()
                startTimer()

                val latLngs = drawMap()
                if (latLngs.size == 0 && mSelectedData.isNotEmpty()) {
                    val latLng = LatLng(mSelectedData[0].stopList[0].lat, mSelectedData[0].stopList[0].lon)
                    //设置中心点和缩放比例
                    mBinding.vMap.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))
                    mBinding.vMap.map.animateCamera(CameraUpdateFactory.zoomTo(14f))
                } else if (latLngs.size == 0) {
                    val latLng = LatLng(UserUtil.getUser().latitude.toDouble() , UserUtil.getUser().longitude.toDouble() )
                    //设置中心点和缩放比例
                    mBinding.vMap.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))
                    mBinding.vMap.map.animateCamera(CameraUpdateFactory.zoomTo(13f))
                } else if (latLngs.size > 0) {
                    var x = 0.0
                    var y = 0.0
                    latLngs.forEach {
                        x += it.latitude
                        y += it.longitude
                    }
                    val latLng = LatLng(x/latLngs.size , y/latLngs.size)
                    //设置中心点和缩放比例
                    mBinding.vMap.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))
                    mBinding.vMap.map.animateCamera(CameraUpdateFactory.zoomTo(13f))
                }

                mBinding.busView.setOnBusStationClickListener { view, item ->
                    mBinding.vMap.map.mapScreenMarkers.forEach {
                        if (it.zIndex != 500f)  {
                            if (it.position.latitude == item.lat && it.position.longitude == item.lon) {
                                it.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        BitmapFactory
                                            .decodeResource(
                                                resources,
                                                R.mipmap.icon_station_checked
                                            )
                                    )
                                )
                                it.showInfoWindow()
                                mBinding.vMap.map.animateCamera(CameraUpdateFactory.changeLatLng(it.position))
                            } else {
                                it.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        BitmapFactory
                                            .decodeResource(resources, R.mipmap.icon_station)
                                    )
                                )
                                it.hideInfoWindow()
                            }
                        }
                    }
                }

                mBinding.vMap.map.setOnMarkerClickListener {

                    if (mBinding.bottomSheet.visibility == View.GONE) {
                        mBinding.vMap.map.mapScreenMarkers.forEach { it1 ->

                            if (it1.zIndex != 500f)  {
                                it1.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        BitmapFactory
                                            .decodeResource(resources, R.mipmap.icon_station)
                                    )
                                )
                                if (it1 != it) {
                                    it1.hideInfoWindow()
                                }
                            }
                        }


                        it.setIcon(BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory
                                .decodeResource(resources,R.mipmap.icon_station_checked)))
                        it.showInfoWindow()
                        mData.forEach { it2 ->
                            var index = 0
                            it2.stopList?.forEach { it3 ->
                                if (it3.lat == it.position.latitude && it3.lon == it.position.longitude) {
                                    mBinding.bottomSheet.visibility = View.GONE
                                    mBinding.rlRecycler.visibility = View.VISIBLE
                                    mSelectedData.clear()
                                    mSelectedData.add(it2)
                                    mAdapter!!.mSelected = index
                                    mBinding.recyclerView.setNewData(mSelectedData)

                                    return@forEach
                                }
                                index ++
                            }
                        }
                        return@setOnMarkerClickListener true
                    }

                    if (it.zIndex == 500f)  {
                       return@setOnMarkerClickListener false
                    }
                    mBinding.vMap.map.mapScreenMarkers.forEach { it1 ->

                        if (it1.zIndex != 500f)  {
                            it1.setIcon(
                                BitmapDescriptorFactory.fromBitmap(
                                    BitmapFactory
                                        .decodeResource(resources, R.mipmap.icon_station)
                                )
                            )
                            if (it1 != it) {
                                it1.hideInfoWindow()
                            }
                        }
                    }


                    it.setIcon(BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory
                            .decodeResource(resources,R.mipmap.icon_station_checked)))
                    it.showInfoWindow()
                    mBinding.busView.busLineData.forEach { it2 ->
                        it2.isCurrentPosition = false
                        if (it2.lat == it.position.latitude && it2.lon == it.position.longitude) {
                            it2.isCurrentPosition = true
                            mBinding.busView.scrollToCurrent()
                        }
                    }
                    mBinding.busView.invalidate()
                    true
                }
            }

            override fun onFailure(msg: String?) {
                ToastUtil.toastError(msg)
            }

            override fun onFinish() {
                dialog.dismiss()
            }

        },this@BusMapFragment,null)
    }

    private fun drawMap():ArrayList<LatLng> {
        val latLngs = ArrayList<LatLng>()

        if (!TextUtils.isEmpty(mLineDetailBean!!.lonLatStr)){
            mLineDetailBean!!.lonLatStr.split(";").forEach {
                val arr = it.split(",")
                latLngs.add(LatLng(arr[1].toDouble(),arr[0].toDouble()))
            }
        }

        mBinding.vMap.map.clear()


        mPolyline = mBinding.vMap.map.addPolyline(PolylineOptions().addAll(latLngs).width(DimensionUtil.dpToPx(15)))
        mPolyline!!.setCustomTexture(BitmapDescriptorFactory.fromBitmap(
            BitmapFactory
                .decodeResource(resources,R.mipmap.bus_route_texture)))

        drawMarkers()
        return latLngs
    }

    private fun queryLineByOpposite() {
        val dialog = DialogUtil.getLoadingDialog(fragmentManager)
        val json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        json.addProperty("LineId",mLineDetailBean!!.oppositeLineId)
        val lineId = mLineDetailBean!!.oppositeLineId
        HttpUtil.addSubscription(ApiClient.retrofit().queryLineDetail(json),object : ApiCallback<LineDetailBean, Response<BaseBean<LineDetailBean>>>(){
            override fun onSuccess(model: BaseBean<LineDetailBean>) {
                mBinding.bottomSheet.visibility = View.VISIBLE
                mBinding.rlRecycler.visibility = View.GONE

                mLineDetailBean = (model.Data as LineDetailBean)
                mLineId = lineId
                mStationId = mLineDetailBean!!.lineStations[mLineDetailBean!!.lineStations.size - 1].stationId


                mBinding.tvLine.text = mLineDetailBean!!.lineName
                mBinding.tvPrice.text = "票价:" + mLineDetailBean!!.ticketPrice + "元"
                mBinding.tvTo.text = mLineDetailBean!!.directionLineName
                mBinding.tvFrom.text = mLineDetailBean!!.departureLineName
                mBinding.tvTimeStart.text = mLineDetailBean!!.startTime
                mBinding.tvTimeEnd.text = mLineDetailBean!!.lastTime
//                mBinding.tvWaitTime.text = mLineDetailBean!!.serviceInterval + "分钟/趟"
                mBinding.tvLineLength.text = "全程:" + mLineDetailBean!!.lineLength + "公里"
                if (mLineDetailBean!!.isCollection == 0) {
                    mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
                } else {
                    mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
                }

                val lineList = ArrayList<BusLineItem>()
                mData.clear()
                mLineDetailBean!!.lineStations.forEach {
                    val bean = AnnexBusBean.StationListBean()
                    bean.stopList = ArrayList()
                    val stopListBean = AnnexBusBean.StopListBean()
                    stopListBean.lat = it.lat
                    stopListBean.stopId = it.stationId
                    stopListBean.lon = it.lon
                    stopListBean.stopName = it.stationName
                    bean.stopList.add(stopListBean)
                    bean.stationName = it.stationName
                    bean.stationLineInfo = ArrayList<AnnexBusBean.StationListBean.StationLineInfoBean>()
                    val busLineItem = BusLineItem()
                    busLineItem.lat = it.lat
                    busLineItem.lon = it.lon
                    busLineItem.name = it.stationName
                    busLineItem.stationId = it.stationId
                    busLineItem.lines = ArrayList()
                    if (it.transferInfo != null) {
                        it.transferInfo.forEach {it1 ->
                            val stationLineInfoBean = AnnexBusBean.StationListBean.StationLineInfoBean()
                            stationLineInfoBean.lineName = it1.lineName
                            stationLineInfoBean.lineNo = it1.lineID
                            bean.stationLineInfo.add(stationLineInfoBean)
                            busLineItem.lines.add(it1.lineName)
                        }
                    }
                    mData.add(bean)
                    lineList.add(busLineItem)
                }

                mBinding.busView.busLineData = lineList

                stopTimer()
                startTimer()

               val latLngs = drawMap()
                if (latLngs.size == 0 && mSelectedData.isNotEmpty()) {
                    val latLng = LatLng(mSelectedData[0].stopList[0].lat, mSelectedData[0].stopList[0].lon)
                    //设置中心点和缩放比例
                    mBinding.vMap.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))
                    mBinding.vMap.map.animateCamera(CameraUpdateFactory.zoomTo(14f))
                } else if (latLngs.size == 0) {
                    val latLng = LatLng(UserUtil.getUser().latitude.toDouble() , UserUtil.getUser().longitude.toDouble() )
                    //设置中心点和缩放比例
                    mBinding.vMap.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))
                    mBinding.vMap.map.animateCamera(CameraUpdateFactory.zoomTo(13f))
                } else if (latLngs.size > 0) {
                    var x = 0.0
                    var y = 0.0
                    latLngs.forEach {
                        x += it.latitude
                        y += it.longitude
                    }
                    val latLng = LatLng(x/latLngs.size , y/latLngs.size)
                    //设置中心点和缩放比例
                    mBinding.vMap.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))
                    mBinding.vMap.map.animateCamera(CameraUpdateFactory.zoomTo(13f))
                }

                mBinding.busView.setOnBusStationClickListener { view, item ->
                    mBinding.vMap.map.mapScreenMarkers.forEach {
                        if (it.zIndex != 500f)  {
                            if (it.position.latitude == item.lat && it.position.longitude == item.lon) {
                                it.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        BitmapFactory
                                            .decodeResource(
                                                resources,
                                                R.mipmap.icon_station_checked
                                            )
                                    )
                                )
                                it.showInfoWindow()
                                mBinding.vMap.map.animateCamera(CameraUpdateFactory.changeLatLng(it.position))
                            } else {
                                it.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        BitmapFactory
                                            .decodeResource(resources, R.mipmap.icon_station)
                                    )
                                )
                                it.hideInfoWindow()
                            }
                        }
                    }
                }

                mBinding.vMap.map.setOnMarkerClickListener {
                    if (mBinding.bottomSheet.visibility == View.GONE) {
                        mBinding.vMap.map.mapScreenMarkers.forEach { it1 ->

                            if (it1.zIndex != 500f)  {
                                it1.setIcon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        BitmapFactory
                                            .decodeResource(resources, R.mipmap.icon_station)
                                    )
                                )
                                if (it1 != it) {
                                    it1.hideInfoWindow()
                                }
                            }
                        }


                        it.setIcon(BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory
                                .decodeResource(resources,R.mipmap.icon_station_checked)))
                        it.showInfoWindow()
                        mData.forEach { it2 ->
                            var index = 0
                            it2.stopList?.forEach { it3 ->
                                if (it3.lat == it.position.latitude && it3.lon == it.position.longitude) {
                                    mBinding.bottomSheet.visibility = View.GONE
                                    mBinding.rlRecycler.visibility = View.VISIBLE
                                    mSelectedData.clear()
                                    mSelectedData.add(it2)
                                    mAdapter!!.mSelected = index
                                    mBinding.recyclerView.setNewData(mSelectedData)

                                    return@forEach
                                }
                                index ++
                            }

                        }
                        return@setOnMarkerClickListener true
                    }
                    if (it.zIndex == 500f)  {
                        return@setOnMarkerClickListener false
                    }
                    mBinding.vMap.map.mapScreenMarkers.forEach { it1 ->
                        if (it1.zIndex != 500f)  {
                            it1.setIcon(
                                BitmapDescriptorFactory.fromBitmap(
                                    BitmapFactory
                                        .decodeResource(resources, R.mipmap.icon_station)
                                )
                            )
                            if (it1 != it) {
                                it1.hideInfoWindow()
                            }
                        }
                    }
                    it.setIcon(BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory
                            .decodeResource(resources,R.mipmap.icon_station_checked)))
                    it.showInfoWindow()
                    mBinding.busView.busLineData.forEach { it2 ->
                        it2.isCurrentPosition = false
                        if (it2.lat == it.position.latitude && it2.lon == it.position.longitude) {
                            it2.isCurrentPosition = true
                            mBinding.busView.scrollToCurrent()
                        }
                    }
                    mBinding.busView.invalidate()

                    true
                }
            }

            override fun onFailure(msg: String?) {
                ToastUtil.toastError(msg)
            }

            override fun onFinish() {
                dialog.dismiss()
            }

        },this@BusMapFragment,null)
    }

    private fun addSearchListener() {
        mBinding.etSearch.setOnClickListener {
            (parentFragment as QueryMainFragment).replace(BusQueryFragment())
//            QueryMainFragment.newInstance(this,null)
        }

        mBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                (parentFragment as QueryMainFragment).mQueryStr = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }


    private fun showLocation() {

        if (mSelectedData[0].stopList != null && mSelectedData[0].stopList.isNotEmpty()) {
            val latLng = LatLng(mSelectedData[0].stopList[0].lat, mSelectedData[0].stopList[0].lon)
            //设置中心点和缩放比例
            mBinding.vMap.map.animateCamera(CameraUpdateFactory.changeLatLng(latLng))
        }
        mBinding.vMap.map.animateCamera(CameraUpdateFactory.zoomTo(14f))

        drawMarkers()
        mBinding.vMap.map.setOnMarkerClickListener {
            if (mBinding.bottomSheet.visibility == View.GONE) {
                mBinding.vMap.map.mapScreenMarkers.forEach { it1 ->

                    if (it1.zIndex != 500f)  {
                        it1.setIcon(
                            BitmapDescriptorFactory.fromBitmap(
                                BitmapFactory
                                    .decodeResource(resources, R.mipmap.icon_station)
                            )
                        )
                        if (it1 != it) {
                            it1.hideInfoWindow()
                        }
                    }
                }


                it.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory
                        .decodeResource(resources,R.mipmap.icon_station_checked)))
                it.showInfoWindow()
                mData.forEach { it2 ->
                    var index = 0
                    it2.stopList?.forEach { it3 ->
                        if (it3.lat == it.position.latitude && it3.lon == it.position.longitude) {
                            mBinding.bottomSheet.visibility = View.GONE
                            mBinding.rlRecycler.visibility = View.VISIBLE
                            mSelectedData.clear()
                            mSelectedData.add(it2)
                            mAdapter!!.mSelected = index
                            mBinding.recyclerView.setNewData(mSelectedData)

                            return@forEach
                        }
                        index ++
                    }
                }
                return@setOnMarkerClickListener true
            }
            if (it.zIndex != 500f)  {
                return@setOnMarkerClickListener false
            }
            if (mBinding.bottomSheet.visibility == View.VISIBLE) {
                return@setOnMarkerClickListener false
            }
            mBinding.vMap.map.mapScreenMarkers.forEach { it1 ->
                if (it1.zIndex != 500f) {
                    it1.setIcon(
                        BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory
                                .decodeResource(resources, R.mipmap.icon_station)
                        )
                    )
                }
            }
            it.setIcon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory
                    .decodeResource(resources,R.mipmap.icon_station_checked)))

            mData.forEach { it2 ->
                var index = 0
                it2.stopList?.forEach { it3 ->
                    if (it3.lat == it.position.latitude && it3.lon == it.position.longitude) {
                        mBinding.bottomSheet.visibility = View.GONE
                        mBinding.rlRecycler.visibility = View.VISIBLE
                        mSelectedData.clear()
                        mSelectedData.add(it2)
                        mAdapter!!.mSelected = index
                        mBinding.recyclerView.setNewData(mSelectedData)

                        return@forEach
                    }
                    index ++
                }
            }
            false
        }

    }

    private fun drawMarkers() {
        val list = ArrayList<MarkerOptions>()
        mData.forEach {
            if (it.stopList != null && it.stopList.isNotEmpty()) {
                it.stopList.forEach { it2 ->
                    var icon: BitmapDescriptor? = if (mSelectedData.isNotEmpty() && mSelectedData[0].stopList != null && mSelectedData[0].stopList.isNotEmpty()
                        && mSelectedData[0].stopList[0].stopId == it2.stopId) {
                        BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory
                                .decodeResource(resources,R.mipmap.icon_station_checked))
                    }else {
                        BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory
                                .decodeResource(resources,R.mipmap.icon_station))
                    }
                    val latLng = LatLng(it2.lat, it2.lon)
                    list.add(MarkerOptions().position(latLng).title(it2.stopName).icon(icon).setFlat(true))
                }
            }
        }

        if (mLineDetailBean != null) {
            if (mLineDetailBean!!.lineStations != null) {
                if (mLineDetailBean!!.lineStations.size >  0) {
                    if (list.size > 0) {
                        list.removeAt(0)
                    }
                    if (mLineDetailBean!!.lineStations.size > 1) {
                        var icon =  BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources,R.mipmap.icon_trans_start))
                        var latLng = LatLng(mLineDetailBean!!.lineStations[0].lat,mLineDetailBean!!.lineStations[0].lon)
                        list.add(MarkerOptions().position(latLng).title(mLineDetailBean!!.lineStations[0].stationName).icon(icon).setFlat(true))

                        if (list.size > 1) {
                            list.removeAt(list.size - 2)
                        }
                        icon =  BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory
                                .decodeResource(resources,R.mipmap.icon_trans_end))
                        latLng = LatLng(mLineDetailBean!!.lineStations[mLineDetailBean!!.lineStations.size - 1].lat,mLineDetailBean!!.lineStations[mLineDetailBean!!.lineStations.size - 1].lon)
                        list.add(MarkerOptions().position(latLng).title(mLineDetailBean!!.lineStations[mLineDetailBean!!.lineStations.size - 1].stationName).icon(icon).setFlat(true))

                    } else {
                        var icon =  BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources,R.mipmap.icon_trans_start))
                        var latLng = LatLng(mLineDetailBean!!.lineStations[0].lat,mLineDetailBean!!.lineStations[0].lon)
                        list.add(MarkerOptions().position(latLng).title(mLineDetailBean!!.lineStations[0].stationName).icon(icon).setFlat(true))
                    }
//                    mBinding.busView.arriveVehs.forEach { vehs ->
//                        var icon =  BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources,R.mipmap.icon_bus_vertical))
//                        var latLng = LatLng(vehs.lat,vehs.lon)
//                        list.add(MarkerOptions().position(latLng).icon(icon).rotateAngle(360 - vehs.angle.toFloat()).zIndex(500f).setFlat(true))
//                    }
                }
            }
        }
        mBinding.vMap.map.addMarkers(list,false)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        stopTimer()
        mBinding.vMap.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mBinding.vMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.vMap.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.vMap.onSaveInstanceState(outState)
    }

    override fun doAfterAnim() {
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.tv_line_change -> {
                queryLineByOpposite()
            }

            R.id.v_heart -> {
                if (TextUtils.isEmpty(UserUtil.getUserInfoBean().token)) {
                    ToastUtil.toastWaring("如果使用收藏功能，请先登录系统。")
//                    showConfirmDialog("提示","如果使用收藏功能，请先登录系统。","登录","取消",object :DialogCallback{
//                        override fun invoke(p1: MaterialDialog) {
//                            if (parentFragment != null) {
//                                LoginFragment.newInstance(parentFragment as SupportFragment,null)
//                            } else {
//                                LoginFragment.newInstance(this@BusMapFragment,null)
//                            }
//                        }
//                    },null)
                    return
                }
                if (mLineDetailBean != null) {
                    if (mLineDetailBean!!.isCollection == 0) {
                        val json = JsonObject()
                        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                        json.addProperty("Type", 2)
                        json.addProperty("MineId",mLineId)
                        mCollectionPresenter!!.addCollection(json)
                    } else {
                        val json = JsonObject()
                        json.addProperty("CollectionId",mLineDetailBean!!.collectionId)
                        mCollectionPresenter!!.cancelCollection(json)
                    }
                }
            }
        }
    }

}