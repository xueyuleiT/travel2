package com.jtcxw.glcxw.adapter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.JsonObject
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.OrderListBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.dialog.OrderCancelDialog
import com.jtcxw.glcxw.listeners.OrderCancelCallback
import com.jtcxw.glcxw.ui.customized.OrderConfirmFragment
import com.jtcxw.glcxw.ui.customized.OrderListFragment
import com.jtcxw.glcxw.ui.customized.OrderPayFragment
import me.yokeyword.fragmentation.SupportFragment

class OrderListAdapter(context: Context,fragment: OrderListFragment,data: List<OrderListBean.OrderBean>) : BaseRecyclerAdapter<OrderListBean.OrderBean>(context, data)  {
    val mFragment = fragment
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_order
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: OrderListBean.OrderBean?,
        position: Int
    ) {
        val tvPayStatus = holder!!.getView<TextView>(R.id.tv_pay_status)
        val tvType = holder!!.getView<TextView>(R.id.tv_type)
        val tvStationFrom = holder!!.getView<TextView>(R.id.tv_station_from)
        val tvStationTo = holder!!.getView<TextView>(R.id.tv_station_to)
        val tvTime = holder!!.getView<TextView>(R.id.tv_time)
        val tvDelete = holder!!.getView<TextView>(R.id.tv_delete)
        val tvPay = holder!!.getView<TextView>(R.id.tv_pay)
        val vTime = holder!!.getView<ImageView>(R.id.v_time)

        tvPayStatus.text = data!!.state_name
        tvDelete.setOnClickListener(null)
        tvPay.setOnClickListener(null)
        when {
            data!!.order_state == 0 -> {
                vTime.setImageResource(R.mipmap.icon_order_start_time)
                tvPayStatus.setTextColor(tvPayStatus.resources.getColor(R.color.green_light))
                tvType.setTextColor(tvPayStatus.resources.getColor(R.color.black_263238))
                tvStationFrom.setTextColor(tvPayStatus.resources.getColor(R.color.black_263238))
                tvStationTo.setTextColor(tvPayStatus.resources.getColor(R.color.black_263238))
                tvTime.setTextColor(tvPayStatus.resources.getColor(R.color.black_263238))
                tvDelete.setTextColor(tvPayStatus.resources.getColor(R.color.gray_9))
                tvPay.visibility = View.VISIBLE
                tvPay.text = "支付"
                tvPay.setTextColor(tvPayStatus.resources.getColor(R.color.white))
                tvPay.setBackgroundResource(R.drawable.shape_r2_cgreen)
                tvDelete.text = "删除"
                tvDelete.setOnClickListener {
                    OrderCancelDialog()
                        .setTitle("是否删除订单")
                        .setOrderCancelCallback(object :OrderCancelCallback{
                            override fun onOrderCancel() {
                            }

                            override fun onOrderConfirm() {
                                val json = JsonObject()
                                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                                json.addProperty("OrderId",data!!.order_id)
                                mFragment.mPresenter!!.deleteBusOrder(json)
                            }

                        }).show(mFragment.childFragmentManager,"OrderCancelDialog")
                }
                tvPay.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_PAY_TYPE,"glcx_custombuspay")
                    bundle.putString(BundleKeys.KEY_ORDER_ID,data!!.order_id)
                    bundle.putString(BundleKeys.KEY_ORDER_AMOUNT,data!!.order_price.toString())
                    OrderPayFragment.newInstance(mFragment.parentFragment as SupportFragment,bundle)
                }
            }
            data!!.order_state == 16 -> {
                vTime.setImageResource(R.mipmap.icon_order_start_time_gray)
                tvPayStatus.setTextColor(tvPayStatus.resources.getColor(R.color.gray_9))
                tvType.setTextColor(tvPayStatus.resources.getColor(R.color.gray_9))
                tvStationFrom.setTextColor(tvPayStatus.resources.getColor(R.color.gray_9))
                tvStationTo.setTextColor(tvPayStatus.resources.getColor(R.color.gray_9))
                tvTime.setTextColor(tvPayStatus.resources.getColor(R.color.gray_9))
                tvDelete.setTextColor(tvPayStatus.resources.getColor(R.color.gray_9))
                tvDelete.text = "删除"
                tvPay.visibility = View.VISIBLE
                tvPay.text = "订单详情"
                tvPay.setTextColor(tvPayStatus.resources.getColor(R.color.black_263238))
                tvPay.setBackgroundResource(R.drawable.shape_r2_b1_cgreen)

                tvDelete.setOnClickListener{
                    OrderCancelDialog()
                        .setTitle("是否删除订单")
                        .setOrderCancelCallback(object :OrderCancelCallback{
                            override fun onOrderCancel() {
                            }

                            override fun onOrderConfirm() {
                                val json = JsonObject()
                                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                                json.addProperty("OrderId",data!!.order_id)
                                mFragment.mPresenter!!.deleteBusOrder(json)
                            }

                        }).show(mFragment.childFragmentManager,"OrderCancelDialog")
                }

                tvPay.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_ORDER_ID,data!!.order_id)
                    OrderConfirmFragment.newInstance(mFragment.parentFragment as SupportFragment,bundle)
                }
            }
            else -> {
                vTime.setImageResource(R.mipmap.icon_order_start_time)
                tvPay.visibility = View.GONE
                tvDelete.text = "订单详情"
                tvDelete.setTextColor(tvPayStatus.resources.getColor(R.color.black_263238))
                tvPayStatus.setTextColor(tvPayStatus.resources.getColor(R.color.red_ff3737))
                tvDelete.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_ORDER_ID,data!!.order_id)
                    OrderConfirmFragment.newInstance(mFragment.parentFragment as SupportFragment,bundle)
                }

            }
        }

        tvType.text = data!!.order_name

        tvStationFrom.text = data!!.order_details[0].ride_station
        tvStationTo.text = data!!.order_details[0].reach_station
        tvTime.text = data!!.order_details[0].ride_dates[0]



    }

    override fun onClick(
        view: View?,
        model: OrderListBean.OrderBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}