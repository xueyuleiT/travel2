package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.PayListBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder

class PayListAdapter(
    context: Context,
    date: List<PayListBean.PayBean>,
    helper: ItemTouchHelper?
):
    BaseRecyclerAdapter<PayListBean.PayBean>(context,date) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_pay_list
    }

    private var mHelper = helper
    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: PayListBean.PayBean?,
        position: Int
    ) {
        val tvName = holder!!.getView<TextView>(R.id.tv_name)
        val vDrag = holder!!.getView<ImageView>(R.id.v_drag)
        tvName.text = data!!.payTypeName

        vDrag.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                mHelper!!.startDrag(holder)
            }
            false
        }

    }

    override fun onClick(
        view: View?,
        model: PayListBean.PayBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}