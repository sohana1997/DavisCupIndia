package com.os.daviscupindia.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.os.daviscupindia.R
import com.os.daviscupindia.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    fun init(){
        binding?.apply {
            login.setOnClickListener { startActivity(Intent(this@WelcomeActivity , LoingActivity::class.java))
                finish()}
            donTHave.setOnClickListener { startActivity(Intent(this@WelcomeActivity , SignupActivity::class.java))
                finish()}

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
    }
}