package com.jtcxw.glcxw.ui.login

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.glcxw.lib.util.CacheUtil
import com.glcxw.lib.util.RuleUtil
import com.glcxw.lib.util.constants.SPKeys
import com.google.gson.JsonObject
import com.jtcxw.glcxw.BR
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.respmodels.PicVerifyCodeBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.utils.*
import com.jtcxw.glcxw.databinding.FragmentLoginBinding
import com.jtcxw.glcxw.events.LoginEvent
import com.jtcxw.glcxw.presenters.impl.LoginPresenter
import com.jtcxw.glcxw.ui.MainActivity
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.LoginView
import com.toptechs.libaction.action.SingleCall
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment

class LoginFragment: BaseFragment<FragmentLoginBinding, CommonModel>(),LoginView {
    override fun onLoginFailed(msg: String) {
        mPresenter!!.loginVerifyCode(mBinding.etUser.text.toString())
    }

    override fun onLoginSucc(userInfoBean: UserInfoBean) {
        ToastUtil.toastSuccess(userInfoBean.loginMsg)
        UserUtil.getUser().save(userInfoBean)
        UserUtil.isShowLogin = false
        if (BaseUtil.sTopAct is MainActivity){
            pop()
            RxBus.getDefault().postDelay(LoginEvent(),300)
            SingleCall.getInstance().doCall()
        } else {
            startActivity(Intent(activity!!, MainActivity::class.java))
            mBinding.etCode.postDelayed({
                activity!!.finish()
            }, 300)
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment,bundle: Bundle?) {
            val loginFragment = LoginFragment()
            loginFragment.arguments = bundle
            fragment.start(loginFragment, ISupportFragment.SINGLETOP)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.tv_policy -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TREATY_TYPE,"2")
                AgreementFragment.newInstance(this,bundle)
            }

            R.id.tv_agreement -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.KEY_TREATY_TYPE,"1")
                AgreementFragment.newInstance(this,bundle)
            }

            R.id.iv_code -> {
                if (!RuleUtil.isPhone(mBinding.etUser.text.toString())) {
                    ToastUtil.toastWaring("请输入正确的手机号")
                    return
                }
                mPresenter!!.loginVerifyCode(mBinding.etUser.text.toString())
            }
            R.id.iv_phone -> {
                var bundle:Bundle? = null
                if (RuleUtil.isPhone(mBinding.etUser.text.toString())) {
                    bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_PHONE,mBinding.etUser.text.toString())
                }
                SmsLoginFragment.newInstance(this,bundle)
            }
            R.id.tv_find_pwd -> {
                var bundle:Bundle? = null
                if (RuleUtil.isPhone(mBinding.etUser.text.toString())) {
                    bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_PHONE,mBinding.etUser.text.toString())
                }
                FindPwdFragment.newInstance(this,bundle)
            }
            R.id.tv_register -> {
                var bundle:Bundle? = null
                if (RuleUtil.isPhone(mBinding.etUser.text.toString())) {
                    bundle = Bundle()
                    bundle.putString(BundleKeys.KEY_PHONE,mBinding.etUser.text.toString())
                }
                RegisterFragment.newInstance(this,bundle)
            }

            R.id.btn_login -> {
                if (!RuleUtil.isPhone(mBinding.etUser.text.toString())) {
                    ToastUtil.toastWaring("请输入正确的手机号")
                    return
                }

                if (mBinding.etCode.text.toString().length < 4) {
                    ToastUtil.toastWaring("请输入正确的验证码")
                    return
                }

                if (TextUtils.isEmpty(mBinding.etPwd.text.toString())) {
                    ToastUtil.toastWaring("请输入正确的密码")
                    return
                }
                hideSoftInput()
                val json = JsonObject()
                json.addProperty("Account", mBinding.etUser.text.toString())
                json.addProperty("Password", MD5Util.md5(mBinding.etPwd.text.toString()))
                json.addProperty("VCode",mBinding.etCode.text.toString())
                json.addProperty("LoginGuid", DeviceUtil.getDeviceId(context))
                json.addProperty("PhoneModel", Build.MODEL)
                json.addProperty("RegId", JPushInterface.getRegistrationID(context))
                mPresenter!!.login(json)
            }
        }
    }


    var hasCode = false
    override fun onLoginVerifyCodeSucc(json: PicVerifyCodeBean) {
        val decodeArr = Base64.decode(json.base64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodeArr,0,decodeArr.size)
        mBinding.ivCode.setImageBitmap(bitmap)
        mBinding.etCode.tag = mBinding.etUser.text.toString()
    }

    override fun onLoginVerifyCodeFailed(msg: String) {
    }

    override fun onLoginVerifyCodeFinish() {
    }

    private var mPresenter:LoginPresenter ?= null
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (BaseUtil.isDarkMode()) {
            mBinding.vBg.setBackgroundResource(0)
        }

        mBinding.etUser.setText(CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_TELEPHONE,""))
    }

    override fun doAfterAnim() {
        mPresenter = LoginPresenter(this)
        mBinding.tvFindPwd.setOnClickListener(this)
        mBinding.tvRegister.setOnClickListener(this)
        mBinding.ivPhone.setOnClickListener(this)
        mBinding.ivCode.setOnClickListener(this)
        mBinding.tvAgreement.setOnClickListener(this)
        mBinding.tvPolicy.setOnClickListener(this)
//        mBinding.etCode.setOnFocusChangeListener { view, b ->
//            if (!RuleUtil.isPhone(mBinding.etUser.text.toString())) {
//                return@setOnFocusChangeListener
//            }
//            if (b && !hasCode) {
//                if (mBinding.etCode.tag != mBinding.etUser.text.toString()) {
//                    mPresenter!!.loginVerifyCode(mBinding.etUser.text.toString())
//                }
//            }
//        }

        mBinding.etPwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnLogin.isEnabled = RuleUtil.isPhone(mBinding.etUser.text.toString())
                        && mBinding.etCode.text.toString().isNotEmpty()
                        && mBinding.etPwd.text.toString().isNotEmpty()            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        mBinding.etUser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnLogin.isEnabled = RuleUtil.isPhone(mBinding.etUser.text.toString())
                        && mBinding.etCode.text.toString().isNotEmpty()
                        && mBinding.etPwd.text.toString().isNotEmpty()            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        mBinding.etCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnLogin.isEnabled = RuleUtil.isPhone(mBinding.etUser.text.toString())
                        && mBinding.etCode.text.toString().isNotEmpty()
                        && mBinding.etPwd.text.toString().isNotEmpty()
            }

        })

        mPresenter!!.loginVerifyCode(mBinding.etUser.text.toString())

        mBinding.btnLogin.setOnClickListener(this)

    }

    override fun needSetStatusHeight(): Boolean {
        return false
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (resultCode == ISupportFragment.RESULT_OK) {
            if (BaseUtil.sTopAct is MainActivity){
                pop()
                RxBus.getDefault().postDelay(LoginEvent(),300)
                SingleCall.getInstance().doCall()
            } else {
                startActivity(Intent(activity!!, MainActivity::class.java))
                mBinding.etCode.postDelayed({
                    activity!!.finish()
                }, 300)
            }
        }
    }
}