package com.kenshi.deliveryapp.screen.home.restaurant.detail

import android.app.AlertDialog
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantEntity
import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.kenshi.deliveryapp.databinding.ActivityRestaurantDetailBinding
import com.kenshi.deliveryapp.extensions.fromDpToPx
import com.kenshi.deliveryapp.extensions.load
import com.kenshi.deliveryapp.screen.base.BaseActivity
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantListFragment
import com.kenshi.deliveryapp.screen.home.restaurant.detail.menu.RestaurantMenuListFragment
import com.kenshi.deliveryapp.screen.home.restaurant.detail.review.RestaurantReviewListFragment
import com.kenshi.deliveryapp.screen.main.MainTabMenu
import com.kenshi.deliveryapp.screen.order.OrderMenuListActivity
import com.kenshi.deliveryapp.util.event.MenuChangeEventBus
import com.kenshi.deliveryapp.widget.adapter.RestaurantDetailListFragmentPagerAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

//RestaurantDetailViewModel 을 구독하고 있는 Activity
class RestaurantDetailActivity : BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) = Intent(context, RestaurantDetailActivity::class.java).apply{
            putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
        }
    }

    override fun getViewBinding(): ActivityRestaurantDetailBinding
            = ActivityRestaurantDetailBinding.inflate(layoutInflater)

//    override val viewModel: RestaurantDetailViewModel
//        get() = TODO("Not yet implemented")
    //import 3번째
    //파라미터를 넘겨받음
    override val viewModel by viewModel<RestaurantDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    //구독
    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun initViews() {
        initAppBar()
    }

    private lateinit var viewPagerAdapter: RestaurantDetailListFragmentPagerAdapter

    private fun initAppBar() = with(binding) {
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener{ appBarLayout, verticalOffset ->
            val topPadding = 300f.fromDpToPx().toFloat()
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = kotlin.math.abs(verticalOffset)

            //스크롤 했을 때 300이상을 스크롤했을 경우
            val realAlphaVerticalOffset: Float = if (abstractOffset - topPadding <= 0) 0f else abstractOffset - topPadding

            if(realAlphaScrollHeight < topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }

            //전체 길이를 움직인 값으로 나눠 비율을 계산
            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            restaurantTitleTextView.alpha = 1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })
        toolbar.setNavigationOnClickListener {
            finish()
        }
        callButton.setOnClickListener {
            viewModel.getRestaurantTelNumber()?.let { telNumber ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNumber"))
                startActivity(intent)
            }
        }
        likeButton.setOnClickListener {
            viewModel.toggleLikedRestaurant()
        }
        shareButton.setOnClickListener {
            //공유 기능
            //외부의 다른 어플(Third Party Application)에 ACTION_SENT 라는 것을 이용해서 Data 를 보냄
            //restaurantInfo 객체를 가져와서 Intent 에 담아 보냄
            viewModel.getRestaurantInfo()?.let { restaurantInfo ->
                val intent = Intent(Intent.ACTION_SEND).apply{
                    //ClipDescription
                    type = MIMETYPE_TEXT_PLAIN
                    putExtra(
                        //텍스트 파일의 형식으로
                        Intent.EXTRA_TEXT,
                        "맛있는 음식점 : ${restaurantInfo.restaurantTitle}" +
                                "\n평점 : ${restaurantInfo.grade}" +
                                "\n연락처 : ${restaurantInfo.restaurantTelNumber}"
                    )
                    //ActionChooser 액션을 담음
                    //target 은 해당 intent
                    Intent.createChooser(this, "친구에게 공유하기")
                }
                startActivity(intent)
            }
        }

    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when(it) {
            is RestaurantDetailState.Loading -> {
                handleLoading()
            }

            is RestaurantDetailState.Success -> {
                handleSuccess(it)
            }
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
        progressBar.isGone = true

        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTelNumber == null

        restaurantTitleTextView.text = restaurantEntity.restaurantTitle

        //extension function
        restaurantImage.load(restaurantEntity.restaurantImageUrl)

        restaurantMainTitleTextView.text = restaurantEntity.restaurantTitle

        ratingBar.rating = restaurantEntity.grade

        deliveryTimeText.text =
            getString(
                R.string.delivery_expected_time,
                restaurantEntity.deliveryTimeRange.first,
                restaurantEntity.deliveryTimeRange.second
            )
        deliveryTipText.text =
            getString(
                R.string.delivery_tip_range,
                restaurantEntity.deliveryTipRange.first,
                restaurantEntity.deliveryTipRange.second
            )

        //찜 기능
        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this@RestaurantDetailActivity, if (state.isLiked == true) {
                    R.drawable.ic_heart_enable
                } else {
                    R.drawable.ic_heart_disable
                }
            ),
            null, null, null
        )

        if(::viewPagerAdapter.isInitialized.not()) {
            initViewPager(state.restaurantEntity.restaurantInfoId, state.restaurantEntity.restaurantTitle, state.restaurantFoodList)
        }

        notifyBasketCount(state.foodMenuListInBasket)

        val (isClearNeed, afterAction) = state.isClearNeedInBasketAndAction
        if(isClearNeed) {
            alertClearNeedInBasket(afterAction)
        }
    }

    //viewPager 에 pageAdapter 객체를 넣어줌
    //viewPager 는 restaurantDetailList 의 FragmentPageAdapter 용도
    private fun initViewPager(
        restaurantInfoId: Long,
        restaurantTitle: String,
        restaurantFoodList: List<RestaurantFoodEntity>?
    ) {
        viewPagerAdapter = RestaurantDetailListFragmentPagerAdapter(
            this,
            listOf(
                RestaurantMenuListFragment.newInstance(
                    restaurantInfoId,
                    ArrayList(restaurantFoodList ?: listOf())
                ),
                RestaurantReviewListFragment.newInstance(
                    restaurantTitle
                )
            )

        )
        binding.menuAndReviewViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.menuAndReviewTabLayout, binding.menuAndReviewViewPager) { tab, position ->
            tab.setText(RestaurantCategoryDetail.values()[position].categoryNameId)
        }.attach() //<- 빼먹는 거 주의

    }

    private fun notifyBasketCount(foodMenuListInBasket: List<RestaurantFoodEntity>?) = with(binding) {
        basketCountTextView.text = if (foodMenuListInBasket.isNullOrEmpty()) {
            "0"
        } else {
            getString(R.string.basket_count, foodMenuListInBasket.size)
        }
        basketButton.setOnClickListener {
            if (firebaseAuth.currentUser == null) {
                alertLoginNeed {
                    //flow 를 이용하여 어디서든지 이용가능하도록
                    //sharedFlow 이용
                    //상세화면을 종료 시키고 myTab 으로 이동할 수 있도록 값을 넘겨준 것을 받고
                    //MainActivity 에서 구독을 하고 있다가 Tab 을 바꿔줄 수 있는 로직

                    //코루틴 블럭 생성
                    lifecycleScope.launch {
                        menuChangeEventBus.changeMenu(MainTabMenu.MY)
                        finish()
                    }
                }
            } else {
                startActivity(
                    OrderMenuListActivity.newIntent(this@RestaurantDetailActivity)
                )
            }
        }
    }

    //인자로 lambda function 을 넘겨줌
    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
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


    private fun alertClearNeedInBasket(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("장바구니에는 같은 가게의 메뉴만 담을 수 있습니다.")
            .setMessage("선택하신 메뉴를 장바구니에 담을 경우 이전에 담은 메뉴가 삭제 됩니다.")
            .setPositiveButton("담기") {dialog, _ ->
                //새로운 식당 메뉴가 담김
                //이전에 남겼던 메뉴들은 다 지워짐
                viewModel.notifyClearBasket()
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}