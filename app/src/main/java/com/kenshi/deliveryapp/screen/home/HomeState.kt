package com.kenshi.deliveryapp.screen.home

import androidx.annotation.StringRes
import com.kenshi.deliveryapp.data.entity.location.MapSearchInfoEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity

//State Pattern by Sealed Class
sealed class HomeState {

    //property 가 없어도 되면 object type 으로
    object Uninitialized: HomeState()

    object Loading: HomeState()

    //위치를 불러오게 되면(성공적으로 데이터를 불러왔을 때)
    //위치(검색) 정보를 넣어주기 위한 용도
    //data class 로 변경 예정
    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity,
        val isLocationSame: Boolean,
        val foodMenuListInBasket: List<RestaurantFoodEntity>? = null
    ): HomeState()

    data class Error(
        @StringRes val messageId: Int,
    ): HomeState()
}