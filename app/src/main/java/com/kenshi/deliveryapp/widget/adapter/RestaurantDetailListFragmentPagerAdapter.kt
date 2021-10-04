package com.kenshi.deliveryapp.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//이 Adapter 는 restaurantListFragmentPagerAdapter 와 다르게 Fragment 에서 넘겨받지 않음 않음
class RestaurantDetailListFragmentPagerAdapter(
    activity: FragmentActivity,
    val fragmentList: List<Fragment>
): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}