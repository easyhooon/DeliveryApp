package com.kenshi.deliveryapp.model.restaurant.food

import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.model.CellType
import com.kenshi.deliveryapp.model.Model

data class FoodModel(
    //FoodModel 을 위한 id
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val foodId: String,
    val restaurantTitle: String
): Model(id, type) {
    //식당 상세화면에서의 FoodEntity
    //고유의 foodEntity 의 ID 값을 넣어주기 위한 함수
    //장바구니에 한 메뉴에 대해서 여러번 넣어줄 수 있게 하기위해 basketIdx 를 만들어줌
    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}",
        title,
        description,
        price,
        imageUrl,
        restaurantId,
        restaurantTitle
    )
}