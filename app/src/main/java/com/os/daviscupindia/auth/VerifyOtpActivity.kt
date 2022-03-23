package com.os.daviscupindia.auth

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.os.daviscupindia.R
import com.os.daviscupindia.databinding.ActivityVerifyOtpBinding
import com.os.daviscupindia.ui.home.MainActivity
import com.os.daviscupindia.ui.home.model.ResendOTPRequest
import com.os.daviscupindia.ui.home.model.VerifyRequest
import com.os.daviscupindia.utils.*
import kotlinx.coroutines.launch
const val COUNTDOWNTIMER = 60000

class VerifyOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding
    private var authViewModel: AuthViewModel? = null
    lateinit var countDown: CountDownTimer

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CountDownTimer()
        init()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init() {
        authViewModel = AuthViewModel()
        binding.apply {
            include2.title.text = "Verify Account"
            include2.back.setOnClickListener { onBackPressed() }
            loginBt.setOnClickListener {
                val OTP = binding.firstPinView.text.toString().trim()

                OTP.length.let {
                    if (it > 3) {
                        ProgressDialog.showProgressDialog(this@VerifyOtpActivity)
                        authViewModel?.viewModelScope?.launch {
                            intent.getStringExtra("email")?.let { it1 ->
                                VerifyRequest(
                                    email = it1,
                                    otp = firstPinView.text.toString()
                                )
                            }?.let { it2 -> authViewModel?.verifyRequest(it2) }
                        }

                    }
                }
            }

        }



        authViewModel?.verifyResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    200 -> {
                        ProgressDialog.showProgressDialog(this)
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        when(intent.hasExtra("isForgotPasswordActivity")){
                            true -> {
                                startActivity(Intent(this , ChangePasswordActivity::class.java).putExtra(
                                    "otp",binding.firstPinView.text.toString().trim()
                                ))

                            }
                                false -> {
                                    startActivity(Intent(this , MainActivity::class.java))
                                    MyApplication.tinyDB.putBoolean(
                                        Constants.LOGGGED_IN,
                                        true
                                    )
                                }
                        }
                        startActivity(Intent(this , MainActivity::class.java))
                        MyApplication.tinyDB.putBoolean(
                            Constants.LOGGGED_IN,
                            true
                        )
                        ProgressDialog.hideProgressDialog()
                        finish()

                    }
                    else -> Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.donTReceive.setOnClickListener {
            intent.getStringExtra("email")?.let {
                authViewModel?.viewModelScope?.launch {
                    authViewModel?.resendOTPRequest(ResendOTPRequest(email = it))
                    ProgressDialog.showProgressDialog(this@VerifyOtpActivity)

                }

            }
        }

        authViewModel?.resendOTPResponse?.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    200 -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        CountDownTimer()
                    }
                    else -> Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        authViewModel?.error?.observe(this, Observer {
            handleApiError(it)
            ProgressDialog.hideProgressDialog()
        })


    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun CountDownTimer() {
        countDown = object : CountDownTimer(COUNTDOWNTIMER.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {

                var count: Int = (millisUntilFinished / 1000).toInt()

                if (count == 0 || count == 1 || count == 2 || count == 3 || count == 4 || count == 5 || count == 6 || count == 7 || count == 8 || count == 9)
                    binding.secs.text = "${millisUntilFinished / 1000} sec"
                else
                    binding.secs.text = "${millisUntilFinished / 1000} sec"

                binding.donTReceive.isEnabled = false
                binding.donTReceive.setTextColor(Color.GRAY)

            }

            override fun onFinish() {
                binding.donTReceive.isEnabled = true
                binding.donTReceive.setTextColor(getColor(R.color.app_color))
            }
        }.start()
    }


}