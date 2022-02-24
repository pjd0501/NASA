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
import com.obvious.nasaapod.databinding.FragmentHomeBinding
import com.obvious.nasaapod.ext.readFile
import com.obvious.nasaapod.home.HomeViewModel
import com.obvious.nasaapod.home.HomeViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeFragment : Fragment() {

    private var _homeLayoutBinding: FragmentHomeBinding? = null
    private val homeLayoutBinding get() = _homeLayoutBinding!!

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(Dispatchers.Default)
    }

    private val groupAdapter: GroupAdapter<GroupieViewHolder> = GroupAdapter()

    private val onItemClickListener: OnItemClickListener by lazy {
        OnItemClickListener { item, view ->
            if (item is ImageItem) {

                //TODO: Add navigation to detail fragment
            }
        }
    }

    init {
        Timber.plant(Timber.DebugTree())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _homeLayoutBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeLayoutBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        lifecycleScope.launch {
            val data = requireContext().assets.readFile("data.json")

            homeViewModel.getImages(data).observe(viewLifecycleOwner, { imageList ->
                imageList?.forEach {
                    groupAdapter.add(ImageItem(requireContext(), it))
                }
            })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeLayoutBinding = null
    }
}