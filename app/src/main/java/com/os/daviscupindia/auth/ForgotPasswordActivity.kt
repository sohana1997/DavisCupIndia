package com.os.daviscupindia.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.os.daviscupindia.databinding.ActivityForgotPasswordBinding
import com.os.daviscupindia.auth.SignupActivity.Companion.checkEmail
import com.os.daviscupindia.ui.home.model.ForgotPassword
import com.os.daviscupindia.utils.ProgressDialog
import com.os.daviscupindia.utils.handleApiError
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private var authViewModel: AuthViewModel? = null
    private var state: Boolean = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun init() {
        authViewModel = AuthViewModel()
        binding?.bar.apply {
            title.text = "Forgot Password"
            back.setOnClickListener { onBackPressed() }

        }
        binding.apply {
            loginBt.setOnClickListener {
                if (checkEmail(newPassword.text.toString().trim())) {
                    authViewModel?.viewModelScope?.launch {
                        authViewModel?.forgotPassword(
                            ForgotPassword(
                                email = newPassword.text.toString().trim()
                            )
                        )
                    }
                    ProgressDialog.showProgressDialog(this@ForgotPasswordActivity)
                }else{
                    Toast.makeText(this@ForgotPasswordActivity, "Please enter your Email", Toast.LENGTH_SHORT).show()
                }
            }
        }


        authViewModel?.forgotPassword?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    200 -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        ProgressDialog.hideProgressDialog()

                        startActivity(
                            Intent(this, ChangePasswordActivity::class.java).putExtra(
                                "email",binding?.newPassword.text.toString().trim()

                            ).putExtra(
                                "isForgotPasswordActivity",
                                true
                            )
                        )


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