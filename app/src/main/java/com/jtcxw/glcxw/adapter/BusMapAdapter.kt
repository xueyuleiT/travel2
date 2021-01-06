package com.jtcxw.glcxw.adapter

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cunoraz.tagview.TagView
import com.glcxw.lib.util.AmountUtil
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.listeners.CollectCancelCallback
import com.jtcxw.glcxw.listeners.InnerClickListener
import me.yokeyword.fragmentation.SupportFragment

class BusMapAdapter(fragment: SupportFragment, data: List<AnnexBusBean.StationListBean>,innerClickListener: InnerClickListener,collectCancelCallback: CollectCancelCallback): BaseRecyclerAdapter<AnnexBusBean.StationListBean>(fragment.context, data)  {
    var mFragment = fragment
    var mSelected = 0
    var mInnerClickListener = innerClickListener
    var mCollectCancelCallback = collectCancelCallback
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_station
    }
    
    open fun setSelected(pos:Int){
        this.mSelected = pos
        notifyAllItems()
    }

    override fun getItemViewType(position: Int): Int {
        return mDatas[position].type
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: AnnexBusBean.StationListBean?, position: Int) {

        val recyclerView = holder!!.getView<RecyclerView>(R.id.recycler_view)
        val tvStation = holder!!.getView<TextView>(R.id.tv_station)
        val tvDistance = holder!!.getView<TextView>(R.id.tv_distance)
        val tagView = holder!!.getView<TagView>(R.id.tag_view)
        val rlBottom  = holder!!.getView<RelativeLayout>(R.id.rl_bottom)
        val ivArrow = holder!!.getView<ImageView>(R.id.iv_arrow)
        val vHeart = holder!!.getView<ImageView>(R.id.v_heart)

        vHeart.setOnClickListener(null)
        if (data!!.stopList != null && data!!.stopList.isNotEmpty()) {
            if (data!!.stopList[mSelected].isCollection == 0) {
                vHeart.setImageResource(R.mipmap.icon_heart)
            } else {
                vHeart.setImageResource(R.mipmap.icon_heart_red)
            }

            vHeart.setOnClickListener {
                if (data!!.stopList[mSelected].isCollection == 1) {
                    mCollectCancelCallback.onDialogCallback(data!!.stopList[mSelected].isCollection, data!!.stopList[mSelected].collectionId)
                } else {
                    mCollectCancelCallback.onDialogCallback(data!!.stopList[mSelected].isCollection, data!!.stopList[mSelected].stopId)
                }
            }
        }

        tvStation.text = data!!.stopList[mSelected].stopName
        if (data!!.stopList[mSelected].distance < 1000) {
            tvDistance.text = data!!.stopList[mSelected].distance.toString() + "米"
        } else {
            tvDistance.text = AmountUtil.formatDouble(data!!.stopList[mSelected].distance * 1.0 / 1000,2).toString() + "公里"
        }
        val manager = LinearLayoutManager(recyclerView.context)
        rlBottom.visibility = View.GONE

        recyclerView.layoutManager = manager
        val list = ArrayList<AnnexBusBean.StationListBean.StationLineInfoBean>()

        data!!.stationLineInfo.forEach {
            if (it.stopId == data!!.stopList[mSelected].stopId) {
                list.add(it)
            }
        }

        val adapter = BusInnerAdapter(recyclerView.context,list)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object :OnItemClickListener<AnnexBusBean.StationListBean.StationLineInfoBean>{
            override fun onItemClick(
                view: View?,
                model: AnnexBusBean.StationListBean.StationLineInfoBean?,
                pos: Int
            ) {
                if (mInnerClickListener != null) {
                    mInnerClickListener.onInnerClickListener(pos,position)
                }
            }

            override fun onItemLongClick(
                view: View?,
                model: AnnexBusBean.StationListBean.StationLineInfoBean?,
                position: Int
            ) {
            }

        })
    }


    override fun onClick(
        view: View?,
        model: AnnexBusBean.StationListBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}