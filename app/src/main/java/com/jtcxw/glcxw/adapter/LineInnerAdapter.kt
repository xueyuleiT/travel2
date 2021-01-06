package com.jtcxw.glcxw.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean
import com.jtcxw.glcxw.base.respmodels.StationBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.localbean.BusSiteOrLineBean

class LineInnerAdapter (context: Context, data: List<SiteOrLineBean.LineDateBean.LineDirectionBean>) : BaseRecyclerAdapter<SiteOrLineBean.LineDateBean.LineDirectionBean>(context, data) {
    override fun onClick(
        view: View?,
        model: SiteOrLineBean.LineDateBean.LineDirectionBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {

    }


    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_bus_direction
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: SiteOrLineBean.LineDateBean.LineDirectionBean?, position: Int) {

        val tvTo = holder!!.getView<TextView>(R.id.tv_to)
        val tvStart = holder!!.getView<TextView>(R.id.tv_star)
        val tvEnd = holder!!.getView<TextView>(R.id.tv_end)

        tvTo.text = data!!.directionLineName
        tvStart.text = data!!.startTime
        tvEnd.text = data!!.lastTime

    }
}