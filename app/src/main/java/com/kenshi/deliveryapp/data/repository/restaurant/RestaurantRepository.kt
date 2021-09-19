package com.kenshi.deliveryapp.data.repository.restaurant

import com.kenshi.deliveryapp.data.entity.RestaurantEntity
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantCategory

interface RestaurantRepository {

    //코루틴으로 구현할 예정
    suspend fun getList(
        restaurantCategory: RestaurantCategory,
    ): List<RestaurantEntity>
}