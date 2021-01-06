package com.jtcxw.glcxw.ui.travel

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.ScenicBannerAdapter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.ScenicDetailBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentScenicDetailBinding
import com.jtcxw.glcxw.presenters.impl.CollectionPresenter
import com.jtcxw.glcxw.presenters.impl.ScenicDetailPresenter
import com.jtcxw.glcxw.ui.LocationMapFragment
import com.jtcxw.glcxw.ui.login.LoginFragment
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.CollectionView
import com.jtcxw.glcxw.views.ScenicDetailView
import com.youth.banner.indicator.CircleIndicator
import me.yokeyword.fragmentation.SupportFragment

class ScenicDetailFragment:BaseFragment<FragmentScenicDetailBinding,CommonModel>() ,ScenicDetailView,CollectionView{
    override fun onAddCollectionSucc(jsonObject: JsonObject) {
        if (jsonObject.get("Status").asBoolean){
            mScenicDetailBean!!.isCollection = 1
            mScenicDetailBean!!.collectionId = jsonObject.get("CollectionId").asString
            ToastUtil.toastSuccess("已收藏")
        } else {
            mScenicDetailBean!!.isCollection = 0
            ToastUtil.toastSuccess("收藏失败")
        }

        if (mScenicDetailBean!!.isCollection == 0) {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
        } else {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
        }
    }

    override fun onCancelCollectionSucc(jsonObject: JsonObject) {
        if (jsonObject.get("Status").asBoolean){
            mScenicDetailBean!!.isCollection = 0
            ToastUtil.toastSuccess("已取消收藏")
        } else {
            mScenicDetailBean!!.isCollection = 1
            ToastUtil.toastSuccess("取消失败")
        }

        if (mScenicDetailBean!!.isCollection == 0) {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
        } else {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
        }
    }

    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?) {
            val scenicDetailFragment = ScenicDetailFragment()
            scenicDetailFragment.arguments = bundle
            fragment.start(scenicDetailFragment)
        }
    }

    var mScenicDetailBean:ScenicDetailBean?= null

    override fun onScenicDetailSucc(scenicDetailBean: ScenicDetailBean) {
        mScenicDetailBean = scenicDetailBean
        mBinding.tvScenicTitle.text = scenicDetailBean.scenicName
        if (TextUtils.isEmpty(scenicDetailBean.scenicLevel)){
            mBinding.tvLevel.visibility = View.GONE
        }
        mBinding.tvLevel.text = scenicDetailBean.scenicLevel
        mBinding.tvLocation.text = scenicDetailBean.address
        mBinding.tvLocationDetail.text = scenicDetailBean.trafficMsg

        if (scenicDetailBean.isCollection == 0) {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart)
        } else {
            mBinding.vHeart.setImageResource(R.mipmap.icon_heart_red)
        }

        if (scenicDetailBean.scenicPhotoList != null) {
            mBinding.banner.adapter = ScenicBannerAdapter(scenicDetailBean.scenicPhotoList, 0)
            mBinding.banner.addBannerLifecycleObserver(this)
            mBinding.banner.indicator = CircleIndicator(context)
        }
//        mData.clear()
//        mData.addAll(scenicDetailBean.scenicMsgList)
//        if (mData.size > 0) {
//            mData[0].tel = scenicDetailBean.tel
//        }
//        mBinding.recyclerView.setNewData(mData)
        mBinding.webView.loadDataWithBaseURL(null,scenicDetailBean.scenicIntroduce, "text/html" , "utf-8", null)

        mBinding.ivLocation.setOnClickListener{
            val bundle = Bundle()
            bundle.putString(BundleKeys.KEY_LOCATION_TYPE,"scenic")
            bundle.putDouble(BundleKeys.KEY_LAT,scenicDetailBean.lat)
            bundle.putDouble(BundleKeys.KEY_LON,scenicDetailBean.lon)
            bundle.putString(BundleKeys.KEY_TITLE,scenicDetailBean.scenicName)
            LocationMapFragment.newInstance(this,bundle)

        }

    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_scenic_detail
    }

//    var mData = ArrayList<ScenicDetailBean.ScenicMsgListBean>()
    var mPresenter:ScenicDetailPresenter?= null
    var mCollectionPresenter:CollectionPresenter?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar("")
        initWeb()
        mPresenter = ScenicDetailPresenter(this)
        mCollectionPresenter = CollectionPresenter(this)
//        mBinding.recyclerView.layoutManager = LinearLayoutManager(context!!)
//        mBinding.recyclerView.adapter = ScenicDetailAdapter(context!!,mData)

        val json = JsonObject()
        json.addProperty("ScenicId",arguments!!.getString(BundleKeys.KEY_SCENIC_ID,""))
        json.addProperty("Longitude",UserUtil.getUser().longitude)
        json.addProperty("latitude",UserUtil.getUser().latitude)
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)

        mPresenter!!.getScenicDetail(json)

        mBinding.vHeart.setOnClickListener {
            if (TextUtils.isEmpty(UserUtil.getUserInfoBean().token)) {
                ToastUtil.toastWaring("如果使用收藏功能，请先登录系统。")
//                showConfirmDialog("提示","如果使用收藏功能，请先登录系统。","登录","取消",object :DialogCallback{
//                    override fun invoke(p1: MaterialDialog) {
//                        LoginFragment.newInstance(this@ScenicDetailFragment,null)
//                    }
//                },null)
                return@setOnClickListener
            }
            if (mScenicDetailBean != null) {
                if (mScenicDetailBean!!.isCollection == 0) {
                    val json = JsonObject()
                    json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                    json.addProperty("Type", 4)
                    json.addProperty("MineId",arguments!!.getString(BundleKeys.KEY_SCENIC_ID,""))
                    mCollectionPresenter!!.addCollection(json)
                } else {
                    val json = JsonObject()
                    json.addProperty("CollectionId",mScenicDetailBean!!.collectionId)
                    mCollectionPresenter!!.cancelCollection(json)
                }
            }
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

    override fun statusBarColor(): Int {
        return R.color.transparent
    }

    override fun doAfterAnim() {
    }
}