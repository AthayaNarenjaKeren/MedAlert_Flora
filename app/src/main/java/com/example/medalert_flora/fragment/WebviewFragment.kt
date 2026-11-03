package com.example.medalert_flora.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.medalert_flora.MainActivity
import com.example.medalert_flora.R
import com.google.android.material.progressindicator.LinearProgressIndicator

// Layar Tips P3K: menampilkan halaman web di dalam aplikasi memakai WebView
class WebViewFragment : Fragment(R.layout.fragment_webview) {

    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wv = view.findViewById<WebView>(R.id.webView)
        val progress = view.findViewById<LinearProgressIndicator>(R.id.progressWeb)
        val tvError = view.findViewById<TextView>(R.id.tvError)
        webView = wv

        wv.settings.javaScriptEnabled = true

        // WebViewClient: link dibuka di dalam WebView, bukan di browser luar
        wv.webViewClient = object : WebViewClient() {
            override fun onPageStarted(v: WebView?, url: String?, favicon: Bitmap?) {
                progress.visibility = View.VISIBLE
                tvError.visibility = View.GONE
                wv.visibility = View.VISIBLE
            }

            override fun onPageFinished(v: WebView?, url: String?) {
                progress.visibility = View.GONE
            }

            override fun onReceivedError(
                v: WebView, request: WebResourceRequest, error: WebResourceError
            ) {
                if (request.isForMainFrame) {
                    wv.visibility = View.INVISIBLE
                    tvError.visibility = View.VISIBLE
                }
            }
        }

        // WebChromeClient: menggerakkan garis loading
        wv.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(v: WebView?, newProgress: Int) {
                progress.setProgressCompat(newProgress, true)
            }
        }

        wv.loadUrl(TIPS_URL)

        view.findViewById<View>(R.id.btnBack).setOnClickListener { goBackOrClose() }

        // Tombol Back sistem: mundur di halaman web dulu, baru keluar dari layar ini
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = goBackOrClose()
            }
        )
    }

    private fun goBackOrClose() {
        val wv = webView
        if (wv != null && wv.canGoBack()) wv.goBack()
        else (activity as MainActivity).closeOverlay()
    }

    override fun onDestroyView() {
        webView?.destroy()
        webView = null
        super.onDestroyView()
    }

    companion object {
        // Ganti alamat ini kalau dosen meminta halaman tertentu
        const val TIPS_URL = "https://id.wikipedia.org/wiki/Pertolongan_pertama"
    }
}
