package com.kenshi.deliveryapp.screen.mylocation

import com.kenshi.deliveryapp.data.entity.MapSearchInfoEntity

sealed class MyLocationState {

    object Uninitialized: MyLocationState()

    object Loading: MyLocationState()


    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    //위치가 변경이 되도 최종적으로 확인이 된 것에 대해서 넘겨주는 상태 값이 필요
    data class Confirm(
        //마지막 위치를 넘겨줌
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

}