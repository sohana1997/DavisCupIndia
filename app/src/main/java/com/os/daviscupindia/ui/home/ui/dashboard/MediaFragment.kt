package com.os.daviscupindia.ui.home.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.os.daviscupindia.WebViewActivity
import com.os.daviscupindia.databinding.FragmentMediaBinding
import com.os.daviscupindia.ui.home.MainViewModel
import com.os.daviscupindia.ui.home.model.GalleryData

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
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

        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        val root: View = binding.root


            // onPageFinished and override Url loading.
            binding.media.apply {
              l1.setOnClickListener { startActivity(Intent(requireActivity() , WebViewActivity::class.java).putExtra("url",l1.text.toString()).putExtra("title","")) }
              l2.setOnClickListener { startActivity(Intent(requireActivity() , WebViewActivity::class.java).putExtra("url",l2.text.toString()).putExtra("title","")) }
              l3.setOnClickListener { startActivity(Intent(requireActivity() , WebViewActivity::class.java).putExtra("url",l3.text.toString()).putExtra("title","")) }

            }
        return root
    }

    /** Called when the user taps the Send button  */




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}