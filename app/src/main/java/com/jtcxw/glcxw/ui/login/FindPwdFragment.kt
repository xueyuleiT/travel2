package com.jtcxw.glcxw.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
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
import com.jtcxw.glcxw.base.respmodels.SmsBean
import com.jtcxw.glcxw.base.respmodels.VerifySmsBean
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentFindPwdBinding
import com.jtcxw.glcxw.presenters.impl.FindPwdPresenter
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.FindPwdView
import me.yokeyword.fragmentation.SupportFragment

class FindPwdFragment: BaseFragment<FragmentFindPwdBinding, CommonModel>(),FindPwdView {
    companion object {
        fun newInstance(fragment:SupportFragment,bundle: Bundle?){
            val findPwdFragment = FindPwdFragment()
            findPwdFragment.arguments = bundle
            fragment.start(findPwdFragment)
        }
    }
    override fun onSendSmsCodeSucc(smsBean: SmsBean) {
        if (smsBean.SendFlag) {
            ToastUtil.toastSuccess(smsBean.Notice)
            mBinding.tvTime.task.startWithType(mCodeType)
        } else {
            ToastUtil.toastWaring(smsBean.Notice)
        }
    }



    override fun onVerifySmsCodeSucc(verifySmsBean: VerifySmsBean) {
        if (verifySmsBean.isVerifyFlag) {
            ToastUtil.toastSuccess(verifySmsBean.notice)
            val bundle = Bundle()
            if (!TextUtils.isEmpty(UserUtil.getUserInfoBean().realTelphoneNo)) {
                bundle.putString(
                    BundleKeys.KEY_PHONE,
                            UserUtil.getUserInfoBean().realTelphoneNo.replace("+86", "")
                )
            } else {
                bundle.putString(
                    BundleKeys.KEY_PHONE, mBinding.etPhone.text.toString()
                )
            }
            bundle.putString(BundleKeys.KEY_SMS_CODE, mBinding.etCode.text.toString())
            bundle.putString(BundleKeys.KEY_MEMBER_ID, verifySmsBean.memberId)
            ResetPwdFragment.newInstance(this, bundle)
            CacheUtil.getInstance().setProperty(SPKeys.SP_KEY_SMS_TIME + mCodeType,0L)
        } else {
            ToastUtil.toastWaring(verifySmsBean.notice)
        }
    }

    private val mCodeType = 3
    var mPresenter: FindPwdPresenter? = null
    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_find_pwd
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BaseUtil.isDarkMode()) {
            mBinding.vBg.setBackgroundResource(0)
        }

        mPresenter = FindPwdPresenter(this)
        mBinding.tvTime.setOnClickListener(this)
        mBinding.btnNext.setOnClickListener(this)


        if (System.currentTimeMillis() -  CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_SMS_TIME + mCodeType, 0L) < Constant.SMS_TIME * 1000) {
            mBinding.tvTime.task.start(
                Constant.SMS_TIME - (System.currentTimeMillis() -  CacheUtil.getInstance().getProperty(
                    SPKeys.SP_KEY_SMS_TIME + mCodeType, 0L)) / (1000))
        }

        if (arguments != null) {
            mBinding.etPhone.setText(arguments!!.getString(BundleKeys.KEY_PHONE,""))
            if (!TextUtils.isEmpty(UserUtil.getUserInfoBean().realTelphoneNo)) {
                mBinding.etPhone.isEnabled = false
                mBinding.etPhone.setTextColor(resources.getColor(R.color.gray_9))
            }
            mBinding.tvTime.isEnabled = RuleUtil.isPhone( mBinding.etPhone.text.toString()) && !mBinding.tvTime.isRunning
        }

        mBinding.etPhone.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                mBinding.tvTime.isEnabled = RuleUtil.isPhone(mBinding.etPhone.text.toString()) && !mBinding.tvTime.isRunning
                mBinding.btnNext.isEnabled = RuleUtil.isPhone(mBinding.etPhone.text.toString()) && mBinding.etCode.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        mBinding.etCode.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                mBinding.btnNext.isEnabled = RuleUtil.isPhone(mBinding.etPhone.text.toString()) && mBinding.etCode.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        initSpinner()
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
                if (!TextUtils.isEmpty(UserUtil.getUserInfoBean().realTelphoneNo)) {
                    json.addProperty(
                        "TelePhone",
                        UserUtil.getUserInfoBean().realTelphoneNo.replace("+86","")
                    )
                } else {
                    json.addProperty(
                        "TelePhone", mBinding.etPhone.text.toString()
                    )
                }
                json.addProperty("CodeType",mCodeType)
                mPresenter!!.sendSmsCode(json)
            }

            R.id.btn_next -> {
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
                json.addProperty("CodeType",mCodeType)
                json.addProperty("SmsCode",mBinding.etCode.text.toString())
                if (!TextUtils.isEmpty(UserUtil.getUserInfoBean().realTelphoneNo)) {
                    json.addProperty(
                        "TelePhone",
                        UserUtil.getUserInfoBean().realTelphoneNo.replace("+86","")
                    )
                } else {
                    json.addProperty("TelePhone", mBinding.etPhone.text.toString())
                }
                mPresenter!!.verifySmsCode(json)
            }

            R.id.tv_agreement -> {

            }

        }
    }

    override fun needSetStatusHeight(): Boolean {
        return false
    }

    override fun doAfterAnim() {
    }

    private fun initSpinner() {
        val list = ArrayList<String>()
        list.add("+86")
        mBinding.spinner.adapter = MySpinnerAdaqpter(context!!,list)
        mBinding.spinner.setPopupBackgroundResource(R.drawable.shape_r10_cef)
        mBinding.spinner.setSelection(0)

    }
}