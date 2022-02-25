package com.obvious.nasaapod.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.obvious.nasaapod.R
import com.obvious.nasaapod.databinding.FragmentHomeBinding
import com.obvious.nasaapod.detail.view.DetailFragment
import com.obvious.nasaapod.ext.readFile
import com.obvious.nasaapod.home.HomeViewModel
import com.obvious.nasaapod.home.HomeViewModelFactory
import com.obvious.nasaapod.home.data.ImageDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _homeLayoutBinding: FragmentHomeBinding? = null
    private val homeLayoutBinding get() = _homeLayoutBinding!!

    private var sortedImagesList: List<ImageDto> = mutableListOf()

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(Dispatchers.Default)
    }

    private val groupAdapter: GroupAdapter<GroupieViewHolder> = GroupAdapter()

    private val onItemClickListener: OnItemClickListener by lazy {
        OnItemClickListener { item, view ->
            if (item is ImageItem) {
                val position = groupAdapter.getAdapterPosition(item)

                if (position == RecyclerView.NO_POSITION) {
                    return@OnItemClickListener
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, DetailFragment.newInstance(sortedImagesList, position))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _homeLayoutBinding = FragmentHomeBinding.inflate(inflater, container, false)
        homeLayoutBinding.toolbar.title = resources.getString(R.string.home)
        return homeLayoutBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress()
        groupAdapter.apply {
            setOnItemClickListener(onItemClickListener)
            //spanCount = 2
        }

        homeLayoutBinding.recyclerView.apply {
//            layoutManager = GridLayoutManager(context, groupAdapter.spanCount).apply {
//                spanSizeLookup = groupAdapter.spanSizeLookup
//            }
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = groupAdapter
            setRecycledViewPool(RecyclerView.RecycledViewPool())
            setHasFixedSize(true)
        }

        lifecycleScope.launch(Dispatchers.Default) {
            val data = requireContext().assets.readFile("data.json")
            withContext(Dispatchers.Main) {
                homeViewModel.getImages(data).observe(viewLifecycleOwner, { imageList ->
                    hideProgress()
                    sortedImagesList = imageList ?: listOf()
                    groupAdapter.clear()
                    imageList?.forEach {
                        groupAdapter.add(ImageItem(requireContext(), it))
                    }
                })
            }
        }
    }

    private fun showProgress() {
        homeLayoutBinding.loadingProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        homeLayoutBinding.loadingProgress.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeLayoutBinding = null
    }
}