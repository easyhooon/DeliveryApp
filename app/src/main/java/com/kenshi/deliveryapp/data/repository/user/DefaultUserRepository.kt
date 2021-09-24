package com.kenshi.deliveryapp.data.repository.user

import com.kenshi.deliveryapp.data.db.dao.LocationDao
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

//UserRepository 를 구현하는 구현체
//interface 라 ()가 없다
class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher
    ): UserRepository {

    override suspend fun getUserLocation(): LocationLatLngEntity? = withContext(ioDispatcher) {
        //default value
        locationDao.get(-1)
    }

    override suspend fun insertUserLocation(
        locationLatLngEntity: LocationLatLngEntity
    ) = withContext(ioDispatcher) {
        locationDao.insert(locationLatLngEntity)
    }
}