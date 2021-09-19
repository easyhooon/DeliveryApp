package com.kenshi.deliveryapp.widget.adapter.listener.restaurant

import com.kenshi.deliveryapp.model.restaurant.RestaurantModel
import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener

//이 인터페이스를 갖고 아이템을 클릭 했었을 때
//어댑터의 뷰홀더에서 호출할수있도록
interface RestaurantListListener: AdapterListener {

    fun onClickItem(model: RestaurantModel)
}