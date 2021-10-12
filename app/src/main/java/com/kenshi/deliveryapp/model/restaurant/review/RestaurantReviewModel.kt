package com.kenshi.deliveryapp.model.restaurant.review

import android.net.Uri
import com.kenshi.deliveryapp.model.CellType
import com.kenshi.deliveryapp.model.Model

data class RestaurantReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description: String,
    val grade: Float,
    val thumbnailImageUri: Uri? = null
): Model(id, type)
