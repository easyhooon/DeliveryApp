package com.kenshi.deliveryapp.widget.adapter.listener.restaurant

import com.kenshi.deliveryapp.model.restaurant.RestaurantModel

interface RestaurantLikeListListener: RestaurantListListener {

    fun onDislikeItem(model: RestaurantModel)



}