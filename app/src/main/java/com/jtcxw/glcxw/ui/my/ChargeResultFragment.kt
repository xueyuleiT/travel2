package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.glcxw.lib.util.AmountUtil
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.ExtraOrderBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentChargeResultBinding
import com.jtcxw.glcxw.presenters.impl.MyPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.MyView
import me.yokeyword.fragmentation.SupportFragment

class ChargeResultFragment : BaseFragment<FragmentChargeResultBinding,CommonModel>(),MyView {
    override fun onOrderStatisticsSucc(extraOrderBean: ExtraOrderBean) {
    }

    override fun onOrderStatisticsFinish() {
    }

    var mCount = 0

    override fun onMemberInfoSucc(userInfoBean: UserInfoBean) {
        mCount ++
        if (userInfoBean.ownerAmount != arguments!!.getString("currentAmount","0.0").toDouble()) {
            mBinding.ivLoading.setImageResource(R.mipmap.icon_pay_success)
            mBinding.tvChargeStatus.text = "充值完成"
            mBinding.tvChargeTip.text = "可在余额明细界面查看充值记录"
            mBinding.tvChargeTip1.text = ""
            mBinding.tvChargeTip2.text = ""
        } else {
            if (mCount < 5) {
                mBinding.tvChargeStatus.postDelayed({
                    if (context != null && !isDetached) {
                        var json = JsonObject()
                        json.addProperty("MemberId", UserUtil.getUser().userInfoBean.memberId)
                        mPresenter!!.getMemberInfo(json)
                    }
                },1000)
            } else {
                mBinding.tvChargeStatus.text = "处理中..."
                mBinding.tvChargeTip.text = "系统正在处理本次充值业务"
                mBinding.tvChargeTip1.text = SpannelUtil.getSpannelStr("预计5分钟内到账",resources.getColor(R.color.green_light),2,3)
                mBinding.tvChargeTip2.text = "稍后可在余额明细洁界面查看充值记录"
            }
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val chargeResultFragment = ChargeResultFragment()
            chargeResultFragment.arguments = bundle
            fragment.start(chargeResultFragment)
        }
    }

    override fun onMemberInfoFailed(msg: String) {
    }

    override fun onMemberInfoFinish() {
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_charge_result
    }

    var mPresenter:MyPresenter?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("支付结果")

        mBinding.tvAmount.text = "¥"+AmountUtil.formatTosepara(arguments!!.getString(BundleKeys.KEY_ORDER_AMOUNT))+"元"
        mBinding.tvPayWay.text = arguments!!.getString(BundleKeys.KEY_PAY_TYPE)

        mPresenter = MyPresenter(this)
        Glide.with(this).load(R.drawable.gif_loading).into(mBinding.ivLoading)
        mBinding.tvChargeStatus.text = "支付中..."
        mBinding.tvChargeTip.text = "可在余额明细洁界面查看充值记录"
        mBinding.tvChargeTip1.text = ""
        mBinding.tvChargeTip2.text = ""


        var json = JsonObject()
        json.addProperty("MemberId", UserUtil.getUser().userInfoBean.memberId)
        mPresenter!!.getMemberInfo(json)
    }

    override fun doAfterAnim() {
    }
}