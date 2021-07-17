package com.citor.app.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.citor.app.databinding.ActivityTermsConditionBinding

class TermsConditionActivity : AppCompatActivity() {

    private lateinit var termsConditionBinding: ActivityTermsConditionBinding
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        termsConditionBinding = ActivityTermsConditionBinding.inflate(layoutInflater)
        setContentView(termsConditionBinding.root)

        webView = termsConditionBinding.webviewTermsCondition
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        webView.loadUrl("https://test.icaapps.online/auth/termscondition")
    }
}