package com.kenshi.deliveryapp.widget.adapter.viewholder.food

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.databinding.ViewholderFoodMenuBinding
import com.kenshi.deliveryapp.extensions.clear
import com.kenshi.deliveryapp.extensions.load
import com.kenshi.deliveryapp.model.restaurant.food.FoodModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener
import com.kenshi.deliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import com.kenshi.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class FoodMenuViewHolder(
    private val binding: ViewholderFoodMenuBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider,
): ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {

    //binding 쪽에서 직접 접근해서 초기화
    override fun reset() = with(binding) {
        foodImage.clear()
    }

    override fun bindData(model: FoodModel) {
        super.bindData(model)
        with(binding) {
            foodImage.load(model.imageUrl, 24f, CenterCrop())
            foodTitleText.text = model.title
            foodDescriptionText.text = model.description
            priceText.text = resourcesProvider.getString(R.string.price, model.price)
        }
    }

    //interface 를 구현하는 로직 복습
    override fun bindViews(model: FoodModel, adapterListener: AdapterListener) {
        if (adapterListener is FoodMenuListListener) {
            binding.root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }

}