package com.kenshi.deliveryapp.extensions

import android.content.res.Resources

//Float type 의 DP -> Px
fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}