package com.obvious.nasaapod.detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.obvious.nasaapod.databinding.FragmentImageDetailBinding
import com.obvious.nasaapod.home.data.ImageDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class DetailFragment : Fragment() {

    private var _detailLayoutBinding: FragmentImageDetailBinding? = null
    private val detailLayoutBinding get() = _detailLayoutBinding!!
    private var imageList: List<ImageDto> = listOf()

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private val onPageChangeListener = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            detailLayoutBinding.toolbar.title = imageList[position].title ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _detailLayoutBinding = FragmentImageDetailBinding.inflate(inflater, container, false)
        setupToolbar()
        return detailLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageList = arguments?.getParcelableArrayList(BUNDLE_IMAGE_LIST) ?: listOf()
        val selectedPosition = arguments?.getInt(BUNDLE_POSITION) ?: 0

        imageList.forEach {
            groupAdapter.add(ImageDetailItem(it, requireContext()))
        }

        detailLayoutBinding.viewpager.apply {
            adapter = groupAdapter
            offscreenPageLimit = 2
            registerOnPageChangeCallback(onPageChangeListener)
        }

        detailLayoutBinding.viewpager.setCurrentItem(selectedPosition, false)
    }

    private fun setupToolbar() {
        detailLayoutBinding.toolbar.apply {
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(imageList: List<ImageDto>, selectedPosition: Int): DetailFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList(BUNDLE_IMAGE_LIST, ArrayList(imageList))
            bundle.putInt(BUNDLE_POSITION, selectedPosition)
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }

        const val BUNDLE_IMAGE_LIST = "image_list"
        const val BUNDLE_POSITION = "position"
    }
}