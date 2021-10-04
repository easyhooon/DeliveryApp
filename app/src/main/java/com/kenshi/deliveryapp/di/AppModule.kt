package com.kenshi.deliveryapp.di

import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.location.MapSearchInfoEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.data.preference.AppPreferenceManager
import com.kenshi.deliveryapp.data.repository.map.DefaultMapRepository
import com.kenshi.deliveryapp.data.repository.map.MapRepository
import com.kenshi.deliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import com.kenshi.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.kenshi.deliveryapp.data.repository.restaurant.food.DefaultRestaurantFoodRepository
import com.kenshi.deliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.kenshi.deliveryapp.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import com.kenshi.deliveryapp.data.repository.restaurant.review.RestaurantReviewRepository
import com.kenshi.deliveryapp.data.repository.user.DefaultUserRepository
import com.kenshi.deliveryapp.data.repository.user.UserRepository
import com.kenshi.deliveryapp.screen.main.home.HomeViewModel
import com.kenshi.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.kenshi.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import com.kenshi.deliveryapp.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.kenshi.deliveryapp.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import com.kenshi.deliveryapp.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import com.kenshi.deliveryapp.screen.main.MainViewModel
import com.kenshi.deliveryapp.screen.my.MyViewModel
import com.kenshi.deliveryapp.screen.mylocation.MyLocationViewModel
import com.kenshi.deliveryapp.util.provider.DefaultResourcesProvider
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named

val appModule = module {

    //Singleton 으로 받아서 처리
    //Koin 에 대한 주입

    //AppModule 내에 각각의 뷰모델을 추가시켜주지 않으면(하나라도 주입하지 않으면) 에러 발생함
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get(), get() , get()) }
    viewModel { MyViewModel(get()) }

    //restaurantCategory 를 필요로 하기 때문에 람다로 넘겨줌
    //constructor 를 통해 restaurant 카테고리를 다음과 같이 구성
//    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
//        RestaurantListViewModel(restaurantCategory, locationLatLng, get(), )
//    }

    factory { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }

    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        //파라미터로 명시한 클래스의 경우가 아니면 모두 get()으로 퉁치는 건가?
        MyLocationViewModel(mapSearchInfoEntity, get(), get())}

    viewModel { (restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity, get(), get())}

    //람다 표현식으로 파라미터를 넣어줌
    viewModel { (restaurantId: Long, restaurantFoodList:List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(restaurantId, restaurantFoodList, get())}

    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }


    // < > <-- 반환 타입
    //두 개의 값(파라미터) 주입
    single<RestaurantRepository> { DefaultRestaurantRepository ( get(), get(), get()) }
    single<MapRepository> {DefaultMapRepository(get(), get())}
    single<UserRepository> {DefaultUserRepository(get(), get(),get())}
    single<RestaurantFoodRepository> {DefaultRestaurantFoodRepository(get(), get(), get())}
    single<RestaurantReviewRepository> {DefaultRestaurantReviewRepository(get())}

    single() { provideGsonConvertFactory()}
    single() { buildOkHttpClient() }

    single(named("map")) { provideMapRetrofit(get(), get())}
    //이 두가지 의 객체의 용도의 차이를 주기 위해서 Koin 의 named 라는 함수를 사용
    //같은 타입이지만 named 에 의해 나뉘어짐
    single(named("food")) { provideFoodRetrofit(get(), get())}

    //서비스 객체가 생성이 되고 그 객체를 DefaultRepository 에서 주입 받아 사용
    single { provideMapApiService(get(qualifier = named("map"))) }
    single { provideFoodApiService(get(qualifier = named("food")))}

    single { provideDB(androidApplication())}
    single { provideLocationDao(get())}
    single { provideRestaurantDao(get())}
    single { provideFoodMenuBasketDao(get())}


    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }
    single { AppPreferenceManager(androidApplication()) }

    single { Dispatchers.IO}
    single { Dispatchers.Main}
}