package com.kenshi.deliveryapp.widget.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kenshi.deliveryapp.model.Model
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener

//Model 을 generic 으로 받고 (<> 내부)
abstract class ModelViewHolder<M: Model>(
    binding: ViewBinding,
    private val viewModel: BaseViewModel,
    private val resourcesProvider: ResourcesProvider
): RecyclerView.ViewHolder(binding.root) {

    abstract fun reset()

    open fun bindData(model: M) {
        //bindData 시점에 그 이전에 있었던 데이터가 초기화가 필요한 경우가 존재
        reset()
    }

    abstract fun bindViews(model: M, adapterListener: AdapterListener)

}