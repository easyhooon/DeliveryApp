package com.kenshi.deliveryapp.data.repository.restaurant

import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import com.kenshi.deliveryapp.screen.main.home.restaurant.RestaurantCategory

interface RestaurantRepository {

    //코루틴으로 구현할 예정
    suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity>
}