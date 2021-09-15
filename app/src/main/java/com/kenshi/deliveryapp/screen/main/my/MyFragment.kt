package com.kenshi.deliveryapp.screen.main.my

import com.kenshi.deliveryapp.databinding.FragmentHomeBinding
import com.kenshi.deliveryapp.databinding.FragmentMyBinding
import com.kenshi.deliveryapp.screen.base.BaseFragment
import com.kenshi.deliveryapp.screen.main.home.HomeFragment
import com.kenshi.deliveryapp.screen.main.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MyFragment: BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {

        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"
    }

}