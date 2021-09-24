package com.kenshi.deliveryapp.data.repository.user

import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity

interface UserRepository {

    //HomeViewModel 쪽에서 getUserLocation 이라는 함수를 쓸 수 있도록 만들어줘야 함
    //-> HomeViewModel 에 userRepository 의존성 추가
    // constructor parameter 에 userRepository 를 추가함
    suspend fun getUserLocation(): LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)
}