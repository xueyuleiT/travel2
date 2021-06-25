package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.glcxw.lib.util.AmountUtil
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.MemberAccountHistoryBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder

class AccountRecordAdapter(context: Context,list: List<MemberAccountHistoryBean.MemberAccountHistoryListBean>):BaseRecyclerAdapter<MemberAccountHistoryBean.MemberAccountHistoryListBean>(context,list) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_account_record
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: MemberAccountHistoryBean.MemberAccountHistoryListBean?,
        position: Int
    ) {
        val ivType = holder!!.getView<ImageView>(R.id.iv_type)
        val tvType = holder!!.getView<TextView>(R.id.tv_type)
        val tvTime = holder!!.getView<TextView>(R.id.tv_time)
        val tvTransAmount = holder!!.getView<TextView>(R.id.tv_trans_amount)

        when (data!!.changeType) {
            1 -> { // 支付宝支付
                ivType.setImageResource(R.mipmap.icon_pay_ali)
                tvTransAmount.setTextColor(tvTransAmount.resources.getColor(R.color.red_ff3737))
            }
            8 -> { // 微信充值
                ivType.setImageResource(R.mipmap.icon_pay_wx_recharge)
                tvTransAmount.setTextColor(tvTransAmount.resources.getColor(R.color.red_ff3737))
            }

            7 -> { // 微信充值
                ivType.setImageResource(R.mipmap.icon_pay_wx_recharge)
                tvTransAmount.setTextColor(tvTransAmount.resources.getColor(R.color.red_ff3737))
            }

            2 -> { //乘车
                tvTransAmount.setTextColor(tvTransAmount.resources.getColor(R.color.red_ff3737))
                ivType.setImageResource(R.mipmap.icon_order_bus)
            }
            98 -> { // 老版本充值
                tvTransAmount.setTextColor(tvTransAmount.resources.getColor(R.color.red_ff3737))
                ivType.setImageResource(R.mipmap.icon_recharge_old)
            }
            99 -> { // 老版本消费
                tvTransAmount.setTextColor(tvTransAmount.resources.getColor(R.color.black_263238))
                ivType.setImageResource(R.mipmap.icon_consumer_old)
            }
            101 -> { // 老版本客服处理
                tvTransAmount.setTextColor(tvTransAmount.resources.getColor(R.color.black_263238))
                ivType.setImageResource(R.mipmap.icon_pay_kefu)
            }
            3 -> {
                tvTransAmount.setTextColor(tvTransAmount.resources.getColor(R.color.black_263238))
                ivType.setImageResource(R.mipmap.icon_order_qr)
            }
            4 -> {
                tvTransAmount.setTextColor(tvTransAmount.resources.getColor(R.color.black_263238))
                ivType.setImageResource(R.mipmap.icon_order_bus)
            }
        }

        tvType.text = data!!.changeTypeName
        tvTime.text = data!!.changeTime
            when {
                data.amount == 0.0 ->  tvTransAmount.text = AmountUtil.formatTosepara(data.amount.toString())
                data.amount > 0 ->  tvTransAmount.text = "+" + AmountUtil.formatTosepara(data.amount.toString())
                else ->  tvTransAmount.text = AmountUtil.formatTosepara(data.amount.toString())
            }
    }

    override fun onClick(
        view: View?,
        model: MemberAccountHistoryBean.MemberAccountHistoryListBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}