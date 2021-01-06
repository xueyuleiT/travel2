package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.localbean.BusSiteOrLineHistoryBean

class BusHistoryAdapter(
    context: Context,
    data: List<BusSiteOrLineHistoryBean>
) : BaseRecyclerAdapter<BusSiteOrLineHistoryBean>(context, data) {

    override fun onClick(
        view: View?,
        data: BusSiteOrLineHistoryBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }


    override fun getItemViewType(position: Int): Int {
        return mDatas[position].type
    }

    override fun getConvertViewId(viewType: Int): Int {
        if ( viewType == 1) {
            return R.layout.item_bus_history_line
        }else {
            return R.layout.item_bus_history_station
        }
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: BusSiteOrLineHistoryBean?, position: Int) {
        when {
            getItemViewType(position) == 0 -> {
                val tvStation = holder!!.getView<TextView>(R.id.tv_name)
                tvStation.text = data!!.stationName

            }
            getItemViewType(position) == 1 -> {
                val tvLine = holder!!.getView<TextView>(R.id.tv_line)
                val tvTo = holder!!.getView<TextView>(R.id.tv_to)

                tvTo.visibility = View.GONE
                holder!!.getView<View>(R.id.tv_go_to).visibility = View.GONE
                tvLine.text = data!!.lineName
                tvTo.text = data!!.lineDirection.directionLineName
            }

        }



    }


}