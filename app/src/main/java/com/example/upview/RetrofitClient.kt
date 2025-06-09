package com.example.upview

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

//    private val loggingInterceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor)
//        .build()
//
//    val apiService: CoinGeckoApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://api.coingecko.com/api/v3/")
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(CoinGeckoApiService::class.java)
//    }
}
