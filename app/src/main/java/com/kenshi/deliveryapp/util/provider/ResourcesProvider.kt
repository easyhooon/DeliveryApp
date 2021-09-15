package com.kenshi.deliveryapp.util.provider

import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface ResourcesProvider {
    //resId -> resourceId's short

    fun getString(@StringRes resId: Int): String

    //String 값 안에 여러개의 인자가 들어갈 수 있는 경우를 체크
    fun getString(@StringRes resId: Int, vararg formArgs: Any): String

    fun getColor(@ColorRes resId: Int): Int

    fun getColorStateList(@ColorRes resId: Int): ColorStateList


}