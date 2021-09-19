package com.kenshi.deliveryapp.data.repository.map

import com.kenshi.deliveryapp.data.entity.LocationLatLngEntity
import com.kenshi.deliveryapp.data.network.MapApiService
import com.kenshi.deliveryapp.data.response.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMapRepository(
    //MapApiService
    //getReverseGeoCode 를 사용하기 위해 mapApiService 를 주입받음
    private val mapApiService: MapApiService,
    private val ioDispatcher: CoroutineDispatcher,
): MapRepository {
    override suspend fun getReverseGeoInfo(
        locationLatLngEntity: LocationLatLngEntity
    ): AddressInfo? = withContext(ioDispatcher) {
        val response = mapApiService.getReverseGeoCode(
            lat = locationLatLngEntity.latitude,
            lon = locationLatLngEntity.longitude
        )

        if (response.isSuccessful) {
            response.body()?.addressInfo
        } else {
            null
        }
    }
}