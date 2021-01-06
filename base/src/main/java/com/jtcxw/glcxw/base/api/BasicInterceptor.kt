package com.jtcxw.glcxw.base.api

import android.text.TextUtils
import android.util.Log
import com.jtcxw.glcxw.base.BuildConfig
import com.jtcxw.glcxw.base.utils.RSAUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import okhttp3.*
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset


class BasicInterceptor : Interceptor {
    private var isEncrypt = true
    private var needAuth = true


    fun setEncrypt(isEncrypt:Boolean){
        this.isEncrypt = isEncrypt
    }
    @Throws(Exception::class)
    override fun intercept(it: Interceptor.Chain): Response {
        var request: Request = it.request()

        if (request.body != null && request.body is RequestBody && !request.url.toString().contains("/Api/PublicKey")) {
            //判断类型
            val contentType = request.body!!.contentType()
            if (contentType != null) {
                /*如果是二进制上传  则不进行加密*/
                if (contentType!!.type.toLowerCase() != "multipart") {
                    val charset = Charset.forName("UTF-8")
                    val buffer = Buffer()
                    request.body!!.writeTo(buffer)
                    var data: String = buffer.readString(charset).trim()
                    Log.d("okhttp1","" + data)
                    if (!TextUtils.isEmpty(data)) {
                        val json = JSONObject()
                        var dataEncrypt = RSAUtil.aesEncrypt(data)
                        if (dataEncrypt.endsWith("\n")) {
                            dataEncrypt = dataEncrypt.substring(0,dataEncrypt.length - 1)
                        }
                        json.put("Data", dataEncrypt)
                        if (!TextUtils.isEmpty(UserUtil.getUserInfoBean().token)) {
                            json.put("Token", UserUtil.getUserInfoBean().token)
                        }
                        var ASEKey =  RSAUtil.encrypt("123456".toByteArray())
                        if (ASEKey.endsWith("\n")) {
                            ASEKey = ASEKey.substring(0,ASEKey.length - 1)
                        }
                        json.put("AESKey",ASEKey)
                        val newRequestBody =
                            RequestBody.create(contentType, json.toString())
                        val newRequestBuilder = request.newBuilder()

                        when (request.method.toLowerCase().trim()) {
                            "post" -> {
                                newRequestBuilder.post(newRequestBody)
                            }
                            "put" -> {
                                newRequestBuilder.put(newRequestBody)
                            }
                            "delete" -> {
                                newRequestBuilder.delete(newRequestBody)
                            }
                        }
                        request = newRequestBuilder.build()
                    }

                }
            }
        }

        if (needAuth) {
            request = request.newBuilder()
                .addHeader("osType", "Android")
                .addHeader("model", android.os.Build.MODEL)
                .addHeader("sysVersion", android.os.Build.VERSION.RELEASE)
                .addHeader("version", BuildConfig.VERSION_NAME)
                .build()
        }else{
            request = request.newBuilder()
                .addHeader("reqId", System.nanoTime().toString() + (0..100000).random())
                .addHeader("osType", "Android")
                .addHeader("model", android.os.Build.MODEL)
                .addHeader("sysVersion", android.os.Build.VERSION.RELEASE)
                .addHeader("version", BuildConfig.VERSION_NAME)
                .build()
        }
        try {
            var response = it.proceed(request)

            if (response.code == 200 && response.body != null && !request.url.toString().contains("/Api/PublicKey")) {

                val source = response.body!!.source()
                source.request(java.lang.Long.MAX_VALUE)
                val buffer = source.buffer()
                val charset = Charset.forName("UTF-8")
                val bodyString = buffer.clone().readString(charset)
                var json = JSONObject(bodyString)

                val data = json.optString("Data")
                if (data != "null" && data != "") {
                    val decryptStr = RSAUtil.aesDecrypt(data)
                    if (isJson(decryptStr)){
                        json.put("Data", JSONObject(decryptStr))
                    } else if (isJsonArr(decryptStr)) {
                        json.put("Data", JSONArray(decryptStr))
                    }else {
                        json.put("Data", decryptStr)
                    }
                    Log.d("okhttp1",json.toString())
                    val contentType = response.body!!.contentType()
                    val body = ResponseBody.create(contentType, json.toString())
                    response = response.newBuilder().body(body).build()
                } else {
                    json.put("Data",JSONObject())
                    val contentType = response.body!!.contentType()
                    val body = ResponseBody.create(contentType, json.toString())
                    response = response.newBuilder().body(body).build()
                }
            }

            return response
        }catch (e:Exception){
            throw e
        }
    }

    fun isJson(content: String): Boolean {
        return try {
            JSONObject(content)
            true
        } catch (e: Exception) {
            false
        }

    }

    fun isJsonArr(content: String): Boolean {
        return try {
            JSONArray(content)
            true
        } catch (e: Exception) {
            false
        }
    }

//    fun getKey():String{
//        val accessToken = UserUtil.getUser().accessToken
//        val refreshToken = UserUtil.getUser().refreshToken
//        val a = accessToken!!.toByteArray()
//        for (i in a.indices) {
//            a[i] = (a[i] and i.toByte() or refreshToken!![i].toByte())
//        }
//
//        return String(a)
//    }
}