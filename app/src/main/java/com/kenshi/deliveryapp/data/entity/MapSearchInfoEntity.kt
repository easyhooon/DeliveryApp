package com.kenshi.deliveryapp.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MapSearchInfoEntity (
    val fullAddress: String,
    val name:String, //건물명
    val locationLatLng: LocationLatLngEntity
): Parcelable