package com.kenshi.deliveryapp.data.repository.map

import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.response.address.AddressInfo

interface MapRepository {

    suspend fun getReverseGeoInfo(
        locationLatLngEntity: LocationLatLngEntity
    ): AddressInfo?
}