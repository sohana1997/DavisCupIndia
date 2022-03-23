package com.os.daviscupindia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.os.daviscupindia.databinding.ActivityWebView2Binding
import com.os.daviscupindia.utils.ProgressDialog

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebView2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebView2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.include.back.setOnClickListener { onBackPressed() }
        binding.include.title.text = getString(R.string.app_name)
        ProgressDialog.showProgressDialog(this@WebViewActivity)


        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        binding.apply {
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, weburl: String) {
                  ProgressDialog.hideProgressDialog()
                }
            }
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {

                }
            }



            // this will load the url of the website
            intent.getStringExtra("url")?.let { webView.loadUrl(it) }

            // this will enable the javascript settings
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true

            // if you want to enable zoom feature
            webView.settings.setSupportZoom(false)
        }

    }
}