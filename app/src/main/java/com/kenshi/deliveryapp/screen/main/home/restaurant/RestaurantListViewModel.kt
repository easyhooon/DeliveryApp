package com.kenshi.deliveryapp.screen.main.home.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.kenshi.deliveryapp.model.restaurant.RestaurantModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private var locationLatLng: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository,
    //뷰모델이 생성될 때 디폴트 값이 들어값
    private var restaurantOrder: RestaurantOrder = RestaurantOrder.DEFAULT
): BaseViewModel() {
    //live data 의 형태로 구독하면 뿌려줌
    //편의성을 위해 MutableLiveData 를 생성 해서
    //Fragment 쪽에서 구독처리 하도록 만들어줌

//    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    private var _restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()
    val restaurantListLiveData: LiveData<List<RestaurantModel>>
        get() = _restaurantListLiveData

    //fetchData 를 이용
    override fun fetchData(): Job = viewModelScope.launch {
        //코루틴 블록에서 실행되야하는 함수 이므로
        val restaurantList = restaurantRepository.getList(restaurantCategory, locationLatLng)
        val sortedList = when (restaurantOrder) {
            RestaurantOrder.DEFAULT -> {
                restaurantList
            }
            RestaurantOrder.LOW_DELIVERY_TIP -> {
                restaurantList.sortedBy {
                    it.deliveryTipRange.first
                }
            }
            RestaurantOrder.FAST_DELIVERY -> {
                restaurantList.sortedBy {
                    it.deliveryTimeRange.first
                }
            }
            RestaurantOrder.TOP_RATE -> {
                restaurantList.sortedByDescending {
                    it.grade
                }
            }
        }

        _restaurantListLiveData.value = sortedList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantTitle = it.restaurantTitle,
                restaurantCategory = it.restaurantCategory,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange,
                restaurantTelNumber = it.restaurantTelNumber
            )
        }
    }

    fun setLocationLatLng(locationLatLngEntity: LocationLatLngEntity) {
        this.locationLatLng = locationLatLngEntity
        //바뀐값을 기반으로 호출
        fetchData()
    }

    fun setRestaurantOrder(order: RestaurantOrder) {
        this.restaurantOrder = order
        fetchData()
    }
}