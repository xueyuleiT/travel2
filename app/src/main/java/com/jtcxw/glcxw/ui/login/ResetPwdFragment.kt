package com.jtcxw.glcxw.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.glcxw.lib.util.CacheUtil
import com.glcxw.lib.util.RuleUtil
import com.glcxw.lib.util.constants.SPKeys
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.ResetPwdBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.RxBus
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentResetPwdBinding
import com.jtcxw.glcxw.events.LogoutEvent
import com.jtcxw.glcxw.fragment.MainFragment
import com.jtcxw.glcxw.presenters.impl.ResetPwdPresenter
import com.jtcxw.glcxw.ui.WelcomeActivity
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.ResetPwdView
import me.yokeyword.fragmentation.SupportFragment

class ResetPwdFragment:
    BaseFragment<FragmentResetPwdBinding, CommonModel>(),ResetPwdView {
    override fun onResetPwdSucc(resetPwdBean: ResetPwdBean) {
        if (resetPwdBean.IsPass) {
            ToastUtil.toastSuccess(resetPwdBean.ResponseContent)
            UserUtil.getUser().clear()
            if (activity is WelcomeActivity) {
                popTo(LoginFragment::class.java, false)
            } else {
                popTo(MainFragment::class.java, false)
                RxBus.getDefault().postDelay(LogoutEvent(), 300)
//            startActivity(Intent(activity,WelcomeActivity::class.java))
//            activity!!.finish()
            }
            CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_SMS_TIME + mCodeType,0L)
        }else {
            ToastUtil.toastError(resetPwdBean.ResponseContent)
        }
    }
    private val mCodeType = 3
    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?) {
            val resetPwdFragment = ResetPwdFragment()
            resetPwdFragment.arguments = bundle
            fragment.start(resetPwdFragment)
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_reset_pwd
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.btn_confirm -> {
                if (!RuleUtil.checkPasswordRule(mBinding.etPwd.text.toString())) {
                    ToastUtil.toastWaring("请输入八位及以上包含字母大小写和数字并且不含特殊字符。")
                    return
                }

                if (mBinding.etPwd.text.toString() != mBinding.etPwdConfirm.text.toString()){
                    ToastUtil.toastWaring("两次输入的密码不一致")
                    return
                }
                hideSoftInput()
                val json = JsonObject()
                json.addProperty("MemberId",arguments!!.getString(BundleKeys.KEY_MEMBER_ID))
                json.addProperty("SmsCode",arguments!!.getString(BundleKeys.KEY_SMS_CODE))
                json.addProperty("TelePhone",arguments!!.getString(BundleKeys.KEY_PHONE))
                json.addProperty("Password",mBinding.etPwd.text.toString())
                json.addProperty("CodeType",mCodeType)
                mPresenter!!.changePwd(json)
            }
        }
    }

    var mPresenter:ResetPwdPresenter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (BaseUtil.isDarkMode()) {
            mBinding.vBg.setBackgroundResource(0)
        }
    }

    override fun doAfterAnim() {
        mBinding.btnConfirm.setOnClickListener(this)
        mPresenter = ResetPwdPresenter(this)

        mBinding.etPwdConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnConfirm.isEnabled = mBinding.etPwd.text.toString().isNotEmpty()
                        && mBinding.etPwdConfirm.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        mBinding.etPwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnConfirm.isEnabled = mBinding.etPwd.text.toString().isNotEmpty()
                        && mBinding.etPwdConfirm.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    override fun needSetStatusHeight(): Boolean {
        return false
    }
}