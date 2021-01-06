package com.jtcxw.glcxw.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.BusHistoryAdapter
import com.jtcxw.glcxw.adapter.BusStationLineAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.DeviceUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentBusQueryBinding
import com.jtcxw.glcxw.listeners.InnerClickListener
import com.jtcxw.glcxw.localbean.BusSiteOrLineBean
import com.jtcxw.glcxw.localbean.BusSiteOrLineHistoryBean
import com.jtcxw.glcxw.presenters.impl.BusQueryPresenter
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.BusQueryView
import me.yokeyword.fragmentation.SupportFragment



class BusQueryFragment:BaseFragment<FragmentBusQueryBinding,CommonModel>(),BusQueryView {
    override fun onClearQueryHistorySucc(jsonObject: JsonObject) {
        if (jsonObject.get("result").asBoolean) {
            mHistoryDatas.clear()
            mBinding.recyclerViewHistory.setNewData(mHistoryDatas,false)
            mBinding.ivClear.visibility = View.GONE
        }
    }

    override fun onListHistorySucc(siteOrLineBean: SiteOrLineBean) {
        if (mBinding.recyclerView.visibility == View.VISIBLE) {
            return
        }

        mHistoryDatas.clear()
        siteOrLineBean.lineData.forEach {
            val busSiteOrLineHistoryBean = BusSiteOrLineHistoryBean()
            busSiteOrLineHistoryBean.lineNo = it.lineNo
            busSiteOrLineHistoryBean.lineName = it.lineName
            val lineDirectionBean = SiteOrLineBean.LineDateBean.LineDirectionBean()
            lineDirectionBean.lineId = it.lineId
            lineDirectionBean.directionLineName = it.directionLineName
            busSiteOrLineHistoryBean.lineDirection = lineDirectionBean
            busSiteOrLineHistoryBean.type = 1
            mHistoryDatas.add(busSiteOrLineHistoryBean)

        }

        siteOrLineBean.siteData.forEach {
            val busSiteOrLineHistoryBean = BusSiteOrLineHistoryBean()
            if (it.stopList != null && it.stopList.isNotEmpty()) {
                busSiteOrLineHistoryBean.lat = it.stopList[0].lat
                busSiteOrLineHistoryBean.lon = it.stopList[0].lon
                busSiteOrLineHistoryBean.stationId = it.stopList[0].stopId
            }
            busSiteOrLineHistoryBean.stationName = it.stationName
            busSiteOrLineHistoryBean.distance = it.distance
            busSiteOrLineHistoryBean.stationLineInfo = it.stationLineInfo
            busSiteOrLineHistoryBean.type = 0
            mHistoryDatas.add(busSiteOrLineHistoryBean)

        }
        if (mHistoryDatas.size > 0) {
            mBinding.ivClear.visibility = View.VISIBLE
        } else {
            mBinding.ivClear.visibility = View.GONE
        }
        mBinding.recyclerViewHistory.setNewData(mHistoryDatas,false)
    }

    override fun onBusInquiryAnnexBusSucc(siteOrLineBean: SiteOrLineBean) {
        mBinding.llHistory.visibility = View.GONE
        mBinding.recyclerView.visibility = View.VISIBLE
        mDatas.clear()
        siteOrLineBean.lineData.forEach {
            val busSiteOrLineBean = BusSiteOrLineBean()
            busSiteOrLineBean.lineDateBean = it
            busSiteOrLineBean.type = 1
            mDatas.add(busSiteOrLineBean)

        }

        siteOrLineBean.siteData.forEach {
            if (it.stationLineInfo.size > 0){
                val busSiteOrLineBean = BusSiteOrLineBean()
                busSiteOrLineBean.siteDataBean = it
                busSiteOrLineBean.type = 0
                mDatas.add(busSiteOrLineBean)
            }

        }
        mBinding.recyclerView.setNewData(mDatas,false)
    }

    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?) {
            val busQueryFragment = BusQueryFragment()
            busQueryFragment.arguments = bundle
            fragment.start(busQueryFragment)
        }
    }

    private var mPresenter:BusQueryPresenter?= null
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bus_query
    }
    private var mDatas = ArrayList<BusSiteOrLineBean>()
    private var mHistoryDatas = ArrayList<BusSiteOrLineHistoryBean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SwipeUtil.initHeader(mBinding.header)
        initToolBar("公交查询")
        mPresenter = BusQueryPresenter(this)


        addSearchListener()
        mBinding.ivClear.setOnClickListener {
            val json = JsonObject()
            json.addProperty("LoginGuid",DeviceUtil.getDeviceId(context))
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            mPresenter!!.clearQueryHistory(json)
        }

        mBinding.swipeLayout.setOnRefreshListener {
            val json = JsonObject()
            json.addProperty("Longitude", UserUtil.getUser().longitude)
            json.addProperty("latitude", UserUtil.getUser().latitude)
            json.addProperty("QueryText", mBinding.etSearch.text.toString())
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            (parentFragment as QueryMainFragment).mQueryStr =  mBinding.etSearch.text.toString()
            mPresenter!!.querySiteOrLine(json,mBinding.swipeLayout)
        }

        mBinding.recyclerViewHistory.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerViewHistory.setSupportLoadNextPage(true)
        mBinding.recyclerViewHistory.layoutManager = LinearLayoutManager(context)
        val historyAdapter = BusHistoryAdapter(context!!,mHistoryDatas)
        historyAdapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<BusSiteOrLineHistoryBean>{
            override fun onItemClick(view: View?, data: BusSiteOrLineHistoryBean?, position: Int) {
                if (mHistoryDatas[position].type == 0) {
                    mBinding.llHistory.visibility = View.GONE
                    mBinding.recyclerView.visibility = View.VISIBLE
                    mBinding.etSearch.setText(data!!.stationName)
                    mBinding.swipeLayout.autoRefresh()
                } else {
                    val bundle = Bundle()
                    bundle.putString(
                        BundleKeys.KEY_LINE_ID,
                        data!!.lineDirection.lineId)
                    if (BaseUtil.sTopAct!!.topFragment is QueryMainFragment) {
                        val busMapFragment = BusMapFragment()
                        busMapFragment.arguments = bundle
                        (BaseUtil.sTopAct!!.topFragment as QueryMainFragment).replace(busMapFragment)
                    } else {
                        QueryMainFragment.newInstance(
                            BaseUtil.sTopAct!!.topFragment as SupportFragment,
                            bundle
                        )
                    }
                }
            }

            override fun onItemLongClick(
                view: View?,
                model: BusSiteOrLineHistoryBean?,
                position: Int
            ) {
            }

        })
        mBinding.recyclerViewHistory.adapter = historyAdapter


        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = BusStationLineAdapter(context!!,mDatas,object : InnerClickListener{
            override fun onInnerClickListener(position: Int, outPosition: Int) {
                var lineId: String
                if (mDatas[outPosition].type == 1){
                    val bundle = Bundle()
                    lineId = mDatas[outPosition].lineDateBean.lineDirection[position].lineId
                    bundle.putString(BundleKeys.KEY_LINE_ID,
                        lineId)


//                    val bundle = Bundle()
//                    bundle.putParcelableArrayList(BundleKeys.KEY_STATION_BEAN,mDatas)
//                    bundle.putString(BundleKeys.KEY_STATION_ID,model!!.stopList[0].stopId)
//                    QueryMainFragment.newInstance(this@GoTravelFragment.parentFragment as SupportFragment,bundle)

                    if (parentFragment != null) {
                        val busMapFragment = BusMapFragment()
                        busMapFragment.arguments = bundle
                        (parentFragment as QueryMainFragment).replace(busMapFragment)
                    } else {
                        QueryMainFragment.newInstance(
                            this@BusQueryFragment as SupportFragment,
                            bundle
                        )
                    }
                } else {
                    val bundle = Bundle()
                    lineId =  mDatas[outPosition].siteDataBean.stationLineInfo[position].lineId
                    bundle.putString(BundleKeys.KEY_LINE_ID,
                        mDatas[outPosition].siteDataBean.stationLineInfo[position].lineId)
                    if (parentFragment != null) {
                        val busMapFragment = BusMapFragment()
                        busMapFragment.arguments = bundle
                        (parentFragment as QueryMainFragment).replace(busMapFragment)
                    } else {
                        QueryMainFragment.newInstance(
                            this@BusQueryFragment as SupportFragment,
                            bundle
                        )
                    }
                }

                val json = JsonObject()
                json.addProperty("LoginGuid", DeviceUtil.getDeviceId(context))
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                json.addProperty("HistoryType",2)
                json.addProperty("HistoryId",lineId)
                mPresenter!!.saveQueryHistory(json)
            }

        })
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<BusSiteOrLineBean>{
            override fun onItemLongClick(
                view: View?,
                model: BusSiteOrLineBean?,
                position: Int
            ) {
            }

            override fun onItemClick(
                view: View?,
                model: BusSiteOrLineBean?,
                position: Int
            ) {


                if (model!!.type == 1) {
                    return
                }




                val stationBean = AnnexBusBean.StationListBean()
                stationBean.distance = model!!.siteDataBean.distance


                stationBean.stopList = ArrayList()

                model!!.siteDataBean.stopList?.forEach {
                    val stopListBean = AnnexBusBean.StopListBean()
                    stopListBean.lat = it.lat
                    stopListBean.stopId = it.stopId
                    stopListBean.lon = it.lon
                    stopListBean.distance = it.distance
                    stopListBean.stopName = it.stopName
                    stopListBean.isCollection =it.isCollection
                    stopListBean.collectionId = it.collectionId
                    stationBean.stopList.add(stopListBean)
                }

                stationBean.stationName = model!!.siteDataBean.stationName
                stationBean.stationLineInfo = ArrayList<AnnexBusBean.StationListBean.StationLineInfoBean>()
                model!!.siteDataBean.stationLineInfo.forEach {
                    val bean = AnnexBusBean.StationListBean.StationLineInfoBean()
                    bean.lineName = it.lineName
                    bean.lineNo = it.lineId
                    bean.stopId = it.stopId
                    bean.directionLineName = it.directionLineName
                    bean.lastTime = it.lastTime
                    bean.startTime = it.startTime
                    bean.lineId = it.lineId
//                    it.lineDirection.forEach { it_ ->
//                        bean.directionLineName = it_.directionLineName
//                        bean.lastTime = it_.lastTime
//                        bean.startTime = it_.startTime
//                        bean.lineId = it_.lineId
//                    }
                    stationBean.stationLineInfo.add(bean)
                }

                val bundle = Bundle()
                val arr = ArrayList<AnnexBusBean.StationListBean>()
                arr.add(stationBean)
                bundle.putParcelableArrayList(BundleKeys.KEY_STATION_BEAN,arr)
                bundle.putString(BundleKeys.KEY_STATION_ID,mDatas[position]!!.siteDataBean.stopList[0].stopId)
                if (parentFragment != null) {
                    val busMapFragment = BusMapFragment()
                    busMapFragment.arguments = bundle
                    (parentFragment as QueryMainFragment).replace(busMapFragment)
                } else {
                    QueryMainFragment.newInstance(this@BusQueryFragment as SupportFragment, bundle)
                }

                val json = JsonObject()
                json.addProperty("LoginGuid", DeviceUtil.getDeviceId(context))
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                json.addProperty("HistoryType",1)
                json.addProperty("HistoryId", mDatas[position]!!.siteDataBean.stopList[0].stopId)
                mPresenter!!.saveQueryHistory(json)
            }

        })
        mBinding.recyclerView.adapter = adapter
//        mBinding.recyclerView.setNewData(mDatas)

//        val defaultItemAnimator = DefaultItemAnimator()
//        defaultItemAnimator.addDuration = 1000
//        defaultItemAnimator.removeDuration = 1000
//        mBinding.recyclerView.itemAnimator = defaultItemAnimator

//        mBinding.swipeLayout.autoRefresh()

        if (!TextUtils.isEmpty((parentFragment as QueryMainFragment).mQueryStr)) {
            mBinding.llHistory.visibility = View.GONE
            mBinding.recyclerView.visibility = View.VISIBLE
            mBinding.etSearch.setText((parentFragment as QueryMainFragment).mQueryStr)
            hideKeyboard()
            mBinding.swipeLayout.autoRefresh()
        } else {
            val json = JsonObject()
            json.addProperty("LoginGuid",DeviceUtil.getDeviceId(context))
            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
            mPresenter!!.listQueryHistory(json)
        }



    }

    private fun addSearchListener() {
        mBinding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event != null && event!!.keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideKeyboard()
                    mBinding.swipeLayout.autoRefresh()
                    return true
                }
                return false
            }
        })
        mBinding.etSearch.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable) {
                (parentFragment as QueryMainFragment).mQueryStr = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun doAfterAnim() {


    }

    var hasInit = false
    override fun onSupportVisible() {
        super.onSupportVisible()
        if (hasInit) {
            if (mBinding.recyclerView.visibility == View.VISIBLE) {
                    mBinding.swipeLayout.autoRefresh()
            } else {
                val json = JsonObject()
                json.addProperty("LoginGuid",DeviceUtil.getDeviceId(context))
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                mPresenter!!.listQueryHistory(json)
            }
        }
        hasInit = true
    }


}