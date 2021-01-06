package com.jtcxw.glcxw.ui.travel

import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.BusStationAdapter
import com.jtcxw.glcxw.adapter.HomeBannerAdapter
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.*
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.RxBus
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentGoTravelBinding
import com.jtcxw.glcxw.events.CollectEvent
import com.jtcxw.glcxw.events.MessageEvent
import com.jtcxw.glcxw.fragment.MainFragment
import com.jtcxw.glcxw.listeners.InnerClickListener
import com.jtcxw.glcxw.localbean.HomeBannerBean
import com.jtcxw.glcxw.presenters.impl.GoTravelPresenter
import com.jtcxw.glcxw.ui.BusMapFragment
import com.jtcxw.glcxw.ui.LocationFragment
import com.jtcxw.glcxw.ui.QueryMainFragment
import com.jtcxw.glcxw.ui.WebFragment
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.GoTravelView
import com.youth.banner.indicator.CircleIndicator
import me.yokeyword.fragmentation.SupportFragment
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer

class GoTravelFragment: LocationFragment<FragmentGoTravelBinding, CommonModel>() ,GoTravelView{
    var mModuleConfigBean:ModuleConfigBean?= null
    override fun onModuleConfigSucc(moduleConfigBean: ModuleConfigBean) {
        mModuleConfigBean = moduleConfigBean
        val bundle = Bundle()
        bundle.putString(BundleKeys.KEY_WEB_TITLE,mModuleConfigBean!!.funName)
        bundle.putString(BundleKeys.KEY_WEB_URL,mModuleConfigBean!!.url)
        WebFragment.newInstance(parentFragment as SupportFragment,bundle)
    }

    override fun onGetMisceAneousBeanSucc(misceAneousBean: MisceAneousBean) {
        mTel = misceAneousBean.itemName + "(" +misceAneousBean.itemValue + ")"
        if (mDatas.size > 0) {
            mDatas[0].phoneTip = mTel
        }
        mBinding.recyclerView.innerAdapter.updateItem(0)
    }

    override fun onGetBannerSucc(bannerBean: BannerBean) {
        mBannerList.clear()
        mBannerList.addAll(bannerBean.bannerList)
        mBinding.banner.adapter.notifyDataSetChanged()
    }

    override fun onGetBannerFinish() {
    }

    override fun onForcastArriveQuerySucc(busArriveListBean: BusArriveListBean) {

        if (busArriveListBean.stationLineList.isNotEmpty()) {
            if (mDatas.isNotEmpty()) {
                for (i in mDatas) {
                    for (j in busArriveListBean.stationLineList.indices) {
                        val bean = busArriveListBean.stationLineList[j]
                        if (i.stopList != null && i.stopList.isNotEmpty()) {
                            i.stopList.forEach { stop ->
                                if (stop.stopId == bean.stationId) {
                                    if (i.stationLineInfo != null) {
                                        i.stationLineInfo.forEach {
                                            if (it.lineId == bean.lineId){
                                                if (bean.forcastArriveVehs != null) {
                                                    it.lastUpdTime = bean.lastUpdTime
                                                    if (bean.forcastArriveVehs.size > 0) {
                                                        it.forecastTime =
                                                            bean.forcastArriveVehs[0].forecastTime
                                                        it.nextLevel = bean.forcastArriveVehs[0].nextLevel
                                                    } else {
                                                        it.forecastTime = ""
                                                        it.nextLevel = ""
                                                    }
                                                } else {
                                                    it.forecastTime = ""
                                                }
//                                            busArriveListBean.stationLineList.removeAt(j)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                mBinding.recyclerView.innerAdapter.notifyAllItems()
            }
        }

    }

    override fun onLogout() {
        mBinding.swipeLayout.autoRefresh()
    }

    override fun refresh() {
        mBinding.swipeLayout.autoRefresh()
    }
    override fun onBusInquiryAnnexBusFinish() {
        mBinding.swipeLayout.finishRefresh(0)
    }

    override fun onBusInquiryAnnexBusSucc(annexBusBean: AnnexBusBean) {
        mDatas.clear()
        val header = AnnexBusBean.StationListBean()
        header.type = 1
        header.phoneTip = mTel
        mDatas.add(header)
        mDatas.addAll(annexBusBean.stationList)
        mBinding.recyclerView.setNewData(mDatas,false)
        if(mDatas.isNotEmpty()) {
            stopTimer()
            startTimer()
        }
    }

    override fun onLocationChange(aMapLocation: AMapLocation) {
//        mTitle!!.text = aMapLocation.city.substring(0,aMapLocation.city.length - 1)+ "出行"
        if (ismBindingInitialized()) {
            mTitle!!.text = UserUtil.getUser().city + "出行"
            refreshData()
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.rl_car_search -> {
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_go_travel
    }
    var mTel = ""
    val mBannerList = ArrayList<BannerBean.BannerListBean>()
    var timer: Timer?= null
    private var mPresenter: GoTravelPresenter?= null
    private var mDatas = ArrayList<AnnexBusBean.StationListBean>()
    private var mTitle:TextView ?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BaseUtil.isDarkMode()) {
            mBinding.rlBg.setBackgroundResource(0)
        }

        mPresenter = GoTravelPresenter(this)
        val header = AnnexBusBean.StationListBean()
        header.type = 1
        header.phoneTip = mTel
        mDatas.add(header)
        mTitle = mBinding.root.findViewById<TextView>(R.id.tv_center_title)
        mTitle!!.text = "出行"
        mTitle!!.visibility = View.VISIBLE
        mTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        mTitle!!.setTextColor(resources.getColor(backColor()))

        SwipeUtil.initHeader(mBinding.header)

        mBinding.swipeLayout.setOnRefreshListener {

            if (TextUtils.isEmpty(UserUtil.getUser().latitude)) {
                (parentFragment as MainFragment).checkLocation()
                it.finishRefresh(0)
                return@setOnRefreshListener
            }

            refreshData()
        }


        mBinding.banner.adapter = HomeBannerAdapter(mBannerList, 10)
        mBinding.banner.addBannerLifecycleObserver(this)
        mBinding.banner.indicator = CircleIndicator(context)

        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = BusStationAdapter(this,mDatas,object : InnerClickListener {
            override fun onInnerClickListener(position: Int, outPosition: Int) {

                if (outPosition == 0) {
                    if (position == 3) {
                        if (mModuleConfigBean == null) {
                            val json = JsonObject()
                            json.addProperty("Longitude", UserUtil.getUser().longitude)
                            json.addProperty("Latitude", UserUtil.getUser().latitude)
                            json.addProperty("FunId", "1")
                            json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
                            mPresenter!!.h5ModuleConfig(json)
                        } else {
                            val bundle = Bundle()
                            bundle.putString(BundleKeys.KEY_WEB_TITLE,mModuleConfigBean!!.funName)
                            bundle.putString(BundleKeys.KEY_WEB_URL,mModuleConfigBean!!.url)
                            WebFragment.newInstance(parentFragment as SupportFragment,bundle)
                        }
                    }
                    return
                }

                val bundle = Bundle()
                bundle.putParcelable(BundleKeys.KEY_SELECTION_BEAN,mDatas[outPosition])
                bundle.putString(BundleKeys.KEY_LINE_ID,
                    mDatas[outPosition].stationLineInfo[position].lineId)
                QueryMainFragment.newInstance(this@GoTravelFragment.parentFragment as SupportFragment,bundle)
            }

        })
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<AnnexBusBean.StationListBean>{
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
                if (position == 0) {
                    return
                }
                val bundle = Bundle()
                bundle.putParcelableArrayList(BundleKeys.KEY_STATION_BEAN,mDatas)
                bundle.putString(BundleKeys.KEY_STATION_ID,model!!.stopList[0].stopId)
                QueryMainFragment.newInstance(this@GoTravelFragment.parentFragment as SupportFragment,bundle)
            }

        })
        mBinding.recyclerView.setItemViewCacheSize(30)
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.setNewData(mDatas)

        mBinding.swipeLayout.autoRefresh()

        RxBus.getDefault().toObservable(CollectEvent::class.java)
            .subscribe {
                it as CollectEvent
                mDatas.forEach { it_ ->

                    if (it_.stationLineInfo != null) {
                        it_.stationLineInfo.forEach { it_2 ->

                            if (it_2.lineId == it.id) {
                                it_2.isCollection = it.isCollection
                                it_2.collectionId = it.collectionId
                            }
                        }
                    }
                }
                if (mBinding.recyclerView.adapter != null && mDatas.isNotEmpty()) {
                    mBinding.recyclerView.innerAdapter.notifyAllItems()
                }
            }



    }

    private fun refreshData() {
        var json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        json.addProperty("Longitude",UserUtil.getUser().longitude)
        json.addProperty("latitude",UserUtil.getUser().latitude)
        mPresenter!!.busInquiryAnnexBus(json,mBinding.swipeLayout)

        json = JsonObject()
        json.addProperty("Type","2")
        json.addProperty("Longitude",UserUtil.getUser().longitude)
        json.addProperty("latitude",UserUtil.getUser().latitude)
        mPresenter!!.getBanner(json,mBinding.swipeLayout)

        json = JsonObject()
        json.addProperty("ItemType","1")
        mPresenter!!.getMisceAneous(json)
    }

    override fun doAfterAnim() {

    }

//    private fun callPhoneWithoutPermission(phoneNum:String) {
//        val intent = Intent(Intent.ACTION_DIAL)
//        val data = Uri.parse("tel:$phoneNum")
//        intent.data = data
//        startActivity(intent)
//    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }

    private fun startTimer() {
        timer = fixedRateTimer("", false, 0, 15000) {
            if (mDatas.isEmpty()) {
                return@fixedRateTimer
            }
            val json = JsonObject()
            val jsonArray = JsonArray()
            mDatas.forEach {
                if (it.stationLineInfo != null) {
                    it.stationLineInfo.forEach { it_ ->
                        val item = JsonObject()
                        item.addProperty("StationId",it_.stopId)
                        item.addProperty("LineId",it_.lineId)
                        jsonArray.add(item)
                    }
                }
            }
            json.add("StationLineList",jsonArray)
            mPresenter!!.forcastArriveQuery(json)
        }
    }

    private fun stopTimer() {
        timer?.cancel()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

//    override fun onSupportVisible() {
//        super.onSupportVisible()
//        if (ismBindingInitialized()) {
//            if (mBinding.recyclerView.adapter != null && mDatas.isNotEmpty()) {
//                mBinding.recyclerView.innerAdapter.notifyAllItems()
//            }
//        }
//    }
}
