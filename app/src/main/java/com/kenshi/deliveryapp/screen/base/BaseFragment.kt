package com.kenshi.deliveryapp.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

//base 의 경우 되도록이면 abstract 하게
//base Activity 와 동일한 구조
abstract class BaseFragment<VM: BaseViewModel, VB: ViewBinding> : Fragment() {

    abstract val viewModel: VM

    protected lateinit var binding: VB

    private lateinit var fetchJob: Job

    abstract fun getViewBinding(): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
    }

    //상태 값을 초기화
    open fun initState() {
        arguments?.let {
            viewModel.storeState(it)
        }
        initViews()
        fetchJob = viewModel.fetchData()
        observeData()
    }

    open fun initViews() = Unit

    //abstract 함수는 몸체를 가지지 않는다
    abstract fun observeData()

    override fun onDestroy() {
        if(fetchJob.isActive){
            fetchJob.cancel()
        }
        super.onDestroy()
    }
}