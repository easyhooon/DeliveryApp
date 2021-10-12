package com.kenshi.deliveryapp.data.repository.user

import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity

interface UserRepository {

    //HomeViewModel 쪽에서 getUserLocation 이라는 함수를 쓸 수 있도록 만들어줘야 함
    //-> HomeViewModel 에 userRepository 의존성 추가
    // constructor parameter 에 userRepository 를 추가함
    suspend fun getUserLocation(): LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)

    //mocking data 를 가져오기 때문에 id 보다 title 이 비교적(그나마) 더 정확함
    //title 에 해당하는 entity 가 저장안되어있을 수 있기 때문에
    suspend fun getUserLikedRestaurant(restaurantTitle: String): RestaurantEntity?

    suspend fun getAllUserLikedRestaurantList(): List<RestaurantEntity>

    suspend fun insertUserLikedRestaurant(restaurantEntity: RestaurantEntity)

    suspend fun deleteUserLikedRestaurant(restaurantTitle: String)

    suspend fun deleteAllUserLikedRestaurant()
}