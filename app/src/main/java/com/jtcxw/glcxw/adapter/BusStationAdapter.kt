package com.jtcxw.glcxw.adapter

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.cunoraz.tagview.Tag
import com.cunoraz.tagview.TagView
import com.glcxw.lib.util.AmountUtil
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.respmodels.AnnexBusBean
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.listeners.InnerClickListener
import com.jtcxw.glcxw.ui.BusFragment
import com.jtcxw.glcxw.ui.customized.CustomizedMainFragment
import com.jtcxw.glcxw.ui.login.LoginFragment
import com.jtcxw.glcxw.utils.JumpValid
import com.toptechs.libaction.action.SingleCall
import me.yokeyword.fragmentation.SupportFragment

class BusStationAdapter(
    fragment: SupportFragment,
    data: List<AnnexBusBean.StationListBean>,
    innerClickListener: InnerClickListener
): BaseRecyclerAdapter<AnnexBusBean.StationListBean>(fragment.context, data)  {
    var mFragment = fragment
    var mInnerClickListener = innerClickListener
    override fun getConvertViewId(viewType: Int): Int {
        return if (viewType == 0) {
            R.layout.item_station
        }else {
            R.layout.item_go_travel_header
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mDatas[position].type
    }



    override fun convert(holder: CommonRecyclerViewHolder?, data: AnnexBusBean.StationListBean?, position: Int) {

        if (getItemViewType(position) == 1) {
            val  rlCarSearch = holder!!.getView<View>(R.id.rl_car_search)
            val  tvPhone = holder!!.getView<TextView>(R.id.tv_phone)
            val rlCustomized = holder!!.getView<View>(R.id.rl_customized)
            val rlCarSpot = holder!!.getView<View>(R.id.rl_car_spot)
            val rlFunctionOther = holder!!.getView<View>(R.id.rl_function_other)

            tvPhone.text = data!!.phoneTip
            rlCarSpot.setOnClickListener {
                ToastUtil.toastWaring("功能待开放")
            }
            rlFunctionOther.setOnClickListener {
                mInnerClickListener.onInnerClickListener(3,0)
            }
            rlCustomized.setOnClickListener {
                if (TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
                    LoginFragment.newInstance(mFragment.parentFragment as SupportFragment,null)
                    val jumpValid = JumpValid(CustomizedMainFragment())
                    SingleCall.getInstance().clear()
                    SingleCall.getInstance().addAction(jumpValid).addValid(jumpValid).doCall()
                    return@setOnClickListener
                }
                CustomizedMainFragment.newInstance(mFragment.parentFragment as SupportFragment,null)
            }
            rlCarSearch.setOnClickListener{
                BusFragment.newInstance(mFragment.parentFragment as SupportFragment,null)
            }
            tvPhone.setOnClickListener {
                if (TextUtils.isEmpty(data!!.phoneTip)) {
                    return@setOnClickListener
                }
                val text = data.phoneTip
                MaterialDialog(rlCarSearch.context!!)
                    .title(null, "提示")
                    .message(null, SpannelUtil.getSpannelStr(text,rlCarSearch.context!!.resources.getColor(R.color.red_ff3737),text.indexOf("(") + 1,text.indexOf(")")))
                    .negativeButton(null, SpannelUtil.getSpannelStr("取消",rlCarSearch.context!!.resources.getColor(R.color.blue_3a75f3)))
                    .positiveButton(null, SpannelUtil.getSpannelStr("拨打",rlCarSearch.context!!.resources.getColor(R.color.blue_3a75f3)), object : DialogCallback {
                        override fun invoke(p1: MaterialDialog) {
                            val intent = Intent(Intent.ACTION_DIAL)
                            val phoneNum = data.phoneTip.substring(text.indexOf("(") + 1,text.indexOf(")"))
                            val data = Uri.parse("tel:$phoneNum")
                            intent.data = data
                            mContext!!.startActivity(intent)
                        }
                    })
                    .cornerRadius(DimensionUtil.dpToPx(2), null)
                    .cancelable(false)
                    .show()
            }
            return
        }
        val recyclerView = holder!!.getView<RecyclerView>(R.id.recycler_view)
        val tvStation = holder!!.getView<TextView>(R.id.tv_station)
        val tvDistance = holder!!.getView<TextView>(R.id.tv_distance)
        val tagView = holder!!.getView<TagView>(R.id.tag_view)
        val rlBottom  = holder!!.getView<RelativeLayout>(R.id.rl_bottom)
        val ivArrow = holder!!.getView<ImageView>(R.id.iv_arrow)
        holder.convertView.setOnTouchListener(object :View.OnTouchListener{
            var downTime = 0L
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if (isTouchPointInView(recyclerView,p1!!.rawX, p1!!.rawY)) {
                    if (p1!!.action == MotionEvent.ACTION_DOWN) {
                        downTime = System.currentTimeMillis()
                    } else if (p1!!.action == MotionEvent.ACTION_UP) {
                        if (System.currentTimeMillis() - downTime < 100) {
                            mOnItemClickListener.onItemClick(holder.convertView, data, position)
                        }
                    }
                }
                return false
            }

        })
        tvStation.text = data!!.stationName
        if (data!!.distance < 1000) {
            tvDistance.text = data!!.distance.toString() + "米"
        } else {
            tvDistance.text = AmountUtil.formatDouble(data!!.distance * 1.0 / 1000,2).toString() + "公里"
        }
        val manager = LinearLayoutManager(recyclerView.context)
        ivArrow.setOnClickListener {
            data!!.isFold = !data!!.isFold
            updateItem(position)
        }

        when {
            data!!.stationLineInfo.size > 2 -> {
                rlBottom.visibility = View.VISIBLE
                ivArrow.visibility = View.VISIBLE
            }
            data!!.stationLineInfo.size > 0 -> {
                rlBottom.visibility = View.VISIBLE
                tagView.visibility = View.GONE
                ivArrow.visibility = View.GONE
            }
            else -> {
                rlBottom.visibility = View.GONE
                ivArrow.visibility = View.GONE
            }
        }

        if (!data!!.isFold) {
            ivArrow.setImageResource(R.mipmap.icon_arrow_up_green)
            recyclerView.layoutManager = manager
            initAdapter(this, recyclerView,data!!.stationLineInfo,position)
            tagView.visibility = View.GONE
            tagView.removeAll()
        } else if (data!!.stationLineInfo.size > 2) {
            ivArrow.setImageResource(R.mipmap.icon_arrow_down_green)
            recyclerView.layoutManager = manager
            initAdapter(this, recyclerView,data!!.stationLineInfo.subList(0,2),position)

            tagView.visibility = View.VISIBLE
            tagView.removeAll()
            val list = ArrayList<Tag>()
            data!!.stationLineInfo.subList(2,data!!.stationLineInfo.size).forEach {
                var has = false
                list.forEach { it_ ->
                    if (it_.text == it.lineName) {
                        has = true
                    }
                }
                if (!has) {
                    val tag = Tag(it.lineName)
                    tag.background = tagView.resources.getDrawable(R.drawable.shape_r2_c_bdc8ce)
                    tag.tagTextColor = tagView.resources.getColor(R.color.gray_6)
                    tag.tagTextSize = 12f
                    list.add(tag)
                }
            }
            tagView.addTags(list)
        } else {
            tagView.removeAll()
            if (data!!.stationLineInfo.size > 0) {
                tagView.visibility = View.GONE
                recyclerView.layoutManager = manager
                initAdapter(this, recyclerView,data!!.stationLineInfo,position)
            } else {
                tagView.visibility = View.GONE
                recyclerView.adapter = BusStationInnerAdapter(recyclerView.context,ArrayList())
            }

        }
    }

    private fun isTouchPointInView(view: View?, x: Float, y: Float): Boolean {
        if (view == null) {
            return false
        }
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.measuredWidth
        val bottom = top + view.measuredHeight
        if (y in top..bottom && x in left..right) {
            return true
        }
        return false
    }

    override fun onClick(
        view: View?,
        model: AnnexBusBean.StationListBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }

    private fun initAdapter(
        busStationAdapter: BusStationAdapter,
        recyclerView: RecyclerView, list:List<AnnexBusBean.StationListBean.StationLineInfoBean>, position: Int) {
        val adapter = BusStationInnerAdapter(recyclerView.context,list)
        adapter.setOnItemClickListener(object : OnItemClickListener<AnnexBusBean.StationListBean.StationLineInfoBean> {
            override fun onItemClick(
                view: View?,
                model: AnnexBusBean.StationListBean.StationLineInfoBean?,
                pos: Int
            ) {
                busStationAdapter.mInnerClickListener.onInnerClickListener(pos,position)
            }

            override fun onItemLongClick(
                view: View?,
                model: AnnexBusBean.StationListBean.StationLineInfoBean?,
                position: Int
            ) {
            }

        })
        recyclerView.adapter = adapter
    }
}