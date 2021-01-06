package com.jtcxw.glcxw.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import java.text.SimpleDateFormat


class BusStationInnerAdapter(context: Context, data: List<AnnexBusBean.StationListBean.StationLineInfoBean>): BaseRecyclerAdapter<AnnexBusBean.StationListBean.StationLineInfoBean>(context, data)  {

    private val mSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_station_bus
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: AnnexBusBean.StationListBean.StationLineInfoBean?, position: Int) {
        val tvBusName = holder!!.getView<TextView>(R.id.tv_bus_name)
        val tvTo = holder!!.getView<TextView>(R.id.tv_to)
        val tvTime = holder!!.getView<TextView>(R.id.tv_time)
        val divider = holder!!.getView<View>(R.id.v_divider)
        val vTime = holder!!.getView<ImageView>(R.id.v_time)
        val vHeart = holder!!.getView<ImageView>(R.id.iv_heart)

        tvBusName.text = data!!.lineName
            tvTo.text = data!!.directionLineName


        if(data!!.isCollection == 0) {
            vHeart.setImageResource(R.mipmap.icon_heart)
        } else {
            vHeart.setImageResource(R.mipmap.icon_heart_red)
        }

        vTime.visibility = View.VISIBLE
        val ob = vTime.background
        val anim = ob as AnimationDrawable
        anim.start()

        if (!TextUtils.isEmpty(data.lastUpdTime)) {
            val marginTime =
                (System.currentTimeMillis() - mSdf.parse(data.lastUpdTime).time) / (1000 * 60)
            if (marginTime > 5) {
                tvTime.text = "车辆无信号"
            } else if (marginTime <= 5) {
                if (TextUtils.isEmpty(data!!.forecastTime)) {
                    tvTime.text = "等待发车"
                } else if (data!!.forecastTime.toInt() <= 1) {
                    tvTime.text = "即将到站"
                } else if (data!!.forecastTime.toInt() > 1) {
                    if (!TextUtils.isEmpty(data.nextLevel)) {
                        tvTime.text =
                            data.forecastTime + "分钟-" + (data.level - data.nextLevel.toInt() + 1) + "站"
                    }
                }
            }

        } else {
            tvTime.text = "等待发车"
        }


    }

    override fun onClick(
        view: View?,
        model: AnnexBusBean.StationListBean.StationLineInfoBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}