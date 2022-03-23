package com.os.daviscupindia.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.os.daviscupindia.databinding.ActivitySignupBinding
import com.os.daviscupindia.ui.home.model.RegisterRequest
import com.os.daviscupindia.utils.ProgressDialog
import com.os.daviscupindia.utils.handleApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var authViewModel: AuthViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = AuthViewModel()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.include.back.setOnClickListener { onBackPressed() }
        binding.donTHave.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LoingActivity::class.java
                )
            )
        }
        val mainString = binding.donTHave.text
        val subString = "Login"

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

        binding.loginBt.setOnClickListener {
            binding.apply {
                if (isValid()) {
                    authViewModel?.viewModelScope?.launch(Dispatchers.Main) {
                        authViewModel?.userRegister(
                            RegisterRequest(
                                email = etEmail.text.toString().trim(),
                                name = etName.text.toString().trim(),
                                mobile_number = etMobile.text.toString().trim(),
                                password = password.text.toString().trim()
                            )
                        )
                        ProgressDialog.showProgressDialog(this@SignupActivity)

                    }
                }
            }
        }

        authViewModel?.registerResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    200 -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        ProgressDialog.hideProgressDialog()

                        startActivity(
                            Intent(this, VerifyOtpActivity::class.java).putExtra(
                                "email",
                                it.data.email
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

    private fun isValid(): Boolean {
        binding.apply {
            when {
                etName.text.isNullOrEmpty() -> {
                    etName.error = "Please enter your name"
                    return false
                }
                !checkEmail(etEmail.text.toString()) -> {
                    etEmail.error = "Please enter your Email"
                    return false
                }
                password.text.isNullOrEmpty() && password.text.length < 6 -> {
                    password.error = "password should be at least 6"
                    return false
                }
                etMobile.text.isNullOrEmpty() -> {
                    etMobile.error = "Please enter your mobile number"
                    return false
                }

            }

        }
        return true
    }


    companion object {

        val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        fun checkEmail(email: String): Boolean {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
        }
    }


}