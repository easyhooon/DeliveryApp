package com.kenshi.deliveryapp.model.order

import com.kenshi.deliveryapp.data.entity.OrderEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.model.CellType
import com.kenshi.deliveryapp.model.Model

data class OrderModel(
    override val id: Long,
    override val type: CellType = CellType.ORDER_CELL,
    val orderId: String,
    val userId: String,
    val restaurantId: Long,
    val foodMenuList: List<RestaurantFoodEntity>,
    val restaurantTitle: String
): Model(id, type) {

    fun toEntity() = OrderEntity(
        id = orderId,
        userId = userId,
        restaurantId = restaurantId,
        foodMenuList = foodMenuList,
        restaurantTitle = restaurantTitle
    )
}
