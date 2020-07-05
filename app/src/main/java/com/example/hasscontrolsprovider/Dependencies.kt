package com.example.hasscontrolsprovider

import com.example.hasscontrolsprovider.network.HassRestService
import com.example.hasscontrolsprovider.network.HassWebSocketClient
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object Dependencies {

    private val moshi = Moshi.Builder().build()

    private val authInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .header("Authorization", "Bearer ${Config.AUTH_TOKEN}")
            .build()
        chain.proceed(newRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        )
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Config.HASS_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val hassRestService: HassRestService = retrofit.create(HassRestService::class.java)

    val hassWebSocketClient = HassWebSocketClient(
        HassWebSocketClient.Config(Config.HASS_URL, Config.AUTH_TOKEN),
        okHttpClient,
        moshi
    )

}