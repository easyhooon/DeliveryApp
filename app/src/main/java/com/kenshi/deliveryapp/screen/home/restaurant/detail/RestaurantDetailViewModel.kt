package com.kenshi.deliveryapp.screen.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.kenshi.deliveryapp.data.repository.user.UserRepository
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity,
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val userRepository: UserRepository

) : BaseViewModel()
{
    //liveData 로  State 를 관리
    val restaurantDetailStateLiveData = MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitialized)

    //coroutine block
    override fun fetchData(): Job = viewModelScope.launch {
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
        restaurantDetailStateLiveData.value = RestaurantDetailState.Loading
        //장바구니에 얼마나 들어있는지 판단
        val foods = restaurantFoodRepository.getFoods(
            restaurantId = restaurantEntity.restaurantInfoId,
            restaurantTitle = restaurantEntity.restaurantTitle)
        val foodMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket()

        //식당들에 좋아요(찜) 상태 활성화 체크(전에 하트를 눌렀던 것들이 다시 들어와서 봐도 하트가 눌려있는지)
        val isLiked = userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle) != null
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity,
            restaurantFoodList = foods,
            foodMenuListInBasket =foodMenuListInBasket,
            isLiked = isLiked
        )
    }

    fun getRestaurantTelNumber(): String? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }
    }

    fun getRestaurantInfo(): RestaurantEntity? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                //restaurantEntity 를 꺼냄
                data.restaurantEntity
            }
            else -> null
        }
    }

    fun toggleLikedRestaurant() = viewModelScope.launch {
        //userRepository 쪽에 접근해서 현재 유저가 해당 식당에 대해서 좋아요를 했는지 안했는지 체크
        //그래서 ViewModel 의 생성자쪽에 userRepository 추가
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                //get 했을 때 존재하면, 이미 좋아요 상태 -> 좋아요 취소
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let{
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                    //반대의 경우, get 했을 때 존재하지 않음 -> 좋아요
                } ?: kotlin.run {
                    userRepository.insertUserLikedRestaurant(restaurantEntity)
                }
            }
        }
    }

    fun notifyFoodMenuListInBasket(restaurantFoodEntity: RestaurantFoodEntity) = viewModelScope.launch {
        //현재 상태가 Success
        //-> 상세화면에 있는 Entity 가 불려와진 형태
        // -> 그 상태에서 basket(장바구니)에 얼마나 담겨있는지 알려주기 위해 변수로 관리

        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                   foodMenuListInBasket = data.foodMenuListInBasket?.toMutableList()?.apply {
                        add(restaurantFoodEntity)
                    }
                )
            }
            else -> Unit
        }
    }

    fun notifyClearNeedAlertInBasket(clearNeed: Boolean, afterAction: () -> Unit) {

        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                   isClearNeedInBasketAndAction = Pair(clearNeed, afterAction)
                    //LiveData 를 구독하여 해당 액션에 대해 alert 호출
                )
            }
            else -> Unit
        }
    }

    fun notifyClearBasket() = viewModelScope.launch {

        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = listOf(),
                    isClearNeedInBasketAndAction = Pair(false, {})
                    //LiveData 를 구독하여 해당 액션에 대해 alert 호출
                )
            }
            else -> Unit
        }

    }
}