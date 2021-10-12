package com.kenshi.deliveryapp.data.entity.restaurant

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kenshi.deliveryapp.data.entity.Entity
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantCategory
import com.kenshi.deliveryapp.util.converter.RoomTypeConverters
import kotlinx.parcelize.Parcelize

//데이터의 형태로 담아서 번들, 인텐트로 넘겨 처리해야 할 수 있기 때문에 parcelize
@Parcelize
//해당 어노테이션을 추가해줘야 Dao 의 쿼리 문에서 인식을 할 수 있음
@androidx.room.Entity

//Pair 형태의 property 들은 converter 를 이용하지 않으면 room 에서 저장을 할 수 없기 때문에
//저장 가능한 형태로 변환을 해줘야 함
@TypeConverters(RoomTypeConverters::class)
data class RestaurantEntity (
    //Entity 를 override
    //고유 ID
    override val id: Long,
    //식당의 id 를 넣어주기 위한
    //API 호출의 용도
    val restaurantInfoId: Long,
    //view pager 를 여러가지 탭들로 나누어 검색을 수행 검색의 용도
    val restaurantCategory:RestaurantCategory,
    @PrimaryKey val restaurantTitle: String,
    val restaurantImageUrl:String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange:Pair<Int,Int>,
    val deliveryTipRange: Pair<Int,Int>,
    val restaurantTelNumber: String?
): Entity, Parcelable