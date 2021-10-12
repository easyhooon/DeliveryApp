package com.kenshi.deliveryapp.screen.like

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import com.kenshi.deliveryapp.data.repository.user.UserRepository
import com.kenshi.deliveryapp.model.CellType
import com.kenshi.deliveryapp.model.restaurant.RestaurantModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantLikeListViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch{
        //userRepository 에서 찜 한거 가져옴
        restaurantListLiveData.value = userRepository.getAllUserLikedRestaurantList().map {
            RestaurantModel (
                id = it.id,
                type = CellType.LIKE_RESTAURANT_CELL,
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

    fun dislikeRestaurant(restaurant: RestaurantEntity) = viewModelScope.launch {
        userRepository.deleteUserLikedRestaurant(restaurant.restaurantTitle)
        fetchData()
    }
}