package com.jtcxw.glcxw.base.api

import android.text.TextUtils
import com.glcxw.lib.util.constants.RouterPaths
import com.glcxw.router.IAppRouter
import com.glcxw.router.RouterUtil
import org.json.JSONObject
import retrofit2.Response
import retrofit2.adapter.rxjava.HttpException
import rx.Subscriber
import java.util.regex.Pattern

abstract class ApiCallbackWithOutBaseBean<T,M: Response<T>> : Subscriber<M>() {
    abstract fun onSuccess(model: T)
    abstract fun onFailure(msg: String?)
    abstract fun onFinish()

    override fun onCompleted() {
        onFinish()
    }

    override fun onNext(m: M) {
        if ( m.code() == 200 && m?.body() != null) {
            onSuccess(m.body()!!)
        }else{
            if (m.code() == 401){
                var waring = "请重新登录"
                if (m.errorBody() != null) {
                    val json = JSONObject(m.errorBody()!!.string())
                    waring = json.optString("Data")
                }
                (RouterUtil.get(RouterPaths.CLASS_APP) as IAppRouter).goLogin(waring)
            }else {
                if (m?.errorBody() != null) {
                    val error = m.errorBody()?.byteString()
                    if (error != null) {
                        val errorUtf8 = error.utf8()
                        val json = JSONObject(errorUtf8)
                        val message = json.optString("message")
                        if (!TextUtils.isEmpty(message)){
                            onFailure(message)
                        } else if (checkExceptionStartWithChar(errorUtf8)) {
                            onError(Throwable(errorUtf8))
                        } else {
                            onError(Throwable("网络或数据异常"))
                        }
                    } else {
                        onError(Throwable("网络或数据异常"))
                    }
                } else {
                    onError(Throwable("网络或数据异常"))
                }
            }
        }
    }

    override fun onError(e: Throwable?) {
        if (e is HttpException) {
            val code = e.code()
            var msg = e.message
            if (code == 401) {
                (RouterUtil.get(RouterPaths.CLASS_APP) as IAppRouter).goLogin("请重新登录")
                return
            }
            onFailure(msg)
        } else {
            try {
                val json = JSONObject(e!!.message)
                val message = json.optString("message")
                if (!TextUtils.isEmpty(message)){
                    if (checkExceptionStartWithChar(message)) {
                        onFailure(message)
                    } else {
                        onFailure("网络或数据异常")
                    }
                }
            }catch (e1:Exception){
                if (e!!.message != null) {
                    if (checkExceptionStartWithChar(e!!.message!!)) {
                        onFailure(e!!.message)
                    } else {
                        onFailure("网络或数据异常")
                    }
                }
            }
        }
        onFinish()
    }

    private fun checkExceptionStartWithChar(msg: String):Boolean{
        val china = Pattern.compile("[\\u4e00-\\u9fa5]")
        return china.matcher(msg[0].toString()).matches()

    }

}