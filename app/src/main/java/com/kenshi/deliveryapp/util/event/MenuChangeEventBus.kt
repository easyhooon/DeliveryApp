package com.kenshi.deliveryapp.util.event

import com.kenshi.deliveryapp.screen.main.MainTabMenu
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

//AppModule 에 추가하여 주입하여 여러 곳에 쓸 수 있게
class MenuChangeEventBus {

    private val _mainTabMenuFlow = MutableSharedFlow<MainTabMenu>()
    val mainTabMenuFlow = _mainTabMenuFlow.asSharedFlow()

    //flow 의 emit 을 호출할때 문제없이 호출해주기 위함
    suspend fun changeMenu(menu: MainTabMenu) {
        _mainTabMenuFlow.emit(menu)
    }
}