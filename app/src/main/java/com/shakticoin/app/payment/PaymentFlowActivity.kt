package com.shakticoin.app.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.shakticoin.app.databinding.ActivityPaymentFlowBinding
import com.shakticoin.app.util.CommonUtil

class PaymentFlowActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentFlowBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hostedPage = intent.getStringExtra(CommonUtil.prefixed("targetUrl", this))

        binding.webView.settings.javaScriptEnabled = true
        val activity = this
        binding.webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // The URL would be like:
                // https://stg.shakticoin.com/subscriptions/success?hostedpage_id=2-bcb3b7aa956...
                // So, we just wait for subscriptions/success and go to wallet
                if (url != null && "subscriptions/success" in url) {
                    NavUtils.navigateUpFromSameTask(activity)
                }
            }

        }
        binding.webView.loadUrl(hostedPage)
    }
}