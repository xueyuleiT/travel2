package com.jtcxw.glcxw.ui.travel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.HotelBannerAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.HotelDetailBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentHotelDetailBinding
import com.jtcxw.glcxw.presenters.impl.CollectionPresenter
import com.jtcxw.glcxw.presenters.impl.HotelDetailPresenter
import com.jtcxw.glcxw.ui.LocationMapFragment
import com.jtcxw.glcxw.ui.login.LoginFragment
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.CollectionView
import com.jtcxw.glcxw.views.HotelDetailView
import com.youth.banner.indicator.CircleIndicator
import me.yokeyword.fragmentation.SupportFragment


class HotelDetailFragment:BaseFragment<FragmentHotelDetailBinding,CommonModel>(),HotelDetailView ,
    CollectionView {
    override fun onAddCollectionSucc(jsonObject: JsonObject) {
        if (jsonObject.get("Status").asBoolean){
            mHotelDetailBean!!.isCollection = 1
            mHotelDetailBean!!.collectionId = jsonObject.get("CollectionId").asString
            ToastUtil.toastSuccess("已收藏")
        } else {
            mHotelDetailBean!!.isCollection = 0
            ToastUtil.toastSuccess("收藏失败")
        }

        if (mHotelDetailBean!!.isCollection == 0) {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
        } else {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
        }
    }

    override fun onCancelCollectionSucc(jsonObject: JsonObject) {
        if (jsonObject.get("Status").asBoolean){
            mHotelDetailBean!!.isCollection = 0
            ToastUtil.toastSuccess("已取消收藏")
        } else {
            mHotelDetailBean!!.isCollection = 1
            ToastUtil.toastSuccess("取消失败")
        }

        if (mHotelDetailBean!!.isCollection == 0) {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
        } else {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
        }
    }

    var mHotelDetailBean:HotelDetailBean?=null

    override fun onGetHotelDetailSucc(hotelDetailBean: HotelDetailBean) {
        mHotelDetailBean = hotelDetailBean
        mBinding.tvScenicTitle.text = hotelDetailBean.hotelName
        mBinding.tvLocation.text = hotelDetailBean.address
        mBinding.tvLocationDetail.text = hotelDetailBean.trafficMsg
        if (hotelDetailBean.isCollection == 0) {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
        } else {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
        }
        if (hotelDetailBean.hotelPhotoList != null) {
            mBinding.banner.adapter = HotelBannerAdapter(hotelDetailBean.hotelPhotoList, 0)
            mBinding.banner.addBannerLifecycleObserver(this)
            mBinding.banner.indicator = CircleIndicator(context)
        }



        mBinding.tvPhone.text = hotelDetailBean.tel
        mBinding.webView.loadDataWithBaseURL(null,hotelDetailBean.hotelIntroduce, "text/html" , "utf-8", null)
        mBinding.ivLocation.setOnClickListener{

        }
        mBinding.tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            val data = Uri.parse("tel:${hotelDetailBean.tel}")
            intent.data = data
            startActivity(intent)
        }

        mBinding.ivLocation.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(BundleKeys.KEY_LOCATION_TYPE,"hotel")
            bundle.putDouble(BundleKeys.KEY_LAT,hotelDetailBean.lat)
            bundle.putDouble(BundleKeys.KEY_LON,hotelDetailBean.lon)
            bundle.putString(BundleKeys.KEY_TITLE,hotelDetailBean.hotelName)
            LocationMapFragment.newInstance(this,bundle)
        }

    }

    private fun initWeb(){

        mBinding.webView.setBackgroundColor(0) // 设置背景色
        mBinding.webView.background.alpha = 0 // 设置填充透明度 范围：0-255

        mBinding.webView.settings.loadWithOverviewMode = true
        mBinding.webView.settings.useWideViewPort = true
        mBinding.webView.settings.builtInZoomControls = true

        mBinding.webView.settings.javaScriptEnabled = true//是否允许JavaScript脚本运行，默认为false
        mBinding.webView.settings.domStorageEnabled = true//开启本地DOM存储

    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val hotelDetailFragment = HotelDetailFragment()
            hotelDetailFragment.arguments = bundle
            fragment.start(hotelDetailFragment)
        }
    }


    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_hotel_detail
    }

    //    var mData = ArrayList<HotelDetailBean.HotelMsgListBean>()
    var mPresenter:HotelDetailPresenter?= null
    var mCollectionPresenter: CollectionPresenter?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = HotelDetailPresenter(this)

        mCollectionPresenter = CollectionPresenter(this)

        initToolBar("")

        initWeb()
//        mBinding.recyclerView.layoutManager = LinearLayoutManager(context!!)
//        mBinding.recyclerView.adapter = HotelDetailAdapter(context!!,mData)

        val json = JsonObject()
        json.addProperty("Longitude",UserUtil.getUser().longitude)
        json.addProperty("Latitude",UserUtil.getUser().latitude)
        json.addProperty("HotelId",arguments!!.getString(BundleKeys.KEY_HOTEL_ID))
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        mPresenter!!.getHotelDetail(json)

        mBinding.vHeart.setOnClickListener {
            if (TextUtils.isEmpty(UserUtil.getUserInfoBean().token)) {
                ToastUtil.toastWaring("如果使用收藏功能，请先登录系统。")
//                showConfirmDialog("提示","如果使用收藏功能，请先登录系统。","登录","取消",object :DialogCallback{
//                    override fun invoke(p1: MaterialDialog) {
//                        LoginFragment.newInstance(this@HotelDetailFragment,null)
//                    }
//                },null)
                return@setOnClickListener
            }
            if (mHotelDetailBean != null) {
                if (mHotelDetailBean!!.isCollection == 0) {
                    val json = JsonObject()
                    json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                    json.addProperty("Type", 3)
                    json.addProperty("MineId",arguments!!.getString(BundleKeys.KEY_HOTEL_ID))
                    mCollectionPresenter!!.addCollection(json)
                } else {
                    val json = JsonObject()
                    json.addProperty("CollectionId",mHotelDetailBean!!.collectionId)
                    mCollectionPresenter!!.cancelCollection(json)
                }
            }
        }
    }

    override fun doAfterAnim() {
    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }

}