package com.kenshi.deliveryapp.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

//model recycler adapter 에서 쓰일 수 있는 Model 의 초기 상태
abstract class Model(
    open val id: Long,
    open val type: CellType
) {
    //diff Util 로 처리해서 쉽게 데이터를 비교
    //id 가 같으면서, 내용도 같은지
    //Model 을 제네릭으로 받아서 object 로 interface 를 넘겨줌
    //구현체로 구현
    companion object{
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Model> = object: DiffUtil.ItemCallback<Model>(){
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                //id가 같아도 여러화면에서 쓰인 경우 다른 데이터를 가지고 있을 수 있기 때문에
                return oldItem.id == newItem.id && oldItem.type == newItem.type
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                //같은 내용, 같은 타입인지 까지 비교
                return oldItem === newItem
            }
        }
    }
}