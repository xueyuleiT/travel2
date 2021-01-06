package com.jtcxw.glcxw.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.JsonObject
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.RecruitBean
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.ui.customized.RecruitFragment

class RecruitAdapter(context: Context, fragment: RecruitFragment, date: List<RecruitBean.RecruitListBean>): BaseRecyclerAdapter<RecruitBean.RecruitListBean>(context,date) {

    var mFragment = fragment
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_recruit
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: RecruitBean.RecruitListBean?,
        position: Int
    ) {
        val tvTime = holder!!.getView<TextView>(R.id.tv_time)
        val tvWant = holder!!.getView<TextView>(R.id.tv_want)
        val tvStart = holder!!.getView<TextView>(R.id.tv_start)
        val tvEnd = holder!!.getView<TextView>(R.id.tv_end)
        val tvJoinedCount = holder!!.getView<TextView>(R.id.tv_joined_count)
        val tvMinUserCount = holder!!.getView<TextView>(R.id.tv_min_user_count)

        tvTime.text = data!!.start_time
        tvStart.text = data!!.start_station_name
        tvEnd.text = data!!.end_station_name
        tvJoinedCount.text = data!!.joined_count.toString()
        tvMinUserCount.text = "成行人数:" + data!!.min_user_count.toString()
        tvTime.text = data!!.start_time

        if(data!!.join_state == "Y"){
            tvWant.text = "已报名"
            tvWant.setOnClickListener(null)
            tvWant.setTextColor(mContext.resources.getColor(R.color.black_263238))
            tvWant.setBackgroundResource(R.drawable.shape_r5_c_gray_light)
        } else {
            tvWant.text = "我要报名"
            tvWant.setTextColor(mContext.resources.getColor(R.color.white))
            tvWant.setBackgroundResource(R.drawable.shape_r5_c_green)
        }

        tvWant.setOnClickListener {
            if(data!!.join_state == "Y"){
                mFragment.showConfirmDialog("确认取消报名","确定要取消("+data!!.start_station_name+"到"+data!!.end_station_name+")吗？","取消报名","取消",object :DialogCallback{
                    override fun invoke(p1: MaterialDialog) {
                        val json = JsonObject()
                        json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
                        json.addProperty("RecruitId", data!!.recruit_id)
                        mFragment.mPresenter!!.cancelRejoinRecruit(json)
                    }

                },null)
            } else {
                mFragment.showConfirmDialog("确认报名","确定要报名("+data!!.start_station_name+"到"+data!!.end_station_name+")吗？","报名","取消",object :DialogCallback{
                    override fun invoke(p1: MaterialDialog) {
                        val json = JsonObject()
                        json.addProperty("MemberId", UserUtil.getUserInfoBean().memberId)
                        json.addProperty("RecruitId", data!!.recruit_id)
                        mFragment.mPresenter!!.joinRecruit(json)
                    }

                },null)
            }
        }

    }

    override fun onClick(
        view: View?,
        model: RecruitBean.RecruitListBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}