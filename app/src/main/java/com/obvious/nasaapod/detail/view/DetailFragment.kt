package com.obvious.nasaapod.detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.obvious.nasaapod.databinding.FragmentImageDetailBinding
import com.obvious.nasaapod.home.data.ImageDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class DetailFragment : Fragment() {

    private var _detailLayoutBinding: FragmentImageDetailBinding? = null
    private val detailLayoutBinding get() = _detailLayoutBinding!!

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _detailLayoutBinding = FragmentImageDetailBinding.inflate(inflater, container, false)
        return detailLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = arguments?.getParcelableArrayList<ImageDto>("s")
        val selectedPosition = arguments?.getInt("i") ?: 0

        imageList?.forEach {
            groupAdapter.add(ImageDetailItem(it, requireContext()))
        }

        detailLayoutBinding.viewpager.apply {
            adapter = groupAdapter
            offscreenPageLimit = 2
        }

        detailLayoutBinding.viewpager.setCurrentItem(selectedPosition, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(imageList: List<ImageDto>, selectedPosition: Int): DetailFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList("s", ArrayList(imageList))
            bundle.putInt("i", selectedPosition)
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}