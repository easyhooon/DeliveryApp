package com.kenshi.deliveryapp.di

import com.kenshi.deliveryapp.data.repository.map.DefaultMapRepository
import com.kenshi.deliveryapp.data.repository.map.MapRepository
import com.kenshi.deliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import com.kenshi.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.kenshi.deliveryapp.screen.home.HomeViewModel
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantCategory
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantListViewModel
import com.kenshi.deliveryapp.screen.my.MyViewModel
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
    viewModel { HomeViewModel(get()) }
    viewModel { MyViewModel() }

    //restaurantCategory 를 필요로 하기 때문에 람다로 넘겨줌
    //constructor 를 통해 restaurant 카테고리를 다음과 같이 구성
    viewModel { (restaurantCategory: RestaurantCategory) -> RestaurantListViewModel(restaurantCategory, get()) }


    // < > <-- 반환 타입
    //두 개의 값(파라미터) 주입
    single<RestaurantRepository> { DefaultRestaurantRepository ( get(), get()) }
    single<MapRepository> {DefaultMapRepository(get(), get())}

    single { provideGsonConvertFactory()}
    single { buildOkHttpClient() }

    single { provideMapRetrofit(get(), get())}

    //서비스 객체가 생성이 되고 그 객체를 DefaultRepository 에서 주입 받아 사용
    single { provideMapApiService(get()) }


    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO}
    single { Dispatchers.Main}
}