package com.kenshi.deliveryapp.screen.mylocation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.data.entity.MapSearchInfoEntity
import com.kenshi.deliveryapp.databinding.ActivityMyLocationBinding
import com.kenshi.deliveryapp.screen.base.BaseActivity
import com.kenshi.deliveryapp.screen.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

//구글 맵을 사용
class MyLocationActivity: BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>() {

//    override val viewModel: MyLocationViewModel
//        get() = TODO("Not yet implemented")

    //Koin ViewModel 초기화 방법 3번째 꺼 import org.koin.android.viewmodel.ext.android.viewModel
    override val viewModel by viewModel<MyLocationViewModel> {
        //parameter 를 받아서 처리해주기 위해
        //myLocationViewModel 에서 해당 키 값을 기반으로 데이터를 가져옴
        parametersOf(
            intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
        )
    }

    //Intent 생성 코드
    companion object {
        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KEY, mapSearchInfoEntity)
            }
    }

   override fun getViewBinding(): ActivityMyLocationBinding = ActivityMyLocationBinding.inflate(layoutInflater)

    override fun initViews() {
        super.initViews()
    }

    //state 기반으로 data 를 적용
    override fun observeData() = viewModel.myLocationStateLiveData.observe(this) {
        when(it) {
            is MyLocationState.Loading -> {

            }
            is MyLocationState.Success -> {

            }
            is MyLocationState.Confirm -> {

            }
            else -> Unit
        }


    }
    }
}