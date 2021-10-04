package com.kenshi.deliveryapp.extensions

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

class ImageViewExtensions {
}


private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

//reset 이라고 하는 viewHolder 의 default 메소드에 이미지를 clear 하도록 부착 시킬 것
fun ImageView.clear() = Glide.with(context).clear(this)

//fade in, fade out 애니메이션을 구현하기 위한
fun ImageView.loadCenterInside(url: String, corner: Float = 0f,scaleType: Transformation<Bitmap> = CenterInside()) {
    Glide.with(this)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(factory))
            //캐시는 디스크에 관리하는 형태로
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .apply {
            //라운드 처리 용도
            if (corner > 0) transforms(CenterInside(), RoundedCorners(corner.fromDpToPx()))
        }
        .into(this)
}