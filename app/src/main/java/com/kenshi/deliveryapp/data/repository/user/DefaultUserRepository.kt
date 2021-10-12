package com.kenshi.deliveryapp.data.repository.user

import com.kenshi.deliveryapp.data.db.dao.LocationDao
import com.kenshi.deliveryapp.data.db.dao.RestaurantDao
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

//UserRepository 를 구현하는 구현체
//interface 라 ()가 없다
class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val restaurantDao: RestaurantDao,
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

    override suspend fun getUserLikedRestaurant(restaurantTitle: String): RestaurantEntity? = withContext(ioDispatcher) {
        restaurantDao.get(restaurantTitle)
    }

    override suspend fun getAllUserLikedRestaurantList(): List<RestaurantEntity> = withContext(ioDispatcher) {
        restaurantDao.getAll()
    }

    override suspend fun insertUserLikedRestaurant(restaurantEntity: RestaurantEntity) = withContext(ioDispatcher) {
        restaurantDao.insert(restaurantEntity)
    }

    override suspend fun deleteUserLikedRestaurant(restaurantTitle: String) = withContext(ioDispatcher) {
        restaurantDao.delete(restaurantTitle)
    }

    override suspend fun deleteAllUserLikedRestaurant() = withContext(ioDispatcher) {
        restaurantDao.deleteAll()
    }

}