package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.OrderMixBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder

class OrdersAdapter (context: Context, date:List<OrderMixBean.MemberOrderListBean>):
    BaseRecyclerAdapter<OrderMixBean.MemberOrderListBean>(context,date) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_orders
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: OrderMixBean.MemberOrderListBean?,
        position: Int
    ) {

        val vType = holder!!.getView<ImageView>(R.id.v_type)
        val tvType = holder!!.getView<TextView>(R.id.tv_type)
        val tvTime = holder!!.getView<TextView>(R.id.tv_time)
        val tvTrans = holder!!.getView<TextView>(R.id.tv_trans)
        val tvNo = holder!!.getView<TextView>(R.id.tv_no)
        val tvStatus = holder!!.getView<TextView>(R.id.tv_status)
        val vArrow = holder!!.getView<ImageView>(R.id.v_arrow)

        if (data!!.orderType == 2) {
            vType.setImageResource(R.mipmap.icon_order_bus)
            tvType.text = "定制公交"
            vArrow.visibility = View.VISIBLE
        } else {
            vArrow.visibility = View.INVISIBLE
            tvType.text = "公交乘车码"
            vType.setImageResource(R.mipmap.icon_order_qr)
        }

        tvStatus.text = data!!.orderStatus

        tvTime.text = data!!.orderTime

        tvTrans.text = data!!.orderContent

        tvNo.text = "订单号:${data!!.orderNo}"
    }

    override fun onClick(
        view: View?,
        model: OrderMixBean.MemberOrderListBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }

}