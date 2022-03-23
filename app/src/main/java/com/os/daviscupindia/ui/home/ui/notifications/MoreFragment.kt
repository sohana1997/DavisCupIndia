package com.os.daviscupindia.ui.home.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.os.daviscupindia.WebViewActivity
import com.os.daviscupindia.databinding.FragmentNotificationsBinding
import com.os.daviscupindia.utils.MyApplication

class MoreFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

       binding?.ss.tvEmail.text = MyApplication.tinyDB.getString("support_email")
       binding?.ss.tvNumber.text = MyApplication.tinyDB.getString("number")

        binding.ss.tc.setOnClickListener {
            startActivity(Intent(requireActivity() , WebViewActivity::class.java).putExtra("url",MyApplication.tinyDB.getString("tramAndConditions")).putExtra("title",""))

        }

        binding.ss.pp.setOnClickListener {
            startActivity(Intent(requireActivity() , WebViewActivity::class.java).putExtra("url",MyApplication.tinyDB.getString("privacyPolicy")).putExtra("title",""))

        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}