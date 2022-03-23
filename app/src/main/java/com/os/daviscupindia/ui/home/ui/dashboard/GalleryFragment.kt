package com.os.daviscupindia.ui.home.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.os.daviscupindia.databinding.FragmentDashboardBinding
import com.os.daviscupindia.ui.home.MainViewModel
import com.os.daviscupindia.ui.home.adapter.RvGalleryAdapter
import com.os.daviscupindia.ui.home.model.GalleryData
import com.os.daviscupindia.utils.ProgressDialog
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class GalleryFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    var galleryData = ArrayList<GalleryData>()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        ProgressDialog.showProgressDialog(requireActivity())
        dashboardViewModel.homeDataModel.observe(viewLifecycleOwner , Observer { it ->
        galleryData = it.data.galleryData
            setGallery()
            ProgressDialog.hideProgressDialog()
        })
        return root
    }

    private fun setGallery() {
        val flexboxLayoutManager = FlexboxLayoutManager(requireActivity()).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.FLEX_START
        }
        binding?.recyclerview.apply {
            layoutManager = flexboxLayoutManager
            adapter = RvGalleryAdapter(galleryData, requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}