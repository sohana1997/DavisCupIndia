package com.os.daviscupindia.network.api

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitHelper {

    val baseUrl = "http://tennisinindia.com:2023/"

    @RequiresApi(Build.VERSION_CODES.N)
    fun getInstance(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .build()
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}


