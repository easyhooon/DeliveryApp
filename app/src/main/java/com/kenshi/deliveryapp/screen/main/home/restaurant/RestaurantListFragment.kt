package com.kenshi.deliveryapp.screen.main.home.restaurant

import android.util.Log
import androidx.core.os.bundleOf
import com.kenshi.deliveryapp.data.entity.location.LocationLatLngEntity
import com.kenshi.deliveryapp.databinding.FragmentRestaurantListBinding
import com.kenshi.deliveryapp.model.restaurant.RestaurantModel
import com.kenshi.deliveryapp.screen.base.BaseFragment
import com.kenshi.deliveryapp.screen.main.home.restaurant.detail.RestaurantDetailActivity
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.kenshi.deliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

//뷰페이저 안에 들어갈 프래그먼트
//parameter 가 아니라 제너릭임
class RestaurantListFragment: BaseFragment<RestaurantListViewModel, FragmentRestaurantListBinding>() {
//    override val viewModel: RestaurantListViewModel
//        get() = TODO("Not yet implemented")

    private val restaurantCategory by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory}
    private val locationLatLng by lazy { arguments?.getParcelable<LocationLatLngEntity>(LOCATION_KEY) }

    //viewModel 호출 시에 생성이 되기 위해서 파라미터로 호출할 때 넣어줌
    override val viewModel by viewModel<RestaurantListViewModel>{
        parametersOf(
            restaurantCategory,
            locationLatLng
        )
    }

    override fun getViewBinding(): FragmentRestaurantListBinding
        = FragmentRestaurantListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object: RestaurantListListener{
                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(
                            requireContext(),
                            model.toEntity()
                        )
                    )
                }
            })
    }

    override fun initViews() = with(binding) {
        recyclerVIew.adapter = adapter
    }

    //viewModel 구독처리
    override fun observeData() {
        viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
            Log.e("restaurantList", it.toString())
            //recyclerView 의 어댑터 구성
            adapter.submitList(it)
        }
    }

    companion object {
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"
        const val LOCATION_KEY = "location"
        const val RESTAURANT_KEY = "Restaurant"

        fun newInstance(
            restaurantCategory: RestaurantCategory,
            locationLatLngEntity: LocationLatLngEntity
        ): RestaurantListFragment {
            return RestaurantListFragment().apply {
                //pair 로 담아서 처리할 수 있도록 만듬
                arguments = bundleOf(
                    RESTAURANT_CATEGORY_KEY to restaurantCategory,
                    //key 추가
                    LOCATION_KEY to locationLatLngEntity
                )
            }
        }
    }
}