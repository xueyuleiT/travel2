package com.jtcxw.glcxw.ui.qr

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
import com.jtcxw.glcxw.base.respmodels.SmsBean
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.respmodels.VerifySmsBean
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.databinding.FragmentOpenQrBinding
import com.jtcxw.glcxw.presenters.impl.OpenQrPresenter
import com.jtcxw.glcxw.ui.login.FindPwdFragment
import com.jtcxw.glcxw.ui.my.AuthFragment
import com.jtcxw.glcxw.viewmodel.CommonModel
import com.jtcxw.glcxw.views.OpenQrView
import me.yokeyword.fragmentation.SupportFragment

class OpenQrFragment:BaseFragment<FragmentOpenQrBinding,CommonModel>() ,OpenQrView{

    override fun onOpenQRCodeSucc(jsonObject: JsonObject) {
        if (jsonObject.has("Flag") && jsonObject.get("Flag").asString == "true") {
            ToastUtil.toastSuccess("开通成功")
            pop()
            if (UserUtil.getUserInfoBean().realNameVerifyFlag == "0") {
                val bundle = Bundle()
                bundle.putString("type" , "openQr")
                AuthFragment.newInstance(this,bundle)
            }
        }
    }

    companion object {
        fun newInstance(fragment: SupportFragment, bundle: Bundle?){
            val openQrFragment = OpenQrFragment()
            openQrFragment.arguments = bundle
            fragment.start(openQrFragment)
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
        ToastUtil.toastSuccess(verifySmsBean.notice)
        val json = JsonObject()
        json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
        mPresenter!!.openQRCode(json)

    }

    override fun getVariableId(): Int {
        return BR.common
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_open_qr
    }
    private val mCodeType = 4

    private var mPresenter:OpenQrPresenter?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar("乘车码开通")
        mPresenter = OpenQrPresenter(this)

        mBinding.tvTime.setOnClickListener(this)
        mBinding.btnNext.setOnClickListener(this)

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
                val json = JsonObject()
                json.addProperty("TelePhone",mBinding.spinner.selectedItem.toString() + mBinding.etPhone.text.toString())
                json.addProperty("CodeType",mCodeType)
                mPresenter!!.sendSmsCode(json)
            }

            R.id.btn_next -> {
                if (!RuleUtil.isPhone(mBinding.etPhone.text.toString())) {
                    ToastUtil.toastWaring("请输入正确的手机号")
                    return
                }

//                if (mBinding.etCode.text.toString().length < 4) {
//                    ToastUtil.toastWaring("请输入正确的验证码")
//                    return
//                }

//                val json = JsonObject()
//                json.addProperty("CodeType",mCodeType)
//                json.addProperty("SmsCode",mBinding.etCode.text.toString())
//                json.addProperty("TelePhone",mBinding.spinner.selectedItem.toString() + mBinding.etPhone.text.toString())
//                mPresenter!!.verifySmsCode(json)

                val json = JsonObject()
                json.addProperty("MemberId",UserUtil.getUserInfoBean().memberId)
                mPresenter!!.openQRCode(json)
            }

            R.id.tv_agreement -> {

            }

        }
    }

    override fun doAfterAnim() {

    }
}