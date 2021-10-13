package com.kenshi.deliveryapp.widget.adapter.viewholder.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.kenshi.deliveryapp.databinding.ViewholderRestaurantReviewBinding
import com.kenshi.deliveryapp.extensions.clear
import com.kenshi.deliveryapp.extensions.load
import com.kenshi.deliveryapp.model.restaurant.review.RestaurantReviewModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener
import com.kenshi.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class RestaurantReviewViewHolder(
    private val binding: ViewholderRestaurantReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantReviewModel>(binding, viewModel, resourcesProvider) {
    override fun reset() = with(binding) {
        reviewThumbnailImage.clear()
        //리뷰같은 경우 thumbnail 이미지가 없는 경우가 존재하기 때문에 default view gone
        reviewThumbnailImage.isGone = true
    }

    override fun bindData(model: RestaurantReviewModel) {
        super.bindData(model)
        with(binding) {
            if(model.thumbnailImageUri != null) {
                reviewThumbnailImage.isVisible = true
                reviewThumbnailImage.load(model.thumbnailImageUri.toString())
            } else {
                reviewThumbnailImage.isGone = true
            }

            reviewTitleText.text = model.title
            reviewText.text = model.description

            ratingBar.rating = model.grade
        }
    }

    //따로 구성할 interface 가 없기 때문에
    override fun bindViews(model: RestaurantReviewModel, adapterListener: AdapterListener) = Unit

}