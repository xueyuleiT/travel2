package com.jtcxw.glcxw.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.localbean.MessageBean
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(context: Context,list:List<MessageBean>):BaseRecyclerAdapter<MessageBean>(context,list) {
    override fun getConvertViewId(viewType: Int): Int {
        return R.layout.item_message
    }

    override fun convert(holder: CommonRecyclerViewHolder?, data: MessageBean?, position: Int) {
        val tvTitle = holder!!.getView<TextView>(R.id.tv_title)
        val tvRead = holder!!.getView<TextView>(R.id.tv_read)
        val tvTime = holder!!.getView<TextView>(R.id.tv_time)
        val tvDetail = holder!!.getView<TextView>(R.id.tv_detail)
        val vType = holder!!.getView<ImageView>(R.id.v_type)

        tvTitle.text = data!!.title
//        tvRead.text =  if(data!!.read == 0) "未读" else "已读"
        tvTime.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(data!!.time.toLong()))
        tvDetail.text = data.detail

        if (!TextUtils.isEmpty(data.PushType) && data.PushType == "2") {
            when {
                data.MessageType == "1" -> {
                    if(data!!.read == 1) {
                        vType.setImageResource(R.mipmap.icon_pay_ali_read)
                    } else {
                        vType.setImageResource(R.mipmap.icon_pay_ali_unread)
                    }
                }
                data.MessageType == "2" -> {
                    if(data!!.read == 1) {
                        vType.setImageResource(R.mipmap.icon_order_bus_read)
                    } else {
                        vType.setImageResource(R.mipmap.icon_order_bus_unread)
                    }
                }
                data.MessageType == "3" -> vType.setImageResource(R.mipmap.icon_order_qr)
                data.MessageType == "4" -> {
                    if(data!!.read == 1) {
                        vType.setImageResource(R.mipmap.icon_order_bus_read)
                    } else {
                        vType.setImageResource(R.mipmap.icon_order_bus_unread)
                    }
                }
            }
        } else {
            vType.setImageResource(R.mipmap.icon_launch)
        }


    }

    override fun onClick(
        view: View?,
        model: MessageBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}