package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.PayTypeBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder

class OrderPayAdapter(context: Context,date:List<PayTypeBean.TypeArrayBean>):BaseRecyclerAdapter<PayTypeBean.TypeArrayBean>(context,date) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_pay
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: PayTypeBean.TypeArrayBean?,
        position: Int
    ) {
        val tvType = holder!!.getView<TextView>(R.id.tv_type)
        val vCheck = holder!!.getView<ImageView>(R.id.v_check)
        val vType = holder!!.getView<ImageView>(R.id.v_type)

        if (data!!.itemValue == "1"){
            vType.setImageResource(R.mipmap.icon_pay_account)
            tvType.text = "${data!!.itemName}（${UserUtil.getUser().userInfoBean.ownerAmount}元）"
        } else if (data!!.itemValue == "2") {
            tvType.text = data!!.itemName
            vType.setImageResource(R.mipmap.icon_pay_ali)
        } else {
            tvType.text = data!!.itemName
            vType.setImageResource(R.mipmap.icon_pay_other)
            tvType.text = data!!.itemName
        }

        if (data!!.isChecked) {
            vCheck.setImageResource(R.mipmap.icon_choose_checked)
        } else {
            vCheck.setImageResource(R.mipmap.icon_choose_nor)
        }
    }

    override fun onClick(
        view: View?,
        model: PayTypeBean.TypeArrayBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}