package com.kindaboii.journal

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import java.io.File

class PrintActivity : ComponentActivity() {

    private var webView: WebView? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val htmlPath = intent.getStringExtra(EXTRA_HTML_PATH) ?: return finish()
        val docName = intent.getStringExtra(EXTRA_FILE_NAME) ?: "Дневник"

        val wv = WebView(this).also {
            webView = it
            setContentView(it)
        }

        wv.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val printManager = getSystemService(PRINT_SERVICE) as PrintManager
                val adapter = view.createPrintDocumentAdapter(docName)
                val printJob = printManager.print(
                    docName,
                    adapter,
                    PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .build(),
                )
                pollUntilDone(printJob)
            }
        }

        val html = File(htmlPath).readText(Charsets.UTF_8)
        wv.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }

    private fun pollUntilDone(printJob: PrintJob) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (printJob.isCompleted || printJob.isFailed || printJob.isCancelled) {
                    finish()
                } else {
                    handler.postDelayed(this, 500)
                }
            }
        }, 500)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        webView?.destroy()
        webView = null
    }

    companion object {
        const val EXTRA_HTML_PATH = "html_path"
        const val EXTRA_FILE_NAME = "file_name"
    }
}
