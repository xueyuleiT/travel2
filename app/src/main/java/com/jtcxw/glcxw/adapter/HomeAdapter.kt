package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.localbean.HomeItem

class HomeAdapter(context: Context,data: List<HomeItem>) : BaseRecyclerAdapter<HomeItem>(context, data) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_home
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: HomeItem?, position: Int) {
        val icon = holder!!.convertView.findViewById<ImageView>(R.id.icon)
        val text = holder!!.convertView.findViewById<TextView>(R.id.text)
        icon.setImageResource(data!!.res!!)
        text.text = data!!.text!!
    }

    override fun onClick(
        view: View?,
        model: HomeItem?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {

    }
}