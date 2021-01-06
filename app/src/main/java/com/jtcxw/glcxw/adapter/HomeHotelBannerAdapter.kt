package com.jtcxw.glcxw.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.HotelBean
import com.jtcxw.glcxw.base.respmodels.ScenicBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.localbean.HomeBannerBean
import com.jtcxw.glcxw.ui.travel.HotelDetailFragment
import com.youth.banner.adapter.BannerAdapter
import me.yokeyword.fragmentation.SupportFragment


class HomeHotelBannerAdapter(data: List<HotelBean.HotelInfoListBean>, corners: Int): BannerAdapter<HotelBean.HotelInfoListBean, HomeHotelBannerAdapter.BannerViewHolder>(data) {

    var corners = corners
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_home_hot_banner,null) as ViewGroup
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: HotelBean.HotelInfoListBean?,
        position: Int,
        size: Int
    ) {
        if (corners > 0) {
            Glide.with(holder!!.imageView)
                .load(data!!.defaultPhotoUrl)
//                .apply(RequestOptions.bitmapTransform(RoundedCorners(DimensionUtil.dpToPx(corners).toInt())))
                .into(holder!!.imageView)
        }else {
            Glide.with(holder!!.imageView)
                .load(data!!.defaultPhotoUrl)
                .into(holder!!.imageView)
        }

        holder!!.tvName.text = data!!.hotelName
        holder!!.tvDetail.text = data!!.address

        holder!!.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(BundleKeys.KEY_HOTEL_ID,data!!.hotelId)
            HotelDetailFragment.newInstance(BaseUtil.sTopAct!!.topFragment as SupportFragment,bundle)
        }
    }


    inner class BannerViewHolder(view: ViewGroup) :
        RecyclerView.ViewHolder(view) {
        var imageView = view.findViewById<ImageView>(R.id.iv)
        var tvName = view.findViewById<TextView>(R.id.tv_name)
        var tvDetail = view.findViewById<TextView>(R.id.tv_detail)
        var ivLike = view.findViewById<ImageView>(R.id.iv_like)

    }
}