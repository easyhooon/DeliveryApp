package com.kenshi.deliveryapp.widget.adapter.listener.order

import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener

interface OrderListListener: AdapterListener {

    fun writeRestaurantReview(orderId: String, restaurantTitle: String)
}