package com.jtcxw.glcxw.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.GoodListBean
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder

class GoodListAdapter(context: Context, list: List<GoodListBean.GoodsListBean>):BaseRecyclerAdapter<GoodListBean.GoodsListBean>(context,list) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_goods
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: GoodListBean.GoodsListBean?,
        position: Int
    ) {
        val tvPrice = holder!!.getView<TextView>(R.id.tv_price)
        tvPrice.text = data!!.price.toString() + "元"
        if (data!!.isChecked) {
            tvPrice.setBackgroundResource(R.drawable.shape_r2_cw_b1)
        } else {
            tvPrice.setBackgroundResource(R.drawable.shape_r2_b1_cgreen)
        }

        if (data!!.goodsType == "自主定价") {
            tvPrice.text = data!!.goodsType
        } else {

        }
    }

    override fun onClick(
        view: View?,
        model: GoodListBean.GoodsListBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}