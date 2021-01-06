package com.jtcxw.glcxw.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.amap.api.location.AMapLocation
import com.amap.api.services.weather.LocalWeatherForecastResult
import com.amap.api.services.weather.LocalWeatherLiveResult
import com.google.gson.JsonObject
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.BusAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentBusBinding
import com.jtcxw.glcxw.listeners.InnerClickListener
import com.jtcxw.glcxw.listeners.LocationCallback
import com.jtcxw.glcxw.presenters.impl.BusPresenter
import com.jtcxw.glcxw.utils.LocationUtil
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.GoTravelView
import me.yokeyword.fragmentation.SupportFragment
import com.amap.api.services.weather.WeatherSearch
import com.amap.api.services.weather.WeatherSearchQuery
import com.jtcxw.glcxw.base.respmodels.*


class BusFragment:BaseFragment<FragmentBusBinding,CommonModel>() , GoTravelView ,WeatherSearch.OnWeatherSearchListener{
    override fun onModuleConfigSucc(moduleConfigBean: ModuleConfigBean) {
    }

    override fun onGetMisceAneousBeanSucc(misceAneousBean: MisceAneousBean) {
    }

    override fun onWeatherLiveSearched(weatherLiveResult: LocalWeatherLiveResult?, rCode: Int) {
        if (rCode == 1000) {
            if (weatherLiveResult?.liveResult != null) {
               val weatherlive = weatherLiveResult.liveResult
                mBinding.tvTemperature.text = "${weatherlive.temperature}°C  ${weatherlive.weather}"
            }else {

            }
        } else {

        }
    }

    override fun onWeatherForecastSearched(p0: LocalWeatherForecastResult?, p1: Int) {
    }

    override fun onGetBannerSucc(bannerBean: BannerBean) {
    }

    override fun onGetBannerFinish() {
    }

    override fun onForcastArriveQuerySucc(busArriveListBean: BusArriveListBean) {
    }

    override fun onBusInquiryAnnexBusSucc(annexBusBean: AnnexBusBean) {
        mDatas.clear()
        mDatas.addAll(annexBusBean.stationList)
        mBinding.recyclerView.setNewData(mDatas,false)
    }

    override fun onBusInquiryAnnexBusFinish() {
        mBinding.swipeLayout.finishRefresh(0)
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val busFragment = BusFragment()
            busFragment.arguments = bundle
            fragment.start(busFragment)
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bus
    }

    private val mLocation = LocationUtil()

    private var mPresenter: BusPresenter?= null
    private var mDatas = ArrayList<AnnexBusBean.StationListBean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SwipeUtil.initHeader(mBinding.header)
        initToolBar("公交查询")
        queryWeather()
        mBinding.tvCity.text = UserUtil.getUser().city
        mPresenter = BusPresenter(this)
        mBinding.etSearch.setOnClickListener(this)
        mBinding.swipeLayout.setOnRefreshListener {

            val json = JsonObject()
            json.addProperty("Longitude", UserUtil.getUser().longitude)
            json.addProperty("latitude", UserUtil.getUser().latitude)
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            mPresenter!!.busInquiryAnnexBus(json,mBinding.swipeLayout)
        }

        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = BusAdapter(context!!,mDatas,object :InnerClickListener{
            override fun onInnerClickListener(position: Int, outPosition: Int) {
                val bundle = Bundle()
                bundle.putParcelable(BundleKeys.KEY_SELECTION_BEAN,mDatas[outPosition])
                bundle.putString(BundleKeys.KEY_LINE_ID,
                    mDatas[outPosition].stationLineInfo[position].lineId)
                QueryMainFragment.newInstance(this@BusFragment as SupportFragment,bundle)
            }

        })
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<AnnexBusBean.StationListBean>{
            override fun onItemLongClick(
                view: View?,
                model: AnnexBusBean.StationListBean?,
                position: Int
            ) {
            }

            override fun onItemClick(
                view: View?,
                model: AnnexBusBean.StationListBean?,
                position: Int
            ) {
                val bundle = Bundle()
                bundle.putParcelableArrayList(BundleKeys.KEY_STATION_BEAN,mDatas)
                bundle.putString(BundleKeys.KEY_STATION_ID,model!!.stopList[0].stopId)
                QueryMainFragment.newInstance(this@BusFragment as SupportFragment,bundle)
            }

        })
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.setNewData(mDatas)

        mBinding.swipeLayout.autoRefresh()

        mBinding.ivLocation.setOnClickListener {
            XXPermissions.with(activity)
                .permission(Permission.Group.LOCATION) //不指定权限则自动获取清单中的危险权限
                .request(object : OnPermission {
                    override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                        if (quick) {
                        } else {
                            MaterialDialog(context!!)
                                .title(null, "提示")
                                .message(null, "未授权定位权限,可能会影响您的使用")
                                .positiveButton(null, SpannelUtil.getSpannelStr("确认", context!!.resources.getColor(R.color.blue_3A75F3)),
                                    null)
                                .lifecycleOwner(activity)
                                .cornerRadius(DimensionUtil.dpToPx(2), null)
                                .cancelable(false)
                                .show()
                        }
                    }

                    override fun hasPermission(granted: List<String>, isAll: Boolean) {
                        if (isAll) {
                            mLocation.init(context,object :LocationCallback{
                                override fun onLocationCallback(aMapLocation: AMapLocation) {
                                    mLocation.stopLocation()
                                    mBinding.tvCity.text = UserUtil.getUser().city
                                }

                            })
                            mLocation.start()
                        }
                    }
                })
        }

    }

    private fun queryWeather() {
       val query = WeatherSearchQuery("桂林", WeatherSearchQuery.WEATHER_TYPE_LIVE)
       val weathersearch = WeatherSearch(context)
        weathersearch.setOnWeatherSearchListener(this)
        weathersearch.query = query
        weathersearch.searchWeatherAsyn() //异步搜索
    }

    override fun doAfterAnim() {
    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.et_search -> {
                QueryMainFragment.newInstance(this,null)
            }
        }
    }

    override fun backColor(): Int {
        return R.color.white
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocation.destroy()
    }

    var hasInit = false
    override fun onSupportVisible() {
        super.onSupportVisible()
        if (hasInit) {
            mBinding.swipeLayout.autoRefresh()
        }
        hasInit = true
    }

}