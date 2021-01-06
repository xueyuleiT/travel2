package com.jtcxw.glcxw.ui.customized

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.ComplimentaryTicketBean
import com.jtcxw.glcxw.base.respmodels.OrderCancelBean
import com.jtcxw.glcxw.base.respmodels.OrderConfirmBean
import com.jtcxw.glcxw.base.utils.*
import com.jtcxw.glcxw.databinding.FragmentOrderConfirmBinding
import com.jtcxw.glcxw.dialog.OrderCancelDialog
import com.jtcxw.glcxw.listeners.OrderCancelCallback
import com.jtcxw.glcxw.presenters.impl.OrderConfirmPresenter
import com.jtcxw.glcxw.ui.my.OrdersFragment
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.OrderConfirmView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer

class OrderConfirmFragment:BaseFragment<FragmentOrderConfirmBinding,CommonModel>() ,OrderConfirmView{
    override fun onComplimentaryTicketSucc(complimentaryTicketBean: ComplimentaryTicketBean) {
        ToastUtil.toastSuccess("成功")
        val cm = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText(null, complimentaryTicketBean.complimentaryUrl))
        mOrder!!.passenger_info[mBinding.vPager.currentItem].free_ticket = "1"
        mBinding.tvGive.text = "赠票（已赠）"
        MaterialDialog(context!!)
            .title(null, "提示")
            .message(null, "分享地址已复制,请到浏览器打开分享")
            .positiveButton(null, SpannelUtil.getSpannelStr("前往", context!!.resources.getColor(R.color.blue_3A75F3)),
                object :DialogCallback{
                    override fun invoke(p1: MaterialDialog) {
                        openBrowser(complimentaryTicketBean.complimentaryUrl)
                    }
                })
            .negativeButton(null,SpannelUtil.getSpannelStr("取消", context!!.resources.getColor(R.color.blue_3A75F3)), null)
            .lifecycleOwner(activity)
            .cornerRadius(DimensionUtil.dpToPx(2), null)
            .cancelable(true)
            .show()
    }

    override fun onOrderCancelSucc(orderCancelBean: OrderCancelBean) {
        ToastUtil.toastSuccess("已取消订单")
        refresh()
    }

    var mOrder: OrderConfirmBean ?= null
    override fun onOrderDetailSucc(orderConfirmBean: OrderConfirmBean) {
        mOrder = orderConfirmBean

        mBinding.llGive.visibility = View.GONE
        mBinding.tvReturn.isEnabled = true
        mBinding.tvReturn.setBackgroundResource(R.drawable.shape_r5_c_green)

        mBinding.tvNo.text = "订单号: ${orderConfirmBean.order_id}"
        mBinding.tvOrderTime.text = "${orderConfirmBean.order_time} 下单"
        var orderStatus = "状态: "
        when {
            orderConfirmBean.order_state == 0 ->{
                orderStatus += "未支付"
                mBinding.tvPayStatus.text = SpannelUtil.getSpannelStr(orderStatus,resources.getColor(R.color.green_light),orderStatus.length - 3,orderStatus.length)
                mBinding.llBottom.visibility = View.VISIBLE
                mBinding.llTip.visibility = View.VISIBLE

                startTimer()
            }
            orderConfirmBean.order_state == 1 -> {
                orderStatus += "支付中"
                mBinding.tvPayStatus.text = SpannelUtil.getSpannelStr(orderStatus,resources.getColor(R.color.red_ff3737),orderStatus.length - 3,orderStatus.length)
                mBinding.llBottom.visibility = View.GONE
                mBinding.llTip.visibility = View.GONE
            }
            orderConfirmBean.order_state == 2 -> {
                orderStatus += "已支付未出票"
                mBinding.tvPayStatus.text = SpannelUtil.getSpannelStr(orderStatus,resources.getColor(R.color.red_ff3737),orderStatus.length - 6,orderStatus.length)
                mBinding.llBottom.visibility = View.GONE
                mBinding.llTip.visibility = View.GONE
            }
            orderConfirmBean.order_state == 4 -> {
                orderStatus += "已支付已出票"
                mBinding.tvPayStatus.text = SpannelUtil.getSpannelStr(orderStatus,resources.getColor(R.color.red_ff3737),orderStatus.length - 6,orderStatus.length)
                mBinding.llBottom.visibility = View.GONE
                mBinding.llTip.visibility = View.GONE
                mBinding.llGive.visibility = View.VISIBLE
                mBinding.llProof.visibility = View.VISIBLE
                mBinding.llGive.visibility = View.VISIBLE
                if (mOrder!!.passenger_info!= null && mOrder!!.passenger_info.isNotEmpty()) {
                    if (mOrder!!.passenger_info[mBinding.vPager.currentItem].tikcet_state != "N"){ //N 未检票
                        mBinding.llGive.visibility = View.GONE
                    }
                }


                if (!TextUtils.isEmpty(mOrder!!.ride_time)) {
                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val leftTime =
                        (format.parse(mOrder!!.ride_time).time - System.currentTimeMillis()) / 1000
                    if (leftTime < 60 * 60) {
                        mBinding.tvReturn.isEnabled = false
                        mBinding.tvGive.isEnabled = false
                        mBinding.tvReturn.setBackgroundResource(R.drawable.shape_r5_gray)
                        mBinding.tvGive.setBackgroundResource(R.drawable.shape_r5_gray)
                    }
                }

                initViewPager(orderConfirmBean.passenger_info)
            }
            orderConfirmBean.order_state == 16 -> {
                orderStatus += "已取消"
                mBinding.tvPayStatus.text = SpannelUtil.getSpannelStr(orderStatus,resources.getColor(R.color.gray_9),orderStatus.length - 3,orderStatus.length)
                mBinding.llBottom.visibility = View.GONE
                mBinding.llTip.visibility = View.GONE
            }
             else -> {
                 mBinding.llBottom.visibility = View.GONE
             }
        }

        val money = "金额 ¥" + orderConfirmBean.order_price
        mBinding.tvMoney.text = SpannelUtil.getSpannelStr(money,resources.getColor(R.color.red_ff3737),money.length - orderConfirmBean.order_price.toString().length - 1,money.length)
        mBinding.tvType.text = if (orderConfirmBean.trip_type == 4) "定制公交" else "校园定制公交"
        mBinding.tvStationFrom.text = orderConfirmBean.start_station_name
        mBinding.tvCityFrom.text = orderConfirmBean.from_city

        mBinding.tvStationTo.text = orderConfirmBean.end_station_name
        mBinding.tvCityTo.text = orderConfirmBean.to_city
        mBinding.tvTransTime.text = orderConfirmBean.ride_time
        mBinding.tvTransName.text = orderConfirmBean.vehicle_type_name


        val ticket = "购票:${orderConfirmBean.passenger_info.size}张"
        mBinding.tvTicketCount.text = SpannelUtil.getSpannelStr(ticket,resources.getColor(R.color.red_ff3737),3,ticket.length)

        mBinding.tvRemark.text = orderConfirmBean.refund_remark

    }

    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?) {
            val orderConfirmFragment = OrderConfirmFragment()
            orderConfirmFragment.arguments = bundle
            fragment.start(orderConfirmFragment)
        }
    }


    fun getTikmodelId():Long {
        return mOrder!!.tikmodel_id
    }

    fun getTakeNum():Int {
        return mOrder!!.passenger_info.size
    }

    fun getPassengerInfo(i: Int):OrderConfirmBean.PassengerInfoBean {
        return mOrder!!.passenger_info[i]
    }

    val mFragments = ArrayList<Fragment>(2)
    @SuppressLint("ClickableViewAccessibility")
    private fun initViewPager(passengerInfo: MutableList<OrderConfirmBean.PassengerInfoBean>) {

        if (mBinding.vPager.adapter != null) {
            mFragments.forEach {
                (it as TicketFragment).refresh()
            }

        } else {
            mFragments.clear()
            var index = 0
            passengerInfo.forEach {
                mFragments.add(TicketFragment(index))
                index ++
            }

            if (mOrder!!.passenger_info!= null && mOrder!!.passenger_info.isNotEmpty()) {
                if (mOrder!!.passenger_info[0].tikcet_state != "N"){
                    mBinding.llGive.visibility = View.GONE
                } else {
                    mBinding.llGive.visibility = View.VISIBLE
                    if (mOrder!!.passenger_info[0].free_ticket == "1") {
                        mBinding.tvGive.text = "赠票（已赠）"
                    } else {
                        mBinding.tvGive.text = "赠  票"
                        mBinding.tvGive.isEnabled = true
                        mBinding.tvGive.setBackgroundResource(R.drawable.shape_r5_c_green)
                    }
                }
            }

            mBinding.vPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    return mFragments[position]
                }

                override fun getCount(): Int {
                    return mFragments.size
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return ""
                }
            }

            mBinding.tabLayout.setupWithViewPager(mBinding.vPager)
            for (i in 0 until mBinding.tabLayout.tabCount) {
                val view = LayoutInflater.from(context).inflate(R.layout.item_ticket, null)
                mBinding.tabLayout.getTabAt(i)!!.customView = view
            }

            val scroller = ViewPagerScroller(activity)
            scroller.setScrollDuration(200)//时间越长，速度越慢。
            scroller.initViewPagerScroll(mBinding.vPager)


            mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab!!.customView!!.findViewById<View>(R.id.v_dot).setBackgroundResource(R.drawable.shape_dot_gray9)
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab!!.customView!!.findViewById<View>(R.id.v_dot).setBackgroundResource(R.drawable.shape_dot_green)

                }
            })
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
                    if (mOrder!!.passenger_info!= null && mOrder!!.passenger_info.isNotEmpty()) {
                        if (mOrder!!.passenger_info[position].tikcet_state != "N"){
                            mBinding.llGive.visibility = View.GONE
                        } else {
                            mBinding.llGive.visibility = View.VISIBLE
                            if (mOrder!!.passenger_info[position].free_ticket == "1") {
                                mBinding.tvGive.text = "赠票（已赠）"
                            } else {
                                mBinding.tvGive.text = "赠  票"
                                mBinding.tvGive.isEnabled = true
                                mBinding.tvGive.setBackgroundResource(R.drawable.shape_r5_c_green)
                            }
                        }
                    }
                }

            })

            mBinding.tabLayout.getTabAt(0)!!.select()

            if (passengerInfo.size < 2){
                mBinding.tabLayout.visibility = View.GONE
            } else {
                mBinding.tabLayout.visibility = View.VISIBLE
                mBinding.tabLayout.getTabAt(0)!!.customView!!.findViewById<View>(R.id.v_dot).setBackgroundResource(R.drawable.shape_dot_green)
            }
        }

    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_order_confirm
    }

    var mPresenter: OrderConfirmPresenter?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("订单详情")

        mPresenter = OrderConfirmPresenter(this)

        mBinding.tvCancel.setOnClickListener(this)
        mBinding.tvPay.setOnClickListener(this)
        mBinding.tvGive.setOnClickListener(this)
        mBinding.tvReturn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (mOrder == null) {
            return
        }
        when(v?.id) {
            R.id.tv_return -> {
                val bundle = Bundle()
                bundle.putParcelable(BundleKeys.KEY_ORDER,mOrder)
                TurnBackFragment.newInstance(this,bundle)
            }
            R.id.tv_give -> {
//                if (getPassengerInfo(mBinding.vPager.currentItem).free_ticket == "1") {
//                    return
//                }
                val json = JsonObject()
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                json.addProperty("OrderId", arguments!!.getString(BundleKeys.KEY_ORDER_ID))
                json.addProperty("TicketNo", getPassengerInfo(mBinding.vPager.currentItem).ticket_no)
                mPresenter!!.complimentaryTicket(json)
            }
            R.id.tv_pay -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_PAY_TYPE,"glcx_custombuspay")
                bundle.putString(BundleKeys.KEY_ORDER_ID,arguments!!.getString(BundleKeys.KEY_ORDER_ID))
                bundle.putString(BundleKeys.KEY_ORDER_AMOUNT, mOrder!!.order_price.toString())
                OrderPayFragment.newInstance(this,bundle)
            }
            R.id.tv_cancel -> {
                OrderCancelDialog()
                    .setOrderCancelCallback(object : OrderCancelCallback{
                        override fun onOrderCancel() {
                        }

                        override fun onOrderConfirm() {
                            val json = JsonObject()
                            json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                            json.addProperty("OrderId", arguments!!.getString(BundleKeys.KEY_ORDER_ID))
                            mPresenter!!.orderCancel(json,DialogUtil.getLoadingDialog(fragmentManager))
                        }

                    })
                    .show(fragmentManager!!,"OrderCancelDialog")
            }
        }
    }

    override fun doAfterAnim() {
        refresh()
    }


    fun refresh(){
        val json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        json.addProperty("BusiType","32")
        json.addProperty("OrderId",arguments!!.getString(BundleKeys.KEY_ORDER_ID))
        mPresenter!!.getOrderDetail(json,DialogUtil.getLoadingDialog(fragmentManager))
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    private var timer: Timer?= null

    private fun stopTimer() {
        timer?.cancel()
    }
    private fun startTimer() {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        timer = fixedRateTimer("", false, 0, 1000) {
          val leftTime =  (format.parse(mOrder!!.order_time).time + 20 * 60 * 1000 - System.currentTimeMillis()) / 1000
            if (leftTime < 0) {
                stopTimer()
                return@fixedRateTimer
            }
            val timeStr = "剩余时间:" + leftTime / 60 + "分" + leftTime % 60 + "秒"
            mBinding.tvLeftTime.text = SpannelUtil.getSpannelStr(timeStr,resources.getColor(R.color.red_ff3737),5, timeStr.length)
        }
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (requestCode == 90) {
            if (resultCode == ISupportFragment.RESULT_OK) {
                pop()
            }
        } else if (requestCode == 11){
            refresh()
        } else {
            if (resultCode == ISupportFragment.RESULT_OK) {
                refresh()
//                popToChild(OrderListFragment::class.java,false)
//               startWithPop(OrdersFragment())
            }
        }
    }


    fun openBrowser(url: String) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val contentUrl = Uri.parse(url)
        intent.data = contentUrl
        startActivity(intent)
    }

}