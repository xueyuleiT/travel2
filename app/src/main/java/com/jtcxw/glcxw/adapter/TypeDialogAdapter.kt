package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.localbean.KVBean

class TypeDialogAdapter(context: Context, data: List<KVBean>): BaseRecyclerAdapter<KVBean>(context, data)  {

    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_type
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: KVBean?,
        position: Int
    ) {

        val tvName = holder!!.getView<TextView>(R.id.tv_name)
        val ivChecked = holder!!.getView<ImageView>(R.id.iv_checked)

        tvName.text = data!!.value



        if (data!!.checked) {
            ivChecked.setImageResource(R.mipmap.icon_trans_confirm)
        } else {
            ivChecked.setImageDrawable(null)
        }
    }



    override fun onClick(
        view: View?,
        model: KVBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}