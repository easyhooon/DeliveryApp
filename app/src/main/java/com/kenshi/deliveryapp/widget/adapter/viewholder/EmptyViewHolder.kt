package com.kenshi.deliveryapp.widget.adapter.viewholder

import com.kenshi.deliveryapp.databinding.ViewholderEmptyBinding
import com.kenshi.deliveryapp.model.Model
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    private val binding: ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourcesProvider:ResourcesProvider
): ModelViewHolder<Model>(binding, viewModel, resourcesProvider) {
    override fun reset() = Unit

    override fun bindViews(model: Model, adapterListener: AdapterListener) = Unit
}