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
import com.jtcxw.glcxw.adapter.MySpinnerAdaqpter
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.constant.Constant
import com.jtcxw.glcxw.base.respmodels.RegisterBean
import com.jtcxw.glcxw.base.respmodels.SmsBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.MD5Util
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.databinding.FragmentRegisterBinding
import com.jtcxw.glcxw.presenters.impl.RegisterPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.RegisterView
import me.yokeyword.fragmentation.SupportFragment

class RegisterFragment:
    BaseFragment<FragmentRegisterBinding, CommonModel>(),RegisterView {
    override fun onSmsRegisterSucc(registerBean: RegisterBean) {
        if (registerBean.RegisterFlag) {
            ToastUtil.toastSuccess(registerBean.Notice)
            pop()
            CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_SMS_TIME + mCodeType,0L)
        } else {
            ToastUtil.toastWaring(registerBean.Notice)
        }
    }

    override fun onSendSmsCodeSucc(smsBean: SmsBean) {
        if(smsBean.SendFlag) {
            mBinding.tvTime.task.startWithType(mCodeType)
            ToastUtil.toastSuccess(smsBean.Notice)
        } else {
            ToastUtil.toastWaring(smsBean.Notice)
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment,bundle: Bundle?) {
            val registerFragment = RegisterFragment()
            registerFragment.arguments = bundle
            fragment.start(registerFragment)
        }
    }

    val mCodeType = 2
    private var mPresenter: RegisterPresenter? = null

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BaseUtil.isDarkMode()) {
            mBinding.vBg.setBackgroundResource(0)
        }

        mPresenter = RegisterPresenter(this)
        mBinding.tvTime.setOnClickListener(this)
        mBinding.btnRegister.setOnClickListener(this)
        mBinding.tvAgreement.setOnClickListener(this)


        if (System.currentTimeMillis() -  CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_SMS_TIME + mCodeType, 0L) < Constant.SMS_TIME * 1000) {
            mBinding.tvTime.task.start(Constant.SMS_TIME - (System.currentTimeMillis() -  CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_SMS_TIME + mCodeType, 0L)) / (1000))
        }

        if (arguments != null) {
            mBinding.etPhone.setText(arguments!!.getString(BundleKeys.KEY_PHONE,""))
            mBinding.tvTime.isEnabled = RuleUtil.isPhone( mBinding.etPhone.text.toString()) && !mBinding.tvTime.isRunning
        }

        mBinding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mBinding.tvTime.isEnabled = RuleUtil.isPhone(mBinding.etPhone.text.toString()) && !mBinding.tvTime.isRunning
                mBinding.btnRegister.isEnabled =  RuleUtil.isPhone(mBinding.etPhone.text.toString())
                        && mBinding.etCode.text.toString().isNotEmpty()
                        && mBinding.etPwd.text.toString().isNotEmpty()
                        && mBinding.etPwdConfirm.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        mBinding.etCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnRegister.isEnabled =  RuleUtil.isPhone(mBinding.etPhone.text.toString())
                        && mBinding.etCode.text.toString().isNotEmpty()
                        && mBinding.etPwd.text.toString().isNotEmpty()
                        && mBinding.etPwdConfirm.text.toString().isNotEmpty()

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        mBinding.etPwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnRegister.isEnabled =  RuleUtil.isPhone(mBinding.etPhone.text.toString())
                        && mBinding.etCode.text.toString().isNotEmpty()
                        && mBinding.etPwd.text.toString().isNotEmpty()
                        && mBinding.etPwdConfirm.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        mBinding.etPwdConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnRegister.isEnabled =  RuleUtil.isPhone(mBinding.etPhone.text.toString())
                        && mBinding.etCode.text.toString().isNotEmpty()
                        && mBinding.etPwd.text.toString().isNotEmpty()
                        && mBinding.etPwdConfirm.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
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


    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
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

            R.id.btn_register -> {
                if (!RuleUtil.isPhone(mBinding.etPhone.text.toString())) {
                    ToastUtil.toastWaring("请输入正确的手机号")
                    return
                }

                if (mBinding.etCode.text.toString().length < 4) {
                    ToastUtil.toastWaring("请输入正确的验证码")
                    return
                }

                if (!RuleUtil.checkPasswordRule(mBinding.etPwd.text.toString())) {
                    ToastUtil.toastWaring("请输入八位及以上包含字母大小写和数字并且不含特殊字符。")
                    return
                }

                if (mBinding.etPwd.text.toString() != mBinding.etPwdConfirm.text.toString()) {
                    ToastUtil.toastWaring("两次输入的密码不一致")
                    return
                }

                if (!mBinding.cbAgreement.isChecked) {
                    ToastUtil.toastWaring("请勾选阅读并将遵守《用户协议》")
                    return
                }
                hideSoftInput()
                val json = JsonObject()
                json.addProperty("CodeType",mCodeType)
                json.addProperty("Password", mBinding.etPwd.text.toString())
                json.addProperty("SmsCode",mBinding.etCode.text.toString())
                json.addProperty("TelePhone",mBinding.spinner.selectedItem.toString() + mBinding.etPhone.text.toString())
                mPresenter!!.smsRegister(json)
            }

            R.id.tv_agreement -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TREATY_TYPE,"1")
                AgreementFragment.newInstance(this,bundle)
            }

        }
    }

    override fun needSetStatusHeight(): Boolean {
        return false
    }

    override fun doAfterAnim() {
    }
}