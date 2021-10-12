package com.kenshi.deliveryapp.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kenshi.deliveryapp.databinding.*
import com.kenshi.deliveryapp.model.CellType
import com.kenshi.deliveryapp.model.Model
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.viewholder.EmptyViewHolder
import com.kenshi.deliveryapp.widget.adapter.viewholder.restaurant.LikeRestaurantViewHolder
import com.kenshi.deliveryapp.widget.adapter.viewholder.ModelViewHolder
import com.kenshi.deliveryapp.widget.adapter.viewholder.restaurant.RestaurantViewHolder
import com.kenshi.deliveryapp.widget.adapter.viewholder.food.FoodMenuViewHolder
import com.kenshi.deliveryapp.widget.adapter.viewholder.order.OrderMenuViewHolder
import com.kenshi.deliveryapp.widget.adapter.viewholder.order.OrderViewHolder
import com.kenshi.deliveryapp.widget.adapter.viewholder.review.RestaurantReviewViewHolder

//말 그대로 viewHolder 를 각 화면에 맞게 갈아 끼워주는 클래스
object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M: Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel:BaseViewModel,
        resourcesProvider: ResourcesProvider
    ) : ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.LIKE_RESTAURANT_CELL -> LikeRestaurantViewHolder(
                ViewholderLikeRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.REVIEW_CELL -> RestaurantReviewViewHolder(
                ViewholderRestaurantReviewBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_FOOD_CELL -> OrderMenuViewHolder(
                ViewholderOrderMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_CELL -> OrderViewHolder(
                ViewholderOrderBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }
        return viewHolder as ModelViewHolder<M>

    }
}