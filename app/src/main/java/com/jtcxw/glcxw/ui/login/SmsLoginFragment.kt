package com.jtcxw.glcxw.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.glcxw.lib.util.CacheUtil
import com.glcxw.lib.util.RuleUtil
import com.glcxw.lib.util.constants.SPKeys
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.adapter.MySpinnerAdaqpter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.constant.Constant
import com.jtcxw.glcxw.base.respmodels.SmsBean
import com.jtcxw.glcxw.base.respmodels.SmsLoginBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentSmsLoginBinding
import com.jtcxw.glcxw.presenters.impl.SmsLoginPresenter
import com.jtcxw.glcxw.ui.MainActivity
import com.jtcxw.glcxw.ui.WelcomeActivity
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.SmsLoginView
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment

class SmsLoginFragment:
    BaseFragment<FragmentSmsLoginBinding, CommonModel>(),SmsLoginView {
    override fun onSendSmsCodeSucc(smsBean: SmsBean) {
        if(smsBean.SendFlag) {
            ToastUtil.toastSuccess(smsBean.Notice)
            mBinding.tvTime.task.startWithType(mCodeType)
        } else {
            ToastUtil.toastWaring(smsBean.Notice)
        }
    }

    override fun onSmsLoginSucc(smsLoginBean: SmsLoginBean) {
        if (smsLoginBean.isLoginOk()) {
            ToastUtil.toastSuccess(smsLoginBean.getLoginMsg())
            UserUtil.getUser().save(smsLoginBean)
            UserUtil.isShowLogin = false
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            if (activity is WelcomeActivity) {
                mBinding.tvTime.postDelayed({
                    if (activity is WelcomeActivity) {
                        activity?.finish()
                    }
                }, 300)
            } else {
                setFragmentResult(ISupportFragment.RESULT_OK, Bundle())
                pop()
            }
            CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_SMS_TIME + mCodeType,0L)
        } else {
            ToastUtil.toastWaring(smsLoginBean.getLoginMsg())
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?) {
            val smsLoginFragment = SmsLoginFragment()
            smsLoginFragment.arguments = bundle
            fragment.startForResult(smsLoginFragment,12)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.btn_login -> {
                if (!RuleUtil.isPhone(mBinding.etPhone.text.toString())) {
                    ToastUtil.toastWaring("请输入正确的手机号")
                    return
                }

                if (mBinding.etCode.text.toString().length < 4) {
                    ToastUtil.toastWaring("请输入正确的验证码")
                    return
                }
                hideSoftInput()
                val json = JsonObject()
                json.addProperty("ClientMsg",Build.BRAND + " "+ Build.MODEL)
                json.addProperty("CodeType",mCodeType)
                json.addProperty("SmsCode",mBinding.etCode.text.toString())
                json.addProperty("RegId", JPushInterface.getRegistrationID(context))
                json.addProperty("TelePhone",mBinding.spinner.selectedItem.toString() + mBinding.etPhone.text.toString())
                mPresenter!!.smsLogin(json)
            }

            R.id.tv_time -> {
                if (!RuleUtil.isPhone(mBinding.etPhone.text.toString())) {
                    ToastUtil.toastWaring("请输入正确的手机号")
                    return
                }
                hideSoftInput()
                val json = JsonObject()
                json.addProperty("TelePhone",mBinding.spinner.selectedItem.toString() + mBinding.etPhone.text.toString())
                json.addProperty("CodeType",mCodeType)
                mPresenter!!.sendSmsCode(json)
            }
        }
    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_sms_login
    }

    var mPresenter:SmsLoginPresenter ?= null
    val mCodeType = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = SmsLoginPresenter(this)

        if (BaseUtil.isDarkMode()) {
            mBinding.vBg.setBackgroundResource(0)
        }

        mBinding.tvTime.setOnClickListener(this)
        mBinding.btnLogin.setOnClickListener(this)

        if (System.currentTimeMillis() -  CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_SMS_TIME + mCodeType, 0L) < Constant.SMS_TIME * 1000) {
            mBinding.tvTime.task.start(
                Constant.SMS_TIME - (System.currentTimeMillis() -  CacheUtil.getInstance().getProperty(
                    SPKeys.SP_KEY_SMS_TIME + mCodeType, 0L)) / (1000))
        }

        if (arguments != null) {
            mBinding.etPhone.setText(arguments!!.getString(BundleKeys.KEY_PHONE,""))
            mBinding.tvTime.isEnabled = RuleUtil.isPhone( mBinding.etPhone.text.toString()) && !mBinding.tvTime.isRunning
        }

        mBinding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mBinding.tvTime.isEnabled = RuleUtil.isPhone(mBinding.etPhone.text.toString()) && !mBinding.tvTime.isRunning
                mBinding.btnLogin.isEnabled = RuleUtil.isPhone(mBinding.etPhone.text.toString()) && mBinding.etCode.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        mBinding.etCode.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnLogin.isEnabled = RuleUtil.isPhone(mBinding.etPhone.text.toString()) && mBinding.etCode.text.toString().isNotEmpty()
            }

        })
        initSpinner()
    }

    private fun initSpinner() {
        val list = ArrayList<String>()
        list.add("+86")
        mBinding.spinner.adapter = MySpinnerAdaqpter(context!!,list)
        mBinding.spinner.setPopupBackgroundResource(R.drawable.shape_r10_cef)
        mBinding.spinner.setSelection(0)
    }

    override fun doAfterAnim() {
    }

    override fun needSetStatusHeight(): Boolean {
        return false
    }
}