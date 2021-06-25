package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glcxw.lib.util.AmountUtil
import com.google.android.flexbox.FlexDirection.ROW
import com.google.android.flexbox.FlexWrap.WRAP
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent.FLEX_START
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.SiteOrLineBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.listeners.InnerClickListener
import com.jtcxw.glcxw.localbean.BusSiteOrLineBean
import com.jtcxw.glcxw.localmodel.Tag

class BusStationLineAdapter(
    context: Context,
    data: List<BusSiteOrLineBean>,
    innerClickListener: InnerClickListener
) : BaseRecyclerAdapter<BusSiteOrLineBean>(context, data) {

    var mInnerClickListener = innerClickListener
    override fun onClick(
        view: View?,
        model: BusSiteOrLineBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {

    }


    override fun getItemViewType(position: Int): Int {
        return mDatas[position].type
    }

    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_bus
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: BusSiteOrLineBean?, position: Int) {
        when {
            getItemViewType(position) == 0 -> { //0 站点site 1 线路line
                val tvStation = holder!!.getView<TextView>(R.id.tv_station)
                val tvDistance = holder!!.getView<TextView>(R.id.tv_distance)
                tvStation.text = data!!.siteDataBean.stationName
                if (data!!.siteDataBean.distance < 1000) {
                    tvDistance.text = data!!.siteDataBean.distance.toString() + "米"
                } else {
                    tvDistance.text = AmountUtil.formatDouble(data!!.siteDataBean.distance * 1.0 / 1000,2).toString() + "公里"
                }
                val rlBottom  = holder!!.getView<RelativeLayout>(R.id.rl_bottom)
                val ivArrow = holder!!.getView<ImageView>(R.id.iv_arrow)
                val divider = holder!!.getView<View>(R.id.v_divider)

                val recyclerView = holder!!.getView<RecyclerView>(R.id.recycler_view)
                val tagView = holder!!.getView<RecyclerView>(R.id.tag_view)
                val manager = LinearLayoutManager(recyclerView.context)


                ivArrow.setOnClickListener {
                    data!!.isFold = !data!!.isFold
                   updateItem(position)
                }

                if (data!!.siteDataBean.stationLineInfo.size > 0){
                    rlBottom.visibility = View.VISIBLE
                    divider.visibility = View.VISIBLE
                }else {
                    rlBottom.visibility = View.GONE
                    divider.visibility = View.GONE
                }

                // 如果站点的状态是打开则展示列表 否则展示折叠状态
                if (!data!!.isFold) {
                    ivArrow.setImageResource(R.mipmap.icon_arrow_up_green)
                    recyclerView.layoutManager = manager
                    initAdapter(recyclerView,data!!.siteDataBean.stationLineInfo,position)
                    tagView.visibility = View.GONE
                } else if (data!!.siteDataBean.stationLineInfo.size > 0) {
                    ivArrow.setImageResource(R.mipmap.icon_arrow_down_green)
                    recyclerView.adapter = BusQueryInnerAdapter(recyclerView.context,ArrayList())
                    tagView.visibility = View.VISIBLE
                    val list = ArrayList<Tag>()
                    data!!.siteDataBean.stationLineInfo.forEach {
                        var has = false
                        list.forEach { it_ ->
                            if (it_.text == it.lineName) {
                                has = true
                            }
                        }
                        if (!has) {
                            val tag = Tag(it.lineName)
                            tag.background =
                                tagView.resources.getDrawable(R.drawable.shape_r2_c_bdc8ce)
                            tag.tagTextColor = tagView.resources.getColor(R.color.gray_6)
                            list.add(tag)
                        }
                    }
                    val manager = FlexboxLayoutManager(mContext)
                    manager.flexDirection = ROW
                    manager.flexWrap = WRAP
                    manager.justifyContent = FLEX_START
                    tagView.layoutManager = manager
                    val adapter = TagAdapter(mContext,list)
                    tagView.adapter = adapter
                }
            }
            // 1表示线路 直接展示线路列表
            getItemViewType(position) == 1 -> {//展开
                val tvStation = holder!!.getView<TextView>(R.id.tv_station)
                val recyclerView = holder!!.getView<RecyclerView>(R.id.recycler_view)
                val tagView = holder!!.getView<RecyclerView>(R.id.tag_view)
                val ivArrow = holder!!.getView<View>(R.id.iv_arrow)
                tagView.visibility = View.GONE
                ivArrow.visibility = View.GONE

                tvStation.text = data!!.lineDateBean.lineName
                recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
                if (data.lineDateBean.lineDirection.size > 0) {
                    val adapter = LineInnerAdapter(recyclerView.context,data!!.lineDateBean.lineDirection)
                    adapter.setOnItemClickListener(object :OnItemClickListener<SiteOrLineBean.LineDateBean.LineDirectionBean>{
                        override fun onItemClick(
                            view: View?,
                            model: SiteOrLineBean.LineDateBean.LineDirectionBean?,
                            pos: Int
                        ) {
                            mInnerClickListener.onInnerClickListener(pos,position)
                        }

                        override fun onItemLongClick(
                            view: View?,
                            model: SiteOrLineBean.LineDateBean.LineDirectionBean?,
                            position: Int
                        ) {
                        }

                    })
                    recyclerView.adapter = adapter
                }else {
                    recyclerView.adapter = LineInnerAdapter(recyclerView.context,ArrayList())

                }

            }

        }



    }

    private fun initAdapter(recyclerView: RecyclerView, list:List<SiteOrLineBean.SiteDataBean.StationLineInfoBean>, position: Int) {
        val adapter = BusQueryInnerAdapter(recyclerView.context,list)
        adapter.setOnItemClickListener(object : OnItemClickListener<SiteOrLineBean.SiteDataBean.StationLineInfoBean> {
            override fun onItemClick(
                view: View?,
                model: SiteOrLineBean.SiteDataBean.StationLineInfoBean?,
                pos: Int
            ) {
                mInnerClickListener.onInnerClickListener(pos,position)
            }

            override fun onItemLongClick(
                view: View?,
                model: SiteOrLineBean.SiteDataBean.StationLineInfoBean?,
                position: Int
            ) {
            }

        })
        recyclerView.adapter = adapter
    }

}