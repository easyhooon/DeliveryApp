package com.kenshi.deliveryapp.screen.home.restaurant.detail.menu

import com.kenshi.deliveryapp.model.restaurant.food.FoodModel

sealed class RestaurantMenuListState {

    object Uninitialized: RestaurantMenuListState()

    object Loading: RestaurantMenuListState()

    data class Success(
        val restaurantFoodModelList: List<FoodModel>? = null
    ): RestaurantMenuListState()

}