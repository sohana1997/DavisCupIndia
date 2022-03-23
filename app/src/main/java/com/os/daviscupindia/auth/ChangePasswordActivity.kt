package com.os.daviscupindia.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.os.daviscupindia.R
import com.os.daviscupindia.databinding.ActivityChangePasswordBinding
import com.os.daviscupindia.databinding.ActivityForgotPasswordBinding
import com.os.daviscupindia.ui.home.model.CustomerLoginRequest
import com.os.daviscupindia.utils.ProgressDialog
import com.os.daviscupindia.utils.handleApiError
import kotlinx.coroutines.launch

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private var authViewModel: AuthViewModel? = null
    private var state: Boolean = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun init() {
        authViewModel = AuthViewModel()
        binding?.bar.apply {
            title.text = "Change Password"
            back.setOnClickListener { onBackPressed() }

        }
        binding?.apply {
            val OTP = binding.firstPinView.text.toString().trim()

            loginBt.setOnClickListener {
                ProgressDialog.showProgressDialog(this@ChangePasswordActivity)
                val OTP = binding.firstPinView.text.toString().trim()
                if (newPassword.text.toString().length > 5 && OTP.length > 3) {
                    authViewModel?.viewModelScope?.launch {
                        authViewModel?.updatePasswordWithOtp(CustomerLoginRequest(password = newPassword.text.toString().trim(),otp = OTP))

                    }
                } else {
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Passwords must be at least 6 && Please enter valid otp",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        }


        authViewModel?.updatePasswordWithOtp?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    200 -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        ProgressDialog.hideProgressDialog()

                        startActivity(Intent(this, LoingActivity::class.java))


                    }
                    else -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        ProgressDialog.hideProgressDialog()
                    }
                }
            }
        })
        authViewModel?.error?.observe(this, Observer {
            handleApiError(it)
            ProgressDialog.hideProgressDialog()
        })

    }
}