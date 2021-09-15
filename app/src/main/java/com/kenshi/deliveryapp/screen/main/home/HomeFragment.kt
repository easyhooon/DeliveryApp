package com.kenshi.deliveryapp.screen.main.home

import android.os.Bundle
import com.kenshi.deliveryapp.databinding.FragmentHomeBinding
import com.kenshi.deliveryapp.screen.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {
//    override val viewModel: HomeViewModel
//        get() = TODO("Not yet implemented")

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }

}