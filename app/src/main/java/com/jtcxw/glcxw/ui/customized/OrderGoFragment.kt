package com.jtcxw.glcxw.ui.customized

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import com.glcxw.lib.util.AmountUtil
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.BusScheduleAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.respmodels.AddOrderBean
import com.jtcxw.glcxw.base.respmodels.FrequencyBean
import com.jtcxw.glcxw.base.respmodels.TicketBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.RecyclerAdapterWrapper
import com.jtcxw.glcxw.databinding.FragmentOrderGoBinding
import com.jtcxw.glcxw.dialog.BusStationDialog
import com.jtcxw.glcxw.listeners.BusDialogCallback
import com.jtcxw.glcxw.listeners.OrderDateListener
import com.jtcxw.glcxw.localbean.OrderPriceBean
import com.jtcxw.glcxw.presenters.impl.OrderTicketGoPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.OrderTicketGoView
import java.text.SimpleDateFormat
import java.util.*

class OrderGoFragment:BaseFragment<FragmentOrderGoBinding,CommonModel>(), OrderDateListener,OrderTicketGoView {
    override fun checkTicketCount(): Boolean {
        if (checkOrderInfo()) {
            return if (mTicketCount < mBinding.tvTicketCount.text.toString().toInt()) {
                ToastUtil.toastWaring("可购买票数不足")
                false
            } else {
                true
            }
        }
        ToastUtil.toastWaring("可购买票数不足")
        return false
    }

    override fun getFrequencyBean(): FrequencyBean {
        return mFrequencyBean!!
    }

    override fun getOrderPriceInfo(): OrderPriceBean {
        val orderPriceBean = OrderPriceBean()
        orderPriceBean.ticketCount = mBinding.tvTicketCount.text.toString().toInt()
        orderPriceBean.price = orderPriceBean.ticketCount * getSelectedDate().ticketBean.tik_price
        return orderPriceBean
    }

    override fun checkOrderInfo(): Boolean {
        if (mFrequencyBean == null) {
            return false
        }


        if (mFrequencyBean!!.schedule_list == null || mFrequencyBean!!.schedule_list.isEmpty()) {
            return false
        }

        if (mFrequencyBean!!.schedule_list[mSelectedIndex].ticketBean == null) {
            return false
        }

        return true

    }

    override fun getSelectedDate(): FrequencyBean.scheduleListBean {
        return mFrequencyBean!!.schedule_list[mSelectedIndex]
    }

    override fun onRoundTikmodelListFailed(id: String) {
        mFrequencyBean!!.schedule_list.forEach {
            if (id == it.schedule_id.toString()) {
                it.ticketLoadStatus = -1
                (mBinding.recyclerView.innerAdapter as BusScheduleAdapter).updateItem(it)
                return@forEach
            }
        }

    }
    var mTicketCount = 0

    override fun onRoundTikmodelListSucc(ticketBean: TicketBean,id:String) {
        mFrequencyBean!!.schedule_list.forEach {
            if (id == it.schedule_id.toString()) {
                it.ticketLoadStatus = 0
                it.ride_time
                val sdf =  SimpleDateFormat("yyyy-MM-dd HH:mm")
                val yyyyMMdd = SimpleDateFormat("yyyyMMdd")
                val currentDate = yyyyMMdd.format(mDate.time)
                ticketBean.tikmodel_list?.forEach {it_ ->
                    if (currentDate == yyyyMMdd.format(sdf.parse(it_.ride_time))) {
                        it.realTime = it_.ride_time
                        it.ticketBean = it_
                        if (it.isSelected) {
                            val bean = it_
                            mBinding.tvLeftTicket.text =  "余票${bean.leave_seats}张"
                            mTicketCount = bean.leave_seats
                            mBinding.tvPrice.text = "¥${AmountUtil.formatTosepara(bean.tik_price)}"
                            mBinding.tvTicketCount.text = "1"
                        }
                    }
                }
                (mBinding.recyclerView.innerAdapter as BusScheduleAdapter).updateItem(it)
                return@forEach
            }
        }
    }

    override fun notifyDateChange(date: Date) {
        mDate = date
        if (ismBindingInitialized()){
            mBinding.tvRideTime.text = SimpleDateFormat("MM月dd日").format(mDate)
//            mBinding.tvLeftTicket.text =  "余票0张"
//            mTicketCount = 0
//            mBinding.tvPrice.text = "¥--"
//            mBinding.tvTicketCount.text = "1"
        }

    }

    override fun getDate(): Date {
        return mDate
    }

    override fun getVariableId(): Int {
        return BR.common
    }
    var mRouteName = ""
    var mFrequencyBean : FrequencyBean?= null
    var mRideStationId = ""
    var mReachStationId = ""
    var mDate = Date()
    override fun getLayoutId(): Int {
        return R.layout.fragment_order_go
    }

    var mSelectedIndex = -1
    var mPresenter:OrderTicketGoPresenter? = null
    override fun onClick(v: View?) {
        super.onClick(v)
        if (mSelectedIndex == -1) {
            ToastUtil.toastWaring("请先选择发车班次")
            return
        }
        when(v?.id) {
            R.id.v_plus -> {
                if (mFrequencyBean!!.schedule_list[mSelectedIndex].ticketBean == null) {
                    return
                }
                var count = mBinding.tvTicketCount.text.toString().toInt()
                if (count < mFrequencyBean!!.schedule_list[mSelectedIndex].ticketBean.leave_seats) {
                    count ++
                    mBinding.tvTicketCount.text = "$count"
                    mBinding.tvPrice.text = "¥${AmountUtil.formatTosepara(mFrequencyBean!!.schedule_list[mSelectedIndex].ticketBean.tik_price * count)}"
                }
            }

            R.id.v_decrease -> {
                if (mFrequencyBean!!.schedule_list[mSelectedIndex].ticketBean == null) {
                    return
                }

                var count = mBinding.tvTicketCount.text.toString().toInt()
                if (count > 1) {
                    count --
                    mBinding.tvTicketCount.text = "$count"
                    mBinding.tvPrice.text = "¥${AmountUtil.formatTosepara(mFrequencyBean!!.schedule_list[mSelectedIndex].ticketBean.tik_price * count)}"
                }
            }

            R.id.tv_ride , R.id.tv_ride_choose-> {
                BusStationDialog().setTitle("选择上车点")
                    .setData(mFrequencyBean!!.schedule_list[mSelectedIndex].ride_stations)
                    .setCallback(object :BusDialogCallback{
                        override fun onBusDialogCallback(i: Int) {
                            mBinding.tvRide.text = mFrequencyBean!!.schedule_list[mSelectedIndex].ride_stations[i].station_name + "（" + mFrequencyBean!!.schedule_list[mSelectedIndex].ride_stations[i].ride_time +"）"
                            mBinding.vRide.visibility = View.VISIBLE
                        }

                    })
                    .show(childFragmentManager,"BusStationDialog")
            }

            R.id.tv_reach , R.id.tv_reach_choose-> {
                mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations.forEach {
                    it.type = 1
                }
                BusStationDialog().setTitle("选择下车点")
                    .setData(mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations)
                    .setCallback(object :BusDialogCallback{
                        override fun onBusDialogCallback(i: Int) {
                            mBinding.tvReach.text = mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations[i].station_name + "（" + mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations[i].ride_time +"）"
                            mBinding.vReach.visibility = View.VISIBLE
                        }

                    })
                    .show(childFragmentManager,"BusStationDialog")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = OrderTicketGoPresenter(this)

        mBinding.nestView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                mBinding.nestView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mBinding.nestView.setPadding(0,0,0,0)
            }

        })

        mBinding.tvRide.setOnClickListener(this)
        mBinding.tvReach.setOnClickListener(this)

        mBinding.tvRideChoose.setOnClickListener(this)
        mBinding.tvReachChoose.setOnClickListener(this)
        mBinding.vPlus.setOnClickListener(this)
        mBinding.vDecrease.setOnClickListener(this)
        mBinding.tvTicketCount

        if (mFrequencyBean != null) {
            notifyDate(mFrequencyBean!!)
        }


    }

    override fun doAfterAnim() {
    }


    fun notifyDate(frequencyBean: FrequencyBean) {
        mFrequencyBean = frequencyBean

        if (!ismBindingInitialized()) {
            return
        }

        mBinding.vReach.visibility = View.GONE
        mBinding.vRide.visibility = View.GONE
        mBinding.tvRide.text = ""
        mBinding.tvReach.text = ""
        mBinding.tvTicketCount.text = "1"
        mBinding.tvPrice.text = "¥--"
        mBinding.tvLeftTicket.text = "余票0张"
        mTicketCount = 0
        mSelectedIndex = -1

        mBinding.tvName.text = mFrequencyBean!!.route_name
        if (mFrequencyBean!!.schedule_list.size > 0) {
            mSelectedIndex = 0
            mFrequencyBean!!.schedule_list[0].isSelected = true
            if (mFrequencyBean!!.schedule_list[mSelectedIndex].ticketBean != null) {
                mBinding.tvPrice.text =
                    "¥${AmountUtil.formatTosepara(mFrequencyBean!!.schedule_list[mSelectedIndex].ticketBean.tik_price)}"
            }
            if (mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations.isNotEmpty()) {
                val bean = mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations[mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations.size - 1]
                bean.isChecked = true
                mReachStationId = bean.station_id.toString()
                mBinding.tvReach.text = "${bean.station_name}（${bean.ride_time}）"
                mBinding.vReach.visibility = View.VISIBLE
            }

            if (mFrequencyBean!!.schedule_list[mSelectedIndex].ride_stations.isNotEmpty()) {
                val bean = mFrequencyBean!!.schedule_list[mSelectedIndex].ride_stations[0]
                bean.isChecked = true
                mRideStationId = bean.station_id.toString()
                mBinding.tvRide.text = "${bean.station_name}（${bean.ride_time}）"
                mBinding.vRide.visibility = View.VISIBLE
            }
        }

        val adapter = BusScheduleAdapter(this,mFrequencyBean!!.schedule_list,mPresenter)
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<FrequencyBean.scheduleListBean>{
            override fun onItemClick(
                view: View?,
                model: FrequencyBean.scheduleListBean?,
                position: Int
            ) {


                mFrequencyBean!!.schedule_list.forEach {
                    it.isSelected = false
                }
                model!!.isSelected = true
                mBinding.recyclerView.innerAdapter.notifyAllItems()
                mSelectedIndex = position
                if (mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations.isNotEmpty()) {
                    mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations.forEach {
                        it.isChecked = false
                    }
                    val bean = mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations[mFrequencyBean!!.schedule_list[mSelectedIndex].reach_stations.size - 1]
                    bean.isChecked = true
                    mReachStationId = bean.station_id.toString()
                    mBinding.tvReach.text = "${bean.station_name}（${bean.ride_time}）"
                    mBinding.vReach.visibility = View.VISIBLE

                } else {
                    mBinding.tvReach.text = "--"
                    mBinding.vReach.visibility = View.VISIBLE
                }

                if (mFrequencyBean!!.schedule_list[mSelectedIndex].ride_stations.isNotEmpty()) {
                    mFrequencyBean!!.schedule_list[mSelectedIndex].ride_stations.forEach {
                        it.isChecked = false
                    }
                    val bean = mFrequencyBean!!.schedule_list[mSelectedIndex].ride_stations[0]
                    bean.isChecked = true
                    mRideStationId = bean.station_id.toString()
                    mBinding.tvRide.text = "${bean.station_name}（${bean.ride_time}）"
                    mBinding.vRide.visibility = View.VISIBLE
                } else {
                    mBinding.tvRide.text = "--"
                    mBinding.vRide.visibility = View.VISIBLE
                }

                if (model!!.ticketBean != null) {
                    val bean = model!!.ticketBean
                    mBinding.tvRideTime.text = SimpleDateFormat("MM月dd日").format(mDate)
                    mBinding.tvLeftTicket.text =  "余票${bean.leave_seats}张"
                    mTicketCount = bean.leave_seats
                    mBinding.tvPrice.text = "¥${AmountUtil.formatTosepara(bean.tik_price)}"
                    mBinding.tvTicketCount.text = "1"
                } else {
                    mBinding.tvRideTime.text = SimpleDateFormat("MM月dd日").format(mDate)
                    mBinding.tvLeftTicket.text =  "余票0张"
                    mTicketCount = 0
                    mBinding.tvPrice.text = "¥--"
                    mBinding.tvTicketCount.text = "1"
                }

            }

            override fun onItemLongClick(
                view: View?,
                model: FrequencyBean.scheduleListBean?,
                position: Int
            ) {
            }

        })
        mBinding.recyclerView.layoutManager = GridLayoutManager(context!!,2)
        mBinding.recyclerView.adapter = adapter


    }
}