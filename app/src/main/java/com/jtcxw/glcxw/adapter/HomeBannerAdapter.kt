package com.jtcxw.glcxw.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.BannerBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.localbean.HomeBannerBean
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.ui.WebFragment
import com.youth.banner.adapter.BannerAdapter
import me.yokeyword.fragmentation.SupportFragment


class HomeBannerAdapter(data: List<BannerBean.BannerListBean>, corners: Int): BannerAdapter<BannerBean.BannerListBean, HomeBannerAdapter.BannerViewHolder>(data) {

    var corners = corners
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_home_banner,null) as ImageView
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: BannerBean.BannerListBean?,
        position: Int,
        size: Int
    ) {
        if (corners > 0) {
            Glide.with(holder!!.imageView)
                .load(data!!.bannerUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(DimensionUtil.dpToPx(corners).toInt())))
                .into(holder!!.imageView)
        }else {
            Glide.with(holder!!.imageView)
                .load(data!!.bannerUrl)
                .into(holder!!.imageView)
        }

        holder.itemView.setOnClickListener {
            if (data!!.jumpMark == 1 && data.jumpMethod == 1) {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_WEB_TITLE,"")
                bundle.putString(BundleKeys.KEY_WEB_URL,data.jumpRoute)
                WebFragment.newInstance(BaseUtil.sTopAct!!.topFragment as SupportFragment,bundle)
            }
        }
    }


    inner class BannerViewHolder(view: ImageView) :
        RecyclerView.ViewHolder(view) {
        var imageView = view

    }
}