package com.kenshi.deliveryapp.screen.main.home.restaurant.detail.review

import androidx.core.os.bundleOf
import com.kenshi.deliveryapp.databinding.FragmentListBinding
import com.kenshi.deliveryapp.model.restaurant.review.RestaurantReviewModel
import com.kenshi.deliveryapp.screen.base.BaseFragment
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

//Fragment 또한 LiveData 를 구독한다. observeData 를 통해
class RestaurantReviewListFragment: BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    //Koin ViewModel 초기화
    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantReviewListViewModel>{
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantReviewModel, RestaurantReviewListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            //당장에 쓰일 클릭 이벤트가 없으므로 BaseListener 로
            adapterListener = object: AdapterListener { }
        )
    }

    override fun initViews() {
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.reviewStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is RestaurantReviewState.Success -> {
                handleSuccess(it)
            }
        }
    }

    private fun handleSuccess(state: RestaurantReviewState.Success) {
//        Toast.makeText(requireContext(), state.reviewList.toString(), Toast.LENGTH_SHORT).show()
        adapter.submitList(state.restaurantReviewList)
    }

    companion object{

        const val RESTAURANT_TITLE_KEY = "restaurantTitle"

        fun newInstance(restaurantTitle: String): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_TITLE_KEY to restaurantTitle,
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}