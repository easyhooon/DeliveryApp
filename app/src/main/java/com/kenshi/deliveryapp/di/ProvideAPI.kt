package com.kenshi.deliveryapp.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//api 의 대한 연동
fun provideRetrofit (
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("")
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

}

fun provideGsonConvertFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

//OKHttp 도 반환이 필요
fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()

    if(BuildConfig.DEBUG) {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        interceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}