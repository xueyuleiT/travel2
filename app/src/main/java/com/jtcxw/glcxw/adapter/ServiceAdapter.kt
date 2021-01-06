package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.CusServerBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder

class ServiceAdapter(context: Context,list:List<CusServerBean>):BaseRecyclerAdapter<CusServerBean>(context,list) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_complaint
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: CusServerBean?, position: Int) {
        val tvTitle = holder!!.getView<TextView>(R.id.tv_title)
        val tvDetail = holder!!.getView<TextView>(R.id.tv_detail)
        val tvTime = holder!!.getView<TextView>(R.id.tv_time)
        val tvSysTitle = holder!!.getView<TextView>(R.id.tv_sys_title)
        val tvSysReply = holder!!.getView<TextView>(R.id.tv_sys_reply)

        tvTitle.text = data!!.customerServerTypeName
        tvDetail.text = data.ask
        tvTime.text = data!!.askTime
        tvSysReply.text = data.answer
    }

    override fun onClick(
        view: View?,
        model: CusServerBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}