package com.jtcxw.glcxw.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.CustomizedBusAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.CityBean
import com.jtcxw.glcxw.base.respmodels.LineBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.databinding.FragmentCustomizedBinding
import com.jtcxw.glcxw.listeners.CityCallback
import com.jtcxw.glcxw.localbean.ProvinceBean
import com.jtcxw.glcxw.presenters.impl.BusCustomizedPresenter
import com.jtcxw.glcxw.utils.DialogSelectorUtil
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.BusCustomizedView
import me.yokeyword.fragmentation.SupportFragment

class CustomizedBusFragment:BaseFragment<FragmentCustomizedBinding,CommonModel>() ,
    BusCustomizedView {

    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?) {
            val customizedBusFragment = CustomizedBusFragment()
            customizedBusFragment.arguments = bundle
            fragment.start(customizedBusFragment)
        }
    }

    var mCity = ""
    var mPresenter:BusCustomizedPresenter?= null
    var mDatas = ArrayList<LineBean.RouteListBean>()
    var mCitys = ArrayList<CityBean.ProvinceListBean>()
    var mCityId = ""
    override fun onGetCitysSucc(cityBean: CityBean) {
        mCitys.clear()
        if (cityBean.province_list != null) {
            mCitys.addAll(cityBean.province_list)
        }
        mCitys.forEach {
            it.city_list.forEach{it_ ->
                if (it_.is_default == "Y") {
                    mCityId = it_.city_id.toString()
                    setTitle(it_.city_name)
                    mCity = it_.city_name
                }

            }
        }
        if (!TextUtils.isEmpty(mCityId)) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("MemberId", UserUtil.getUser().userInfoBean.memberId)
            jsonObject.addProperty("TripType", 4)
            jsonObject.addProperty("CityId", mCityId)
            mPresenter!!.getLines(jsonObject, mBinding.swipeLayout)
        } else {
            mBinding.swipeLayout.finishRefresh(0)
            mBinding.recyclerView.setNewData(mDatas,false)

        }
    }

    override fun onGetCitysFinish() {
    }

    override fun onGetLinesSucc(lineBean: LineBean) {
        mDatas.clear()
        mDatas.addAll(lineBean.route_list)
        mBinding.recyclerView.setNewData(mDatas,false)
    }

    override fun onGetLinesFinish() {
        mBinding.swipeLayout.finishRefresh(0)
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_customized
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(UserUtil.getUser().city)
        mCity = UserUtil.getUser().city

        SwipeUtil.initHeader(mBinding.header)

        mPresenter = BusCustomizedPresenter(this)

        mBinding.swipeLayout.setOnRefreshListener {
            if (mCitys.isEmpty()) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
                jsonObject.addProperty("TripType",4)
                mPresenter!!.getCitys(jsonObject,mBinding.swipeLayout)
                return@setOnRefreshListener
            }
            val jsonObject = JsonObject()
            jsonObject.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
            jsonObject.addProperty("TripType",4)
            jsonObject.addProperty("CityId",mCityId)
            mPresenter!!.getLines(jsonObject,mBinding.swipeLayout)
        }

        mRootView!!.findViewById<TextView>(R.id.tv_center_title).setOnClickListener {
            if (mCitys.isEmpty()) {
                return@setOnClickListener
            }
            var provinceBeanList = ArrayList<ProvinceBean>()
            var cityList = ArrayList<ArrayList<String>>()
            var cityListId = ArrayList<ArrayList<Int>>()
            mCitys.forEach {
                val province_id = it.province_id
                val province_name = it.province_name
                provinceBeanList.add(ProvinceBean(province_name, province_id))

                var cities = java.util.ArrayList<String>()
                var citiesId = java.util.ArrayList<Int>()

                it.city_list.forEach {
                    val city_id = it.city_id
                    val city_name = it.city_name
                    //将城市对象存入集合
                    cities.add(city_name)
                    citiesId.add(city_id)
                }
                //  将存放城市的集合放入集合
                cityList.add(cities)
                cityListId.add(citiesId)
            }
            DialogSelectorUtil.showCityPickView(context!!,provinceBeanList,cityList,cityListId,object :CityCallback{
                override fun onCityCallback(provinceCode: Int, cityCode: Int, city: String,address: String) {
                    mCityId = cityCode.toString()
                    setTitle(city)
                    mCity = city
                    mBinding.swipeLayout.autoRefresh()
                }

            })
        }

        mBinding.recyclerView.setShouldRefreshPartWhenLoadMore(true)
        mBinding.recyclerView.setSupportScrollToTop(false)
        mBinding.recyclerView.setSupportLoadNextPage(true)
        val adapter = CustomizedBusAdapter(context!!,mDatas)
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<LineBean.RouteListBean>{
            override fun onItemClick(view: View?, model: LineBean.RouteListBean?, position: Int) {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_ROUTE_ID,model!!.route_id.toString())
                bundle.putString(BundleKeys.KEY_ROUTE_NAME,model!!.end_station_name)
                bundle.putString(BundleKeys.KEY_TITLE,model!!.route_name)
                bundle.putString(BundleKeys.KEY_ROUTE_TYPE,model!!.route_type.toString())
                bundle.putInt(BundleKeys.KEY_TRIP_TYPE,4)
                if (model!!.route_type == 1) {
                    bundle.putString(BundleKeys.KEY_BACK_ROUTE_ID, model!!.back_route_id.toString())
                    bundle.putString(BundleKeys.KEY_ROUTE_BACK_NAME, model!!.start_station_name)
                }
                BusLineGoOrderFragment.newInstance(this@CustomizedBusFragment.parentFragment as SupportFragment,bundle)
            }

            override fun onItemLongClick(
                view: View?,
                model: LineBean.RouteListBean?,
                position: Int
            ) {
            }

        })
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.setNewData(mDatas)
        mBinding.swipeLayout.autoRefresh()

    }

    override fun doAfterAnim() {

    }

    override fun statusBarColor(): Int {
        return R.color.back_white
    }
    var hasInit = false

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (hasInit || ismBindingInitialized()) {
            initToolBar(mCity)
        }
        hasInit = true
    }


}