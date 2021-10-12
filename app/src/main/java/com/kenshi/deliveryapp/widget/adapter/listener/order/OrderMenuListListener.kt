package com.kenshi.deliveryapp.widget.adapter.listener.order

import com.kenshi.deliveryapp.model.restaurant.food.FoodModel
import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener

//interface 도 interface 를 상속할 수 있구나
interface OrderMenuListListener: AdapterListener {

    fun onRemoveItem(model: FoodModel)
}