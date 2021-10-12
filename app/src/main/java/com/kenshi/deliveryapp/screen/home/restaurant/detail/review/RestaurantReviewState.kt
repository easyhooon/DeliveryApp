package com.kenshi.deliveryapp.screen.home.restaurant.detail.review

import com.kenshi.deliveryapp.model.restaurant.review.RestaurantReviewModel

sealed class RestaurantReviewState {

    object Uninitialized: RestaurantReviewState()

    object Loading: RestaurantReviewState()

    data class Success(
        val restaurantReviewList: List<RestaurantReviewModel>
    ): RestaurantReviewState()
}
