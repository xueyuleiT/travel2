package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean
import com.jtcxw.glcxw.base.respmodels.StationBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.localbean.BusStationBean

class BusQueryInnerAdapter(context: Context, data: List<SiteOrLineBean.SiteDataBean.StationLineInfoBean>): BaseRecyclerAdapter<SiteOrLineBean.SiteDataBean.StationLineInfoBean>(context, data)  {

    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_bus_inner
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: SiteOrLineBean.SiteDataBean.StationLineInfoBean?, position: Int) {
        val tvBusName = holder!!.getView<TextView>(R.id.tv_bus_name)
        val tvTo = holder!!.getView<TextView>(R.id.tv_to)
        val tvStart = holder!!.getView<TextView>(R.id.tv_star)
        val tvEnd = holder!!.getView<TextView>(R.id.tv_end)

        tvBusName.text = data!!.lineName
            tvTo.text = data!!.directionLineName
            tvStart.text = data!!.startTime
            tvEnd.text = data!!.lastTime

    }

    override fun onClick(
        view: View?,
        model: SiteOrLineBean.SiteDataBean.StationLineInfoBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}