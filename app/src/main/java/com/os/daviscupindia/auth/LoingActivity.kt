package com.os.daviscupindia.auth

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.os.daviscupindia.R
import com.os.daviscupindia.auth.SignupActivity.Companion.checkEmail
import com.os.daviscupindia.databinding.ActivityLoingBinding
import com.os.daviscupindia.ui.home.MainActivity
import com.os.daviscupindia.ui.home.model.CustomerLoginRequest
import com.os.daviscupindia.utils.Constants
import com.os.daviscupindia.utils.MyApplication
import com.os.daviscupindia.utils.ProgressDialog
import com.os.daviscupindia.utils.handleApiError
import kotlinx.coroutines.launch


class LoingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoingBinding
    private var authViewModel: AuthViewModel? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loing)
        binding = ActivityLoingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init() {
        authViewModel = AuthViewModel()
        binding?.apply {
            loginBt.setOnClickListener {
                if (checkEmail(editTextTextEmailAddress.text.toString())) {
                    authViewModel?.viewModelScope?.launch {
                        authViewModel?.customerLoginRequest(
                            CustomerLoginRequest(
                                email = editTextTextEmailAddress.text.toString().trim(),
                                password = editTextTextPassword.text.toString()
                            )
                        )
                    }
                    ProgressDialog.showProgressDialog(this@LoingActivity)
                }

            }
            donTHave.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoingActivity,
                        SignupActivity::class.java
                    )
                )
            }
            forgotPass.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoingActivity,
                        ForgotPasswordActivity::class.java
                    )
                )
            }
            val mainString = binding.donTHave.text
            val subString = "Sign Up"

            if (mainString.contains(subString)) {
                val startIndex = mainString.indexOf(subString)
                val endIndex = startIndex + subString.length
                val spannableString = SpannableString(mainString)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#05CBFA")), startIndex, endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.donTHave.text = spannableString
            }
        }

        authViewModel?.customerLoginResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {

                    200 -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        MyApplication.tinyDB.putBoolean(
                            Constants.LOGGGED_IN,
                            true
                        )
                        finish()
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
