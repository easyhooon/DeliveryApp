package com.kenshi.deliveryapp.model.restaurant

import com.kenshi.deliveryapp.data.entity.RestaurantEntity
import com.kenshi.deliveryapp.model.CellType
import com.kenshi.deliveryapp.model.Model
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantCategory

data class RestaurantModel (
    override val id: Long,
    override val type: CellType = CellType.RESTAURANT_CELL,

    val restaurantInfoId: Long,
    //view pager 를 여러가지 탭들로 나누어 검색을 수행 검색의 용도
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl:String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange:Pair<Int,Int>,
    val deliveryTipRange: Pair<Int,Int>
): Model(id, type) {
    fun toEntity() = RestaurantEntity(
        id,
        restaurantInfoId,
        restaurantCategory,
        restaurantTitle,
        restaurantImageUrl,
        grade,
        reviewCount,
        deliveryTimeRange,
        deliveryTipRange
    )
}