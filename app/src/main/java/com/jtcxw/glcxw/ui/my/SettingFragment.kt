package com.jtcxw.glcxw.ui.my

import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.BuildConfig
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.utils.DimensionUtil
import com.jtcxw.glcxw.base.utils.RxBus
import com.jtcxw.glcxw.base.utils.SpannelUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentSettingBinding
import com.jtcxw.glcxw.events.LogoutEvent
import com.jtcxw.glcxw.ui.login.AgreementFragment
import com.jtcxw.glcxw.ui.login.FindPwdFragment
import com.jtcxw.glcxw.ui.login.LoginFragment
import com.jtcxw.glcxw.viewmodel.CommonModel
import me.yokeyword.fragmentation.SupportFragment

class SettingFragment:BaseFragment<FragmentSettingBinding,CommonModel>() {
    override fun getVariableId(): Int {
        return BR.common
    }

    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?){
            val settingFragment = SettingFragment()
            settingFragment.arguments = bundle
            fragment.start(settingFragment)

        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {

            R.id.tv_about -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TREATY_TYPE,"3")
                AgreementFragment.newInstance(this,bundle)
            }

            R.id.tv_phone -> {

            }

            R.id.tv_logout -> {
                MaterialDialog(context!!)
                    .title(null, "提示")
                    .message(null, "确定退出登录吗？")
                    .positiveButton(null, SpannelUtil.getSpannelStr("确认", context!!.resources.getColor(R.color.blue_3A75F3)),
                        object :DialogCallback{
                            override fun invoke(p1: MaterialDialog) {
                                UserUtil.getUser().clear()
                                pop()
                                RxBus.getDefault().postDelay(LogoutEvent(),500)
                            }

                        })
                    .negativeButton(null,SpannelUtil.getSpannelStr("取消", context!!.resources.getColor(R.color.blue_3A75F3)),null)
                    .cornerRadius(DimensionUtil.dpToPx(2), null)
                    .cancelable(false)
                    .show()
            }

            R.id.tv_pwd -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_PHONE,UserUtil.getUserInfoBean().telphoneNo.replace("+86",""))
                FindPwdFragment.newInstance(this,bundle)
            }

            R.id.ll_real_name -> {
                AuthFragment.newInstance(this,null)
            }

            R.id.tv_service -> {
                ServiceFragment.newInstance(this,null)
            }

            R.id.ll_version -> {
                VersionFragment.newInstance(this,null)
            }


        }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("设置")
        mBinding.tvLogout.setOnClickListener(this)
        mBinding.tvAbout.setOnClickListener(this)
        mBinding.rlPhone.setOnClickListener(this)
        mBinding.tvPwd.setOnClickListener(this)
        mBinding.llRealName.setOnClickListener(this)
        mBinding.tvAbout.setOnClickListener(this)
        mBinding.tvService.setOnClickListener(this)
        mBinding.llVersion.setOnClickListener(this)

        mBinding.tvPhone.text = UserUtil.getUserInfoBean().telphoneNo
        if (UserUtil.getUserInfoBean().realNameVerifyFlag == "1") {
            mBinding.tvAuth.text = "已实名"
        } else {
            mBinding.tvAuth.text = "未实名"
        }

        mBinding.tvVersion.text = "v"+BuildConfig.VERSION_NAME
    }

    override fun doAfterAnim() {
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (ismBindingInitialized()) {
            if (UserUtil.getUserInfoBean().realNameVerifyFlag == "1") {
                mBinding.tvAuth.text = "已实名"
            } else {
                mBinding.tvAuth.text = "未实名"
            }
        }
    }
}