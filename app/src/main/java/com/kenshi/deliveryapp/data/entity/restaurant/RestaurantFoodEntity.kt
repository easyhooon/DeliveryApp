package com.kenshi.deliveryapp.data.entity.restaurant

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.kenshi.deliveryapp.data.entity.Entity
import kotlinx.parcelize.Parcelize

//데이터의 형태로 담아서 번들, 인텐트로 넘겨 처리해야 할 수 있기 때문에 parcelize
@Parcelize
//해당 어노테이션을 추가해줘야 Dao 의 쿼리 문에서 인식을 할 수 있음
@androidx.room.Entity
data class RestaurantFoodEntity (
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val restaurantTitle: String
): Parcelable