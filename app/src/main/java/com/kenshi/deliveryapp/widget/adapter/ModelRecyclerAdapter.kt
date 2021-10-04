package com.kenshi.deliveryapp.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kenshi.deliveryapp.model.CellType
import com.kenshi.deliveryapp.model.Model
import com.kenshi.deliveryapp.screen.base.BaseViewModel
import com.kenshi.deliveryapp.util.provider.ResourcesProvider
import com.kenshi.deliveryapp.util.mapper.ModelViewHolderMapper
import com.kenshi.deliveryapp.widget.adapter.listener.AdapterListener
import com.kenshi.deliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import com.kenshi.deliveryapp.widget.adapter.viewholder.ModelViewHolder


//제너릭
class ModelRecyclerAdapter<M: Model, VM: BaseViewModel>(
    private var modelList: List<Model>,
    private val viewModel: VM,
    //뷰홀더는 context 를 뷰바인딩을 통해 가져올 수 있으나, 좀 더 쉽게 리소스에 접근하기 위해
    //공통적으로 리소스에 접근가능하도록 resourceProvider 를 만들어줌
    private val resourcesProvider: ResourcesProvider,
    //뷰홀더 구현시에 필요한 adapterListener
    private val adapterListener: AdapterListener
) : ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
        //뷰홀더 wrapper 들을 구현해서 여러가지 viewHolder 들을 각 타입에 맞게 반환 하도록
        return ModelViewHolderMapper.map(parent, CellType.values()[viewType], viewModel, resourcesProvider)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder) {
            bindData(modelList[position] as M)
            //이벤트 처리
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun getItemCount(): Int = modelList.size

    //특정 아이템의 타입을 통해 아이템을 분류
    //타입을 순서(인덱스) 값으로 반환
    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun submitList(list: List<Model>?) {
        list?.let {
            modelList = it
        }
        super.submitList(list)
    }
}
