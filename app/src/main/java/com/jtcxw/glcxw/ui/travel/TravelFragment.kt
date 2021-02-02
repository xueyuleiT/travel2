package com.jtcxw.glcxw.ui.travel

import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.amap.api.location.AMapLocation
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.HomeBannerAdapter
import com.jtcxw.glcxw.base.respmodels.BannerBean
import com.jtcxw.glcxw.base.respmodels.HotelBean
import com.jtcxw.glcxw.base.respmodels.ScenicBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.utils.ViewPagerScroller
import com.jtcxw.glcxw.databinding.FragmentTravelBinding
import com.jtcxw.glcxw.fragment.MainFragment
import com.jtcxw.glcxw.localmodel.TabEntity
import com.jtcxw.glcxw.presenters.impl.TravelPresenter
import com.jtcxw.glcxw.ui.LocationFragment
import com.jtcxw.glcxw.utils.SwipeUtil
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.TravelView
import com.youth.banner.indicator.CircleIndicator

class TravelFragment: LocationFragment<FragmentTravelBinding, CommonModel>(),TravelView {
    override fun onGetBannerSucc(bannerBean: BannerBean) {
        mBannerList.clear()
        mBannerList.addAll(bannerBean.bannerList)
        mBinding.banner.adapter.notifyDataSetChanged()
    }

    override fun onGetBannerFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }

    override fun onLogout() {

    }

    override fun refresh() {
        mBinding.swipeLayout.autoRefresh()
    }
    override fun onHotelInfoListSucc(hotelBean: HotelBean) =
        (mFragment[1] as HotHotelFragment).onDataChange(hotelBean)

    override fun onHotelInfoListFailed() {
        (mFragment[1] as HotHotelFragment).onDataChange(null)
    }

    override fun onHotelInfoListFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }

    override fun onScenicInfoListSucc(scenicBean: ScenicBean) {
        (mFragment[0] as HotSpotFragment).onDataChange(scenicBean)
    }

    override fun onScenicInfoListFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }

    override fun onScenicInfoListFailed() {
        (mFragment[0] as HotSpotFragment).onDataChange(null)
    }

    override fun onLocationChange(aMapLocation: AMapLocation) {
//        mTitle!!.text = aMapLocation.city.substring(0,aMapLocation.city.length - 1)+ "出游"
        mTitle!!.text = UserUtil.getUser().city+ "出游"
        mBinding.swipeLayout.autoRefresh()
        mFinishCount += 3

        refreshData()

    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_travel
    }

    val mBannerList = ArrayList<BannerBean.BannerListBean>()
    var mTitle:TextView ?= null
    var mFragment = ArrayList<Fragment>(2)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mIconSelectIds = intArrayOf(
        R.mipmap.pic_hot_spot_checked,
        R.mipmap.pic_hot_hotel_checked
    )

    private val mIconUnselectIds = intArrayOf(
        R.mipmap.pic_hot_spot_nor,
        R.mipmap.pic_hot_hotel_nor
    )

    private var mPresenter:TravelPresenter? = null
    private var mFinishCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BaseUtil.isDarkMode()) {
            mBinding.rlBg.setBackgroundResource(0)
        }

        SwipeUtil.initHeader(mBinding.header)
        mPresenter = TravelPresenter(this)

        mTitle = mBinding.root.findViewById<TextView>(R.id.tv_center_title)
        mTitle!!.text = "出游"
        mTitle!!.visibility = View.VISIBLE
        mTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        mTitle!!.setTextColor(resources.getColor(backColor()))



        mBinding.banner.adapter = HomeBannerAdapter(mBannerList, 10)
        mBinding.banner.addBannerLifecycleObserver(this)
        mBinding.banner.indicator = CircleIndicator(context)

        mBinding.swipeLayout.setOnRefreshListener {

            if (TextUtils.isEmpty(UserUtil.getUser().latitude)) {
                (parentFragment as MainFragment).checkLocation()
                it.finishRefresh(0)
                return@setOnRefreshListener
            }
            mFinishCount = 3

           refreshData()

        }

        mBinding.swipeLayout.autoRefresh()

        initViewpager()
        addSearchListener()
    }

   fun refreshData() {
       var json = JsonObject()
       json.addProperty("IsSearchHot","1")
       json.addProperty("Longitude",UserUtil.getUser().longitude)
       json.addProperty("Latitude",UserUtil.getUser().latitude)
       json.addProperty("KeyWord",mBinding.etSearch.text.toString())
       mPresenter!!.getHotelInfoList(json,mBinding.swipeLayout)

       json = JsonObject()
       json.addProperty("Longitude",UserUtil.getUser().longitude)
       json.addProperty("Latitude",UserUtil.getUser().latitude)
       json.addProperty("IsSearchHot","1")
       json.addProperty("KeyWord",mBinding.etSearch.text.toString())
       mPresenter!!.getScenicInfoList(json,mBinding.swipeLayout)

       json = JsonObject()
       json.addProperty("Type","4")
       mPresenter!!.getBanner(json,mBinding.swipeLayout)
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
    }

    fun checkHotel() {
        mBinding.tabLayout.currentTab = 1
        mBinding.vPager.currentItem = 1
    }

    fun checkScenic() {
        mBinding.tabLayout.currentTab = 0
        mBinding.vPager.currentItem = 0
    }

    private fun initViewpager() {
        mFragment.add(HotSpotFragment())
        mFragment.add(HotHotelFragment())

        mBinding.vPager.adapter = object : FragmentPagerAdapter(childFragmentManager!!) {
            override fun getItem(position: Int): Fragment {
                return mFragment[position]
            }

            override fun getCount(): Int {
                mBinding.vPager.background = null
                return mFragment.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return ""
            }
        }

        val scroller = ViewPagerScroller(context)
        scroller.setScrollDuration(500)//时间越长，速度越慢。
        scroller.initViewPagerScroll(mBinding.vPager)

        for (i in mIconSelectIds.indices) {
            mTabEntities.add(TabEntity("", mIconSelectIds[i], mIconUnselectIds[i]))
        }
        mBinding.tabLayout.setTabData(mTabEntities)


        mBinding.vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

                mBinding.tabLayout.currentTab = position
            }

        })

        mBinding.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {

                mBinding.vPager.setCurrentItem(position,true)
            }

            override fun onTabReselect(position: Int) {
            }

        })
    }

    override fun doAfterAnim() {
    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }
}