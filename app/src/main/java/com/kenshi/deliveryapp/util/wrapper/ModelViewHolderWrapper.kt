package com.kenshi.deliveryapp.util.wrapper

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.kenshi.deliveryapp.databinding.ViewholderEmptyBinding
import com.kenshi.deliveryapp.model.CellType
import com.kenshi.deliveryapp.model.Model
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.widget.adapter.viewholder.EmptyViewHolder
import com.kenshi.deliveryapp.widget.adapter.viewholder.ModelViewHolder

object ModelViewHolderWrapper {

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
        }
        return viewHolder as ModelViewHolder<M>

    }
}