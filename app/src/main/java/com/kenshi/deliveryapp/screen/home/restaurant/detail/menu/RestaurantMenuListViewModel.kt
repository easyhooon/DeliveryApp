package com.kenshi.deliveryapp.screen.home.restaurant.detail.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.kenshi.deliveryapp.model.restaurant.food.FoodModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantMenuListViewModel(
    private val restaurantId: Long,
    private val foodEntityList: List<RestaurantFoodEntity>,
    private val restaurantFoodRepository: RestaurantFoodRepository
): BaseViewModel() {

    val restaurantFoodListLiveData = MutableLiveData<List<FoodModel>>()

    val menuBasketLiveData = MutableLiveData<RestaurantFoodEntity>()

    //pair 에 function type 도 들어갈 수 있구나
    val isClearedNeedInBasketLiveData = MutableLiveData<Pair<Boolean,() -> Unit>>()

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantFoodListLiveData.value = foodEntityList.map {
            FoodModel(
                id = it.hashCode().toLong(),
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = restaurantId,
                foodId = it.id,
                restaurantTitle = it.restaurantTitle
            )
        }
    }

    fun insertMenuInBasket(foodModel: FoodModel) = viewModelScope.launch {
        //식당의 ID 값이 다른 경우, 기존의 담겨진 장바구니 데이터를 모두 삭제 후 추가
        val restaurantMenuListInBasket = restaurantFoodRepository.getFoodMenuListInBasket(restaurantId)
        //0번째 부터 시작하므로 size 를 넣어주면 딱 다음에 추가될 위치
        val foodMenuEntity = foodModel.toEntity(restaurantMenuListInBasket.size)
        //다른 식당의 메뉴인지 체크
        val anotherRestaurantMenuListInBasket =
            restaurantFoodRepository.getAllFoodMenuListInBasket().filter {
                it.restaurantId != restaurantId
            }
        if (anotherRestaurantMenuListInBasket.isNotEmpty() ) {
            //비워져있지 않으므로 비움
            isClearedNeedInBasketLiveData.value = Pair(true, { clearMenuAndInsertNewMenuInBasket(foodMenuEntity)})
        } else {
            //다른 곳에서 담은 것이 없음
            restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
            menuBasketLiveData.value = foodMenuEntity
        }
    }

    private fun clearMenuAndInsertNewMenuInBasket(foodMenuEntity: RestaurantFoodEntity) = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
        //새로 넣어줌
        restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
        menuBasketLiveData.value = foodMenuEntity
    }
}