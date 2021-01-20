package com.jtcxw.glcxw.adapter

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.localmodel.Tag

class TagAdapter(context: Context, list: List<Tag>) : BaseRecyclerAdapter<Tag>(context,list) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_tag
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: Tag?, position: Int) {
        val tvTitle = holder!!.getView<TextView>(R.id.tv_tab_title)

        tvTitle.text = data!!.text
        tvTitle.setTextColor(data!!.tagTextColor)
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, data!!.tagTextSize)
        tvTitle.background = data!!.background
    }


    override fun onClick(
        view: View?,
        model: Tag?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}