package com.obvious.nasaapod.home.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.obvious.nasaapod.R
import com.obvious.nasaapod.databinding.ItemImageBinding
import com.obvious.nasaapod.home.data.ImageDto
import com.xwray.groupie.viewbinding.BindableItem

class ImageItem(
    private val context: Context,
    private val imageDto: ImageDto
) :
    BindableItem<ItemImageBinding>() {

    override fun getLayout() = R.layout.item_image

    override fun initializeViewBinding(view: View): ItemImageBinding {
        return ItemImageBinding.bind(view)
    }

    override fun bind(viewBinding: ItemImageBinding, position: Int) {
        Glide.with(context)
            .load(getImageUrl())
            .placeholder(ColorDrawable(Color.GRAY))
            .downsample(DownsampleStrategy.CENTER_INSIDE)
            .skipMemoryCache(true)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(viewBinding.titleImage)
    }

    /**
     * Span size of 2 to divide
     */
//    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2

    private fun getImageUrl(): String {
        return imageDto.url ?: ""
    }
}