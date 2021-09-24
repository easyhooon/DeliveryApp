package com.kenshi.deliveryapp.data.entity.location

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.kenshi.deliveryapp.data.entity.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@androidx.room.Entity
data class LocationLatLngEntity (
    val latitude: Double,
    val longitude:Double,
    @PrimaryKey(autoGenerate = true)
    override val id: Long = -1, //Default value, 필요가 없는 관계로
): Entity, Parcelable
//Entity 는 id 가 존재해야 한다.