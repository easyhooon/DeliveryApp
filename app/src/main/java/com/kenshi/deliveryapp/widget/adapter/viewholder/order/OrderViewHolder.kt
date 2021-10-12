package com.kenshi.deliveryapp.widget.adapter.viewholder.order

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.databinding.ViewholderFoodMenuBinding
import com.kenshi.deliveryapp.databinding.ViewholderOrderBinding
import com.kenshi.deliveryapp.model.order.OrderModel
import com.kenshi.deliveryapp.model.restaurant.food.FoodModel
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener
import com.kenshi.deliveryapp.widget.adapter.listener.order.OrderListListener
import com.kenshi.deliveryapp.widget.adapter.listener.order.OrderMenuListListener
import com.kenshi.deliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import com.kenshi.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider,
): ModelViewHolder<OrderModel>(binding, viewModel, resourcesProvider) {

    //binding 쪽에서 직접 접근해서 초기화
    override fun reset() = Unit

    override fun bindData(model: OrderModel) {
        super.bindData(model)
        with(binding) {
            orderTitleText.text = resourcesProvider.getString(R.string.order_history_title, model.orderId)

            val foodMenuList = model.foodMenuList

            foodMenuList
                    //같은 메뉴이면 하나로 합침
                .groupBy { it.title }
                .entries.forEach { (title, menuList) ->
                    val orderDataStr =
                        orderContentText.text.toString() + "메뉴 : $title | 가격 : ${menuList.first().price}원 X ${menuList.size}\n"
                    orderContentText.text = orderDataStr
                }
            //trim() 으로 enter 처리 빼줌 (문자열 앞 뒤 공백 제거)
            orderContentText.text = orderContentText.text.trim()

            orderTotalPriceText.text =
                resourcesProvider.getString(
                    R.string.price,
                    foodMenuList.map{ it.price }.reduce { total, price -> total + price}
                )
        }
    }

    //interface 를 구현하는 로직 복습
    override fun bindViews(model: OrderModel, adapterListener: AdapterListener) {
        if (adapterListener is OrderListListener) {
            binding.root.setOnClickListener {
                adapterListener.writeRestaurantReview(model.orderId, model.restaurantTitle)
            }
        }
    }
}