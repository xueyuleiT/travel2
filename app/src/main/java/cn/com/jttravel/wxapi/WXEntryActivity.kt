package cn.com.jttravel.wxapi

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import com.google.gson.JsonObject
import com.jtcxw.glcxw.base.api.ApiCallbackWithCode
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseActivity
import com.jtcxw.glcxw.base.constant.BundleKeys
import com.jtcxw.glcxw.base.constant.Constant
import com.jtcxw.glcxw.base.respmodels.UserInfoBean
import com.jtcxw.glcxw.base.respmodels.WechatBean
import com.jtcxw.glcxw.base.utils.*
import com.jtcxw.glcxw.events.LoginEvent
import com.jtcxw.glcxw.fragment.MainFragment
import com.jtcxw.glcxw.ui.MainActivity
import com.jtcxw.glcxw.ui.login.RegisterFragment
import com.jtcxw.glcxw.utils.MySingleCall
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import models.BaseBean
import retrofit2.Response

class WXEntryActivity: BaseActivity(), IWXAPIEventHandler {
    override fun onResp(p0: BaseResp?) {
        if (p0 is SendAuth.Resp) {
            val resp = p0 as SendAuth.Resp
            if (TextUtils.isEmpty(resp.code)) {
                ToastUtil.toastError("微信授权失败")
                finish()
                return
            }

            val loadDialog = DialogUtil.getLoadingDialog(supportFragmentManager)
            val json = JsonObject()
            json.addProperty("WechatCode", resp.code)
            json.addProperty("LoginGuid", DeviceUtil.getDeviceId(BaseUtil.sTopAct))
            json.addProperty("PhoneModel", Build.MODEL)
            addSubscription(ApiClient.retrofitCreate().wechatLogin(json),
                object : ApiCallbackWithCode<WechatBean, Response<BaseBean<WechatBean>>>() {

                    override fun onFinish() {
                        loadDialog.dismiss()
                    }



                    override fun onSuccess(model: BaseBean<WechatBean>) {
                        val wechatBean = model.Data!!
                        if (wechatBean.needToRegister) {
                            val registerFragment = RegisterFragment()
                            val bundle = Bundle()
                            bundle.putString(BundleKeys.KEY_OPEN_ID, wechatBean.wechatOpenId)
                            registerFragment.arguments = bundle
                            BaseUtil.sTopAct!!.start(registerFragment)
                        } else {
                            if (wechatBean.pass && wechatBean.loginEntity.loginOk) {
                                val userInfoBean = UserInfoBean()
                                userInfoBean.memberId = wechatBean.loginEntity.memberId
                                userInfoBean.userName = wechatBean.loginEntity.userName
                                userInfoBean.nikeName = wechatBean.loginEntity.nikeName
                                userInfoBean.profilePhoto = wechatBean.loginEntity.profilePhoto
                                userInfoBean.token = wechatBean.loginEntity.token
                                onSuccess(userInfoBean)
                            } else {
                                ToastUtil.toastError(wechatBean.message)
                            }
                        }
                        finish()
                    }

                    override fun onFailure(code: Int, innerCode: Int, msg: String?) {
                        if (code == 400 || innerCode == 400) {
                            ToastUtil.toastError(msg)
                        }
                        finish()
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        finish()
                    }
                })
        }
    }

    fun onSuccess(userInfoBean:UserInfoBean) {
        UserUtil.getUser().save(userInfoBean)
        UserUtil.isShowLogin = false

        if (BaseUtil.sTopAct is MainActivity){
            Handler().postDelayed({
                RxBus.getDefault().post(LoginEvent())
                MySingleCall.getInstance().doCall()
            },300)
            BaseUtil.sTopAct!!.popTo(MainFragment::class.java,false)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            Handler().postDelayed({
                RxBus.getDefault().post(LoginEvent())
                MySingleCall.getInstance().doCall()
            },300)
        }
    }

    override fun onReq(p0: BaseReq?) {
    }


    var wxAPI: IWXAPI?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wxAPI = WXAPIFactory.createWXAPI(this,Constant.APP_ID_WE_CHAT,false);
        wxAPI!!.registerApp(Constant.APP_ID_WE_CHAT);
        wxAPI!!.handleIntent(intent, this)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        wxAPI!!.handleIntent(intent, this)
    }
}