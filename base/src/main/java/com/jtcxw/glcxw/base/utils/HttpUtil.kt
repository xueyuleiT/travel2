package com.jtcxw.glcxw.base.utils

import com.jtcxw.glcxw.base.api.ApiCallbackWithOutBaseBean
import com.jtcxw.glcxw.base.api.ApiClient
import com.jtcxw.glcxw.base.basic.BaseFragment
import com.jtcxw.glcxw.base.dialogs.BaseDialogFragment
import com.jtcxw.glcxw.base.listeners.RefreshCallback
import com.jtcxw.glcxw.base.localmodels.PubKeyBean
import retrofit2.Response
import rx.Observable
import rx.Subscriber

class HttpUtil {
    companion object{

        open fun <M> addSubscription(
            observable:Observable<Response<M>>, subscriber: Subscriber<Response<M>>,
            fragment: BaseFragment<*, *>,
            refreshCallback: RefreshCallback?
        ) {
            if (RSAUtil.pKey == "") {
                fragment.addSubscription(ApiClient.retrofitCreate().publicKey(),object : ApiCallbackWithOutBaseBean<PubKeyBean, Response<PubKeyBean>>(){
                    override fun onSuccess(model: PubKeyBean) {
                        if (model.code == 200) {
                            RSAUtil.pKey = model.data.replace("-----END PUBLIC KEY-----","")
                                .replace("-----BEGIN PUBLIC KEY-----","")
                                .replace("\n","").replace("\r","")
                            fragment.addSubscription(observable, subscriber)
                        }else {
                            refreshCallback?.onRefreshBack(false)
                        }
                    }

                    override fun onFailure(msg: String?) {
                        refreshCallback?.onRefreshBack(false)
                    }

                    override fun onFinish() {
                    }

                })
            } else {
                fragment.addSubscription(observable, subscriber)
            }
//            if (checkExpire()){
//                fragment.addSubscription(observable,subscriber)
//            }else {
//                refreshToken(object :RefreshCallback{
//                    override fun onRefreshBack(refreshSucc: Boolean) {
//                        if (refreshSucc){
//                            fragment.addSubscription(observable,subscriber)
//                        }else{
//                            refreshCallback?.onRefreshBack(refreshSucc)
//                        }
//                    }
//
//                })
//            }
        }

        open fun <M> addSubscription(
            observable:Observable<Response<M>>, subscriber: Subscriber<Response<M>>,
            fragment: BaseDialogFragment,
            refreshCallback: RefreshCallback?
        ) {
            if (RSAUtil.pKey == "") {
                fragment.addSubscription(ApiClient.retrofitCreate().publicKey(),object : ApiCallbackWithOutBaseBean<PubKeyBean, Response<PubKeyBean>>(){
                    override fun onSuccess(model: PubKeyBean) {
                        if (model.code == 200) {
                            RSAUtil.pKey = model.data.replace("-----END PUBLIC KEY-----","")
                                .replace("-----BEGIN PUBLIC KEY-----","")
                                .replace("\n","").replace("\r","")
                            fragment.addSubscription(observable, subscriber)
                        }else {
                            refreshCallback?.onRefreshBack(false)
                        }
                    }

                    override fun onFailure(msg: String?) {
                        refreshCallback?.onRefreshBack(false)
                    }

                    override fun onFinish() {
                    }

                })
            } else {
                fragment.addSubscription(observable, subscriber)
            }
//            if (checkExpire()){
//                fragment.addSubscription(observable,subscriber)
//            }else {
//                refreshToken(object :RefreshCallback{
//                    override fun onRefreshBack(refreshSucc: Boolean) {
//                        if (refreshSucc){
//                            fragment.addSubscription(observable,subscriber)
//                        }else{
//                            refreshCallback?.onRefreshBack(refreshSucc)
//                        }
//                    }
//
//                })
//            }
        }


//        private fun checkExpire():Boolean{
//            if (UserUtil.getUser().expiresIn != 0L) {
//                if (UserUtil.getUser().expiresIn!! - System.currentTimeMillis() > 0 && UserUtil.getUser().expiresIn!! - System.currentTimeMillis() <= 10 * 60 * 1000) {
//                    refreshToken(null)
//                    return true
//                }else if (UserUtil.getUser().expiresIn!! - System.currentTimeMillis() <= 0){
//                    return false
//                }
//            }
//            return true
//        }
//        var sExecutorService: ExecutorService? = null
//        @Synchronized private fun refreshToken(refreshCallback: RefreshCallback?){
//
//            if (TextUtils.isEmpty(CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_REFRESH_TOKEN))) {
//                return
//            }
//
//            if (sExecutorService == null){
//                sExecutorService = Executors.newSingleThreadExecutor()
//            }
//
//            sExecutorService!!.execute(Runnable {
//                try {
//                    if (UserUtil.getUser().expiresIn != 0L) {
//                        if (UserUtil.getUser().expiresIn!! - System.currentTimeMillis() > 10 * 60 * 1000) {
//                            BaseUtil.topAct!!.runOnUiThread {
//                                refreshCallback?.onRefreshBack(true)
//                            }
//                            return@Runnable
//                        }
//                    }
//                    val response =
//                        OAuth2Client.Builder("klb", "klbApi", (RouterUtil.get(RouterPaths.CLASS_APP) as IAppRouter).getAuth2Api())
//                            .build()
//                            .refreshAccessToken(CacheUtil.getInstance().getProperty(SPKeys.SP_KEY_REFRESH_TOKEN))
//
//                    if (response.isSuccessful) {
//                        BaseUtil.topAct?.runOnUiThread {
//                            UserUtil.getUser().save(response)
//                            refreshCallback?.onRefreshBack(true)
//                        }
//
//                    } else {
//                        BaseUtil.topAct?.runOnUiThread {
//                            refreshCallback?.onRefreshBack(false)
//                        }
//                        val error = response.oAuthError
//                        BaseUtil.topAct?.runOnUiThread {
//                            if (response.code == 401) {
//                                BaseUtil.clearSp(UserUtil.getUser().telePhone)
//                                UserUtil.getUser().clear()
//                                (RouterUtil.get(RouterPaths.CLASS_APP) as IAppRouter).goLogin("登录状态失效,请重新登录")
//                            }else {
//                                if (!TextUtils.isEmpty(error.errorDescription)) {
//                                    ToastUtil.toastError(error.errorDescription)
//                                }else{
//                                    if (!TextUtils.isEmpty(response.body) && response.isJsonResponse){
//                                        val json = JSONObject(response.body)
//                                        ToastUtil.toastError(json.optString("message"))
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }catch (e: Exception) {
//                    BaseUtil.topAct?.runOnUiThread {
//                        refreshCallback?.onRefreshBack(false)
//                        ToastUtil.toastError("其他错误")
//                    }
//                }
//            })
//
//        }

    }
}
