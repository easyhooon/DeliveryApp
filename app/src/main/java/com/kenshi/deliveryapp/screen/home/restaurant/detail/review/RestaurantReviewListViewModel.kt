package com.kenshi.deliveryapp.screen.home.restaurant.detail.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kenshi.deliveryapp.data.entity.ReviewEntity
import com.kenshi.deliveryapp.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import com.kenshi.deliveryapp.data.repository.restaurant.review.RestaurantReviewRepository
import com.kenshi.deliveryapp.model.restaurant.review.RestaurantReviewModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantReviewRepository: RestaurantReviewRepository
): BaseViewModel() {

    val reviewStateLiveData = MutableLiveData<RestaurantReviewState>(RestaurantReviewState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        reviewStateLiveData.value = RestaurantReviewState.Loading

        //reviews 를 받아와서 state 패턴으로 표현
        val result = restaurantReviewRepository.getReviews(restaurantTitle)

        when(result) {
            is DefaultRestaurantReviewRepository.Result.Success<*> -> {
                val reviews = result.data as List<ReviewEntity>
                reviewStateLiveData.value = RestaurantReviewState.Success(
                    reviews.map{
                        RestaurantReviewModel (
                            id = it.hashCode().toLong(),
                            title = it.title,
                            description = it.content,
                            //0.5 점도 가능하기 때문에 Float
                            grade = it.rating,
                            thumbnailImageUri = if (it.imageUrlList.isNullOrEmpty()) {
                                null
                            } else {
                                //upload 한 첫번째 이미지를 가져옴
                                Uri.parse(it.imageUrlList.first())
                            }
                        )
                    }
                )
            }
            else -> Unit
        }
    }
}