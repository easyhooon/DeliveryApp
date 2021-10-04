package com.kenshi.deliveryapp.data.entity.restaurant

import android.net.Uri
import com.kenshi.deliveryapp.data.entity.Entity

data class RestaurantReviewEntity(
    override val id: Long,
    val title: String,
    val description: String,
    val grade: Int,
    val images: List<Uri>? = null //image 는 꼭 넣지 않아도 됨
): Entity
