package com.kenshi.deliveryapp.screen.my

import android.net.Uri
import androidx.annotation.StringRes

sealed class MyState {

    object Uninitialized: MyState()

    object Loading: MyState()

    data class Login(
        val idToken: String,
    ): MyState()

    //idToken 값을 현재 저장한 상태면 세션을 갖고 있다는 의미
    //(Token 값을 갖고)세션을 갖고 로그인 시도를 할 때
    //Registered State 로 넘김
    //Token 값이 없으면
    //Not Registered State 로 넘김

    sealed class Success: MyState() {
        data class Registered(
            val userName: String,
            val profileImageUri: Uri?
        ): Success()

        object NotRegistered: Success()
    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): MyState()
}
