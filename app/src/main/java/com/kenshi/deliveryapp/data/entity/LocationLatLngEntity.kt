package com.kenshi.deliveryapp.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationLatLngEntity (
    val latitude: Double,
    val longitude:Double,
    override val id: Long = -1, //Default value, 필요가 없는 관계로
): Entity, Parcelable
//Entity 는 id 가 존재해야 한다.