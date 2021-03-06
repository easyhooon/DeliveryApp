package com.kenshi.deliveryapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.location.MapSearchInfoEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.data.preference.AppPreferenceManager
import com.kenshi.deliveryapp.data.repository.map.DefaultMapRepository
import com.kenshi.deliveryapp.data.repository.map.MapRepository
import com.kenshi.deliveryapp.data.repository.order.DefaultOrderRepository
import com.kenshi.deliveryapp.data.repository.order.OrderRepository
import com.kenshi.deliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import com.kenshi.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.kenshi.deliveryapp.data.repository.restaurant.food.DefaultRestaurantFoodRepository
import com.kenshi.deliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.kenshi.deliveryapp.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import com.kenshi.deliveryapp.data.repository.restaurant.review.RestaurantReviewRepository
import com.kenshi.deliveryapp.data.repository.user.DefaultUserRepository
import com.kenshi.deliveryapp.data.repository.user.UserRepository
import com.kenshi.deliveryapp.screen.home.HomeViewModel
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantCategory
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantListViewModel
import com.kenshi.deliveryapp.screen.home.restaurant.detail.RestaurantDetailViewModel
import com.kenshi.deliveryapp.screen.home.restaurant.detail.menu.RestaurantMenuListViewModel
import com.kenshi.deliveryapp.screen.home.restaurant.detail.review.RestaurantReviewListViewModel
import com.kenshi.deliveryapp.screen.like.RestaurantLikeListViewModel
import com.kenshi.deliveryapp.screen.main.MainViewModel
import com.kenshi.deliveryapp.screen.my.MyViewModel
import com.kenshi.deliveryapp.screen.mylocation.MyLocationViewModel
import com.kenshi.deliveryapp.screen.order.OrderMenuListViewModel
import com.kenshi.deliveryapp.util.event.MenuChangeEventBus
import com.kenshi.deliveryapp.util.provider.DefaultResourcesProvider
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named

val appModule = module {

    //Singleton ?????? ????????? ??????
    //Koin ??? ?????? ??????

    //AppModule ?????? ????????? ???????????? ?????????????????? ?????????(???????????? ???????????? ?????????) ?????? ?????????
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get(), get() , get()) }
    viewModel { MyViewModel(get(), get(), get()) }
    viewModel { RestaurantLikeListViewModel(get()) }

    //restaurantCategory ??? ????????? ?????? ????????? ????????? ?????????
    //constructor ??? ?????? restaurant ??????????????? ????????? ?????? ??????
    factory { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }

    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        //??????????????? ????????? ???????????? ????????? ????????? ?????? get()?????? ????????? ???????
        MyLocationViewModel(mapSearchInfoEntity, get(), get())}

    viewModel { (restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity, get(), get())}

    //?????? ??????????????? ??????????????? ?????????
    viewModel { (restaurantId: Long, restaurantFoodList:List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(restaurantId, restaurantFoodList, get())}

    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }

    viewModel { OrderMenuListViewModel(get(), get())}


    // < > <-- ?????? ??????
    //??? ?????? ???(????????????) ??????
    single<RestaurantRepository> { DefaultRestaurantRepository ( get(), get(), get()) }
    single<MapRepository> {DefaultMapRepository(get(), get())}
    single<UserRepository> {DefaultUserRepository(get(), get(),get())}
    single<RestaurantFoodRepository> {DefaultRestaurantFoodRepository(get(), get(), get())}
    single<RestaurantReviewRepository> {DefaultRestaurantReviewRepository(get(), get())}
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }

    single() { provideGsonConvertFactory()}
    single() { buildOkHttpClient() }

    single(named("map")) { provideMapRetrofit(get(), get())}
    //??? ????????? ??? ????????? ????????? ????????? ?????? ????????? Koin ??? named ?????? ????????? ??????
    //?????? ??????????????? named ??? ?????? ????????????
    single(named("food")) { provideFoodRetrofit(get(), get())}

    //????????? ????????? ????????? ?????? ??? ????????? DefaultRepository ?????? ?????? ?????? ??????
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

    single { MenuChangeEventBus() }

    single { Firebase.firestore }
    single { FirebaseAuth.getInstance() }
    single { FirebaseStorage.getInstance() }
}