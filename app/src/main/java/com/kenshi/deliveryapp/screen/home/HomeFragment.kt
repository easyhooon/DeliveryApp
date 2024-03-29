package com.kenshi.deliveryapp.screen.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.data.entity.location.MapSearchInfoEntity
import com.kenshi.deliveryapp.databinding.FragmentHomeBinding
import com.kenshi.deliveryapp.screen.base.BaseFragment
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantCategory
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantListFragment
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantOrder
import com.kenshi.deliveryapp.screen.main.MainActivity
import com.kenshi.deliveryapp.screen.main.MainTabMenu
import com.kenshi.deliveryapp.screen.mylocation.MyLocationActivity
import com.kenshi.deliveryapp.screen.order.OrderMenuListActivity
import com.kenshi.deliveryapp.widget.adapter.RestaurantListFragmentPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    companion object {

        val locationPermissions = arrayOf(
            //GPS, Network
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        const val TAG = "HomeFragment"

        fun newInstance() = HomeFragment()
    }

//    override val viewModel: HomeViewModel
//        get() = TODO("Not yet implemented")

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    //koin viewModel
    override val viewModel by viewModel<HomeViewModel>()

    private lateinit var locationManager: LocationManager

    private lateinit var myLocationListener: MyLocationListener

    private lateinit var viewPagerAdapter : RestaurantListFragmentPagerAdapter

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    //ActivityResultForResult 대체
    //activity result 를 받아오기 위한 launcher
    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        //callback 을 받음
            if(result.resultCode == Activity.RESULT_OK){
                //result 의 데이저 정보를 기반으로 검색한 정보를 가져옴
                //위치를 한번 더 불러옴
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)?.let { myLocationInfo ->
                    viewModel.loadReverseGeoInfo(myLocationInfo.locationLatLng)
                }
            }
        }

    //Permission 에 대해서 받아오는 launcher
    //androidx 에서 requestCode 에 대해서 deprecated 되었기 때문에
    //registerForActivityResult 를 사용하기 위한 용도
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            //현재 받아온 permission 이 들어가 있는지 체크
            val responsePermissions = permissions.entries.filter {
                //key, value 형태로 되어있는 거에 key 값이 있는지 체크
                //value 를 통해 permission 이 enabled 되어있는지 검증(optional)
                it.key == Manifest.permission.ACCESS_FINE_LOCATION
                        || it.key == Manifest.permission.ACCESS_COARSE_LOCATION
            }
            if (responsePermissions.filter { it.value == true }.size == locationPermissions.size) {
                //권한이 부여됨
                // locationListener 등록
                setMyLocationListener()
            } else {
                // 권한 설정 되지 않음
                // 위치 권한 필요
                with(binding.locationTitleTextView) {
                    text = getString(R.string.please_request_location_permission)
                    setOnClickListener{
                        getMyLocation()
                    }
                }
                Toast.makeText(requireContext(), R.string.can_not_assigned_permission, Toast.LENGTH_SHORT).show()
            }
        }

    override fun initViews() = with(binding) {
        locationTitleTextView.setOnClickListener {
            //현재 위치 값을 가져와서 그 위치 값으로 activity 를 실행시킬 수 있도록
            //MyLocationActivity
            //viewModel 에서 현재 검색한 위치 정보에 대해 있는지 판별을 하고
            //그것을 기반으로 activity 를 실행
            viewModel.getMapSearchInfo()?.let{ mapInfo ->
                changeLocationLauncher.launch(
                    //액티비티를 실행 시켜줌
                    MyLocationActivity.newIntent(
                        requireContext(), mapInfo
                    )
                )
            }
        }

        orderChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipDefault -> {
                    chipInitialize.isGone = true
                    changeRestaurantOrder(RestaurantOrder.DEFAULT)
                }
                R.id.chipInitialize -> {
                    chipDefault.isChecked = true
                }
                R.id.chipDeliveryTip -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.LOW_DELIVERY_TIP)
                }
                R.id.chipFastDelivery -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.FAST_DELIVERY)
                }
                R.id.chipTopRate -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.TOP_RATE)
                }
            }
        }
    }


    private fun changeRestaurantOrder(order: RestaurantOrder) {
        viewPagerAdapter.fragmentList.forEach {
            it.viewModel.setRestaurantOrder(order)
        }
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()){
            //LocationManager 가 초기화 되어있지 않으면,
            // 컨텍스트에 접근하여 getSystemService 로 Location Service 를 가져온 뒤에 locationManger 로 캐스팅
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        // locationManager 가 초기화 되었으면 GPS 가 켜져있는 것을 확인
        // 켜져 있으면, Permission 에 대한 권한 체크를 할 수 있는 launcher를 실행
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(isGpsEnable) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    //locationListener 등록 함수
    //이미 permission 허용한 상태로 가정
    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L //1.5초
        val minDistance = 100f //최소 거리 100 미터

        if(::myLocationListener.isInitialized.not()){
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    //위치 권한을 가져와 데이터를 뿌려주는 작업 수행
    override fun observeData() {
        viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
            //fetch Data 시에 데이터를 받을 수 있도록 처리
            //하위에 뷰페이저를 달아 뷰페이저에서 데이터를 처리
            //뷰페이저로 fragment 를 만들어 데이터를 넣어줄 것

            //현재 위치를 불러오는 코드
            //권한이 없으면 권한을 요청하고, 있으면 현재 위치를 불러옴
            when(it) {
                //is -> checks that a value has a certain type
                is HomeState.Uninitialized -> {
                    //초기화가 되어있지 않음 -> 권한이 없음
                    getMyLocation()
                }
                is HomeState.Loading -> {
                    binding.locationLoading.isVisible = true
                    binding.locationTitleTextView.text = getString(R.string.loading)

                }
                is HomeState.Success -> {
                    binding.locationLoading.isGone = true
                    binding.locationTitleTextView.text = it.mapSearchInfoEntity.fullAddress
                    binding.tabLayout.isVisible = true
                    binding.filterScrollView.isVisible = true
                    binding.viewPager.isVisible = true
                    initViewPager(it.mapSearchInfoEntity.locationLatLng)
                    if (it.isLocationSame.not()) {
                        Toast.makeText(requireContext(), R.string.please_set_your_current_location, Toast.LENGTH_SHORT).show()
                    }
                    Log.d(TAG, "현재 위치는 ${binding.locationTitleTextView.text} 입니다.")
                }
                is HomeState.Error -> {
                    binding.locationLoading.isGone = true
                    binding.locationTitleTextView.setText(R.string.location_not_found)

                    //이게 문제였다
                    //에러 상태일때만 해당 클릭리스너가 달리므로 클릭리스너가 중복으로 달려있는 것이 아니다. 케이스마다 다르게 존재하는 것
                    //binding.locationTitleTextView.setOnClickListener {
                    //        getMyLocation()
                    //}
                    Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, it.messageId.toString())
                }
                else -> Unit
            }
        }

        viewModel.foodMenuBasketLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.basketButtonContainer.isVisible = true
                binding.basketCountTextView.text = getString(R.string.basket_count, it.size)
                binding.basketButton.setOnClickListener {
                    if (firebaseAuth.currentUser == null) {
                        alertLoginNeed {
                            (requireActivity() as MainActivity).goToTab(MainTabMenu.MY)
                        }
                    } else {
                        startActivity(
                            OrderMenuListActivity.newIntent(requireActivity())
                        )
                    }
                }
            } else {
                binding.basketButtonContainer.isGone = true
                binding.basketButton.setOnClickListener (null)
            }
        }
    }

    //인자로 lambda function 을 넘겨줌
    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("로그인이 필요합니다")
            .setMessage("주문하려면 로그인이 필요합니다. My탭으로 이동하시겠습니까?")
            .setPositiveButton("이동") {dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") {dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        //fetchData() 를 호출, 상태값에 대해서 갱신
        //내 장바구니에 포함된 음식 수 체크
        viewModel.checkMyBasket()
    }

    //메인화면의 식당리스트는 뷰페이저 형태로 구성되어있는데 이 뷰페이저에 하나의 프래그먼트를 넣어서
    //식당 데이터를 mocking data 를 이용해서 불러옴
    private fun initViewPager(locationLatLng: LocationLatLngEntity) = with(binding) {
        orderChipGroup.isVisible = true
        val restaurantCategories = RestaurantCategory.values()

        if(::viewPagerAdapter.isInitialized.not()){
            //뷰페이저가 초기화되고 난 이후에 Chip group 을 보여줌
            val restaurantListFragmentList = restaurantCategories.map{
                //위치 변경 후 위치에 맞게 데이터를 다시 넘겨주는 로직
                RestaurantListFragment.newInstance(it, locationLatLng)
            }

            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList,
                locationLatLng
            )
            //viewPager Fragment 에 장착
            viewPager.adapter = viewPagerAdapter

            //한번 만들어지면 매번 페이지가 바뀔때마다 프래그먼트를 다시 만드는 것이 아니라 계속 쓸 수 있도록
            //위치가 바뀌면 api 다시 호출해주는 코드 필요
            viewPager.offscreenPageLimit = restaurantCategories.size
            //탭 레이아웃에 탭 들을 뿌려줄 수 있도록 구성
            //콜백으로 tab, position 을 넘겨받을 수 있음
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach() //<- 빼먹는 거 주의
        }
        if (locationLatLng != viewPagerAdapter.locationLatLng) {
            //다르면 새 값을 넣어줌
            viewPagerAdapter.locationLatLng = locationLatLng
            viewPagerAdapter.fragmentList.forEach {
                //위치 값이 변경이 되었다는 것을 viewModel 에 알려줌
                it.viewModel.setLocationLatLng(locationLatLng)
            }
        }
    }

    private fun removeLocationListener() {
        if(::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    inner class MyLocationListener: LocationListener {

        override fun onLocationChanged(location: Location) {
//            binding.locationTitleTextView.text = "${location.latitude}, ${location.longitude}"
            //T map API 를 이용하여 이위도, 경도 좌표를 리버스 지오코딩이라는 방법을 통해 위치 정보를 불러옴
            Log.d(TAG, "MyLocation = ${location.latitude}, ${location.longitude}")
            viewModel.loadReverseGeoInfo(
                LocationLatLngEntity(
                    location.latitude,
                    location.longitude
                )
            )
            removeLocationListener()
        }
    }
}