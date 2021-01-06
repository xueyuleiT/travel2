//package com.jtcxw.glcxw.adapter
//
//import android.content.Context
//import android.text.TextUtils
//import android.view.View
//import android.widget.TextView
//import com.jtcxw.glcxw.R
//import com.jtcxw.glcxw.base.respmodels.ScenicDetailBean
//import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
//import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
//import com.tencent.smtt.sdk.WebView
//
//class ScenicDetailAdapter(context: Context,list: List<ScenicDetailBean.ScenicMsgListBean>):BaseRecyclerAdapter<ScenicDetailBean.ScenicMsgListBean>(context,list) {
//    override fun getConvertViewId(viewType: Int): Int {
//        return R.layout.item_scenic_detail
//    }
//
//    override fun convert(
//        holder: CommonRecyclerViewHolder?,
//        data: ScenicDetailBean.ScenicMsgListBean?,
//        position: Int
//    ) {
//        val tvTitle = holder!!.getView<TextView>(R.id.tv_title)
//        val tvScenicDetail = holder!!.getView<WebView>(R.id.tv_scenic_detail)
//        val tvPhone = holder!!.getView<TextView>(R.id.tv_phone)
//
//        tvTitle.text = data!!.type
//        tvScenicDetail.loadDataWithBaseURL(null,data!!.introduce, "text/html" , "utf-8", null)
//
//        if (!TextUtils.isEmpty(data!!.tel)) {
//            tvPhone.visibility = View.VISIBLE
//            tvPhone.text = data.tel
//        } else {
//            tvPhone.visibility = View.GONE
//        }
//    }
//
//    override fun onClick(
//        view: View?,
//        model: ScenicDetailBean.ScenicMsgListBean?,
//        position: Int,
//        holder: CommonRecyclerViewHolder?
//    ) {
//    }
//}