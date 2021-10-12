package com.kenshi.deliveryapp.screen.home.restaurant.detail

import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity

sealed class RestaurantDetailState {

    object Uninitialized: RestaurantDetailState()

    object Loading: RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val restaurantFoodList: List<RestaurantFoodEntity>? = null,
        //메뉴 리스트에서 장바구니로 담음
        val foodMenuListInBasket: List<RestaurantFoodEntity>? = null,
        val isClearNeedInBasketAndAction: Pair<Boolean, () -> Unit> = Pair(false, {}),

        //처음에는 없으니까 null 값이 될 수 있음
        val isLiked: Boolean? = null
    ): RestaurantDetailState()

}
