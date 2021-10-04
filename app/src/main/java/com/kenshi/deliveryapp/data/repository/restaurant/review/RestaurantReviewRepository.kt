package com.kenshi.deliveryapp.data.repository.restaurant.review

import com.kenshi.deliveryapp.data.entity.restaurant.RestaurantReviewEntity

interface RestaurantReviewRepository {

    //mockApi 용으로 id 를 0~10 의 Int 값으로 설정하기 때문에 restaurantTitle 이 더 적절한 unique 값
    //interface 의 함수이므로 몸체가 존재하면 안된다.
    suspend fun getReviews(restaurantTitle:String): List<RestaurantReviewEntity>
}