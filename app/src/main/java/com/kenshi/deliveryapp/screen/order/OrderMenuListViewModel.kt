package com.kenshi.deliveryapp.screen.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.data.repository.order.DefaultOrderRepository
import com.kenshi.deliveryapp.data.repository.order.OrderRepository
import com.kenshi.deliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.kenshi.deliveryapp.model.CellType
import com.kenshi.deliveryapp.model.restaurant.food.FoodModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderMenuListViewModel(
    //리스트를 가져오기 위한 의존성 주입
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val orderRepository: OrderRepository,
): BaseViewModel() {

    //firebaseAuth 주입
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    val orderMenuStateLiveData = MutableLiveData<OrderMenuState>(OrderMenuState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        //state pattern 을 통해
        //Loading 이후에 repository 에서 리스트 불러와서 modelList 반영
        orderMenuStateLiveData.value = OrderMenuState.Loading
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        orderMenuStateLiveData.value = OrderMenuState.Success(
            foodMenuList.map {
                FoodModel(
                    id  = it.hashCode().toLong(),
                    type = CellType.ORDER_FOOD_CELL,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    restaurantId = it.restaurantId,
                    foodId = it.id,
                    restaurantTitle = it.restaurantTitle
                )
            }
        )
    }

    fun clearOrderMenu() = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
        fetchData()
    }

    fun removeOrderMenu(foodModel: FoodModel) = viewModelScope.launch {
        restaurantFoodRepository.removeFoodMenuListInBasket(foodModel.foodId)
        fetchData()
    }

    //주문하기
    fun orderMenu() = viewModelScope.launch {
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        if (foodMenuList.isNotEmpty()) {
            val restaurantId = foodMenuList.first().restaurantId
            val restaurantTitle = foodMenuList.first().restaurantTitle
            firebaseAuth.currentUser?.let { user ->
                when(val data = orderRepository.orderMenu(user.uid, restaurantId, foodMenuList, restaurantTitle)) {
                    is DefaultOrderRepository.Result.Success<*> -> {
                        restaurantFoodRepository.clearFoodMenuListInBasket()
                        orderMenuStateLiveData.value = OrderMenuState.Order
                    }
                    is DefaultOrderRepository.Result.Error -> {
                        orderMenuStateLiveData.value = OrderMenuState.Error(
                            R.string.request_error, data.e
                        )
                    }
                }
            } ?: kotlin.run {
                orderMenuStateLiveData.value = OrderMenuState.Error(
                    R.string.user_id_not_found, IllegalAccessException()
                )
            }
        }
    }
}