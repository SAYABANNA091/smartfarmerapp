package com.soni.activity

import android.graphics.Bitmap
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityWebViewBinding

class WebViewActivity : BaseActivity() {
    lateinit var binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //    setContentView(R.layout.activity_web_view)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    fun init() {
        val i = intent
        val name = i.getStringExtra("name")
        val url = i.getStringExtra("url")


        ivBack = findViewById(R.id.iv_back)
        ivRight = findViewById(R.id.iv_right)
        title = findViewById(R.id.tv_title)
        title.setText(name)
        ivRight.visibility = View.INVISIBLE

        ivBack.setOnClickListener { finish() }
        loadPage(url!!)
       // binding.txtContinue.visibility = View.GONE
    }
    private fun loadPage(url:String) {
        binding.webview.settings.loadWithOverviewMode = true
         binding.webview.settings.useWideViewPort = true

        //To upload data from
         binding.webview.settings.saveFormData = true
         binding.webview.settings.builtInZoomControls = true
         binding.webview.settings.displayZoomControls = false
         binding.webview.settings.pluginState = WebSettings.PluginState.ON
         binding.webview.settings.useWideViewPort = true
         binding.webview.settings.javaScriptEnabled = true
         binding.webview.settings.allowFileAccess = true
         binding.webview.settings.domStorageEnabled = true



         binding.webview.loadUrl(url)


         binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
             }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

            }

            override fun onPageFinished(view: WebView, url: String) {

            }

            override fun onLoadResource(view: WebView, url: String) {



            }
        }

    }
}
