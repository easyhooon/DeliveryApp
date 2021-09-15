package com.kenshi.deliveryapp.screen.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//base 의 경우 되도록이면 abstract 하게
abstract class BaseViewModel : ViewModel() {

    //내부적으로 데이터를 관리하기 위해

    //Hilt 의 경우에는 viewModelFactory 를 통해서
    //Activity 나 여러 LifeCycle 을 가진 컴포넌트들의 상태를
    //번들의 형태로 관리해줄 수 있는 기술이 존재

    //Koin 을 사용하여 번들을 Android component(Activity 나 fragment) 의
    //lifecycle 에 따라 저장, 관리
    protected var stateBundle: Bundle? = null

    //공통적으로 호출했었을 때 이 함수를 통해 데이터를 가공
    open fun fetchData(): Job = viewModelScope.launch{}

    //뷰의 대한 상태를 저장하기 위해서
    open fun storeState(stateBundle: Bundle) {
        this.stateBundle = stateBundle
    }

    //-> lifecycle 이 destroy 가 되기 전까지
    //activity,fragment 가 종료가 되기 전 까지는
    //stateBundle 을 통해 데이터를 유지
}