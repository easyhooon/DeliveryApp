package com.kenshi.deliveryapp.screen.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.data.entity.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.MapSearchInfoEntity
import com.kenshi.deliveryapp.data.repository.map.MapRepository
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository
): BaseViewModel() {

    companion object {
        const val MY_LOCATION_KEY = "MyLocation"
    }

    //liveData 를 이용해서 State 를 관리
    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    fun loadReverseGeoInfo(locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        homeStateLiveData.value = HomeState.Loading
        val addressInfo = mapRepository.getReverseGeoInfo(locationLatLngEntity)
        //info 를 람다로 받음
        addressInfo?.let {  info ->
            homeStateLiveData.value = HomeState.Success(
                mapSearchInfo = info.toSearchInfoEntity(locationLatLngEntity)
            )
        } ?: kotlin.run {
            homeStateLiveData.value = HomeState.Error(
                R.string.can_not_load_address_info
            )
        }
    }

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when(val data = homeStateLiveData.value){
            is HomeState.Success -> {
                return data.mapSearchInfo
            }
        }
        return null
    }
}