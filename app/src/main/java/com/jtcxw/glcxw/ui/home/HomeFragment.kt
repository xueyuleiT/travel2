package com.jtcxw.glcxw.ui.home

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.amap.api.location.AMapLocation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.HomeAdapter
import com.jtcxw.glcxw.adapter.HomeBannerAdapter
import com.jtcxw.glcxw.adapter.HomeHotelBannerAdapter
import com.jtcxw.glcxw.adapter.HomeScenicBannerAdapter
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.*
import com.jtcxw.glcxw.base.utils.*
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.OnLoadNextPageListener
import com.jtcxw.glcxw.base.views.recyclerview.OnRefreshListener
import com.jtcxw.glcxw.databinding.FragmentHomeBinding
import com.jtcxw.glcxw.events.MessageEvent
import com.jtcxw.glcxw.fragment.MainFragment
import com.jtcxw.glcxw.localbean.HomeItem
import com.jtcxw.glcxw.presenters.impl.HomePresenter
import com.jtcxw.glcxw.ui.BusFragment
import com.jtcxw.glcxw.ui.LocationFragment
import com.jtcxw.glcxw.ui.MessageFragment
import com.jtcxw.glcxw.ui.WebFragment
import com.jtcxw.glcxw.ui.customized.CustomizedMainFragment
import com.jtcxw.glcxw.ui.login.LoginFragment
import com.jtcxw.glcxw.utils.*
import com.jtcxw.glcxw.viewmodel.HomeModel
import com.jtcxw.glcxw.views.HomeView
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.toptechs.libaction.action.SingleCall
import com.youth.banner.indicator.CircleIndicator
import me.yokeyword.fragmentation.SupportFragment
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min


class HomeFragment: LocationFragment<FragmentHomeBinding, HomeModel>() ,
    OnLoadNextPageListener,HomeView,
    OnRefreshListener,View.OnClickListener{
    override fun onModuleConfigSucc(moduleConfigBean: ModuleConfigBean) {
        if (moduleConfigBean.funId == "1") {
            mModuleConfigBean = moduleConfigBean
        } else {
            mParkingModuleConfigBean = moduleConfigBean
        }
        val bundle = Bundle()
        bundle.putString(BundleKeys.KEY_WEB_TITLE, moduleConfigBean!!.funName)
        bundle.putString(BundleKeys.KEY_WEB_URL, moduleConfigBean!!.url)
        WebFragment.newInstance(parentFragment as SupportFragment, bundle)
    }

    override fun onMemberInfoSucc(userInfoBean: UserInfoBean) {
        UserUtil.getUser().save(userInfoBean)
        initNotification()
    }

    override fun onMemberInfoFailed(msg: String) {
    }

    override fun onMemberInfoFinish() {
    }

    override fun onGetContentTypeListSucc(dictionaryInfoBean: DictionaryInfoBean) {
        initPager(dictionaryInfoBean)
    }

    override fun onGetContentTypeListFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }

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
        mModuleConfigBean = null
        mBinding.vNotification.setImageResource(R.mipmap.icon_notification)
        mBinding.tvNum.visibility = View.GONE
        mBinding.swipeLayout.autoRefresh()
        (parentFragment as MainFragment).checkHome()
    }

    override fun refresh() {
        mBinding.swipeLayout.autoRefresh()
    }
    override fun onScenicInfoListSucc(scenicBean: ScenicBean) {
        mSpotList.clear()
        mSpotList.addAll(scenicBean.scenicInfoList)
        if (mSelected == 0) {
            mBinding.bannerHot.adapter = HomeScenicBannerAdapter(mSpotList, 0,this)
        }
    }

    override fun onHotelInfoListSucc(hotelBean: HotelBean) {
        mHotelList.clear()
        mHotelList.addAll(hotelBean.hotelInfoList)
        if (mSelected == 1) {
            mBinding.bannerHot.adapter = HomeHotelBannerAdapter(mHotelList, 0)
        }
    }

    override fun onHotelInfoListFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }


    override fun onScenicInfoListFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }


    override fun onLocationChange(aMapLocation: AMapLocation) {
//        mBinding.tvCity.text = aMapLocation.city.substring(0,aMapLocation.city.length - 1)
        mBinding.tvCity.text = UserUtil.getUser().city
        mFinishCount += 4
        refreshData()
    }

    override fun onRefresh() {
    }

    override fun onLoadNextPage() {

    }

    override fun getVariableId(): Int {
        return BR.home
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun doAfterAnim() {

    }

    private var mSelected = 0
    private var mFinishCount = 0
    private var mSpotList = ArrayList<ScenicBean.ScenicInfoBean>()
    private var mHotelList = ArrayList<HotelBean.HotelInfoListBean>()

    private var mAdapter : HomeAdapter?= null
    private var mData = ArrayList<HomeItem>()
    private var mPresenter:HomePresenter?= null
    val mBannerList = ArrayList<BannerBean.BannerListBean>()
    var mModuleConfigBean:ModuleConfigBean?= null
    var mParkingModuleConfigBean:ModuleConfigBean?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BaseUtil.isDarkMode()) {
            mBinding.rlBg.setBackgroundResource(0)
        }

        SwipeUtil.initHeader(mBinding.header)
        mPresenter = HomePresenter(this)

        var item = HomeItem(R.mipmap.icon_public_car, "公交查询")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_special_car, "定制公交")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_customized_school, "校园班车")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_hotel, "酒店")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_scenic_spot, "景点门票")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_car_change, "换乘查询")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_car_ai_parking, "智慧停车")
        mData.add(item)
        item = HomeItem(R.mipmap.icon_more, "更多")
        mData.add(item)

        mBinding.recyclerView.layoutManager = GridLayoutManager(context,4)
        mAdapter = HomeAdapter(context!!, mData)
        mAdapter!!.setOnItemClickListener(object :
            BaseRecyclerAdapter.OnItemClickListener<HomeItem> {
            override fun onItemClick(view: View?, model: HomeItem?, position: Int) {
                if (position == 0) {
                    BusFragment.newInstance(this@HomeFragment.parentFragment as SupportFragment,null)
                } else if (position == 1) {
                    if (TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
                        LoginFragment.newInstance(parentFragment as SupportFragment,null)
                        val jumpValid = JumpValid(CustomizedMainFragment())
                        SingleCall.getInstance().clear()
                        SingleCall.getInstance().addAction(jumpValid).addValid(jumpValid).doCall()
                        return
                    }
                    CustomizedMainFragment.newInstance(this@HomeFragment.parentFragment as SupportFragment,null)
                } else if (position == 2) {
                    if (TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
                        LoginFragment.newInstance(parentFragment as SupportFragment,null)
                        val bundle = Bundle()
                        bundle.putInt(BundleKeys.KEY_INDEX,1)
                        val fragment = CustomizedMainFragment()
                        fragment.arguments = bundle
                        val jumpValid = JumpValid(fragment)
                        SingleCall.getInstance().clear()
                        SingleCall.getInstance().addAction(jumpValid).addValid(jumpValid).doCall()
                        return
                    }
                    val bundle = Bundle()
                    bundle.putInt(BundleKeys.KEY_INDEX,1)
                    CustomizedMainFragment.newInstance(this@HomeFragment.parentFragment as SupportFragment,bundle)
                } else if (position == 3) {
                    HotelFragment.newInstance(this@HomeFragment.parentFragment as SupportFragment,null)
                } else if (position == 4) {
                    ScenicFragment.newInstance(this@HomeFragment.parentFragment as SupportFragment,null)
                } else if (position == 5) {
//                    if (TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
//                        LoginFragment.newInstance(parentFragment as SupportFragment,null)
//                        val clickValid = ClickValid(view!!)
//                        SingleCall.getInstance().clear()
//                        SingleCall.getInstance().addAction(clickValid).addValid(clickValid).doCall()
//                        return
//                    }
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
                } else if (position == 6) {
                    if (mParkingModuleConfigBean == null) {
                        val json = JsonObject()
                        json.addProperty("Longitude", UserUtil.getUser().longitude)
                        json.addProperty("Latitude", UserUtil.getUser().latitude)
                        json.addProperty("FunId", "2")
                        json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
                        mPresenter!!.h5ModuleConfig(json)
                    } else {
                        val bundle = Bundle()
                        bundle.putString(BundleKeys.KEY_WEB_TITLE,mParkingModuleConfigBean!!.funName)
                        bundle.putString(BundleKeys.KEY_WEB_URL,mParkingModuleConfigBean!!.url)
                        WebFragment.newInstance(parentFragment as SupportFragment,bundle)
                    }
                } else if (position == 7) {
                    ToastUtil.toastWaring("功能待开放")
                }
            }

            override fun onItemLongClick(view: View?, model: HomeItem?, position: Int) {
            }

        })
        mBinding.recyclerView.adapter = mAdapter


        mBinding.banner.adapter = HomeBannerAdapter(mBannerList, 10)
        mBinding.banner.addBannerLifecycleObserver(this)
        mBinding.banner.indicator = CircleIndicator(context)

        mBinding.llTop.setPadding(mBinding.llTop.paddingLeft,
            (DimensionUtil.dpToPx(50) + DimensionUtil.getStatusBarHeight(context!!)).toInt(),mBinding.llTop.paddingRight,mBinding.llTop.paddingBottom)

        mBinding.llToolbar.layoutParams.height = (DimensionUtil.getStatusBarHeight(context!!) + DimensionUtil.dpToPx(50)).toInt()
            mBinding.llToolbar.setPadding(mBinding.llToolbar.paddingLeft,
            DimensionUtil.getStatusBarHeight(context!!),mBinding.llToolbar.paddingRight,mBinding.llToolbar.paddingBottom)
        mBinding.llToolbar.requestLayout()

        mBinding.tvTitle.layoutParams.height = mBinding.llToolbar.layoutParams.height
        mBinding.tvTitle.setPadding(mBinding.tvTitle.paddingLeft,
            DimensionUtil.getStatusBarHeight(context!!),mBinding.tvTitle.paddingRight,mBinding.tvTitle.paddingBottom)
        mBinding.tvTitle.requestLayout()

        (mBinding.ivTop.layoutParams as ViewGroup.MarginLayoutParams).topMargin = (mBinding.llToolbar.layoutParams.height + DimensionUtil.dpToPx(20)).toInt()
        mBinding.ivTop.requestLayout()
//        val w = DimensionUtil.dpToPx(10).toInt()
//        mBinding.etSearch.compoundDrawables[0].bounds = Rect((mBinding.etSearch.compoundDrawables[0].intrinsicWidth - w) / 2, (mBinding.etSearch.compoundDrawables[0].intrinsicHeight - w) / 2 ,mBinding.etSearch.compoundDrawables[0].intrinsicWidth - (mBinding.etSearch.compoundDrawables[0].intrinsicWidth - w) / 2, w + (mBinding.etSearch.compoundDrawables[0].intrinsicWidth - w) / 2)

        mBinding.rgHot.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                R.id.rb_spot -> {
                    mSelected = 0
                    mBinding.bannerHot.adapter =
                        HomeScenicBannerAdapter(mSpotList, 0, this)
                }

                R.id.rb_hotel -> {
                    mSelected = 1
                    mBinding.bannerHot.adapter =
                        HomeHotelBannerAdapter(mHotelList, 0)
                }
            }
        }

        mBinding.bannerHot.adapter = HomeHotelBannerAdapter(mHotelList, 0)
        mBinding.bannerHot.addBannerLifecycleObserver(this)
        mBinding.bannerHot.indicator = CircleIndicator(context)
        mBinding.bannerHot.setBannerGalleryEffect(0, DimensionUtil.dpToPx(10).toInt(),
            DimensionUtil.dpToPx(10).toInt(),1f)


        mBinding.ivTop.setOnClickListener(this)
        mBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            mVerticalOffY = verticalOffset
            var alpha = max(0,((255 * min((-verticalOffset/mverticalOffsetMax), 1.0f)).toInt()))
            if (alpha < 16) {
                mBinding.llToolbar.setBackgroundColor(Color.parseColor("#0" + Integer.toHexString(alpha).toUpperCase() + if(BaseUtil.isDarkMode()) "0a191d" else "63D0D0"))
            }else {
                mBinding.llToolbar.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(alpha).toUpperCase() + if(BaseUtil.isDarkMode()) "0a191d" else "63D0D0"))
            }

            if (alpha > 100) {
                mBinding.ivTop.visibility = View.VISIBLE
                mBinding.ivTop.alpha = alpha / 255.0f
                mBinding.ivTop.scaleX = mBinding.ivTop.alpha
                mBinding.ivTop.scaleY = mBinding.ivTop.alpha
            }else {
                mBinding.ivTop.visibility = View.GONE
            }
        })

        mBinding.swipeLayout.setOnRefreshListener {
            if (TextUtils.isEmpty(UserUtil.getUser().latitude)) {
                (parentFragment as MainFragment).checkLocation()
                it.finishRefresh(0)
                return@setOnRefreshListener
            }

            mFinishCount = 4
            refreshData()

        }

        mBinding.swipeLayout.autoRefresh()

        addSearchListener()

        mBinding.ivLocation.setOnClickListener(this)
        mBinding.vNotification.setOnClickListener(this)

        initNotification()

        RxBus.getDefault().toObservable(MessageEvent::class.java)
            .subscribe {
                initNotification()
            }

    }

    private fun initNotification() {
        if (!TextUtils.isEmpty(UserUtil.getUserInfoBean().token) && !TextUtils.isEmpty(UserUtil.getUserInfoBean().realTelphoneNo)) {
            var list = DaoUtilsStore.getInstance().userDaoUtils.queryAll()
            var count = 0
            list.forEach {
                if (it.phone == UserUtil.getUserInfoBean().realTelphoneNo) {
                    if (it.read == 0) {
                        count ++
                    }
                }
            }
            if (count > 0) {
                mBinding.tvNum.visibility = View.VISIBLE
                mBinding.tvNum.text = if (count < 100) count.toString() else "99+"
            } else {
                mBinding.tvNum.visibility = View.GONE
            }
        }else {
            mBinding.tvNum.visibility = View.GONE
        }
    }

    private fun refreshData() {

        var json = JsonObject()
        json.addProperty("Longitude",UserUtil.getUser().longitude)
        json.addProperty("Latitude",UserUtil.getUser().latitude)
        json.addProperty("IsSearchHot","1")
        json.addProperty("KeyWord","")
        mPresenter!!.getHotelInfoList(json,mBinding.swipeLayout)

        json = JsonObject()
        json.addProperty("Longitude",UserUtil.getUser().longitude)
        json.addProperty("Latitude",UserUtil.getUser().latitude)
        json.addProperty("IsSearchHot","1")
        json.addProperty("KeyWord","")
        mPresenter!!.getScenicInfoList(json,mBinding.swipeLayout)


        json = JsonObject()
        json.addProperty("Type","1")
        mPresenter!!.getBanner(json,mBinding.swipeLayout)

        json = JsonObject()
        json.addProperty("Type","1")
        mPresenter!!.getContentTypeList(json,mBinding.swipeLayout)

        json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
        mPresenter!!.getMemberInfo(json)
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

    var mOnTabSelectedListener: TabLayout.OnTabSelectedListener?= null
    private fun initPager(dictionaryInfoBean: DictionaryInfoBean) {

        mTitle.clear()
        mFragment.clear()
        mBinding.tabLayout.removeAllTabs()
        dictionaryInfoBean.dictionaryInfo.forEach {
            mTitle.add(it.itemName)
            mFragment.add(HomeSpotFragment(it.itemValue.toInt()))
        }

        mBinding.tabLayout.setupWithViewPager(mBinding.vPager)
        if (mOnTabSelectedListener != null) {
            mBinding.tabLayout.removeOnTabSelectedListener(mOnTabSelectedListener!!)
        }
        mOnTabSelectedListener = object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab!!.customView = null
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                mBinding.vPager.setCurrentItem(tab!!.position,true)
                val view = LayoutInflater.from(context).inflate(R.layout.item_tab,null)
                val tabTextView = view.findViewById<TextView>(R.id.tv_top_item)
                val selectedSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20f, resources.displayMetrics)
                tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,selectedSize)
                tabTextView.setTextColor(resources.getColor(R.color.black_263238))
                tabTextView.text = tab!!.text
                tab.customView = view
            }

        }

        mBinding.tabLayout.addOnTabSelectedListener(mOnTabSelectedListener!!)

        mBinding.vPager.adapter = object : FragmentPagerAdapter(childFragmentManager!!) {
            override fun getItem(position: Int): Fragment {
                return mFragment[position]
            }

            override fun getCount(): Int {
                return mFragment.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitle[position]
            }


        }

        mBinding.vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
//                if(state == 1) {//滑动过程中关闭下拉刷新
//                    mBinding.swipeLayout.isEnabled = false;//设置不可触发
//                }else if(2 == state){//停止滑动时触发，可以刷新
//                    mBinding.swipeLayout.isEnabled = true;//设置可触发
//                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mBinding.tabLayout.getTabAt(position)!!.select()
            }

        })
        mBinding.vPager.offscreenPageLimit = 2

        mBinding.vPager.currentItem = mTitle.size / 2
        mBinding.tabLayout.getTabAt(mTitle.size / 2)!!.select()
        TabLayoutUtil.setTabWidth(mBinding.tabLayout,DimensionUtil.dpToPx(20).toInt())
    }


    private var mVerticalOffY = 0
    private val mverticalOffsetMax = DimensionUtil.dpToPx(200)

    override fun onPause() {
        super.onPause()
        mBinding.banner.stop()
    }

    override fun onResume() {
        super.onResume()
        mBinding.banner.start()
    }

    override fun needSetStatusHeight(): Boolean {
        return false
    }

    var valueAnimator : ValueAnimator ?= null

    var mTitle = ArrayList<String>(2)
    var mFragment = ArrayList<Fragment>(2)

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.v_notification -> {
//                val s:StringBuilder?= null
//                s!!.toString()
                if (!TextUtils.isEmpty(UserUtil.getUserInfoBean().token)) {
                    MessageFragment.newInstance(parentFragment as SupportFragment, null)
                } else {
                    LoginFragment.newInstance(parentFragment as SupportFragment, null)
                }
            }
            R.id.iv_location -> {

            }
            R.id.iv_top -> {
                if (mVerticalOffY == 0) {
                    return
                }
                if (valueAnimator != null && valueAnimator!!.isRunning) {
                    return
                }
                val behavior = (mBinding.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams).behavior as NoBounsBehavior
                valueAnimator = ValueAnimator.ofInt(mVerticalOffY,0)
                valueAnimator!!.addUpdateListener {
                    val value = it.animatedValue as Int
                    behavior.topAndBottomOffset = value
                    mVerticalOffY = value
                    var alpha = ((255 * min((-value/mverticalOffsetMax), 1.0f)).toInt())
                    if (alpha < 16) {
                        mBinding.llToolbar.setBackgroundColor(Color.parseColor("#0" + Integer.toHexString(alpha).toUpperCase() + if(BaseUtil.isDarkMode()) "0a191d" else "63D0D0"))
                    }else {
                        mBinding.llToolbar.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(alpha).toUpperCase() + if(BaseUtil.isDarkMode()) "0a191d" else "63D0D0"))
                    }
                    mBinding.toolBar.requestLayout()
                }
                valueAnimator!!.addListener(object : Animator.AnimatorListener{
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        behavior.stopAppbarLayoutFling(mBinding.appBarLayout)
                        mBinding.llToolbar.setBackgroundColor(Color.parseColor(if(BaseUtil.isDarkMode()) "#000a191d" else "#0063D0D0"))

                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }

                })
                valueAnimator!!.interpolator = DecelerateInterpolator()
                valueAnimator!!.duration = min(-mVerticalOffY, 300).toLong()
                valueAnimator!!.start()

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
            valueAnimator!!.removeAllListeners()
            valueAnimator!!.removeAllUpdateListeners()
        }
    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        initNotification()
    }
}