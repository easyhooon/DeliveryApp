package com.kenshi.deliveryapp.screen.mylocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.location.MapSearchInfoEntity
import com.kenshi.deliveryapp.data.repository.map.MapRepository
import com.kenshi.deliveryapp.data.repository.user.UserRepository
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MyLocationViewModel(
    private val mapSearchInfoEntity: MapSearchInfoEntity,
    private val mapRepository: MapRepository,
    //Room 기반 (변경된 위치를 저장, 저장된 위치와 GPS 위치가 다를때 알림을 통해 알려줌)
    private val userRepository: UserRepository
): BaseViewModel() {

    //uninitialized 로 초기화
    val myLocationStateLiveData = MutableLiveData<MyLocationState>(MyLocationState.Uninitialized)

    //fetch -> 가져오다
    override fun fetchData(): Job = viewModelScope.launch {
        myLocationStateLiveData.value = MyLocationState.Loading
        myLocationStateLiveData.value = MyLocationState.Success(
            mapSearchInfoEntity
        )
    }

    fun changeLocationInfo(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        val addressInfo = mapRepository.getReverseGeoInfo(locationLatLngEntity)
        //info 를 람다로 받음
        addressInfo?.let {  info ->
            myLocationStateLiveData.value = MyLocationState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLngEntity)
            )
        } ?: kotlin.run {
            myLocationStateLiveData.value = MyLocationState.Error(
                R.string.can_not_load_address_info
            )
        }
    }


    fun confirmSelectLocation() = viewModelScope.launch {
        when(val data = myLocationStateLiveData.value) {
            is MyLocationState.Success -> {
                //현재 유저 위치 반영 로직
                //Room 기반
                //confirm 상태로 변경
                //유저의 location 을 관리하는 로직 필요
                userRepository.insertUserLocation(data.mapSearchInfoEntity.locationLatLng)
                //현재 유저의 위치와 변경된 위치가 상이할 경우 데이터를 불러오눈 로작
                myLocationStateLiveData.value = MyLocationState.Confirm(
                    data.mapSearchInfoEntity
                )
           }
        }
    }
}