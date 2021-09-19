package com.kenshi.deliveryapp.widget.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kenshi.deliveryapp.screen.home.restaurant.RestaurantListFragment

class RestaurantListFragmentPagerAdapter(
    fragment: Fragment,
    val fragmentList: List<RestaurantListFragment>
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}