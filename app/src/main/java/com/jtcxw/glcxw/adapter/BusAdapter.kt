package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glcxw.lib.util.AmountUtil
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.listeners.InnerClickListener
import me.kaede.tagview.Tag
import me.kaede.tagview.TagView

class BusAdapter(context: Context, data: List<AnnexBusBean.StationListBean>,innerClickListener:InnerClickListener): BaseRecyclerAdapter<AnnexBusBean.StationListBean>(context, data)  {

    private var mInnerClickListener = innerClickListener
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_bus
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: AnnexBusBean.StationListBean?,
        position: Int
    ) {
        val recyclerView = holder!!.getView<RecyclerView>(R.id.recycler_view)
        val tvStation = holder!!.getView<TextView>(R.id.tv_station)
        val tvDistance = holder!!.getView<TextView>(R.id.tv_distance)
        val tagView = holder!!.getView<TagView>(R.id.tag_view)
        val rlBottom  = holder!!.getView<RelativeLayout>(R.id.rl_bottom)
        val ivArrow = holder!!.getView<ImageView>(R.id.iv_arrow)
        val divider = holder!!.getView<View>(R.id.v_divider)

        tvStation.text = data!!.stationName
        if (data!!.distance < 1000) {
            tvDistance.text = data!!.distance.toString() + "米"
        } else {
            tvDistance.text = AmountUtil.formatDouble(data!!.distance * 1.0 / 1000,2).toString() + "公里"
        }
        val manager = LinearLayoutManager(recyclerView.context)

        ivArrow.setOnClickListener {
            data!!.isFold = !data!!.isFold
            updateItem(position)
        }

        if (data!!.stationLineInfo.size > 0){
            rlBottom.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
        }else {
            rlBottom.visibility = View.GONE
            divider.visibility = View.GONE
        }

        if (!data!!.isFold) {
            ivArrow.setImageResource(R.mipmap.icon_arrow_up_green)
            recyclerView.layoutManager = manager
            initAdapter(recyclerView,data!!.stationLineInfo,position)
            tagView.visibility = View.GONE
            tagView.removeAllTags()
        } else if (data!!.stationLineInfo.size > 0) {
            ivArrow.setImageResource(R.mipmap.icon_arrow_down_green)
            recyclerView.adapter = BusInnerAdapter(recyclerView.context,ArrayList())
            tagView.visibility = View.VISIBLE
            tagView.removeAllTags()
            val list = ArrayList<Tag>()
            data!!.stationLineInfo.forEach {
                var has = false
                list.forEach { it_ ->
                    if (it_.text == it.lineName) {
                        has = true
                    }
                }
                if (!has) {
                    val tag = Tag(it.lineName)
                    tag.background = tagView.resources.getDrawable(R.drawable.shape_r2_c_bdc8ce)
                    tag.tagTextColor = tagView.resources.getColor(R.color.gray_6)
                    tag.tagTextSize = 12f
                    list.add(tag)
                }
            }
            tagView.addTags(list)
        } else {
            ivArrow.setImageBitmap(null)
        }

    }

    private fun initAdapter(recyclerView: RecyclerView, list:List<AnnexBusBean.StationListBean.StationLineInfoBean>, position: Int) {
        val adapter = BusInnerAdapter(recyclerView.context,list)
        adapter.setOnItemClickListener(object : OnItemClickListener<AnnexBusBean.StationListBean.StationLineInfoBean> {
            override fun onItemClick(
                view: View?,
                model: AnnexBusBean.StationListBean.StationLineInfoBean?,
                pos: Int
            ) {
                mInnerClickListener.onInnerClickListener(pos,position)
            }

            override fun onItemLongClick(
                view: View?,
                model: AnnexBusBean.StationListBean.StationLineInfoBean?,
                position: Int
            ) {
            }

        })
        recyclerView.adapter = adapter
    }

    override fun onClick(
        view: View?,
        model: AnnexBusBean.StationListBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}