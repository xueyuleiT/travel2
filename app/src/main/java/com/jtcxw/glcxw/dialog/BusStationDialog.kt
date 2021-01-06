package com.jtcxw.glcxw.dialog

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BaseBottomSheetDialogFragment
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.BusStationDialogAdapter
import com.jtcxw.glcxw.base.respmodels.FrequencyBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.RefreshLoadMoreRecyclerView
import com.jtcxw.glcxw.listeners.BusDialogCallback

class BusStationDialog:BaseBottomSheetDialogFragment() {
    override fun getLayoutId(): Int {
        return R.layout.dialog_bus_station
    }
    var mDialogCallback: BusDialogCallback?= null

    fun setCallback(dialogCallback: BusDialogCallback):BusStationDialog {
        mDialogCallback = dialogCallback
        return this
    }

    var mList = ArrayList<FrequencyBean.scheduleListBean.RideStationsBean>()
    var mTitle = ""

    fun setTitle(title:String):BusStationDialog {
        mTitle = title
        return this
    }


    fun setData(list: List<FrequencyBean.scheduleListBean.RideStationsBean>):BusStationDialog {
        mList.clear()
        mList.addAll(list)
        return this
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        tvTitle.text = mTitle

        val recycler = view.findViewById<RefreshLoadMoreRecyclerView>(R.id.recycler_view)
        val adapter = BusStationDialogAdapter(context!!,mList!!)
        adapter.setOnItemClickListener(object :BaseRecyclerAdapter.OnItemClickListener<FrequencyBean.scheduleListBean.RideStationsBean>{
            override fun onItemClick(
                view: View?,
                model: FrequencyBean.scheduleListBean.RideStationsBean?,
                position: Int
            ) {
                mList!!.forEach {
                    it.isChecked = false
                }
                model!!.isChecked = true
                if (mDialogCallback != null) {
                    mDialogCallback!!.onBusDialogCallback(position)
                }
                dismiss()
            }

            override fun onItemLongClick(
                view: View?,
                model: FrequencyBean.scheduleListBean.RideStationsBean?,
                position: Int
            ) {
            }

        })

        recycler.adapter = adapter

    }
}