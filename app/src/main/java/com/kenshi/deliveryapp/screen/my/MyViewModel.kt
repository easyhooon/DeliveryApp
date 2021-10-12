package com.kenshi.deliveryapp.screen.my

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.data.entity.OrderEntity
import com.kenshi.deliveryapp.data.preference.AppPreferenceManager
import com.kenshi.deliveryapp.data.repository.order.DefaultOrderRepository
import com.kenshi.deliveryapp.data.repository.order.OrderRepository
import com.kenshi.deliveryapp.data.repository.user.UserRepository
import com.kenshi.deliveryapp.model.order.OrderModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    //AppPreferenceManager 코드를 사용
    private val appPreferenceManager: AppPreferenceManager,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
): BaseViewModel() {

    val myStateLiveData = MutableLiveData<MyState>(MyState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        myStateLiveData.value = MyState.Loading
        appPreferenceManager.getIdToken()?.let {
            myStateLiveData.value = MyState.Login(it)
        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }
    }

    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope. launch {
        firebaseUser?.let { user ->
            when(val orderMenusResult = orderRepository.getAllOrderMenus(user.uid)) {
                is DefaultOrderRepository.Result.Success<*> -> {
                    val orderList = orderMenusResult.data as List<OrderEntity>
                    myStateLiveData.value = MyState.Success.Registered(
                        userName = user.displayName ?: "익명",
                        profileImageUri = user.photoUrl,
                        orderList = orderList.map {
                            OrderModel(
                                id = it.hashCode().toLong(),
                                orderId = it.id,
                                userId = it.userId,
                                restaurantId = it.restaurantId,
                                foodMenuList = it.foodMenuList,
                                restaurantTitle = it.restaurantTitle
                            )
                        }
                    )
                }
                is DefaultOrderRepository.Result.Error -> {
                    myStateLiveData.value = MyState.Error(
                        R.string.request_error,
                        orderMenusResult.e
                    )
                }
            }
        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }
    }

    fun signOut() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.removeIdToken()
        }
        //장바구니에 담았었던 데이터 날려주기
        userRepository.deleteAllUserLikedRestaurant()
        fetchData()
    }
}