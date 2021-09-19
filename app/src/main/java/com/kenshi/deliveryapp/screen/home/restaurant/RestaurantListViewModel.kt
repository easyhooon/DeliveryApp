package com.kenshi.deliveryapp.screen.home.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kenshi.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.kenshi.deliveryapp.model.restaurant.RestaurantModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private val restaurantRepository: RestaurantRepository
): BaseViewModel() {
    //live data 의 형태로 구독하면 뿌려줌
    //편의성을 위해 MutableLiveData 를 생성 해서
    //Fragment 쪽에서 구독처리 하도록 만들어줌
    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    //fetchData 를 이용
    override fun fetchData(): Job = viewModelScope.launch {
        //코루틴 블록에서 실행되야하는 함수 이므로
        val restaurantList = restaurantRepository.getList(restaurantCategory)
        restaurantListLiveData.value = restaurantList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantTitle = it.restaurantTitle,
                restaurantCategory = it.restaurantCategory,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange
            )
        }
    }
}