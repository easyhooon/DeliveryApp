package com.kenshi.deliveryapp.data.entity.restaurant

import android.os.Parcelable
import com.kenshi.deliveryapp.data.entity.Entity
import com.kenshi.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import kotlinx.parcelize.Parcelize

//데이터의 형태로 담아서 번들, 인텐트로 넘겨 처리해야 할 수 있기 때문에 parcelize
@Parcelize
data class RestaurantEntity (
    //Entity 를 override
    //고유 ID
    override val id: Long,
    //식당의 id 를 넣어주기 위한
    //API 호출의 용도
    val restaurantInfoId: Long,
    //view pager 를 여러가지 탭들로 나누어 검색을 수행 검색의 용도
    val restaurantCategory:RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl:String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange:Pair<Int,Int>,
    val deliveryTipRange: Pair<Int,Int>
): Entity, Parcelable