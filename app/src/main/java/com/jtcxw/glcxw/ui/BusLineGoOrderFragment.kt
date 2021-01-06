package com.jtcxw.glcxw.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.beiing.weekcalendar.listener.GetViewHelper
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.dialogs.LoadingDialog
import com.jtcxw.glcxw.base.respmodels.AddOrderBean
import com.jtcxw.glcxw.base.respmodels.FrequencyBean
import com.jtcxw.glcxw.base.utils.DialogUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.utils.ViewPagerScroller
import com.jtcxw.glcxw.databinding.FragmentBuslineGoorderBinding
import com.jtcxw.glcxw.listeners.OrderDateListener
import com.jtcxw.glcxw.presenters.impl.BusLineGoOrderPresenter
import com.jtcxw.glcxw.ui.customized.OrderConfirmFragment
import com.jtcxw.glcxw.ui.customized.OrderGoFragment
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.BusLineGoOrderView
import com.prolificinteractive.materialcalendarview.CalendarDay
import me.yokeyword.fragmentation.SupportFragment
import org.joda.time.DateTime
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.min


class BusLineGoOrderFragment:BaseFragment<FragmentBuslineGoorderBinding,CommonModel>(),BusLineGoOrderView {
    override fun onAddOrderSucc(addOrderBean: AddOrderBean) {
        ToastUtil.toastSuccess("下单成功")
        val bundle = Bundle()
        bundle.putString(BundleKeys.KEY_ORDER_ID,addOrderBean.order_id)
        OrderConfirmFragment.newInstance(this,bundle)

    }

    override fun onFrequencyFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mDialog?.dismiss()
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    companion object {
        fun newInstance(fragment: SupportFragment,bundle: Bundle?) {
            val busLineGoOrderFragment = BusLineGoOrderFragment()
            busLineGoOrderFragment.arguments = bundle
            fragment.start(busLineGoOrderFragment)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_busline_goorder
    }

    var mTripType = 4
    var mPresenter: BusLineGoOrderPresenter? =null
    var mFrequencyArr = ArrayList<FrequencyBean>(2)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar(arguments!!.getString(BundleKeys.KEY_TITLE,""))
        mTripType = arguments!!.getInt(BundleKeys.KEY_TRIP_TYPE,4)
        mBinding.tvCommit.setOnClickListener(this)

        if (arguments!!.getString(BundleKeys.KEY_ROUTE_TYPE) == "0"){
            mFrequencyArr.add(FrequencyBean())
        } else {
            mFrequencyArr.add(FrequencyBean())
            mFrequencyArr.add(FrequencyBean())
        }

        mPresenter = BusLineGoOrderPresenter(this)
        initViewPager()
        initCalendar()
        getRouteInfo(SimpleDateFormat("yyyy-MM-dd").format(Date()), SimpleDateFormat("yyyy-MM-dd").format(Date()))

    }

    var mFinishCount = 0
    var mDialog:LoadingDialog?= null
    var hasInit = false

    private fun getRouteInfo(rideDate: String,backRideDate: String) {
        mDialog = DialogUtil.getLoadingDialog(fragmentManager)
        if (arguments!!.getString(BundleKeys.KEY_ROUTE_TYPE) == "0") {
            mFinishCount = 1
            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
            json.addProperty("TripType",mTripType)
            json.addProperty("RouteId",arguments!!.getString(BundleKeys.KEY_ROUTE_ID))
            json.addProperty("RideDate",rideDate)
            mPresenter!!.getFrequencys(json,mDialog!!)
        } else {
            mFinishCount = 2
            var json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
            json.addProperty("TripType",mTripType)
            json.addProperty("RouteId",arguments!!.getString(BundleKeys.KEY_ROUTE_ID))
            json.addProperty("RideDate",rideDate)
            mPresenter!!.getFrequencys(json,mDialog!!)

            json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
            json.addProperty("TripType",mTripType)
            json.addProperty("RouteId",arguments!!.getString(BundleKeys.KEY_BACK_ROUTE_ID))
            json.addProperty("RideDate",backRideDate)
            mPresenter!!.getFrequencys(json,mDialog!!)
        }

    }

    val mTitles =  ArrayList<String>(2)
    val mFragments = ArrayList<Fragment>(2)
    @SuppressLint("ClickableViewAccessibility")
    private fun initViewPager() {

        if (arguments!!.getString(BundleKeys.KEY_ROUTE_TYPE) == "0") {
            mFragments.add(OrderGoFragment())
            mTitles.add("去往")
            mBinding.rlTab.visibility = View.GONE
        } else {
            mTitles.add("去往")
            mTitles.add("返回")
            mFragments.add(OrderGoFragment())
            mFragments.add(OrderGoFragment())
        }


        mBinding.vPager.adapter = object : FragmentPagerAdapter(childFragmentManager){
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }
        }

        val scroller = ViewPagerScroller(activity)
        scroller.setScrollDuration(200)//时间越长，速度越慢。
        scroller.initViewPagerScroll(mBinding.vPager)

        mBinding.tabLayout.setupWithViewPager(mBinding.vPager)
        for (i in 0 until mBinding.tabLayout.tabCount) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_tab_order, null)
            if (i == 0) {
                view.setBackgroundResource(R.drawable.shape_r10_cw_top)
            } else {
                view.setBackgroundResource(R.drawable.shape_r10_c_e7ebee)
            }

            val tvTitle = view.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = mTitles[i]
            mBinding.tabLayout.getTabAt(i)!!.customView = view
        }

        mBinding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab!!.customView!!.setBackgroundResource(R.drawable.shape_r10_c_e7ebee)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab!!.customView!!.setBackgroundResource(R.drawable.shape_r10_cw_top)

            }
        })
        mBinding.vPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            val runnable = Runnable {
                val date = (mFragments[mBinding.vPager.currentItem] as OrderDateListener).getDate()
                val arr = SimpleDateFormat("yyyy-MM-dd").format(date).split("-")
                val selected = CalendarDay.from(arr[0].toInt(),arr[1].toInt(),arr[2].toInt())
                mBinding.calendar.selectedDate = selected
                mBinding.calendar.currentDate = selected
                mBinding.weekCalendar.selectDateTime = DateTime(date.time)
            }
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mBinding.weekCalendar.removeCallbacks(runnable)
                mBinding.weekCalendar.postDelayed(runnable,200)

            }

        })

        mBinding.tabLayout.getTabAt(0)!!.select()

        mBinding.vDrag.setOnTouchListener { view, motionEvent ->
            if (mBinding.vDrag.alpha == 0f) {
                return@setOnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain()
                }
                mDownY = motionEvent.y
                mVelocityTracker!!.clear()
                mVelocityTracker!!.addMovement(motionEvent)
            } else if (motionEvent.action == MotionEvent.ACTION_MOVE) {

                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain()
                    mVelocityTracker!!.clear()
                }
                mVelocityTracker!!.addMovement(motionEvent)

                val layoutParams = (mBinding.vDrag.layoutParams as ViewGroup.MarginLayoutParams)
                layoutParams.topMargin = min((layoutParams.topMargin + (motionEvent.y - mDownY)).toInt(), mBinding.calendar.height)

                if (layoutParams.topMargin < 0) {
                    layoutParams.topMargin = 0
                }
                mBinding.vDrag.requestLayout()
                mBinding.calendar.alpha = layoutParams.topMargin.toFloat() / mBinding.calendar.height
                mBinding.ivArrow.rotation = 180 * mBinding.calendar.alpha
                mBinding.vDrag.alpha = mBinding.calendar.alpha
            } else if (motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_CANCEL) {
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain()
                    mVelocityTracker!!.clear()
                }

                mVelocityTracker!!.addMovement(motionEvent)

                mVelocityTracker!!.computeCurrentVelocity(1000)
                if (mVelocityTracker!!.yVelocity > 0) {
                    animOut((mBinding.vDrag.alpha * mBinding.calendar.height).toInt(),mBinding.calendar.height)
                } else {
                    animIn((mBinding.vDrag.alpha * mBinding.calendar.height).toInt(),0)
                }
                mVelocityTracker!!.clear()
            }

            true
        }

    }
    private var mVelocityTracker: VelocityTracker? = null
    var mDownY = 0f

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.tv_commit -> {
                val fragment = (mFragments[mBinding.vPager.currentItem] as OrderDateListener)

                if (mFrequencyArr[mBinding.vPager.currentItem]!!.schedule_list == null || mFrequencyArr[mBinding.vPager.currentItem]!!.schedule_list.isEmpty()) {
                    ToastUtil.toastWaring("无班次可选择")
                    return
                }

               if (!fragment.checkTicketCount()) {
                   return
               }
                if (!fragment.checkOrderInfo()) {
                    ToastUtil.toastWaring("请将信息填写完整")
                    return
                }



                val json = JsonObject()

                val passengerIds = JsonArray()
                passengerIds.add("")

                val tikmodelDetailIds = JsonArray()
                fragment.getFrequencyBean().schedule_list.forEach {
                    if (it.ticketBean != null && it.isSelected) {
                        tikmodelDetailIds.add(it.ticketBean.tikmodel_detail_id)
                    }
                }

                val tikCounts = JsonArray()
                tikCounts.add(fragment.getOrderPriceInfo().ticketCount.toString())

                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                json.addProperty("TripType",mTripType)
                json.addProperty("OrderPrice",fragment.getOrderPriceInfo().price)
                json.add("TikmodelDetailIds",tikmodelDetailIds)
                json.add("TikCounts",tikCounts)
                json.addProperty("ScheduleId",fragment.getSelectedDate().schedule_id)
                json.addProperty("CouponId",0)
                json.addProperty("CouponTpid",0)
                json.addProperty("CouponPrice",0)
                json.add("PassengerIds", passengerIds)
                mPresenter!!.addOrder(json,DialogUtil.getLoadingDialog(fragmentManager))
            }
            R.id.rl_calendar -> {
                if (mBinding.calendar.alpha == 1f) {
                    animIn(mBinding.calendar.height,0)
                } else {
                    animOut(0,mBinding.calendar.height)
                }
            }
        }
    }
    private var mValueAnimator:ValueAnimator ?= null
    private fun animOut(from: Int,to: Int) {
        if (mValueAnimator != null && mValueAnimator!!.isRunning) {
            return
        }

        mValueAnimator = ValueAnimator.ofInt(from,to)
        mValueAnimator!!.addUpdateListener {
            (mBinding.vDrag.layoutParams as ViewGroup.MarginLayoutParams).topMargin = it.animatedValue as Int
            mBinding.vDrag.requestLayout()
            mBinding.calendar.alpha = (it.animatedValue as Int).toFloat() / mBinding.calendar.height
            mBinding.ivArrow.rotation = 180 * mBinding.calendar.alpha
            mBinding.vDrag.alpha = mBinding.calendar.alpha

        }
        mValueAnimator!!.duration = 300
        mValueAnimator!!.interpolator = DecelerateInterpolator()
        mValueAnimator!!.start()
    }

    private fun animIn(from: Int,to: Int) {
        if (mValueAnimator != null && mValueAnimator!!.isRunning) {
            return
        }

        mValueAnimator = ValueAnimator.ofInt(from,to)
        mValueAnimator!!.addUpdateListener {
            (mBinding.vDrag.layoutParams as ViewGroup.MarginLayoutParams).topMargin = it.animatedValue as Int
            mBinding.vDrag.requestLayout()
            mBinding.calendar.alpha = (it.animatedValue as Int).toFloat() / mBinding.calendar.height
            mBinding.ivArrow.rotation = 180 * mBinding.calendar.alpha
            mBinding.vDrag.alpha = mBinding.calendar.alpha

        }
        mValueAnimator!!.duration = 300
        mValueAnimator!!.interpolator = DecelerateInterpolator()
        mValueAnimator!!.start()
    }

    private fun initCalendar() {
        mBinding.weekCalendar.setGetViewHelper(object : GetViewHelper {
            override fun getDayView(
                position: Int,
                convertView: View?,
                parent: ViewGroup?,
                dateTime: DateTime?,
                select: Boolean
            ): View {
                var view = convertView
                if (view == null) {
                    view = LayoutInflater.from(context)
                        .inflate(R.layout.item_day, parent, false)
                }

                val tvDay = view!!.findViewById(R.id.tv_day) as TextView
                val tvWeek = view!!.findViewById<TextView>(R.id.tv_week)
                val llContent = view!!.findViewById<LinearLayout>(R.id.ll_content)
                tvDay.text = dateTime!!.toString("MM-dd")
                when (dateTime.dayOfWeek) {
                    1 -> {
                        tvWeek.text = "周一"
                    }
                    2 -> {
                        tvWeek.text = "周二"
                    }
                    3 -> {
                        tvWeek.text = "周三"
                    }
                    4 -> {
                        tvWeek.text = "周四"
                    }
                    5 -> {
                        tvWeek.text = "周五"
                    }
                    6 -> {
                        tvWeek.text = "周六"
                    }
                    else -> {
                        tvWeek.text = "周天"
                    }

                }

                val calendar = Calendar.getInstance()
                calendar.time = Date()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val now = calendar.time

                if (now.after(dateTime.toDate())) {
                    tvDay.setTextColor(resources.getColor(R.color.c_bdc8ce))
                    tvWeek.setTextColor(resources.getColor(R.color.c_bdc8ce))
                    return view!!
                }

                when {
                    select -> {
                        (mFragments[mBinding.vPager.currentItem] as OrderDateListener).notifyDateChange(dateTime.toDate())
                        llContent.setBackgroundResource(R.drawable.shape_r10_c_25c8c8)
                    }
                    else -> llContent.setBackgroundColor(Color.TRANSPARENT)
                }

                return view!!
            }

            override fun getWeekView(
                position: Int,
                convertView: View?,
                parent: ViewGroup?,
                week: String?
            ): View {
                var view = convertView
                if (view == null) {
                    view = LayoutInflater.from(context)
                        .inflate(R.layout.item_week, parent, false)
                }
                return view!!
            }

        })
        mBinding.calendar.newState().setMinimumDate(LocalDate.now()).commit()
        mBinding.calendar.setOnDateChangedListener { widget, date, selected ->

            var dataStr = date.year.toString()
            if (date.month.toString().length == 1) {
                dataStr += ("0"+date.month)
            } else {
                dataStr +=  date.month
            }

            if (date.day.toString().length == 1) {
                dataStr += ("0"+date.day)
            } else {
                dataStr += date.day
            }

            dataStr += " 23:59:59"
            mBinding.weekCalendar.selectDateTime = DateTime(SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(dataStr).time)

            refresh()
        }

        mBinding.weekCalendar.selectDateTime = DateTime(Date().time)
        val date = Date()
        val arr = SimpleDateFormat("yyyy-MM-dd").format(date).split("-")
        val selected = CalendarDay.from(arr[0].toInt(),arr[1].toInt(),arr[2].toInt())
        mBinding.calendar.selectedDate = selected

        mBinding.weekCalendar.setDateSelectListener {

            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val now = calendar.time

            if (now.after(it.toDate())) {
                return@setDateSelectListener
            }

            val date = it.toDate()
            val arr = SimpleDateFormat("yyyy-MM-dd").format(date).split("-")
            val selected = CalendarDay.from(arr[0].toInt(),arr[1].toInt(),arr[2].toInt())
            mBinding.calendar.selectedDate = selected
            mBinding.calendar.currentDate = selected

            if (mBinding.vPager.currentItem == 0) {
                mDialog?.dismiss()
                mDialog = DialogUtil.getLoadingDialog(fragmentManager)
                mFinishCount = 1
                val json = JsonObject()
                json.addProperty("MemberId", UserUtil.getUser().userInfoBean.memberId)
                json.addProperty("TripType", mTripType)
                json.addProperty("RouteId", arguments!!.getString(BundleKeys.KEY_ROUTE_ID))
                json.addProperty("RideDate", it.toString("yyyy-MM-dd"))
                mPresenter!!.getFrequencys(json, mDialog!!)
            } else {
                mDialog?.dismiss()
                mDialog = DialogUtil.getLoadingDialog(fragmentManager)
                mFinishCount = 1
                val json = JsonObject()
                json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
                json.addProperty("TripType",mTripType)
                json.addProperty("RouteId",arguments!!.getString(BundleKeys.KEY_BACK_ROUTE_ID))
                json.addProperty("RideDate",it.toString("yyyy-MM-dd"))
                mPresenter!!.getFrequencys(json,mDialog!!)
            }

        }
        mBinding.rlCalendar.setOnClickListener(this)
    }

    override fun doAfterAnim() {
    }

    override fun onFrequencySucc(frequencyBean: FrequencyBean,routeId: String) {
        if (routeId == arguments!!.getString(BundleKeys.KEY_ROUTE_ID)) {
            frequencyBean.route_name = arguments!!.getString(BundleKeys.KEY_ROUTE_NAME,"")
            mFrequencyArr[0] = frequencyBean
            (mFragments[0] as OrderGoFragment).notifyDate(frequencyBean)
        } else {
            frequencyBean.route_name = arguments!!.getString(BundleKeys.KEY_ROUTE_BACK_NAME,"")
            mFrequencyArr[1] = frequencyBean
            (mFragments[1] as OrderGoFragment).notifyDate(frequencyBean)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mValueAnimator != null) {
            mValueAnimator!!.cancel()
            mValueAnimator!!.removeAllUpdateListeners()
            mValueAnimator = null
        }
    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        if (!hasInit) {
            hasInit = true
        } else {
            refresh()
        }
    }

    private fun refresh() {
        if (mBinding.vPager.currentItem == 0) {
            mDialog = DialogUtil.getLoadingDialog(fragmentManager)
            mFinishCount = 1
            val json = JsonObject()
            json.addProperty("MemberId", UserUtil.getUser().userInfoBean.memberId)
            json.addProperty("TripType", mTripType)
            json.addProperty("RouteId", arguments!!.getString(BundleKeys.KEY_ROUTE_ID))
            json.addProperty("RideDate", mBinding.weekCalendar.selectDateTime.toString("yyyy-MM-dd"))
            mPresenter!!.getFrequencys(json, mDialog!!)
        } else {
            mDialog = DialogUtil.getLoadingDialog(fragmentManager)
            mFinishCount = 1
            val json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
            json.addProperty("TripType",mTripType)
            json.addProperty("RouteId",arguments!!.getString(BundleKeys.KEY_BACK_ROUTE_ID))
            json.addProperty("RideDate",mBinding.weekCalendar.selectDateTime.toString("yyyy-MM-dd"))
            mPresenter!!.getFrequencys(json,mDialog!!)
        }
    }
}