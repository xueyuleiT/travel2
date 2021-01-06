package com.jtcxw.glcxw.adapter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.views.recyclerview.BaseRecyclerAdapter
import com.jtcxw.glcxw.base.views.recyclerview.CommonRecyclerViewHolder
import com.jtcxw.glcxw.listeners.CollectCancelCallback
import com.jtcxw.glcxw.listeners.DialogCallback
import com.jtcxw.glcxw.localbean.MyCollectionBean
import com.jtcxw.glcxw.ui.QueryMainFragment
import com.jtcxw.glcxw.ui.travel.HotelDetailFragment
import com.jtcxw.glcxw.ui.travel.ScenicDetailFragment
import me.yokeyword.fragmentation.SupportFragment

class CollectionAdapter(context: Context,list:List<MyCollectionBean>,callback: CollectCancelCallback):BaseRecyclerAdapter<MyCollectionBean>(context,list) {

    private var mCallback = callback

    override fun getConvertViewId(viewType: Int): Int {
        return when(viewType) {
            0 -> {
                R.layout.item_collection_header
            }
            3 -> {
                R.layout.item_collection_header
            }
            2 -> {
                R.layout.item_collection_tail
            }
            else -> {
                R.layout.item_collection
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mDatas[position].type
    }

    override fun convert(
        holder: CommonRecyclerViewHolder?,
        data: MyCollectionBean?,
        position: Int
    ) {
        val llContent = holder!!.getView<View>(R.id.ll_content)
        val vDel = holder!!.getView<View>(R.id.v_del)
        if (data!!.isEdit) {
            vDel.visibility = View.VISIBLE
        } else {
            vDel.visibility = View.GONE
        }
        when(getItemViewType(position)) {
            0 -> {
                val tvTitle = holder!!.getView<TextView>(R.id.tv_title)
                val llLayout = holder!!.getView<LinearLayout>(R.id.ll_layout)
                val vType = holder!!.getView<ImageView>(R.id.v_type)
                val tvContent = holder!!.getView<TextView>(R.id.tv_content)
                tvContent.text = data!!.collectInfoBean.collectionName
                llLayout.setBackgroundResource(R.drawable.shape_r10_cw_top)
                tvTitle.text = data!!.collectInfoBean.typeName
                when {
                    data!!.collectInfoBean.type == 1 -> {
                        vType.setImageResource(R.mipmap.icon_collect_station)
                    }
                    data!!.collectInfoBean.type == 2 -> {
                        vType.setImageResource(R.mipmap.icon_collect_line)
                    }
                    data!!.collectInfoBean.type == 3 -> {
                        vType.setImageResource(R.mipmap.icon_collect_hotel)
                    }
                    data!!.collectInfoBean.type == 4 -> {
                        vType.setImageResource(R.mipmap.icon_collect_scenic)
                    }
                }
            }
            2 -> {
                val tvContent = holder!!.getView<TextView>(R.id.tv_content)

                tvContent.text = data!!.collectInfoBean.collectionName

            }
            3 -> {
                val tvTitle = holder!!.getView<TextView>(R.id.tv_title)
                val llLayout = holder!!.getView<LinearLayout>(R.id.ll_layout)
                val vType = holder!!.getView<ImageView>(R.id.v_type)
                val vBottom = holder!!.getView<View>(R.id.v_bottom)
                val tvContent = holder!!.getView<TextView>(R.id.tv_content)


                tvContent.text = data!!.collectInfoBean.collectionName
                vBottom.visibility = View.GONE

                llLayout.setBackgroundResource(R.drawable.shape_r10_cw)
                tvTitle.text = data!!.collectInfoBean.typeName
                when {
                    data!!.collectInfoBean.type == 1 -> {
                        vType.setImageResource(R.mipmap.icon_collect_station)
                    }
                    data!!.collectInfoBean.type == 2 -> {
                        vType.setImageResource(R.mipmap.icon_collect_line)
                    }
                    data!!.collectInfoBean.type == 3 -> {
                        vType.setImageResource(R.mipmap.icon_collect_hotel)
                    }
                    data!!.collectInfoBean.type == 4 -> {
                        vType.setImageResource(R.mipmap.icon_collect_scenic)
                    }
                }

            }
            else -> {
                val tvContent = holder!!.getView<TextView>(R.id.tv_content)

                tvContent.text = data!!.collectInfoBean.collectionName

            }
        }

        vDel.setOnClickListener {
            mCallback.onDialogCallback(data!!.collectInfoBean.type,data!!.collectInfoBean.id)
        }

        llContent.setOnClickListener {
            when (data!!.collectInfoBean.type) {
                1 -> {
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_QUERY_TEXT,data!!.collectInfoBean.collectionName)
                    QueryMainFragment.newInstance(BaseUtil.sTopAct!!.topFragment as SupportFragment,bundle)
//                    val bundle = Bundle()
//                    bundle.putString(BundleKeys.KEY_STATION_ID,data!!.collectInfoBean.mineId)
//                    QueryMainFragment.newInstance(BaseUtil.sTopAct!!.topFragment as SupportFragment,bundle)
                }

                2 -> {
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_LINE_ID,
                        data!!.collectInfoBean.mineId)
                    QueryMainFragment.newInstance(BaseUtil.sTopAct!!.topFragment as SupportFragment,bundle)
                }

                3 -> {
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_HOTEL_ID,data!!.collectInfoBean.mineId)
                    HotelDetailFragment.newInstance(BaseUtil.sTopAct!!.topFragment as SupportFragment,bundle)
                }

                4 -> {
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_SCENIC_ID,data!!.collectInfoBean.mineId)
                    ScenicDetailFragment.newInstance(BaseUtil.sTopAct!!.topFragment as SupportFragment,bundle)
                }
            }
        }
    }

    override fun onClick(
        view: View?,
        model: MyCollectionBean?,
        position: Int,
        holder: CommonRecyclerViewHolder?
    ) {
    }
}