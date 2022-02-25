package com.obvious.nasaapod.detail.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.obvious.nasaapod.R
import com.obvious.nasaapod.databinding.ItemImageDetailBinding
import com.obvious.nasaapod.home.data.ImageDto
import com.xwray.groupie.viewbinding.BindableItem

class ImageDetailItem(
    private val imageData: ImageDto?,
    private val context: Context
) :
    BindableItem<ItemImageDetailBinding>() {

    override fun getLayout(): Int {
        return R.layout.item_image_detail
    }

    override fun bind(viewBinding: ItemImageDetailBinding, position: Int) {
        val imageUrl = imageData?.url

        Glide.with(context)
            .load(imageUrl)
            .placeholder(ColorDrawable(Color.GRAY))
            .downsample(DownsampleStrategy.CENTER_INSIDE)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(viewBinding.imageView)
    }

    override fun initializeViewBinding(view: View): ItemImageDetailBinding {
        return ItemImageDetailBinding.bind(view)
    }
}