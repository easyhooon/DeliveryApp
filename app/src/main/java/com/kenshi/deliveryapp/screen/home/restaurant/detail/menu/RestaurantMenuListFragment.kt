package com.kenshi.deliveryapp.screen.home.restaurant.detail.menu

import android.widget.Toast
import androidx.core.os.bundleOf
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.databinding.FragmentListBinding
import com.kenshi.deliveryapp.model.restaurant.food.FoodModel
import com.kenshi.deliveryapp.screen.base.BaseFragment
import com.kenshi.deliveryapp.screen.home.restaurant.detail.RestaurantDetailViewModel
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.kenshi.deliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantMenuListFragment: BaseFragment<RestaurantMenuListViewModel, FragmentListBinding>() {

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    private val restaurantId by lazy {
        arguments?.getLong(RESTAURANT_ID_KEY, -1)
    }

    private val restaurantFoodList by lazy {
        arguments?.getParcelableArrayList<RestaurantFoodEntity>(FOOD_LIST_KEY)!!
    }

    //Koin ViewModel 초기화
    override val viewModel by viewModel<RestaurantMenuListViewModel> {
        parametersOf(
            restaurantId,
            restaurantFoodList
        )
    }

    //상위 뷰모델에 접근하기 위해 주입 받음
    private val restaurantDetailViewModel by sharedViewModel<RestaurantDetailViewModel>()

    private val resourcesProvider by inject<ResourcesProvider>()

    //기존의 구현했던 adapter 재사용
    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : FoodMenuListListener {
                override fun onClickItem(model: FoodModel) {
                    //장바구니 담기
                    viewModel.insertMenuInBasket(model)
                }
            }
        )
    }

    override fun initViews() {
        binding.recyclerView.adapter = adapter
    }

    //ViewModel 구독 처리 (Activity 가 ViewModel 을 구독한다)
    override fun observeData() {
        viewModel.restaurantFoodListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        //여려개의 LiveData 를 구독
        viewModel.menuBasketLiveData.observe(viewLifecycleOwner) {
            //restaurantFoodEntity 를 넘겨주기 때문에 장바구니에 담겻다라는 것을 알려줌
            Toast.makeText(requireContext(),"장바구니에 담겼습니다. 메뉴 : ${it.title}", Toast.LENGTH_SHORT).show()
            //상위 뷰모델에 접근하기 위해
            restaurantDetailViewModel.notifyFoodMenuListInBasket(it)
        }
        //Pair 를 분리
        viewModel.isClearedNeedInBasketLiveData.observe(viewLifecycleOwner) { (isClearNeed, afterAction) ->
            if (isClearNeed) {
                restaurantDetailViewModel.notifyClearNeedAlertInBasket(isClearNeed, afterAction)
            }
        }

    }

    companion object{

        const val RESTAURANT_ID_KEY = "restaurantId"

        const val FOOD_LIST_KEY = "foodList"

        fun newInstance(restaurantId: Long, foodList: ArrayList<RestaurantFoodEntity>): RestaurantMenuListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                FOOD_LIST_KEY to foodList
            )
            return RestaurantMenuListFragment().apply {
                arguments = bundle
            }
        }
    }

}