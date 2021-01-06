package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder

class BusStationInnerFoldAdapter(context: Context, data: List<SiteOrLineBean.SiteDataBean.StationLineInfoBean>): BaseRecyclerAdapter<SiteOrLineBean.SiteDataBean.StationLineInfoBean>(context, data)   {
    override fun getConvertViewId(viewType: Int): Int {
       return R.layout.item_station_inner_fold
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: SiteOrLineBean.SiteDataBean.StationLineInfoBean?, position: Int) {
        val tvLineName = holder!!.getView<TextView>(R.id.tv_line_name)
        tvLineName.text = data!!.lineName
    }

    override fun onClick(
        view: View?,
        model: SiteOrLineBean.SiteDataBean.StationLineInfoBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}