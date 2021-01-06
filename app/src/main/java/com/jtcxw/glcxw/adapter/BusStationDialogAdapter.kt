package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.FrequencyBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder

class BusStationDialogAdapter(context: Context, data: List<FrequencyBean.scheduleListBean.RideStationsBean>): BaseRecyclerAdapter<FrequencyBean.scheduleListBean.RideStationsBean>(context, data)  {

    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_bus_station_checek
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: FrequencyBean.scheduleListBean.RideStationsBean?,
        position: Int
    ) {

        val tvName = holder!!.getView<TextView>(R.id.tv_name)
        val ivChecked = holder!!.getView<ImageView>(R.id.iv_checked)
        val ride = holder!!.getView<ImageView>(R.id.iv_ride)

        tvName.text = data!!.station_name + "（" + data!!.ride_time + "）"

        if (data!!.type == 0) {
            if (position == 0) {
                ride.setImageResource(R.mipmap.icon_trans_start)
            } else {
                ride.setImageResource(R.mipmap.icon_station_ride)
            }
        } else {
            if (position == mDatas.size - 1) {
                ride.setImageResource(R.mipmap.icon_trans_end)
            } else {
                ride.setImageResource(R.mipmap.icon_station_reach)
            }
        }

        if (data!!.isChecked) {
            ivChecked.setImageResource(R.mipmap.icon_trans_confirm)
        } else {
            ivChecked.setImageDrawable(null)
        }
    }



    override fun onClick(
        view: View?,
        model: FrequencyBean.scheduleListBean.RideStationsBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}