package com.kenshi.deliveryapp.screen.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.location.MapSearchInfoEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.data.repository.map.MapRepository
import com.kenshi.deliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.kenshi.deliveryapp.data.repository.user.UserRepository
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository,
    private val restaurantFoodRepository: RestaurantFoodRepository
): BaseViewModel() {

    companion object {
        const val MY_LOCATION_KEY = "MyLocation"
    }

    //liveData 를 이용해서 State 를 관리
    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    val foodMenuBasketLiveData = MutableLiveData<List<RestaurantFoodEntity>>()

    fun loadReverseGeoInfo(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        homeStateLiveData.value = HomeState.Loading
        //Loading 상태일 때 저장된 userLocation 을 가져옴
        val userLocation = userRepository.getUserLocation()
        val currentLocation = userLocation ?: locationLatLngEntity

        //현재 내 위치가 GPS 기반 위치가 일치하는지 확인하는 property
        //mapRepository 라는 변수를 사용하려면 private val 선언을 해줘야 한다.
        val addressInfo = mapRepository.getReverseGeoInfo(currentLocation)
        //info 를 람다로 받음
        addressInfo?.let {  info ->
            homeStateLiveData.value = HomeState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLngEntity),
                isLocationSame = currentLocation == locationLatLngEntity
            )
        } ?: kotlin.run {
            homeStateLiveData.value = HomeState.Error(
                messageId = R.string.can_not_load_address_info
            )
        }
    }

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when(val data = homeStateLiveData.value){
            is HomeState.Success -> {
                return data.mapSearchInfoEntity
            }
        }
        return null
    }

    fun checkMyBasket() = viewModelScope.launch {
        foodMenuBasketLiveData.value = restaurantFoodRepository.getAllFoodMenuListInBasket()
    }


}