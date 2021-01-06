package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.LineBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import me.kaede.tagview.Tag
import me.kaede.tagview.TagView

class CustomizedBusAdapter(context: Context,list: List<LineBean.RouteListBean>) : BaseRecyclerAdapter<LineBean.RouteListBean>(context, list) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_bus_customized
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: LineBean.RouteListBean?, position: Int) {
        val tvLineNo = holder!!.getView<TextView>(R.id.tv_line_no)
        val tvTitle = holder!!.getView<TextView>(R.id.tv_title)
        val tvStationFrom = holder!!.getView<TextView>(R.id.tv_station_from)
        val tvStationTo = holder!!.getView<TextView>(R.id.tv_station_to)
        val tagView = holder!!.getView<TagView>(R.id.tag_view)
        val tvTip = holder!!.getView<TextView>(R.id.tv_tip)
        val llTip = holder!!.getView<LinearLayout>(R.id.ll_tip)
        val llVisit = holder!!.getView<LinearLayout>(R.id.ll_visit)
        val ivArrowVisit =  holder!!.getView<ImageView>(R.id.iv_arrow_visit)
        val ivArrowTip =  holder!!.getView<ImageView>(R.id.iv_arrow_tip)

        tvTitle.text = data!!.route_name
        tvLineNo.text = data!!.route_no
        tvStationFrom.text = data!!.start_station_name
        tvStationTo.text = data!!.end_station_name
        if (data!!.openTip) {
            ivArrowTip.setImageResource(R.mipmap.icon_arrow_up_green)
            tvTip.visibility = View.VISIBLE
        } else {
            ivArrowTip.setImageResource(R.mipmap.icon_arrow_down_green)
            tvTip.visibility = View.GONE
        }

        if (data!!.openVisit) {
            ivArrowVisit.setImageResource(R.mipmap.icon_arrow_up_green)
            tagView.visibility = View.VISIBLE
            tagView.removeAllTags()
            val list = ArrayList<Tag>()
            data!!.vis_list.forEach {
                val tag = Tag(it)
                tag.background = tagView.resources.getDrawable(R.drawable.shape_r2_c_bdc8ce)
                tag.tagTextColor = tagView.resources.getColor(R.color.gray_6)
                list.add(tag)
            }
            tagView.addTags(list)
        } else {
            ivArrowVisit.setImageResource(R.mipmap.icon_arrow_down_green)
            tagView.visibility = View.GONE
        }

        tvTip.text = data!!.route_remark

        llVisit.setOnClickListener{
            data!!.openVisit = !data!!.openVisit
           updateItem(position)
        }

        llTip.setOnClickListener {
            data!!.openTip = !data!!.openTip
            updateItem(position)
        }

    }

    override fun onClick(
        view: View?,
        model: LineBean.RouteListBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}