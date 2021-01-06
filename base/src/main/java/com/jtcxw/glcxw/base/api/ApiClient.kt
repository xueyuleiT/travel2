package com.jtcxw.glcxw.base.api

import com.jtcxw.glcxw.api.ApiService
import com.jtcxw.glcxw.base.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


//object声明单例模式
object ApiClient {
    private var sEncryptApiService : ApiService? = null
    private var sEncryptClient :OkHttpClient.Builder? = null
    fun retrofit(): ApiService {
        return retrofitEncrypt()
    }

    fun retrofitCreate(): ApiService {
        synchronized(ApiClient::class.java) {
           val client = OkHttpClient.Builder()
                .callTimeout(30L,TimeUnit.SECONDS)
                .readTimeout(30L,TimeUnit.SECONDS)
                .writeTimeout(30L,TimeUnit.SECONDS)

            val basicInterceptor = BasicInterceptor()
            basicInterceptor.setEncrypt(false)
            client!!.addInterceptor(basicInterceptor)

            if (BuildConfig.DEBUG) {
                // Log信息拦截器
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                //设置 Debug Log 模式
                client!!.addInterceptor(loggingInterceptor)

            }
            val okHttpClient = client!!.build()
            val retrofit = Retrofit.Builder()
                .baseUrl(ApiService.API_SERVER_URL.path)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

    private fun retrofitEncrypt(): ApiService {

        if (sEncryptApiService == null) {
            synchronized(ApiClient::class.java) {
                if (sEncryptApiService == null) {
                    sEncryptClient = OkHttpClient.Builder()
                        .callTimeout(30L,TimeUnit.SECONDS)
                        .readTimeout(30L,TimeUnit.SECONDS)
                        .writeTimeout(30L,TimeUnit.SECONDS)

                    val basicInterceptor = BasicInterceptor()
                    sEncryptClient!!.addInterceptor(basicInterceptor)

                    if (BuildConfig.DEBUG) {
                        // Log信息拦截器
                        val loggingInterceptor = HttpLoggingInterceptor()
                        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                        //设置 Debug Log 模式
                        sEncryptClient!!.addInterceptor(loggingInterceptor)

                    }

                    val okHttpClient = sEncryptClient!!.build()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(ApiService.API_SERVER_URL.path)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .client(okHttpClient)
                        .build()
                    sEncryptApiService = retrofit.create(ApiService::class.java)
                }
            }
        }

        return sEncryptApiService!!
    }

}