package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.glcxw.lib.util.AmountUtil
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.ExtraOrderBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentMyBinding
import com.jtcxw.glcxw.presenters.impl.MyPresenter
import com.jtcxw.glcxw.ui.LocationFragment
import com.jtcxw.glcxw.ui.customized.OrderListFragment
import com.jtcxw.glcxw.ui.login.LoginFragment
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.MyView
import me.yokeyword.fragmentation.SupportFragment

class MyFragment:LocationFragment<FragmentMyBinding,CommonModel>() ,MyView{
    override fun onOrderStatisticsSucc(extraOrderBean: ExtraOrderBean) {
        if (extraOrderBean.ticketreFundedCount > 0) {
            mBinding.tvReturn.visibility = View.VISIBLE
            mBinding.tvReturn.text = if (extraOrderBean.ticketreFundedCount > 99) "99+" else extraOrderBean.ticketreFundedCount.toString()
        } else {
            mBinding.tvReturn.visibility = View.GONE
        }

        if (extraOrderBean.needPayCount > 0) {
            mBinding.tvWaitingPay.visibility = View.VISIBLE
            mBinding.tvWaitingPay.text = if (extraOrderBean.needPayCount > 99) "99+" else extraOrderBean.needPayCount.toString()
        } else {
            mBinding.tvWaitingPay.visibility = View.GONE
        }

        if (extraOrderBean.waitingToUseCount > 0) {
            mBinding.tvWaitingGo.visibility = View.VISIBLE
            mBinding.tvWaitingGo.text = if (extraOrderBean.waitingToUseCount > 99) "99+" else extraOrderBean.waitingToUseCount.toString()
        } else {
            mBinding.tvWaitingGo.visibility = View.GONE
        }
    }

    override fun onOrderStatisticsFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }
    }

    override fun onMemberInfoFailed(msg:String) {
    }

    override fun onLogout() {
        mBinding.tvName.text = "注册/登录"
    }

    override fun refresh() {
        mBinding.swipeLayout.autoRefresh()
    }

    override fun onMemberInfoFinish() {
        mFinishCount --
        if (mFinishCount <= 0) {
            mBinding.swipeLayout.finishRefresh(0)
        }

    }

    override fun onMemberInfoSucc(userInfoBean: UserInfoBean) {
        UserUtil.getUser().save(userInfoBean)
        if (!TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
            if (!TextUtils.isEmpty(UserUtil.getUser().userInfoBean.nikeName)) {
                mBinding.tvName.text = UserUtil.getUser().userInfoBean.nikeName
            } else {
                mBinding.tvName.text = UserUtil.getUser().userInfoBean.telphoneNo
            }
            mBinding.tvAmount.text = AmountUtil.format(UserUtil.getUser().userInfoBean.ownerAmount)
            if (!TextUtils.isEmpty(userInfoBean.headImageUrl) && userInfoBean.headImageUrl.startsWith("http")) {
                Glide.with(context!!)
                    .load(userInfoBean.headImageUrl)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(DimensionUtil.dpToPx(25).toInt())))
                    .into(mBinding.ivHead)
            } else {
                Glide.with(context!!)
                    .load(R.mipmap.icon_logo_green)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(DimensionUtil.dpToPx(25).toInt())))
                    .into(mBinding.ivHead)
            }
        }

    }

    override fun onLocationChange(aMapLocation: AMapLocation) {
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {

            R.id.tv_collection -> {
                CollectionFragment.newInstance(parentFragment as SupportFragment, null)
            }
            R.id.rl_pay_outer -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TITLE,"待付款")
                bundle.putString(BundleKeys.KEY_ORDER_CLASS_TYPE,"1")
                OrdersFragment.newInstance(parentFragment as SupportFragment,bundle)
            }

            R.id.rl_travel_outer -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TITLE,"待出行")
                bundle.putString(BundleKeys.KEY_ORDER_CLASS_TYPE,"2")
                OrdersFragment.newInstance(parentFragment as SupportFragment,bundle)
            }

            R.id.rl_return_outer -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TITLE,"退款")
                bundle.putString(BundleKeys.KEY_ORDER_CLASS_TYPE,"3")
                OrdersFragment.newInstance(parentFragment as SupportFragment,bundle)
            }

            R.id.ll_bank -> {
                PayWayFragment.newInstance(parentFragment as SupportFragment,null)
            }

            R.id.ll_balance -> {
                ChargeFragment.newInstance(parentFragment as SupportFragment,null)
            }
            R.id.ll_order -> {
                OrdersFragment.newInstance(parentFragment as SupportFragment,null)
            }

            R.id.ll_personal -> {
                UserInfoFragment.newInstance(parentFragment as SupportFragment, null)
            }

            R.id.iv_setting -> {
                SettingFragment.newInstance(parentFragment as SupportFragment, null)
            }
        }
    }

    override fun doAfterAnim() {

    }

    var mFinishCount = 0
    var mPresenter:MyPresenter? =null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BaseUtil.isDarkMode()) {
            mBinding.rlBg.setBackgroundResource(0)
        }

        val ivSetting = mBinding.root.findViewById<ImageView>(R.id.iv_setting)
        val tvTitle = mBinding.root.findViewById<TextView>(R.id.tv_title)
        tvTitle.setTextColor(resources.getColor(backColor()))
        mBinding.llPersonal.setOnClickListener(this)
        mBinding.ivSetting.setOnClickListener(this)
        mBinding.llOrder.setOnClickListener(this)
        mBinding.llBalance.setOnClickListener(this)
        mBinding.llBank.setOnClickListener(this)
        mBinding.rlTravelOuter.setOnClickListener(this)
        mBinding.rlReturnOuter.setOnClickListener(this)
        mBinding.rlPayOuter.setOnClickListener(this)
        mBinding.tvCollection.setOnClickListener(this)

        mPresenter = MyPresenter(this)
        mBinding.swipeLayout.setOnRefreshListener {
            if (TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
                mBinding.swipeLayout.finishRefresh(0)
                return@setOnRefreshListener
            }

            mFinishCount = 2

            var json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
            mPresenter!!.getMemberInfo(json)

            json = JsonObject()
            json.addProperty("MemberId",UserUtil.getUser().userInfoBean.memberId)
            mPresenter!!.getOrderStatistics(json)
        }

        if (!TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
            mBinding.swipeLayout.autoRefresh()
        } else {
            mBinding.swipeLayout.finishRefresh(0)
        }

    }

    var hasInit = false
    override fun onSupportVisible() {
        super.onSupportVisible()
        if (ismBindingInitialized() || hasInit) {
            if (!TextUtils.isEmpty(UserUtil.getUser().userInfoBean.memberId)) {
                mBinding.swipeLayout.autoRefresh()
                if (!TextUtils.isEmpty(UserUtil.getUser().userInfoBean.nikeName)) {
                    mBinding.tvName.text = UserUtil.getUser().userInfoBean.nikeName
                } else {
                    mBinding.tvName.text = UserUtil.getUser().userInfoBean.telphoneNo
                }
                mBinding.tvAmount.text =
                    AmountUtil.format(UserUtil.getUser().userInfoBean.ownerAmount) + "元"
            }
        }

        if (!hasInit) {
            hasInit = true
        }
    }

    override fun statusBarColor(): Int {
        return R.color.transparent
    }

}