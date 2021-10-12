package com.kenshi.deliveryapp.data.response.restaurant

import android.os.Parcelable
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity

data class RestaurantFoodResponse(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    //val restaurantId: Long
) {
    //parsing 해서 entity 형태로 바꿔줌
    fun toEntity(restaurantId: Long, restaurantTitle: String) = RestaurantFoodEntity(
        id = id,
        title = title,
        description = description,
        price = price.toDouble().toInt(),
        imageUrl = imageUrl,
        restaurantId = restaurantId,
        restaurantTitle = restaurantTitle
    )
}