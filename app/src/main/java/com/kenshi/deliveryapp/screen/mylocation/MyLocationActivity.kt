package com.kenshi.deliveryapp.screen.mylocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.maps.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.location.MapSearchInfoEntity
import com.kenshi.deliveryapp.databinding.ActivityMyLocationBinding
import com.kenshi.deliveryapp.screen.base.BaseActivity
import com.kenshi.deliveryapp.screen.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

//구글 맵을 사용
class MyLocationActivity: BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>(), OnMapReadyCallback {

    companion object {
        const val CAMERA_ZOOM_LEVEL = 17f

        //Intent 생성 코드
        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KEY, mapSearchInfoEntity)
            }
    }

//    override val viewModel: MyLocationViewModel
//        get() = TODO("Not yet implemented")

    //Koin ViewModel 초기화 방법 3번째 꺼 import org.koin.android.viewModel.ext.android.viewModel
    override val viewModel by viewModel<MyLocationViewModel> {
        //parameter 를 받아서 처리해주기 위해
        //myLocationViewModel 에서 해당 키 값을 기반으로 데이터를 가져옴
        parametersOf(
            intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
        )
    }

    override fun getViewBinding() = ActivityMyLocationBinding.inflate(layoutInflater)

    private lateinit var map: GoogleMap

    private var isMapInitialized: Boolean = false
    private var isChangeLocation: Boolean = false

    //구글 맵에서 사용할 수 있는 뷰 객체를 초기화
    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
//            finish()
            onBackPressed()
        }
        confirmButton.setOnClickListener {
            viewModel.confirmSelectLocation()
        }
        setupGoogleMap()
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map ?: return
        viewModel.fetchData()
    }

    private fun setupGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync (this)
    }

    //state 기반으로 data 를 적용
    override fun observeData() {
        viewModel.myLocationStateLiveData.observe(this) {

            when (it) {
                is MyLocationState.Loading -> {
                    handleLoadingState()
                }
                is MyLocationState.Success -> {
                    if (::map.isInitialized) {
                        handleSuccessState(it)
                    }
                }
                is MyLocationState.Confirm -> {
                    //위치를 바꾼 후 confirm button 을 누르면 confirm 상태로 변함(종료)
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(HomeViewModel.MY_LOCATION_KEY, it.mapSearchInfoEntity)
                    })
                    finish()
                }
                is MyLocationState.Error -> {
                    Toast.makeText(this, it.messageId, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }

    private fun handleLoadingState() = with(binding) {
        locationLoading.isVisible = true
        locationTitleTextView.text = getString(R.string.loading)
    }

    private fun handleSuccessState(state: MyLocationState.Success) = with(binding) {
        val mapSearchInfo = state.mapSearchInfoEntity
        locationLoading.isGone = true
        locationTitleTextView.text = mapSearchInfo.fullAddress
        //map 이 초기화가 되었는지 확인하는 로직이 필요
        //이 것을 기반으로 카메라를 어떻게 움직일지 세팅
        //처음엔 적절한 거리로 현재 위치를 보여줘여 함
        if (isMapInitialized.not()) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapSearchInfo.locationLatLng.latitude,
                        mapSearchInfo.locationLatLng.longitude
                    ), CAMERA_ZOOM_LEVEL
                )
            )

            //현재 지도가 멈춰있는지를 판단(중지 후 1초동안 더 움직임이 없으면 그때 api call 하도록)
            map.setOnCameraIdleListener {
                //맵의 위치가 바꼈다고 판단
                if(isChangeLocation.not()) {
                    isChangeLocation = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        val cameraLatLng = map.cameraPosition.target
                        viewModel.changeLocationInfo(
                            LocationLatLngEntity(
                                cameraLatLng.latitude,
                                cameraLatLng.longitude
                            )
                        )
                        isChangeLocation = false
                    }, 1000)
                }
            }
            isMapInitialized = true
        }
    }
}