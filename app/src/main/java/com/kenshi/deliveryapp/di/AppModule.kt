package com.kenshi.deliveryapp.di

import com.kenshi.deliveryapp.screen.main.home.HomeViewModel
import com.kenshi.deliveryapp.screen.main.my.MyViewModel
import com.kenshi.deliveryapp.util.provider.DefaultResourcesProvider
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel

val appModule = module {

    //Singleton 으로 받아서 처리
    //Koin 에 대한 주입

    //AppModule 내에 각각의 뷰모델을 추가시켜주지 않으면(하나라도 주입하지 않으면) 에러 발생함
    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }

    single { provideGsonConvertFactory()}
    single { buildOkHttpClient() }

    single { provideRetrofit(get(), get())}

    single <ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO}
    single { Dispatchers.Main}
}