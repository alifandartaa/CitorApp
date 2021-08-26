package com.citor.app.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.citor.app.R
import com.citor.app.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var privacyPolicyBinding: ActivityPrivacyPolicyBinding
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        privacyPolicyBinding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(privacyPolicyBinding.root)

        webView = privacyPolicyBinding.webviewPrivacyPolicy
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        webView.loadUrl("https://citor.id/privacy_policy")
    }
}