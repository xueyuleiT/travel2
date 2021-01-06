package com.jtcxw.glcxw.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.google.gson.JsonObject
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.FrequencyBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.presenters.impl.OrderTicketGoPresenter
import com.jtcxw.glcxw.ui.BusLineGoOrderFragment
import com.jtcxw.glcxw.ui.customized.OrderGoFragment
import java.text.SimpleDateFormat

class BusScheduleAdapter(
    fragment: OrderGoFragment,
    data: List<FrequencyBean.scheduleListBean>,
    presenter: OrderTicketGoPresenter?
): BaseRecyclerAdapter<FrequencyBean.scheduleListBean>(fragment.context, data)  {


    val mFragment = fragment
    val mPresenter = presenter
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_schedule
    }

    @SuppressLint("SetTextI18n")
    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: FrequencyBean.scheduleListBean?,
        position: Int
    ) {
        val tvTime = holder!!.getView<TextView>(R.id.tv_time)
        val tvTicket = holder!!.getView<TextView>(R.id.tv_ticket)
        val tvLeft = holder!!.getView<TextView>(R.id.tv_left)
        val rlBg = holder!!.getView<View>(R.id.rl_bg)
        val llTicket = holder!!.getView<View>(R.id.ll_ticket)
        val rlTicket = holder!!.getView<View>(R.id.rl_ticket)
        val vDivider = holder!!.getView<View>(R.id.v_divider)
        val tvNone = holder!!.getView<View>(R.id.tv_none)
        val progressBar = holder!!.getView<View>(R.id.progress_bar)
//        val tvGet = holder!!.getView<View>(R.id.tv_get)

        tvNone.visibility = View.GONE
        if (data!!.isSelected) {
            tvTime.setTextColor(mContext.resources.getColor(R.color.green_light))
            tvTicket.setTextColor(mContext.resources.getColor(R.color.white))
            tvLeft.setTextColor(mContext.resources.getColor(R.color.white))
            vDivider.setBackgroundColor(mContext.resources.getColor(R.color.white))
            rlBg.setBackgroundResource(R.drawable.shape_r2_b1_cgreen)
            rlTicket.setBackgroundResource(R.drawable.shape_r2_cgreen_bottom)

        } else {
            tvTime.setTextColor(mContext.resources.getColor(R.color.black_263238))
            tvTicket.setTextColor(mContext.resources.getColor(R.color.black_263238))
            tvLeft.setTextColor(mContext.resources.getColor(R.color.black_263238))
            vDivider.setBackgroundColor(mContext.resources.getColor(R.color.black_263238))
            rlBg.setBackgroundResource(R.drawable.shape_r2_c_gray9)
            rlTicket.setBackgroundResource(R.drawable.shape_r2_c_gray9_bottom)

            if (data!!.ticketLoadStatus == -1) {
                progressBar.visibility = View.VISIBLE
                holder.convertView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                    override fun onGlobalLayout() {
                        holder.convertView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        progressBar.layoutParams.height = holder.convertView.height
                        progressBar.requestLayout()
                    }
                })
                progressBar.setBackgroundResource(R.drawable.shape_r2_c_gray9_progress)
            } else {
                progressBar.visibility = View.GONE
            }
        }


//        tvGet.visibility = View.GONE
//        tvGet.setOnClickListener(null)
        if (data!!.ticketLoadStatus < 0) {
            llTicket.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE


            if (data!!.ticketBean !== null) {
                tvTime.text = data!!.ticketBean.ride_time.split(" ")[1]
            } else {
                tvTime.text = data.ride_time
            }
            if (data!!.ticketLoadStatus == -2) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
                jsonObject.addProperty("TripType",(mFragment.parentFragment as BusLineGoOrderFragment).mTripType)
                jsonObject.addProperty("ScheduleId",data!!.schedule_id)
                jsonObject.addProperty("RideStId", mFragment.mRideStationId)
                jsonObject.addProperty("ReachStId",mFragment.mReachStationId)
                jsonObject.addProperty("Month",SimpleDateFormat("yyyy-MM").format(mFragment.getDate()))
                mPresenter!!.getRoundTikmodelList(jsonObject)
            } else {
                progressBar.visibility = View.GONE
                tvNone.visibility = View.VISIBLE
//                tvGet.visibility = View.VISIBLE
//                tvGet.setOnClickListener{
//                    tvGet.visibility = View.GONE
//                    val jsonObject = JsonObject()
//                    jsonObject.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
//                    jsonObject.addProperty("TripType",(mFragment.parentFragment as BusLineGoOrderFragment).mTripType)
//                    jsonObject.addProperty("ScheduleId",data!!.schedule_id)
//                    jsonObject.addProperty("RideStId", mFragment.mRideStationId)
//                    jsonObject.addProperty("ReachStId",mFragment.mReachStationId)
//                    jsonObject.addProperty("Month",SimpleDateFormat("yyyy-MM").format(mFragment.getDate()))
//                    mPresenter!!.getRoundTikmodelList(jsonObject)
//                }
            }
            holder.convertView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    holder.convertView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    progressBar.layoutParams.height = holder.convertView.height
                    progressBar.requestLayout()
                }

            })
            if (data!!.isSelected) {
                progressBar.setBackgroundResource(R.drawable.shape_r2_cgreen_progress)
            } else {
                progressBar.setBackgroundResource(R.drawable.shape_r2_c_gray9_progress)
            }
        } else {
            llTicket.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            if (data!!.ticketBean == null){
                llTicket.visibility = View.INVISIBLE
                tvNone.visibility = View.VISIBLE
            } else {
                llTicket.visibility = View.VISIBLE
                tvNone.visibility = View.GONE
                tvTime.text = data!!.ride_time

                tvTicket.text = "总票:${data!!.ticketBean.total_seats}张"
                tvLeft.text = "余:${data!!.ticketBean.leave_seats}张"
            }

        }


    }



    override fun onClick(
        view: View?,
        model: FrequencyBean.scheduleListBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}